package vn.rapphuongnam.entity;

//QuyTacGia.loaiNgay: thu 7, chu nhat la cuoi tuan; ngay le lay tu bang NgayLe
public enum LoaiNgay {
    ngayThuong("Ngày thường", "mute"),
    cuoiTuan("Cuối tuần", "info"),
    ngayLe("Ngày lễ", "bad");

    private final String nhan;
    private final String css;

    LoaiNgay(String nhan, String css) {
        this.nhan = nhan;
        this.css = css;
    }

    public String getNhan() { return nhan; }
    public String getCss() { return css; }
}
