package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.rapphuongnam.entity.LichSuGia;

import java.time.LocalDateTime;
import java.util.List;

public interface LichSuGiaRepository extends JpaRepository<LichSuGia, Integer> {

    //UC5.1.3 lich su gia: loc theo loai ve hoac mot quy tac, trong khoang thoi gian; moi nhat truoc
    @Query("select l from LichSuGia l where (:loaiVeId is null or l.quyTacGia.loaiVe.id = :loaiVeId) "
            + "and (:quyTacGiaId is null or l.quyTacGia.id = :quyTacGiaId) "
            + "and (:tu is null or l.thoiDiem >= :tu) and (:den is null or l.thoiDiem < :den)")
    Page<LichSuGia> timKiem(@Param("loaiVeId") Integer loaiVeId,
                            @Param("quyTacGiaId") Integer quyTacGiaId,
                            @Param("tu") LocalDateTime tu,
                            @Param("den") LocalDateTime den,
                            Pageable pageable);

    List<LichSuGia> findByQuyTacGia_IdOrderByThoiDiemDescIdDesc(Integer quyTacGiaId);
}
