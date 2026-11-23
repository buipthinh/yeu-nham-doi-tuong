//UC7.2.1 xem, UC7.2.2 cap nhat thong tin ca nhan + doi mat khau
veHeader('tk');
const noiDung = document.getElementById('noiDung');
const o = (id, nhan, gt, kieu = 'text', them = '') => `<div class="field"><label for="${id}">${nhan}</label><input class="input" id="${id}" type="${kieu}" value="${esc(gt ?? '')}" ${them}><span class="err" data-loi="${id}"></span></div>`;
const gt = id => document.getElementById(id).value;

function hienLoi(form, e) {
    form.querySelectorAll('[data-loi]').forEach(x => x.textContent = '');
    if (e.duLieu) Object.entries(e.duLieu).forEach(([k, v]) => { const x = form.querySelector(`[data-loi="${k}"]`); if (x) x.textContent = v; });
    toast(e.message, true);
}

async function tai() {
    let tk;
    try { tk = await Api.taiKhoan(); } catch (e) { noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`; return; }
    noiDung.innerHTML = `
        <div class="toolbar"><div><h1>Tài khoản của tôi</h1>
            <p class="muted" style="margin:0">Mã tài khoản <span class="mono">${esc(tk.maTaiKhoan)}</span> · đăng ký ngày ${ngayVN(tk.ngayTao)}</p></div>
            <a class="btn" href="ve-cua-toi.html">Vé của tôi</a></div>
        <div class="grid" style="grid-template-columns:repeat(auto-fit,minmax(min(100%,380px),1fr));align-items:start">
            <form class="card pad grid" id="formTk" novalidate>
                <h2 style="margin:0">Thông tin cá nhân</h2>
                ${o('hoTen', 'Họ và tên', tk.hoTen)}${o('email', 'Email (dùng để đăng nhập)', tk.email, 'email')}${o('soDienThoai', 'Số điện thoại', tk.soDienThoai)}
                ${o('ngaySinh', 'Ngày sinh', tk.ngaySinh, 'date')}
                <div class="field"><label for="gioiTinh">Giới tính</label><select class="input" id="gioiTinh">
                    ${[['nam', 'Nam'], ['nu', 'Nữ'], ['khac', 'Khác']].map(([k, t]) => `<option value="${k}" ${k === tk.gioiTinh ? 'selected' : ''}>${t}</option>`).join('')}</select></div>
                ${o('diaChi', 'Địa chỉ', tk.diaChi)}
                <div><button class="btn primary" type="submit">Lưu thay đổi</button></div>
            </form>
            <form class="card pad grid" id="formMk" novalidate>
                <h2 style="margin:0">Đổi mật khẩu</h2>
                ${o('matKhauCu', 'Mật khẩu hiện tại', '', 'password')}${o('matKhauMoi', 'Mật khẩu mới (tối thiểu 8 ký tự)', '', 'password')}${o('nhapLaiMatKhau', 'Nhập lại mật khẩu mới', '', 'password')}
                <div><button class="btn" type="submit">Đổi mật khẩu</button></div>
            </form>
        </div>`;
    document.getElementById('formTk').onsubmit = async e => {
        e.preventDefault();
        try {
            const kq = await Api.capNhatTaiKhoan({ hoTen: gt('hoTen'), email: gt('email'), soDienThoai: gt('soDienThoai'), ngaySinh: gt('ngaySinh'), gioiTinh: gt('gioiTinh'), diaChi: gt('diaChi') });
            Phien.luu(null, kq.taiKhoan);
            toast('Đã cập nhật thông tin cá nhân');
            veHeader('tk');
            e.target.querySelectorAll('[data-loi]').forEach(x => x.textContent = '');
        } catch (err) { hienLoi(e.target, err); }
    };
    document.getElementById('formMk').onsubmit = async e => {
        e.preventDefault();
        try {
            await Api.doiMatKhau({ matKhauCu: gt('matKhauCu'), matKhauMoi: gt('matKhauMoi'), nhapLaiMatKhau: gt('nhapLaiMatKhau') });
            toast('Đã đổi mật khẩu');
            e.target.reset();
            e.target.querySelectorAll('[data-loi]').forEach(x => x.textContent = '');
        } catch (err) { hienLoi(e.target, err); }
    };
}
if (canDangNhap()) tai();
