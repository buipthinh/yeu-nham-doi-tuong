package vn.rapphuongnam.entity;

//Tinh trang hien thi cua suat chieu (tinh ra luc xem, khong luu DB)
public enum TinhTrangSuat {
    conVe("Còn vé", "ok"),
    hetVe("Hết vé", "warn"),
    tamNgung("Tạm ngưng", "mute"),
    daKetThuc("Đã kết thúc", "mute");

    private final String nhan;
    private final String css;

    TinhTrangSuat(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
