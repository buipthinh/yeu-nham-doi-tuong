package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.LoaiVeForm;
import vn.rapphuongnam.entity.LoaiVe;
import vn.rapphuongnam.service.LoaiVeService;
import vn.rapphuongnam.service.NghiepVuException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//UC5.2 Quan ly loai ve va doi tuong (Admin)
@Controller
@RequestMapping("/admin/loai-ve")
public class LoaiVeController {
    private final LoaiVeService loaiVeService;

    public LoaiVeController(LoaiVeService loaiVeService) {
        this.loaiVeService = loaiVeService;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "gia-ve");
        model.addAttribute("tab", "loai-ve");
    }

    @GetMapping
    public String danhSach(Model model) {
        List<LoaiVe> ds = loaiVeService.dsLoaiVe();
        Map<Integer, Long> soGhe = new HashMap<>();
        Map<Integer, Long> soGia = new HashMap<>();
        for (LoaiVe lv : ds) {
            soGhe.put(lv.getId(), loaiVeService.demGhe(lv.getId()));
            soGia.put(lv.getId(), loaiVeService.demQuyTacDangApDung(lv.getId()));
        }
        model.addAttribute("dsLoaiVe", ds);
        model.addAttribute("soGhe", soGhe);
        model.addAttribute("soGia", soGia);
        model.addAttribute("dsDoiTuong", loaiVeService.dsDoiTuong());
        return "admin/loai-ve/danh-sach";
    }

    //UC5.2.1 them loai ve
    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("loaiVeForm", new LoaiVeForm());
        return "admin/loai-ve/form";
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("loaiVeForm") LoaiVeForm f, BindingResult kq, RedirectAttributes ra) {
        f.setId(null);
        loaiVeService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/loai-ve/form";
        }
        LoaiVe lv = loaiVeService.luu(f);
        ra.addFlashAttribute("thongBao", "Đã thêm loại vé \"" + lv.getTenLoaiVe() + "\", mặc định áp dụng cho Người lớn.");
        return "redirect:/admin/loai-ve/" + lv.getId();
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, Model model) {
        LoaiVe lv = loaiVeService.layTheoId(id);
        model.addAttribute("lv", lv);
        model.addAttribute("dsDoiTuong", loaiVeService.dsDoiTuong());
        model.addAttribute("soGiaTheoDoiTuong", loaiVeService.demQuyTacTheoDoiTuong(id));
        model.addAttribute("soGhe", loaiVeService.demGhe(id));
        model.addAttribute("soGhePhongHoatDong", loaiVeService.demGhePhongHoatDong(id));
        model.addAttribute("soGia", loaiVeService.demQuyTacDangApDung(id));
        model.addAttribute("soVe", loaiVeService.demVeDaBan(id));
        return "admin/loai-ve/chi-tiet";
    }

    //UC5.2.2 sua loai ve
    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Integer id, Model model) {
        model.addAttribute("loaiVeForm", loaiVeService.taoForm(loaiVeService.layTheoId(id)));
        return "admin/loai-ve/form";
    }

    @PostMapping("/{id}/sua")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("loaiVeForm") LoaiVeForm f,
                      BindingResult kq, RedirectAttributes ra) {
        loaiVeService.layTheoId(id);
        f.setId(id);
        loaiVeService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/loai-ve/form";
        }
        loaiVeService.luu(f);
        ra.addFlashAttribute("thongBao", "Đã cập nhật loại vé.");
        return "redirect:/admin/loai-ve/" + id;
    }

    //UC5.2.3 doi trang thai
    @PostMapping("/{id}/trang-thai")
    public String doiTrangThai(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", loaiVeService.doiTrangThai(id));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/loai-ve/" + id;
    }

    //UC5.2.4 thiet lap doi tuong ap dung
    @PostMapping("/{id}/doi-tuong")
    public String capNhatDoiTuong(@PathVariable Integer id,
                                  @RequestParam(required = false) List<Integer> doiTuongIds,
                                  @RequestParam(defaultValue = "false") boolean xacNhan,
                                  RedirectAttributes ra) {
        LoaiVeService.KetQuaDoiTuong kq = loaiVeService.capNhatDoiTuong(id, doiTuongIds, xacNhan);
        if (kq.isCanXacNhan()) {
            ra.addFlashAttribute("canXacNhan", kq.getThongBao());
            ra.addFlashAttribute("doiTuongChon", doiTuongIds == null ? List.of() : doiTuongIds);
        } else {
            ra.addFlashAttribute("thongBao", kq.getThongBao());
        }
        return "redirect:/admin/loai-ve/" + id;
    }

    //Them doi tuong moi vao danh muc
    @PostMapping("/doi-tuong")
    public String themDoiTuong(@RequestParam String tenDoiTuong, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", "Đã thêm đối tượng \"" + loaiVeService.themDoiTuong(tenDoiTuong).getTenDoiTuong()
                    + "\". Vào từng loại vé để gán đối tượng này.");
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/loai-ve";
    }
}
