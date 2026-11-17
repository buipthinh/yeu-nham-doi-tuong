package vn.rapphuongnam.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import vn.rapphuongnam.dto.*;
import vn.rapphuongnam.entity.TrangThaiDonHang;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

//UC6.2 Quan ly dat ve: chi doc du lieu don hang nen dung JdbcTemplate + T-SQL (khong can entity DonHang).
//Don hang do trang khach (UC7) tao ra; o day khong sua, khong huy don thu cong (ngoai pham vi UC6.2).
@Repository
public class DonHangRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public DonHangRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final String COT = "SELECT d.id, d.maVe, d.taiKhoanId, t.hoTen, t.soDienThoai, t.email, p.tenPhim, pc.tenPhong, "
            + "s.ngayChieu, s.gioBatDau, s.gioKetThuc, d.tongTienVe, d.tongTienThucPham, d.tongTien, d.trangThai, "
            + "d.thoiGianDat, d.hanGiuGhe, "
            + "(SELECT COUNT(*) FROM Ve v WHERE v.donHangId = d.id) AS soVe, "
            + "gd.phuongThuc, gd.maGiaoDichCong, gd.soTien AS soTienThanhToan, gd.trangThai AS trangThaiGiaoDich, gd.thoiGian AS thoiGianThanhToan ";

    private static final String TU = "FROM DonHang d "
            + "JOIN TaiKhoan t ON t.id = d.taiKhoanId "
            + "JOIN SuatChieu s ON s.id = d.suatChieuId "
            + "JOIN Phim p ON p.id = s.phimId "
            + "JOIN PhongChieu pc ON pc.id = s.phongChieuId ";

    //Giao dich thanh cong neu co, khong thi lan thanh toan gan nhat
    private static final String GIAO_DICH = "OUTER APPLY (SELECT TOP 1 g.phuongThuc, g.maGiaoDichCong, g.soTien, g.trangThai, g.thoiGian "
            + "FROM GiaoDich g WHERE g.donHangId = d.id "
            + "ORDER BY CASE WHEN g.trangThai = 'thanhCong' THEN 0 ELSE 1 END, g.thoiGian DESC) gd ";

    private static final RowMapper<DonHangDong> MAP = (rs, i) -> {
        DonHangDong d = new DonHangDong();
        d.setId(rs.getInt("id"));
        d.setMaVe(rs.getString("maVe") == null ? null : rs.getString("maVe").trim());
        d.setTaiKhoanId(rs.getInt("taiKhoanId"));
        d.setHoTen(rs.getString("hoTen"));
        d.setSoDienThoai(rs.getString("soDienThoai"));
        d.setEmail(rs.getString("email"));
        d.setTenPhim(rs.getString("tenPhim"));
        d.setTenPhong(rs.getString("tenPhong"));
        d.setNgayChieu(rs.getDate("ngayChieu").toLocalDate());
        d.setGioBatDau(rs.getTime("gioBatDau").toLocalTime());
        d.setGioKetThuc(rs.getTime("gioKetThuc").toLocalTime());
        d.setTongTienVe(rs.getBigDecimal("tongTienVe"));
        d.setTongTienThucPham(rs.getBigDecimal("tongTienThucPham"));
        d.setTongTien(rs.getBigDecimal("tongTien"));
        d.setTrangThai(TrangThaiDonHang.valueOf(rs.getString("trangThai")));
        d.setThoiGianDat(rs.getTimestamp("thoiGianDat").toLocalDateTime());
        d.setHanGiuGhe(rs.getTimestamp("hanGiuGhe").toLocalDateTime());
        d.setSoVe(rs.getInt("soVe"));
        d.setPhuongThuc(rs.getString("phuongThuc"));
        d.setMaGiaoDichCong(rs.getString("maGiaoDichCong"));
        d.setSoTienThanhToan(rs.getBigDecimal("soTienThanhToan"));
        d.setTrangThaiGiaoDich(rs.getString("trangThaiGiaoDich"));
        Timestamp tt = rs.getTimestamp("thoiGianThanhToan");
        d.setThoiGianThanhToan(tt == null ? null : tt.toLocalDateTime());
        return d;
    };

    //UC6.2.1 danh sach don hang co loc + phan trang, moi nhat truoc
    public List<DonHangDong> timKiem(DonHangLoc loc, int batDau, int soDong) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        String where = dieuKien(loc, p);
        p.addValue("batDau", batDau).addValue("soDong", soDong);
        String sql = COT + TU + GIAO_DICH + where
                + "ORDER BY d.thoiGianDat DESC, d.id DESC OFFSET :batDau ROWS FETCH NEXT :soDong ROWS ONLY";
        return jdbc.query(sql, p, MAP);
    }

    public long dem(DonHangLoc loc) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        Long n = jdbc.queryForObject("SELECT COUNT(*) " + TU + dieuKien(loc, p), p, Long.class);
        return n == null ? 0 : n;
    }

    private static String dieuKien(DonHangLoc loc, MapSqlParameterSource p) {
        StringBuilder w = new StringBuilder("WHERE 1 = 1 ");
        if (loc.getTrangThai() != null) {
            w.append("AND d.trangThai = :trangThai ");
            p.addValue("trangThai", loc.getTrangThai().name());
        }
        if (loc.getPhimId() != null) {
            w.append("AND s.phimId = :phimId ");
            p.addValue("phimId", loc.getPhimId());
        }
        if (loc.getTuNgay() != null) {
            w.append("AND d.thoiGianDat >= :tuNgay ");
            p.addValue("tuNgay", loc.getTuNgay().atStartOfDay());
        }
        if (loc.getDenNgay() != null) {
            w.append("AND d.thoiGianDat < :denNgay ");
            p.addValue("denNgay", loc.getDenNgay().plusDays(1).atStartOfDay());
        }
        String q = loc.getQ() == null ? "" : loc.getQ().trim();
        if (!q.isEmpty()) {
            //Tim theo ma don, ma ve, so dien thoai hoac ten khach
            w.append("AND (d.maVe = :q OR t.soDienThoai = :q OR t.hoTen LIKE :qLike");
            Integer maDon = maDon(q);
            if (maDon != null) {
                w.append(" OR d.id = :maDon");
                p.addValue("maDon", maDon);
            }
            w.append(") ");
            p.addValue("q", q).addValue("qLike", "%" + q + "%");
        }
        return w.toString();
    }

    //"123", "#123", "DH123" deu hieu la ma don 123
    public static Integer maDon(String q) {
        String s = q.trim().replaceFirst("^(#|[dD][hH])", "");
        if (!s.matches("\\d{1,9}")) return null;
        return Integer.valueOf(s);
    }

    public Optional<DonHangDong> theoId(Integer id) {
        return jdbc.query(COT + TU + GIAO_DICH + "WHERE d.id = :id", new MapSqlParameterSource("id", id), MAP)
                .stream().findFirst();
    }

    //UC6.2.2 tra cuu theo ma ve (8 ky tu)
    public Optional<DonHangDong> theoMaVe(String maVe) {
        return jdbc.query(COT + TU + GIAO_DICH + "WHERE d.maVe = :maVe", new MapSqlParameterSource("maVe", maVe), MAP)
                .stream().findFirst();
    }

    //UC6.2.2 tra cuu theo so dien thoai: co the nhieu don
    public List<DonHangDong> theoSoDienThoai(String sdt) {
        return jdbc.query(COT + TU + GIAO_DICH + "WHERE t.soDienThoai = :sdt ORDER BY d.thoiGianDat DESC, d.id DESC",
                new MapSqlParameterSource("sdt", sdt), MAP);
    }

    public List<VeTrongDon> veCuaDon(Integer donHangId) {
        String sql = "SELECT g.maGhe, lv.tenLoaiVe, dt.tenDoiTuong, v.giaVe, gs.trangThai "
                + "FROM Ve v JOIN GheSuatChieu gs ON gs.id = v.gheSuatChieuId "
                + "JOIN Ghe g ON g.id = gs.gheId "
                + "JOIN LoaiVe lv ON lv.id = v.loaiVeId "
                + "JOIN DoiTuong dt ON dt.id = v.doiTuongId "
                + "WHERE v.donHangId = :id ORDER BY g.hang, g.cot";
        return jdbc.query(sql, new MapSqlParameterSource("id", donHangId), (rs, i) -> new VeTrongDon(
                rs.getString("maGhe"), rs.getString("tenLoaiVe"), rs.getString("tenDoiTuong"),
                rs.getBigDecimal("giaVe"), rs.getString("trangThai")));
    }

    public List<MonTrongDon> monCuaDon(Integer donHangId) {
        String sql = "SELECT c.thucPhamId, tp.tenMon, c.soLuong, c.donGia FROM ChiTietThucPham c "
                + "JOIN ThucPham tp ON tp.id = c.thucPhamId WHERE c.donHangId = :id ORDER BY tp.tenMon";
        return jdbc.query(sql, new MapSqlParameterSource("id", donHangId), (rs, i) -> new MonTrongDon(
                rs.getInt("thucPhamId"), rs.getString("tenMon"), rs.getInt("soLuong"), rs.getBigDecimal("donGia")));
    }

    public List<GiaoDichDong> giaoDichCuaDon(Integer donHangId) {
        String sql = "SELECT phuongThuc, maGiaoDichCong, soTien, trangThai, thoiGian FROM GiaoDich "
                + "WHERE donHangId = :id ORDER BY thoiGian DESC";
        return jdbc.query(sql, new MapSqlParameterSource("id", donHangId), (rs, i) -> new GiaoDichDong(
                rs.getString("phuongThuc"), rs.getString("maGiaoDichCong"), rs.getBigDecimal("soTien"),
                rs.getString("trangThai"), rs.getTimestamp("thoiGian").toLocalDateTime()));
    }

    //Thong ke nhanh tren dau trang danh sach don
    public long demTheoTrangThai(TrangThaiDonHang tt) {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM DonHang WHERE trangThai = :tt",
                new MapSqlParameterSource("tt", tt.name()), Long.class);
        return n == null ? 0 : n;
    }

    public long demDonHomNay() {
        Long n = jdbc.queryForObject("SELECT COUNT(*) FROM DonHang WHERE CAST(thoiGianDat AS DATE) = CAST(GETDATE() AS DATE)",
                new MapSqlParameterSource(), Long.class);
        return n == null ? 0 : n;
    }

    //UC11: tra ghe giu qua han va chuyen don sang Het han truoc khi hien thi, de trang thai luon dung
    public void giaiPhongGheQuaHan() {
        jdbc.getJdbcTemplate().execute("EXEC dbo.sp_GiaiPhongGheQuaHan");
    }
}
