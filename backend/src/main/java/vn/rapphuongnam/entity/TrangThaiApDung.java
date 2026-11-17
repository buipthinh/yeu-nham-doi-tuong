package vn.rapphuongnam.entity;

//Trang thai cua LoaiVe va QuyTacGia
public enum TrangThaiApDung {
    dangApDung("Đang áp dụng", "ok"),
    ngungApDung("Ngừng áp dụng", "mute");

    private final String nhan;
    private final String css;

    TrangThaiApDung(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
