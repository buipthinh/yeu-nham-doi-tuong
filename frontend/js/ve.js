//Ve dien tu / chi tiet mot don (UC7.1.6)
veHeader('ve');
const noiDung = document.getElementById('noiDung');

async function tai() {
    let d;
    try {
        d = await Api.don(thamSo('id'));
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
        return;
    }
    const quayLai = '<a class="muted small" href="ve-cua-toi.html">← Vé của tôi</a>';
    const chiTiet = `<div class="grid" style="gap:0">${d.ve.map(v => `<div class="line"><span><b class="mono">${esc(v.maGhe)}</b> <span class="muted small">${esc(v.tenLoaiVe)} · ${esc(v.tenDoiTuong)}</span></span><span>${tien(v.giaVe)}</span></div>`).join('')}
        ${d.thucPham.map(t => `<div class="line"><span>${esc(t.tenMon)} × ${t.soLuong}</span><span>${tien(t.thanhTien)}</span></div>`).join('')}
        <div class="total" style="padding-top:10px"><span>Tổng cộng</span><span>${tien(d.tongTien)}</span></div></div>`;
    if (d.trangThai === 'daThanhToan') {
        const gd = d.giaoDich.find(g => g.trangThai === 'thanhCong') || {};
        noiDung.innerHTML = `${quayLai}
            ${thamSo('moi') ? '<div class="note ok" style="margin:12px 0">Thanh toán thành công! Đây là vé điện tử của bạn.</div>' : ''}
            <div class="ticket" style="margin-top:12px">
                <div class="l">
                    <div class="row between"><b>Rạp Phương Nam</b>${pill('ve', d.trangThaiVe)}</div>
                    <div><h1>${esc(d.tenPhim)}</h1><span class="age ${esc(d.doTuoi)}">${esc(d.doTuoi)}</span> <span class="muted small">${d.thoiLuong} phút</span></div>
                    <dl class="kv">
                        <dt>Suất chiếu</dt><dd><b>${d.gioBatDau} – ${d.gioKetThuc}</b>, ${thu(d.ngayChieu)} ${ngayVN(d.ngayChieu)}</dd>
                        <dt>Phòng</dt><dd>${esc(d.tenPhong)}</dd>
                        <dt>Ghế</dt><dd class="mono"><b>${esc(d.dsGhe || '')}</b></dd>
                        <dt>Khách hàng</dt><dd>${esc(d.hoTen)} · ${esc(d.soDienThoai)}</dd>
                        <dt>Thanh toán</dt><dd>${tien(d.tongTien)} qua ${esc(TEN.pt[gd.phuongThuc] || '')}<br><span class="muted small">${(gd.thoiGian || '').replace('T', ' ')} · mã GD ${esc(gd.maGiaoDichCong || '')}</span></dd>
                    </dl>
                    ${chiTiet}
                </div>
                <div class="r"><span class="small" style="color:#a39d95">Mã vé</span><span class="code">${esc(d.maVe)}</span>
                    <span class="small" style="color:#a39d95">Đọc mã này cho nhân viên khi cần hỗ trợ</span></div>
            </div>
            <p class="muted small">Vé hợp lệ đến khi suất chiếu kết thúc. Hủy vé sau khi thanh toán và hoàn tiền nằm ngoài phạm vi hệ thống.</p>`;
        return;
    }
    noiDung.innerHTML = `${quayLai}
        <div class="booking" style="margin-top:12px">
            <div class="card pad grid">
                <div class="row"><h1 style="margin:0">Đơn #${d.id}</h1>${pill('don', d.trangThai)}</div>
                <div class="muted">${esc(d.tenPhim)} · ${esc(d.tenPhong)} · ${d.gioBatDau} ${thu(d.ngayChieu)} ${ngayVN(d.ngayChieu)}</div>
                ${d.trangThai === 'choThanhToan'
                    ? `<div class="note warn">Ghế đang được giữ thêm khoảng ${Math.ceil(d.conLaiGiay / 60)} phút. Thanh toán trước khi hết giờ để nhận vé.</div>
                       <div class="row"><a class="btn primary" href="dat-ve.html?don=${d.id}&buoc=thanhToan">Tiếp tục thanh toán</a></div>`
                    : `<div class="note ${d.trangThai === 'hetHan' ? 'bad' : ''}">${d.trangThai === 'hetHan' ? 'Đơn hết hạn vì quá thời gian giữ ghế, ghế đã được trả lại.' : 'Đơn đã bị hủy trước khi thanh toán.'}</div>
                       <div class="row"><a class="btn" href="phim.html?id=${d.phimId}">Đặt lại phim này</a></div>`}
            </div>
            <aside class="card pad">${chiTiet}</aside>
        </div>`;
}
if (canDangNhap()) tai();
