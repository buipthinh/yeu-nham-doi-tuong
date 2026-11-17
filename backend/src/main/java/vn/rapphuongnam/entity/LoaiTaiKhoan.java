package vn.rapphuongnam.entity;

//Gia tri luu trong cot TaiKhoan.loaiTaiKhoan
public enum LoaiTaiKhoan {
    khachHang("Khách hàng", "mute"),
    nhanVien("Nhân viên", "info"),
    admin("Admin", "ok");

    private final String nhan;
    private final String css;

    LoaiTaiKhoan(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }

    //Tai khoan noi bo (Admin, Nhan vien) moi co chuc vu
    public boolean isNoiBo() { return this != khachHang; }
}
