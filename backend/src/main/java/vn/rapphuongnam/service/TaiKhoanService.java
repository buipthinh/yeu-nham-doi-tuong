package vn.rapphuongnam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.DonHangKhach;
import vn.rapphuongnam.dto.TaiKhoanForm;
import vn.rapphuongnam.entity.LoaiTaiKhoan;
import vn.rapphuongnam.entity.TaiKhoan;
import vn.rapphuongnam.entity.TrangThaiTaiKhoan;
import vn.rapphuongnam.repository.DonHangKhachRepository;
import vn.rapphuongnam.repository.TaiKhoanRepository;
import vn.rapphuongnam.util.MatKhauUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

//UC8: Quan ly tai khoan
@Service
public class TaiKhoanService {
    public static final int SO_DONG_MOI_TRANG = 10;
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final TaiKhoanRepository taiKhoanRepo;
    private final DonHangKhachRepository donHangRepo;
    private final NguoiDungHienTai nguoiDung;

    public TaiKhoanService(TaiKhoanRepository taiKhoanRepo, DonHangKhachRepository donHangRepo,
                           NguoiDungHienTai nguoiDung) {
        this.taiKhoanRepo = taiKhoanRepo;
        this.donHangRepo = donHangRepo;
        this.nguoiDung = nguoiDung;
    }

    //UC8.7 tim kiem + phan trang (trang bat dau tu 0)
    @Transactional(readOnly = true)
    public Page<TaiKhoan> timKiem(String q, LoaiTaiKhoan loai, TrangThaiTaiKhoan trangThai, int trang) {
        String tuKhoa = (q == null || q.isBlank()) ? null : q.trim();
        PageRequest pr = PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG, Sort.by("id"));
        return taiKhoanRepo.timKiem(tuKhoa, loai, trangThai, pr);
    }

    @Transactional(readOnly = true)
    public TaiKhoan layTheoId(Integer id) {
        return taiKhoanRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản"));
    }

    //UC8.4 buoc 3: don da dat cua khach hang
    public List<DonHangKhach> donHangCuaKhach(Integer taiKhoanId) {
        return donHangRepo.theoTaiKhoan(taiKhoanId);
    }

    public boolean laChinhMinh(Integer taiKhoanId) {
        return Objects.equals(nguoiDung.layId(), taiKhoanId);
    }

    //Do du lieu vao form khi sua
    public TaiKhoanForm taoForm(TaiKhoan t) {
        TaiKhoanForm f = new TaiKhoanForm();
        f.setId(t.getId());
        f.setHoTen(t.getHoTen());
        f.setLoaiTaiKhoan(t.getLoaiTaiKhoan());
        f.setNgaySinh(t.getNgaySinh());
        f.setGioiTinh(t.getGioiTinh());
        f.setSoDienThoai(t.getSoDienThoai() == null ? null : t.getSoDienThoai().trim());
        f.setEmail(t.getEmail());
        f.setDiaChi(t.getDiaChi());
        f.setNgayTuyenDung(t.getNgayTuyenDung());
        f.setMaTaiKhoan(t.getMaTaiKhoan());
        f.setNoiBo(t.isNoiBo());
        return f;
    }

    //Quy tac khong viet duoc bang annotation, loi gan vao dung o nhap
    @Transactional(readOnly = true)
    public void kiemTra(TaiKhoanForm f, Errors errors) {
        int boQuaId = f.getId() == null ? 0 : f.getId();
        if (f.isThemMoi()) {
            if (f.getLoaiTaiKhoan() == null || !f.getLoaiTaiKhoan().isNoiBo()) {
                errors.rejectValue("loaiTaiKhoan", "loai", "Chọn chức vụ Admin hoặc Nhân viên");
            }
            String email = f.getEmail() == null ? "" : f.getEmail().trim();
            if (email.isEmpty()) {
                errors.rejectValue("email", "trong", "Nhập email");
            } else if (!EMAIL.matcher(email).matches()) {
                errors.rejectValue("email", "dang", "Email không hợp lệ");
            } else if (taiKhoanRepo.existsByEmailIgnoreCaseAndIdNot(email, boQuaId)) {
                errors.rejectValue("email", "trung", "Email đã được dùng cho tài khoản khác");
            }
        }
        if (f.getSoDienThoai() != null && !errors.hasFieldErrors("soDienThoai")
                && taiKhoanRepo.existsBySoDienThoaiAndIdNot(f.getSoDienThoai().trim(), boQuaId)) {
            errors.rejectValue("soDienThoai", "trung", "Số điện thoại đã được dùng cho tài khoản khác");
        }
    }

    //Ket qua tao tai khoan: tai khoan vua luu + mat khau chua ma hoa (chi hien 1 lan)
    public static class KetQuaTao {
        private final TaiKhoan taiKhoan;
        private final String matKhau;

        public KetQuaTao(TaiKhoan taiKhoan, String matKhau) {
            this.taiKhoan = taiKhoan;
            this.matKhau = matKhau;
        }

        public TaiKhoan getTaiKhoan() { return taiKhoan; }
        public String getMatKhau() { return matKhau; }
    }

    //UC8.1: tao tai khoan noi bo
    @Transactional
    public KetQuaTao themNoiBo(TaiKhoanForm f) {
        String matKhauTam = MatKhauUtil.sinhMatKhauTam();
        TaiKhoan t = new TaiKhoan();
        t.setTenDangNhap(sinhMaNhanVien());
        t.setMatKhau(MatKhauUtil.maHoa(matKhauTam));
        t.setLoaiTaiKhoan(f.getLoaiTaiKhoan());
        t.setEmail(f.getEmail().trim().toLowerCase());
        t.setTrangThai(TrangThaiTaiKhoan.hoatDong);
        t.setSoLanNhapSai(0);
        t.setNgayTao(LocalDateTime.now());
        ganThongTin(t, f);
        return new KetQuaTao(taiKhoanRepo.save(t), matKhauTam);
    }

    //UC8.3: sua thong tin ca nhan
    @Transactional
    public TaiKhoan capNhat(TaiKhoanForm f) {
        TaiKhoan t = layTheoId(f.getId());
        ganThongTin(t, f);
        return t;
    }

    private void ganThongTin(TaiKhoan t, TaiKhoanForm f) {
        t.setHoTen(f.getHoTen().trim());
        t.setNgaySinh(f.getNgaySinh());
        t.setGioiTinh(f.getGioiTinh());
        t.setSoDienThoai(f.getSoDienThoai().trim());
        t.setDiaChi(rongThanhNull(f.getDiaChi()));
        //Khach hang khong co ngay tuyen dung (rang buoc CK_TaiKhoan_ngayTuyenDung)
        t.setNgayTuyenDung(t.isNoiBo() ? f.getNgayTuyenDung() : null);
    }

    //UC8.2: khoa / mo khoa, tra ve thong bao
    @Transactional
    public String khoaHoacMo(Integer id) {
        TaiKhoan t = layTheoId(id);
        if (!t.isHoatDong()) {
            t.setTrangThai(TrangThaiTaiKhoan.hoatDong);
            t.setSoLanNhapSai(0);
            t.setKhoaDen(null);
            return "Đã mở khóa tài khoản " + t.getMaTaiKhoan() + ".";
        }
        if (laChinhMinh(id)) {
            throw new NghiepVuException("Không thể khóa chính tài khoản đang đăng nhập");
        }
        if (t.getLoaiTaiKhoan() == LoaiTaiKhoan.admin && demAdminHoatDong() <= 1) {
            throw new NghiepVuException("Không thể khóa tài khoản Admin hoạt động cuối cùng");
        }
        t.setTrangThai(TrangThaiTaiKhoan.biKhoa);
        return "Đã khóa tài khoản " + t.getMaTaiKhoan() + ". Tài khoản này không đăng nhập được nữa.";
    }

    //UC8.5: dat lai mat khau, tra ve mat khau tam (chi hien 1 lan)
    @Transactional
    public String khoiTaoMatKhau(Integer id) {
        TaiKhoan t = layTheoId(id);
        String matKhauTam = MatKhauUtil.sinhMatKhauTam();
        t.setMatKhau(MatKhauUtil.maHoa(matKhauTam));
        t.setSoLanNhapSai(0);
        t.setKhoaDen(null);
        return matKhauTam;
    }

    //UC8.6: doi chuc vu giua Admin va Nhan vien
    @Transactional
    public String doiChucVu(Integer id, LoaiTaiKhoan moi) {
        TaiKhoan t = layTheoId(id);
        if (!t.isNoiBo()) {
            throw new NghiepVuException("Tài khoản khách hàng không có chức vụ");
        }
        if (moi == null || !moi.isNoiBo()) {
            throw new NghiepVuException("Chức vụ phải là Admin hoặc Nhân viên");
        }
        if (moi == t.getLoaiTaiKhoan()) {
            return "Chức vụ không thay đổi";
        }
        if (laChinhMinh(id)) {
            throw new NghiepVuException("Không thể tự đổi chức vụ của chính mình");
        }
        if (t.getLoaiTaiKhoan() == LoaiTaiKhoan.admin && t.isHoatDong() && demAdminHoatDong() <= 1) {
            throw new NghiepVuException("Không thể hạ chức vụ Admin hoạt động cuối cùng");
        }
        t.setLoaiTaiKhoan(moi);
        return "Đã đổi chức vụ thành " + moi.getNhan() + ". Quyền mới có hiệu lực từ lần đăng nhập kế tiếp.";
    }

    private long demAdminHoatDong() {
        return taiKhoanRepo.countByLoaiTaiKhoanAndTrangThai(LoaiTaiKhoan.admin, TrangThaiTaiKhoan.hoatDong);
    }

    //Ma NV tiep theo theo dinh dang du lieu mau: NV0001, NV0002...
    private String sinhMaNhanVien() {
        int max = 0;
        for (String ma : taiKhoanRepo.layCacMaNhanVien()) {
            String so = ma.trim().substring(2);
            if (so.matches("\\d+")) {
                max = Math.max(max, Integer.parseInt(so));
            }
        }
        return String.format("NV%04d", max + 1);
    }

    private static String rongThanhNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
