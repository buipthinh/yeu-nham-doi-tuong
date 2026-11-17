package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.LoaiThucPham;
import vn.rapphuongnam.entity.ThucPham;
import vn.rapphuongnam.entity.TrangThaiThucPham;

public interface ThucPhamRepository extends JpaRepository<ThucPham, Integer> {

    //UC6.1 danh sach mon: tim theo ten/thanh phan, loc loai, trang thai, sap het hang (ton kho <= nguong)
    @Query("select t from ThucPham t where (:q is null or lower(t.tenMon) like lower(concat('%', :q, '%')) "
            + "or lower(t.thanhPhan) like lower(concat('%', :q, '%'))) "
            + "and (:loai is null or t.loai = :loai) "
            + "and (:trangThai is null or t.trangThai = :trangThai) "
            + "and (:nguong is null or t.tonKho <= :nguong)")
    Page<ThucPham> timKiem(@Param("q") String q,
                           @Param("loai") LoaiThucPham loai,
                           @Param("trangThai") TrangThaiThucPham trangThai,
                           @Param("nguong") Integer nguong,
                           Pageable pageable);

    boolean existsByTenMonIgnoreCaseAndIdNot(String tenMon, Integer id);

    long countByTrangThai(TrangThaiThucPham trangThai);

    long countByTrangThaiAndTonKhoLessThanEqual(TrangThaiThucPham trangThai, Integer tonKho);

    //Nguong canh bao ton kho trong bang CaiDat (mac dinh 20 neu chua cai dat)
    @Query(value = "SELECT TOP 1 nguongCanhBaoTonKho FROM CaiDat ORDER BY id", nativeQuery = true)
    Integer nguongCanhBao();

    //Cong / tru ton kho nguyen tu, khong de ton kho am (tra ve 0 neu khong du hang)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE ThucPham SET tonKho = tonKho + :thayDoi WHERE id = :id AND tonKho + :thayDoi >= 0", nativeQuery = true)
    int capNhatTonKho(@Param("id") Integer id, @Param("thayDoi") int thayDoi);

    @Query(value = "SELECT tonKho FROM ThucPham WHERE id = :id", nativeQuery = true)
    Integer docTonKho(@Param("id") Integer id);

    //So luong da ban (don da thanh toan)
    @Query(value = "SELECT ISNULL(SUM(c.soLuong), 0) FROM ChiTietThucPham c JOIN DonHang d ON d.id = c.donHangId "
            + "WHERE c.thucPhamId = :id AND d.trangThai = 'daThanhToan'", nativeQuery = true)
    long demDaBan(@Param("id") Integer id);

    //So don chua thanh toan dang co mon nay (gia cu van giu cho cac don do)
    @Query(value = "SELECT COUNT(*) FROM ChiTietThucPham c JOIN DonHang d ON d.id = c.donHangId "
            + "WHERE c.thucPhamId = :id AND d.trangThai = 'choThanhToan'", nativeQuery = true)
    long demDonDangCho(@Param("id") Integer id);
}
