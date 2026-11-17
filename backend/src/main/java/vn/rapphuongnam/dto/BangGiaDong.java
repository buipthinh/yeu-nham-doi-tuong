package vn.rapphuongnam.dto;

import vn.rapphuongnam.entity.KhungGio;
import vn.rapphuongnam.entity.LoaiNgay;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

//Mot dong cua bang gia dang ap dung hom nay: mot cap loai ve x doi tuong, gia theo loai ngay x khung gio
public class BangGiaDong {
    private final String tenLoaiVe;
    private final String tenDoiTuong;
    private final Map<LoaiNgay, Map<KhungGio, BigDecimal>> gia = new EnumMap<>(LoaiNgay.class);

    public BangGiaDong(String tenLoaiVe, String tenDoiTuong) {
        this.tenLoaiVe = tenLoaiVe;
        this.tenDoiTuong = tenDoiTuong;
    }

    public void dat(LoaiNgay ln, KhungGio kg, BigDecimal g) {
        gia.computeIfAbsent(ln, k -> new EnumMap<>(KhungGio.class)).put(kg, g);
    }

    //null = chua co gia cho o nay
    public BigDecimal gia(LoaiNgay ln, KhungGio kg) {
        Map<KhungGio, BigDecimal> m = gia.get(ln);
        return m == null ? null : m.get(kg);
    }

    public String getTenLoaiVe() { return tenLoaiVe; }
    public String getTenDoiTuong() { return tenDoiTuong; }
}
