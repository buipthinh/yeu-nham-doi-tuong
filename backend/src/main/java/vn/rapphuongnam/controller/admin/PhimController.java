package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.PhimForm;
import vn.rapphuongnam.entity.DoTuoi;
import vn.rapphuongnam.entity.Phim;
import vn.rapphuongnam.entity.TrangThaiPhim;
import vn.rapphuongnam.service.NghiepVuException;
import vn.rapphuongnam.service.PhimService;

import java.time.LocalDate;

//UC2: Quan ly phim (Admin)
@Controller
@RequestMapping("/admin/phim")
public class PhimController {
    private final PhimService phimService;

    public PhimController(PhimService phimService) {
        this.phimService = phimService;
    }

    //Du lieu dung chung cho moi trang trong controller nay
    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "phim");
        model.addAttribute("dsTrangThai", TrangThaiPhim.values());
        model.addAttribute("dsDoTuoi", DoTuoi.values());
    }

    //UC2.4 xem danh sach + UC2.5 tim kiem
    @GetMapping
    public String danhSach(@RequestParam(required = false) String q,
                           @RequestParam(required = false) Integer theLoaiId,
                           @RequestParam(required = false) TrangThaiPhim trangThai,
                           @RequestParam(defaultValue = "0") int trang,
                           Model model) {
        Page<Phim> ketQua = phimService.timKiem(q, theLoaiId, trangThai, trang);
        model.addAttribute("ketQua", ketQua);
        model.addAttribute("dsTheLoai", phimService.dsTheLoai());
        model.addAttribute("q", q);
        model.addAttribute("theLoaiId", theLoaiId);
        model.addAttribute("trangThai", trangThai);
        return "admin/phim/danh-sach";
    }

    //UC2.1 them phim
    @GetMapping("/them")
    public String formThem(Model model) {
        PhimForm f = new PhimForm();
        f.setNgayKhoiChieu(LocalDate.now());
        f.setNgayKetThuc(LocalDate.now().plusDays(30));
        model.addAttribute("phimForm", f);
        model.addAttribute("dsTheLoai", phimService.dsTheLoai());
        return "admin/phim/form";
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("phimForm") PhimForm f, BindingResult kq,
                       Model model, RedirectAttributes ra) {
        f.setId(null);
        return luu(f, kq, model, ra, "Đã thêm phim mới");
    }

    //UC2.2 sua phim
    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Integer id, Model model) {
        model.addAttribute("phimForm", phimService.taoForm(phimService.layTheoId(id)));
        model.addAttribute("dsTheLoai", phimService.dsTheLoai());
        return "admin/phim/form";
    }

    @PostMapping("/{id}/sua")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("phimForm") PhimForm f, BindingResult kq,
                      Model model, RedirectAttributes ra) {
        f.setId(id);
        return luu(f, kq, model, ra, "Đã cập nhật thông tin phim");
    }

    private String luu(PhimForm f, BindingResult kq, Model model, RedirectAttributes ra, String thongBao) {
        phimService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            model.addAttribute("dsTheLoai", phimService.dsTheLoai());
            return "admin/phim/form";
        }
        Phim p = phimService.luu(f);
        ra.addFlashAttribute("thongBao", thongBao);
        return "redirect:/admin/phim/" + p.getId();
    }

    //Xem chi tiet phim
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, Model model) {
        model.addAttribute("phim", phimService.layTheoId(id));
        model.addAttribute("thongKe", phimService.thongKe(id));
        return "admin/phim/chi-tiet";
    }

    //UC2.3 thay doi trang thai phim
    @PostMapping("/{id}/trang-thai")
    public String doiTrangThai(@PathVariable Integer id, @RequestParam TrangThaiPhim trangThai, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", phimService.doiTrangThai(id, trangThai));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/phim/" + id;
    }
}
