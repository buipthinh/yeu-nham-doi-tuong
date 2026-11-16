package vn.rapphuongnam.entity;

//Gia tri luu trong cot SuatChieu.trangThai
public enum TrangThaiSuatChieu {
    moBan("Mở bán", "ok"),
    tamNgung("Tạm ngưng", "mute");

    private final String nhan;
    private final String css;

    TrangThaiSuatChieu(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
