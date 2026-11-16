package vn.rapphuongnam.entity;

//Gia tri luu trong cot PhongChieu.trangThai
public enum TrangThaiPhong {
    hoatDong("Hoạt động"),
    baoTri("Bảo trì"),
    an("Ẩn");

    private final String nhan;

    TrangThaiPhong(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }
}
