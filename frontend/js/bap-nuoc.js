//Thuc don bap nuoc dang ban. Khach chon mon o buoc 3 khi dat ve (UC7.1.4)
veHeader('bap');
const noiDung = document.getElementById('noiDung');
const NHOM = [['combo', 'Combo'], ['bap', 'Bắp rang'], ['nuoc', 'Nước uống']];

async function tai() {
    try {
        const ds = await Api.thucPham();
        noiDung.innerHTML = `
            <h1>Bắp nước</h1>
            <p class="muted" style="margin-top:0">Bạn chọn bắp nước ở bước “Bắp nước” khi đặt vé, nhận tại quầy bằng mã vé.</p>
            ${!ds.length ? '<div class="empty">Hiện chưa có món nào đang bán.</div>' : NHOM.map(([loai, ten]) => {
                const mon = ds.filter(m => m.loai === loai);
                return mon.length ? `<h2 style="margin-top:22px">${ten}</h2>
                    <div class="foods">${mon.map(m => `
                        <div class="card food">
                            <div class="food-img">${m.hinhAnh ? `<img src="${esc(m.hinhAnh)}" alt="" onerror="this.remove()">` : ''}${esc(m.tenMon.charAt(0))}</div>
                            <div class="grid" style="gap:4px;align-content:start">
                                <b>${esc(m.tenMon)}</b>
                                <span class="muted small">${esc(m.thanhPhan || '')}</span>
                                <span class="row"><b>${tien(m.giaBan)}</b>${m.tonKho > 0 ? '' : '<span class="pill bad">Tạm hết</span>'}</span>
                            </div>
                        </div>`).join('')}</div>` : '';
            }).join('')}
            <p style="margin-top:24px"><a class="btn primary" href="lich-chieu.html">Xem lịch chiếu và đặt vé</a></p>`;
    } catch (e) {
        noiDung.innerHTML = `<div class="empty">${esc(e.message)}</div>`;
    }
}

tai();
