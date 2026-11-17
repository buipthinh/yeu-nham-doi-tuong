package vn.rapphuongnam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.rapphuongnam.entity.LichSuTonKho;

public interface LichSuTonKhoRepository extends JpaRepository<LichSuTonKho, Integer> {

    Page<LichSuTonKho> findByThucPham_Id(Integer thucPhamId, Pageable pageable);
}
