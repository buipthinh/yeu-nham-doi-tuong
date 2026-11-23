package vn.rapphuongnam.khach;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rapphuongnam.util.MatKhauUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//UC1.1 dang ky, UC1.2 dang nhap (khach hang), UC7.2 thong tin ca nhan
@Service
public class KhachHangService {
    private static final int SAI_TOI_DA = 5;
    private static final int PHUT_TAM_KHOA = 5;
    private static final String EMAIL = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String SDT = "^0\\d{9}$";
    private final JdbcTemplate jdbc;

    public KhachHangService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //UC1.2: dang nhap bang email hoac so dien thoai. Sai 5 lan lien tiep thi tam khoa 5 phut
    @Transactional(noRollbackFor = ApiLoi.class)
    public KhachDangNhap dangNhap(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.isBlank() || matKhau == null || matKhau.isBlank())
            throw new ApiLoi("Vui lòng nhập đầy đủ email/số điện thoại và mật khẩu");
        String u = tenDangNhap.trim();
        List<Map<String, Object>> ds = jdbc.queryForList(
                "SELECT * FROM TaiKhoan WHERE (email = ? OR soDienThoai = ?) AND loaiTaiKhoan = 'khachHang'", u, u);
        if (ds.isEmpty()) throw new ApiLoi("Email/số điện thoại hoặc mật khẩu không đúng");
        Map<String, Object> tk = ds.get(0);
        int id = ((Number) tk.get("id")).intValue();
        if ("biKhoa".equals(tk.get("trangThai"))) throw new ApiLoi("Tài khoản đã bị khóa. Vui lòng liên hệ rạp");
        if (tk.get("khoaDen") instanceof Timestamp t && t.toLocalDateTime().isAfter(LocalDateTime.now()))
            throw new ApiLoi("Bạn đã nhập sai " + SAI_TOI_DA + " lần. Vui lòng thử lại sau "
                    + t.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        if (!MatKhauUtil.khop(matKhau, (String) tk.get("matKhau"))) {
            int sai = ((Number) tk.get("soLanNhapSai")).intValue() + 1;
            if (sai >= SAI_TOI_DA) {
                jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = 0, khoaDen = ? WHERE id = ?", LocalDateTime.now().plusMinutes(PHUT_TAM_KHOA), id);
                throw new ApiLoi("Bạn đã nhập sai " + SAI_TOI_DA + " lần. Tài khoản tạm khóa " + PHUT_TAM_KHOA + " phút");
            }
            jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = ? WHERE id = ?", sai, id);
            throw new ApiLoi("Email/số điện thoại hoặc mật khẩu không đúng (còn " + (SAI_TOI_DA - sai) + " lần thử)");
        }
        jdbc.update("UPDATE TaiKhoan SET soLanNhapSai = 0, khoaDen = NULL, lanDangNhapCuoi = ? WHERE id = ?", LocalDateTime.now(), id);
        return new KhachDangNhap(id, (String) tk.get("tenDangNhap"), (String) tk.get("hoTen"));
    }

    //UC1.1: dang ky tai khoan khach hang, email la ten dang nhap
    @Transactional
    public KhachDangNhap dangKy(Map<String, String> f) {
        Map<String, String> loi = new LinkedHashMap<>();
        String hoTen = s(f.get("hoTen")), email = s(f.get("email")), sdt = s(f.get("soDienThoai"));
        String mk = f.getOrDefault("matKhau", ""), mk2 = f.getOrDefault("nhapLaiMatKhau", "");
        if (hoTen == null) loi.put("hoTen", "Vui lòng nhập họ tên");
        else if (hoTen.length() > 100) loi.put("hoTen", "Họ tên tối đa 100 ký tự");
        if (email == null || !email.matches(EMAIL)) loi.put("email", "Email không đúng định dạng");
        else if (trung("email", email, 0)) loi.put("email", "Email đã được sử dụng");
        if (sdt == null || !sdt.matches(SDT)) loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0");
        else if (trung("soDienThoai", sdt, 0)) loi.put("soDienThoai", "Số điện thoại đã được sử dụng");
        if (mk == null || mk.length() < 8) loi.put("matKhau", "Mật khẩu tối thiểu 8 ký tự");
        if (mk != null && !mk.equals(mk2)) loi.put("nhapLaiMatKhau", "Mật khẩu nhập lại không khớp");
        if (!loi.isEmpty()) throw new ApiLoi("Thông tin đăng ký chưa hợp lệ", 400, loi);
        LocalDateTime now = LocalDateTime.now();
        Integer id = jdbc.queryForObject("INSERT INTO TaiKhoan (tenDangNhap, matKhau, loaiTaiKhoan, hoTen, gioiTinh, soDienThoai, email, "
                        + "trangThai, soLanNhapSai, ngayTao, lanDangNhapCuoi) OUTPUT INSERTED.id VALUES (?, ?, 'khachHang', ?, 'khac', ?, ?, 'hoatDong', 0, ?, ?)",
                Integer.class, email, MatKhauUtil.maHoa(mk), hoTen, sdt, email, now, now);
        return new KhachDangNhap(id, email, hoTen);
    }

    private boolean trung(String cot, String giaTri, int boQuaId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM TaiKhoan WHERE " + cot + " = ? AND id <> ?", Integer.class, giaTri, boQuaId);
        return n != null && n > 0;
    }

    //UC7.2.1 xem thong tin ca nhan
    public Map<String, Object> thongTin(int id) {
        Map<String, Object> tk = jdbc.queryForMap("SELECT id, tenDangNhap, hoTen, ngaySinh, gioiTinh, soDienThoai, email, diaChi, ngayTao "
                + "FROM TaiKhoan WHERE id = ?", id);
        tk.put("maTaiKhoan", String.format("KH%04d", id));
        return Json.m(tk);
    }

    //UC7.2.2 cap nhat thong tin ca nhan. Email doi thi ten dang nhap doi theo
    @Transactional
    public KhachDangNhap capNhat(int id, Map<String, String> f) {
        Map<String, String> loi = new LinkedHashMap<>();
        String hoTen = s(f.get("hoTen")), email = s(f.get("email")), sdt = s(f.get("soDienThoai"));
        if (hoTen == null) loi.put("hoTen", "Vui lòng nhập họ tên");
        if (email == null || !email.matches(EMAIL)) loi.put("email", "Email không đúng định dạng");
        else if (trung("email", email, id)) loi.put("email", "Email đã thuộc tài khoản khác");
        if (sdt == null || !sdt.matches(SDT)) loi.put("soDienThoai", "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0");
        else if (trung("soDienThoai", sdt, id)) loi.put("soDienThoai", "Số điện thoại đã thuộc tài khoản khác");
        LocalDate ngaySinh = null;
        if (s(f.get("ngaySinh")) != null) {
            try {
                ngaySinh = LocalDate.parse(f.get("ngaySinh").trim());
                if (!ngaySinh.isBefore(LocalDate.now())) loi.put("ngaySinh", "Ngày sinh phải trước hôm nay");
            } catch (Exception e) {
                loi.put("ngaySinh", "Ngày sinh không hợp lệ");
            }
        }
        String gt = f.get("gioiTinh");
        if (!"nam".equals(gt) && !"nu".equals(gt)) gt = "khac";
        String diaChi = s(f.get("diaChi"));
        if (diaChi != null && diaChi.length() > 255) loi.put("diaChi", "Địa chỉ tối đa 255 ký tự");
        if (!loi.isEmpty()) throw new ApiLoi("Thông tin chưa hợp lệ", 400, loi);
        jdbc.update("UPDATE TaiKhoan SET hoTen = ?, email = ?, tenDangNhap = ?, soDienThoai = ?, ngaySinh = ?, gioiTinh = ?, diaChi = ? WHERE id = ?",
                hoTen, email, email, sdt, ngaySinh, gt, diaChi, id);
        return new KhachDangNhap(id, email, hoTen);
    }

    @Transactional
    public void doiMatKhau(int id, String cu, String moi, String nhapLai) {
        Map<String, String> loi = new LinkedHashMap<>();
        String daMaHoa = jdbc.queryForObject("SELECT matKhau FROM TaiKhoan WHERE id = ?", String.class, id);
        if (cu == null || !MatKhauUtil.khop(cu, daMaHoa)) loi.put("matKhauCu", "Mật khẩu hiện tại không đúng");
        if (moi == null || moi.length() < 8) loi.put("matKhauMoi", "Mật khẩu mới tối thiểu 8 ký tự");
        else if (!moi.equals(nhapLai)) loi.put("nhapLaiMatKhau", "Mật khẩu nhập lại không khớp");
        if (!loi.isEmpty()) throw new ApiLoi(loi.values().iterator().next(), 400, loi);
        jdbc.update("UPDATE TaiKhoan SET matKhau = ? WHERE id = ?", MatKhauUtil.maHoa(moi), id);
    }

    private static String s(String v) {
        return (v == null || v.isBlank()) ? null : v.trim();
    }
}
