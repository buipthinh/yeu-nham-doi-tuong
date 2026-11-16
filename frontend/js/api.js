//Dia chi backend Spring Boot
const API_URL = 'http://localhost:8080/api';

//Goi API va tra ve JSON
async function goiApi(duongDan) {
    const res = await fetch(API_URL + duongDan);
    if (!res.ok) throw new Error('Lỗi ' + res.status);
    return res.json();
}

const PhimApi = {
    dangChieu: () => goiApi('/phim?trangThai=dangChieu'),
    sapChieu: () => goiApi('/phim?trangThai=sapChieu'),
    chiTiet: id => goiApi('/phim/' + id)
};
