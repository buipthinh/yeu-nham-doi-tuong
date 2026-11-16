package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//Mot dong trong bang gia cua suat chieu
public record GiaDong(String tenLoaiVe, String tenDoiTuong, BigDecimal gia) {
}
