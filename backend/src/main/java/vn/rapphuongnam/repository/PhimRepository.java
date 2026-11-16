package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.Phim;
import vn.rapphuongnam.entity.TrangThaiPhim;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PhimRepository extends JpaRepository<Phim, Integer> {

    //Dieu kien "suat chieu chua ket thuc" viet bang T-SQL (dung trong cac cau native ben duoi)
    String SUAT_CHUA_KET_THUC = " (s.ngayChieu > CAST(GETDATE() AS DATE) OR (s.ngayChieu = CAST(GETDATE() AS DATE) AND s.gioKetThuc > CAST(GETDATE() AS TIME))) ";

    String DIEU_KIEN_TIM = "where (:q is null or lower(p.tenPhim) like lower(concat('%', :q, '%')) "
            + "or lower(p.daoDien) like lower(concat('%', :q, '%')) or lower(p.dienVien) like lower(concat('%', :q, '%'))) "
            + "and (:theLoaiId is null or t.id = :theLoaiId) "
            + "and (:trangThai is null or p.trangThai = :trangThai)";

    //UC2.4 + UC2.5: xem danh sach va tim kiem phim (theo ten/dao dien/dien vien, the loai, trang thai)
    @Query(value = "select distinct p from Phim p left join p.theLoai t " + DIEU_KIEN_TIM,
            countQuery = "select count(distinct p) from Phim p left join p.theLoai t " + DIEU_KIEN_TIM)
    Page<Phim> timKiem(@Param("q") String q,
                       @Param("theLoaiId") Integer theLoaiId,
                       @Param("trangThai") TrangThaiPhim trangThai,
                       Pageable pageable);

    //Trang khach hang: phim theo trang thai
    List<Phim> findByTrangThaiOrderByNgayKhoiChieuDesc(TrangThaiPhim trangThai);

    //Kiem tra trung: cung ten va cung ngay khoi chieu (id = 0 khi them moi)
    boolean existsByTenPhimIgnoreCaseAndNgayKhoiChieuAndIdNot(String tenPhim, LocalDate ngayKhoiChieu, Integer id);

    @Query(value = "SELECT COUNT(*) FROM SuatChieu s WHERE s.phimId = :phimId AND" + SUAT_CHUA_KET_THUC, nativeQuery = true)
    long demSuatChuaKetThuc(@Param("phimId") Integer phimId);

    //Suat chua ket thuc da co don (dang giu ghe hoac da thanh toan)
    @Query(value = "SELECT COUNT(DISTINCT s.id) FROM SuatChieu s JOIN DonHang d ON d.suatChieuId = s.id "
            + "WHERE s.phimId = :phimId AND d.trangThai IN ('choThanhToan', 'daThanhToan') AND" + SUAT_CHUA_KET_THUC, nativeQuery = true)
    long demSuatChuaKetThucCoDon(@Param("phimId") Integer phimId);

    //Suat chua ket thuc nam ngoai khoang chieu moi
    @Query(value = "SELECT COUNT(*) FROM SuatChieu s WHERE s.phimId = :phimId "
            + "AND (s.ngayChieu < :tuNgay OR s.ngayChieu > :denNgay) AND" + SUAT_CHUA_KET_THUC, nativeQuery = true)
    long demSuatNgoaiKhoang(@Param("phimId") Integer phimId, @Param("tuNgay") LocalDate tuNgay, @Param("denNgay") LocalDate denNgay);

    //Khi an phim: tam ngung cac suat chua ket thuc (cac suat nay chac chan chua co don)
    @Modifying
    @Query(value = "UPDATE s SET trangThai = 'tamNgung' FROM SuatChieu s WHERE s.phimId = :phimId AND" + SUAT_CHUA_KET_THUC, nativeQuery = true)
    int tamNgungSuatChuaKetThuc(@Param("phimId") Integer phimId);

    @Query(value = "SELECT COUNT(*) FROM SuatChieu s WHERE s.phimId = :phimId", nativeQuery = true)
    long demTongSuat(@Param("phimId") Integer phimId);

    @Query(value = "SELECT COUNT(*) FROM Ve v JOIN DonHang d ON d.id = v.donHangId JOIN SuatChieu s ON s.id = d.suatChieuId "
            + "WHERE s.phimId = :phimId AND d.trangThai = 'daThanhToan'", nativeQuery = true)
    long demVeDaBan(@Param("phimId") Integer phimId);

    @Query(value = "SELECT ISNULL(SUM(d.tongTienVe), 0) FROM DonHang d JOIN SuatChieu s ON s.id = d.suatChieuId "
            + "WHERE s.phimId = :phimId AND d.trangThai = 'daThanhToan'", nativeQuery = true)
    BigDecimal tinhDoanhThuVe(@Param("phimId") Integer phimId);
}
