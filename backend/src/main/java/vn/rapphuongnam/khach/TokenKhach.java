package vn.rapphuongnam.khach;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Token dang nhap cua khach hang. Frontend gui kem header "Authorization: Bearer <token>".
//Luu trong bo nho: khoi dong lai backend thi khach phai dang nhap lai
@Component
public class TokenKhach {
    private final Map<String, KhachDangNhap> tokens = new ConcurrentHashMap<>();
    private final SecureRandom rnd = new SecureRandom();

    public String tao(KhachDangNhap kh) {
        byte[] b = new byte[24];
        rnd.nextBytes(b);
        String t = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
        tokens.put(t, kh);
        return t;
    }

    public String token(HttpServletRequest req) {
        String h = req.getHeader("Authorization");
        return (h != null && h.startsWith("Bearer ")) ? h.substring(7).trim() : null;
    }

    //Bat buoc dang nhap: chua co token hop le thi tra loi 401
    public KhachDangNhap batBuoc(HttpServletRequest req) {
        String t = token(req);
        KhachDangNhap kh = t == null ? null : tokens.get(t);
        if (kh == null) throw new ApiLoi("Vui lòng đăng nhập", 401, null);
        return kh;
    }

    public void capNhat(HttpServletRequest req, KhachDangNhap kh) {
        String t = token(req);
        if (t != null && tokens.containsKey(t)) tokens.put(t, kh);
    }

    public void xoa(HttpServletRequest req) {
        String t = token(req);
        if (t != null) tokens.remove(t);
    }
}
