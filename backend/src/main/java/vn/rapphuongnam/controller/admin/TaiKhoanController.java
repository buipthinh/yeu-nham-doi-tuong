package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.TaiKhoanForm;
import vn.rapphuongnam.entity.GioiTinh;
import vn.rapphuongnam.entity.LoaiTaiKhoan;
import vn.rapphuongnam.entity.TaiKhoan;
import vn.rapphuongnam.entity.TrangThaiTaiKhoan;
import vn.rapphuongnam.service.NghiepVuException;
import vn.rapphuongnam.service.TaiKhoanService;

//UC8: Quan ly tai khoan (Admin)
@Controller
@RequestMapping("/admin/tai-khoan")
public class TaiKhoanController {
    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "tai-khoan");
        model.addAttribute("dsLoai", LoaiTaiKhoan.values());
        model.addAttribute("dsTrangThai", TrangThaiTaiKhoan.values());
        model.addAttribute("dsGioiTinh", GioiTinh.values());
    }

    //UC8.7 xem danh sach + tim kiem
    @GetMapping
    public String danhSach(@RequestParam(required = false) String q,
                           @RequestParam(required = false) LoaiTaiKhoan loai,
                           @RequestParam(required = false) TrangThaiTaiKhoan trangThai,
                           @RequestParam(defaultValue = "0") int trang,
                           Model model) {
        Page<TaiKhoan> ketQua = taiKhoanService.timKiem(q, loai, trangThai, trang);
        model.addAttribute("ketQua", ketQua);
        model.addAttribute("q", q);
        model.addAttribute("loai", loai);
        model.addAttribute("trangThai", trangThai);
        return "admin/tai-khoan/danh-sach";
    }

    //UC8.1 them tai khoan noi bo
    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("taiKhoanForm", new TaiKhoanForm());
        return "admin/tai-khoan/form";
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("taiKhoanForm") TaiKhoanForm f, BindingResult kq, RedirectAttributes ra) {
        f.setId(null);
        f.setNoiBo(true);
        taiKhoanService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/tai-khoan/form";
        }
        TaiKhoanService.KetQuaTao moi = taiKhoanService.themNoiBo(f);
        ra.addFlashAttribute("thongBao", "Đã tạo tài khoản " + moi.getTaiKhoan().getTenDangNhap() + ".");
        ra.addFlashAttribute("matKhauMoi", moi.getMatKhau());
        return "redirect:/admin/tai-khoan/" + moi.getTaiKhoan().getId();
    }

    //UC8.4 xem chi tiet
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, Model model) {
        TaiKhoan t = taiKhoanService.layTheoId(id);
        model.addAttribute("tk", t);
        model.addAttribute("chinhMinh", taiKhoanService.laChinhMinh(id));
        if (!t.isNoiBo()) {
            model.addAttribute("dsDon", taiKhoanService.donHangCuaKhach(id));
        }
        return "admin/tai-khoan/chi-tiet";
    }

    //UC8.3 sua thong tin
    @GetMapping("/{id}/sua")
    public String formSua(@PathVariable Integer id, Model model) {
        model.addAttribute("taiKhoanForm", taiKhoanService.taoForm(taiKhoanService.layTheoId(id)));
        return "admin/tai-khoan/form";
    }

    @PostMapping("/{id}/sua")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("taiKhoanForm") TaiKhoanForm f,
                      BindingResult kq, RedirectAttributes ra) {
        TaiKhoan cu = taiKhoanService.layTheoId(id);
        f.setId(id);
        f.setMaTaiKhoan(cu.getMaTaiKhoan());
        f.setEmail(cu.getEmail());
        f.setNoiBo(cu.isNoiBo());
        taiKhoanService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return "admin/tai-khoan/form";
        }
        taiKhoanService.capNhat(f);
        ra.addFlashAttribute("thongBao", "Đã cập nhật thông tin tài khoản");
        return "redirect:/admin/tai-khoan/" + id;
    }

    //UC8.2 khoa / mo khoa
    @PostMapping("/{id}/khoa")
    public String khoaHoacMo(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", taiKhoanService.khoaHoacMo(id));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/tai-khoan/" + id;
    }

    //UC8.5 khoi tao mat khau
    @PostMapping("/{id}/mat-khau")
    public String khoiTaoMatKhau(@PathVariable Integer id, RedirectAttributes ra) {
        ra.addFlashAttribute("thongBao", "Đã khởi tạo mật khẩu mới, mật khẩu cũ không còn dùng được.");
        ra.addFlashAttribute("matKhauMoi", taiKhoanService.khoiTaoMatKhau(id));
        return "redirect:/admin/tai-khoan/" + id;
    }

    //UC8.6 doi chuc vu
    @PostMapping("/{id}/chuc-vu")
    public String doiChucVu(@PathVariable Integer id, @RequestParam LoaiTaiKhoan loai, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", taiKhoanService.doiChucVu(id, loai));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/tai-khoan/" + id;
    }
}
