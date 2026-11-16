package vn.rapphuongnam.entity;

import jakarta.persistence.*;

//Bang TheLoai
@Entity
@Table(name = "TheLoai")
public class TheLoai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tenTheLoai", nullable = false, length = 50)
    private String tenTheLoai;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenTheLoai() { return tenTheLoai; }
    public void setTenTheLoai(String tenTheLoai) { this.tenTheLoai = tenTheLoai; }
}
