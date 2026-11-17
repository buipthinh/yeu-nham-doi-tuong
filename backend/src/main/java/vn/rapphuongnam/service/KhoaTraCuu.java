package vn.rapphuongnam.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

//UC6.2.2: nhap sai ma ve nhieu lan lien tiep thi tam khoa chuc nang tra cuu theo ma ve (luu trong session)
@Component
public class KhoaTraCuu {
    public static final int SO_LAN_TOI_DA = 5;
    public static final int SO_PHUT_KHOA = 5;
    private static final String SO_LAN = "traCuuSai";
    private static final String KHOA_DEN = "traCuuKhoaDen";

    private final HttpSession session;

    public KhoaTraCuu(HttpSession session) {
        this.session = session;
    }

    //So giay con bi khoa, 0 neu khong bi khoa
    public long conKhoaGiay() {
        Object o = session.getAttribute(KHOA_DEN);
        if (o instanceof LocalDateTime den) {
            long s = Duration.between(LocalDateTime.now(), den).getSeconds();
            if (s > 0) return s;
            session.removeAttribute(KHOA_DEN);
        }
        return 0;
    }

    //Ghi nhan mot lan sai, tra ve so lan con lai truoc khi bi khoa
    public int ghiSai() {
        Object o = session.getAttribute(SO_LAN);
        int n = (o instanceof Integer i ? i : 0) + 1;
        if (n >= SO_LAN_TOI_DA) {
            session.setAttribute(KHOA_DEN, LocalDateTime.now().plusMinutes(SO_PHUT_KHOA));
            session.removeAttribute(SO_LAN);
            return 0;
        }
        session.setAttribute(SO_LAN, n);
        return SO_LAN_TOI_DA - n;
    }

    public void datLai() {
        session.removeAttribute(SO_LAN);
    }
}
