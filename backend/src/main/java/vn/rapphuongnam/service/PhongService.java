package vn.rapphuongnam.service;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//UC4 quan ly phong chieu va so do cho ngoi
@Service
public class PhongService {
    private static final String SUAT_CHUA_KET_THUC = " (CAST(s.ngayChieu AS DATETIME) + CAST(s.gioKetThuc AS DATETIME) > GETDATE()) ";
    private final JdbcTemplate jdbc;

    public PhongService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //UC4.5 xem danh sach + UC4.6 tim kiem
    public List<Map<String, Object>> timKiem(String q, String trangThai) {
        StringBuilder sql = new StringBuilder("SELECT p.*, "
                + "(SELECT COUNT(*) FROM Ghe g WHERE g.phongChieuId = p.id) AS soGhe, "
                + "(SELECT COUNT(*) FROM Ghe g WHERE g.phongChieuId = p.id AND g.loaiVeId IN (SELECT id FROM LoaiVe WHERE soChoMoiVe = 2)) AS soGheDoi, "
                + "(SELECT COUNT(*) FROM Ghe g WHERE g.phongChieuId = p.id AND g.trangThai = 'baoTri') AS soBaoTri, "
                + "(SELECT COUNT(*) FROM SuatChieu s WHERE s.phongChieuId = p.id AND" + SUAT_CHUA_KET_THUC + ") AS suatSapToi "
                + "FROM PhongChieu p WHERE 1 = 1");
        List<Object> ts = new ArrayList<>();
        if (q != null && !q.isBlank()) { sql.append(" AND p.tenPhong LIKE ?"); ts.add("%" + q.trim() + "%"); }
        if (trangThai != null && !trangThai.isBlank()) { sql.append(" AND p.trangThai = ?"); ts.add(trangThai); }
        sql.append(" ORDER BY p.id");
        return jdbc.queryForList(sql.toString(), ts.toArray());
    }

    public Map<String, Object> layTheoId(int id) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT p.*, "
                + "(SELECT COUNT(*) FROM SuatChieu s WHERE s.phongChieuId = p.id) AS tongSuat, "
                + "(SELECT COUNT(*) FROM SuatChieu s WHERE s.phongChieuId = p.id AND" + SUAT_CHUA_KET_THUC + ") AS suatSapToi "
                + "FROM PhongChieu p WHERE p.id = ?", id);
        if (ds.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng chiếu");
        return ds.get(0);
    }

    private boolean trungTen(String ten, int boQuaId) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM PhongChieu WHERE tenPhong = ? AND id <> ?", Integer.class, ten, boQuaId);
        return n != null && n > 0;
    }

    private static String kiemTraTen(String ten) {
        if (ten == null || ten.isBlank()) throw new NghiepVuException("Tên phòng không được để trống");
        if (ten.trim().length() > 50) throw new NghiepVuException("Tên phòng tối đa 50 ký tự");
        return ten.trim();
    }

    //UC4.1 them phong (chua co ghe, suc chua 0)
    @Transactional
    public int them(String ten, String ghiChu) {
        String t = kiemTraTen(ten);
        if (trungTen(t, 0)) throw new NghiepVuException("Tên phòng đã tồn tại");
        return jdbc.queryForObject("INSERT INTO PhongChieu (tenPhong, soHang, soCot, sucChua, ghiChu, trangThai) OUTPUT INSERTED.id "
                + "VALUES (?, 0, 0, 0, ?, 'hoatDong')", Integer.class, t, rong(ghiChu));
    }

    //UC4.2 sua thong tin phong
    @Transactional
    public void sua(int id, String ten, String ghiChu) {
        layTheoId(id);
        String t = kiemTraTen(ten);
        if (trungTen(t, id)) throw new NghiepVuException("Tên phòng đã tồn tại");
        jdbc.update("UPDATE PhongChieu SET tenPhong = ?, ghiChu = ? WHERE id = ?", t, rong(ghiChu), id);
    }

    //UC4.3 doi trang thai phong
    @Transactional
    public String doiTrangThai(int id, String moi) {
        Map<String, Object> p = layTheoId(id);
        if (!List.of("hoatDong", "baoTri", "an").contains(moi)) throw new NghiepVuException("Trạng thái không hợp lệ");
        if (moi.equals(p.get("trangThai"))) return "Trạng thái không thay đổi";
        String them = "";
        if (!"hoatDong".equals(moi)) {
            Integer coDon = jdbc.queryForObject("SELECT COUNT(*) FROM SuatChieu s WHERE s.phongChieuId = ? AND" + SUAT_CHUA_KET_THUC
                    + "AND EXISTS (SELECT 1 FROM DonHang d WHERE d.suatChieuId = s.id AND d.trangThai IN ('choThanhToan','daThanhToan'))", Integer.class, id);
            if (coDon != null && coDon > 0) throw new NghiepVuException("Phòng còn " + coDon + " suất chiếu đã có khách đặt vé, chưa đổi trạng thái được");
            int n = jdbc.update("UPDATE s SET trangThai = 'tamNgung' FROM SuatChieu s WHERE s.phongChieuId = ? AND s.trangThai = 'moBan' AND" + SUAT_CHUA_KET_THUC, id);
            if (n > 0) them = " Đã tạm ngưng " + n + " suất chiếu sắp tới.";
        }
        jdbc.update("UPDATE PhongChieu SET trangThai = ? WHERE id = ?", moi, id);
        return "Đã cập nhật trạng thái phòng." + them;
    }

    //UC4.4.1 thiet lap ma tran ghe: chi lam duoc khi phong chua tung co suat chieu
    @Transactional
    public void thietLapMaTran(int id, int soHang, int soCot) {
        Map<String, Object> p = layTheoId(id);
        if (soHang < 1 || soHang > 26) throw new NghiepVuException("Số hàng từ 1 đến 26");
        if (soCot < 1 || soCot > 30) throw new NghiepVuException("Số cột từ 1 đến 30");
        if (((Number) p.get("tongSuat")).intValue() > 0)
            throw new NghiepVuException("Phòng đã có suất chiếu nên không tạo lại ma trận được. Hãy chỉnh loại ghế và trạng thái ghế");
        jdbc.update("DELETE FROM Ghe WHERE phongChieuId = ?", id);
        List<Object[]> ds = new ArrayList<>();
        for (int h = 0; h < soHang; h++) {
            String hang = String.valueOf((char) ('A' + h));
            for (int c = 1; c <= soCot; c++) ds.add(new Object[]{id, hang + c, hang, c});
        }
        jdbc.batchUpdate("INSERT INTO Ghe (phongChieuId, loaiVeId, maGhe, hang, cot, trangThai) VALUES (?, 1, ?, ?, ?, 'hoatDong')", ds);
        jdbc.update("UPDATE PhongChieu SET soHang = ?, soCot = ? WHERE id = ?", soHang, soCot, id);
        capNhatSucChua(id);
    }

    //Ghe dang duoc giu hoac da ban cho suat chua ket thuc thi khong doi loai/trang thai duoc
    private Map<String, Object> layGheDeSua(int phongId, int gheId) {
        List<Map<String, Object>> ds = jdbc.queryForList("SELECT * FROM Ghe WHERE id = ? AND phongChieuId = ?", gheId, phongId);
        if (ds.isEmpty()) throw new NghiepVuException("Không tìm thấy ghế");
        Integer dung = jdbc.queryForObject("SELECT COUNT(*) FROM GheSuatChieu gs JOIN SuatChieu s ON s.id = gs.suatChieuId "
                + "WHERE gs.gheId = ? AND gs.trangThai <> 'trong' AND" + SUAT_CHUA_KET_THUC, Integer.class, gheId);
        if (dung != null && dung > 0) throw new NghiepVuException("Ghế " + ds.get(0).get("maGhe") + " đang được giữ hoặc đã bán cho suất chiếu sắp tới");
        return ds.get(0);
    }

    //UC4.4.2 phan loai ghe (Thuong / Doi)
    @Transactional
    public String doiLoaiGhe(int phongId, int gheId, int loaiVeId) {
        Map<String, Object> g = layGheDeSua(phongId, gheId);
        List<Map<String, Object>> lv = jdbc.queryForList("SELECT * FROM LoaiVe WHERE id = ? AND trangThai = 'dangApDung'", loaiVeId);
        if (lv.isEmpty()) throw new NghiepVuException("Loại vé không tồn tại hoặc đã ngừng áp dụng");
        jdbc.update("UPDATE Ghe SET loaiVeId = ? WHERE id = ?", loaiVeId, gheId);
        capNhatSucChua(phongId);
        return "Ghế " + g.get("maGhe") + " → " + lv.get(0).get("tenLoaiVe");
    }

    //UC4.4.3 doi trang thai ghe (Hoat dong / Bao tri)
    @Transactional
    public String doiTrangThaiGhe(int phongId, int gheId) {
        Map<String, Object> g = layGheDeSua(phongId, gheId);
        String moi = "hoatDong".equals(g.get("trangThai")) ? "baoTri" : "hoatDong";
        jdbc.update("UPDATE Ghe SET trangThai = ? WHERE id = ?", moi, gheId);
        capNhatSucChua(phongId);
        return "Ghế " + g.get("maGhe") + " → " + ("baoTri".equals(moi) ? "Bảo trì" : "Hoạt động");
    }

    //Suc chua = tong so cho cua cac ghe dang hoat dong (ghe doi tinh 2)
    public void capNhatSucChua(int phongId) {
        jdbc.update("UPDATE PhongChieu SET sucChua = (SELECT ISNULL(SUM(lv.soChoMoiVe), 0) FROM Ghe g JOIN LoaiVe lv ON lv.id = g.loaiVeId "
                + "WHERE g.phongChieuId = ? AND g.trangThai = 'hoatDong') WHERE id = ?", phongId, phongId);
    }

    public List<Map<String, Object>> dsLoaiVe() {
        return jdbc.queryForList("SELECT * FROM LoaiVe WHERE trangThai = 'dangApDung' ORDER BY id");
    }

    private static String rong(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
