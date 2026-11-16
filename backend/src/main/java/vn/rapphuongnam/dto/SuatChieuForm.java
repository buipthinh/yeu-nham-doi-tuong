package vn.rapphuongnam.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

//Du lieu tren form them/sua suat chieu (UC3.1, UC3.2)
public class SuatChieuForm {
    private Integer id;

    @NotNull(message = "Chọn phim")
    private Integer phimId;

    @NotNull(message = "Chọn phòng")
    private Integer phongChieuId;

    @NotNull(message = "Chọn ngày chiếu")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayChieu;

    @NotNull(message = "Nhập giờ bắt đầu")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime gioBatDau;

    //false: buoc 1 (kiem tra + xem truoc), true: buoc 2 (luu)
    private boolean xacNhan;

    public boolean isThemMoi() { return id == null; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPhimId() { return phimId; }
    public void setPhimId(Integer phimId) { this.phimId = phimId; }
    public Integer getPhongChieuId() { return phongChieuId; }
    public void setPhongChieuId(Integer phongChieuId) { this.phongChieuId = phongChieuId; }
    public LocalDate getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(LocalDate ngayChieu) { this.ngayChieu = ngayChieu; }
    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }
    public boolean isXacNhan() { return xacNhan; }
    public void setXacNhan(boolean xacNhan) { this.xacNhan = xacNhan; }
}
