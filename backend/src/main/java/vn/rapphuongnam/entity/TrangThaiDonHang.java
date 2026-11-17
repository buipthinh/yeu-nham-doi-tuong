package vn.rapphuongnam.entity;

//DonHang.trangThai
public enum TrangThaiDonHang {
    choThanhToan("Chờ thanh toán", "info"),
    daThanhToan("Đã thanh toán", "ok"),
    daHuy("Đã hủy", "bad"),
    hetHan("Hết hạn", "mute");

    private final String nhan;
    private final String css;

    TrangThaiDonHang(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
