package vn.rapphuongnam.khach;

import java.io.Serializable;

//Khach hang dang dang nhap o trang frontend (gan voi token)
public record KhachDangNhap(Integer id, String tenDangNhap, String hoTen) implements Serializable {
    public Integer getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getHoTen() { return hoTen; }
}
