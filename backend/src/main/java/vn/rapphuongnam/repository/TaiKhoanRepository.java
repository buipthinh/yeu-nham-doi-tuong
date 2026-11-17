package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.LoaiTaiKhoan;
import vn.rapphuongnam.entity.TaiKhoan;
import vn.rapphuongnam.entity.TrangThaiTaiKhoan;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    //UC8.7: tim theo ho ten, ma/ten dang nhap, email, so dien thoai; loc theo loai va trang thai
    @Query("select t from TaiKhoan t "
            + "where (:q is null or lower(t.hoTen) like lower(concat('%', :q, '%')) "
            + "or lower(t.tenDangNhap) like lower(concat('%', :q, '%')) "
            + "or lower(t.email) like lower(concat('%', :q, '%')) "
            + "or t.soDienThoai like concat('%', :q, '%')) "
            + "and (:loai is null or t.loaiTaiKhoan = :loai) "
            + "and (:trangThai is null or t.trangThai = :trangThai)")
    Page<TaiKhoan> timKiem(@Param("q") String q,
                           @Param("loai") LoaiTaiKhoan loai,
                           @Param("trangThai") TrangThaiTaiKhoan trangThai,
                           Pageable pageable);

    //Kiem tra trung (id = 0 khi them moi)
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);

    boolean existsBySoDienThoaiAndIdNot(String soDienThoai, Integer id);

    //Dem Admin con hoat dong: khong cho khoa / ha chuc vu Admin cuoi cung
    long countByLoaiTaiKhoanAndTrangThai(LoaiTaiKhoan loai, TrangThaiTaiKhoan trangThai);

    //Lay cac ma NV hien co de sinh ma tiep theo
    @Query("select t.tenDangNhap from TaiKhoan t where t.tenDangNhap like 'NV%'")
    List<String> layCacMaNhanVien();

    //Dung cho dang nhap (UC1)
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
}
