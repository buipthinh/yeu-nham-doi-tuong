package vn.rapphuongnam.controller.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Trang tong quan cua Admin: so lieu nhanh, doanh thu 14 ngay, top phim, suat chieu hom nay, ton kho thap, don moi nhat
@Controller
public class TongQuanController {
    private static final String HOM_NAY = "CAST(GETDATE() AS DATE)";
    private static final Map<String, String[]> TRANG_THAI_DON = Map.of(
            "choThanhToan", new String[]{"Chờ thanh toán", "warn"},
            "daThanhToan", new String[]{"Đã thanh toán", "ok"},
            "daHuy", new String[]{"Đã hủy", "mute"},
            "hetHan", new String[]{"Hết hạn", "bad"});

    private final JdbcTemplate jdbc;

    public TongQuanController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/admin/tong-quan")
    public String tongQuan(Model model) {
        model.addAttribute("menu", "tong-quan");
        model.addAttribute("homNay", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        //---- So lieu nhanh ----
        model.addAttribute("doanhThuHomNay", so("SELECT ISNULL(SUM(soTien), 0) FROM GiaoDich WHERE trangThai = 'thanhCong' "
                + "AND CAST(thoiGian AS DATE) = " + HOM_NAY));
        model.addAttribute("doanhThu7Ngay", so("SELECT ISNULL(SUM(soTien), 0) FROM GiaoDich WHERE trangThai = 'thanhCong' "
                + "AND thoiGian >= DATEADD(DAY, -6, " + HOM_NAY + ")"));
        model.addAttribute("ve7Ngay", so("SELECT COUNT(*) FROM Ve v JOIN DonHang d ON d.id = v.donHangId "
                + "WHERE d.trangThai = 'daThanhToan' AND d.thoiGianDat >= DATEADD(DAY, -6, " + HOM_NAY + ")"));
        model.addAttribute("donCho", so("SELECT COUNT(*) FROM DonHang WHERE trangThai = 'choThanhToan'"));
        model.addAttribute("suatHomNay", so("SELECT COUNT(*) FROM SuatChieu WHERE ngayChieu = " + HOM_NAY));
        model.addAttribute("phimDangChieu", so("SELECT COUNT(*) FROM Phim WHERE trangThai = 'dangChieu'"));

        //---- Doanh thu 14 ngay gan nhat (bieu do cot) ----
        Map<LocalDate, BigDecimal> theoNgay = new HashMap<>();
        for (Map<String, Object> r : jdbc.queryForList("SELECT CAST(thoiGian AS DATE) AS ngay, SUM(soTien) AS tien FROM GiaoDich "
                + "WHERE trangThai = 'thanhCong' AND thoiGian >= DATEADD(DAY, -13, " + HOM_NAY + ") GROUP BY CAST(thoiGian AS DATE)")) {
            theoNgay.put(((java.sql.Date) r.get("ngay")).toLocalDate(), (BigDecimal) r.get("tien"));
        }
        BigDecimal max = theoNgay.values().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal tong14 = BigDecimal.ZERO;
        List<Map<String, Object>> cot = new ArrayList<>();
        for (int i = 13; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusDays(i);
            BigDecimal tien = theoNgay.getOrDefault(d, BigDecimal.ZERO);
            tong14 = tong14.add(tien);
            Map<String, Object> c = new HashMap<>();
            c.put("nhan", d.format(DateTimeFormatter.ofPattern("dd/MM")));
            c.put("tien", tien);
            c.put("phanTram", max.signum() == 0 ? 0 : tien.multiply(BigDecimal.valueOf(100)).divide(max, 0, java.math.RoundingMode.HALF_UP).intValue());
            cot.add(c);
        }
        model.addAttribute("bieuDo", cot);
        model.addAttribute("tong14Ngay", tong14);

        //---- Top 5 phim ban nhieu ve nhat 30 ngay ----
        List<Map<String, Object>> top = jdbc.queryForList("SELECT TOP 5 p.tenPhim, COUNT(v.id) AS soVe, SUM(v.giaVe) AS doanhThu "
                + "FROM Ve v JOIN DonHang d ON d.id = v.donHangId JOIN SuatChieu s ON s.id = d.suatChieuId JOIN Phim p ON p.id = s.phimId "
                + "WHERE d.trangThai = 'daThanhToan' AND d.thoiGianDat >= DATEADD(DAY, -29, " + HOM_NAY + ") "
                + "GROUP BY p.tenPhim ORDER BY soVe DESC");
        int maxVe = top.stream().mapToInt(r -> ((Number) r.get("soVe")).intValue()).max().orElse(0);
        for (Map<String, Object> r : top) {
            int ve = ((Number) r.get("soVe")).intValue();
            r.put("phanTram", maxVe == 0 ? 0 : ve * 100 / maxVe);
        }
        model.addAttribute("topPhim", top);

        //---- Suat chieu hom nay ----
        List<Map<String, Object>> suat = jdbc.queryForList("SELECT s.id, CONVERT(CHAR(5), s.gioBatDau, 108) AS gio, "
                + "CONVERT(CHAR(5), s.gioKetThuc, 108) AS gioKetThuc, p.tenPhim, ph.tenPhong, s.trangThai, "
                + "(SELECT COUNT(*) FROM GheSuatChieu g WHERE g.suatChieuId = s.id) AS tongGhe, "
                + "(SELECT COUNT(*) FROM GheSuatChieu g WHERE g.suatChieuId = s.id AND g.trangThai = 'daBan') AS daBan "
                + "FROM SuatChieu s JOIN Phim p ON p.id = s.phimId JOIN PhongChieu ph ON ph.id = s.phongChieuId "
                + "WHERE s.ngayChieu = " + HOM_NAY + " ORDER BY s.gioBatDau, ph.tenPhong");
        for (Map<String, Object> r : suat) {
            int tongGhe = ((Number) r.get("tongGhe")).intValue();
            int daBan = ((Number) r.get("daBan")).intValue();
            r.put("phanTram", tongGhe == 0 ? 0 : daBan * 100 / tongGhe);
            r.put("tamNgung", "tamNgung".equals(r.get("trangThai")));
        }
        model.addAttribute("suatChieu", suat);

        //---- Thuc pham sap het (theo nguong canh bao trong Cai dat) ----
        model.addAttribute("tonThap", jdbc.queryForList("SELECT id, tenMon, tonKho FROM ThucPham WHERE trangThai = 'dangBan' "
                + "AND tonKho <= ISNULL((SELECT TOP 1 nguongCanhBaoTonKho FROM CaiDat), 20) ORDER BY tonKho, tenMon"));

        //---- 8 don moi nhat ----
        List<Map<String, Object>> don = jdbc.queryForList("SELECT TOP 8 d.id, d.maVe, tk.hoTen, p.tenPhim, d.tongTien, d.trangThai, "
                + "FORMAT(d.thoiGianDat, 'dd/MM HH:mm') AS luc "
                + "FROM DonHang d JOIN TaiKhoan tk ON tk.id = d.taiKhoanId JOIN SuatChieu s ON s.id = d.suatChieuId "
                + "JOIN Phim p ON p.id = s.phimId ORDER BY d.thoiGianDat DESC, d.id DESC");
        for (Map<String, Object> r : don) {
            String[] tt = TRANG_THAI_DON.getOrDefault((String) r.get("trangThai"), new String[]{(String) r.get("trangThai"), "mute"});
            r.put("nhanTrangThai", tt[0]);
            r.put("cssTrangThai", tt[1]);
        }
        model.addAttribute("donMoi", don);

        return "admin/tong-quan";
    }

    private Number so(String sql) {
        Number n = jdbc.queryForObject(sql, Number.class);
        return n == null ? 0 : n;
    }
}
