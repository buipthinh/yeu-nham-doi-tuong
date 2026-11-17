package vn.rapphuongnam.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.rapphuongnam.entity.GioiTinh;
import vn.rapphuongnam.entity.LoaiTaiKhoan;

import java.time.LocalDate;

//Du lieu tren form them tai khoan noi bo (UC8.1) va sua thong tin (UC8.3).
//Khi sua: khong doi ma tai khoan, email, chuc vu (doi chuc vu o UC8.6).
public class TaiKhoanForm {
    private Integer id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String hoTen;

    //Chi dung khi them moi: admin hoac nhanVien
    private LoaiTaiKhoan loaiTaiKhoan = LoaiTaiKhoan.nhanVien;

    @Past(message = "Ngày sinh phải trước ngày hiện tại")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;

    @NotNull(message = "Chọn giới tính")
    private GioiTinh gioiTinh = GioiTinh.nam;

    @NotBlank(message = "Nhập số điện thoại")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0")
    private String soDienThoai;

    //Chi kiem tra khi them moi (xem TaiKhoanService.kiemTra)
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String diaChi;

    @PastOrPresent(message = "Ngày tuyển dụng không được sau ngày hiện tại")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayTuyenDung;

    //Hien thi khi sua (khong cho sua)
    private String maTaiKhoan;
    private boolean noiBo = true;

    public boolean isThemMoi() { return id == null; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public LoaiTaiKhoan getLoaiTaiKhoan() { return loaiTaiKhoan; }
    public void setLoaiTaiKhoan(LoaiTaiKhoan loaiTaiKhoan) { this.loaiTaiKhoan = loaiTaiKhoan; }
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
    public String getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(String maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }
    public boolean isNoiBo() { return noiBo; }
    public void setNoiBo(boolean noiBo) { this.noiBo = noiBo; }
}
