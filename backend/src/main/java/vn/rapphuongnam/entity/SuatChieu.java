package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//Bang SuatChieu
@Entity
@Table(name = "SuatChieu")
public class SuatChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phimId")
    private Phim phim;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phongChieuId")
    private PhongChieu phongChieu;

    @Column(name = "ngayChieu", nullable = false)
    private LocalDate ngayChieu;

    @Column(name = "gioBatDau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gioKetThuc", nullable = false)
    private LocalTime gioKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 10)
    private TrangThaiSuatChieu trangThai = TrangThaiSuatChieu.moBan;

    public String getMaSuat() {
        return id == null ? "" : String.format("S%04d", id);
    }

    public LocalDateTime batDauLuc() { return LocalDateTime.of(ngayChieu, gioBatDau); }
    public LocalDateTime ketThucLuc() { return LocalDateTime.of(ngayChieu, gioKetThuc); }
    public boolean daBatDau(LocalDateTime now) { return !batDauLuc().isAfter(now); }
    public boolean daKetThuc(LocalDateTime now) { return !ketThucLuc().isAfter(now); }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Phim getPhim() { return phim; }
    public void setPhim(Phim phim) { this.phim = phim; }
    public PhongChieu getPhongChieu() { return phongChieu; }
    public void setPhongChieu(PhongChieu phongChieu) { this.phongChieu = phongChieu; }
    public LocalDate getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(LocalDate ngayChieu) { this.ngayChieu = ngayChieu; }
    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }
    public LocalTime getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }
    public TrangThaiSuatChieu getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiSuatChieu trangThai) { this.trangThai = trangThai; }
}
