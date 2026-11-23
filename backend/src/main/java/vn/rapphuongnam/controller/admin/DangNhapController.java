package vn.rapphuongnam.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.rapphuongnam.service.NguoiDungHienTai;
import vn.rapphuongnam.util.MatKhauUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

//UC1.2 dang nhap trang quan ly: Admin / Nhan vien dang nhap bang ma nhan vien (vd NV001) hoac email.
//Sai 5 lan lien tiep thi tam khoa 5 phut (giong trang khach)
@Controller
@RequestMapping("/admin")
public class DangNhapController {
    public static final String HO_TEN = "hoTen";
    public static final String VAI_TRO = "vaiTro";
    public static final String MA_NV = "maNhanVien";
    private static final int SAI_TOI_DA = 5;
    private static final int PHUT_TAM_KHOA = 5;

    private final JdbcTemplate jdbc;

    public DangNhapController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/dang-nhap")
    public String form(@RequestParam(required = false) String tiep, HttpSession session, Model model) {
        if (session.getAttribute(NguoiDungHienTai.KHOA_SESSION) != null) return "redirect:" + diaChiTiep(tiep);
        model.addAttribute("tiep", tiep);
        return "admin/dang-nhap";
    }

    @PostMapping("/dang-nhap")
    @Transactional
    public String dangNhap(@RequestParam(defaultValue = "") String tenDangNhap, @RequestParam(defaultValue = "") String matKhau,
                           @RequestParam(required = false) String tiep, HttpServletRequest req, Model model) {
        model.addAttribute("tenDangNhap", tenDangNhap);
        model.addAttribute("tiep", tiep);
        String u = tenDangNhap.trim();
        if (u.isEmpty() || matKhau.isEmpty()) return loi(model, "Vui lòng nhập mã nhân viên và mật khẩu");

        List<Map<String, Object>> ds = jdbc.queryForList(
                "SELECT * FROM TaiKhoan WHERE (tenDangNhap = ? OR email = ?) AND loaiTaiKhoan IN ('admin', 'nhanVien')", u, u);
        if (ds.isEmpty()) return loi(model, "Mã nhân viên hoặc mật khẩu không đúng");
        Map<String, Object> tk = ds.get(0);
        int id = ((Number) tk.get("id")).intValue();

        if ("biKhoa".equals(tk.get("trangThai"))) return loi(model, "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên");
        if (tk.get("khoaDen") instanceof Timestamp t && t.toLocalDateTime().isAfter(LocalDateTime.now()))
            return loi(model, "Bạn đã nhập sai " + SAI_TOI_DA + " lần. Vui lòng thử lại sau "
                    + t.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        if (!MatKhauUtil.khop(matKhau, (String) tk.get("matKhau"))) {
            int sai = ((Number) tk.get("soLanNhapSai")).intValue() + 1;
            if (sai >= SAI_TOI_DA) {
                jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = 0, khoaDen = ? WHERE id = ?", LocalDateTime.now().plusMinutes(PHUT_TAM_KHOA), id);
                return loi(model, "Bạn đã nhập sai " + SAI_TOI_DA + " lần. Tài khoản tạm khóa " + PHUT_TAM_KHOA + " phút");
            }
            jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = ? WHERE id = ?", sai, id);
            return loi(model, "Mã nhân viên hoặc mật khẩu không đúng (còn " + (SAI_TOI_DA - sai) + " lần thử)");
        }

        jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = 0, khoaDen = NULL, lanDangNhapCuoi = ? WHERE id = ?", LocalDateTime.now(), id);
        //Tao session moi de tranh dung lai session cu
        HttpSession cu = req.getSession(false);
        if (cu != null) cu.invalidate();
        HttpSession s = req.getSession(true);
        s.setAttribute(NguoiDungHienTai.KHOA_SESSION, id);
        s.setAttribute(HO_TEN, tk.get("hoTen"));
        s.setAttribute(VAI_TRO, tk.get("loaiTaiKhoan"));
        s.setAttribute(MA_NV, tk.get("tenDangNhap"));
        //Nhan vien chi duoc tra cuu ve (UC6.2.2)
        if ("nhanVien".equals(tk.get("loaiTaiKhoan"))) return "redirect:/admin/don-hang/tra-cuu";
        return "redirect:" + diaChiTiep(tiep);
    }

    @PostMapping("/dang-xuat")
    public String dangXuat(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        return "redirect:/admin/dang-nhap?daDangXuat";
    }

    private String loi(Model model, String thongBao) {
        model.addAttribute("loiDangNhap", thongBao);
        return "admin/dang-nhap";
    }

    //Chi cho quay lai cac trang /admin, tranh chuyen huong sang trang ngoai
    private static String diaChiTiep(String tiep) {
        if (tiep != null && tiep.startsWith("/admin") && !tiep.startsWith("//") && !tiep.startsWith("/admin/dang-nhap")) return tiep;
        return "/admin";
    }
}
