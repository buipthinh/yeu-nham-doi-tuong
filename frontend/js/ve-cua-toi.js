//UC7.1.6 quan ly ve dien tu va lich su dat ve
veHeader('ve');
const noiDung = document.getElementById('noiDung');
let loc = '';

async function tai() {
    const tk = Phien.taiKhoan();
    noiDung.innerHTML = `
        <div class="toolbar"><div><h1>Vé của tôi</h1><p class="muted" style="margin:0">Lịch sử đặt vé của ${esc(tk?.hoTen || '')}</p></div>
            <div class="tabs">${[['', 'Tất cả'], ['choThanhToan', 'Chờ thanh toán'], ['daThanhToan', 'Đã thanh toán'], ['daHuy', 'Đã hủy'], ['hetHan', 'Hết hạn']]
                .map(([k, t]) => `<button class="tab ${k === loc ? 'on' : ''}" data-loc="${k}">${t}</button>`).join('')}</div></div>
        <div id="ds"><p class="muted">Đang tải…</p></div>`;
    noiDung.querySelectorAll('[data-loc]').forEach(b => b.onclick = () => { loc = b.dataset.loc; tai(); });
    try {
        const ds = await Api.donCuaToi(loc);
        document.getElementById('ds').innerHTML = !ds.length
            ? `<div class="empty">${loc ? 'Không có đơn ở trạng thái này.' : 'Bạn chưa đặt vé nào.'} <a class="btn sm" href="index.html">Xem phim đang chiếu</a></div>`
            : `<div class="card table-wrap"><table><thead><tr><th>Mã đơn</th><th>Phim</th><th>Suất chiếu</th><th>Ghế</th><th class="r">Tổng tiền</th><th>Trạng thái</th></tr></thead><tbody>
                ${ds.map(d => `<tr class="click" data-id="${d.id}"><td class="mono">#${d.id}</td><td>${esc(d.tenPhim)}</td>
                    <td>${d.gioBatDau} ${ngayVN(d.ngayChieu)}<div class="muted small">${esc(d.tenPhong)}</div></td>
                    <td class="mono small">${esc(d.dsGhe || '')}</td><td class="r">${tien(d.tongTien)}</td>
                    <td>${pill('don', d.trangThai)} ${d.trangThai === 'daThanhToan' ? pill('ve', d.trangThaiVe) : ''}</td></tr>`).join('')}
               </tbody></table></div>`;
        noiDung.querySelectorAll('tr[data-id]').forEach(r => r.onclick = () => location.href = 've.html?id=' + r.dataset.id);
    } catch (e) {
        document.getElementById('ds').innerHTML = `<div class="empty">${esc(e.message)}</div>`;
    }
}
if (canDangNhap()) tai();
