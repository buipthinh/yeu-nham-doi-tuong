package vn.rapphuongnam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.ThucPhamForm;
import vn.rapphuongnam.entity.*;
import vn.rapphuongnam.repository.LichSuTonKhoRepository;
import vn.rapphuongnam.repository.TaiKhoanRepository;
import vn.rapphuongnam.repository.ThucPhamRepository;

import java.time.LocalDateTime;

//UC6.1 Quan ly thuc pham (bap, nuoc, combo) va ton kho
@Service
public class ThucPhamService {
    public static final int SO_DONG_MOI_TRANG = 10;
    private static final int NGUONG_MAC_DINH = 20;

    private final ThucPhamRepository thucPhamRepo;
    private final LichSuTonKhoRepository lichSuRepo;
    private final TaiKhoanRepository taiKhoanRepo;
    private final NguoiDungHienTai nguoiDung;

    public ThucPhamService(ThucPhamRepository thucPhamRepo, LichSuTonKhoRepository lichSuRepo,
                           TaiKhoanRepository taiKhoanRepo, NguoiDungHienTai nguoiDung) {
        this.thucPhamRepo = thucPhamRepo;
        this.lichSuRepo = lichSuRepo;
        this.taiKhoanRepo = taiKhoanRepo;
        this.nguoiDung = nguoiDung;
    }

    @Transactional(readOnly = true)
    public Page<ThucPham> timKiem(String q, LoaiThucPham loai, TrangThaiThucPham trangThai, boolean sapHet, int trang) {
        String tuKhoa = (q == null || q.isBlank()) ? null : q.trim();
        PageRequest pr = PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG, Sort.by("loai", "tenMon"));
        return thucPhamRepo.timKiem(tuKhoa, loai, trangThai, sapHet ? nguongCanhBao() : null, pr);
    }

    @Transactional(readOnly = true)
    public ThucPham layTheoId(Integer id) {
        return thucPhamRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy món"));
    }

    //Nguong canh bao sap het hang lay tu Cai dat (UC10)
    @Transactional(readOnly = true)
    public int nguongCanhBao() {
        Integer n = thucPhamRepo.nguongCanhBao();
        return n == null ? NGUONG_MAC_DINH : n;
    }

    @Transactional(readOnly = true)
    public long demDangBan() { return thucPhamRepo.countByTrangThai(TrangThaiThucPham.dangBan); }

    @Transactional(readOnly = true)
    public long demSapHet() {
        return thucPhamRepo.countByTrangThaiAndTonKhoLessThanEqual(TrangThaiThucPham.dangBan, nguongCanhBao());
    }

    @Transactional(readOnly = true)
    public long demDaBan(Integer id) { return thucPhamRepo.demDaBan(id); }

    @Transactional(readOnly = true)
    public long demDonDangCho(Integer id) { return thucPhamRepo.demDonDangCho(id); }

    public ThucPhamForm taoForm(ThucPham t) {
        ThucPhamForm f = new ThucPhamForm();
        f.setId(t.getId());
        f.setTenMon(t.getTenMon());
        f.setLoai(t.getLoai());
        f.setThanhPhan(t.getThanhPhan());
        f.setGiaBan(t.getGiaBan().stripTrailingZeros().scale() <= 0 ? t.getGiaBan().setScale(0) : t.getGiaBan());
        f.setHinhAnh(t.getHinhAnh());
        f.setTonKho(t.getTonKho());
        return f;
    }

    @Transactional(readOnly = true)
    public void kiemTra(ThucPhamForm f, Errors errors) {
        if (f.getTenMon() != null && !f.getTenMon().isBlank()
                && thucPhamRepo.existsByTenMonIgnoreCaseAndIdNot(f.getTenMon().trim(), f.getId() == null ? 0 : f.getId())) {
            errors.rejectValue("tenMon", "trung", "Tên món đã tồn tại");
        }
        if (f.isThemMoi() && f.getTonKho() == null) {
            errors.rejectValue("tonKho", "rong", "Nhập số lượng tồn kho ban đầu");
        }
        if (f.getHinhAnh() != null && !f.getHinhAnh().isBlank()
                && !f.getHinhAnh().trim().toLowerCase().matches(".+\\.(jpg|jpeg|png|webp|gif)(\\?.*)?$")) {
            errors.rejectValue("hinhAnh", "dinhDang", "Ảnh phải là tệp .jpg, .jpeg, .png, .webp hoặc .gif");
        }
    }

    //UC6.1.1 them mon: trang thai Dang ban, ton kho ban dau ghi vao lich su (nhap hang)
    @Transactional
    public ThucPham them(ThucPhamForm f) {
        ThucPham t = new ThucPham();
        ganThongTin(t, f);
        t.setTonKho(f.getTonKho());
        t.setTrangThai(TrangThaiThucPham.dangBan);
        t = thucPhamRepo.save(t);
        if (t.getTonKho() > 0) {
            ghiLichSu(t, t.getTonKho(), t.getTonKho(), LyDoTonKho.nhapHang);
        }
        return t;
    }

    //UC6.1.2 sua thong tin, UC6.1.3 sua gia. Don da dat giu nguyen don gia cu (luu trong ChiTietThucPham).
    @Transactional
    public String sua(ThucPhamForm f) {
        ThucPham t = layTheoId(f.getId());
        boolean doiGia = t.getGiaBan().compareTo(f.getGiaBan()) != 0;
        ganThongTin(t, f);
        return doiGia
                ? "Đã cập nhật món. Giá mới chỉ áp dụng cho các đơn đặt từ bây giờ, đơn đã đặt giữ giá cũ."
                : "Đã cập nhật thông tin món.";
    }

    private static void ganThongTin(ThucPham t, ThucPhamForm f) {
        t.setTenMon(f.getTenMon().trim());
        t.setLoai(f.getLoai());
        t.setThanhPhan(f.getThanhPhan() == null || f.getThanhPhan().isBlank() ? null : f.getThanhPhan().trim());
        t.setGiaBan(f.getGiaBan());
        t.setHinhAnh(f.getHinhAnh() == null || f.getHinhAnh().isBlank() ? null : f.getHinhAnh().trim());
    }

    //UC6.1.4 ngung ban / ban lai
    @Transactional
    public String doiTrangThai(Integer id) {
        ThucPham t = layTheoId(id);
        if (t.getTrangThai() == TrangThaiThucPham.dangBan) {
            t.setTrangThai(TrangThaiThucPham.ngungBan);
            return "Đã ngừng bán \"" + t.getTenMon() + "\". Món không còn hiện khi khách đặt vé; các đơn đã đặt không bị ảnh hưởng.";
        }
        t.setTrangThai(TrangThaiThucPham.dangBan);
        String tb = "Đã mở bán lại \"" + t.getTenMon() + "\".";
        if (t.getTonKho() == 0) tb += " Lưu ý: tồn kho đang bằng 0, khách chưa đặt được món này.";
        return tb;
    }

    //Nhap hang (+) hoac dieu chinh ton kho (+/-) sau kiem kho, co ghi lich su ton kho
    @Transactional
    public String capNhatTonKho(Integer id, LyDoTonKho lyDo, Integer soLuong) {
        ThucPham t = layTheoId(id);
        if (lyDo == null || lyDo == LyDoTonKho.ban) {
            throw new NghiepVuException("Chọn lý do: Nhập hàng hoặc Điều chỉnh");
        }
        if (soLuong == null || soLuong == 0) {
            throw new NghiepVuException("Số lượng thay đổi phải khác 0");
        }
        if (lyDo == LyDoTonKho.nhapHang && soLuong < 0) {
            throw new NghiepVuException("Nhập hàng phải là số dương. Muốn giảm tồn kho thì chọn Điều chỉnh.");
        }
        if (Math.abs((long) soLuong) > 1_000_000) {
            throw new NghiepVuException("Số lượng quá lớn");
        }
        if (thucPhamRepo.capNhatTonKho(id, soLuong) == 0) {
            throw new NghiepVuException("Tồn kho hiện có " + t.getTonKho() + ", không thể giảm " + (-soLuong) + ".");
        }
        int sau = thucPhamRepo.docTonKho(id);
        ThucPham moi = layTheoId(id);
        ghiLichSu(moi, soLuong, sau, lyDo);
        String tb = (lyDo == LyDoTonKho.nhapHang ? "Đã nhập thêm " + soLuong : "Đã điều chỉnh " + (soLuong > 0 ? "+" : "") + soLuong)
                + ". Tồn kho hiện tại: " + sau + ".";
        if (sau <= nguongCanhBao()) tb += " Món sắp hết hàng.";
        return tb;
    }

    private void ghiLichSu(ThucPham t, int thayDoi, int tonKhoSau, LyDoTonKho lyDo) {
        LichSuTonKho l = new LichSuTonKho();
        l.setThucPham(t);
        l.setNguoiThucHien(taiKhoanRepo.getReferenceById(nguoiDung.layId()));
        l.setSoLuongThayDoi(thayDoi);
        l.setTonKhoSau(tonKhoSau);
        l.setLyDo(lyDo);
        l.setThoiDiem(LocalDateTime.now());
        lichSuRepo.save(l);
    }

    @Transactional(readOnly = true)
    public Page<LichSuTonKho> lichSuTonKho(Integer id, int trang) {
        return lichSuRepo.findByThucPham_Id(id, PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG,
                Sort.by(Sort.Direction.DESC, "thoiDiem", "id")));
    }
}
