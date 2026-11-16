package vn.rapphuongnam.dto;

import vn.rapphuongnam.entity.KhungGio;
import vn.rapphuongnam.entity.LoaiNgay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

//Du lieu man hinh xac nhan truoc khi luu suat chieu
public record XemTruocSuat(String tenPhim, String tenPhong, LocalDate ngay, LocalTime gioBatDau, LocalTime gioKetThuc,
                           LoaiNgay loaiNgay, KhungGio khungGio, BigDecimal giaTu, long soGheSeTao) {
}
