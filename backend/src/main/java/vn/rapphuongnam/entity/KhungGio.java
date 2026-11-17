package vn.rapphuongnam.entity;

import java.time.LocalTime;

//Gia tri cot QuyTacGia.khungGio. Xet theo gio bat dau cua suat, giong view vw_SuatChieuGia trong database:
//sang < 11:00, trua 11:00-13:59, chieu 14:00-17:59, toi >= 18:00
public enum KhungGio {
    sang("Sáng", "trước 11:00"),
    trua("Trưa", "11:00 – 13:59"),
    chieu("Chiều", "14:00 – 17:59"),
    toi("Tối", "từ 18:00");

    private final String nhan;
    private final String moTa;

    KhungGio(String nhan, String moTa) {
        this.nhan = nhan;
        this.moTa = moTa;
    }

    public String getNhan() { return nhan; }
    public String getMoTa() { return moTa; }

    public static KhungGio tuGio(LocalTime gio) {
        if (gio.isBefore(LocalTime.of(11, 0))) return sang;
        if (gio.isBefore(LocalTime.of(14, 0))) return trua;
        if (gio.isBefore(LocalTime.of(18, 0))) return chieu;
        return toi;
    }
}
