package vn.rapphuongnam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.BangGiaDong;
import vn.rapphuongnam.dto.QuyTacGiaForm;
import vn.rapphuongnam.entity.*;
import vn.rapphuongnam.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//UC5.1 Quan ly gia ve: thiet lap, sua quy tac gia va xem lich su gia
@Service
public class GiaVeService {
    public static final int SO_DONG_MOI_TRANG = 15;
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final QuyTacGiaRepository quyTacRepo;
    private final LichSuGiaRepository lichSuRepo;
    private final LoaiVeRepository loaiVeRepo;
    private final DoiTuongRepository doiTuongRepo;
    private final TaiKhoanRepository taiKhoanRepo;
    private final NguoiDungHienTai nguoiDung;

    public GiaVeService(QuyTacGiaRepository quyTacRepo, LichSuGiaRepository lichSuRepo, LoaiVeRepository loaiVeRepo,
                        DoiTuongRepository doiTuongRepo, TaiKhoanRepository taiKhoanRepo, NguoiDungHienTai nguoiDung) {
        this.quyTacRepo = quyTacRepo;
        this.lichSuRepo = lichSuRepo;
        this.loaiVeRepo = loaiVeRepo;
        this.doiTuongRepo = doiTuongRepo;
        this.taiKhoanRepo = taiKhoanRepo;
        this.nguoiDung = nguoiDung;
    }

    @Transactional(readOnly = true)
    public Page<QuyTacGia> timKiem(Integer loaiVeId, Integer doiTuongId, LoaiNgay loaiNgay, KhungGio khungGio,
                                   TrangThaiApDung trangThai, int trang) {
        Sort sx = Sort.by("loaiVe.id", "doiTuong.id", "id");
        return quyTacRepo.timKiem(loaiVeId, doiTuongId, loaiNgay, khungGio, trangThai,
                PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG, sx));
    }

    @Transactional(readOnly = true)
    public QuyTacGia layTheoId(Integer id) {
        return quyTacRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy quy tắc giá"));
    }

    @Transactional(readOnly = true)
    public long demDangApDung() {
        return quyTacRepo.countByTrangThai(TrangThaiApDung.dangApDung);
    }

    //Bang gia co hieu luc hom nay cho moi cap loai ve (dang ap dung) x doi tuong
    @Transactional(readOnly = true)
    public List<BangGiaDong> bangGiaHomNay() {
        LocalDate homNay = LocalDate.now();
        List<QuyTacGia> hieuLuc = quyTacRepo.findByTrangThai(TrangThaiApDung.dangApDung).stream()
                .filter(q -> q.hieuLucVao(homNay))
                .sorted(Comparator.comparing(QuyTacGia::getNgayBatDau))
                .toList();
        List<BangGiaDong> kq = new ArrayList<>();
        for (LoaiVe lv : loaiVeRepo.findByTrangThaiOrderByIdAsc(TrangThaiApDung.dangApDung)) {
            lv.getDoiTuong().stream().sorted(Comparator.comparing(DoiTuong::getId)).forEach(dt -> {
                BangGiaDong dong = new BangGiaDong(lv.getTenLoaiVe(), dt.getTenDoiTuong());
                //Quy tac bat dau sau ghi de len quy tac bat dau truoc
                hieuLuc.stream()
                        .filter(q -> q.getLoaiVe().getId().equals(lv.getId()) && q.getDoiTuong().getId().equals(dt.getId()))
                        .forEach(q -> dong.dat(q.getLoaiNgay(), q.getKhungGio(), q.getGia()));
                kq.add(dong);
            });
        }
        return kq;
    }

    public QuyTacGiaForm taoForm(QuyTacGia q) {
        QuyTacGiaForm f = new QuyTacGiaForm();
        f.setId(q.getId());
        f.setLoaiVeId(q.getLoaiVe().getId());
        f.setDoiTuongId(q.getDoiTuong().getId());
        f.setLoaiNgay(q.getLoaiNgay());
        f.setKhungGio(q.getKhungGio());
        f.setGia(q.getGia().stripTrailingZeros().scale() <= 0 ? q.getGia().setScale(0) : q.getGia());
        f.setNgayBatDau(q.getNgayBatDau());
        f.setNgayKetThuc(q.getNgayKetThuc());
        f.setTrangThai(q.getTrangThai());
        f.setMoTaNgan(q.getMoTaNgan());
        return f;
    }

    //Kiem tra cac quy tac nghiep vu cua UC5.1.1 / UC5.1.2, loi gan vao o nhap tuong ung
    @Transactional(readOnly = true)
    public void kiemTra(QuyTacGiaForm f, Errors errors) {
        if (f.getNgayBatDau() != null && f.getNgayKetThuc() != null && f.getNgayKetThuc().isBefore(f.getNgayBatDau())) {
            errors.rejectValue("ngayKetThuc", "ngay", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
        }
        LoaiVe lv = f.getLoaiVeId() == null ? null : loaiVeRepo.findById(f.getLoaiVeId()).orElse(null);
        if (f.isThemMoi()) {
            if (f.getLoaiVeId() != null && (lv == null || !lv.isDangApDung())) {
                errors.rejectValue("loaiVeId", "loaiVe", "Chỉ thiết lập giá cho loại vé đang áp dụng");
            } else if (lv != null && f.getDoiTuongId() != null && !lv.coDoiTuong(f.getDoiTuongId())) {
                errors.rejectValue("doiTuongId", "doiTuong", "Đối tượng này chưa được gán cho loại vé " + lv.getTenLoaiVe());
            }
        } else if (f.getTrangThai() == TrangThaiApDung.dangApDung && lv != null && !lv.isDangApDung()) {
            errors.rejectValue("trangThai", "loaiVe", "Loại vé " + lv.getTenLoaiVe() + " đang ngừng áp dụng, không áp dụng lại giá được");
        }
        //Khong chong lan voi quy tac dang ap dung cung loai ve, doi tuong, loai ngay, khung gio
        if (f.getTrangThai() != TrangThaiApDung.ngungApDung && !errors.hasFieldErrors("ngayKetThuc")
                && f.getLoaiVeId() != null && f.getDoiTuongId() != null && f.getLoaiNgay() != null
                && f.getKhungGio() != null && f.getNgayBatDau() != null) {
            LocalDate tu = f.getNgayBatDau();
            LocalDate den = f.getNgayKetThuc();
            quyTacRepo.findByLoaiVe_IdAndDoiTuong_IdAndLoaiNgayAndKhungGioAndTrangThai(
                            f.getLoaiVeId(), f.getDoiTuongId(), f.getLoaiNgay(), f.getKhungGio(), TrangThaiApDung.dangApDung)
                    .stream()
                    .filter(q -> !q.getId().equals(f.getId()))
                    .filter(q -> (den == null || !q.getNgayBatDau().isAfter(den))
                            && (q.getNgayKetThuc() == null || !q.getNgayKetThuc().isBefore(tu)))
                    .findFirst()
                    .ifPresent(q -> errors.rejectValue("ngayBatDau", "chongLan",
                            "Trùng thời gian hiệu lực với quy tắc " + q.getMaQuyTac() + " (" + khoangNgay(q)
                                    + ", giá " + String.format("%,.0f", q.getGia()).replace(',', '.') + " đ)"));
        }
    }

    //UC5.1.1 thiet lap quy tac gia moi
    @Transactional
    public QuyTacGia them(QuyTacGiaForm f) {
        QuyTacGia q = new QuyTacGia();
        q.setLoaiVe(loaiVeRepo.getReferenceById(f.getLoaiVeId()));
        q.setDoiTuong(doiTuongRepo.getReferenceById(f.getDoiTuongId()));
        q.setLoaiNgay(f.getLoaiNgay());
        q.setKhungGio(f.getKhungGio());
        q.setGia(f.getGia());
        q.setNgayBatDau(f.getNgayBatDau());
        q.setNgayKetThuc(f.getNgayKetThuc());
        q.setTrangThai(TrangThaiApDung.dangApDung);
        q = quyTacRepo.save(q);
        ghiLichSu(q, ThaoTacGia.thietLap, null, q.getGia());
        return q;
    }

    //UC5.1.2 sua gia, thoi gian ap dung hoac trang thai; tra ve thong bao ket qua
    @Transactional
    public String sua(QuyTacGiaForm f) {
        QuyTacGia q = layTheoId(f.getId());
        boolean doiGia = q.getGia().compareTo(f.getGia()) != 0;
        boolean doiNgay = !Objects.equals(q.getNgayBatDau(), f.getNgayBatDau()) || !Objects.equals(q.getNgayKetThuc(), f.getNgayKetThuc());
        boolean doiTrangThai = f.getTrangThai() != null && q.getTrangThai() != f.getTrangThai();
        if (!doiGia && !doiNgay && !doiTrangThai) {
            return "Không có thay đổi nào, quy tắc giá giữ nguyên.";
        }
        var giaCu = q.getGia();
        q.setGia(f.getGia());
        q.setNgayBatDau(f.getNgayBatDau());
        q.setNgayKetThuc(f.getNgayKetThuc());
        if (doiTrangThai) q.setTrangThai(f.getTrangThai());
        ThaoTacGia thaoTac = doiTrangThai && f.getTrangThai() == TrangThaiApDung.ngungApDung ? ThaoTacGia.ngungApDung : ThaoTacGia.sua;
        ghiLichSu(q, thaoTac, giaCu, q.getGia());
        if (thaoTac == ThaoTacGia.ngungApDung) {
            return "Đã ngừng áp dụng quy tắc " + q.getMaQuyTac() + ". Các vé đã bán không bị ảnh hưởng.";
        }
        return "Đã cập nhật quy tắc " + q.getMaQuyTac() + ". Giá mới áp dụng cho các đơn đặt từ bây giờ.";
    }

    //Ngung ap dung mot quy tac va ghi lich su (dung khi ngung loai ve hoac go doi tuong)
    @Transactional
    public void ngungApDung(QuyTacGia q) {
        if (q.getTrangThai() == TrangThaiApDung.ngungApDung) return;
        q.setTrangThai(TrangThaiApDung.ngungApDung);
        ghiLichSu(q, ThaoTacGia.ngungApDung, q.getGia(), q.getGia());
    }

    private void ghiLichSu(QuyTacGia q, ThaoTacGia thaoTac, java.math.BigDecimal giaCu, java.math.BigDecimal giaMoi) {
        LichSuGia l = new LichSuGia();
        l.setQuyTacGia(q);
        l.setNguoiThucHien(taiKhoanRepo.getReferenceById(nguoiDung.layId()));
        l.setThaoTac(thaoTac);
        l.setGiaCu(giaCu);
        l.setGiaMoi(giaMoi);
        l.setThoiDiem(LocalDateTime.now());
        lichSuRepo.save(l);
    }

    //UC5.1.3 xem lich su gia
    @Transactional(readOnly = true)
    public Page<LichSuGia> lichSu(Integer loaiVeId, Integer quyTacGiaId, LocalDate tuNgay, LocalDate denNgay, int trang) {
        PageRequest pr = PageRequest.of(Math.max(trang, 0), SO_DONG_MOI_TRANG,
                Sort.by(Sort.Direction.DESC, "thoiDiem", "id"));
        return lichSuRepo.timKiem(loaiVeId, quyTacGiaId,
                tuNgay == null ? null : tuNgay.atStartOfDay(),
                denNgay == null ? null : denNgay.plusDays(1).atStartOfDay(), pr);
    }

    @Transactional(readOnly = true)
    public List<LichSuGia> lichSuCuaQuyTac(Integer quyTacGiaId) {
        return lichSuRepo.findByQuyTacGia_IdOrderByThoiDiemDescIdDesc(quyTacGiaId);
    }

    @Transactional(readOnly = true)
    public List<QuyTacGia> quyTacCuaLoaiVe(Integer loaiVeId) {
        return loaiVeId == null ? List.of() : quyTacRepo.findByLoaiVe_IdOrderByDoiTuong_IdAscLoaiNgayAscKhungGioAscIdDesc(loaiVeId);
    }

    public static String khoangNgay(QuyTacGia q) {
        return q.getNgayBatDau().format(NGAY) + " – " + (q.getNgayKetThuc() == null ? "không thời hạn" : q.getNgayKetThuc().format(NGAY));
    }
}
