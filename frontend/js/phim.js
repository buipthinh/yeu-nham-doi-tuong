//Chi tiet phim + UC7.1.2 tim kiem suat chieu
veHeader('phim');
const noiDung = document.getElementById('noiDung');
const phimId = thamSo('id');
let dsSuat = [], ngayChon = null, khung = '';

async function tai() {
    try {
        const [p, suat] = await Promise.all([Api.chiTietPhim(phimId), Api.suatCuaPhim(phimId)]);
        dsSuat = suat;
        document.title = p.tenPhim + ' · Rạp Phương Nam';
        const ngay = [...new Set(suat.map(s => s.ngayChieu))];
        ngayChon = ngay.includes(thamSo('ngay')) ? thamSo('ngay') : ngay[0];
        noiDung.innerHTML = `
            <a class="muted small" href="index.html">← Danh sách phim</a>
            <div class="detail" style="margin-top:12px">
                <div>${poster(p, true)}</div>
                <div class="grid" style="align-content:start">
                    <div><span class="age ${esc(p.doTuoi)}">${esc(p.doTuoi)}</span> <span class="pill">${p.trangThai === 'sapChieu' ? 'Sắp chiếu' : 'Đang chiếu'}</span>
                        <h1 style="margin-top:8px">${esc(p.tenPhim)}</h1></div>
                    <p style="margin:0;max-width:70ch">${esc(p.moTa || '')}</p>
                    <dl class="kv">
                        <dt>Đạo diễn</dt><dd>${esc(p.daoDien || '—')}</dd>
                        <dt>Diễn viên</dt><dd>${esc(p.dienVien || '—')}</dd>
                        <dt>Thể loại</dt><dd>${esc(p.theLoai.join(', '))}</dd>
                        <dt>Thời lượng</dt><dd>${p.thoiLuong} phút</dd>
                        <dt>Ngôn ngữ</dt><dd>${esc(p.ngonNgu || '—')} · Phụ đề: ${esc(p.phuDe || 'Không')}</dd>
                        <dt>Khởi chiếu</dt><dd>${ngayVN(p.ngayKhoiChieu)}</dd>
                        ${p.trailer ? `<dt>Trailer</dt><dd><a href="${esc(p.trailer)}" target="_blank" rel="noopener">Xem trailer</a></dd>` : ''}
                    </dl>
                    <div class="card pad" id="khungSuat"></div>
                </div>
            </div>`;
        veSuat(p);
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.status === 404 ? 'Phim không tồn tại hoặc đã ngừng chiếu.' : e.message)}</div>`;
    }
}

function veSuat(p) {
    const k = document.getElementById('khungSuat');
    const ngay = [...new Set(dsSuat.map(s => s.ngayChieu))];
    if (!ngay.length) {
        k.innerHTML = `<h2>Lịch chiếu</h2><div class="note warn">${p.trangThai === 'sapChieu'
            ? 'Phim khởi chiếu ngày ' + ngayVN(p.ngayKhoiChieu) + '. Rạp chưa mở bán suất chiếu, mời bạn quay lại sau.'
            : 'Phim hiện chưa có suất chiếu đang mở bán.'}</div>`;
        return;
    }
    const ds = dsSuat.filter(s => s.ngayChieu === ngayChon && (!khung || s.khungGio === khung));
    k.innerHTML = `
        <div class="row between"><h2 style="margin:0">Chọn suất chiếu</h2>
            <select class="input" style="width:auto" id="khung" aria-label="Khung giờ"><option value="">Mọi khung giờ</option>
                ${Object.entries(TEN.khungGio).map(([v, t]) => `<option value="${v}" ${v === khung ? 'selected' : ''}>${t}</option>`).join('')}</select></div>
        <div class="dates" style="margin-top:12px">${ngay.map(d => `<button class="date ${d === ngayChon ? 'on' : ''}" data-ngay="${d}">
            <span class="small">${d === homNay() ? 'Hôm nay' : thu(d)}</span><b>${d.slice(8)}</b><span class="small">Th${+d.slice(5, 7)}</span></button>`).join('')}</div>
        ${ds.length ? `<div class="times">${ds.map(s => `<a class="time" href="dat-ve.html?suat=${s.id}">
            <b>${s.gioBatDau}</b> <span class="muted">– ${s.gioKetThuc}</span><br>
            <span class="small">${esc(s.tenPhong)} · ${s.soGheTrong > 0 ? s.soGheTrong + ' ghế trống' : 'Hết vé'}</span><br>
            <span class="small muted">${s.giaTu ? 'Từ ' + tien(s.giaTu) : ''} · ${TEN.loaiNgay[s.loaiNgay]}</span></a>`).join('')}</div>`
            : '<p class="note" style="margin-top:12px">Không có suất phù hợp, bạn chọn ngày hoặc khung giờ khác.</p>'}`;
    k.querySelectorAll('.date').forEach(b => b.onclick = () => { ngayChon = b.dataset.ngay; veSuat(p); });
    document.getElementById('khung').onchange = e => { khung = e.target.value; veSuat(p); };
}

if (!phimId) location.href = 'index.html'; else tai();
