package vn.rapphuongnam.khach;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rapphuongnam.entity.KhungGio;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

//UC7.1 dat ve truc tuyen: tim suat (7.1.2), chon ghe va giu ghe (7.1.3), chon bap nuoc (7.1.4),
//thanh toan (7.1.5), ve cua toi (7.1.6); UC11 giai phong ghe giu qua han
@Service
public class DatVeKhachService {
    private static final String KY_TU_MA_VE = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RND = new SecureRandom();

    //Cac cot cua mot don + thong tin suat, phim, phong, khach, danh sach ghe
    private static final String COT_DON = "d.*, s.ngayChieu, s.gioBatDau, s.gioKetThuc, s.phimId, p.tenPhim, p.thoiLuong, p.doTuoi, p.poster, "
            + "ph.tenPhong, tk.hoTen, tk.soDienThoai, tk.email, "
            + "(SELECT STRING_AGG(g.maGhe, ', ') WITHIN GROUP (ORDER BY g.hang, g.cot) FROM Ve v JOIN GheSuatChieu gs ON gs.id = v.gheSuatChieuId "
            + "JOIN Ghe g ON g.id = gs.gheId WHERE v.donHangId = d.id) AS dsGhe, "
            + "CASE WHEN CAST(s.ngayChieu AS DATETIME) + CAST(s.gioKetThuc AS DATETIME) <= GETDATE() THEN 1 ELSE 0 END AS suatDaKetThuc "
            + "FROM DonHang d JOIN SuatChieu s ON s.id = d.suatChieuId JOIN Phim p ON p.id = s.phimId "
            + "JOIN PhongChieu ph ON ph.id = s.phongChieuId JOIN TaiKhoan tk ON tk.id = d.taiKhoanId ";

    private final JdbcTemplate jdbc;

    public DatVeKhachService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //---------- Cai dat, loai ngay, khung gio, gia ----------

    public Map<String, Object> caiDat() {
        return jdbc.queryForMap("SELECT TOP 1 * FROM CaiDat ORDER BY id");
    }

    private int caiDatSo(String cot) {
        Integer v = jdbc.queryForObject("SELECT TOP 1 " + cot + " FROM CaiDat ORDER BY id", Integer.class);
        return v == null ? 0 : v;
    }

    public String loaiNgay(LocalDate ngay) {
        Integer le = jdbc.queryForObject("SELECT COUNT(*) FROM NgayLe WHERE ngay = ?", Integer.class, ngay);
        if (le != null && le > 0) return "ngayLe";
        DayOfWeek d = ngay.getDayOfWeek();
        return (d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY) ? "cuoiTuan" : "ngayThuong";
    }

    public BigDecimal timGia(int loaiVeId, int doiTuongId, String loaiNgay, String khungGio, LocalDate ngay) {
        List<BigDecimal> ds = jdbc.queryForList("SELECT TOP 1 gia FROM QuyTacGia WHERE loaiVeId = ? AND doiTuongId = ? AND loaiNgay = ? "
                        + "AND khungGio = ? AND trangThai = 'dangApDung' AND ngayBatDau <= ? AND (ngayKetThuc IS NULL OR ngayKetThuc >= ?) "
                        + "ORDER BY ngayBatDau DESC",
                BigDecimal.class, loaiVeId, doiTuongId, loaiNgay, khungGio, ngay, ngay);
        return ds.isEmpty() ? null : ds.get(0);
    }

    //---------- UC7.1.2 suat chieu cua phim ----------

    public List<Map<String, Object>> suatCuaPhim(int phimId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT s.id, s.ngayChieu, s.gioBatDau, s.gioKetThuc, ph.tenPhong, "
                + "(SELECT COUNT(*) FROM GheSuatChieu g JOIN Ghe gh ON gh.id = g.gheId WHERE g.suatChieuId = s.id AND g.trangThai = 'trong' "
                + "AND gh.trangThai = 'hoatDong') AS soGheTrong "
                + "FROM SuatChieu s JOIN PhongChieu ph ON ph.id = s.phongChieuId JOIN Phim p ON p.id = s.phimId "
                + "WHERE s.phimId = ? AND s.trangThai = 'moBan' AND ph.trangThai = 'hoatDong' AND p.trangThai <> 'an' "
                + "AND CAST(s.ngayChieu AS DATETIME) + CAST(s.gioBatDau AS DATETIME) > GETDATE() ORDER BY s.ngayChieu, s.gioBatDau", phimId);
        for (Map<String, Object> s : ds) {
            LocalDate ngay = ((java.sql.Date) s.get("ngayChieu")).toLocalDate();
            String ln = loaiNgay(ngay), kg = KhungGio.tuGio(((java.sql.Time) s.get("gioBatDau")).toLocalTime()).name();
            s.put("loaiNgay", ln);
            s.put("khungGio", kg);
            s.put("giaTu", timGia(1, 1, ln, kg, ngay));
        }
        return Json.ds(ds);
    }

    //---------- UC7.1.3 thong tin suat, so do ghe, bang gia ----------

    private Map<String, Object> laySuat(int suatId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT s.*, p.tenPhim, p.thoiLuong, p.doTuoi, p.poster, ph.tenPhong, "
                + "ph.trangThai AS trangThaiPhong, p.trangThai AS trangThaiPhim, "
                + "CASE WHEN CAST(s.ngayChieu AS DATETIME) + CAST(s.gioBatDau AS DATETIME) <= GETDATE() THEN 1 ELSE 0 END AS daBatDau "
                + "FROM SuatChieu s JOIN Phim p ON p.id = s.phimId JOIN PhongChieu ph ON ph.id = s.phongChieuId WHERE s.id = ?", suatId);
        if (ds.isEmpty()) throw new ApiLoi("Không tìm thấy suất chiếu", 404, null);
        Map<String, Object> s = ds.get(0);
        LocalDate ngay = ((java.sql.Date) s.get("ngayChieu")).toLocalDate();
        s.put("loaiNgay", loaiNgay(ngay));
        s.put("khungGio", KhungGio.tuGio(((java.sql.Time) s.get("gioBatDau")).toLocalTime()).name());
        s.put("moBan", "moBan".equals(s.get("trangThai")) && "hoatDong".equals(s.get("trangThaiPhong"))
                && !"an".equals(s.get("trangThaiPhim")) && ((Number) s.get("daBatDau")).intValue() == 0);
        return s;
    }

    public Map<String, Object> thongTinSuat(int suatId) {
        Map<String, Object> s = laySuat(suatId);
        LocalDate ngay = ((java.sql.Date) s.get("ngayChieu")).toLocalDate();
        String ln = (String) s.get("loaiNgay"), kg = (String) s.get("khungGio");
        //Moi loai ve co trong phong: danh sach doi tuong co gia dang ap dung
        List<Map<String, Object>> loaiVe = jdbc.queryForList("SELECT DISTINCT lv.id, lv.tenLoaiVe, lv.soChoMoiVe FROM GheSuatChieu gs "
                + "JOIN Ghe g ON g.id = gs.gheId JOIN LoaiVe lv ON lv.id = g.loaiVeId WHERE gs.suatChieuId = ?", suatId);
        for (Map<String, Object> lv : loaiVe) {
            List<Map<String, Object>> gia = new ArrayList<>();
            for (Map<String, Object> dt : jdbc.queryForList("SELECT dt.id, dt.tenDoiTuong FROM LoaiVeDoiTuong x JOIN DoiTuong dt ON dt.id = x.doiTuongId "
                    + "WHERE x.loaiVeId = ? ORDER BY dt.id", lv.get("id"))) {
                BigDecimal g = timGia(((Number) lv.get("id")).intValue(), ((Number) dt.get("id")).intValue(), ln, kg, ngay);
                if (g != null) {
                    Map<String, Object> m = new LinkedHashMap<>(dt);
                    m.put("gia", g);
                    gia.add(m);
                }
            }
            lv.put("bangGia", gia);
        }
        Map<String, Object> kq = Json.m(s);
        kq.put("loaiVe", loaiVe);
        kq.put("ghe", jdbc.queryForList("SELECT g.id, g.maGhe, g.hang, g.cot, g.loaiVeId, g.trangThai AS trangThaiGhe, gs.trangThai "
                + "FROM GheSuatChieu gs JOIN Ghe g ON g.id = gs.gheId WHERE gs.suatChieuId = ? ORDER BY g.hang, g.cot", suatId));
        kq.put("soVeToiDa", caiDatSo("soVeToiDaMoiDon"));
        kq.put("thoiGianGiuGhe", caiDatSo("thoiGianGiuGhe"));
        return kq;
    }

    //UC7.1.3 giu ghe: giu tat ca ghe cung luc; chi can mot ghe khong con trong thi khong giu ghe nao
    @Transactional
    public int giuGhe(int khId, int suatId, List<Map<String, Object>> ve) {
        Map<String, Object> s = laySuat(suatId);
        if (!Boolean.TRUE.equals(s.get("moBan"))) throw new ApiLoi("Suất chiếu không còn mở bán");
        int max = caiDatSo("soVeToiDaMoiDon");
        if (ve == null || ve.isEmpty()) throw new ApiLoi("Vui lòng chọn ít nhất một ghế");
        if (ve.size() > max) throw new ApiLoi("Mỗi đơn tối đa " + max + " vé");

        LocalDate ngay = ((java.sql.Date) s.get("ngayChieu")).toLocalDate();
        String ln = (String) s.get("loaiNgay"), kg = (String) s.get("khungGio");
        Set<Integer> daChon = new HashSet<>();
        List<Object[]> chiTiet = new ArrayList<>();
        BigDecimal tongVe = BigDecimal.ZERO;
        for (Map<String, Object> v : ve) {
            int gheId = ((Number) v.get("gheId")).intValue();
            int dtId = v.get("doiTuongId") == null ? 1 : ((Number) v.get("doiTuongId")).intValue();
            if (!daChon.add(gheId)) throw new ApiLoi("Ghế bị chọn trùng");
            List<Map<String, Object>> g = jdbc.queryForList("SELECT gs.id AS gscId, g.maGhe, g.loaiVeId, g.trangThai FROM GheSuatChieu gs "
                    + "JOIN Ghe g ON g.id = gs.gheId WHERE gs.suatChieuId = ? AND gs.gheId = ?", suatId, gheId);
            if (g.isEmpty()) throw new ApiLoi("Ghế không thuộc suất chiếu này");
            if (!"hoatDong".equals(g.get(0).get("trangThai"))) throw new ApiLoi("Ghế " + g.get(0).get("maGhe") + " đang bảo trì");
            int lv = ((Number) g.get(0).get("loaiVeId")).intValue();
            BigDecimal gia = timGia(lv, dtId, ln, kg, ngay);
            if (gia == null) throw new ApiLoi("Ghế " + g.get(0).get("maGhe") + " chưa có giá cho đối tượng đã chọn");
            tongVe = tongVe.add(gia);
            chiTiet.add(new Object[]{((Number) g.get(0).get("gscId")).intValue(), lv, dtId, gia});
        }

        LocalDateTime bayGio = LocalDateTime.now();
        LocalDateTime han = bayGio.plusMinutes(caiDatSo("thoiGianGiuGhe"));
        Integer donId = jdbc.queryForObject("INSERT INTO DonHang (taiKhoanId, suatChieuId, maVe, tongTienVe, tongTienThucPham, tongTien, "
                        + "trangThai, thoiGianDat, hanGiuGhe) OUTPUT INSERTED.id VALUES (?, ?, NULL, ?, 0, ?, 'choThanhToan', ?, ?)",
                Integer.class, khId, suatId, tongVe, tongVe, bayGio, han);

        //Chi cap nhat ghe con 'trong'; so dong cap nhat phai bang so ghe chon, neu khong thi rollback ca don
        String dauHoi = String.join(",", Collections.nCopies(chiTiet.size(), "?"));
        List<Object> ts = new ArrayList<>(List.of(donId, han));
        chiTiet.forEach(c -> ts.add(c[0]));
        int n = jdbc.update("UPDATE GheSuatChieu SET trangThai = 'dangGiu', donHangId = ?, hanGiu = ? WHERE id IN (" + dauHoi + ") AND trangThai = 'trong'",
                ts.toArray());
        if (n != chiTiet.size()) {
            List<Object> ts2 = new ArrayList<>();
            chiTiet.forEach(c -> ts2.add(c[0]));
            List<String> trungGhe = jdbc.queryForList("SELECT g.maGhe FROM GheSuatChieu gs JOIN Ghe g ON g.id = gs.gheId WHERE gs.id IN (" + dauHoi + ") "
                    + "AND (gs.trangThai <> 'dangGiu' OR gs.donHangId <> " + donId + ")", String.class, ts2.toArray());
            throw new ApiLoi("Ghế " + String.join(", ", trungGhe) + " vừa có người khác chọn. Vui lòng chọn ghế khác", 409, trungGhe);
        }
        for (Object[] c : chiTiet) {
            jdbc.update("INSERT INTO Ve (donHangId, gheSuatChieuId, loaiVeId, doiTuongId, giaVe) VALUES (?, ?, ?, ?, ?)", donId, c[0], c[1], c[2], c[3]);
        }
        return donId;
    }

    //Don phai cua dung khach, dang cho thanh toan va con han giu ghe
    private Map<String, Object> donDangGiu(int khId, int donId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT * FROM DonHang WITH (UPDLOCK) WHERE id = ? AND taiKhoanId = ?", donId, khId);
        if (ds.isEmpty()) throw new ApiLoi("Không tìm thấy đơn hàng", 404, null);
        Map<String, Object> d = ds.get(0);
        if (!"choThanhToan".equals(d.get("trangThai"))) throw new ApiLoi("Đơn hàng không còn ở trạng thái chờ thanh toán");
        if (((Timestamp) d.get("hanGiuGhe")).toLocalDateTime().isBefore(LocalDateTime.now())) {
            giaiPhong(donId, "hetHan");
            throw new ApiLoi("Đã hết thời gian giữ ghế, ghế đã được trả lại. Vui lòng đặt lại", 410, null);
        }
        return d;
    }

    //---------- UC7.1.4 chon bap nuoc (gui danh sach rong = bo qua) ----------

    public List<Map<String, Object>> dsThucPham() {
        return Json.ds(jdbc.queryForList("SELECT id, tenMon, loai, thanhPhan, giaBan, hinhAnh, tonKho FROM ThucPham WHERE trangThai = 'dangBan' "
                + "ORDER BY CASE loai WHEN 'combo' THEN 1 WHEN 'bap' THEN 2 ELSE 3 END, id"));
    }

    @Transactional(noRollbackFor = ApiLoi.class)
    public void chonThucPham(int khId, int donId, List<Map<String, Object>> mon) {
        donDangGiu(khId, donId);
        Map<Integer, Integer> chon = new LinkedHashMap<>();
        Map<Integer, BigDecimal> gia = new HashMap<>();
        if (mon != null) for (Map<String, Object> x : mon) {
            int id = ((Number) x.get("thucPhamId")).intValue();
            int sl = ((Number) x.get("soLuong")).intValue();
            if (sl <= 0 || chon.containsKey(id)) continue;
            List<Map<String, Object>> tp = jdbc.queryForList("SELECT * FROM ThucPham WHERE id = ?", id);
            if (tp.isEmpty()) throw new ApiLoi("Món không tồn tại");
            if (!"dangBan".equals(tp.get(0).get("trangThai"))) throw new ApiLoi(tp.get(0).get("tenMon") + " đã ngừng bán");
            int ton = ((Number) tp.get(0).get("tonKho")).intValue();
            if (sl > ton) throw new ApiLoi(tp.get(0).get("tenMon") + " chỉ còn " + ton + " phần");
            chon.put(id, sl);
            gia.put(id, (BigDecimal) tp.get(0).get("giaBan"));
        }
        jdbc.update("DELETE FROM ChiTietThucPham WHERE donHangId = ?", donId);
        BigDecimal tongTP = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> e : chon.entrySet()) {
            jdbc.update("INSERT INTO ChiTietThucPham (donHangId, thucPhamId, soLuong, donGia) VALUES (?, ?, ?, ?)", donId, e.getKey(), e.getValue(), gia.get(e.getKey()));
            tongTP = tongTP.add(gia.get(e.getKey()).multiply(BigDecimal.valueOf(e.getValue())));
        }
        jdbc.update("UPDATE DonHang SET tongTienThucPham = ?, tongTien = tongTienVe + ? WHERE id = ?", tongTP, tongTP, donId);
    }

    //---------- UC7.1.5 thanh toan: cong thanh toan mo phong, ketQua = thanhCong / thatBai / huy ----------

    @Transactional(noRollbackFor = ApiLoi.class)
    public void thanhToan(int khId, int donId, String phuongThuc, String ketQua) {
        if (!List.of("momo", "vnpay", "theTinDung").contains(phuongThuc)) throw new ApiLoi("Chọn phương thức thanh toán");
        Map<String, Object> d = donDangGiu(khId, donId);
        BigDecimal tong = (BigDecimal) d.get("tongTien");
        String maGD = (switch (phuongThuc) { case "momo" -> "MOMO"; case "vnpay" -> "VNP"; default -> "CARD"; })
                + String.format("%012d", Math.abs(RND.nextLong() % 1_000_000_000_000L));
        if (!"thanhCong".equals(ketQua)) {
            jdbc.update("INSERT INTO GiaoDich (donHangId, phuongThuc, maGiaoDichCong, soTien, trangThai, thoiGian) VALUES (?, ?, ?, ?, 'thatBai', ?)",
                    donId, phuongThuc, maGD, tong, LocalDateTime.now());
            throw new ApiLoi("huy".equals(ketQua) ? "Bạn đã hủy giao dịch. Ghế vẫn được giữ, bạn có thể thanh toán lại"
                    : "Giao dịch thất bại. Ghế vẫn được giữ, bạn có thể thử lại hoặc đổi phương thức", 402, null);
        }
        //Kiem tra du ton kho truoc (khoa dong mon), roi moi tru
        List<Map<String, Object>> mon = jdbc.queryForList("SELECT c.thucPhamId, c.soLuong, tp.tenMon, tp.tonKho FROM ChiTietThucPham c "
                + "JOIN ThucPham tp WITH (UPDLOCK) ON tp.id = c.thucPhamId WHERE c.donHangId = ?", donId);
        for (Map<String, Object> c : mon) {
            int sl = ((Number) c.get("soLuong")).intValue(), ton = ((Number) c.get("tonKho")).intValue();
            if (sl > ton) throw new ApiLoi(c.get("tenMon") + " chỉ còn " + ton + " phần, vui lòng chọn lại bắp nước");
        }
        LocalDateTime tg = LocalDateTime.now();
        for (Map<String, Object> c : mon) {
            int tp = ((Number) c.get("thucPhamId")).intValue(), sl = ((Number) c.get("soLuong")).intValue();
            jdbc.update("UPDATE ThucPham SET tonKho = tonKho - ? WHERE id = ?", sl, tp);
            Integer ton = jdbc.queryForObject("SELECT tonKho FROM ThucPham WHERE id = ?", Integer.class, tp);
            jdbc.update("INSERT INTO LichSuTonKho (thucPhamId, donHangId, nguoiThucHienId, soLuongThayDoi, tonKhoSau, lyDo, thoiDiem) "
                    + "VALUES (?, ?, NULL, ?, ?, 'ban', ?)", tp, donId, -sl, ton, tg);
        }
        jdbc.update("UPDATE DonHang SET trangThai = 'daThanhToan', maVe = ? WHERE id = ?", sinhMaVe(), donId);
        jdbc.update("UPDATE GheSuatChieu SET trangThai = 'daBan', hanGiu = NULL WHERE donHangId = ? AND trangThai = 'dangGiu'", donId);
        jdbc.update("INSERT INTO GiaoDich (donHangId, phuongThuc, maGiaoDichCong, soTien, trangThai, thoiGian) VALUES (?, ?, ?, ?, 'thanhCong', ?)",
                donId, phuongThuc, maGD, tong, tg);
    }

    //Ma ve 8 ky tu, bo 0/O/1/I de khong doc nham, khong trung
    private String sinhMaVe() {
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) sb.append(KY_TU_MA_VE.charAt(RND.nextInt(KY_TU_MA_VE.length())));
            Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM DonHang WHERE maVe = ?", Integer.class, sb.toString());
            if (n == null || n == 0) return sb.toString();
        }
    }

    //Khach huy don truoc khi thanh toan
    @Transactional
    public void huyDon(int khId, int donId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT trangThai FROM DonHang WHERE id = ? AND taiKhoanId = ?", donId, khId);
        if (ds.isEmpty()) throw new ApiLoi("Không tìm thấy đơn hàng", 404, null);
        if (!"choThanhToan".equals(ds.get(0).get("trangThai"))) throw new ApiLoi("Chỉ hủy được đơn đang chờ thanh toán");
        giaiPhong(donId, "daHuy");
    }

    private void giaiPhong(int donId, String trangThaiMoi) {
        jdbc.update("UPDATE DonHang SET trangThai = ? WHERE id = ? AND trangThai = 'choThanhToan'", trangThaiMoi, donId);
        jdbc.update("UPDATE GheSuatChieu SET trangThai = 'trong', donHangId = NULL, hanGiu = NULL WHERE donHangId = ? AND trangThai = 'dangGiu'", donId);
    }

    //UC11: don cho thanh toan qua han giu ghe -> Het han, ghe tra ve trong
    @Transactional
    public int giaiPhongQuaHan() {
        List<Integer> ds = jdbc.queryForList("SELECT id FROM DonHang WHERE trangThai = 'choThanhToan' AND hanGiuGhe < ?", Integer.class, LocalDateTime.now());
        for (Integer id : ds) giaiPhong(id, "hetHan");
        return ds.size();
    }

    //---------- UC7.1.6 ve cua toi ----------

    public List<Map<String, Object>> donCuaKhach(int khId, String trangThai) {
        List<Object> ts = new ArrayList<>(List.of(khId));
        String loc = "";
        if (trangThai != null && !trangThai.isBlank()) {
            loc = " AND d.trangThai = ?";
            ts.add(trangThai);
        }
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT " + COT_DON + "WHERE d.taiKhoanId = ?" + loc + " ORDER BY d.thoiGianDat DESC", ts.toArray());
        ds.forEach(d -> d.put("trangThaiVe", trangThaiVe(d)));
        return Json.ds(ds);
    }

    //hopLe / daSuDung (suat da ket thuc) / chuaCo (chua thanh toan)
    private static String trangThaiVe(Map<String, Object> d) {
        if (!"daThanhToan".equals(d.get("trangThai"))) return "chuaCo";
        return ((Number) d.get("suatDaKetThuc")).intValue() == 1 ? "daSuDung" : "hopLe";
    }

    //Chi tiet don cua chinh khach: thong tin + ve + bap nuoc + giao dich + so giay giu ghe con lai
    public Map<String, Object> chiTietDon(int khId, int donId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT " + COT_DON + "WHERE d.id = ? AND d.taiKhoanId = ?", donId, khId);
        if (ds.isEmpty()) throw new ApiLoi("Không tìm thấy đơn hàng", 404, null);
        Map<String, Object> d = ds.get(0);
        d.put("trangThaiVe", trangThaiVe(d));
        long conLai = 0;
        if ("choThanhToan".equals(d.get("trangThai"))) {
            conLai = Math.max(0, Duration.between(LocalDateTime.now(), ((Timestamp) d.get("hanGiuGhe")).toLocalDateTime()).getSeconds());
        }
        Map<String, Object> kq = Json.m(d);
        kq.put("conLaiGiay", conLai);
        kq.put("ve", Json.ds(jdbc.queryForList("SELECT v.*, g.maGhe, lv.tenLoaiVe, lv.soChoMoiVe, dt.tenDoiTuong FROM Ve v "
                + "JOIN GheSuatChieu gs ON gs.id = v.gheSuatChieuId JOIN Ghe g ON g.id = gs.gheId "
                + "JOIN LoaiVe lv ON lv.id = v.loaiVeId JOIN DoiTuong dt ON dt.id = v.doiTuongId WHERE v.donHangId = ? ORDER BY g.hang, g.cot", donId)));
        kq.put("thucPham", Json.ds(jdbc.queryForList("SELECT c.*, tp.tenMon, tp.loai, c.soLuong * c.donGia AS thanhTien FROM ChiTietThucPham c "
                + "JOIN ThucPham tp ON tp.id = c.thucPhamId WHERE c.donHangId = ? ORDER BY tp.id", donId)));
        kq.put("giaoDich", Json.ds(jdbc.queryForList("SELECT * FROM GiaoDich WHERE donHangId = ? ORDER BY thoiGian", donId)));
        return kq;
    }
}
