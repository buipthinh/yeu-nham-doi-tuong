package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//Mot moc thoi gian trong bao cao doanh thu (1 gio / 1 ngay / 1 thang tuy ky bao cao)
public class DoanhThuMoc {
    private String nhan;
    private BigDecimal doanhThuVe = BigDecimal.ZERO;
    private BigDecimal doanhThuThucPham = BigDecimal.ZERO;
    private BigDecimal tongDoanhThu = BigDecimal.ZERO;
    private int soDon;
    private int soVe;
    //Chieu cao cot tren bieu do (0 - 100)
    private int phanTram;

    public DoanhThuMoc() {
    }

    public DoanhThuMoc(String nhan) {
        this.nhan = nhan;
    }

    public void congDon(BigDecimal ve, BigDecimal thucPham, BigDecimal tong, int soVeDon) {
        doanhThuVe = doanhThuVe.add(ve);
        doanhThuThucPham = doanhThuThucPham.add(thucPham);
        tongDoanhThu = tongDoanhThu.add(tong);
        soDon++;
        soVe += soVeDon;
    }

    public void congMoc(DoanhThuMoc m) {
        doanhThuVe = doanhThuVe.add(m.doanhThuVe);
        doanhThuThucPham = doanhThuThucPham.add(m.doanhThuThucPham);
        tongDoanhThu = tongDoanhThu.add(m.tongDoanhThu);
        soDon += m.soDon;
        soVe += m.soVe;
    }

    public String getNhan() { return nhan; }
    public void setNhan(String nhan) { this.nhan = nhan; }
    public BigDecimal getDoanhThuVe() { return doanhThuVe; }
    public BigDecimal getDoanhThuThucPham() { return doanhThuThucPham; }
    public BigDecimal getTongDoanhThu() { return tongDoanhThu; }
    public int getSoDon() { return soDon; }
    public int getSoVe() { return soVe; }
    public int getPhanTram() { return phanTram; }
    public void setPhanTram(int phanTram) { this.phanTram = phanTram; }
}
