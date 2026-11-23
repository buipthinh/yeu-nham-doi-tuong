package vn.rapphuongnam.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

//Doc so do ghe (theo phong hoac theo suat chieu) va gom thanh tung hang de ve
@Service
public class SoDoGheService {
    private final JdbcTemplate jdbc;

    public SoDoGheService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    //So do vat ly cua phong (UC4.4.4)
    public List<Map<String, Object>> theoPhong(int phongId) {
        return gomHang(jdbc.queryForList(
                "SELECT g.id, g.maGhe, g.hang, g.cot, g.loaiVeId, g.trangThai, lv.tenLoaiVe, lv.soChoMoiVe "
                        + "FROM Ghe g JOIN LoaiVe lv ON lv.id = g.loaiVeId WHERE g.phongChieuId = ? ORDER BY g.hang, g.cot", phongId));
    }

    //So do theo suat chieu: them trang thai trong / dangGiu / daBan cua tung ghe
    public List<Map<String, Object>> theoSuat(int suatId) {
        return gomHang(danhSachTheoSuat(suatId));
    }

    public List<Map<String, Object>> danhSachTheoSuat(int suatId) {
        return jdbc.queryForList(
                "SELECT g.id, g.maGhe, g.hang, g.cot, g.loaiVeId, g.trangThai, lv.tenLoaiVe, lv.soChoMoiVe, "
                        + "gs.id AS gheSuatChieuId, gs.trangThai AS trangThaiSuat, gs.donHangId, gs.hanGiu "
                        + "FROM GheSuatChieu gs JOIN Ghe g ON g.id = gs.gheId JOIN LoaiVe lv ON lv.id = g.loaiVeId "
                        + "WHERE gs.suatChieuId = ? ORDER BY g.hang, g.cot", suatId);
    }

    private List<Map<String, Object>> gomHang(List<Map<String, Object>> ghe) {
        Map<String, List<Map<String, Object>>> theoHang = new TreeMap<>();
        for (Map<String, Object> g : ghe) {
            String h = g.get("hang").toString().trim();
            g.put("hang", h);
            theoHang.computeIfAbsent(h, k -> new ArrayList<>()).add(g);
        }
        List<Map<String, Object>> kq = new ArrayList<>();
        theoHang.forEach((h, ds) -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("hang", h);
            m.put("ghe", ds);
            kq.add(m);
        });
        return kq;
    }
}
