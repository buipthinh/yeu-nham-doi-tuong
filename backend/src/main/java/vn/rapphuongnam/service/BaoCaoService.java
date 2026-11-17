package vn.rapphuongnam.service;

import org.springframework.stereotype.Service;
import vn.rapphuongnam.dto.BaoCaoDoanhThu;
import vn.rapphuongnam.dto.DoanhThuMoc;
import vn.rapphuongnam.dto.KyBaoCao;
import vn.rapphuongnam.dto.ThongKePhimDong;
import vn.rapphuongnam.repository.BaoCaoRepository;
import vn.rapphuongnam.repository.BaoCaoRepository.DonDaThanhToan;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//UC9: Bao cao va thong ke
@Service
public class BaoCaoService {
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BaoCaoRepository baoCaoRepo;

    public BaoCaoService(BaoCaoRepository baoCaoRepo) {
        this.baoCaoRepo = baoCaoRepo;
    }

    //Tinh [tuNgay, denNgay] tu ky bao cao. Ky "khoang" dung nguyen 2 ngay nguoi dung chon.
    public LocalDate[] tinhKhoang(KyBaoCao ky, LocalDate ngay, LocalDate tuNgay, LocalDate denNgay) {
        if (ky == KyBaoCao.khoang) {
            if (tuNgay == null || denNgay == null) {
                throw new NghiepVuException("Chọn đủ ngày bắt đầu và ngày kết thúc");
            }
            if (tuNgay.isAfter(denNgay)) {
                throw new NghiepVuException("Ngày bắt đầu không được sau ngày kết thúc");
            }
            if (tuNgay.plusYears(1).isBefore(denNgay)) {
                throw new NghiepVuException("Khoảng ngày tối đa 1 năm, hãy chọn kỳ \"Theo năm\"");
            }
            return new LocalDate[]{tuNgay, denNgay};
        }
        LocalDate d = ngay == null ? LocalDate.now() : ngay;
        return switch (ky) {
            case ngay -> new LocalDate[]{d, d};
            case nam -> new LocalDate[]{d.withDayOfYear(1), d.withDayOfYear(d.lengthOfYear())};
            default -> new LocalDate[]{YearMonth.from(d).atDay(1), YearMonth.from(d).atEndOfMonth()};
        };
    }

    //UC9.1: ky ngay chia theo gio, ky nam chia theo thang, con lai chia theo ngay.
    //Moc khong co don van giu (gia tri 0) de bieu do lien tuc.
    public BaoCaoDoanhThu doanhThu(KyBaoCao ky, LocalDate tuNgay, LocalDate denNgay, Integer phimId) {
        Map<String, DoanhThuMoc> cacMoc = new LinkedHashMap<>();
        if (ky == KyBaoCao.ngay) {
            for (int h = 0; h < 24; h++) {
                cacMoc.put(String.valueOf(h), new DoanhThuMoc(h + "h"));
            }
        } else if (ky == KyBaoCao.nam) {
            for (YearMonth ym = YearMonth.from(tuNgay); !ym.isAfter(YearMonth.from(denNgay)); ym = ym.plusMonths(1)) {
                cacMoc.put(ym.toString(), new DoanhThuMoc("T" + ym.getMonthValue()));
            }
        } else {
            for (LocalDate d = tuNgay; !d.isAfter(denNgay); d = d.plusDays(1)) {
                cacMoc.put(d.toString(), new DoanhThuMoc(String.format("%02d/%02d", d.getDayOfMonth(), d.getMonthValue())));
            }
        }

        for (DonDaThanhToan don : baoCaoRepo.donDaThanhToan(tuNgay, denNgay, phimId)) {
            DoanhThuMoc m = cacMoc.get(khoaMoc(ky, don.thoiGian));
            if (m != null) {
                m.congDon(don.tienVe, don.tienThucPham, don.tongTien, don.soVe);
            }
        }

        DoanhThuMoc tong = new DoanhThuMoc("Tổng");
        BigDecimal max = BigDecimal.ZERO;
        for (DoanhThuMoc m : cacMoc.values()) {
            tong.congMoc(m);
            max = max.max(m.getTongDoanhThu());
        }
        for (DoanhThuMoc m : cacMoc.values()) {
            m.setPhanTram(max.signum() == 0 ? 0
                    : m.getTongDoanhThu().multiply(BigDecimal.valueOf(100)).divide(max, 0, java.math.RoundingMode.HALF_UP).intValue());
        }
        return new BaoCaoDoanhThu(tuNgay, denNgay, List.copyOf(cacMoc.values()), tong);
    }

    private String khoaMoc(KyBaoCao ky, LocalDateTime t) {
        return switch (ky) {
            case ngay -> String.valueOf(t.getHour());
            case nam -> YearMonth.from(t).toString();
            default -> t.toLocalDate().toString();
        };
    }

    //UC9.2
    public List<ThongKePhimDong> thongKeTheoPhim(LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay == null || denNgay == null) {
            throw new NghiepVuException("Chọn đủ ngày bắt đầu và ngày kết thúc");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new NghiepVuException("Ngày bắt đầu không được sau ngày kết thúc");
        }
        List<ThongKePhimDong> ds = baoCaoRepo.thongKeTheoPhim(tuNgay, denNgay);
        int max = ds.stream().mapToInt(ThongKePhimDong::getSoVe).max().orElse(0);
        for (ThongKePhimDong t : ds) {
            t.setPhanTram(max == 0 ? 0 : Math.round(t.getSoVe() * 100f / max));
        }
        return ds;
    }

    //Xuat Excel (UC9.1 buoc 5): file CSV UTF-8 co BOM, Excel mo duoc tieng Viet
    public byte[] csvDoanhThu(BaoCaoDoanhThu bc, String tenPhim) {
        StringBuilder sb = new StringBuilder("﻿");
        sb.append("Báo cáo doanh thu ").append(bc.getTuNgay().format(NGAY)).append(" - ").append(bc.getDenNgay().format(NGAY));
        if (tenPhim != null) sb.append(" - Phim: ").append(o(tenPhim));
        sb.append("\r\n\r\nThời gian,Doanh thu vé,Doanh thu thực phẩm,Tổng doanh thu,Số đơn,Số vé\r\n");
        for (DoanhThuMoc m : bc.getMocCoDon()) {
            dongCsv(sb, m);
        }
        dongCsv(sb, bc.getTong());
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void dongCsv(StringBuilder sb, DoanhThuMoc m) {
        sb.append(o(m.getNhan())).append(',').append(m.getDoanhThuVe().toPlainString()).append(',')
                .append(m.getDoanhThuThucPham().toPlainString()).append(',').append(m.getTongDoanhThu().toPlainString())
                .append(',').append(m.getSoDon()).append(',').append(m.getSoVe()).append("\r\n");
    }

    public byte[] csvThongKePhim(List<ThongKePhimDong> ds, LocalDate tuNgay, LocalDate denNgay) {
        StringBuilder sb = new StringBuilder("﻿");
        sb.append("Mức độ quan tâm theo phim ").append(tuNgay.format(NGAY)).append(" - ").append(denNgay.format(NGAY));
        sb.append("\r\n\r\nHạng,Phim,Số suất chiếu,Số vé bán ra,Số đơn,Ghế đã bán,Tổng ghế,Tỷ lệ lấp đầy (%),Doanh thu vé\r\n");
        int hang = 1;
        for (ThongKePhimDong t : ds) {
            sb.append(hang++).append(',').append(o(t.getTenPhim())).append(',').append(t.getSoSuatChieu()).append(',')
                    .append(t.getSoVe()).append(',').append(t.getSoDon()).append(',').append(t.getSoGheDaBan()).append(',')
                    .append(t.getTongSoGhe()).append(',').append(String.format(java.util.Locale.US, "%.1f", t.getTyLeLapDay()))
                    .append(',').append(t.getDoanhThuVe().toPlainString()).append("\r\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    //Boc o CSV khi co dau phay / ngoac kep
    private static String o(String s) {
        if (s.contains(",") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
