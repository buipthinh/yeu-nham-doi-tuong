package vn.rapphuongnam.entity;

//LichSuTonKho.lyDo: ban do he thong ghi khi thanh toan, hai loai con lai do nhan vien nhap
public enum LyDoTonKho {
    nhapHang("Nhập hàng", "ok"),
    ban("Bán", "info"),
    dieuChinh("Điều chỉnh", "mute");

    private final String nhan;
    private final String css;

    LyDoTonKho(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
