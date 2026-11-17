package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.ThucPhamForm;
import vn.rapphuongnam.entity.LoaiThucPham;
import vn.rapphuongnam.entity.LyDoTonKho;
import vn.rapphuongnam.entity.ThucPham;
import vn.rapphuongnam.entity.TrangThaiThucPham;
import vn.rapphuongnam.service.NghiepVuException;
import vn.rapphuongnam.service.ThucPhamService;

//UC6.1 Quan ly thuc pham va ton kho (Admin)
@Controller
@RequestMapping("/admin/thuc-pham")
public class ThucPhamController {
    private final ThucPhamService thucPhamService;

    public ThucPhamController(ThucPhamService thucPhamService) {
        this.thucPhamService = thucPhamService;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "thuc-pham");
        model.addAttribute("dsLoai", LoaiThucPham.values());
        model.addAttribute("dsTrangThai", TrangThaiThucPham.values());
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String q,
                           @RequestParam(required = false) LoaiThucPham loai,
                           @RequestParam(required = false) TrangThaiThucPham trangThai,
                           @RequestParam(defaultValue = "false") boolean sapHet,
                           @RequestParam(defaultValue = "0") int trang,
                           Model model) {
        model.addAttribute("ketQua", thucPhamService.timKiem(q, loai, trangThai, sapHet, trang));
        model.addAttribute("nguong", thucPhamService.nguongCanhBao());
        model.addAttribute("soDangBan", thucPhamService.demDangBan());
        model.addAttribute("soSapHet", thucPhamService.demSapHet());
        model.addAttribute("q", q);
        model.addAttribute("loai", loai);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("sapHet", sapHet);
        return "admin/thuc-pham/danh-sach";
    }

    //UC6.1.1 them mon
    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("thucPhamForm", new ThucPhamForm());
        return "admin/thuc-pham/form";
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("thucPhamForm") ThucPhamForm f, BindingResult kq, RedirectAttributes ra) {
        f.setId(null);
        thucPhamService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/thuc-pham/form";
        }
        ThucPham t = thucPhamService.them(f);
        ra.addFlashAttribute("thongBao", "Đã thêm món \"" + t.getTenMon() + "\".");
        return "redirect:/admin/thuc-pham/" + t.getId();
    }

    //Chi tiet mon + nhap hang / dieu chinh ton kho + lich su ton kho
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, @RequestParam(defaultValue = "0") int trang, Model model) {
        ThucPham t = thucPhamService.layTheoId(id);
        model.addAttribute("mon", t);
        model.addAttribute("nguong", thucPhamService.nguongCanhBao());
        model.addAttribute("daBan", thucPhamService.demDaBan(id));
        model.addAttribute("donDangCho", thucPhamService.demDonDangCho(id));
        model.addAttribute("lichSu", thucPhamService.lichSuTonKho(id, trang));
        return "admin/thuc-pham/chi-tiet";
    }

    //UC6.1.2 sua thong tin, UC6.1.3 sua gia
    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Integer id, Model model) {
        model.addAttribute("thucPhamForm", thucPhamService.taoForm(thucPhamService.layTheoId(id)));
        return "admin/thuc-pham/form";
    }

    @PostMapping("/{id}/sua")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("thucPhamForm") ThucPhamForm f,
                      BindingResult kq, RedirectAttributes ra) {
        f.setTonKho(thucPhamService.layTheoId(id).getTonKho());
        f.setId(id);
        thucPhamService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/thuc-pham/form";
        }
        ra.addFlashAttribute("thongBao", thucPhamService.sua(f));
        return "redirect:/admin/thuc-pham/" + id;
    }

    //UC6.1.4 ngung ban / ban lai
    @PostMapping("/{id}/trang-thai")
    public String doiTrangThai(@PathVariable Integer id, RedirectAttributes ra) {
        ra.addFlashAttribute("thongBao", thucPhamService.doiTrangThai(id));
        return "redirect:/admin/thuc-pham/" + id;
    }

    //Nhap hang / dieu chinh ton kho
    @PostMapping("/{id}/ton-kho")
    public String capNhatTonKho(@PathVariable Integer id,
                                @RequestParam(required = false) LyDoTonKho lyDo,
                                @RequestParam(required = false) Integer soLuong,
                                RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", thucPhamService.capNhatTonKho(id, lyDo, soLuong));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/thuc-pham/" + id;
    }
}
