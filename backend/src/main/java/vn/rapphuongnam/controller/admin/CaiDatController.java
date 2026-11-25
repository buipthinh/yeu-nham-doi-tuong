package vn.rapphuongnam.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.service.NguoiDungHienTai;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

//UC10 Cai dat he thong: UC10.1 chinh thong so rap (kem ngay le), UC10.2 sao luu du lieu
@Controller
@RequestMapping("/admin/cai-dat")
public class CaiDatController {
    private static final String[] TRUONG = {"tenRap", "diaChiRap", "hotline", "emailRap",
            "thoiGianGiuGhe", "soVeToiDaMoiDon", "thoiGianDonPhong", "nguongCanhBaoTonKho"};

    private final JdbcTemplate jdbc;
    private final NguoiDungHienTai nguoiDung;
    private final AtomicBoolean dangSaoLuu = new AtomicBoolean(false);

    //Thu muc luu file .bak tren may chay SQL Server. Bo trong = thu muc Backup mac dinh cua SQL Server
    @Value("${app.sao-luu.thu-muc:}")
    private String thuMucCauHinh;

    public CaiDatController(JdbcTemplate jdbc, NguoiDungHienTai nguoiDung) {
        this.jdbc = jdbc;
        this.nguoiDung = nguoiDung;
    }

    //================= UC10.1 Thong so rap =================
    @GetMapping
    public String thongSo(Model model) {
        Map<String, Object> cd = caiDat();
        Map<String, String> f = new LinkedHashMap<>();
        for (String t : TRUONG) f.put(t, cd.get(t) == null ? "" : cd.get(t).toString().trim());
        return trangThongSo(model, f, new HashMap<>(), cd);
    }

    @PostMapping
    public String luuThongSo(@RequestParam Map<String, String> p, Model model, RedirectAttributes ra) {
        Map<String, String> f = new LinkedHashMap<>();
        for (String t : TRUONG) f.put(t, p.getOrDefault(t, "").trim());
        Map<String, String> loi = new HashMap<>();

        if (f.get("tenRap").isEmpty()) loi.put("tenRap", "Không được để trống");
        else if (f.get("tenRap").length() > 100) loi.put("tenRap", "Tối đa 100 ký tự");
        if (f.get("diaChiRap").isEmpty()) loi.put("diaChiRap", "Không được để trống");
        else if (f.get("diaChiRap").length() > 255) loi.put("diaChiRap", "Tối đa 255 ký tự");
        if (!f.get("hotline").matches("0\\d{9}")) loi.put("hotline", "Gồm 10 chữ số, bắt đầu bằng 0");
        if (!f.get("emailRap").matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+") || f.get("emailRap").length() > 100)
            loi.put("emailRap", "Email không đúng định dạng");
        Integer giuGhe = soNguyen(f, "thoiGianGiuGhe", 1, 30, loi);
        Integer soVe = soNguyen(f, "soVeToiDaMoiDon", 1, 10, loi);
        Integer donPhong = soNguyen(f, "thoiGianDonPhong", 0, 60, loi);
        Integer nguong = soNguyen(f, "nguongCanhBaoTonKho", 0, 100000, loi);

        if (!loi.isEmpty()) {
            model.addAttribute("loiForm", "Thông số chưa hợp lệ, giá trị cũ vẫn được giữ. Xem các ô được đánh dấu bên dưới.");
            return trangThongSo(model, f, loi, caiDat());
        }

        int n = jdbc.update("UPDATE CaiDat SET tenRap = ?, diaChiRap = ?, hotline = ?, emailRap = ?, thoiGianGiuGhe = ?, "
                        + "soVeToiDaMoiDon = ?, thoiGianDonPhong = ?, nguongCanhBaoTonKho = ?, nguoiCapNhatId = ?, ngayCapNhat = GETDATE()",
                f.get("tenRap"), f.get("diaChiRap"), f.get("hotline"), f.get("emailRap"), giuGhe, soVe, donPhong, nguong, nguoiDung.layId());
        if (n == 0) {
            jdbc.update("INSERT INTO CaiDat (tenRap, diaChiRap, hotline, emailRap, thoiGianGiuGhe, soVeToiDaMoiDon, thoiGianDonPhong, "
                            + "nguongCanhBaoTonKho, nguoiCapNhatId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    f.get("tenRap"), f.get("diaChiRap"), f.get("hotline"), f.get("emailRap"), giuGhe, soVe, donPhong, nguong, nguoiDung.layId());
        }
        ra.addFlashAttribute("thongBao", "Đã lưu thông số. Thời gian giữ ghế mới chỉ áp dụng cho đơn tạo sau thời điểm này.");
        return "redirect:/admin/cai-dat";
    }

    @PostMapping("/ngay-le/them")
    public String themNgayLe(@RequestParam(defaultValue = "") String ngay, @RequestParam(defaultValue = "") String tenNgayLe,
                             RedirectAttributes ra) {
        LocalDate d;
        try {
            d = LocalDate.parse(ngay);
        } catch (DateTimeParseException e) {
            ra.addFlashAttribute("loi", "Vui lòng chọn ngày lễ.");
            return "redirect:/admin/cai-dat#ngay-le";
        }
        String ten = tenNgayLe.trim();
        if (ten.isEmpty() || ten.length() > 100) {
            ra.addFlashAttribute("loi", "Tên ngày lễ không được để trống (tối đa 100 ký tự).");
            return "redirect:/admin/cai-dat#ngay-le";
        }
        Integer trung = jdbc.queryForObject("SELECT COUNT(*) FROM NgayLe WHERE ngay = ?", Integer.class, d);
        if (trung != null && trung > 0) {
            ra.addFlashAttribute("loi", "Ngày " + d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " đã có trong danh sách ngày lễ.");
            return "redirect:/admin/cai-dat#ngay-le";
        }
        jdbc.update("INSERT INTO NgayLe (ngay, tenNgayLe) VALUES (?, ?)", d, ten);
        ra.addFlashAttribute("thongBao", "Đã thêm ngày lễ " + ten + ".");
        return "redirect:/admin/cai-dat#ngay-le";
    }

    @PostMapping("/ngay-le/{id}/xoa")
    public String xoaNgayLe(@PathVariable int id, RedirectAttributes ra) {
        jdbc.update("DELETE FROM NgayLe WHERE id = ?", id);
        ra.addFlashAttribute("thongBao", "Đã xóa ngày lễ.");
        return "redirect:/admin/cai-dat#ngay-le";
    }

    private String trangThongSo(Model model, Map<String, String> f, Map<String, String> loi, Map<String, Object> cd) {
        model.addAttribute("menu", "cai-dat");
        model.addAttribute("tab", "thong-so");
        model.addAttribute("f", f);
        model.addAttribute("loiTruong", loi);
        model.addAttribute("capNhatLuc", cd.get("capNhatLuc"));
        model.addAttribute("capNhatBoi", cd.get("capNhatBoi"));
        model.addAttribute("dsNgayLe", jdbc.queryForList("SELECT id, FORMAT(ngay, 'dd/MM/yyyy') AS ngay, tenNgayLe, "
                + "CASE WHEN ngay < CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END AS daQua FROM NgayLe ORDER BY NgayLe.ngay DESC"));
        return "admin/cai-dat/thong-so";
    }

    private Map<String, Object> caiDat() {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT TOP 1 c.*, FORMAT(c.ngayCapNhat, 'dd/MM/yyyy HH:mm') AS capNhatLuc, "
                + "tk.hoTen AS capNhatBoi FROM CaiDat c LEFT JOIN TaiKhoan tk ON tk.id = c.nguoiCapNhatId ORDER BY c.id");
        return ds.isEmpty() ? new HashMap<>() : ds.get(0);
    }

    private static Integer soNguyen(Map<String, String> f, String ten, int min, int max, Map<String, String> loi) {
        try {
            int v = Integer.parseInt(f.get(ten));
            if (v < min || v > max) {
                loi.put(ten, "Nhập số nguyên từ " + min + " đến " + max);
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            loi.put(ten, "Phải là số nguyên");
            return null;
        }
    }

    //================= UC10.2 Sao luu du lieu =================
    @GetMapping("/sao-luu")
    public String saoLuu(Model model) {
        model.addAttribute("menu", "cai-dat");
        model.addAttribute("tab", "sao-luu");
        model.addAttribute("thuMuc", thuMuc());
        model.addAttribute("dangChay", dangSaoLuu.get());
        model.addAttribute("dsSaoLuu", jdbc.queryForList("SELECT TOP 30 n.id, FORMAT(n.thoiDiem, 'dd/MM/yyyy HH:mm:ss') AS luc, "
                + "tk.hoTen, n.tenTep, n.dungLuong, n.trangThai FROM NhatKySaoLuu n "
                + "LEFT JOIN TaiKhoan tk ON tk.id = n.nguoiThucHienId ORDER BY n.thoiDiem DESC, n.id DESC"));
        return "admin/cai-dat/sao-luu";
    }

    @PostMapping("/sao-luu")
    public String saoLuuNgay(RedirectAttributes ra) {
        if (!dangSaoLuu.compareAndSet(false, true)) {
            ra.addFlashAttribute("loi", "Đang có một lần sao lưu khác chạy, vui lòng đợi xong rồi thử lại.");
            return "redirect:/admin/cai-dat/sao-luu";
        }
        String thuMuc = thuMuc();
        String duongDan = null;
        try {
            if (thuMuc == null || thuMuc.isBlank()) {
                ra.addFlashAttribute("loi", "Chưa xác định được thư mục sao lưu. Hãy khai báo app.sao-luu.thu-muc trong application.properties.");
                return "redirect:/admin/cai-dat/sao-luu";
            }
            String db = jdbc.queryForObject("SELECT DB_NAME()", String.class);
            String ten = db + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".bak";
            duongDan = thuMuc + (thuMuc.endsWith("\\") || thuMuc.endsWith("/") ? "" : "\\") + ten;

            //BACKUP khong chay duoc trong transaction -> goi truc tiep (auto-commit)
            jdbc.execute("BACKUP DATABASE [" + db.replace("]", "]]") + "] TO DISK = N'" + duongDan.replace("'", "''")
                    + "' WITH INIT, NAME = N'Sao luu tu trang quan ly'");

            Number size = null;
            try {
                size = jdbc.queryForObject("SELECT TOP 1 backup_size FROM msdb.dbo.backupset WHERE database_name = ? "
                        + "ORDER BY backup_finish_date DESC", Number.class, db);
            } catch (DataAccessException ignored) {
                //khong doc duoc msdb thi bo qua dung luong
            }
            jdbc.update("INSERT INTO NhatKySaoLuu (nguoiThucHienId, tenTep, dungLuong, trangThai) VALUES (?, ?, ?, 'thanhCong')",
                    nguoiDung.layId(), cat(duongDan), size == null ? 0 : size.longValue());
            ra.addFlashAttribute("thongBao", "Sao lưu thành công: " + duongDan);
        } catch (DataAccessException e) {
            String lyDo = e.getMostSpecificCause().getMessage();
            try {
                jdbc.update("INSERT INTO NhatKySaoLuu (nguoiThucHienId, tenTep, dungLuong, trangThai) VALUES (?, ?, 0, 'thatBai')",
                        nguoiDung.layId(), cat(duongDan == null ? "(không tạo được tệp)" : duongDan));
            } catch (DataAccessException ignored) {
                //ghi nhat ky that bai cung loi thi thoi
            }
            ra.addFlashAttribute("loi", "Sao lưu thất bại (thư mục không tồn tại, không có quyền ghi hoặc hết dung lượng): " + lyDo);
        } finally {
            dangSaoLuu.set(false);
        }
        return "redirect:/admin/cai-dat/sao-luu";
    }

    private String thuMuc() {
        if (thuMucCauHinh != null && !thuMucCauHinh.isBlank()) return thuMucCauHinh.trim();
        try {
            return jdbc.queryForObject("SELECT CAST(SERVERPROPERTY('InstanceDefaultBackupPath') AS NVARCHAR(400))", String.class);
        } catch (DataAccessException e) {
            return null;
        }
    }

    private static String cat(String s) {
        return s.length() > 255 ? s.substring(s.length() - 255) : s;
    }
}
