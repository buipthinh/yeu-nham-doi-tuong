package vn.rapphuongnam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.rapphuongnam.dto.DonHangLoc;
import vn.rapphuongnam.entity.TrangThaiDonHang;
import vn.rapphuongnam.entity.TrangThaiPhim;
import vn.rapphuongnam.repository.PhimRepository;
import vn.rapphuongnam.service.DonHangService;
import org.springframework.data.domain.Sort;

import java.util.Map;

//UC6.2 Quan ly dat ve (Admin, Nhan vien)
@Controller
@RequestMapping("/admin/don-hang")
public class DonHangController {
    private final DonHangService donHangService;
    private final PhimRepository phimRepo;

    public DonHangController(DonHangService donHangService, PhimRepository phimRepo) {
        this.donHangService = donHangService;
        this.phimRepo = phimRepo;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "don-hang");
        model.addAttribute("dsTrangThai", TrangThaiDonHang.values());
    }

    //UC6.2.1 xem danh sach don hang
    @GetMapping
    public String danhSach(@ModelAttribute("loc") DonHangLoc loc, Model model) {
        model.addAttribute("tab", "danh-sach");
        model.addAttribute("ketQua", donHangService.timKiem(loc));
        //Thymeleaf 3.1 chan T(...) trong template nen tinh san so lieu o day
        Map<TrangThaiDonHang, Long> thongKe = donHangService.demTheoTrangThai();
        model.addAttribute("soChoThanhToan", thongKe.get(TrangThaiDonHang.choThanhToan));
        model.addAttribute("soDaThanhToan", thongKe.get(TrangThaiDonHang.daThanhToan));
        model.addAttribute("soHetHanHuy", thongKe.get(TrangThaiDonHang.hetHan) + thongKe.get(TrangThaiDonHang.daHuy));
        model.addAttribute("donHomNay", donHangService.demDonHomNay());
        model.addAttribute("dsPhim", phimRepo.findAll(Sort.by("tenPhim")).stream()
                .filter(p -> p.getTrangThai() != TrangThaiPhim.an || p.getId().equals(loc.getPhimId())).toList());
        return "admin/don-hang/danh-sach";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Integer id, Model model) {
        model.addAttribute("ct", donHangService.chiTiet(id));
        return "admin/don-hang/chi-tiet";
    }

    //UC6.2.2 tra cuu don hang theo ma don, ma ve, so dien thoai
    @GetMapping("/tra-cuu")
    public String traCuu(@RequestParam(defaultValue = "maVe") String kieu,
                         @RequestParam(required = false) String q,
                         Model model) {
        model.addAttribute("tab", "tra-cuu");
        model.addAttribute("kieu", kieu);
        model.addAttribute("q", q);
        if (q != null) {
            DonHangService.KetQuaTraCuu kq = donHangService.traCuu(kieu, q);
            model.addAttribute("ketQua", kq.getDs());
            model.addAttribute("loi", kq.getLoi());
        }
        return "admin/don-hang/tra-cuu";
    }
}
