package vn.rapphuongnam.dto;

import vn.rapphuongnam.entity.KhungGio;
import vn.rapphuongnam.entity.SuatChieu;
import vn.rapphuongnam.entity.TinhTrangSuat;

import java.math.BigDecimal;

//Mot dong hien thi suat chieu: suat + so ghe + gia tu + tinh trang
public class SuatChieuDong {
    private final SuatChieu suat;
    private final long soGheTrong;
    private final long soGheDaDat;
    private final BigDecimal giaTu;
    private final TinhTrangSuat tinhTrang;
    private final KhungGio khungGio;
    //So phut trong so voi suat truoc cung phong (chi dung o che do "theo phong")
    private Integer khoangTrong;

    public SuatChieuDong(SuatChieu suat, long soGheTrong, long soGheDaDat, BigDecimal giaTu,
                         TinhTrangSuat tinhTrang, KhungGio khungGio) {
        this.suat = suat;
        this.soGheTrong = soGheTrong;
        this.soGheDaDat = soGheDaDat;
        this.giaTu = giaTu;
        this.tinhTrang = tinhTrang;
        this.khungGio = khungGio;
    }

    public SuatChieu getSuat() { return suat; }
    public long getSoGheTrong() { return soGheTrong; }
    public long getSoGheDaDat() { return soGheDaDat; }
    public BigDecimal getGiaTu() { return giaTu; }
    public TinhTrangSuat getTinhTrang() { return tinhTrang; }
    public KhungGio getKhungGio() { return khungGio; }
    public Integer getKhoangTrong() { return khoangTrong; }
    public void setKhoangTrong(Integer khoangTrong) { this.khoangTrong = khoangTrong; }
}
