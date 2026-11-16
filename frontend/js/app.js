//Trang khach: xem danh sach phim dang chieu / sap chieu (lay tu backend)
const dsPhim = document.getElementById('dsPhim');
const thongBao = document.getElementById('thongBao');

//Chong chen HTML tu du lieu
const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const ngay = s => s ? s.split('-').reverse().join('/') : '';

function theHtml(p) {
    return `
        <article class="movie">
            <div class="poster">
                ${p.poster ? `<img src="${esc(p.poster)}" alt="" onerror="this.remove()">` : ''}
                <span class="age ${esc(p.doTuoi)}">${esc(p.doTuoi)}</span>
                <span style="position:relative">${esc(p.tenPhim)}</span>
            </div>
            <div class="body">
                <h3>${esc(p.tenPhim)}</h3>
                <p class="muted">${esc(p.theLoai.join(', '))}</p>
                <p class="muted">${p.thoiLuong} phút · ${esc(p.ngonNgu || '')}</p>
                <p class="muted">Khởi chiếu: ${ngay(p.ngayKhoiChieu)}</p>
            </div>
        </article>`;
}

async function taiPhim(trangThai) {
    thongBao.textContent = 'Đang tải…';
    dsPhim.innerHTML = '';
    try {
        const phim = trangThai === 'sapChieu' ? await PhimApi.sapChieu() : await PhimApi.dangChieu();
        thongBao.textContent = phim.length ? phim.length + ' phim' : 'Hiện chưa có phim nào.';
        dsPhim.innerHTML = phim.map(theHtml).join('');
    } catch (e) {
        thongBao.textContent = 'Không gọi được backend (' + e.message + '). Hãy chạy backend ở cổng 8080 trước.';
    }
}

document.querySelectorAll('.tab').forEach(btn => btn.addEventListener('click', () => {
    document.querySelectorAll('.tab').forEach(b => b.classList.toggle('on', b === btn));
    taiPhim(btn.dataset.tt);
}));

taiPhim('dangChieu');
