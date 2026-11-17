package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

//Bang QuyTacGia: gia cua mot to hop loai ve x doi tuong x loai ngay x khung gio, co thoi gian hieu luc
@Entity
@Table(name = "QuyTacGia")
public class QuyTacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "loaiVeId")
    private LoaiVe loaiVe;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "doiTuongId")
    private DoiTuong doiTuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiNgay", nullable = false, length = 10)
    private LoaiNgay loaiNgay;

    @Enumerated(EnumType.STRING)
    @Column(name = "khungGio", nullable = false, length = 5)
    private KhungGio khungGio;

    @Column(name = "gia", nullable = false, precision = 10, scale = 2)
    private BigDecimal gia;

    @Column(name = "ngayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    //null = ap dung khong thoi han
    @Column(name = "ngayKetThuc")
    private LocalDate ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 12)
    private TrangThaiApDung trangThai = TrangThaiApDung.dangApDung;

    public String getMaQuyTac() {
        return id == null ? "" : String.format("QG%04d", id);
    }

    //Mo ta ngan de hien trong danh sach chon: "Thường · Người lớn · Cuối tuần · Tối"
    public String getMoTaNgan() {
        return loaiVe.getTenLoaiVe() + " · " + doiTuong.getTenDoiTuong() + " · " + loaiNgay.getNhan() + " · " + khungGio.getNhan();
    }

    //Quy tac co hieu luc vao ngay d (dang ap dung va ngay nam trong khoang)
    public boolean hieuLucVao(LocalDate d) {
        return trangThai == TrangThaiApDung.dangApDung && !ngayBatDau.isAfter(d)
                && (ngayKetThuc == null || !ngayKetThuc.isBefore(d));
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LoaiVe getLoaiVe() { return loaiVe; }
    public void setLoaiVe(LoaiVe loaiVe) { this.loaiVe = loaiVe; }
    public DoiTuong getDoiTuong() { return doiTuong; }
    public void setDoiTuong(DoiTuong doiTuong) { this.doiTuong = doiTuong; }
    public LoaiNgay getLoaiNgay() { return loaiNgay; }
    public void setLoaiNgay(LoaiNgay loaiNgay) { this.loaiNgay = loaiNgay; }
    public KhungGio getKhungGio() { return khungGio; }
    public void setKhungGio(KhungGio khungGio) { this.khungGio = khungGio; }
    public BigDecimal getGia() { return gia; }
    public void setGia(BigDecimal gia) { this.gia = gia; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public TrangThaiApDung getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiApDung trangThai) { this.trangThai = trangThai; }
}
