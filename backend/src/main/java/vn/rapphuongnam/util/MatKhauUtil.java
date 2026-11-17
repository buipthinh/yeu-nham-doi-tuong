package vn.rapphuongnam.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

//Ma hoa mat khau SHA-256 dang hex, giong du lieu mau trong database (admin123 -> 240be518...).
//Dang ky / dang nhap (UC1) va tao tai khoan (UC8) phai cung dung lop nay.
public final class MatKhauUtil {
    private static final String KY_TU = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private MatKhauUtil() {
    }

    public static String maHoa(String matKhau) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(matKhau.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean khop(String matKhauNhap, String matKhauDaMaHoa) {
        return matKhauDaMaHoa != null && maHoa(matKhauNhap).equalsIgnoreCase(matKhauDaMaHoa);
    }

    //Mat khau tam ngau nhien, bo cac ky tu de nham (0/O, 1/l/I)
    public static String sinhMatKhauTam() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(KY_TU.charAt(RANDOM.nextInt(KY_TU.length())));
        }
        return sb.toString();
    }
}
