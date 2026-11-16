package vn.rapphuongnam.entity;

import java.time.LocalTime;

//Gia tri cot QuyTacGia.khungGio. Xet theo gio bat dau cua suat:
//sang < 12:00, trua 12:00-14:59, chieu 15:00-17:59, toi >= 18:00
public enum KhungGio {
    sang("Sáng"),
    trua("Trưa"),
    chieu("Chiều"),
    toi("Tối");

    private final String nhan;

    KhungGio(String nhan) { this.nhan = nhan; }

    public String getNhan() { return nhan; }

    public static KhungGio tuGio(LocalTime gio) {
        if (gio.isBefore(LocalTime.of(12, 0))) return sang;
        if (gio.isBefore(LocalTime.of(15, 0))) return trua;
        if (gio.isBefore(LocalTime.of(18, 0))) return chieu;
        return toi;
    }
}
