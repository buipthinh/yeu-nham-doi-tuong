package vn.rapphuongnam.entity;

//LichSuGia.thaoTac
public enum ThaoTacGia {
    thietLap("Thiết lập", "ok"),
    sua("Sửa", "info"),
    ngungApDung("Ngừng áp dụng", "mute");

    private final String nhan;
    private final String css;

    ThaoTacGia(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
