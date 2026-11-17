package vn.rapphuongnam.dto;

import org.springframework.format.annotation.DateTimeFormat;
import vn.rapphuongnam.entity.TrangThaiDonHang;

import java.time.LocalDate;

//Dieu kien loc danh sach don hang (UC6.2.1), gan tu query string
public class DonHangLoc {
    private String q;
    private TrangThaiDonHang trangThai;
    private Integer phimId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgay;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgay;
    private int trang;

    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    public TrangThaiDonHang getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiDonHang trangThai) { this.trangThai = trangThai; }
    public Integer getPhimId() { return phimId; }
    public void setPhimId(Integer phimId) { this.phimId = phimId; }
    public LocalDate getTuNgay() { return tuNgay; }
    public void setTuNgay(LocalDate tuNgay) { this.tuNgay = tuNgay; }
    public LocalDate getDenNgay() { return denNgay; }
    public void setDenNgay(LocalDate denNgay) { this.denNgay = denNgay; }
    public int getTrang() { return trang; }
    public void setTrang(int trang) { this.trang = trang; }
}
