//Bang gia ve dang ap dung: theo loai ve, doi tuong, loai ngay va khung gio
veHeader('gia');
const noiDung = document.getElementById('noiDung');
const LOAI_NGAY = ['ngayThuong', 'cuoiTuan', 'ngayLe'];
let bg = null, doiTuongId = null;

async function tai() {
    try {
        const [g, rap] = await Promise.all([Api.bangGia(), Api.rap().catch(() => ({}))]);
        bg = g;
        bg.rap = rap;
        doiTuongId = bg.doiTuong.length ? bg.doiTuong[0].id : null;
        ve();
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
    }
}

const giaCua = (lv, ln, kg) => {
    const x = bg.gia.find(g => g.loaiVeId === lv && g.doiTuongId === doiTuongId && g.loaiNgay === ln && g.khungGio === kg);
    return x ? `<b>${tien(x.gia)}</b>` : '<span class="muted">—</span>';
};

function ve() {
    noiDung.innerHTML = `
        <h1>Giá vé</h1>
        <p class="muted" style="margin-top:0">Giá tính theo loại ghế, đối tượng, loại ngày và khung giờ của giờ bắt đầu suất chiếu. Bảng dưới là giá đang áp dụng hôm nay.</p>
        <div class="tabs" style="margin:16px 0">${bg.doiTuong.map(d => `<button class="tab ${d.id === doiTuongId ? 'on' : ''}" data-dt="${d.id}">${esc(d.tenDoiTuong)}</button>`).join('')}</div>
        <div class="grid">
            ${bg.loaiVe.map(lv => `
            <section class="card pad">
                <div class="row between"><h2 style="margin:0">Ghế ${esc(lv.tenLoaiVe.toLowerCase())}</h2>
                    <span class="muted small">${esc(lv.moTa || '')}${lv.soChoMoiVe > 1 ? ' · giá cho cả ghế ' + lv.soChoMoiVe + ' chỗ' : ''}</span></div>
                <div class="table-wrap" style="margin-top:10px"><table class="price">
                    <thead><tr><th>Khung giờ</th>${LOAI_NGAY.map(ln => `<th class="r">${TEN.loaiNgay[ln]}</th>`).join('')}</tr></thead>
                    <tbody>${bg.khungGio.map(k => `<tr><td>${esc(k.nhan)} <span class="muted small">(${esc(k.moTa)})</span></td>
                        ${LOAI_NGAY.map(ln => `<td class="r">${giaCua(lv.id, ln, k.ma)}</td>`).join('')}</tr>`).join('')}</tbody>
                </table></div>
            </section>`).join('')}
            <section class="card pad grid" style="gap:10px">
                <h2 style="margin:0">Lưu ý</h2>
                <ul class="rules">
                    <li>Cuối tuần là thứ 7 và chủ nhật. Ngày lễ theo lịch của rạp.</li>
                    <li>Đối tượng Học sinh – Sinh viên cần xuất trình thẻ khi vào phòng chiếu.</li>
                    ${bg.rap.soVeToiDaMoiDon ? `<li>Mỗi đơn đặt tối đa ${bg.rap.soVeToiDaMoiDon} vé. Ghế được giữ ${bg.rap.thoiGianGiuGhe} phút để bạn thanh toán.</li>` : ''}
                    <li>Thanh toán trực tuyến qua MoMo, VNPay hoặc thẻ tín dụng.</li>
                </ul>
                ${bg.ngayLe.length ? `<div><h3>Ngày lễ sắp tới</h3><div class="chips">${bg.ngayLe.map(n =>
                    `<span class="pill">${ngayVN(n.ngay)} · ${esc(n.tenNgayLe)}</span>`).join('')}</div></div>` : ''}
            </section>
        </div>`;
    noiDung.querySelectorAll('[data-dt]').forEach(b => b.onclick = () => { doiTuongId = +b.dataset.dt; ve(); });
}

tai();
