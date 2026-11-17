package vn.rapphuongnam.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.rapphuongnam.dto.DonHangKhach;

import java.util.List;

//Danh sach don cua mot khach hang, hien o trang chi tiet tai khoan (UC8.4 buoc 3).
//Dung JdbcTemplate vi chua co entity DonHang (phan cua Thang).
@Repository
public class DonHangKhachRepository {
    private final JdbcTemplate jdbc;

    public DonHangKhachRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<DonHangKhach> theoTaiKhoan(Integer taiKhoanId) {
        String sql = "SELECT d.id, d.maVe, p.tenPhim, s.ngayChieu, s.gioBatDau, d.tongTien, d.trangThai, d.thoiGianDat, "
                + "(SELECT COUNT(*) FROM Ve v WHERE v.donHangId = d.id) AS soVe "
                + "FROM DonHang d "
                + "JOIN SuatChieu s ON s.id = d.suatChieuId "
                + "JOIN Phim p ON p.id = s.phimId "
                + "WHERE d.taiKhoanId = ? ORDER BY d.thoiGianDat DESC";
        return jdbc.query(sql, (rs, i) -> new DonHangKhach(
                rs.getInt("id"),
                rs.getString("maVe"),
                rs.getString("tenPhim"),
                rs.getDate("ngayChieu").toLocalDate(),
                rs.getTime("gioBatDau").toLocalTime(),
                rs.getInt("soVe"),
                rs.getBigDecimal("tongTien"),
                rs.getString("trangThai"),
                rs.getTimestamp("thoiGianDat").toLocalDateTime()), taiKhoanId);
    }
}
