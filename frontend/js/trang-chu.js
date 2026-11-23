//UC7.1.1 tim kiem va chon phim
veHeader('phim');
const noiDung = document.getElementById('noiDung');
let tab = thamSo('tab') === 'sapChieu' ? 'sapChieu' : 'dangChieu', dsPhim = [], tuKhoa = '', theLoai = '';

noiDung.innerHTML = `
    <div class="toolbar">
        <div class="tabs">
            <button class="tab" data-tt="dangChieu">Phim đang chiếu</button>
            <button class="tab" data-tt="sapChieu">Phim sắp chiếu</button>
        </div>
        <div class="row">
            <input class="input" id="tuKhoa" type="search" placeholder="Tìm theo tên phim…" aria-label="Tìm phim">
            <select class="input" id="theLoai" aria-label="Thể loại"><option value="">Tất cả thể loại</option></select>
        </div>
    </div>
    <p id="thongBao" class="muted">Đang tải…</p>
    <div id="dsPhim" class="movies"></div>`;

const ve = () => {
    const ds = dsPhim.filter(p => (!tuKhoa || p.tenPhim.toLowerCase().includes(tuKhoa)) && (!theLoai || p.theLoai.includes(theLoai)));
    document.getElementById('thongBao').textContent = !dsPhim.length ? (tab === 'dangChieu' ? 'Hiện chưa có phim đang chiếu.' : 'Chưa có phim sắp chiếu.')
        : ds.length ? ds.length + ' phim' : 'Không tìm thấy phim phù hợp.';
    document.getElementById('dsPhim').innerHTML = ds.map(p => `
        <a class="movie" href="phim.html?id=${p.id}">
            ${poster(p)}
            <div class="body">
                <h3>${esc(p.tenPhim)}</h3>
                <span class="muted small">${esc(p.theLoai.join(', '))}</span>
                <span class="muted small">${p.thoiLuong} phút · ${tab === 'sapChieu' ? 'Khởi chiếu ' + ngayVN(p.ngayKhoiChieu) : esc(p.ngonNgu || '')}</span>
            </div>
        </a>`).join('');
};

async function tai() {
    document.querySelectorAll('.tab').forEach(b => b.classList.toggle('on', b.dataset.tt === tab));
    try {
        dsPhim = await Api.phim(tab);
        ve();
    } catch (e) {
        document.getElementById('thongBao').textContent = e.message;
    }
}

document.querySelectorAll('.tab').forEach(b => b.onclick = () => { tab = b.dataset.tt; history.replaceState(null, '', '?tab=' + tab); tai(); });
document.getElementById('tuKhoa').oninput = e => { tuKhoa = e.target.value.trim().toLowerCase(); ve(); };
document.getElementById('theLoai').onchange = e => { theLoai = e.target.value; ve(); };
Api.theLoai().then(ds => {
    document.getElementById('theLoai').innerHTML += ds.map(t => `<option>${esc(t.tenTheLoai)}</option>`).join('');
}).catch(() => {});
tai();
