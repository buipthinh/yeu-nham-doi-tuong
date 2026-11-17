package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Bang LichSuGia: moi lan thiet lap / sua / ngung ap dung quy tac gia ghi mot dong (UC5.1.3)
@Entity
@Table(name = "LichSuGia")
public class LichSuGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "quyTacGiaId")
    private QuyTacGia quyTacGia;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "nguoiThucHienId")
    private TaiKhoan nguoiThucHien;

    @Enumerated(EnumType.STRING)
    @Column(name = "thaoTac", nullable = false, length = 12)
    private ThaoTacGia thaoTac;

    @Column(name = "giaCu", precision = 10, scale = 2)
    private BigDecimal giaCu;

    @Column(name = "giaMoi", nullable = false, precision = 10, scale = 2)
    private BigDecimal giaMoi;

    @Column(name = "thoiDiem", nullable = false)
    private LocalDateTime thoiDiem = LocalDateTime.now();

    public Integer getId() { return id; }
    public QuyTacGia getQuyTacGia() { return quyTacGia; }
    public void setQuyTacGia(QuyTacGia quyTacGia) { this.quyTacGia = quyTacGia; }
    public TaiKhoan getNguoiThucHien() { return nguoiThucHien; }
    public void setNguoiThucHien(TaiKhoan nguoiThucHien) { this.nguoiThucHien = nguoiThucHien; }
    public ThaoTacGia getThaoTac() { return thaoTac; }
    public void setThaoTac(ThaoTacGia thaoTac) { this.thaoTac = thaoTac; }
    public BigDecimal getGiaCu() { return giaCu; }
    public void setGiaCu(BigDecimal giaCu) { this.giaCu = giaCu; }
    public BigDecimal getGiaMoi() { return giaMoi; }
    public void setGiaMoi(BigDecimal giaMoi) { this.giaMoi = giaMoi; }
    public LocalDateTime getThoiDiem() { return thoiDiem; }
    public void setThoiDiem(LocalDateTime thoiDiem) { this.thoiDiem = thoiDiem; }
}
