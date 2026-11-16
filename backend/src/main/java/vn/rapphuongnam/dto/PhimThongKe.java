package vn.rapphuongnam.dto;

import java.math.BigDecimal;

//So lieu nhanh hien o trang chi tiet phim
public record PhimThongKe(long tongSuat, long suatChuaKetThuc, long veDaBan, BigDecimal doanhThuVe) {
}
