package vn.rapphuongnam.khach;

//Loi tra ve cho frontend: ma HTTP + thong bao + (tuy chon) loi theo tung o nhap
public class ApiLoi extends RuntimeException {
    private final int maHttp;
    private final Object duLieu;

    public ApiLoi(String message) {
        this(message, 400, null);
    }

    public ApiLoi(String message, int maHttp, Object duLieu) {
        super(message);
        this.maHttp = maHttp;
        this.duLieu = duLieu;
    }

    public int getMaHttp() { return maHttp; }
    public Object getDuLieu() { return duLieu; }
}
