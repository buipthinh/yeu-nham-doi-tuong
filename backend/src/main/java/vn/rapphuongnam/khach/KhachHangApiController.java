package vn.rapphuongnam.khach;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

//API tai khoan cho trang khach: UC1.1 dang ky, UC1.2 dang nhap, UC7.2 thong tin ca nhan
@RestController
@RequestMapping("/api")
public class KhachHangApiController {
    private final KhachHangService service;
    private final TokenKhach tokenKhach;

    public KhachHangApiController(KhachHangService service, TokenKhach tokenKhach) {
        this.service = service;
        this.tokenKhach = tokenKhach;
    }

    private Map<String, Object> phien(KhachDangNhap kh) {
        Map<String, Object> kq = new LinkedHashMap<>();
        kq.put("token", tokenKhach.tao(kh));
        kq.put("taiKhoan", kh);
        return kq;
    }

    //Body: { hoTen, email, soDienThoai, matKhau, nhapLaiMatKhau }
    @PostMapping("/auth/dang-ky")
    public Map<String, Object> dangKy(@RequestBody Map<String, String> f) {
        return phien(service.dangKy(f));
    }

    //Body: { tenDangNhap (email hoac SDT), matKhau }
    @PostMapping("/auth/dang-nhap")
    public Map<String, Object> dangNhap(@RequestBody Map<String, String> f) {
        return phien(service.dangNhap(f.get("tenDangNhap"), f.get("matKhau")));
    }

    @PostMapping("/auth/dang-xuat")
    public Map<String, Object> dangXuat(HttpServletRequest req) {
        tokenKhach.xoa(req);
        return Map.of("ok", true);
    }

    //UC7.2.1
    @GetMapping("/tai-khoan")
    public Map<String, Object> xem(HttpServletRequest req) {
        return service.thongTin(tokenKhach.batBuoc(req).id());
    }

    //UC7.2.2. Body: { hoTen, email, soDienThoai, ngaySinh, gioiTinh, diaChi }
    @PutMapping("/tai-khoan")
    public Map<String, Object> capNhat(@RequestBody Map<String, String> f, HttpServletRequest req) {
        KhachDangNhap kh = service.capNhat(tokenKhach.batBuoc(req).id(), f);
        tokenKhach.capNhat(req, kh);
        Map<String, Object> kq = service.thongTin(kh.id());
        kq.put("taiKhoan", kh);
        return kq;
    }

    //Body: { matKhauCu, matKhauMoi, nhapLaiMatKhau }
    @PutMapping("/tai-khoan/mat-khau")
    public Map<String, Object> doiMatKhau(@RequestBody Map<String, String> f, HttpServletRequest req) {
        service.doiMatKhau(tokenKhach.batBuoc(req).id(), f.get("matKhauCu"), f.get("matKhauMoi"), f.get("nhapLaiMatKhau"));
        return Map.of("ok", true);
    }
}
