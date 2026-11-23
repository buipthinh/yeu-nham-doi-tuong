//Dia chi backend Spring Boot
const API_URL = 'http://localhost:8080/api';
const KHOA_TOKEN = 'rpn-token', KHOA_TK = 'rpn-tai-khoan';

const Phien = {
    token() { try { return localStorage.getItem(KHOA_TOKEN); } catch (e) { return null; } },
    taiKhoan() { try { return JSON.parse(localStorage.getItem(KHOA_TK) || 'null'); } catch (e) { return null; } },
    luu(token, tk) { try { if (token) localStorage.setItem(KHOA_TOKEN, token); localStorage.setItem(KHOA_TK, JSON.stringify(tk)); } catch (e) {} },
    xoa() { try { localStorage.removeItem(KHOA_TOKEN); localStorage.removeItem(KHOA_TK); } catch (e) {} }
};

//Loi tu backend: { loi, duLieu } + ma HTTP
class LoiApi extends Error {
    constructor(msg, status, duLieu) { super(msg); this.status = status; this.duLieu = duLieu; }
}

async function goiApi(duongDan, { method = 'GET', body } = {}) {
    const headers = {};
    if (body !== undefined) headers['Content-Type'] = 'application/json';
    const t = Phien.token();
    if (t) headers['Authorization'] = 'Bearer ' + t;
    let res;
    try {
        res = await fetch(API_URL + duongDan, { method, headers, body: body === undefined ? undefined : JSON.stringify(body) });
    } catch (e) {
        throw new LoiApi('Không kết nối được máy chủ. Hãy chạy backend ở cổng 8080.', 0);
    }
    const data = await res.json().catch(() => ({}));
    if (res.status === 401) {
        Phien.xoa();
        throw new LoiApi(data.loi || 'Vui lòng đăng nhập', 401);
    }
    if (!res.ok) throw new LoiApi(data.loi || ('Lỗi ' + res.status), res.status, data.duLieu);
    return data;
}

const Api = {
    rap: () => goiApi('/rap'),
    theLoai: () => goiApi('/the-loai'),
    phim: trangThai => goiApi('/phim?trangThai=' + trangThai),
    chiTietPhim: id => goiApi('/phim/' + id),
    suatCuaPhim: id => goiApi('/phim/' + id + '/suat-chieu'),
    suat: id => goiApi('/suat-chieu/' + id),
    thucPham: () => goiApi('/thuc-pham'),
    lichChieu: ngay => goiApi('/lich-chieu' + (ngay ? '?ngay=' + ngay : '')),
    bangGia: () => goiApi('/bang-gia'),
    phong: () => goiApi('/phong'),
    dangNhap: (tenDangNhap, matKhau) => goiApi('/auth/dang-nhap', { method: 'POST', body: { tenDangNhap, matKhau } }),
    dangKy: f => goiApi('/auth/dang-ky', { method: 'POST', body: f }),
    dangXuat: () => goiApi('/auth/dang-xuat', { method: 'POST', body: {} }),
    taiKhoan: () => goiApi('/tai-khoan'),
    capNhatTaiKhoan: f => goiApi('/tai-khoan', { method: 'PUT', body: f }),
    doiMatKhau: f => goiApi('/tai-khoan/mat-khau', { method: 'PUT', body: f }),
    taoDon: (suatChieuId, ve) => goiApi('/don-hang', { method: 'POST', body: { suatChieuId, ve } }),
    donCuaToi: trangThai => goiApi('/don-hang' + (trangThai ? '?trangThai=' + trangThai : '')),
    don: id => goiApi('/don-hang/' + id),
    chonThucPham: (id, mon) => goiApi('/don-hang/' + id + '/thuc-pham', { method: 'PUT', body: { mon } }),
    thanhToan: (id, phuongThuc, ketQua) => goiApi('/don-hang/' + id + '/thanh-toan', { method: 'POST', body: { phuongThuc, ketQua } }),
    huyDon: id => goiApi('/don-hang/' + id + '/huy', { method: 'POST', body: {} })
};
