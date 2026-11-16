package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.SuatChieu;
import vn.rapphuongnam.entity.TrangThaiSuatChieu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SuatChieuRepository extends JpaRepository<SuatChieu, Integer> {

    //Lay suat kem phim + phong (open-in-view dang tat nen phai fetch san)
    @Query("select s from SuatChieu s join fetch s.phim join fetch s.phongChieu where s.id = :id")
    java.util.Optional<SuatChieu> timChiTiet(@Param("id") Integer id);

    //Dieu kien gia hieu luc (dung chung trong cac cau native)
    String GIA_HIEU_LUC = " q.trangThai = 'dangApDung' AND q.loaiNgay = :loaiNgay AND q.khungGio = :khungGio "
            + "AND q.ngayBatDau <= :ngay AND (q.ngayKetThuc IS NULL OR q.ngayKetThuc >= :ngay) ";

    //Kiem tra trung lich: lay cac suat cung phong, cung ngay, khac id
    List<SuatChieu> findByPhongChieuIdAndNgayChieuAndIdNot(Integer phongId, LocalDate ngay, Integer id);

    @Query(value = "SELECT TOP 1 thoiGianDonPhong FROM CaiDat ORDER BY id", nativeQuery = true)
    Integer thoiGianDonPhong();

    @Query(value = "SELECT COUNT(*) FROM NgayLe WHERE ngay = :ngay", nativeQuery = true)
    long demNgayLe(@Param("ngay") LocalDate ngay);

    //So loai ghe cua phong chua co gia hieu luc (phai = 0)
    @Query(value = "SELECT COUNT(DISTINCT g.loaiVeId) FROM Ghe g WHERE g.phongChieuId = :phongId AND g.trangThai = 'hoatDong' "
            + "AND NOT EXISTS (SELECT 1 FROM QuyTacGia q WHERE q.loaiVeId = g.loaiVeId AND" + GIA_HIEU_LUC + ")", nativeQuery = true)
    long demLoaiGheChuaCoGia(@Param("phongId") Integer phongId, @Param("loaiNgay") String loaiNgay,
                             @Param("khungGio") String khungGio, @Param("ngay") LocalDate ngay);

    //Gia thap nhat ("tu X d")
    @Query(value = "SELECT MIN(q.gia) FROM QuyTacGia q WHERE" + GIA_HIEU_LUC
            + "AND q.loaiVeId IN (SELECT g.loaiVeId FROM Ghe g WHERE g.phongChieuId = :phongId AND g.trangThai = 'hoatDong')",
            nativeQuery = true)
    BigDecimal giaThapNhat(@Param("phongId") Integer phongId, @Param("loaiNgay") String loaiNgay,
                           @Param("khungGio") String khungGio, @Param("ngay") LocalDate ngay);

    //Bang gia day du: [tenLoaiVe, tenDoiTuong, gia]
    @Query(value = "SELECT lv.tenLoaiVe, dt.tenDoiTuong, q.gia FROM QuyTacGia q JOIN LoaiVe lv ON lv.id = q.loaiVeId "
            + "JOIN DoiTuong dt ON dt.id = q.doiTuongId WHERE" + GIA_HIEU_LUC
            + "AND q.loaiVeId IN (SELECT g.loaiVeId FROM Ghe g WHERE g.phongChieuId = :phongId AND g.trangThai = 'hoatDong') "
            + "ORDER BY lv.tenLoaiVe, dt.tenDoiTuong", nativeQuery = true)
    List<Object[]> bangGia(@Param("phongId") Integer phongId, @Param("loaiNgay") String loaiNgay,
                           @Param("khungGio") String khungGio, @Param("ngay") LocalDate ngay);

    @Query(value = "SELECT COUNT(*) FROM Ghe WHERE phongChieuId = :phongId AND trangThai = 'hoatDong'", nativeQuery = true)
    long demGheHoatDong(@Param("phongId") Integer phongId);

    //UC3.1 buoc 8: tao trang thai ghe cho suat
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO GheSuatChieu (suatChieuId, gheId, trangThai) "
            + "SELECT :suatId, g.id, 'trong' FROM Ghe g WHERE g.phongChieuId = :phongId AND g.trangThai = 'hoatDong'", nativeQuery = true)
    int taoGheSuatChieu(@Param("suatId") Integer suatId, @Param("phongId") Integer phongId);

    //Chi goi khi suat khong co don nao
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM GheSuatChieu WHERE suatChieuId = :suatId", nativeQuery = true)
    int xoaGheSuatChieu(@Param("suatId") Integer suatId);

    //Don con hieu luc
    @Query(value = "SELECT COUNT(*) FROM DonHang WHERE suatChieuId = :id AND trangThai IN ('choThanhToan', 'daThanhToan')", nativeQuery = true)
    long demDonConHieuLuc(@Param("id") Integer id);

    //Moi don (ke ca da huy / het han)
    @Query(value = "SELECT COUNT(*) FROM DonHang WHERE suatChieuId = :id", nativeQuery = true)
    long demMoiDon(@Param("id") Integer id);

    //Thong ke ghe cho nhieu suat: [suatChieuId, trong, daDat]
    @Query(value = "SELECT suatChieuId, SUM(CASE WHEN trangThai = 'trong' THEN 1 ELSE 0 END), "
            + "SUM(CASE WHEN trangThai IN ('dangGiu', 'daBan') THEN 1 ELSE 0 END) "
            + "FROM GheSuatChieu WHERE suatChieuId IN (:ids) GROUP BY suatChieuId", nativeQuery = true)
    List<Object[]> thongKeGhe(@Param("ids") List<Integer> ids);

    //UC3.4 xem lich theo khoang ngay (va phong)
    @Query("select s from SuatChieu s join fetch s.phim ph join fetch s.phongChieu pc "
            + "where s.ngayChieu between :tu and :den and (:phongId is null or pc.id = :phongId) "
            + "order by s.ngayChieu, pc.tenPhong, s.gioBatDau")
    List<SuatChieu> lich(@Param("tu") LocalDate tu, @Param("den") LocalDate den, @Param("phongId") Integer phongId);

    String DIEU_KIEN_TIM = "where (:q is null or lower(ph.tenPhim) like lower(concat('%', :q, '%'))) "
            + "and (:phimId is null or ph.id = :phimId) and (:phongId is null or pc.id = :phongId) "
            + "and (:tuNgay is null or s.ngayChieu >= :tuNgay) and (:denNgay is null or s.ngayChieu <= :denNgay) "
            + "and (:trangThai is null or s.trangThai = :trangThai) "
            + "and (:anKetThuc = false or s.ngayChieu > :homNay or (s.ngayChieu = :homNay and s.gioKetThuc > local_time))";

    //UC3.5 tim kiem co phan trang
    @Query(value = "select s from SuatChieu s join fetch s.phim ph join fetch s.phongChieu pc " + DIEU_KIEN_TIM,
            countQuery = "select count(s) from SuatChieu s join s.phim ph join s.phongChieu pc " + DIEU_KIEN_TIM)
    Page<SuatChieu> timKiem(@Param("q") String q, @Param("phimId") Integer phimId, @Param("phongId") Integer phongId,
                            @Param("tuNgay") LocalDate tuNgay, @Param("denNgay") LocalDate denNgay,
                            @Param("trangThai") TrangThaiSuatChieu trangThai,
                            @Param("anKetThuc") boolean anKetThuc, @Param("homNay") LocalDate homNay,
                            Pageable pageable);
}
