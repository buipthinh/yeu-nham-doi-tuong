package vn.rapphuongnam.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

//Lay id tai khoan dang dang nhap.
//Dang nhap (UC1) xong thi goi: session.setAttribute(NguoiDungHienTai.KHOA_SESSION, taiKhoan.getId())
//Khi chua co trang dang nhap, tam coi nhu dang dang nhap bang Admin id = 1 (NV0001) de chay thu.
@Component
public class NguoiDungHienTai {
    public static final String KHOA_SESSION = "taiKhoanId";
    private static final Integer TAM_THOI = 1;

    private final HttpSession session;

    public NguoiDungHienTai(HttpSession session) {
        this.session = session;
    }

    public Integer layId() {
        Object id = session.getAttribute(KHOA_SESSION);
        return id instanceof Integer i ? i : TAM_THOI;
    }
}
