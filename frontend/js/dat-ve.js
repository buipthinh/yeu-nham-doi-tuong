//UC7.1.3 chon ghe, UC7.1.4 chon bap nuoc, UC7.1.5 thanh toan
veHeader('phim');
const noiDung = document.getElementById('noiDung');
const BUOC = ['Chọn phim', 'Chọn suất', 'Chọn ghế', 'Bắp nước', 'Thanh toán'];
const buocHtml = n => `<div class="steps">${BUOC.map((b, i) => `<span class="${i === n ? 'on' : i < n ? 'done' : ''}">${i + 1}. ${b}</span>`).join('')}</div>`;
let demNguoc = null;

//==== Buoc chon ghe ====
let suat = null, chon = new Map();

async function taiSuat(id) {
    try {
        suat = await Api.suat(id);
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
        return;
    }
    if (!suat.moBan) {
        noiDung.innerHTML = buocHtml(2) + `<div class="note warn">Suất chiếu này không còn mở bán (đã bắt đầu hoặc tạm ngưng).</div>
            <p><a class="btn" href="phim.html?id=${suat.phimId}">Chọn suất khác</a></p>`;
        return;
    }
    //Bo ghe da chon nhung khong con trong
    for (const g of suat.ghe) if (chon.has(g.id) && (g.trangThai !== 'trong' || g.trangThaiGhe !== 'hoatDong')) chon.delete(g.id);
    veChonGhe();
}

const loaiVe = id => suat.loaiVe.find(l => l.id === id);

function veChonGhe() {
    const hang = {};
    suat.ghe.forEach(g => (hang[g.hang] = hang[g.hang] || []).push(g));
    const giaCua = g => { const lv = loaiVe(g.loaiVeId); const dt = chon.get(g.id); return (lv.bangGia.find(b => b.id === dt) || {}).gia || 0; };
    const tong = [...chon.keys()].reduce((t, id) => t + giaCua(suat.ghe.find(g => g.id === id)), 0);
    noiDung.innerHTML = buocHtml(2) + `
        <div class="booking">
            <div class="card pad">
                <div class="screen"></div><div class="screen-label">Màn hình</div>
                <div class="seatmap">${Object.keys(hang).sort().map(h => `<div class="seat-row"><span class="row-label">${h}</span>
                    ${hang[h].sort((a, b) => a.cot - b.cot).map(g => {
                        const lv = loaiVe(g.loaiVeId);
                        const cls = g.trangThaiGhe !== 'hoatDong' ? 'baoTri' : g.trangThai === 'daBan' ? 'ban' : g.trangThai === 'dangGiu' ? 'giu' : chon.has(g.id) ? 'chon' : '';
                        const khoa = cls === 'baoTri' || cls === 'ban' || cls === 'giu' || !lv.bangGia.length;
                        return `<button class="seat ${lv.soChoMoiVe === 2 ? 'doi' : ''} ${cls}" data-ghe="${g.id}" ${khoa ? 'disabled' : ''}
                            title="${esc(g.maGhe + ' · ' + lv.tenLoaiVe)}">${esc(g.maGhe)}</button>`;
                    }).join('')}<span class="row-label">${h}</span></div>`).join('')}</div>
                <div class="legend"><span><i></i>Ghế thường</span><span><i style="background:#f1e4f5;border-color:#c9a6d6"></i>Ghế đôi (2 người)</span>
                    <span><i style="background:var(--accent);border-color:var(--accent)"></i>Bạn chọn</span><span><i style="background:var(--warn-soft)"></i>Đang giữ</span>
                    <span><i style="background:#e3dfd8"></i>Đã bán</span><span><i style="background:#ddd"></i>Bảo trì</span></div>
            </div>
            <aside class="card pad summary">
                <div><h2 style="margin:0">${esc(suat.tenPhim)}</h2>
                    <div class="muted small">${esc(suat.tenPhong)} · ${suat.gioBatDau}–${suat.gioKetThuc} · ${thu(suat.ngayChieu)} ${ngayVN(suat.ngayChieu)}</div>
                    <div class="muted small">Giá ${TEN.loaiNgay[suat.loaiNgay]}, khung ${TEN.khungGio[suat.khungGio]}</div></div>
                <div>${chon.size ? [...chon.keys()].map(id => {
                    const g = suat.ghe.find(x => x.id === id), lv = loaiVe(g.loaiVeId);
                    return `<div class="line"><span><b class="mono">${esc(g.maGhe)}</b> <span class="muted small">${esc(lv.tenLoaiVe)}</span></span>
                        <select class="input" style="width:auto;padding:4px 6px" data-dt="${id}" aria-label="Đối tượng ghế ${esc(g.maGhe)}">
                            ${lv.bangGia.map(b => `<option value="${b.id}" ${b.id === chon.get(id) ? 'selected' : ''}>${esc(b.tenDoiTuong)}</option>`).join('')}</select>
                        <span>${tien(giaCua(g))}</span></div>`;
                }).join('') : `<p class="muted small" style="margin:0">Chọn tối đa ${suat.soVeToiDa} vé trên sơ đồ. Ghế đôi tính là 1 vé cho 2 người.</p>`}</div>
                <div class="total"><span>Tạm tính</span><span>${tien(tong)}</span></div>
                <button class="btn primary block" id="tiepTuc" ${chon.size ? '' : 'disabled'}>Tiếp tục · giữ ghế ${suat.thoiGianGiuGhe} phút</button>
                <a class="btn ghost block" href="phim.html?id=${suat.phimId}">← Chọn suất khác</a>
            </aside>
        </div>`;
    noiDung.querySelectorAll('[data-ghe]').forEach(b => b.onclick = () => {
        const id = +b.dataset.ghe;
        if (chon.has(id)) chon.delete(id);
        else {
            if (chon.size >= suat.soVeToiDa) return toast('Mỗi đơn tối đa ' + suat.soVeToiDa + ' vé', true);
            chon.set(id, loaiVe(suat.ghe.find(g => g.id === id).loaiVeId).bangGia[0].id);
        }
        veChonGhe();
    });
    noiDung.querySelectorAll('[data-dt]').forEach(s => s.onchange = () => { chon.set(+s.dataset.dt, +s.value); veChonGhe(); });
    document.getElementById('tiepTuc').onclick = giuGhe;
}

async function giuGhe() {
    if (!canDangNhap()) return;
    const nut = document.getElementById('tiepTuc');
    nut.disabled = true;
    try {
        const d = await Api.taoDon(suat.id, [...chon.entries()].map(([gheId, doiTuongId]) => ({ gheId, doiTuongId })));
        location.href = 'dat-ve.html?don=' + d.id + '&buoc=thucPham';
    } catch (e) {
        toast(e.message, true);
        if (e.status === 401) return canDangNhap();
        taiSuat(suat.id);
    }
}

//==== Buoc bap nuoc va thanh toan ====
let don = null, gio = new Map(), dsMon = [], phuongThuc = '';

async function taiDon(id) {
    try {
        don = await Api.don(id);
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
        return;
    }
    if (don.trangThai === 'daThanhToan') { location.href = 've.html?id=' + don.id; return; }
    if (don.trangThai !== 'choThanhToan') {
        noiDung.innerHTML = buocHtml(4) + `<div class="note bad">${don.trangThai === 'hetHan' ? 'Đã hết thời gian giữ ghế, ghế đã được trả lại.' : 'Đơn hàng đã bị hủy.'}</div>
            <p><a class="btn primary" href="phim.html?id=${don.phimId}">Đặt lại</a></p>`;
        return;
    }
    gio = new Map(don.thucPham.map(t => [t.thucPhamId, t.soLuong]));
    if (thamSo('buoc') === 'thanhToan') veThanhToan(); else await veThucPham();
    batDauDemNguoc(don.conLaiGiay);
}

function batDauDemNguoc(giay) {
    clearInterval(demNguoc);
    const het = Date.now() + giay * 1000;
    const capNhat = () => {
        const con = Math.max(0, Math.round((het - Date.now()) / 1000));
        document.querySelectorAll('.timer').forEach(t => {
            t.textContent = String(Math.floor(con / 60)).padStart(2, '0') + ':' + String(con % 60).padStart(2, '0');
            t.classList.toggle('low', con < 60);
        });
        if (con === 0) { clearInterval(demNguoc); toast('Đã hết thời gian giữ ghế', true); setTimeout(() => taiDon(don.id), 1500); }
    };
    capNhat();
    demNguoc = setInterval(capNhat, 1000);
}

function tomTat() {
    const tienTP = dsMon.length ? [...gio.entries()].reduce((t, [id, sl]) => t + (dsMon.find(m => m.id === id)?.giaBan || 0) * sl, 0) : don.tongTienThucPham;
    const dongTP = dsMon.length
        ? [...gio.entries()].filter(([, sl]) => sl > 0).map(([id, sl]) => { const m = dsMon.find(x => x.id === id); return m ? [m.tenMon, sl, m.giaBan * sl] : null; }).filter(Boolean)
        : don.thucPham.map(t => [t.tenMon, t.soLuong, t.thanhTien]);
    return `<aside class="card pad summary">
        <div><span class="muted small">Thời gian giữ ghế còn lại</span><div class="timer">--:--</div></div>
        <div><h2 style="margin:0">${esc(don.tenPhim)}</h2>
            <div class="muted small">${esc(don.tenPhong)} · ${don.gioBatDau}–${don.gioKetThuc} · ${thu(don.ngayChieu)} ${ngayVN(don.ngayChieu)}</div></div>
        <div>${don.ve.map(v => `<div class="line"><span><b class="mono">${esc(v.maGhe)}</b> <span class="muted small">${esc(v.tenLoaiVe)} · ${esc(v.tenDoiTuong)}</span></span><span>${tien(v.giaVe)}</span></div>`).join('')}
            ${dongTP.map(([t, sl, tt]) => `<div class="line"><span>${esc(t)} × ${sl}</span><span>${tien(tt)}</span></div>`).join('')}</div>
        <div class="total"><span>Tổng cộng</span><span>${tien(don.tongTienVe + tienTP)}</span></div>
    </aside>`;
}

async function veThucPham() {
    if (!dsMon.length) {
        try { dsMon = await Api.thucPham(); } catch (e) { dsMon = []; }
    }
    noiDung.innerHTML = buocHtml(3) + `<div class="booking"><div class="grid" style="align-content:start">
        <div><h2 style="margin:0">Bắp nước &amp; combo</h2><p class="muted" style="margin:4px 0 0">Bước này không bắt buộc, bạn có thể bỏ qua.</p></div>
        ${dsMon.length ? `<div class="foods">${dsMon.map(m => {
            const sl = gio.get(m.id) || 0, het = m.tonKho <= 0;
            return `<div class="card food"><div class="food-img">${m.hinhAnh ? `<img src="${esc(m.hinhAnh)}" alt="" onerror="this.remove()">` : ''}${esc(m.tenMon.slice(0, 1))}</div>
                <div class="grid" style="gap:4px;flex:1"><b>${esc(m.tenMon)}</b><span class="muted small">${esc(m.thanhPhan || '')}</span>
                <div class="row between"><span>${tien(m.giaBan)}</span>${het ? '<span class="pill bad">Hết hàng</span>'
                    : `<span class="qty"><button data-mon="${m.id}" data-d="-1" aria-label="Bớt">−</button><span>${sl}</span><button data-mon="${m.id}" data-d="1" aria-label="Thêm">+</button></span>`}</div>
                ${!het && m.tonKho < 20 ? `<span class="small" style="color:var(--warn)">Còn ${m.tonKho} phần</span>` : ''}</div></div>`;
        }).join('')}</div>` : '<div class="note">Hiện chưa có món nào đang bán.</div>'}
        <div class="row"><button class="btn primary" id="tiepTuc">Tiếp tục thanh toán</button><button class="btn" id="boQua">Bỏ qua</button>
            <button class="btn ghost danger" id="huy">Hủy đơn</button></div>
    </div>${tomTat()}</div>`;
    noiDung.querySelectorAll('[data-mon]').forEach(b => b.onclick = () => {
        const id = +b.dataset.mon, m = dsMon.find(x => x.id === id);
        const sl = Math.max(0, (gio.get(id) || 0) + +b.dataset.d);
        if (sl > m.tonKho) return toast('Chỉ còn ' + m.tonKho + ' phần ' + m.tenMon, true);
        gio.set(id, sl);
        veThucPham();
        batDauDemNguoc(Math.max(0, don.conLaiGiay - (Date.now() - batDau) / 1000));
    });
    document.getElementById('tiepTuc').onclick = () => luuThucPham(false);
    document.getElementById('boQua').onclick = () => luuThucPham(true);
    document.getElementById('huy').onclick = huyDon;
}

async function luuThucPham(boQua) {
    try {
        const mon = boQua ? [] : [...gio.entries()].filter(([, sl]) => sl > 0).map(([thucPhamId, soLuong]) => ({ thucPhamId, soLuong }));
        await Api.chonThucPham(don.id, mon);
        location.href = 'dat-ve.html?don=' + don.id + '&buoc=thanhToan';
    } catch (e) {
        toast(e.message, true);
        if (e.status === 410) taiDon(don.id);
    }
}

function veThanhToan() {
    const PT = [['momo', 'MoMo', 'Ví điện tử MoMo'], ['vnpay', 'VNPay', 'Ứng dụng ngân hàng qua VNPay'], ['theTinDung', 'Thẻ tín dụng', 'Visa, Mastercard, JCB']];
    noiDung.innerHTML = buocHtml(4) + `<div class="booking"><div class="grid" style="align-content:start">
        <div><h2 style="margin:0">Chọn phương thức thanh toán</h2><p class="muted" style="margin:4px 0 0">Đơn trực tuyến không thanh toán tiền mặt.</p></div>
        <div class="pms">${PT.map(([k, t, m]) => `<button class="pm ${phuongThuc === k ? 'on' : ''}" data-pt="${k}"><b>${t}</b><span class="muted small">${m}</span></button>`).join('')}</div>
        <div class="row"><button class="btn primary" id="thanhToan" ${phuongThuc ? '' : 'disabled'}>Thanh toán ${tien(don.tongTien)}</button>
            <a class="btn" href="dat-ve.html?don=${don.id}&buoc=thucPham">← Sửa bắp nước</a><button class="btn ghost danger" id="huy">Hủy đơn</button></div>
        <div class="note small">Cổng thanh toán trong đồ án là bản mô phỏng: bạn tự chọn kết quả giao dịch để xem hệ thống xử lý từng trường hợp.</div>
    </div>${tomTat()}</div>`;
    noiDung.querySelectorAll('[data-pt]').forEach(b => b.onclick = () => {
        phuongThuc = b.dataset.pt;
        veThanhToan();
        batDauDemNguoc(Math.max(0, don.conLaiGiay - (Date.now() - batDau) / 1000));
    });
    document.getElementById('thanhToan').onclick = moCongThanhToan;
    document.getElementById('huy').onclick = huyDon;
}

function moCongThanhToan() {
    hopThoai('Cổng thanh toán ' + TEN.pt[phuongThuc] + ' (mô phỏng)',
        `<p style="margin:0">Số tiền: <b>${tien(don.tongTien)}</b><br><span class="muted small">Đơn #${don.id} · Rạp Phương Nam</span></p>
         <p class="muted small">Chọn kết quả mà cổng thanh toán trả về cho hệ thống.</p>`,
        [{ nhan: 'Hủy giao dịch', khiBam: () => guiThanhToan('huy') },
         { nhan: 'Thất bại', lop: 'danger', khiBam: () => guiThanhToan('thatBai') },
         { nhan: 'Thành công', lop: 'primary', khiBam: () => guiThanhToan('thanhCong') }]);
}

async function guiThanhToan(ketQua) {
    try {
        const d = await Api.thanhToan(don.id, phuongThuc, ketQua);
        clearInterval(demNguoc);
        location.href = 've.html?id=' + d.id + '&moi=1';
    } catch (e) {
        toast(e.message, true);
        if (e.status === 410) taiDon(don.id);
    }
}

function huyDon() {
    hopThoai('Hủy đơn', '<p style="margin:0">Ghế đang giữ sẽ được trả lại ngay. Bạn chắc chắn muốn hủy?</p>', [
        { nhan: 'Không' },
        { nhan: 'Hủy đơn', lop: 'primary', khiBam: async () => {
            try { await Api.huyDon(don.id); toast('Đã hủy đơn'); location.href = 'phim.html?id=' + don.phimId; } catch (e) { toast(e.message, true); }
        } }]);
}

const batDau = Date.now();
if (thamSo('don')) { if (canDangNhap()) taiDon(thamSo('don')); }
else if (thamSo('suat')) { if (canDangNhap()) taiSuat(thamSo('suat')); }
else location.href = 'index.html';
