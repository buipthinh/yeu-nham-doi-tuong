package vn.rapphuongnam.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.rapphuongnam.entity.DoTuoi;
import vn.rapphuongnam.entity.TrangThaiPhim;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Du lieu tren form them/sua phim (UC2.1, UC2.2)
public class PhimForm {
    private Integer id;

    @NotBlank(message = "Tên phim không được để trống")
    @Size(max = 200, message = "Tên phim tối đa 200 ký tự")
    private String tenPhim;

    @Size(max = 100, message = "Tối đa 100 ký tự")
    private String daoDien;

    @Size(max = 255, message = "Tối đa 255 ký tự")
    private String dienVien;

    @NotNull(message = "Nhập thời lượng")
    @Min(value = 30, message = "Thời lượng từ 30 đến 300 phút")
    @Max(value = 300, message = "Thời lượng từ 30 đến 300 phút")
    private Integer thoiLuong;

    @Size(max = 50, message = "Tối đa 50 ký tự")
    private String ngonNgu = "Tiếng Việt";

    @Size(max = 50, message = "Tối đa 50 ký tự")
    private String phuDe = "Tiếng Anh";

    @NotNull(message = "Chọn ngày khởi chiếu")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKhoiChieu;

    @NotNull(message = "Chọn ngày kết thúc chiếu")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThuc;

    @NotNull(message = "Chọn độ tuổi")
    private DoTuoi doTuoi = DoTuoi.P;

    private String moTa;

    @Size(max = 255, message = "Tối đa 255 ký tự")
    private String poster;

    @Size(max = 255, message = "Tối đa 255 ký tự")
    @Pattern(regexp = "^$|^https?://.+", message = "Link phải bắt đầu bằng http:// hoặc https://")
    private String trailer;

    @NotEmpty(message = "Chọn ít nhất một thể loại")
    private List<Integer> theLoaiIds = new ArrayList<>();

    //Chi dung khi them moi, sua trang thai lam o UC2.3
    private TrangThaiPhim trangThai = TrangThaiPhim.sapChieu;

    public boolean isThemMoi() { return id == null; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    public String getDaoDien() { return daoDien; }
    public void setDaoDien(String daoDien) { this.daoDien = daoDien; }
    public String getDienVien() { return dienVien; }
    public void setDienVien(String dienVien) { this.dienVien = dienVien; }
    public Integer getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(Integer thoiLuong) { this.thoiLuong = thoiLuong; }
    public String getNgonNgu() { return ngonNgu; }
    public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    public String getPhuDe() { return phuDe; }
    public void setPhuDe(String phuDe) { this.phuDe = phuDe; }
    public LocalDate getNgayKhoiChieu() { return ngayKhoiChieu; }
    public void setNgayKhoiChieu(LocalDate ngayKhoiChieu) { this.ngayKhoiChieu = ngayKhoiChieu; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public DoTuoi getDoTuoi() { return doTuoi; }
    public void setDoTuoi(DoTuoi doTuoi) { this.doTuoi = doTuoi; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getTrailer() { return trailer; }
    public void setTrailer(String trailer) { this.trailer = trailer; }
    public List<Integer> getTheLoaiIds() { return theLoaiIds; }
    public void setTheLoaiIds(List<Integer> theLoaiIds) { this.theLoaiIds = theLoaiIds; }
    public TrangThaiPhim getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiPhim trangThai) { this.trangThai = trangThai; }
}
