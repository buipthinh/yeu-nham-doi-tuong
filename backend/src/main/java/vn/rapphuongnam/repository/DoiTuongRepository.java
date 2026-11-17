package vn.rapphuongnam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.rapphuongnam.entity.DoiTuong;

import java.util.List;
import java.util.Optional;

public interface DoiTuongRepository extends JpaRepository<DoiTuong, Integer> {

    List<DoiTuong> findAllByOrderByIdAsc();

    Optional<DoiTuong> findFirstByTenDoiTuongIgnoreCase(String tenDoiTuong);

    boolean existsByTenDoiTuongIgnoreCase(String tenDoiTuong);
}
