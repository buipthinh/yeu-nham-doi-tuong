package vn.rapphuongnam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Mot lan thanh toan cua don hang (bang GiaoDich)
public class GiaoDichDong {
    private final String phuongThuc;
    private final String maGiaoDichCong;
    private final BigDecimal soTien;
    private final String trangThai;
    private final LocalDateTime thoiGian;

    public GiaoDichDong(String phuongThuc, String maGiaoDichCong, BigDecimal soTien, String trangThai, LocalDateTime thoiGian) {
        this.phuongThuc = phuongThuc;
        this.maGiaoDichCong = maGiaoDichCong;
        this.soTien = soTien;
        this.trangThai = trangThai;
        this.thoiGian = thoiGian;
    }

    public String getTenPhuongThuc() { return DonHangDong.tenPhuongThuc(phuongThuc); }
    public boolean isThanhCong() { return "thanhCong".equals(trangThai); }

    public String getPhuongThuc() { return phuongThuc; }
    public String getMaGiaoDichCong() { return maGiaoDichCong; }
    public BigDecimal getSoTien() { return soTien; }
    public String getTrangThai() { return trangThai; }
    public LocalDateTime getThoiGian() { return thoiGian; }
}
