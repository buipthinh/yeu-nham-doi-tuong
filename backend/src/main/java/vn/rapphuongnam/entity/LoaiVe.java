package vn.rapphuongnam.entity;

import jakarta.persistence.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

//Bang LoaiVe (loai ghe), lien ket n-n voi DoiTuong qua bang LoaiVeDoiTuong
@Entity
@Table(name = "LoaiVe")
public class LoaiVe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenLoaiVe", nullable = false, length = 50)
    private String tenLoaiVe;

    @Column(name = "moTa", length = 255)
    private String moTa;

    //TINYINT trong database, chi nhan 1 hoac 2
    @Column(name = "soChoMoiVe", nullable = false)
    private Integer soChoMoiVe;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 12)
    private TrangThaiApDung trangThai = TrangThaiApDung.dangApDung;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "LoaiVeDoiTuong",
            joinColumns = @JoinColumn(name = "loaiVeId"),
            inverseJoinColumns = @JoinColumn(name = "doiTuongId"))
    private Set<DoiTuong> doiTuong = new LinkedHashSet<>();

    public String getTenCacDoiTuong() {
        return doiTuong.stream()
                .sorted(Comparator.comparing(DoiTuong::getId))
                .map(DoiTuong::getTenDoiTuong)
                .collect(Collectors.joining(", "));
    }

    public boolean coDoiTuong(Integer doiTuongId) {
        return doiTuong.stream().anyMatch(d -> d.getId().equals(doiTuongId));
    }

    //Chuoi id doi tuong "1,2" de loc danh sach doi tuong tren form gia
    public String getIdCacDoiTuong() {
        return doiTuong.stream().map(d -> String.valueOf(d.getId())).collect(Collectors.joining(","));
    }

    public boolean isDangApDung() { return trangThai == TrangThaiApDung.dangApDung; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenLoaiVe() { return tenLoaiVe; }
    public void setTenLoaiVe(String tenLoaiVe) { this.tenLoaiVe = tenLoaiVe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public Integer getSoChoMoiVe() { return soChoMoiVe; }
    public void setSoChoMoiVe(Integer soChoMoiVe) { this.soChoMoiVe = soChoMoiVe; }
    public TrangThaiApDung getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiApDung trangThai) { this.trangThai = trangThai; }
    public Set<DoiTuong> getDoiTuong() { return doiTuong; }
    public void setDoiTuong(Set<DoiTuong> doiTuong) { this.doiTuong = doiTuong; }
}
