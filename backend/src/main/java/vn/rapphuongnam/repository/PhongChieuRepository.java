package vn.rapphuongnam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.rapphuongnam.entity.PhongChieu;

import java.util.List;

public interface PhongChieuRepository extends JpaRepository<PhongChieu, Integer> {

    //Phong hoat dong va co it nhat 1 ghe hoat dong
    @Query(value = "SELECT p.* FROM PhongChieu p WHERE p.trangThai = 'hoatDong' "
            + "AND EXISTS (SELECT 1 FROM Ghe g WHERE g.phongChieuId = p.id AND g.trangThai = 'hoatDong') ORDER BY p.tenPhong",
            nativeQuery = true)
    List<PhongChieu> phongDungDuoc();

}
