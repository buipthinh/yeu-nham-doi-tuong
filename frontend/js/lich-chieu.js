//Lich chieu theo ngay cua tat ca phim (UC7.1.2 xem tu goc ngay chieu)
veHeader('lich');
const noiDung = document.getElementById('noiDung');
let duLieu = null, khung = '';

async function tai(ngay) {
    try {
        duLieu = await Api.lichChieu(ngay);
        if (duLieu.ngay) history.replaceState(null, '', '?ngay=' + duLieu.ngay);
        ve();
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
    }
}

function ve() {
    const d = duLieu;
    if (!d.cacNgay.length) {
        noiDung.innerHTML = `<h1>Lịch chiếu</h1><div class="empty">Hiện chưa có suất chiếu nào đang mở bán. Mời bạn quay lại sau.</div>`;
        return;
    }
    const ds = d.phim.map(p => ({ ...p, suat: p.suat.filter(s => !khung || s.khungGio === khung) })).filter(p => p.suat.length);
    noiDung.innerHTML = `
        <div class="toolbar">
            <div><h1>Lịch chiếu</h1><p class="muted" style="margin:0">${thu(d.ngay)}, ${ngayVN(d.ngay)} · ${TEN.loaiNgay[d.loaiNgay] || ''}</p></div>
            <select class="input" id="khung" aria-label="Khung giờ"><option value="">Mọi khung giờ</option>
                ${Object.entries(TEN.khungGio).map(([v, t]) => `<option value="${v}" ${v === khung ? 'selected' : ''}>${t}</option>`).join('')}</select>
        </div>
        <div class="dates">${d.cacNgay.map(n => `<button class="date ${n === d.ngay ? 'on' : ''}" data-ngay="${n}">
            <span class="small">${n === homNay() ? 'Hôm nay' : thu(n)}</span><b>${n.slice(8)}</b><span class="small">Th${+n.slice(5, 7)}</span></button>`).join('')}</div>
        <div class="lich">
            ${ds.length ? ds.map(p => `
            <article class="card lich-phim">
                <a href="phim.html?id=${p.phimId}">${poster(p)}</a>
                <div>
                    <h3><a href="phim.html?id=${p.phimId}">${esc(p.tenPhim)}</a> <span class="age ${esc(p.doTuoi)}">${esc(p.doTuoi)}</span></h3>
                    <span class="muted small">${esc(p.theLoai || '')} · ${p.thoiLuong} phút${p.ngonNgu ? ' · ' + esc(p.ngonNgu) : ''}</span>
                    <div class="gio-ds">${p.suat.map(s => `
                        <a class="gio ${s.soGheTrong > 0 ? '' : 'het'}" href="dat-ve.html?suat=${s.id}">
                            <b>${s.gioBatDau}</b><span class="small muted">${esc(s.tenPhong)} · ${s.soGheTrong > 0 ? s.soGheTrong + ' ghế' : 'Hết vé'}</span>
                        </a>`).join('')}</div>
                </div>
            </article>`).join('') : '<div class="empty">Không có suất nào trong khung giờ này, bạn chọn khung giờ hoặc ngày khác.</div>'}
        </div>`;
    noiDung.querySelectorAll('.date').forEach(b => b.onclick = () => tai(b.dataset.ngay));
    document.getElementById('khung').onchange = e => { khung = e.target.value; ve(); };
}

tai(thamSo('ngay'));
