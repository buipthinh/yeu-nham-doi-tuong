package vn.rapphuongnam.dto;

import vn.rapphuongnam.entity.TrangThaiDonHang;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Mot don hang trong danh sach (UC6.2.1) va ket qua tra cuu (UC6.2.2), kem giao dich thanh toan gan nhat
public class DonHangDong {
    private Integer id;
    private String maVe;
    private Integer taiKhoanId;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String tenPhim;
    private String tenPhong;
    private LocalDate ngayChieu;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private int soVe;
    private BigDecimal tongTienVe;
    private BigDecimal tongTienThucPham;
    private BigDecimal tongTien;
    private TrangThaiDonHang trangThai;
    private LocalDateTime thoiGianDat;
    private LocalDateTime hanGiuGhe;
    //Giao dich: uu tien giao dich thanh cong, neu khong co thi lay lan gan nhat
    private String phuongThuc;
    private String maGiaoDichCong;
    private BigDecimal soTienThanhToan;
    private String trangThaiGiaoDich;
    private LocalDateTime thoiGianThanhToan;

    public static String tenPhuongThuc(String pt) {
        if (pt == null) return null;
        return switch (pt) {
            case "momo" -> "Ví MoMo";
            case "vnpay" -> "VNPay";
            case "theTinDung" -> "Thẻ tín dụng";
            default -> pt;
        };
    }

    public String getTenPhuongThuc() { return tenPhuongThuc(phuongThuc); }

    public boolean isGiaoDichThanhCong() { return "thanhCong".equals(trangThaiGiaoDich); }

    //So giay con lai cua thoi gian giu ghe, chi co y nghia voi don cho thanh toan
    public long getConLaiGiay() {
        if (trangThai != TrangThaiDonHang.choThanhToan || hanGiuGhe == null) return 0;
        return Math.max(0, Duration.between(LocalDateTime.now(), hanGiuGhe).getSeconds());
    }

    public String getConLai() {
        long s = getConLaiGiay();
        return String.format("%02d:%02d", s / 60, s % 60);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }
    public Integer getTaiKhoanId() { return taiKhoanId; }
    public void setTaiKhoanId(Integer taiKhoanId) { this.taiKhoanId = taiKhoanId; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public LocalDate getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(LocalDate ngayChieu) { this.ngayChieu = ngayChieu; }
    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }
    public LocalTime getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }
    public int getSoVe() { return soVe; }
    public void setSoVe(int soVe) { this.soVe = soVe; }
    public BigDecimal getTongTienVe() { return tongTienVe; }
    public void setTongTienVe(BigDecimal tongTienVe) { this.tongTienVe = tongTienVe; }
    public BigDecimal getTongTienThucPham() { return tongTienThucPham; }
    public void setTongTienThucPham(BigDecimal tongTienThucPham) { this.tongTienThucPham = tongTienThucPham; }
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    public TrangThaiDonHang getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiDonHang trangThai) { this.trangThai = trangThai; }
    public LocalDateTime getThoiGianDat() { return thoiGianDat; }
    public void setThoiGianDat(LocalDateTime thoiGianDat) { this.thoiGianDat = thoiGianDat; }
    public LocalDateTime getHanGiuGhe() { return hanGiuGhe; }
    public void setHanGiuGhe(LocalDateTime hanGiuGhe) { this.hanGiuGhe = hanGiuGhe; }
    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
    public String getMaGiaoDichCong() { return maGiaoDichCong; }
    public void setMaGiaoDichCong(String maGiaoDichCong) { this.maGiaoDichCong = maGiaoDichCong; }
    public BigDecimal getSoTienThanhToan() { return soTienThanhToan; }
    public void setSoTienThanhToan(BigDecimal soTienThanhToan) { this.soTienThanhToan = soTienThanhToan; }
    public String getTrangThaiGiaoDich() { return trangThaiGiaoDich; }
    public void setTrangThaiGiaoDich(String trangThaiGiaoDich) { this.trangThaiGiaoDich = trangThaiGiaoDich; }
    public LocalDateTime getThoiGianThanhToan() { return thoiGianThanhToan; }
    public void setThoiGianThanhToan(LocalDateTime thoiGianThanhToan) { this.thoiGianThanhToan = thoiGianThanhToan; }
}
