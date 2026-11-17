package vn.rapphuongnam.entity;

//Gia tri luu trong cot TaiKhoan.trangThai
public enum TrangThaiTaiKhoan {
    hoatDong("Hoạt động", "ok"),
    biKhoa("Bị khóa", "bad");

    private final String nhan;
    private final String css;

    TrangThaiTaiKhoan(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
