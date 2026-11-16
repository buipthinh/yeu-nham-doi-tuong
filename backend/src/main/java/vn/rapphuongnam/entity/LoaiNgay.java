package vn.rapphuongnam.entity;

//Gia tri cot QuyTacGia.loaiNgay
public enum LoaiNgay {
    ngayThuong("Ngày thường"),
    cuoiTuan("Cuối tuần"),
    ngayLe("Ngày lễ");

    private final String nhan;

    LoaiNgay(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }
}
