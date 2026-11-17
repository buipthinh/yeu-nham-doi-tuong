package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

//Bang ThucPham: bap, nuoc, combo ban kem ve (UC6.1)
@Entity
@Table(name = "ThucPham")
public class ThucPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenMon", nullable = false, length = 100)
    private String tenMon;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai", nullable = false, length = 5)
    private LoaiThucPham loai;

    @Column(name = "thanhPhan", length = 255)
    private String thanhPhan;

    @Column(name = "giaBan", nullable = false, precision = 10, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "hinhAnh", length = 255)
    private String hinhAnh;

    //Chi doi qua chuc nang nhap hang / dieu chinh ton kho (co ghi LichSuTonKho)
    @Column(name = "tonKho", nullable = false)
    private Integer tonKho = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 8)
    private TrangThaiThucPham trangThai = TrangThaiThucPham.dangBan;

    public String getMaMon() {
        return id == null ? "" : String.format("TP%03d", id);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public LoaiThucPham getLoai() { return loai; }
    public void setLoai(LoaiThucPham loai) { this.loai = loai; }
    public String getThanhPhan() { return thanhPhan; }
    public void setThanhPhan(String thanhPhan) { this.thanhPhan = thanhPhan; }
    public BigDecimal getGiaBan() { return giaBan; }
    public void setGiaBan(BigDecimal giaBan) { this.giaBan = giaBan; }
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
    public Integer getTonKho() { return tonKho; }
    public void setTonKho(Integer tonKho) { this.tonKho = tonKho; }
    public TrangThaiThucPham getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiThucPham trangThai) { this.trangThai = trangThai; }
}
