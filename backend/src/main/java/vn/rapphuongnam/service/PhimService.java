package vn.rapphuongnam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.PhimForm;
import vn.rapphuongnam.dto.PhimThongKe;
import vn.rapphuongnam.entity.Phim;
import vn.rapphuongnam.entity.TheLoai;
import vn.rapphuongnam.entity.TrangThaiPhim;
import vn.rapphuongnam.repository.PhimRepository;
import vn.rapphuongnam.repository.TheLoaiRepository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PhimService {
    public static final int SO_DONG_MOI_TRANG = 10;

    private final PhimRepository phimRepo;
    private final TheLoaiRepository theLoaiRepo;

    public PhimService(PhimRepository phimRepo, TheLoaiRepository theLoaiRepo) {
        this.phimRepo = phimRepo;
        this.theLoaiRepo = theLoaiRepo;
    }

    //UC2.4, UC2.5
    @Transactional(readOnly = true)
    public Page<Phim> timKiem(String q, Integer theLoaiId, TrangThaiPhim trangThai, int trang) {
        String tuKhoa = (q == null || q.isBlank()) ? null : q.trim();
        PageRequest pr = PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG, Sort.by(Sort.Direction.DESC, "id"));
        return phimRepo.timKiem(tuKhoa, theLoaiId, trangThai, pr);
    }

    @Transactional(readOnly = true)
    public Phim layTheoId(Integer id) {
        return phimRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phim"));
    }

    public List<TheLoai> dsTheLoai() {
        return theLoaiRepo.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public List<Phim> phimTheoTrangThai(TrangThaiPhim tt) {
        return phimRepo.findByTrangThaiOrderByNgayKhoiChieuDesc(tt);
    }

    //Do du lieu phim vao form khi sua
    public PhimForm taoForm(Phim p) {
        PhimForm f = new PhimForm();
        f.setId(p.getId());
        f.setTenPhim(p.getTenPhim());
        f.setDaoDien(p.getDaoDien());
        f.setDienVien(p.getDienVien());
        f.setThoiLuong(p.getThoiLuong());
        f.setNgonNgu(p.getNgonNgu());
        f.setPhuDe(p.getPhuDe());
        f.setNgayKhoiChieu(p.getNgayKhoiChieu());
        f.setNgayKetThuc(p.getNgayKetThuc());
        f.setDoTuoi(p.getDoTuoi());
        f.setMoTa(p.getMoTa());
        f.setPoster(p.getPoster());
        f.setTrailer(p.getTrailer());
        f.setTrangThai(p.getTrangThai());
        f.setTheLoaiIds(p.getTheLoai().stream().map(TheLoai::getId).collect(Collectors.toList()));
        return f;
    }

    //Cac quy tac nghiep vu khong viet duoc bang annotation, loi gan vao dung o nhap
    @Transactional(readOnly = true)
    public void kiemTra(PhimForm f, Errors errors) {
        if (f.getNgayKhoiChieu() != null && f.getNgayKetThuc() != null
                && f.getNgayKetThuc().isBefore(f.getNgayKhoiChieu())) {
            errors.rejectValue("ngayKetThuc", "ngay", "Ngày kết thúc phải sau hoặc bằng ngày khởi chiếu");
        }
        if (f.getTenPhim() != null && f.getNgayKhoiChieu() != null
                && phimRepo.existsByTenPhimIgnoreCaseAndNgayKhoiChieuAndIdNot(f.getTenPhim().trim(), f.getNgayKhoiChieu(),
                        f.getId() == null ? 0 : f.getId())) {
            errors.rejectValue("tenPhim", "trung", "Đã có phim cùng tên và cùng ngày khởi chiếu");
        }
        if (f.getId() != null) {
            Phim cu = layTheoId(f.getId());
            long suatChuaKetThuc = phimRepo.demSuatChuaKetThuc(cu.getId());
            if (suatChuaKetThuc > 0 && !Objects.equals(cu.getThoiLuong(), f.getThoiLuong())) {
                errors.rejectValue("thoiLuong", "suat",
                        "Phim còn " + suatChuaKetThuc + " suất chiếu chưa kết thúc, không đổi thời lượng được");
            }
            if (f.getNgayKhoiChieu() != null && f.getNgayKetThuc() != null && !errors.hasFieldErrors("ngayKetThuc")) {
                long ngoai = phimRepo.demSuatNgoaiKhoang(cu.getId(), f.getNgayKhoiChieu(), f.getNgayKetThuc());
                if (ngoai > 0) {
                    errors.rejectValue("ngayKetThuc", "suat",
                            "Có " + ngoai + " suất chiếu chưa kết thúc nằm ngoài khoảng chiếu mới");
                }
            }
        }
    }

    //UC2.1 them moi (id null) hoac UC2.2 sua
    @Transactional
    public Phim luu(PhimForm f) {
        Phim p = f.getId() == null ? new Phim() : layTheoId(f.getId());
        p.setTenPhim(f.getTenPhim().trim());
        p.setDaoDien(rongThanhNull(f.getDaoDien()));
        p.setDienVien(rongThanhNull(f.getDienVien()));
        p.setThoiLuong(f.getThoiLuong());
        p.setNgonNgu(rongThanhNull(f.getNgonNgu()));
        p.setPhuDe(rongThanhNull(f.getPhuDe()));
        p.setNgayKhoiChieu(f.getNgayKhoiChieu());
        p.setNgayKetThuc(f.getNgayKetThuc());
        p.setDoTuoi(f.getDoTuoi());
        p.setMoTa(rongThanhNull(f.getMoTa()));
        p.setPoster(rongThanhNull(f.getPoster()));
        p.setTrailer(rongThanhNull(f.getTrailer()));
        if (f.getId() == null) {
            p.setTrangThai(f.getTrangThai() == TrangThaiPhim.dangChieu ? TrangThaiPhim.dangChieu : TrangThaiPhim.sapChieu);
        }
        p.setTheLoai(new LinkedHashSet<>(theLoaiRepo.findAllById(f.getTheLoaiIds())));
        return phimRepo.save(p);
    }

    //UC2.3 doi trang thai phim, tra ve thong bao ket qua
    @Transactional
    public String doiTrangThai(Integer id, TrangThaiPhim moi) {
        Phim p = layTheoId(id);
        if (moi == null || moi == p.getTrangThai()) {
            return "Trạng thái không thay đổi";
        }
        String them = "";
        if (moi == TrangThaiPhim.an) {
            if (phimRepo.demSuatChuaKetThucCoDon(id) > 0) {
                throw new NghiepVuException("Phim còn suất chiếu đã có khách đặt vé, chưa thể ẩn");
            }
            int n = phimRepo.tamNgungSuatChuaKetThuc(id);
            if (n > 0) them = " Đã tạm ngưng " + n + " suất chiếu chưa diễn ra.";
        }
        if (moi == TrangThaiPhim.dangChieu && p.getNgayKhoiChieu().isAfter(LocalDate.now())) {
            them = " Lưu ý: ngày khởi chiếu chưa tới.";
        }
        p.setTrangThai(moi);
        return "Đã chuyển phim sang \"" + moi.getNhan() + "\"." + them;
    }

    @Transactional(readOnly = true)
    public PhimThongKe thongKe(Integer id) {
        return new PhimThongKe(phimRepo.demTongSuat(id), phimRepo.demSuatChuaKetThuc(id),
                phimRepo.demVeDaBan(id), phimRepo.tinhDoanhThuVe(id));
    }

    private static String rongThanhNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
