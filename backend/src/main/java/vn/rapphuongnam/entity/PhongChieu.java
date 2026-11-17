package vn.rapphuongnam.entity;

import jakarta.persistence.*;

//Bang PhongChieu (UC4 quan ly phong, o day chi doc)
@Entity
@Table(name = "PhongChieu")
public class PhongChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenPhong", nullable = false, length = 50)
    private String tenPhong;

    @Column(name = "soHang", nullable = false)
    private Integer soHang;

    @Column(name = "soCot", nullable = false)
    private Integer soCot;

    @Column(name = "sucChua", nullable = false)
    private Integer sucChua;

    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 10)
    private TrangThaiPhong trangThai;

    public Integer getId() { return id; }
    public String getTenPhong() { return tenPhong; }
    public Integer getSoHang() { return soHang; }
    public Integer getSoCot() { return soCot; }
    public Integer getSucChua() { return sucChua; }
    public String getGhiChu() { return ghiChu; }
    public TrangThaiPhong getTrangThai() { return trangThai; }
}
