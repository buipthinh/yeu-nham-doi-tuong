package vn.rapphuongnam.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.rapphuongnam.entity.KhungGio;
import vn.rapphuongnam.entity.LoaiNgay;
import vn.rapphuongnam.entity.TrangThaiApDung;

import java.math.BigDecimal;
import java.time.LocalDate;

//Form thiet lap quy tac gia (UC5.1.1) va sua quy tac gia (UC5.1.2).
//Khi sua: khong doi loai ve, doi tuong, loai ngay, khung gio (muon doi thi tao quy tac moi).
public class QuyTacGiaForm {
    private Integer id;

    @NotNull(message = "Chọn loại vé")
    private Integer loaiVeId;

    @NotNull(message = "Chọn đối tượng")
    private Integer doiTuongId;

    @NotNull(message = "Chọn loại ngày")
    private LoaiNgay loaiNgay;

    @NotNull(message = "Chọn khung giờ")
    private KhungGio khungGio;

    @NotNull(message = "Nhập giá vé")
    @DecimalMin(value = "0", inclusive = false, message = "Giá vé phải lớn hơn 0")
    @Digits(integer = 8, fraction = 2, message = "Giá vé không hợp lệ")
    private BigDecimal gia;

    @NotNull(message = "Chọn ngày bắt đầu áp dụng")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDau = LocalDate.now();

    //De trong = ap dung khong thoi han
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThuc;

    //Chi dung khi sua
    private TrangThaiApDung trangThai = TrangThaiApDung.dangApDung;

    //Hien thi khi sua
    private String moTaNgan;

    public boolean isThemMoi() { return id == null; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getLoaiVeId() { return loaiVeId; }
    public void setLoaiVeId(Integer loaiVeId) { this.loaiVeId = loaiVeId; }
    public Integer getDoiTuongId() { return doiTuongId; }
    public void setDoiTuongId(Integer doiTuongId) { this.doiTuongId = doiTuongId; }
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
    public String getMoTaNgan() { return moTaNgan; }
    public void setMoTaNgan(String moTaNgan) { this.moTaNgan = moTaNgan; }
}
