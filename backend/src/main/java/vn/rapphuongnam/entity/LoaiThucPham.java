package vn.rapphuongnam.entity;

//ThucPham.loai
public enum LoaiThucPham {
    bap("Bắp", "info"),
    nuoc("Nước", "info"),
    combo("Combo", "ok");

    private final String nhan;
    private final String css;

    LoaiThucPham(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
