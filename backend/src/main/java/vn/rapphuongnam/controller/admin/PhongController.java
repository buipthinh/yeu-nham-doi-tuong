package vn.rapphuongnam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.rapphuongnam.service.NghiepVuException;
import vn.rapphuongnam.service.PhongService;
import vn.rapphuongnam.service.SoDoGheService;

//UC4: Quan ly phong chieu va so do cho ngoi
@Controller
@RequestMapping("/admin/phong")
public class PhongController {
    private final PhongService service;
    private final SoDoGheService soDoGhe;

    public PhongController(PhongService service, SoDoGheService soDoGhe) {
        this.service = service;
        this.soDoGhe = soDoGhe;
    }

    @ModelAttribute
    public void chung(Model model) {
        model.addAttribute("menu", "phong");
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String q, @RequestParam(required = false) String trangThai, Model model) {
        model.addAttribute("ds", service.timKiem(q, trangThai));
        model.addAttribute("q", q);
        model.addAttribute("trangThai", trangThai);
        return "admin/phong/danh-sach";
    }

    //UC4.1
    @PostMapping("/them")
    public String them(@RequestParam String tenPhong, @RequestParam(required = false) String ghiChu, RedirectAttributes ra) {
        try {
            int id = service.them(tenPhong, ghiChu);
            ra.addFlashAttribute("thongBao", "Đã thêm phòng. Hãy thiết lập sơ đồ chỗ ngồi (UC4.4.1)");
            return "redirect:/admin/phong/" + id + "?cheDo=maTran";
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/admin/phong";
        }
    }

    //Trang chi tiet phong + so do ghe. cheDo: xem (UC4.4.4), maTran (4.4.1), loai (4.4.2), trangThai (4.4.3)
    @GetMapping("/{id}")
    public String chiTiet(@PathVariable int id, @RequestParam(defaultValue = "xem") String cheDo,
                          @RequestParam(required = false) Integer loaiVeId, Model model) {
        model.addAttribute("p", service.layTheoId(id));
        model.addAttribute("soDo", soDoGhe.theoPhong(id));
        model.addAttribute("cheDo", cheDo);
        model.addAttribute("dsLoaiVe", service.dsLoaiVe());
        model.addAttribute("loaiVeId", loaiVeId == null ? 1 : loaiVeId);
        return "admin/phong/chi-tiet";
    }

    //UC4.2
    @PostMapping("/{id}/sua")
    public String sua(@PathVariable int id, @RequestParam String tenPhong, @RequestParam(required = false) String ghiChu, RedirectAttributes ra) {
        try {
            service.sua(id, tenPhong, ghiChu);
            ra.addFlashAttribute("thongBao", "Đã cập nhật thông tin phòng");
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/phong/" + id;
    }

    //UC4.3
    @PostMapping("/{id}/trang-thai")
    public String doiTrangThai(@PathVariable int id, @RequestParam String trangThai, RedirectAttributes ra) {
        try {
            ra.addFlashAttribute("thongBao", service.doiTrangThai(id, trangThai));
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/phong/" + id;
    }

    //UC4.4.1
    @PostMapping("/{id}/ma-tran")
    public String maTran(@PathVariable int id, @RequestParam int soHang, @RequestParam int soCot, RedirectAttributes ra) {
        try {
            service.thietLapMaTran(id, soHang, soCot);
            ra.addFlashAttribute("thongBao", "Đã tạo sơ đồ " + soHang + " × " + soCot + " ghế");
            return "redirect:/admin/phong/" + id + "?cheDo=loai";
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
            return "redirect:/admin/phong/" + id + "?cheDo=maTran";
        }
    }

    //UC4.4.2 va UC4.4.3: bam vao mot ghe tren so do
    @PostMapping("/{id}/ghe")
    public String ghe(@PathVariable int id, @RequestParam int gheId, @RequestParam String cheDo,
                      @RequestParam(required = false) Integer loaiVeId, RedirectAttributes ra) {
        try {
            String kq = "loai".equals(cheDo) ? service.doiLoaiGhe(id, gheId, loaiVeId == null ? 1 : loaiVeId) : service.doiTrangThaiGhe(id, gheId);
            ra.addFlashAttribute("thongBao", kq);
        } catch (NghiepVuException e) {
            ra.addFlashAttribute("loi", e.getMessage());
        }
        return "redirect:/admin/phong/" + id + "?cheDo=" + cheDo + (loaiVeId == null ? "" : "&loaiVeId=" + loaiVeId);
    }
}
