package vn.rapphuongnam.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.rapphuongnam.dto.ThongKePhimDong;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Truy van cho UC9 Bao cao. Dung JdbcTemplate vi ket qua la so lieu tong hop, khong phai entity.
@Repository
public class BaoCaoRepository {
    private final JdbcTemplate jdbc;

    public BaoCaoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //Mot don da thanh toan trong ky
    public static class DonDaThanhToan {
        public final LocalDateTime thoiGian;
        public final BigDecimal tienVe;
        public final BigDecimal tienThucPham;
        public final BigDecimal tongTien;
        public final int soVe;

        public DonDaThanhToan(LocalDateTime thoiGian, BigDecimal tienVe, BigDecimal tienThucPham, BigDecimal tongTien, int soVe) {
            this.thoiGian = thoiGian;
            this.tienVe = tienVe;
            this.tienThucPham = tienThucPham;
            this.tongTien = tongTien;
            this.soVe = soVe;
        }
    }

    //UC9.1: cac don thanh toan thanh cong trong [tuNgay, denNgay], tinh theo thoi diem giao dich
    //(giong view vw_DoanhThuNgay). phimId = null: tat ca phim.
    public List<DonDaThanhToan> donDaThanhToan(LocalDate tuNgay, LocalDate denNgay, Integer phimId) {
        String sql = "SELECT gd.thoiGian, d.tongTienVe, d.tongTienThucPham, d.tongTien, COALESCE(v.soVe, 0) AS soVe "
                + "FROM DonHang d "
                + "JOIN GiaoDich gd ON gd.donHangId = d.id AND gd.trangThai = 'thanhCong' "
                + "JOIN SuatChieu s ON s.id = d.suatChieuId "
                + "LEFT JOIN (SELECT donHangId, COUNT(*) AS soVe FROM Ve GROUP BY donHangId) v ON v.donHangId = d.id "
                + "WHERE d.trangThai = 'daThanhToan' AND gd.thoiGian >= ? AND gd.thoiGian < ?";
        List<Object> thamSo = new ArrayList<>();
        thamSo.add(Timestamp.valueOf(tuNgay.atStartOfDay()));
        thamSo.add(Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay()));
        if (phimId != null) {
            sql += " AND s.phimId = ?";
            thamSo.add(phimId);
        }
        return jdbc.query(sql, (rs, i) -> new DonDaThanhToan(
                rs.getTimestamp("thoiGian").toLocalDateTime(),
                rs.getBigDecimal("tongTienVe"),
                rs.getBigDecimal("tongTienThucPham"),
                rs.getBigDecimal("tongTien"),
                rs.getInt("soVe")), thamSo.toArray());
    }

    //UC9.2: thong ke theo phim cho cac suat chieu co ngayChieu trong ky, xep theo so ve giam dan
    public List<ThongKePhimDong> thongKeTheoPhim(LocalDate tuNgay, LocalDate denNgay) {
        String sql = "SELECT p.id, p.tenPhim, sc.soSuat, "
                + "COALESCE(gh.tongGhe, 0) AS tongGhe, COALESCE(gh.daBan, 0) AS daBan, "
                + "COALESCE(dh.soDon, 0) AS soDon, COALESCE(dh.doanhThu, 0) AS doanhThu, "
                + "COALESCE(ve.soVe, 0) AS soVe "
                + "FROM (SELECT phimId, COUNT(*) AS soSuat FROM SuatChieu "
                + "      WHERE ngayChieu BETWEEN ? AND ? GROUP BY phimId) sc "
                + "JOIN Phim p ON p.id = sc.phimId "
                + "LEFT JOIN (SELECT s.phimId, COUNT(*) AS tongGhe, "
                + "      SUM(CASE WHEN g.trangThai = 'daBan' THEN 1 ELSE 0 END) AS daBan "
                + "      FROM GheSuatChieu g JOIN SuatChieu s ON s.id = g.suatChieuId "
                + "      WHERE s.ngayChieu BETWEEN ? AND ? GROUP BY s.phimId) gh ON gh.phimId = p.id "
                + "LEFT JOIN (SELECT s.phimId, COUNT(*) AS soDon, SUM(d.tongTienVe) AS doanhThu "
                + "      FROM DonHang d JOIN SuatChieu s ON s.id = d.suatChieuId "
                + "      WHERE d.trangThai = 'daThanhToan' AND s.ngayChieu BETWEEN ? AND ? GROUP BY s.phimId) dh ON dh.phimId = p.id "
                + "LEFT JOIN (SELECT s.phimId, COUNT(*) AS soVe "
                + "      FROM Ve v JOIN DonHang d ON d.id = v.donHangId JOIN SuatChieu s ON s.id = d.suatChieuId "
                + "      WHERE d.trangThai = 'daThanhToan' AND s.ngayChieu BETWEEN ? AND ? GROUP BY s.phimId) ve ON ve.phimId = p.id "
                + "ORDER BY soVe DESC, p.tenPhim";
        Date tu = Date.valueOf(tuNgay);
        Date den = Date.valueOf(denNgay);
        return jdbc.query(sql, (rs, i) -> {
            ThongKePhimDong t = new ThongKePhimDong();
            t.setPhimId(rs.getInt("id"));
            t.setTenPhim(rs.getString("tenPhim"));
            t.setSoSuatChieu(rs.getInt("soSuat"));
            t.setTongSoGhe(rs.getInt("tongGhe"));
            t.setSoGheDaBan(rs.getInt("daBan"));
            t.setSoDon(rs.getInt("soDon"));
            t.setDoanhThuVe(rs.getBigDecimal("doanhThu"));
            t.setSoVe(rs.getInt("soVe"));
            return t;
        }, tu, den, tu, den, tu, den, tu, den);
    }
}
