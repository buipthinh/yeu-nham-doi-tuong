package vn.rapphuongnam.entity;

import jakarta.persistence.*;

//Bang DoiTuong: doi tuong ap dung gia (Nguoi lon, HSSV...)
@Entity
@Table(name = "DoiTuong")
public class DoiTuong {
    public static final String NGUOI_LON = "Người lớn";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenDoiTuong", nullable = false, length = 50)
    private String tenDoiTuong;

    public boolean isNguoiLon() {
        return NGUOI_LON.equalsIgnoreCase(tenDoiTuong);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenDoiTuong() { return tenDoiTuong; }
    public void setTenDoiTuong(String tenDoiTuong) { this.tenDoiTuong = tenDoiTuong; }
}
