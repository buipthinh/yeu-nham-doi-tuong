package vn.rapphuongnam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.LoaiVe;
import vn.rapphuongnam.entity.TrangThaiApDung;

import java.util.List;

public interface LoaiVeRepository extends JpaRepository<LoaiVe, Integer> {

    List<LoaiVe> findAllByOrderByIdAsc();

    List<LoaiVe> findByTrangThaiOrderByIdAsc(TrangThaiApDung trangThai);

    //Kiem tra trung ten (id = 0 khi them moi)
    boolean existsByTenLoaiVeIgnoreCaseAndIdNot(String tenLoaiVe, Integer id);

    //So ghe vat ly dang dung loai ve nay (UC5.2.2: co ghe thi khong doi so cho)
    @Query(value = "SELECT COUNT(*) FROM Ghe WHERE loaiVeId = :id", nativeQuery = true)
    long demGhe(@Param("id") Integer id);

    //So ghe thuoc phong dang hoat dong (UC5.2.3: con ghe thi khong ngung ap dung)
    @Query(value = "SELECT COUNT(*) FROM Ghe g JOIN PhongChieu p ON p.id = g.phongChieuId "
            + "WHERE g.loaiVeId = :id AND p.trangThai = 'hoatDong'", nativeQuery = true)
    long demGhePhongHoatDong(@Param("id") Integer id);

    @Query(value = "SELECT COUNT(*) FROM Ve WHERE loaiVeId = :id", nativeQuery = true)
    long demVeDaBan(@Param("id") Integer id);
}
