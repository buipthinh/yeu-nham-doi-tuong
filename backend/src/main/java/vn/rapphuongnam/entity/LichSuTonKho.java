package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//Bang LichSuTonKho: ban hang (lyDo = ban, co donHangId) hoac nhap hang / dieu chinh (co nguoi thuc hien)
@Entity
@Table(name = "LichSuTonKho")
public class LichSuTonKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "thucPhamId")
    private ThucPham thucPham;

    @Column(name = "donHangId")
    private Integer donHangId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nguoiThucHienId")
    private TaiKhoan nguoiThucHien;

    @Column(name = "soLuongThayDoi", nullable = false)
    private Integer soLuongThayDoi;

    @Column(name = "tonKhoSau", nullable = false)
    private Integer tonKhoSau;

    @Enumerated(EnumType.STRING)
    @Column(name = "lyDo", nullable = false, length = 10)
    private LyDoTonKho lyDo;

    @Column(name = "thoiDiem", nullable = false)
    private LocalDateTime thoiDiem = LocalDateTime.now();

    public Integer getId() { return id; }
    public ThucPham getThucPham() { return thucPham; }
    public void setThucPham(ThucPham thucPham) { this.thucPham = thucPham; }
    public Integer getDonHangId() { return donHangId; }
    public void setDonHangId(Integer donHangId) { this.donHangId = donHangId; }
    public TaiKhoan getNguoiThucHien() { return nguoiThucHien; }
    public void setNguoiThucHien(TaiKhoan nguoiThucHien) { this.nguoiThucHien = nguoiThucHien; }
    public Integer getSoLuongThayDoi() { return soLuongThayDoi; }
    public void setSoLuongThayDoi(Integer soLuongThayDoi) { this.soLuongThayDoi = soLuongThayDoi; }
    public Integer getTonKhoSau() { return tonKhoSau; }
    public void setTonKhoSau(Integer tonKhoSau) { this.tonKhoSau = tonKhoSau; }
    public LyDoTonKho getLyDo() { return lyDo; }
    public void setLyDo(LyDoTonKho lyDo) { this.lyDo = lyDo; }
    public LocalDateTime getThoiDiem() { return thoiDiem; }
    public void setThoiDiem(LocalDateTime thoiDiem) { this.thoiDiem = thoiDiem; }
}
