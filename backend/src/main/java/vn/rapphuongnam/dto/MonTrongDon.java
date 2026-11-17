package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//Mot mon bap nuoc trong chi tiet don hang (don gia la gia luc dat)
public class MonTrongDon {
    private final Integer thucPhamId;
    private final String tenMon;
    private final int soLuong;
    private final BigDecimal donGia;

    public MonTrongDon(Integer thucPhamId, String tenMon, int soLuong, BigDecimal donGia) {
        this.thucPhamId = thucPhamId;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() { return donGia.multiply(BigDecimal.valueOf(soLuong)); }

    public Integer getThucPhamId() { return thucPhamId; }
    public String getTenMon() { return tenMon; }
    public int getSoLuong() { return soLuong; }
    public BigDecimal getDonGia() { return donGia; }
}
