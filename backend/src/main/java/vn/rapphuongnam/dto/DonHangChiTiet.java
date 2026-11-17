package vn.rapphuongnam.dto;

import java.util.List;

//Chi tiet mot don hang: thong tin chung + ghe + bap nuoc + cac lan thanh toan
public class DonHangChiTiet {
    private final DonHangDong don;
    private final List<VeTrongDon> dsVe;
    private final List<MonTrongDon> dsMon;
    private final List<GiaoDichDong> dsGiaoDich;

    public DonHangChiTiet(DonHangDong don, List<VeTrongDon> dsVe, List<MonTrongDon> dsMon, List<GiaoDichDong> dsGiaoDich) {
        this.don = don;
        this.dsVe = dsVe;
        this.dsMon = dsMon;
        this.dsGiaoDich = dsGiaoDich;
    }

    public DonHangDong getDon() { return don; }
    public List<VeTrongDon> getDsVe() { return dsVe; }
    public List<MonTrongDon> getDsMon() { return dsMon; }
    public List<GiaoDichDong> getDsGiaoDich() { return dsGiaoDich; }
}
