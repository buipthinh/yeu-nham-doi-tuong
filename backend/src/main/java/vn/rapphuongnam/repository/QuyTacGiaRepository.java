package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.*;

import java.util.List;

public interface QuyTacGiaRepository extends JpaRepository<QuyTacGia, Integer> {

    //UC5.1 danh sach quy tac gia, loc theo loai ve / doi tuong / loai ngay / khung gio / trang thai
    @Query("select q from QuyTacGia q where (:loaiVeId is null or q.loaiVe.id = :loaiVeId) "
            + "and (:doiTuongId is null or q.doiTuong.id = :doiTuongId) "
            + "and (:loaiNgay is null or q.loaiNgay = :loaiNgay) "
            + "and (:khungGio is null or q.khungGio = :khungGio) "
            + "and (:trangThai is null or q.trangThai = :trangThai)")
    Page<QuyTacGia> timKiem(@Param("loaiVeId") Integer loaiVeId,
                            @Param("doiTuongId") Integer doiTuongId,
                            @Param("loaiNgay") LoaiNgay loaiNgay,
                            @Param("khungGio") KhungGio khungGio,
                            @Param("trangThai") TrangThaiApDung trangThai,
                            Pageable pageable);

    //Cac quy tac cung to hop dang ap dung, dung de kiem tra chong lan thoi gian hieu luc
    List<QuyTacGia> findByLoaiVe_IdAndDoiTuong_IdAndLoaiNgayAndKhungGioAndTrangThai(
            Integer loaiVeId, Integer doiTuongId, LoaiNgay loaiNgay, KhungGio khungGio, TrangThaiApDung trangThai);

    List<QuyTacGia> findByLoaiVe_IdAndTrangThai(Integer loaiVeId, TrangThaiApDung trangThai);

    List<QuyTacGia> findByLoaiVe_IdAndDoiTuong_IdAndTrangThai(Integer loaiVeId, Integer doiTuongId, TrangThaiApDung trangThai);

    List<QuyTacGia> findByTrangThai(TrangThaiApDung trangThai);

    long countByLoaiVe_IdAndDoiTuong_Id(Integer loaiVeId, Integer doiTuongId);

    long countByLoaiVe_IdAndTrangThai(Integer loaiVeId, TrangThaiApDung trangThai);

    long countByTrangThai(TrangThaiApDung trangThai);

    //Danh sach chon quy tac o trang lich su gia
    List<QuyTacGia> findByLoaiVe_IdOrderByDoiTuong_IdAscLoaiNgayAscKhungGioAscIdDesc(Integer loaiVeId);
}
