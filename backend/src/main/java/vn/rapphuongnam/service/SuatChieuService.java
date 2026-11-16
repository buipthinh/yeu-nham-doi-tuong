package vn.rapphuongnam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.GiaDong;
import vn.rapphuongnam.dto.SuatChieuDong;
import vn.rapphuongnam.dto.SuatChieuForm;
import vn.rapphuongnam.dto.XemTruocSuat;
import vn.rapphuongnam.entity.*;
import vn.rapphuongnam.repository.PhimRepository;
import vn.rapphuongnam.repository.PhongChieuRepository;
import vn.rapphuongnam.repository.SuatChieuRepository;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//UC3: Quan ly suat chieu
@Service
public class SuatChieuService {
    public static final int SO_DONG_MOI_TRANG = 20;
    private static final int DON_PHONG_MAC_DINH = 15;
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter GIO = DateTimeFormatter.ofPattern("HH:mm");

    private final SuatChieuRepository suatRepo;
    private final PhimRepository phimRepo;
    private final PhongChieuRepository phongRepo;

    public SuatChieuService(SuatChieuRepository suatRepo, PhimRepository phimRepo, PhongChieuRepository phongRepo) {
        this.suatRepo = suatRepo;
        this.phimRepo = phimRepo;
        this.phongRepo = phongRepo;
    }

    // ===== Ham dung chung =====

    @Transactional(readOnly = true)
    public SuatChieu layTheoId(Integer id) {
        return suatRepo.timChiTiet(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy suất chiếu"));
    }

    public List<PhongChieu> dsPhongDungDuoc() {
        return phongRepo.phongDungDuoc();
    }

    public List<PhongChieu> dsTatCaPhong() {
        return phongRepo.findAll(Sort.by("tenPhong"));
    }

    //Ngay le uu tien truoc cuoi tuan
    public LoaiNgay loaiNgay(LocalDate d) {
        if (suatRepo.demNgayLe(d) > 0) return LoaiNgay.ngayLe;
        DayOfWeek t = d.getDayOfWeek();
        return (t == DayOfWeek.SATURDAY || t == DayOfWeek.SUNDAY) ? LoaiNgay.cuoiTuan : LoaiNgay.ngayThuong;
    }

    //Tra ve null neu suat vuot qua nua dem
    static LocalTime tinhGioKetThuc(LocalTime bd, int thoiLuong) {
        if (bd.toSecondOfDay() / 60 + thoiLuong >= 24 * 60) return null;
        return bd.plusMinutes(thoiLuong);
    }

    TinhTrangSuat tinhTrang(SuatChieu s, long soTrong, LocalDateTime now) {
        if (s.daKetThuc(now)) return TinhTrangSuat.daKetThuc;
        if (s.getTrangThai() == TrangThaiSuatChieu.tamNgung) return TinhTrangSuat.tamNgung;
        return soTrong == 0 ? TinhTrangSuat.hetVe : TinhTrangSuat.conVe;
    }

    // ===== Kiem tra (UC3.1 buoc 3-6, UC3.2 buoc 5) =====

    @Transactional(readOnly = true)
    public void kiemTra(SuatChieuForm f, Errors errors) {
        Phim phim = null;
        PhongChieu phong = null;

        if (f.getPhimId() != null) {
            phim = phimRepo.findById(f.getPhimId()).orElse(null);
            if (phim == null) {
                errors.rejectValue("phimId", "phim", "Phim không tồn tại");
            } else if (phim.getTrangThai() == TrangThaiPhim.an) {
                errors.rejectValue("phimId", "phim", "Phim đang Ẩn, không tạo suất chiếu được");
            }
        }
        if (f.getPhongChieuId() != null) {
            phong = phongRepo.findById(f.getPhongChieuId()).orElse(null);
            if (phong == null || phong.getTrangThai() != TrangThaiPhong.hoatDong
                    || suatRepo.demGheHoatDong(phong.getId()) == 0) {
                errors.rejectValue("phongChieuId", "phong", "Phòng không hoạt động hoặc chưa có sơ đồ ghế");
                phong = null;
            }
        }
        if (f.getNgayChieu() == null || f.getGioBatDau() == null) return;

        LocalDate ngay = f.getNgayChieu();
        LocalTime bd = f.getGioBatDau();
        if (!LocalDateTime.of(ngay, bd).isAfter(LocalDateTime.now())) {
            errors.rejectValue("gioBatDau", "qua", "Thời điểm bắt đầu phải sau hiện tại");
            return;
        }
        if (phim == null) return;

        LocalTime kt = tinhGioKetThuc(bd, phim.getThoiLuong());
        if (kt == null) {
            errors.rejectValue("gioBatDau", "dem", "Suất chiếu phải kết thúc trước 24:00");
            return;
        }
        if (ngay.isBefore(phim.getNgayKhoiChieu()) || ngay.isAfter(phim.getNgayKetThuc())) {
            errors.rejectValue("ngayChieu", "khoang", "Ngày chiếu phải nằm trong khoảng "
                    + phim.getNgayKhoiChieu().format(NGAY) + " – " + phim.getNgayKetThuc().format(NGAY) + " của phim");
        }
        if (phong == null) return;

        //Trung lich, ke ca suat Tam ngung vi co the mo ban lai
        Integer buffer = suatRepo.thoiGianDonPhong();
        int dem = buffer == null ? DON_PHONG_MAC_DINH : buffer;
        int moiBd = bd.toSecondOfDay() / 60;
        int moiKt = kt.toSecondOfDay() / 60;
        for (SuatChieu s : suatRepo.findByPhongChieuIdAndNgayChieuAndIdNot(phong.getId(), ngay,
                f.getId() == null ? 0 : f.getId())) {
            int sBd = s.getGioBatDau().toSecondOfDay() / 60;
            int sKt = s.getGioKetThuc().toSecondOfDay() / 60;
            if (moiBd < sKt + dem && sBd < moiKt + dem) {
                errors.rejectValue("gioBatDau", "trung", "Trùng lịch với suất " + s.getMaSuat() + " ("
                        + s.getPhim().getTenPhim() + ") " + s.getGioBatDau().format(GIO) + "–" + s.getGioKetThuc().format(GIO)
                        + " trong phòng; cần cách nhau ít nhất " + dem + " phút để dọn phòng");
                break;
            }
        }

        //Gia hieu luc
        LoaiNgay ln = loaiNgay(ngay);
        KhungGio kg = KhungGio.tuGio(bd);
        if (suatRepo.demLoaiGheChuaCoGia(phong.getId(), ln.name(), kg.name(), ngay) > 0) {
            errors.reject("gia", "Chưa có giá vé hiệu lực cho " + ln.getNhan().toLowerCase() + ", khung "
                    + kg.getNhan().toLowerCase() + ". Cần thiết lập giá vé trước (UC5.1.1)");
        }
    }

    //Du lieu man hinh xac nhan (chi goi khi kiemTra khong loi)
    @Transactional(readOnly = true)
    public XemTruocSuat xemTruoc(SuatChieuForm f) {
        Phim phim = phimRepo.findById(f.getPhimId()).orElseThrow();
        PhongChieu phong = phongRepo.findById(f.getPhongChieuId()).orElseThrow();
        LoaiNgay ln = loaiNgay(f.getNgayChieu());
        KhungGio kg = KhungGio.tuGio(f.getGioBatDau());
        return new XemTruocSuat(phim.getTenPhim(), phong.getTenPhong(), f.getNgayChieu(), f.getGioBatDau(),
                tinhGioKetThuc(f.getGioBatDau(), phim.getThoiLuong()), ln, kg,
                suatRepo.giaThapNhat(phong.getId(), ln.name(), kg.name(), f.getNgayChieu()),
                suatRepo.demGheHoatDong(phong.getId()));
    }

    // ===== UC3.1 Them =====

    @Transactional
    public SuatChieu them(SuatChieuForm f) {
        BeanPropertyBindingResult kq = new BeanPropertyBindingResult(f, "suatChieuForm");
        kiemTra(f, kq);
        nemNeuLoi(kq);

        Phim phim = phimRepo.findById(f.getPhimId()).orElseThrow();
        SuatChieu s = new SuatChieu();
        s.setPhim(phim);
        s.setPhongChieu(phongRepo.getReferenceById(f.getPhongChieuId()));
        s.setNgayChieu(f.getNgayChieu());
        s.setGioBatDau(f.getGioBatDau());
        s.setGioKetThuc(tinhGioKetThuc(f.getGioBatDau(), phim.getThoiLuong()));
        s.setTrangThai(TrangThaiSuatChieu.moBan);
        s = suatRepo.saveAndFlush(s);
        suatRepo.taoGheSuatChieu(s.getId(), f.getPhongChieuId());
        return s;
    }

    // ===== UC3.2 Sua =====

    //Ly do khong sua duoc, null neu sua duoc
    @Transactional(readOnly = true)
    public String lyDoKhongSua(SuatChieu s) {
        long don = suatRepo.demDonConHieuLuc(s.getId());
        if (don > 0) {
            return "Suất chiếu đã có " + don + " đơn đặt vé, không sửa được. Hãy dùng Tạm ngưng nếu cần dừng bán.";
        }
        if (s.daBatDau(LocalDateTime.now())) {
            return "Suất chiếu đã bắt đầu hoặc đã kết thúc, không sửa được.";
        }
        return null;
    }

    @Transactional(readOnly = true)
    public void kiemTraDuocSua(SuatChieu s) {
        String ly = lyDoKhongSua(s);
        if (ly != null) throw new NghiepVuException(ly);
    }

    @Transactional
    public SuatChieu sua(SuatChieuForm f) {
        SuatChieu s = layTheoId(f.getId());
        kiemTraDuocSua(s);
        BeanPropertyBindingResult kq = new BeanPropertyBindingResult(f, "suatChieuForm");
        kiemTra(f, kq);
        nemNeuLoi(kq);

        boolean doiPhong = !s.getPhongChieu().getId().equals(f.getPhongChieuId());
        if (doiPhong && suatRepo.demMoiDon(s.getId()) > 0) {
            throw new NghiepVuException("Suất chiếu từng có đơn (đã hủy/hết hạn) nên không đổi phòng được. "
                    + "Hãy tạm ngưng suất này và tạo suất mới.");
        }
        Phim phim = phimRepo.findById(f.getPhimId()).orElseThrow();
        s.setPhim(phim);
        s.setPhongChieu(phongRepo.getReferenceById(f.getPhongChieuId()));
        s.setNgayChieu(f.getNgayChieu());
        s.setGioBatDau(f.getGioBatDau());
        s.setGioKetThuc(tinhGioKetThuc(f.getGioBatDau(), phim.getThoiLuong()));
        s = suatRepo.saveAndFlush(s);
        if (doiPhong) {
            suatRepo.xoaGheSuatChieu(s.getId());
            suatRepo.taoGheSuatChieu(s.getId(), f.getPhongChieuId());
        }
        return s;
    }

    private static void nemNeuLoi(Errors kq) {
        if (kq.hasErrors()) {
            String msg = kq.getAllErrors().get(0).getDefaultMessage();
            throw new NghiepVuException(msg == null ? "Thông tin suất chiếu chưa hợp lệ" : msg);
        }
    }

    public SuatChieuForm taoForm(SuatChieu s) {
        SuatChieuForm f = new SuatChieuForm();
        f.setId(s.getId());
        f.setPhimId(s.getPhim().getId());
        f.setPhongChieuId(s.getPhongChieu().getId());
        f.setNgayChieu(s.getNgayChieu());
        f.setGioBatDau(s.getGioBatDau());
        return f;
    }

    // ===== UC3.3 Doi trang thai =====

    @Transactional
    public String doiTrangThai(Integer id, TrangThaiSuatChieu moi) {
        SuatChieu s = layTheoId(id);
        if (moi == null || moi == s.getTrangThai()) return "Trạng thái không thay đổi";
        LocalDateTime now = LocalDateTime.now();
        if (s.daKetThuc(now)) {
            throw new NghiepVuException("Suất chiếu đã kết thúc, không đổi trạng thái được");
        }
        if (moi == TrangThaiSuatChieu.moBan) {
            if (s.daBatDau(now)) {
                throw new NghiepVuException("Suất chiếu đã quá giờ bắt đầu, không mở bán lại được");
            }
            if (s.getPhim().getTrangThai() == TrangThaiPhim.an) {
                throw new NghiepVuException("Phim đang Ẩn, hãy chuyển phim về Sắp chiếu/Đang chiếu trước");
            }
        }
        s.setTrangThai(moi);
        String them = moi == TrangThaiSuatChieu.tamNgung
                ? " Khách sẽ không thấy suất này và không đặt vé mới được." : "";
        return "Đã chuyển suất " + s.getMaSuat() + " sang " + moi.getNhan() + "." + them;
    }

    // ===== UC3.4 Lich chieu =====

    public static LocalDate tuNgay(String cheDo, LocalDate ngay) {
        return "tuan".equals(cheDo) ? ngay.with(DayOfWeek.MONDAY) : ngay;
    }

    public static LocalDate denNgay(String cheDo, LocalDate ngay) {
        return "tuan".equals(cheDo) ? ngay.with(DayOfWeek.MONDAY).plusDays(6) : ngay;
    }

    @Transactional(readOnly = true)
    public List<SuatChieuDong> lichChieu(String cheDo, LocalDate ngay, Integer phongId) {
        return thanhDong(suatRepo.lich(tuNgay(cheDo, ngay), denNgay(cheDo, ngay), phongId));
    }

    //Gom theo phong, tinh khoang trong giua cac suat lien tiep
    public Map<String, List<SuatChieuDong>> theoPhong(List<SuatChieuDong> ds) {
        Map<String, List<SuatChieuDong>> kq = new LinkedHashMap<>();
        for (SuatChieuDong d : ds) {
            List<SuatChieuDong> nhom = kq.computeIfAbsent(d.getSuat().getPhongChieu().getTenPhong(), k -> new ArrayList<>());
            if (!nhom.isEmpty()) {
                SuatChieu truoc = nhom.get(nhom.size() - 1).getSuat();
                int phut = (d.getSuat().getGioBatDau().toSecondOfDay() - truoc.getGioKetThuc().toSecondOfDay()) / 60;
                d.setKhoangTrong(Math.max(phut, 0));
            }
            nhom.add(d);
        }
        return kq;
    }

    // ===== UC3.5 Tim kiem =====

    @Transactional(readOnly = true)
    public Page<SuatChieuDong> timKiem(String q, Integer phimId, Integer phongId, LocalDate tuNgay, LocalDate denNgay,
                                       TrangThaiSuatChieu trangThai, boolean anDaKetThuc, int trang) {
        String tuKhoa = (q == null || q.isBlank()) ? null : q.trim();
        PageRequest pr = PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG,
                Sort.by(Sort.Order.desc("ngayChieu"), Sort.Order.asc("gioBatDau")));
        LocalDateTime now = LocalDateTime.now();
        Page<SuatChieu> p = suatRepo.timKiem(tuKhoa, phimId, phongId, tuNgay, denNgay, trangThai,
                anDaKetThuc, now.toLocalDate(), pr);
        List<SuatChieuDong> dong = thanhDong(p.getContent());
        return new org.springframework.data.domain.PageImpl<>(dong, pr, p.getTotalElements());
    }

    // ===== Chi tiet =====

    @Transactional(readOnly = true)
    public SuatChieuDong dong(SuatChieu s) {
        return thanhDong(List.of(s)).get(0);
    }

    @Transactional(readOnly = true)
    public List<GiaDong> bangGia(SuatChieu s) {
        List<GiaDong> kq = new ArrayList<>();
        for (Object[] r : suatRepo.bangGia(s.getPhongChieu().getId(), loaiNgay(s.getNgayChieu()).name(),
                KhungGio.tuGio(s.getGioBatDau()).name(), s.getNgayChieu())) {
            kq.add(new GiaDong((String) r[0], (String) r[1], (BigDecimal) r[2]));
        }
        return kq;
    }

    @Transactional(readOnly = true)
    public long demDon(Integer id) {
        return suatRepo.demMoiDon(id);
    }

    //Doi danh sach suat thanh danh sach dong hien thi: 1 query thong ke ghe, cache gia theo (phong, ngay, khung gio)
    private List<SuatChieuDong> thanhDong(List<SuatChieu> ds) {
        if (ds.isEmpty()) return List.of();
        Map<Integer, long[]> thongKe = new HashMap<>();
        for (Object[] r : suatRepo.thongKeGhe(ds.stream().map(SuatChieu::getId).toList())) {
            thongKe.put(((Number) r[0]).intValue(), new long[]{((Number) r[1]).longValue(), ((Number) r[2]).longValue()});
        }
        Map<String, BigDecimal> cacheGia = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        List<SuatChieuDong> kq = new ArrayList<>();
        for (SuatChieu s : ds) {
            long[] tk = thongKe.getOrDefault(s.getId(), new long[]{0, 0});
            KhungGio kg = KhungGio.tuGio(s.getGioBatDau());
            String khoa = s.getPhongChieu().getId() + "|" + s.getNgayChieu() + "|" + kg;
            if (!cacheGia.containsKey(khoa)) {
                cacheGia.put(khoa, suatRepo.giaThapNhat(s.getPhongChieu().getId(), loaiNgay(s.getNgayChieu()).name(),
                        kg.name(), s.getNgayChieu()));
            }
            kq.add(new SuatChieuDong(s, tk[0], tk[1], cacheGia.get(khoa), tinhTrang(s, tk[0], now), kg));
        }
        return kq;
    }
}
