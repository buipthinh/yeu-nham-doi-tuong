package vn.rapphuongnam.dto;

import jakarta.validation.constraints.*;

//Form them loai ve (UC5.2.1) va sua loai ve (UC5.2.2)
public class LoaiVeForm {
    private Integer id;

    @NotBlank(message = "Tên loại vé không được để trống")
    @Size(max = 50, message = "Tên loại vé tối đa 50 ký tự")
    private String tenLoaiVe;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    private String moTa;

    @NotNull(message = "Chọn số chỗ mỗi vé")
    @Min(value = 1, message = "Số chỗ mỗi vé chỉ là 1 hoặc 2")
    @Max(value = 2, message = "Số chỗ mỗi vé chỉ là 1 hoặc 2")
    private Integer soChoMoiVe = 1;

    public boolean isThemMoi() { return id == null; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenLoaiVe() { return tenLoaiVe; }
    public void setTenLoaiVe(String tenLoaiVe) { this.tenLoaiVe = tenLoaiVe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Integer getSoChoMoiVe() { return soChoMoiVe; }
    public void setSoChoMoiVe(Integer soChoMoiVe) { this.soChoMoiVe = soChoMoiVe; }
}
