//Phan dung chung cho moi trang khach: header, footer, ham tien ich

const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const tien = n => Number(n || 0).toLocaleString('vi-VN') + ' đ';
const ngayVN = s => s ? s.slice(0, 10).split('-').reverse().join('/') : '';
const THU = ['Chủ nhật', 'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7'];
const thu = s => THU[new Date(s + 'T00:00:00').getDay()];
const thamSo = k => new URLSearchParams(location.search).get(k);
const homNay = () => { const d = new Date(); return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0'); };
const TEN = {
    don: { choThanhToan: ['Chờ thanh toán', 'warn'], daThanhToan: ['Đã thanh toán', 'ok'], daHuy: ['Đã hủy', ''], hetHan: ['Hết hạn', 'bad'] },
    ve: { hopLe: ['Vé hợp lệ', 'ok'], daSuDung: ['Đã sử dụng', ''], chuaCo: ['Chưa có vé', ''] },
    pt: { momo: 'MoMo', vnpay: 'VNPay', theTinDung: 'Thẻ tín dụng' },
    loaiNgay: { ngayThuong: 'Ngày thường', cuoiTuan: 'Cuối tuần', ngayLe: 'Ngày lễ' },
    khungGio: { sang: 'Sáng', trua: 'Trưa', chieu: 'Chiều', toi: 'Tối' }
};
const pill = (nhom, k) => { const v = TEN[nhom][k] || [k, '']; return `<span class="pill ${v[1]}">${esc(v[0])}</span>`; };

function poster(p, lon) {
    return `<div class="poster" ${lon ? 'style="border-radius:12px"' : ''}>
        ${p.poster ? `<img src="${esc(p.poster)}" alt="" onerror="this.remove()">` : ''}
        <span class="age ${esc(p.doTuoi)}">${esc(p.doTuoi)}</span><span class="t">${esc(p.tenPhim)}</span></div>`;
}

function toast(msg, loi) {
    const t = document.createElement('div');
    t.className = 'toast' + (loi ? ' bad' : '');
    t.textContent = msg;
    document.body.appendChild(t);
    setTimeout(() => t.remove(), 3800);
}

//Hop thoai don gian; nut: [{ nhan, lop, khiBam }]
function hopThoai(tieuDe, noiDung, nut) {
    const bg = document.createElement('div');
    bg.className = 'modal-bg';
    bg.innerHTML = `<div class="modal" role="dialog" aria-modal="true"><h2 style="margin:0">${esc(tieuDe)}</h2><div>${noiDung}</div><div class="row" style="justify-content:flex-end"></div></div>`;
    const hang = bg.querySelector('.row');
    nut.forEach(n => {
        const b = document.createElement('button');
        b.className = 'btn ' + (n.lop || '');
        b.textContent = n.nhan;
        b.onclick = () => { bg.remove(); n.khiBam && n.khiBam(); };
        hang.appendChild(b);
    });
    bg.addEventListener('click', e => { if (e.target === bg) bg.remove(); });
    document.body.appendChild(bg);
}

//Chua dang nhap thi chuyen sang trang dang nhap, dang nhap xong quay lai
function canDangNhap() {
    if (Phien.token()) return true;
    location.href = 'dang-nhap.html?tiep=' + encodeURIComponent(location.pathname.split('/').pop() + location.search);
    return false;
}

function veHeader(trang) {
    const tk = Phien.taiKhoan();
    document.getElementById('header').innerHTML = `
        <div class="top"><div class="wrap">
            <a class="logo" href="index.html"><span>RP</span>Rạp Phương Nam</a>
            <nav class="nav">
                <a href="index.html" class="${trang === 'phim' ? 'on' : ''}">Phim</a>
                <a href="lich-chieu.html" class="${trang === 'lich' ? 'on' : ''}">Lịch chiếu</a>
                <a href="bang-gia.html" class="${trang === 'gia' ? 'on' : ''}">Giá vé</a>
                <a href="bap-nuoc.html" class="${trang === 'bap' ? 'on' : ''}">Bắp nước</a>
                <a href="gioi-thieu.html" class="${trang === 'gt' ? 'on' : ''}">Giới thiệu</a>
                ${tk ? `<a href="ve-cua-toi.html" class="${trang === 've' ? 'on' : ''}">Vé của tôi</a>
                        <a href="tai-khoan.html" class="${trang === 'tk' ? 'on' : ''}">${esc(tk.hoTen)}</a>
                        <button id="nutDangXuat">Đăng xuất</button>`
                     : `<a href="dang-nhap.html" class="${trang === 'dn' ? 'on' : ''}">Đăng nhập / Đăng ký</a>`}
            </nav>
        </div></div>`;
    const dx = document.getElementById('nutDangXuat');
    if (dx) dx.onclick = async () => {
        try { await Api.dangXuat(); } catch (e) {}
        Phien.xoa();
        location.href = 'index.html';
    };
    Api.rap().then(r => {
        document.getElementById('footer').innerHTML = `<div class="wrap row between">
            <span>${esc(r.tenRap)} · ${esc(r.diaChiRap)} · Hotline ${esc(r.hotline)} · ${esc(r.emailRap)}</span>
            <span class="row"><a href="lich-chieu.html">Lịch chiếu</a><a href="bang-gia.html">Giá vé</a><a href="gioi-thieu.html">Giới thiệu &amp; quy định</a></span></div>`;
    }).catch(() => {});
}
