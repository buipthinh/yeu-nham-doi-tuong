package vn.rapphuongnam.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Mot dong don hang trong trang chi tiet tai khoan khach hang (UC8.4)
public class DonHangKhach {
    private final Integer id;
    private final String maVe;
    private final String tenPhim;
    private final LocalDate ngayChieu;
    private final LocalTime gioBatDau;
    private final int soVe;
    private final BigDecimal tongTien;
    private final String trangThai;
    private final LocalDateTime thoiGianDat;

    public DonHangKhach(Integer id, String maVe, String tenPhim, LocalDate ngayChieu, LocalTime gioBatDau,
                        int soVe, BigDecimal tongTien, String trangThai, LocalDateTime thoiGianDat) {
        this.id = id;
        this.maVe = maVe;
        this.tenPhim = tenPhim;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.soVe = soVe;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.thoiGianDat = thoiGianDat;
    }

    public String getTenTrangThai() {
        return switch (trangThai) {
            case "choThanhToan" -> "Chờ thanh toán";
            case "daThanhToan" -> "Đã thanh toán";
            case "daHuy" -> "Đã hủy";
            case "hetHan" -> "Hết hạn";
            default -> trangThai;
        };
    }

    public String getCss() {
        return switch (trangThai) {
            case "daThanhToan" -> "ok";
            case "choThanhToan" -> "info";
            default -> "mute";
        };
    }

    public Integer getId() { return id; }
    public String getMaVe() { return maVe; }
    public String getTenPhim() { return tenPhim; }
    public LocalDate getNgayChieu() { return ngayChieu; }
    public LocalTime getGioBatDau() { return gioBatDau; }
    public int getSoVe() { return soVe; }
    public BigDecimal getTongTien() { return tongTien; }
    public String getTrangThai() { return trangThai; }
    public LocalDateTime getThoiGianDat() { return thoiGianDat; }
}
