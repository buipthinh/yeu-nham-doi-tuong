package vn.rapphuongnam.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Bang TaiKhoan: dung chung cho khach hang, nhan vien, admin (phan biet bang loaiTaiKhoan).
//Phan dang nhap (UC1) cung dung entity nay.
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Nhan vien/Admin: ma NV0001...; khach hang: email
    @Column(name = "tenDangNhap", nullable = false, length = 100)
    private String tenDangNhap;

    //SHA-256 dang hex, xem util/MatKhauUtil. Khong bao gio hien ra giao dien.
    @Column(name = "matKhau", nullable = false, length = 255)
    private String matKhau;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiTaiKhoan", nullable = false, length = 10)
    private LoaiTaiKhoan loaiTaiKhoan;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioiTinh", nullable = false, length = 4)
    private GioiTinh gioiTinh = GioiTinh.khac;

    @Column(name = "soDienThoai", nullable = false, length = 10)
    private String soDienThoai;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "diaChi", length = 255)
    private String diaChi;

    //Chi tai khoan noi bo moi co (rang buoc CK_TaiKhoan_ngayTuyenDung)
    @Column(name = "ngayTuyenDung")
    private LocalDate ngayTuyenDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 10)
    private TrangThaiTaiKhoan trangThai = TrangThaiTaiKhoan.hoatDong;

    @Column(name = "soLanNhapSai", nullable = false)
    private Integer soLanNhapSai = 0;

    @Column(name = "khoaDen")
    private LocalDateTime khoaDen;

    @Column(name = "lanDangNhapCuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "ngayTao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    public boolean isNoiBo() {
        return loaiTaiKhoan != null && loaiTaiKhoan.isNoiBo();
    }

    public boolean isHoatDong() {
        return trangThai == TrangThaiTaiKhoan.hoatDong;
    }

    //Ma hien thi: nhan vien dung ma NV, khach hang dung KH + id
    public String getMaTaiKhoan() {
        if (id == null) return "";
        return isNoiBo() ? tenDangNhap : String.format("KH%04d", id);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public LoaiTaiKhoan getLoaiTaiKhoan() { return loaiTaiKhoan; }
    public void setLoaiTaiKhoan(LoaiTaiKhoan loaiTaiKhoan) { this.loaiTaiKhoan = loaiTaiKhoan; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    public GioiTinh getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(GioiTinh gioiTinh) { this.gioiTinh = gioiTinh; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public LocalDate getNgayTuyenDung() { return ngayTuyenDung; }
    public void setNgayTuyenDung(LocalDate ngayTuyenDung) { this.ngayTuyenDung = ngayTuyenDung; }
    public TrangThaiTaiKhoan getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiTaiKhoan trangThai) { this.trangThai = trangThai; }
    public Integer getSoLanNhapSai() { return soLanNhapSai; }
    public void setSoLanNhapSai(Integer soLanNhapSai) { this.soLanNhapSai = soLanNhapSai; }
    public LocalDateTime getKhoaDen() { return khoaDen; }
    public void setKhoaDen(LocalDateTime khoaDen) { this.khoaDen = khoaDen; }
    public LocalDateTime getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    public void setLanDangNhapCuoi(LocalDateTime lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
