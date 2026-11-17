package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//Mot dong bao cao muc do quan tam theo phim (UC9.2)
public class ThongKePhimDong {
    private Integer phimId;
    private String tenPhim;
    private int soSuatChieu;
    private int soVe;
    private int soDon;
    private int soGheDaBan;
    private int tongSoGhe;
    private BigDecimal doanhThuVe = BigDecimal.ZERO;
    private int phanTram;

    //Ty le lap day = ghe da ban / tong ghe cua cac suat chieu trong ky (%)
    public double getTyLeLapDay() {
        return tongSoGhe == 0 ? 0 : soGheDaBan * 100.0 / tongSoGhe;
    }

    public Integer getPhimId() { return phimId; }
    public void setPhimId(Integer phimId) { this.phimId = phimId; }
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public int getSoSuatChieu() { return soSuatChieu; }
    public void setSoSuatChieu(int soSuatChieu) { this.soSuatChieu = soSuatChieu; }
    public int getSoVe() { return soVe; }
    public void setSoVe(int soVe) { this.soVe = soVe; }
    public int getSoDon() { return soDon; }
    public void setSoDon(int soDon) { this.soDon = soDon; }
    public int getSoGheDaBan() { return soGheDaBan; }
    public void setSoGheDaBan(int soGheDaBan) { this.soGheDaBan = soGheDaBan; }
    public int getTongSoGhe() { return tongSoGhe; }
    public void setTongSoGhe(int tongSoGhe) { this.tongSoGhe = tongSoGhe; }
    public BigDecimal getDoanhThuVe() { return doanhThuVe; }
    public void setDoanhThuVe(BigDecimal doanhThuVe) { this.doanhThuVe = doanhThuVe; }
    public int getPhanTram() { return phanTram; }
    public void setPhanTram(int phanTram) { this.phanTram = phanTram; }
}
