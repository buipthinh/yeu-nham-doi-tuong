package vn.rapphuongnam.khach;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import vn.rapphuongnam.service.NghiepVuException;

import java.util.HashMap;
import java.util.Map;

//Moi loi cua API khach hang tra ve JSON dang { "loi": "...", "duLieu": {...} }
@RestControllerAdvice(basePackages = "vn.rapphuongnam.khach")
public class ApiLoiHandler {
    @ExceptionHandler(ApiLoi.class)
    public ResponseEntity<Map<String, Object>> apiLoi(ApiLoi e) {
        Map<String, Object> body = new HashMap<>();
        body.put("loi", e.getMessage());
        if (e.getDuLieu() != null) body.put("duLieu", e.getDuLieu());
        return ResponseEntity.status(e.getMaHttp()).body(body);
    }

    @ExceptionHandler(NghiepVuException.class)
    public ResponseEntity<Map<String, Object>> nghiepVu(NghiepVuException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("loi", e.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> trangThai(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("loi", e.getReason() != null ? e.getReason() : "Không tìm thấy"));
    }

    @ExceptionHandler({IllegalArgumentException.class, ClassCastException.class, NullPointerException.class})
    public ResponseEntity<Map<String, Object>> duLieuSai(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("loi", "Dữ liệu gửi lên không hợp lệ"));
    }
}
