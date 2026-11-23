package vn.rapphuongnam.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

//Chuyen gia tri luu trong database thanh chu tieng Viet + mau nhan.
//Dung trong giao dien: ${@nhan.ten('don', d.trangThai)} va ${@nhan.css('don', d.trangThai)}
@Component("nhan")
public class Nhan {
    private static final Map<String, Map<String, String[]>> BANG = new LinkedHashMap<>();

    private static void them(String nhom, String... cap) {
        Map<String, String[]> m = new LinkedHashMap<>();
        for (int i = 0; i < cap.length; i += 3) m.put(cap[i], new String[]{cap[i + 1], cap[i + 2]});
        BANG.put(nhom, m);
    }

    static {
        them("suat", "moBan", "Mở bán", "ok", "tamNgung", "Tạm ngưng", "warn", "ketThuc", "Đã kết thúc", "mute", "hetVe", "Hết vé", "bad");
        them("phong", "hoatDong", "Hoạt động", "ok", "baoTri", "Bảo trì", "warn", "an", "Ẩn", "mute");
        them("ghe", "hoatDong", "Hoạt động", "ok", "baoTri", "Bảo trì", "warn");
        them("apDung", "dangApDung", "Đang áp dụng", "ok", "ngungApDung", "Ngừng áp dụng", "mute");
        them("mon", "dangBan", "Đang bán", "ok", "ngungBan", "Ngừng bán", "mute");
        them("loaiMon", "bap", "Bắp", "info", "nuoc", "Nước", "info", "combo", "Combo", "info");
        them("don", "choThanhToan", "Chờ thanh toán", "warn", "daThanhToan", "Đã thanh toán", "ok", "daHuy", "Đã hủy", "mute", "hetHan", "Hết hạn", "bad");
        them("gd", "thanhCong", "Thành công", "ok", "thatBai", "Thất bại", "bad");
        them("tk", "hoatDong", "Hoạt động", "ok", "biKhoa", "Bị khóa", "bad");
        them("loaiTk", "khachHang", "Khách hàng", "info", "nhanVien", "Nhân viên", "warn", "admin", "Admin", "bad");
        them("loaiNgay", "ngayThuong", "Ngày thường", "mute", "cuoiTuan", "Cuối tuần", "info", "ngayLe", "Ngày lễ", "warn");
        them("khungGio", "sang", "Sáng (trước 11:00)", "mute", "trua", "Trưa (11:00–13:59)", "mute", "chieu", "Chiều (14:00–17:59)", "mute", "toi", "Tối (từ 18:00)", "mute");
        them("pt", "momo", "MoMo", "info", "vnpay", "VNPay", "info", "theTinDung", "Thẻ tín dụng", "info");
        them("lyDo", "nhapHang", "Nhập hàng", "ok", "ban", "Bán theo đơn", "info", "dieuChinh", "Điều chỉnh", "warn");
        them("thaoTac", "thietLap", "Thiết lập", "ok", "sua", "Sửa giá", "info", "ngungApDung", "Ngừng áp dụng", "mute");
        them("gioiTinh", "nam", "Nam", "mute", "nu", "Nữ", "mute", "khac", "Khác", "mute");
    }

    public String ten(String nhom, Object gt) {
        if (gt == null) return "";
        String[] v = BANG.getOrDefault(nhom, Map.of()).get(gt.toString());
        return v == null ? gt.toString() : v[0];
    }

    public String css(String nhom, Object gt) {
        if (gt == null) return "mute";
        String[] v = BANG.getOrDefault(nhom, Map.of()).get(gt.toString());
        return v == null ? "mute" : v[1];
    }

    //Danh sach (gia tri, nhan) de do vao <select>
    public Map<String, String> ds(String nhom) {
        Map<String, String> kq = new LinkedHashMap<>();
        BANG.getOrDefault(nhom, Map.of()).forEach((k, v) -> kq.put(k, v[0]));
        return kq;
    }

    public static String tenTinh(String nhom, Object gt) {
        if (gt == null) return "";
        String[] v = BANG.getOrDefault(nhom, Map.of()).get(gt.toString());
        return v == null ? gt.toString() : v[0];
    }
}
