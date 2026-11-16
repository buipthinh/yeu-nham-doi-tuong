package vn.rapphuongnam.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

//Bang Phim, lien ket n-n voi TheLoai qua bang PhimTheLoai
@Entity
@Table(name = "Phim")
public class Phim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenPhim", nullable = false, length = 200)
    private String tenPhim;

    @Column(name = "daoDien", length = 100)
    private String daoDien;

    @Column(name = "dienVien", length = 255)
    private String dienVien;

    @Column(name = "thoiLuong", nullable = false)
    private Integer thoiLuong;

    @Column(name = "ngonNgu", length = 50)
    private String ngonNgu;

    @Column(name = "phuDe", length = 50)
    private String phuDe;

    @Column(name = "ngayKhoiChieu", nullable = false)
    private LocalDate ngayKhoiChieu;

    @Column(name = "ngayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "doTuoi", nullable = false, length = 3)
    private DoTuoi doTuoi;

    @Column(name = "moTa")
    private String moTa;

    @Column(name = "poster", length = 255)
    private String poster;

    @Column(name = "trailer", length = 255)
    private String trailer;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 10)
    private TrangThaiPhim trangThai = TrangThaiPhim.sapChieu;

    //Danh sach the loai it nen lay luon (EAGER) cho de hien thi
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PhimTheLoai",
            joinColumns = @JoinColumn(name = "phimId"),
            inverseJoinColumns = @JoinColumn(name = "theLoaiId"))
    private Set<TheLoai> theLoai = new LinkedHashSet<>();

    //Chuoi the loai de hien thi: "Hành động, Hài"
    public String getTenCacTheLoai() {
        return theLoai.stream()
                .sorted(Comparator.comparing(TheLoai::getId))
                .map(TheLoai::getTenTheLoai)
                .collect(Collectors.joining(", "));
    }

    public String getMaPhim() {
        return id == null ? "" : String.format("P%04d", id);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public String getDaoDien() { return daoDien; }
    public void setDaoDien(String daoDien) { this.daoDien = daoDien; }
    public String getDienVien() { return dienVien; }
    public void setDienVien(String dienVien) { this.dienVien = dienVien; }
    public Integer getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(Integer thoiLuong) { this.thoiLuong = thoiLuong; }
    public String getNgonNgu() { return ngonNgu; }
    public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    public String getPhuDe() { return phuDe; }
    public void setPhuDe(String phuDe) { this.phuDe = phuDe; }
    public LocalDate getNgayKhoiChieu() { return ngayKhoiChieu; }
    public void setNgayKhoiChieu(LocalDate ngayKhoiChieu) { this.ngayKhoiChieu = ngayKhoiChieu; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public DoTuoi getDoTuoi() { return doTuoi; }
    public void setDoTuoi(DoTuoi doTuoi) { this.doTuoi = doTuoi; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getTrailer() { return trailer; }
    public void setTrailer(String trailer) { this.trailer = trailer; }
    public TrangThaiPhim getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiPhim trangThai) { this.trangThai = trangThai; }
    public Set<TheLoai> getTheLoai() { return theLoai; }
    public void setTheLoai(Set<TheLoai> theLoai) { this.theLoai = theLoai; }
}
