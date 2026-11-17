package vn.rapphuongnam.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.dto.LoaiVeForm;
import vn.rapphuongnam.entity.DoiTuong;
import vn.rapphuongnam.entity.LoaiVe;
import vn.rapphuongnam.entity.QuyTacGia;
import vn.rapphuongnam.entity.TrangThaiApDung;
import vn.rapphuongnam.repository.DoiTuongRepository;
import vn.rapphuongnam.repository.LoaiVeRepository;
import vn.rapphuongnam.repository.QuyTacGiaRepository;

import java.util.*;
import java.util.stream.Collectors;

//UC5.2 Quan ly loai ve va doi tuong ap dung
@Service
public class LoaiVeService {
    private final LoaiVeRepository loaiVeRepo;
    private final DoiTuongRepository doiTuongRepo;
    private final QuyTacGiaRepository quyTacRepo;
    private final GiaVeService giaVeService;

    public LoaiVeService(LoaiVeRepository loaiVeRepo, DoiTuongRepository doiTuongRepo,
                         QuyTacGiaRepository quyTacRepo, GiaVeService giaVeService) {
        this.loaiVeRepo = loaiVeRepo;
        this.doiTuongRepo = doiTuongRepo;
        this.quyTacRepo = quyTacRepo;
        this.giaVeService = giaVeService;
    }

    //Ket qua cap nhat doi tuong: can xac nhan (UC5.2.4 buoc canh bao) hoac da luu
    public static class KetQuaDoiTuong {
        private final boolean canXacNhan;
        private final String thongBao;

        KetQuaDoiTuong(boolean canXacNhan, String thongBao) {
            this.canXacNhan = canXacNhan;
            this.thongBao = thongBao;
        }

        public boolean isCanXacNhan() { return canXacNhan; }
        public String getThongBao() { return thongBao; }
    }

    @Transactional(readOnly = true)
    public List<LoaiVe> dsLoaiVe() {
        return loaiVeRepo.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<LoaiVe> dsLoaiVeDangApDung() {
        return loaiVeRepo.findByTrangThaiOrderByIdAsc(TrangThaiApDung.dangApDung);
    }

    public List<DoiTuong> dsDoiTuong() {
        return doiTuongRepo.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public LoaiVe layTheoId(Integer id) {
        return loaiVeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại vé"));
    }

    @Transactional(readOnly = true)
    public long demGhe(Integer id) { return loaiVeRepo.demGhe(id); }

    @Transactional(readOnly = true)
    public long demGhePhongHoatDong(Integer id) { return loaiVeRepo.demGhePhongHoatDong(id); }

    @Transactional(readOnly = true)
    public long demVeDaBan(Integer id) { return loaiVeRepo.demVeDaBan(id); }

    @Transactional(readOnly = true)
    public long demQuyTacDangApDung(Integer id) {
        return quyTacRepo.countByLoaiVe_IdAndTrangThai(id, TrangThaiApDung.dangApDung);
    }

    //So quy tac dang ap dung cua tung doi tuong trong loai ve (hien canh checkbox)
    @Transactional(readOnly = true)
    public Map<Integer, Long> demQuyTacTheoDoiTuong(Integer loaiVeId) {
        return quyTacRepo.findByLoaiVe_IdAndTrangThai(loaiVeId, TrangThaiApDung.dangApDung).stream()
                .collect(Collectors.groupingBy(q -> q.getDoiTuong().getId(), Collectors.counting()));
    }

    public LoaiVeForm taoForm(LoaiVe lv) {
        LoaiVeForm f = new LoaiVeForm();
        f.setId(lv.getId());
        f.setTenLoaiVe(lv.getTenLoaiVe());
        f.setMoTa(lv.getMoTa());
        f.setSoChoMoiVe(lv.getSoChoMoiVe());
        return f;
    }

    @Transactional(readOnly = true)
    public void kiemTra(LoaiVeForm f, Errors errors) {
        if (f.getTenLoaiVe() != null && !f.getTenLoaiVe().isBlank()
                && loaiVeRepo.existsByTenLoaiVeIgnoreCaseAndIdNot(f.getTenLoaiVe().trim(), f.getId() == null ? 0 : f.getId())) {
            errors.rejectValue("tenLoaiVe", "trung", "Tên loại vé đã tồn tại");
        }
        //UC5.2.2: loai ve da gan cho ghe thi khong doi so cho moi ve
        if (f.getId() != null && f.getSoChoMoiVe() != null) {
            LoaiVe cu = layTheoId(f.getId());
            long soGhe = loaiVeRepo.demGhe(f.getId());
            if (!Objects.equals(cu.getSoChoMoiVe(), f.getSoChoMoiVe()) && soGhe > 0) {
                errors.rejectValue("soChoMoiVe", "ghe",
                        "Đã có " + soGhe + " ghế dùng loại vé này, không đổi được số chỗ mỗi vé");
            }
        }
    }

    //UC5.2.1 them (mac dinh ap dung cho Nguoi lon) / UC5.2.2 sua
    @Transactional
    public LoaiVe luu(LoaiVeForm f) {
        LoaiVe lv = f.getId() == null ? new LoaiVe() : layTheoId(f.getId());
        lv.setTenLoaiVe(f.getTenLoaiVe().trim());
        lv.setMoTa(f.getMoTa() == null || f.getMoTa().isBlank() ? null : f.getMoTa().trim());
        lv.setSoChoMoiVe(f.getSoChoMoiVe());
        if (f.getId() == null) {
            lv.setTrangThai(TrangThaiApDung.dangApDung);
            lv.getDoiTuong().add(nguoiLon());
        }
        return loaiVeRepo.save(lv);
    }

    //UC5.2.3 doi trang thai loai ve
    @Transactional
    public String doiTrangThai(Integer id) {
        LoaiVe lv = layTheoId(id);
        if (lv.isDangApDung()) {
            long ghe = loaiVeRepo.demGhePhongHoatDong(id);
            if (ghe > 0) {
                throw new NghiepVuException("Còn " + ghe + " ghế loại \"" + lv.getTenLoaiVe()
                        + "\" trong các phòng đang hoạt động. Đổi loại các ghế đó trước khi ngừng áp dụng.");
            }
            List<QuyTacGia> ds = quyTacRepo.findByLoaiVe_IdAndTrangThai(id, TrangThaiApDung.dangApDung);
            ds.forEach(giaVeService::ngungApDung);
            lv.setTrangThai(TrangThaiApDung.ngungApDung);
            return "Đã ngừng áp dụng loại vé \"" + lv.getTenLoaiVe() + "\""
                    + (ds.isEmpty() ? "." : " và " + ds.size() + " quy tắc giá của loại vé này.");
        }
        lv.setTrangThai(TrangThaiApDung.dangApDung);
        return "Đã áp dụng lại loại vé \"" + lv.getTenLoaiVe() + "\". Các quy tắc giá cũ vẫn đang ngừng, mở lại ở mục Giá vé nếu cần.";
    }

    //UC5.2.4 thiet lap doi tuong ap dung. Nguoi lon luon duoc chon.
    //Go doi tuong con gia dang ap dung thi phai xac nhan; khi xac nhan cac gia do chuyen sang ngung ap dung.
    //Doi tuong da tung co quy tac gia van giu lien ket trong LoaiVeDoiTuong (khoa ngoai cua QuyTacGia), chi ngung gia.
    @Transactional
    public KetQuaDoiTuong capNhatDoiTuong(Integer id, List<Integer> doiTuongIds, boolean xacNhan) {
        LoaiVe lv = layTheoId(id);
        DoiTuong nl = nguoiLon();
        Set<Integer> chon = new LinkedHashSet<>(doiTuongIds == null ? List.of() : doiTuongIds);
        chon.add(nl.getId());

        List<DoiTuong> boDi = lv.getDoiTuong().stream().filter(d -> !chon.contains(d.getId())).toList();
        Map<Integer, Long> giaDangApDung = demQuyTacTheoDoiTuong(id);
        List<String> canhBao = boDi.stream()
                .filter(d -> giaDangApDung.getOrDefault(d.getId(), 0L) > 0)
                .map(d -> d.getTenDoiTuong() + " (" + giaDangApDung.get(d.getId()) + " quy tắc giá)")
                .toList();
        if (!canhBao.isEmpty() && !xacNhan) {
            return new KetQuaDoiTuong(true, "Đối tượng " + String.join(", ", canhBao)
                    + " còn giá đang áp dụng. Nếu tiếp tục, các quy tắc giá này sẽ chuyển sang Ngừng áp dụng.");
        }

        int soGiaNgung = 0;
        List<String> giuLienKet = new ArrayList<>();
        for (DoiTuong d : boDi) {
            List<QuyTacGia> ds = quyTacRepo.findByLoaiVe_IdAndDoiTuong_IdAndTrangThai(id, d.getId(), TrangThaiApDung.dangApDung);
            ds.forEach(giaVeService::ngungApDung);
            soGiaNgung += ds.size();
            if (quyTacRepo.countByLoaiVe_IdAndDoiTuong_Id(id, d.getId()) == 0) {
                lv.getDoiTuong().remove(d);
            } else {
                giuLienKet.add(d.getTenDoiTuong());
            }
        }
        int soThem = 0;
        for (DoiTuong d : doiTuongRepo.findAllById(chon)) {
            if (!lv.coDoiTuong(d.getId())) {
                lv.getDoiTuong().add(d);
                soThem++;
            }
        }
        if (boDi.isEmpty() && soThem == 0) {
            return new KetQuaDoiTuong(false, "Không có thay đổi về đối tượng áp dụng.");
        }
        String tb = "Đã cập nhật đối tượng áp dụng cho loại vé \"" + lv.getTenLoaiVe() + "\".";
        if (soGiaNgung > 0) tb += " Đã ngừng " + soGiaNgung + " quy tắc giá.";
        if (!giuLienKet.isEmpty()) {
            tb += " " + String.join(", ", giuLienKet) + " đã có lịch sử giá nên vẫn hiện trong danh sách, nhưng không còn giá đang áp dụng.";
        }
        return new KetQuaDoiTuong(false, tb);
    }

    //Them doi tuong moi (vi du: Tre em, Nguoi cao tuoi)
    @Transactional
    public DoiTuong themDoiTuong(String ten) {
        String t = ten == null ? "" : ten.trim();
        if (t.isEmpty()) throw new NghiepVuException("Tên đối tượng không được để trống");
        if (t.length() > 50) throw new NghiepVuException("Tên đối tượng tối đa 50 ký tự");
        if (doiTuongRepo.existsByTenDoiTuongIgnoreCase(t)) throw new NghiepVuException("Đối tượng \"" + t + "\" đã có");
        DoiTuong d = new DoiTuong();
        d.setTenDoiTuong(t);
        return doiTuongRepo.save(d);
    }

    private DoiTuong nguoiLon() {
        return doiTuongRepo.findFirstByTenDoiTuongIgnoreCase(DoiTuong.NGUOI_LON)
                .or(() -> doiTuongRepo.findAllByOrderByIdAsc().stream().findFirst())
                .orElseThrow(() -> new NghiepVuException("Chưa có đối tượng \"Người lớn\" trong database"));
    }
}
