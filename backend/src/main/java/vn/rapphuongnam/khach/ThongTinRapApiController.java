package vn.rapphuongnam.khach;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import vn.rapphuongnam.entity.KhungGio;

import java.time.LocalDate;
import java.util.*;

//API cho cac danh muc con lai cua trang khach: Lich chieu, Bang gia ve, Gioi thieu rap (khong can dang nhap)
@RestController
@RequestMapping("/api")
public class ThongTinRapApiController {
    //Suat con mo ban: phim khong an, phong hoat dong, chua bat dau
    private static final String DK_SUAT = "s.trangThai = 'moBan' AND ph.trangThai = 'hoatDong' AND p.trangThai <> 'an' "
            + "AND CAST(s.ngayChieu AS DATETIME) + CAST(s.gioBatDau AS DATETIME) > GETDATE() ";

    private final JdbcTemplate jdbc;
    private final DatVeKhachService datVe;

    public ThongTinRapApiController(JdbcTemplate jdbc, DatVeKhachService datVe) {
        this.jdbc = jdbc;
        this.datVe = datVe;
    }

    //Lich chieu theo ngay cua tat ca phim. GET /api/lich-chieu?ngay=2026-11-24 (bo trong = ngay gan nhat co suat)
    @GetMapping("/lich-chieu")
    public Map<String, Object> lichChieu(@RequestParam(required = false) String ngay) {
        List<String> cacNgay = jdbc.queryForList("SELECT DISTINCT TOP 14 s.ngayChieu FROM SuatChieu s JOIN PhongChieu ph ON ph.id = s.phongChieuId "
                        + "JOIN Phim p ON p.id = s.phimId WHERE " + DK_SUAT + "ORDER BY s.ngayChieu", java.sql.Date.class)
                .stream().map(d -> d.toLocalDate().toString()).toList();
        Map<String, Object> kq = new LinkedHashMap<>();
        kq.put("cacNgay", cacNgay);
        if (cacNgay.isEmpty()) {
            kq.put("ngay", null);
            kq.put("phim", List.of());
            return kq;
        }
        String chon = (ngay != null && cacNgay.contains(ngay)) ? ngay : cacNgay.get(0);
        kq.put("ngay", chon);
        kq.put("loaiNgay", datVe.loaiNgay(LocalDate.parse(chon)));

        List<Map<String, Object>> suat = jdbc.queryForList("SELECT s.id, s.phimId, s.gioBatDau, s.gioKetThuc, ph.tenPhong, "
                + "p.tenPhim, p.doTuoi, p.poster, p.thoiLuong, p.ngonNgu, "
                + "(SELECT STRING_AGG(tl.tenTheLoai, ', ') FROM PhimTheLoai pt JOIN TheLoai tl ON tl.id = pt.theLoaiId WHERE pt.phimId = p.id) AS theLoai, "
                + "(SELECT COUNT(*) FROM GheSuatChieu g JOIN Ghe gh ON gh.id = g.gheId WHERE g.suatChieuId = s.id AND g.trangThai = 'trong' "
                + "AND gh.trangThai = 'hoatDong') AS soGheTrong "
                + "FROM SuatChieu s JOIN PhongChieu ph ON ph.id = s.phongChieuId JOIN Phim p ON p.id = s.phimId "
                + "WHERE s.ngayChieu = ? AND " + DK_SUAT + "ORDER BY p.tenPhim, s.gioBatDau", LocalDate.parse(chon));

        //Gom suat theo phim
        Map<Object, Map<String, Object>> theoPhim = new LinkedHashMap<>();
        for (Map<String, Object> r : Json.ds(suat)) {
            Map<String, Object> p = theoPhim.computeIfAbsent(r.get("phimId"), k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                for (String c : List.of("phimId", "tenPhim", "doTuoi", "poster", "thoiLuong", "ngonNgu", "theLoai")) m.put(c, r.get(c));
                m.put("suat", new ArrayList<Map<String, Object>>());
                return m;
            });
            Map<String, Object> s = new LinkedHashMap<>();
            for (String c : List.of("id", "gioBatDau", "gioKetThuc", "tenPhong", "soGheTrong")) s.put(c, r.get(c));
            s.put("khungGio", KhungGio.tuGio(java.time.LocalTime.parse((String) r.get("gioBatDau"))).name());
            @SuppressWarnings("unchecked") List<Map<String, Object>> ds = (List<Map<String, Object>>) p.get("suat");
            ds.add(s);
        }
        kq.put("phim", new ArrayList<>(theoPhim.values()));
        return kq;
    }

    //Bang gia ve dang ap dung hom nay: loai ve x doi tuong x loai ngay x khung gio
    @GetMapping("/bang-gia")
    public Map<String, Object> bangGia() {
        LocalDate homNay = LocalDate.now();
        Map<String, Object> kq = new LinkedHashMap<>();
        kq.put("loaiVe", jdbc.queryForList("SELECT id, tenLoaiVe, moTa, soChoMoiVe FROM LoaiVe WHERE trangThai = 'dangApDung' ORDER BY id"));
        kq.put("doiTuong", jdbc.queryForList("SELECT id, tenDoiTuong FROM DoiTuong ORDER BY id"));
        kq.put("gia", jdbc.queryForList("SELECT q.loaiVeId, q.doiTuongId, q.loaiNgay, q.khungGio, q.gia FROM QuyTacGia q "
                + "WHERE q.trangThai = 'dangApDung' AND q.ngayBatDau <= ? AND (q.ngayKetThuc IS NULL OR q.ngayKetThuc >= ?) "
                + "AND q.ngayBatDau = (SELECT MAX(x.ngayBatDau) FROM QuyTacGia x WHERE x.loaiVeId = q.loaiVeId AND x.doiTuongId = q.doiTuongId "
                + "AND x.loaiNgay = q.loaiNgay AND x.khungGio = q.khungGio AND x.trangThai = 'dangApDung' AND x.ngayBatDau <= ? "
                + "AND (x.ngayKetThuc IS NULL OR x.ngayKetThuc >= ?))", homNay, homNay, homNay, homNay));
        List<Map<String, Object>> khung = new ArrayList<>();
        for (KhungGio k : KhungGio.values()) khung.add(Map.of("ma", k.name(), "nhan", k.getNhan(), "moTa", k.getMoTa()));
        kq.put("khungGio", khung);
        kq.put("ngayLe", Json.ds(jdbc.queryForList("SELECT TOP 6 ngay, tenNgayLe FROM NgayLe WHERE ngay >= ? ORDER BY ngay", homNay)));
        return kq;
    }

    //Danh sach phong chieu cho trang Gioi thieu
    @GetMapping("/phong")
    public List<Map<String, Object>> phong() {
        return jdbc.queryForList("SELECT ph.id, ph.tenPhong, ph.sucChua, ph.ghiChu, ph.trangThai, "
                + "(SELECT COUNT(*) FROM Ghe g JOIN LoaiVe lv ON lv.id = g.loaiVeId WHERE g.phongChieuId = ph.id AND lv.soChoMoiVe = 2) AS soGheDoi "
                + "FROM PhongChieu ph WHERE ph.trangThai <> 'an' ORDER BY ph.id");
    }
}
