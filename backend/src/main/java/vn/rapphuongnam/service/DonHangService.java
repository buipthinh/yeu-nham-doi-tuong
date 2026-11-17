package vn.rapphuongnam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.DonHangChiTiet;
import vn.rapphuongnam.dto.DonHangDong;
import vn.rapphuongnam.dto.DonHangLoc;
import vn.rapphuongnam.entity.TrangThaiDonHang;
import vn.rapphuongnam.repository.DonHangRepository;

import java.util.List;
import java.util.Map;
import java.util.EnumMap;

//UC6.2 Quan ly dat ve: xem danh sach, chi tiet va tra cuu don hang (Admin, Nhan vien).
//Xac nhan / huy don thu cong nam ngoai pham vi (theo bao cao).
@Service
public class DonHangService {
    public static final int SO_DONG_MOI_TRANG = 10;
    private static final Logger log = LoggerFactory.getLogger(DonHangService.class);

    //Ket qua tra cuu: danh sach don + thong bao (loi hoac huong dan)
    public static class KetQuaTraCuu {
        private final List<DonHangDong> ds;
        private final String loi;

        KetQuaTraCuu(List<DonHangDong> ds, String loi) {
            this.ds = ds;
            this.loi = loi;
        }

        public List<DonHangDong> getDs() { return ds; }
        public String getLoi() { return loi; }
    }

    private final DonHangRepository donHangRepo;
    private final KhoaTraCuu khoaTraCuu;

    public DonHangService(DonHangRepository donHangRepo, KhoaTraCuu khoaTraCuu) {
        this.donHangRepo = donHangRepo;
        this.khoaTraCuu = khoaTraCuu;
    }

    //Tra ghe giu qua han truoc khi doc, loi o buoc nay khong lam hong trang
    private void capNhatDonQuaHan() {
        try {
            donHangRepo.giaiPhongGheQuaHan();
        } catch (RuntimeException e) {
            log.warn("Khong chay duoc sp_GiaiPhongGheQuaHan: {}", e.getMessage());
        }
    }

    //UC6.2.1 danh sach don hang, moi nhat truoc
    public Page<DonHangDong> timKiem(DonHangLoc loc) {
        capNhatDonQuaHan();
        int trang = Math.max(loc.getTrang(), 0);
        long tong = donHangRepo.dem(loc);
        List<DonHangDong> ds = donHangRepo.timKiem(loc, trang * SO_DONG_MOI_TRANG, SO_DONG_MOI_TRANG);
        return new PageImpl<>(ds, PageRequest.of(trang, SO_DONG_MOI_TRANG), tong);
    }

    public Map<TrangThaiDonHang, Long> demTheoTrangThai() {
        Map<TrangThaiDonHang, Long> m = new EnumMap<>(TrangThaiDonHang.class);
        for (TrangThaiDonHang tt : TrangThaiDonHang.values()) {
            m.put(tt, donHangRepo.demTheoTrangThai(tt));
        }
        return m;
    }

    public long demDonHomNay() {
        return donHangRepo.demDonHomNay();
    }

    public DonHangChiTiet chiTiet(Integer id) {
        capNhatDonQuaHan();
        DonHangDong d = donHangRepo.theoId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));
        return new DonHangChiTiet(d, donHangRepo.veCuaDon(id), donHangRepo.monCuaDon(id), donHangRepo.giaoDichCuaDon(id));
    }

    //UC6.2.2 tra cuu theo ma don, ma ve hoac so dien thoai
    public KetQuaTraCuu traCuu(String kieu, String q) {
        String s = q == null ? "" : q.trim();
        if (s.isEmpty()) return new KetQuaTraCuu(List.of(), "Nhập thông tin cần tra cứu");
        capNhatDonQuaHan();
        switch (kieu == null ? "" : kieu) {
            case "maVe" -> {
                long khoa = khoaTraCuu.conKhoaGiay();
                if (khoa > 0) {
                    return new KetQuaTraCuu(List.of(), "Nhập sai mã vé quá " + KhoaTraCuu.SO_LAN_TOI_DA
                            + " lần. Tra cứu theo mã vé tạm khóa, thử lại sau " + (khoa / 60) + " phút " + (khoa % 60) + " giây.");
                }
                String ma = s.toUpperCase();
                List<DonHangDong> ds = ma.matches("[A-Z0-9]{8}") ? donHangRepo.theoMaVe(ma).stream().toList() : List.of();
                if (ds.isEmpty()) {
                    int conLai = khoaTraCuu.ghiSai();
                    return new KetQuaTraCuu(List.of(), conLai == 0
                            ? "Mã vé không tồn tại. Đã nhập sai " + KhoaTraCuu.SO_LAN_TOI_DA + " lần, tra cứu theo mã vé bị khóa "
                                    + KhoaTraCuu.SO_PHUT_KHOA + " phút."
                            : "Mã vé không tồn tại (mã vé gồm 8 ký tự chữ và số). Còn " + conLai + " lần thử.");
                }
                khoaTraCuu.datLai();
                return new KetQuaTraCuu(ds, null);
            }
            case "sdt" -> {
                if (!s.matches("0\\d{9}")) {
                    return new KetQuaTraCuu(List.of(), "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0");
                }
                List<DonHangDong> ds = donHangRepo.theoSoDienThoai(s);
                return new KetQuaTraCuu(ds, ds.isEmpty() ? "Không có đơn hàng nào của số điện thoại " + s : null);
            }
            default -> {
                Integer id = DonHangRepository.maDon(s);
                if (id == null) return new KetQuaTraCuu(List.of(), "Mã đơn là một số, ví dụ 125");
                List<DonHangDong> ds = donHangRepo.theoId(id).stream().toList();
                return new KetQuaTraCuu(ds, ds.isEmpty() ? "Không tìm thấy đơn hàng có mã " + id : null);
            }
        }
    }
}
