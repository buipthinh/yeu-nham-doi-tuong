package vn.rapphuongnam.dto;

import vn.rapphuongnam.entity.Phim;
import vn.rapphuongnam.entity.TheLoai;

import java.time.LocalDate;
import java.util.List;

//Du lieu phim tra ve cho trang khach hang (JSON)
public record PhimDto(Integer id, String tenPhim, String daoDien, String dienVien, Integer thoiLuong,
                      String ngonNgu, String phuDe, LocalDate ngayKhoiChieu, LocalDate ngayKetThuc,
                      String doTuoi, String moTa, String poster, String trailer, String trangThai,
                      List<String> theLoai) {

    public static PhimDto tu(Phim p) {
        return new PhimDto(p.getId(), p.getTenPhim(), p.getDaoDien(), p.getDienVien(), p.getThoiLuong(),
                p.getNgonNgu(), p.getPhuDe(), p.getNgayKhoiChieu(), p.getNgayKetThuc(),
                p.getDoTuoi().name(), p.getMoTa(), p.getPoster(), p.getTrailer(), p.getTrangThai().name(),
                p.getTheLoai().stream().map(TheLoai::getTenTheLoai).toList());
    }
}
