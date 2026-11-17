package vn.rapphuongnam.controller.admin;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.rapphuongnam.dto.BaoCaoDoanhThu;
import vn.rapphuongnam.dto.KyBaoCao;
import vn.rapphuongnam.dto.ThongKePhimDong;
import vn.rapphuongnam.entity.Phim;
import vn.rapphuongnam.repository.PhimRepository;
import vn.rapphuongnam.service.BaoCaoService;
import vn.rapphuongnam.service.NghiepVuException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

//UC9: Bao cao va thong ke (Admin)
@Controller
@RequestMapping("/admin/bao-cao")
public class BaoCaoController {
    private final BaoCaoService baoCaoService;
    private final PhimRepository phimRepo;

    public BaoCaoController(BaoCaoService baoCaoService, PhimRepository phimRepo) {
        this.baoCaoService = baoCaoService;
        this.phimRepo = phimRepo;
    }

    @ModelAttribute
    public void duLieuChung(Model model) {
        model.addAttribute("menu", "bao-cao");
    }

    //UC9.1 bao cao doanh thu
    @GetMapping
    public String doanhThu(@RequestParam(defaultValue = "thang") KyBaoCao ky,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                           @RequestParam(required = false) Integer phimId,
                           Model model) {
        LocalDate homNay = LocalDate.now();
        model.addAttribute("dsKy", KyBaoCao.values());
        model.addAttribute("dsPhim", phimRepo.findAll(Sort.by("tenPhim")));
        model.addAttribute("ky", ky);
        model.addAttribute("ngay", ngay == null ? homNay : ngay);
        model.addAttribute("tuNgay", tuNgay == null ? homNay.withDayOfMonth(1) : tuNgay);
        model.addAttribute("denNgay", denNgay == null ? homNay : denNgay);
        model.addAttribute("phimId", phimId);
        try {
            LocalDate[] khoang = baoCaoService.tinhKhoang(ky, ngay, tuNgay, denNgay);
            model.addAttribute("bc", baoCaoService.doanhThu(ky, khoang[0], khoang[1], phimId));
        } catch (NghiepVuException e) {
            model.addAttribute("loi", e.getMessage());
        }
        return "admin/bao-cao/doanh-thu";
    }

    @GetMapping("/xuat")
    public ResponseEntity<byte[]> xuatDoanhThu(@RequestParam(defaultValue = "thang") KyBaoCao ky,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                                               @RequestParam(required = false) Integer phimId) {
        LocalDate[] khoang = baoCaoService.tinhKhoang(ky, ngay, tuNgay, denNgay);
        BaoCaoDoanhThu bc = baoCaoService.doanhThu(ky, khoang[0], khoang[1], phimId);
        String tenPhim = phimId == null ? null : phimRepo.findById(phimId).map(Phim::getTenPhim).orElse(null);
        return taiFile(baoCaoService.csvDoanhThu(bc, tenPhim), "doanh-thu_" + khoang[0] + "_" + khoang[1] + ".csv");
    }

    //UC9.2 muc do quan tam theo phim
    @GetMapping("/phim")
    public String theoPhim(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
                           Model model) {
        YearMonth thang = YearMonth.now();
        LocalDate tu = tuNgay == null ? thang.atDay(1) : tuNgay;
        LocalDate den = denNgay == null ? thang.atEndOfMonth() : denNgay;
        model.addAttribute("tuNgay", tu);
        model.addAttribute("denNgay", den);
        try {
            model.addAttribute("ds", baoCaoService.thongKeTheoPhim(tu, den));
        } catch (NghiepVuException e) {
            model.addAttribute("loi", e.getMessage());
            model.addAttribute("ds", List.of());
        }
        return "admin/bao-cao/phim";
    }

    @GetMapping("/phim/xuat")
    public ResponseEntity<byte[]> xuatTheoPhim(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay) {
        List<ThongKePhimDong> ds = baoCaoService.thongKeTheoPhim(tuNgay, denNgay);
        return taiFile(baoCaoService.csvThongKePhim(ds, tuNgay, denNgay), "quan-tam-theo-phim_" + tuNgay + "_" + denNgay + ".csv");
    }

    private ResponseEntity<byte[]> taiFile(byte[] noiDung, String tenFile) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tenFile + "\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(noiDung);
    }
}
