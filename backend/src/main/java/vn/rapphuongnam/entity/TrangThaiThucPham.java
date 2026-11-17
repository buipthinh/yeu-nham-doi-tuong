package vn.rapphuongnam.entity;

//ThucPham.trangThai
public enum TrangThaiThucPham {
    dangBan("Đang bán", "ok"),
    ngungBan("Ngừng bán", "mute");

    private final String nhan;
    private final String css;

    TrangThaiThucPham(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
