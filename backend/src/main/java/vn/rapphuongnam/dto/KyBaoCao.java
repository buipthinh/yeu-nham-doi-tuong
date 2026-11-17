package vn.rapphuongnam.dto;

//Ky bao cao doanh thu (UC9.1)
public enum KyBaoCao {
    ngay("Theo ngày"),
    thang("Theo tháng"),
    nam("Theo năm"),
    khoang("Khoảng ngày");

    private final String nhan;

    KyBaoCao(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }
}
