package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.SuatChieuDong;
import vn.rapphuongnam.dto.SuatChieuForm;
import vn.rapphuongnam.entity.SuatChieu;
import vn.rapphuongnam.entity.TrangThaiSuatChieu;
import vn.rapphuongnam.service.NghiepVuException;
import vn.rapphuongnam.service.PhimService;
import vn.rapphuongnam.service.SuatChieuService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//UC3: Quan ly suat chieu (Admin)
@Controller
@RequestMapping("/admin/suat-chieu")
public class SuatChieuController {
    private final SuatChieuService suatService;
    private final PhimService phimService;

    public SuatChieuController(SuatChieuService suatService, PhimService phimService) {
        this.suatService = suatService;
        this.phimService = phimService;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "suatChieu");
        model.addAttribute("dsTrangThai", TrangThaiSuatChieu.values());
    }

    //UC3.4 xem lich chieu theo ngay / tuan / phong
    @GetMapping
    public String lichChieu(@RequestParam(defaultValue = "ngay") String cheDo,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                            @RequestParam(required = false) Integer phongId,
                            Model model) {
        if (!List.of("ngay", "tuan", "phong").contains(cheDo)) cheDo = "ngay";
        if (ngay == null) ngay = LocalDate.now();
        LocalDate tu = SuatChieuService.tuNgay(cheDo, ngay);
        LocalDate den = SuatChieuService.denNgay(cheDo, ngay);
        int buoc = "tuan".equals(cheDo) ? 7 : 1;

        List<SuatChieuDong> ds = suatService.lichChieu(cheDo, ngay, phongId);
        model.addAttribute("dsSuat", ds);
        if ("tuan".equals(cheDo)) {
            Map<LocalDate, List<SuatChieuDong>> tuan = new LinkedHashMap<>();
            for (int i = 0; i < 7; i++) tuan.put(tu.plusDays(i), new java.util.ArrayList<>());
            for (SuatChieuDong d : ds) tuan.get(d.getSuat().getNgayChieu()).add(d);
            model.addAttribute("tuan", tuan);
        } else if ("phong".equals(cheDo)) {
            model.addAttribute("theoPhong", suatService.theoPhong(ds));
        }
        model.addAttribute("cheDo", cheDo);
        model.addAttribute("ngay", ngay);
        model.addAttribute("tuNgay", tu);
        model.addAttribute("denNgay", den);
        model.addAttribute("ngayTruoc", ngay.minusDays(buoc));
        model.addAttribute("ngaySau", ngay.plusDays(buoc));
        model.addAttribute("homNay", LocalDate.now());
        model.addAttribute("phongId", phongId);
        model.addAttribute("dsPhong", suatService.dsTatCaPhong());
        return "admin/suat-chieu/danh-sach";
    }

    //UC3.5 tim kiem suat chieu
    @GetMapping("/tim-kiem")
    public String timKiem(@RequestParam(required = false) String q,
                          @RequestParam(required = false) Integer phimId,
                          @RequestParam(required = false) Integer phongId,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                          @RequestParam(required = false) TrangThaiSuatChieu trangThai,
                          @RequestParam(defaultValue = "false") boolean anDaKetThuc,
                          @RequestParam(defaultValue = "0") int trang,
                          Model model) {
        Page<SuatChieuDong> ketQua = suatService.timKiem(q, phimId, phongId, tuNgay, denNgay, trangThai, anDaKetThuc, trang);
        model.addAttribute("ketQua", ketQua);
        model.addAttribute("cheDo", "timKiem");
        model.addAttribute("q", q);
        model.addAttribute("phimId", phimId);
        model.addAttribute("phongId", phongId);
        model.addAttribute("tuNgay", tuNgay);
        model.addAttribute("denNgay", denNgay);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("anDaKetThuc", anDaKetThuc);
        model.addAttribute("dsPhong", suatService.dsTatCaPhong());
        model.addAttribute("dsPhim", phimService.phimDeTaoSuat());
        model.addAttribute("ngay", LocalDate.now());
        return "admin/suat-chieu/danh-sach";
    }

    //UC3.1 them suat
    @GetMapping("/them")
    public String formThem(@RequestParam(required = false) Integer phimId,
                           @RequestParam(required = false) Integer phongId,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                           Model model) {
        SuatChieuForm f = new SuatChieuForm();
        f.setPhimId(phimId);
        f.setPhongChieuId(phongId);
        f.setNgayChieu(ngay != null ? ngay : LocalDate.now());
        model.addAttribute("suatChieuForm", f);
        napDuLieuForm(model);
        return "admin/suat-chieu/form";
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("suatChieuForm") SuatChieuForm f, BindingResult kq,
                       Model model, RedirectAttributes ra) {
        f.setId(null);
        return xuLy(f, kq, model, ra, "Thêm suất chiếu thành công");
    }

    //UC3.2 sua suat
    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        SuatChieu s = suatService.layTheoId(id);
        try {
            suatService.kiemTraDuocSua(s);
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/admin/suat-chieu/" + id;
        }
        model.addAttribute("suatChieuForm", suatService.taoForm(s));
        napDuLieuForm(model);
        return "admin/suat-chieu/form";
    }

    @PostMapping("/{id}/sua")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("suatChieuForm") SuatChieuForm f,
                      BindingResult kq, Model model, RedirectAttributes ra) {
        f.setId(id);
        try {
            suatService.kiemTraDuocSua(suatService.layTheoId(id));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/admin/suat-chieu/" + id;
        }
        return xuLy(f, kq, model, ra, "Cập nhật suất chiếu thành công");
    }

    //Buoc 1 (xacNhan=false): kiem tra, hop le thi hien man xac nhan. Buoc 2 (xacNhan=true): luu
    private String xuLy(SuatChieuForm f, BindingResult kq, Model model, RedirectAttributes ra, String thongBao) {
        suatService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            napDuLieuForm(model);
            return "admin/suat-chieu/form";
        }
        if (!f.isXacNhan()) {
            model.addAttribute("xemTruoc", suatService.xemTruoc(f));
            napDuLieuForm(model);
            return "admin/suat-chieu/form";
        }
        try {
            SuatChieu s = f.getId() == null ? suatService.them(f) : suatService.sua(f);
            ra.addFlashAttribute("thongBao", thongBao);
            return "redirect:/admin/suat-chieu/" + s.getId();
        } catch (NghiepVuException e) {
            if (f.getId() != null) {
                ra.addFlashAttribute("loi", e.getMessage());
                return "redirect:/admin/suat-chieu/" + f.getId();
            }
            model.addAttribute("loi", e.getMessage());
            napDuLieuForm(model);
            return "admin/suat-chieu/form";
        }
    }

    private void napDuLieuForm(Model model) {
        model.addAttribute("dsPhim", phimService.phimDeTaoSuat());
        model.addAttribute("dsPhong", suatService.dsPhongDungDuoc());
    }

    //Chi tiet suat
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, Model model) {
        SuatChieu s = suatService.layTheoId(id);
        SuatChieuDong d = suatService.dong(s);
        model.addAttribute("suat", s);
        model.addAttribute("dong", d);
        model.addAttribute("tongGhe", d.getSoGheTrong() + d.getSoGheDaDat());
        model.addAttribute("soDon", suatService.demDon(id));
        model.addAttribute("loaiNgay", suatService.loaiNgay(s.getNgayChieu()));
        model.addAttribute("bangGia", suatService.bangGia(s));
        model.addAttribute("lyDoKhongSua", suatService.lyDoKhongSua(s));
        model.addAttribute("daKetThuc", d.getTinhTrang() == vn.rapphuongnam.entity.TinhTrangSuat.daKetThuc);
        return "admin/suat-chieu/chi-tiet";
    }

    //UC3.3 doi trang thai suat
    @PostMapping("/{id}/trang-thai")
    public String doiTrangThai(@PathVariable Integer id, @RequestParam TrangThaiSuatChieu trangThai, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", suatService.doiTrangThai(id, trangThai));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/suat-chieu/" + id;
    }
}
