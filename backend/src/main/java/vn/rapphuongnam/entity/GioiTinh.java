package vn.rapphuongnam.entity;

//Gia tri luu trong cot TaiKhoan.gioiTinh
public enum GioiTinh {
    nam("Nam"),
    nu("Nữ"),
    khac("Khác");

    private final String nhan;

    GioiTinh(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }
}
