//UC1.1 dang ky, UC1.2 dang nhap
veHeader('dn');
const noiDung = document.getElementById('noiDung');
const tiep = thamSo('tiep') || 'index.html';
const diTiep = () => { location.href = /^[\w\-.]+\.html(\?.*)?$/.test(tiep) ? tiep : 'index.html'; };
if (Phien.token()) diTiep();

let che = thamSo('che') === 'dangKy' ? 'dangKy' : 'dangNhap';
function ve() {
    noiDung.innerHTML = `
        <div class="auth grid">
            <div class="tabs"><button class="tab ${che === 'dangNhap' ? 'on' : ''}" data-che="dangNhap">Đăng nhập</button>
                <button class="tab ${che === 'dangKy' ? 'on' : ''}" data-che="dangKy">Đăng ký</button></div>
            ${che === 'dangNhap' ? `
            <form class="card pad grid" id="form" novalidate>
                <h2 style="margin:0">Đăng nhập</h2>
                <div class="note bad hidden" id="loi"></div>
                <div class="field"><label for="tenDangNhap">Email hoặc số điện thoại</label><input class="input" id="tenDangNhap" autocomplete="username"></div>
                <div class="field"><label for="matKhau">Mật khẩu</label><input class="input" id="matKhau" type="password" autocomplete="current-password"></div>
                <button class="btn primary block" type="submit">Đăng nhập</button>
                <p class="muted small" style="margin:0">Tài khoản thử: khach@demo.vn / khach123. Nhập sai 5 lần liên tiếp tài khoản bị tạm khóa 5 phút.</p>
            </form>` : `
            <form class="card pad grid" id="form" novalidate>
                <h2 style="margin:0">Tạo tài khoản</h2>
                <div class="note bad hidden" id="loi"></div>
                ${o('hoTen', 'Họ và tên')}${o('email', 'Email', 'email')}${o('soDienThoai', 'Số điện thoại')}
                ${o('matKhau', 'Mật khẩu (tối thiểu 8 ký tự)', 'password')}${o('nhapLaiMatKhau', 'Nhập lại mật khẩu', 'password')}
                <button class="btn primary block" type="submit">Đăng ký</button>
            </form>`}
        </div>`;
    noiDung.querySelectorAll('[data-che]').forEach(b => b.onclick = () => { che = b.dataset.che; ve(); });
    document.getElementById('form').onsubmit = guiForm;
}
const o = (id, nhan, kieu = 'text') => `<div class="field"><label for="${id}">${nhan}</label><input class="input" id="${id}" type="${kieu}"><span class="err" data-loi="${id}"></span></div>`;
const gt = id => document.getElementById(id).value;

async function guiForm(e) {
    e.preventDefault();
    const loi = document.getElementById('loi');
    loi.classList.add('hidden');
    noiDung.querySelectorAll('[data-loi]').forEach(x => x.textContent = '');
    const nut = e.target.querySelector('button[type=submit]');
    nut.disabled = true;
    try {
        const kq = che === 'dangNhap'
            ? await Api.dangNhap(gt('tenDangNhap').trim(), gt('matKhau'))
            : await Api.dangKy({ hoTen: gt('hoTen'), email: gt('email'), soDienThoai: gt('soDienThoai'), matKhau: gt('matKhau'), nhapLaiMatKhau: gt('nhapLaiMatKhau') });
        Phien.luu(kq.token, kq.taiKhoan);
        diTiep();
    } catch (err) {
        if (err.duLieu) Object.entries(err.duLieu).forEach(([k, v]) => { const x = noiDung.querySelector(`[data-loi="${k}"]`); if (x) x.textContent = v; });
        loi.textContent = err.message;
        loi.classList.remove('hidden');
        nut.disabled = false;
    }
}
ve();
