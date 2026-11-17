# Rạp Phương Nam – Hệ thống quản lý rạp chiếu phim

Đồ án Phân tích thiết kế hướng đối tượng (nhóm 08).

| Phần | Công nghệ | Thư mục |
|---|---|---|
| Backend + trang quản lý (Admin, Nhân viên) | Java 17, Spring Boot 3, Thymeleaf, Spring Data JPA | `backend/` |
| Trang khách hàng | HTML, CSS, JavaScript thuần, gọi REST API của backend | `frontend/` |
| Cơ sở dữ liệu | SQL Server, script tạo bảng và dữ liệu mẫu | `database/` |

Chỉ có **một backend**. Trang quản lý và trang khách dùng chung nghiệp vụ và database thông qua backend này.

```
rap-phuong-nam/
├── database/
│   └── QuanLyRapChieuPhim.sql        tạo 22 bảng + dữ liệu mẫu
├── backend/
│   ├── pom.xml                        thư viện Maven
│   └── src/main/
│       ├── java/vn/rapphuongnam/
│       │   ├── RapPhuongNamApplication.java   chạy file này
│       │   ├── entity/               lớp ánh xạ bảng (Phim, TheLoai, enum trạng thái…)
│       │   ├── repository/           truy vấn database (Spring Data JPA)
│       │   ├── dto/                  dữ liệu form và JSON trả cho frontend
│       │   ├── service/              quy tắc nghiệp vụ
│       │   ├── controller/admin/     trang quản lý (trả về giao diện Thymeleaf)
│       │   ├── controller/api/       REST API cho trang khách (trả JSON)
│       │   └── config/               cấu hình (CORS…)
│       └── resources/
│           ├── application.properties         chuỗi kết nối database
│           ├── templates/admin/               giao diện quản lý (.html)
│           └── static/admin/admin.css         CSS dùng chung
└── frontend/
    ├── index.html
    ├── css/style.css
    └── js/api.js, app.js
```

## Cài đặt lần đầu

1. Cài **JDK 17** trở lên, **Maven**, **SQL Server** và **VS Code**.
2. Mở thư mục `rap-phuong-nam` bằng VS Code. Bấm *Install* khi VS Code gợi ý các extension (Extension Pack for Java, Spring Boot Extension Pack, Live Server).
3. Tạo database: mở `database/QuanLyRapChieuPhim.sql` bằng SSMS rồi bấm *Execute*.
4. Bật đăng nhập SQL Server bằng tài khoản `sa` và TCP/IP cổng 1433:
   - SSMS: chuột phải server → *Properties* → *Security* → chọn *SQL Server and Windows Authentication mode*; vào *Security → Logins → sa* đặt mật khẩu và bật *Enabled*.
   - *SQL Server Configuration Manager* → *SQL Server Network Configuration* → *Protocols* → bật **TCP/IP**, kiểm tra cổng 1433 → khởi động lại dịch vụ SQL Server.
5. Sửa `backend/src/main/resources/application.properties` cho đúng `username` và `password` của máy mình.

## Chạy

**Backend + trang quản lý**
- Mở `RapPhuongNamApplication.java` → bấm *Run* phía trên hàm `main` (hoặc dùng *Spring Boot Dashboard*).
- Hoặc chạy bằng terminal: `cd backend` rồi `mvn spring-boot:run`.
- Mở http://localhost:8080/admin

**Trang khách**
- Chạy backend trước, sau đó chuột phải `frontend/index.html` → *Open with Live Server* (mở ở cổng 5500).

## REST API hiện có

| Phương thức | Đường dẫn | Mô tả |
|---|---|---|
| GET | `/api/phim?trangThai=dangChieu` | Phim đang chiếu (`sapChieu` để lấy phim sắp chiếu) |
| GET | `/api/phim/{id}` | Chi tiết một phim (phim Ẩn trả về 404) |

## Chức năng đã làm

- [x] UC2 Quản lý phim: 2.1 thêm, 2.2 sửa, 2.3 đổi trạng thái, 2.4 xem danh sách, 2.5 tìm kiếm
- [ ] UC3 Suất chiếu · UC4 Phòng & sơ đồ ghế · UC5 Giá vé · UC6 Thực phẩm, đơn hàng
- [ ] UC8 Tài khoản · UC9 Báo cáo · UC10 Cài đặt · UC1 Đăng nhập, phân quyền
- [ ] Trang khách: UC7 đặt vé trực tuyến

## Cách thêm một chức năng mới (làm giống phần Phim)

1. `entity/` – tạo lớp ánh xạ bảng, ví dụ `SuatChieu.java`.
2. `repository/` – tạo interface `SuatChieuRepository extends JpaRepository<SuatChieu, Integer>`.
3. `service/` – viết quy tắc nghiệp vụ trong `SuatChieuService.java`.
4. `controller/admin/` – tạo `SuatChieuController` với `@RequestMapping("/admin/suat-chieu")`.
5. `templates/admin/suat-chieu/` – tạo giao diện, dùng lại `fragments.html` (head, sidebar, flash).
6. Mở mục đó trong `fragments.html` (bỏ class `soon`, thêm `th:href`).
7. Nếu trang khách cần dữ liệu thì thêm API trong `controller/api/`.

Quy ước: tên bảng/cột giữ nguyên camelCase như database; trạng thái lưu bằng enum có tên trùng giá trị trong database (`sapChieu`, `dangChieu`, `an`…); không xóa cứng dữ liệu, chỉ đổi trạng thái.

# yeu-nham-doi-tuong
Phân tích thiết kế hướng đối tượng
