package vn.rapphuongnam.controller.admin;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.dto.QuyTacGiaForm;
import vn.rapphuongnam.entity.*;
import vn.rapphuongnam.service.GiaVeService;
import vn.rapphuongnam.service.LoaiVeService;

import java.time.LocalDate;

//UC5.1 Quan ly gia ve (Admin)
@Controller
@RequestMapping("/admin/gia-ve")
public class GiaVeController {
    private final GiaVeService giaVeService;
    private final LoaiVeService loaiVeService;

    public GiaVeController(GiaVeService giaVeService, LoaiVeService loaiVeService) {
        this.giaVeService = giaVeService;
        this.loaiVeService = loaiVeService;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "gia-ve");
        model.addAttribute("dsLoaiNgay", LoaiNgay.values());
        model.addAttribute("dsKhungGio", KhungGio.values());
        model.addAttribute("dsTrangThai", TrangThaiApDung.values());
    }

    //Danh sach quy tac gia + bang gia dang ap dung hom nay
    @GetMapping
    public String danhSach(@RequestParam(required = false) Integer loaiVeId,
                           @RequestParam(required = false) Integer doiTuongId,
                           @RequestParam(required = false) LoaiNgay loaiNgay,
                           @RequestParam(required = false) KhungGio khungGio,
                           @RequestParam(required = false) TrangThaiApDung trangThai,
                           @RequestParam(defaultValue = "0") int trang,
                           Model model) {
        model.addAttribute("tab", "quy-tac");
        model.addAttribute("ketQua", giaVeService.timKiem(loaiVeId, doiTuongId, loaiNgay, khungGio, trangThai, trang));
        model.addAttribute("bangGia", giaVeService.bangGiaHomNay());
        model.addAttribute("homNay", LocalDate.now());
        model.addAttribute("dsLoaiVe", loaiVeService.dsLoaiVe());
        model.addAttribute("dsDoiTuong", loaiVeService.dsDoiTuong());
        model.addAttribute("loaiVeId", loaiVeId);
        model.addAttribute("doiTuongId", doiTuongId);
        model.addAttribute("loaiNgay", loaiNgay);
        model.addAttribute("khungGio", khungGio);
        model.addAttribute("trangThai", trangThai);
        return "admin/gia-ve/danh-sach";
    }

    //UC5.1.1 thiet lap gia ve
    @GetMapping("/them")
    public String formThem(@RequestParam(required = false) Integer loaiVeId, Model model) {
        QuyTacGiaForm f = new QuyTacGiaForm();
        f.setLoaiVeId(loaiVeId);
        model.addAttribute("quyTacGiaForm", f);
        return moForm(model);
    }

    @PostMapping("/them")
    public String them(@Valid @ModelAttribute("quyTacGiaForm") QuyTacGiaForm f, BindingResult kq, RedirectAttributes ra, Model model) {
        f.setId(null);
        f.setTrangThai(TrangThaiApDung.dangApDung);
        giaVeService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return moForm(model);
        }
        QuyTacGia q = giaVeService.them(f);
        ra.addFlashAttribute("thongBao", "Đã thiết lập quy tắc giá " + q.getMaQuyTac() + ".");
        return "redirect:/admin/gia-ve/" + q.getId();
    }

    //UC5.1.2 sua quy tac gia (kem lich su cua quy tac)
    @GetMapping("/{id}")
    public String formSua(@PathVariable Integer id, Model model) {
        model.addAttribute("quyTacGiaForm", giaVeService.taoForm(giaVeService.layTheoId(id)));
        return moForm(model);
    }

    @PostMapping("/{id}")
    public String sua(@PathVariable Integer id, @Valid @ModelAttribute("quyTacGiaForm") QuyTacGiaForm f,
                      BindingResult kq, RedirectAttributes ra, Model model) {
        QuyTacGia cu = giaVeService.layTheoId(id);
        f.setId(id);
        f.setLoaiVeId(cu.getLoaiVe().getId());
        f.setDoiTuongId(cu.getDoiTuong().getId());
        f.setLoaiNgay(cu.getLoaiNgay());
        f.setKhungGio(cu.getKhungGio());
        f.setMoTaNgan(cu.getMoTaNgan());
        giaVeService.kiemTra(f, kq);
        if (kq.hasErrors()) {
            return moForm(model);
        }
        ra.addFlashAttribute("thongBao", giaVeService.sua(f));
        return "redirect:/admin/gia-ve/" + id;
    }

    private String moForm(Model model) {
        QuyTacGiaForm f = (QuyTacGiaForm) model.getAttribute("quyTacGiaForm");
        model.addAttribute("tab", "quy-tac");
        model.addAttribute("dsLoaiVe", loaiVeService.dsLoaiVeDangApDung());
        model.addAttribute("dsDoiTuong", loaiVeService.dsDoiTuong());
        if (f != null && !f.isThemMoi()) {
            model.addAttribute("lichSu", giaVeService.lichSuCuaQuyTac(f.getId()));
        }
        return "admin/gia-ve/form";
    }

    //UC5.1.3 xem lich su gia
    @GetMapping("/lich-su")
    public String lichSu(@RequestParam(required = false) Integer loaiVeId,
                         @RequestParam(required = false) Integer quyTacGiaId,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                         @RequestParam(defaultValue = "0") int trang,
                         Model model) {
        model.addAttribute("tab", "lich-su");
        if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay)) {
            model.addAttribute("loi", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
            denNgay = null;
        }
        model.addAttribute("ketQua", giaVeService.lichSu(loaiVeId, quyTacGiaId, tuNgay, denNgay, trang));
        model.addAttribute("dsLoaiVe", loaiVeService.dsLoaiVe());
        model.addAttribute("dsQuyTac", giaVeService.quyTacCuaLoaiVe(loaiVeId));
        model.addAttribute("loaiVeId", loaiVeId);
        model.addAttribute("quyTacGiaId", quyTacGiaId);
        model.addAttribute("tuNgay", tuNgay);
        model.addAttribute("denNgay", denNgay);
        return "admin/gia-ve/lich-su";
    }
}
