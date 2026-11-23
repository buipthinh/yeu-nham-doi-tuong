package vn.rapphuongnam.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.rapphuongnam.controller.admin.DangNhapController;
import vn.rapphuongnam.service.NguoiDungHienTai;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

//Chan cac trang /admin khi chua dang nhap. Nhan vien chi duoc tra cuu ve va xem chi tiet don (UC6.2.2)
public class DangNhapInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession s = req.getSession(false);
        Object id = s == null ? null : s.getAttribute(NguoiDungHienTai.KHOA_SESSION);
        String ctx = req.getContextPath();
        String uri = req.getRequestURI().substring(ctx.length());

        if (id == null) {
            String tiep = uri + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
            String dich = ctx + "/admin/dang-nhap";
            if ("GET".equals(req.getMethod())) dich += "?tiep=" + URLEncoder.encode(tiep, StandardCharsets.UTF_8);
            res.sendRedirect(dich);
            return false;
        }

        if ("nhanVien".equals(s.getAttribute(DangNhapController.VAI_TRO))
                && !uri.equals("/admin/don-hang/tra-cuu") && !uri.matches("/admin/don-hang/\\d+")) {
            res.sendRedirect(ctx + "/admin/don-hang/tra-cuu");
            return false;
        }
        return true;
    }
}
