package vn.rapphuongnam.entity;

//Gia tri luu trong cot Phim.doTuoi
public enum DoTuoi {
    P("P – Mọi lứa tuổi"),
    C13("C13 – Từ 13 tuổi"),
    C16("C16 – Từ 16 tuổi"),
    C18("C18 – Từ 18 tuổi");

    private final String nhan;

    DoTuoi(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }
}
