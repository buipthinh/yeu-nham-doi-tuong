package vn.rapphuongnam.khach;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//Doi kieu ngay gio cua JDBC sang chuoi de frontend doc: ngay "2026-11-17", gio "13:45", thoi diem "2026-11-17T13:45:00"
public final class Json {
    private Json() {}

    public static Map<String, Object> m(Map<String, Object> goc) {
        Map<String, Object> kq = new LinkedHashMap<>();
        goc.forEach((k, v) -> kq.put(k, doi(v)));
        return kq;
    }

    public static List<Map<String, Object>> ds(List<Map<String, Object>> goc) {
        List<Map<String, Object>> kq = new ArrayList<>();
        for (Map<String, Object> r : goc) kq.add(m(r));
        return kq;
    }

    private static Object doi(Object v) {
        if (v instanceof java.sql.Date d) return d.toLocalDate().toString();
        if (v instanceof java.sql.Time t) return t.toLocalTime().toString().substring(0, 5);
        if (v instanceof java.sql.Timestamp ts) return ts.toLocalDateTime().withNano(0).toString();
        if (v instanceof String s) return s.trim();
        return v;
    }
}
