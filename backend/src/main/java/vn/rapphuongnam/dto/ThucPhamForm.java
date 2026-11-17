package vn.rapphuongnam.dto;

import jakarta.validation.constraints.*;
import vn.rapphuongnam.entity.LoaiThucPham;

import java.math.BigDecimal;

//Form them mon (UC6.1.1) va sua mon (UC6.1.2, UC6.1.3).
//Khi sua khong doi ton kho tai day (dung chuc nang nhap hang / dieu chinh de co lich su).
public class ThucPhamForm {
    private Integer id;

    @NotBlank(message = "Tên món không được để trống")
    @Size(max = 100, message = "Tên món tối đa 100 ký tự")
    private String tenMon;

    @NotNull(message = "Chọn loại")
    private LoaiThucPham loai = LoaiThucPham.bap;

    @Size(max = 255, message = "Thành phần tối đa 255 ký tự")
    private String thanhPhan;

    @NotNull(message = "Nhập giá bán")
    @DecimalMin(value = "0", inclusive = false, message = "Giá bán phải lớn hơn 0")
    @Digits(integer = 8, fraction = 2, message = "Giá bán không hợp lệ")
    private BigDecimal giaBan;

    @Size(max = 255, message = "Đường dẫn ảnh tối đa 255 ký tự")
    private String hinhAnh;

    //Chi dung khi them moi
    @Min(value = 0, message = "Tồn kho không được âm")
    @Max(value = 1000000, message = "Tồn kho quá lớn")
    private Integer tonKho = 0;

    public boolean isThemMoi() { return id == null; }

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
}
