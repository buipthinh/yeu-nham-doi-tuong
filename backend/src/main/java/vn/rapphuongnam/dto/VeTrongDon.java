package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//Mot ve (mot ghe) trong chi tiet don hang
public class VeTrongDon {
    private final String maGhe;
    private final String tenLoaiVe;
    private final String tenDoiTuong;
    private final BigDecimal giaVe;
    private final String trangThaiGhe;

    public VeTrongDon(String maGhe, String tenLoaiVe, String tenDoiTuong, BigDecimal giaVe, String trangThaiGhe) {
        this.maGhe = maGhe;
        this.tenLoaiVe = tenLoaiVe;
        this.tenDoiTuong = tenDoiTuong;
        this.giaVe = giaVe;
        this.trangThaiGhe = trangThaiGhe;
    }

    public String getTenTrangThaiGhe() {
        if (trangThaiGhe == null) return "—";
        return switch (trangThaiGhe) {
            case "daBan" -> "Đã bán";
            case "dangGiu" -> "Đang giữ";
            case "trong" -> "Đã trả ghế";
            default -> trangThaiGhe;
        };
    }

    public String getMaGhe() { return maGhe; }
    public String getTenLoaiVe() { return tenLoaiVe; }
    public String getTenDoiTuong() { return tenDoiTuong; }
    public BigDecimal getGiaVe() { return giaVe; }
    public String getTrangThaiGhe() { return trangThaiGhe; }
}
