//Gioi thieu rap, phong chieu, quy dinh dat ve va lien he
veHeader('gt');
const noiDung = document.getElementById('noiDung');
const TRANG_THAI_PHONG = { hoatDong: ['Đang hoạt động', 'ok'], baoTri: ['Đang bảo trì', 'warn'] };
const DO_TUOI = [
    ['P', 'Phim dành cho mọi lứa tuổi'],
    ['C13', 'Khán giả từ đủ 13 tuổi trở lên'],
    ['C16', 'Khán giả từ đủ 16 tuổi trở lên'],
    ['C18', 'Khán giả từ đủ 18 tuổi trở lên']
];

async function tai() {
    try {
        const [rap, phong] = await Promise.all([Api.rap(), Api.phong()]);
        noiDung.innerHTML = `
            <div class="hero">
                <h1 style="margin:0">${esc(rap.tenRap)}</h1>
                <p>Rạp chiếu phim với ${phong.length} phòng chiếu, đặt vé trực tuyến, chọn ghế theo sơ đồ và thanh toán qua ví điện tử hoặc thẻ.</p>
            </div>
            <div class="info-grid" style="margin-top:16px">
                <section class="card pad grid" style="gap:8px">
                    <h2 style="margin:0">Liên hệ</h2>
                    <span><span class="muted">Địa chỉ:</span> ${esc(rap.diaChiRap)}</span>
                    <span><span class="muted">Hotline:</span> <a href="tel:${esc(rap.hotline)}">${esc(rap.hotline)}</a></span>
                    <span><span class="muted">Email:</span> <a href="mailto:${esc(rap.emailRap)}">${esc(rap.emailRap)}</a></span>
                </section>
                <section class="card pad grid" style="gap:8px">
                    <h2 style="margin:0">Quy định đặt vé</h2>
                    <ul class="rules">
                        <li>Xem phim và lịch chiếu không cần đăng nhập. Chọn ghế thì cần đăng nhập.</li>
                        <li>Mỗi đơn tối đa ${rap.soVeToiDaMoiDon} vé.</li>
                        <li>Ghế được giữ ${rap.thoiGianGiuGhe} phút. Quá thời gian chưa thanh toán thì đơn hết hạn và ghế được trả lại.</li>
                        <li>Vé đã thanh toán không hủy hoặc hoàn tiền trực tuyến.</li>
                        <li>Xuất trình mã vé tại quầy để nhận vé và bắp nước.</li>
                    </ul>
                </section>
            </div>
            <section class="card pad" style="margin-top:16px">
                <h2>Phòng chiếu</h2>
                <div class="table-wrap"><table>
                    <thead><tr><th>Phòng</th><th class="r">Sức chứa</th><th class="r">Ghế đôi</th><th>Ghi chú</th><th>Tình trạng</th></tr></thead>
                    <tbody>${phong.map(p => {
                        const tt = TRANG_THAI_PHONG[p.trangThai] || [p.trangThai, ''];
                        return `<tr><td><b>${esc(p.tenPhong)}</b></td><td class="r">${p.sucChua} chỗ</td><td class="r">${p.soGheDoi}</td>
                            <td class="muted">${esc(p.ghiChu || '')}</td><td><span class="pill ${tt[1]}">${tt[0]}</span></td></tr>`;
                    }).join('')}</tbody>
                </table></div>
            </section>
            <section class="card pad" style="margin-top:16px">
                <h2>Phân loại độ tuổi</h2>
                <div class="grid" style="gap:8px">${DO_TUOI.map(([k, t]) => `<span><span class="age ${k}">${k}</span> ${t}</span>`).join('')}</div>
                <p class="muted small" style="margin-bottom:0">Rạp có thể yêu cầu xuất trình giấy tờ tùy thân khi vào phòng chiếu.</p>
            </section>`;
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
    }
}

tai();
