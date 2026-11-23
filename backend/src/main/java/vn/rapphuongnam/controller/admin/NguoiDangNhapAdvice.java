package vn.rapphuongnam.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.rapphuongnam.service.NguoiDungHienTai;

import java.util.HashMap;
import java.util.Map;

//Dua thong tin nguoi dang dang nhap vao moi trang admin: ${dangNhap.hoTen}, ${dangNhap.admin}...
//(Thymeleaf 3.1 khong cho doc session truc tiep trong template)
@ControllerAdvice(basePackages = "vn.rapphuongnam.controller.admin")
public class NguoiDangNhapAdvice {
    @ModelAttribute("dangNhap")
    public Map<String, Object> dangNhap(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(NguoiDungHienTai.KHOA_SESSION) == null) return null;
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getAttribute(NguoiDungHienTai.KHOA_SESSION));
        m.put("hoTen", s.getAttribute(DangNhapController.HO_TEN));
        m.put("ma", s.getAttribute(DangNhapController.MA_NV));
        m.put("admin", "admin".equals(s.getAttribute(DangNhapController.VAI_TRO)));
        m.put("tenVaiTro", "admin".equals(s.getAttribute(DangNhapController.VAI_TRO)) ? "Quản trị viên" : "Nhân viên");
        return m;
    }
}
