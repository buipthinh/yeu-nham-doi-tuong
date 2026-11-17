package vn.rapphuongnam.dto;

import java.time.LocalDate;
import java.util.List;

//Ket qua UC9.1: cac moc thoi gian + dong tong
public class BaoCaoDoanhThu {
    private final LocalDate tuNgay;
    private final LocalDate denNgay;
    private final List<DoanhThuMoc> cacMoc;
    private final DoanhThuMoc tong;

    public BaoCaoDoanhThu(LocalDate tuNgay, LocalDate denNgay, List<DoanhThuMoc> cacMoc, DoanhThuMoc tong) {
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.cacMoc = cacMoc;
        this.tong = tong;
    }

    //Chi cac moc co don, dung cho bang chi tiet
    public List<DoanhThuMoc> getMocCoDon() {
        return cacMoc.stream().filter(m -> m.getSoDon() > 0).toList();
    }

    public boolean isCoDuLieu() {
        return tong.getSoDon() > 0;
    }

    public LocalDate getTuNgay() { return tuNgay; }
    public LocalDate getDenNgay() { return denNgay; }
    public List<DoanhThuMoc> getCacMoc() { return cacMoc; }
    public DoanhThuMoc getTong() { return tong; }
}
