package vn.rapphuongnam.khach;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//API dat ve truc tuyen cho trang khach (UC7.1)
@RestController
@RequestMapping("/api")
public class DatVeApiController {
    private final DatVeKhachService service;
    private final TokenKhach tokenKhach;
    private final JdbcTemplate jdbc;

    public DatVeApiController(DatVeKhachService service, TokenKhach tokenKhach, JdbcTemplate jdbc) {
        this.service = service;
        this.tokenKhach = tokenKhach;
        this.jdbc = jdbc;
    }

    //Thong tin rap (dau/cuoi trang)
    @GetMapping("/rap")
    public Map<String, Object> rap() {
        Map<String, Object> c = service.caiDat();
        Map<String, Object> kq = new LinkedHashMap<>();
        for (String k : List.of("tenRap", "diaChiRap", "hotline", "emailRap", "thoiGianGiuGhe", "soVeToiDaMoiDon")) kq.put(k, c.get(k));
        return Json.m(kq);
    }

    @GetMapping("/the-loai")
    public List<Map<String, Object>> theLoai() {
        return jdbc.queryForList("SELECT id, tenTheLoai FROM TheLoai ORDER BY id");
    }

    //UC7.1.2
    @GetMapping("/phim/{id}/suat-chieu")
    public List<Map<String, Object>> suatCuaPhim(@PathVariable int id) {
        return service.suatCuaPhim(id);
    }

    //UC7.1.3 so do ghe + bang gia
    @GetMapping("/suat-chieu/{id}")
    public Map<String, Object> suat(@PathVariable int id) {
        return service.thongTinSuat(id);
    }

    @GetMapping("/thuc-pham")
    public List<Map<String, Object>> thucPham() {
        return service.dsThucPham();
    }

    //Giu ghe, tao don cho thanh toan. Body: { suatChieuId, ve: [{ gheId, doiTuongId }] }
    @PostMapping("/don-hang")
    @SuppressWarnings("unchecked")
    public Map<String, Object> taoDon(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        KhachDangNhap kh = tokenKhach.batBuoc(req);
        if (!(body.get("suatChieuId") instanceof Number suat)) throw new ApiLoi("Thiếu suất chiếu");
        int donId = service.giuGhe(kh.id(), suat.intValue(), (List<Map<String, Object>>) body.get("ve"));
        return service.chiTietDon(kh.id(), donId);
    }

    //UC7.1.6
    @GetMapping("/don-hang")
    public List<Map<String, Object>> donCuaToi(@RequestParam(required = false) String trangThai, HttpServletRequest req) {
        return service.donCuaKhach(tokenKhach.batBuoc(req).id(), trangThai);
    }

    @GetMapping("/don-hang/{id}")
    public Map<String, Object> chiTiet(@PathVariable int id, HttpServletRequest req) {
        return service.chiTietDon(tokenKhach.batBuoc(req).id(), id);
    }

    //UC7.1.4. Body: { mon: [{ thucPhamId, soLuong }] }
    @PutMapping("/don-hang/{id}/thuc-pham")
    @SuppressWarnings("unchecked")
    public Map<String, Object> thucPham(@PathVariable int id, @RequestBody Map<String, Object> body, HttpServletRequest req) {
        KhachDangNhap kh = tokenKhach.batBuoc(req);
        service.chonThucPham(kh.id(), id, (List<Map<String, Object>>) body.get("mon"));
        return service.chiTietDon(kh.id(), id);
    }

    //UC7.1.5. Body: { phuongThuc: momo | vnpay | theTinDung, ketQua: thanhCong | thatBai | huy }
    @PostMapping("/don-hang/{id}/thanh-toan")
    public Map<String, Object> thanhToan(@PathVariable int id, @RequestBody Map<String, String> body, HttpServletRequest req) {
        KhachDangNhap kh = tokenKhach.batBuoc(req);
        service.thanhToan(kh.id(), id, body.get("phuongThuc"), body.getOrDefault("ketQua", "thanhCong"));
        return service.chiTietDon(kh.id(), id);
    }

    @PostMapping("/don-hang/{id}/huy")
    public Map<String, Object> huy(@PathVariable int id, HttpServletRequest req) {
        KhachDangNhap kh = tokenKhach.batBuoc(req);
        service.huyDon(kh.id(), id);
        return service.chiTietDon(kh.id(), id);
    }
}
