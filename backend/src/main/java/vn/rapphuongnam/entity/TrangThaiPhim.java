package vn.rapphuongnam.entity;

//Gia tri luu trong cot Phim.trangThai
public enum TrangThaiPhim {
    sapChieu("Sắp chiếu", "info"),
    dangChieu("Đang chiếu", "ok"),
    an("Ẩn", "mute");

    private final String nhan;
    private final String css;

    TrangThaiPhim(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
