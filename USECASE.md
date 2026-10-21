# ĐẶC TẢ USE CASE — HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM

## 1. Actor

| Actor | Vai trò |
|---|---|
| Customer | Đăng ký, đăng nhập, quản lý thông tin cá nhân, tìm phim/suất chiếu, đặt vé online, quản lý vé điện tử và xem lịch sử đặt vé |
| Admin | Đăng nhập; quản lý phim, suất chiếu, phòng chiếu, sơ đồ chỗ ngồi, loại vé, giá vé, Bắp Nước, vé bán ra, tài khoản Customer/Employee; xem báo cáo/thống kê; cài đặt hệ thống |

## 2. BFD

| # | Chức năng BFD (phân hệ) | Chức năng con trong BFD | UC |
|---|---|---|---|
| 1 | Đăng nhập và đăng ký | Đăng nhập | UC02 |
| | | Đăng ký tài khoản | UC01 |
| 2 | Quản lý phim | Cập nhật phim (thêm, sửa, thay đổi trạng thái); xem danh sách; tìm kiếm | UC09, UC10, UC11, UC12, UC13 |
| 3 | Quản lý suất chiếu | Cập nhật suất chiếu (thêm, sửa, thay đổi trạng thái); xem danh sách; tìm kiếm | UC14, UC15, UC16, UC17, UC18 |
| 4 | Quản lý phòng chiếu và sơ đồ chỗ ngồi | Cập nhật phòng chiếu (thêm, sửa, thay đổi trạng thái); xem danh sách; tìm kiếm phòng | UC19, UC20, UC21, UC22, UC23 |
| | | Cập nhật sơ đồ chỗ ngồi (thiết lập, phân loại ghế, thay đổi trạng thái ghế); xem sơ đồ | UC24, UC25, UC26, UC27 |
| 5 | Quản lý loại vé và giá vé | Quản lý giá vé (thiết lập, sửa, tra cứu lịch sử thay đổi) | UC28, UC29, UC30 |
| | | Quản lý loại vé (thêm, sửa, thay đổi trạng thái, thiết lập đối tượng áp dụng) | UC31, UC32, UC33, UC34 |
| 6 | Quản lý đặt vé và dịch vụ | Quản lý thực phẩm (thêm, sửa, thay đổi trạng thái món, theo dõi tồn kho) | UC35, UC36, UC37, UC38 |
| | | Quản lý đơn hàng (xem danh sách toàn bộ đơn; kiểm tra tình trạng thanh toán của khách) | UC39, UC40 |
| 7 | Chức năng khách hàng | Đặt vé trực tuyến: tìm kiếm và chọn phim; tìm kiếm và chọn suất chiếu | UC05 |
| | | Đặt vé trực tuyến: chọn sơ đồ ghế; chọn dịch vụ đi kèm | UC06 |
| | | Đặt vé trực tuyến: thanh toán trực tuyến | UC07 |
| | | Đặt vé trực tuyến: quản lý vé điện tử | UC08 |
| | | Quản lý thông tin cá nhân (xem, cập nhật) | UC03, UC04 |
| 8 | Quản lý tài khoản | Thêm tài khoản; khóa/mở; chỉnh sửa thông tin; xem chi tiết; khởi tạo mật khẩu; chỉnh sửa chức vụ; tìm kiếm | UC41, UC42, UC43, UC44, UC45, UC46, UC47 |
| 9 | Quản lý báo cáo và thống kê | Báo cáo doanh thu; báo cáo mức độ quan tâm theo phim | UC48, UC49 |
| 10 | Cài đặt hệ thống | Chỉnh sửa thông số rạp phim; sao lưu dữ liệu | UC50, UC51 |

UC52 (Giải phóng Hold hết hạn) là tác vụ nền của SystemScheduler nên không nằm trong BFD.

# 3. DANH SÁCH 52 USE CASE

| # | Use Case | Actor | Nhóm |
|---|---|---|---|
| UC01 | Đăng ký tài khoản | Customer | Tài khoản và đăng nhập |
| UC02 | Đăng nhập | Customer, Admin, Employee | Tài khoản và đăng nhập |
| UC03 | Xem thông tin cá nhân | Customer | Tài khoản và đăng nhập |
| UC04 | Cập nhật thông tin cá nhân | Customer | Tài khoản và đăng nhập |
| UC05 | Xem phim và suất chiếu | Customer | Đặt vé trực tuyến |
| UC06 | Đặt vé trực tuyến | Customer | Đặt vé trực tuyến |
| UC07 | Thanh toán trực tuyến | Customer | Đặt vé trực tuyến |
| UC08 | Quản lý vé điện tử và lịch sử đặt vé | Customer | Đặt vé trực tuyến |
| UC09 | Thêm phim | Admin | Quản lý phim |
| UC10 | Sửa phim | Admin | Quản lý phim |
| UC11 | Thay đổi trạng thái phim | Admin | Quản lý phim |
| UC12 | Xem danh sách phim | Admin | Quản lý phim |
| UC13 | Tìm kiếm phim | Admin | Quản lý phim |
| UC14 | Thêm suất chiếu | Admin | Quản lý suất chiếu |
| UC15 | Sửa suất chiếu | Admin | Quản lý suất chiếu |
| UC16 | Thay đổi trạng thái suất chiếu | Admin | Quản lý suất chiếu |
| UC17 | Xem danh sách suất chiếu | Admin | Quản lý suất chiếu |
| UC18 | Tìm kiếm suất chiếu | Admin | Quản lý suất chiếu |
| UC19 | Thêm phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC20 | Sửa phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC21 | Thay đổi trạng thái phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC22 | Xem danh sách phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC23 | Tìm kiếm phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC24 | Thiết lập sơ đồ chỗ ngồi | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC25 | Phân loại ghế | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC26 | Xem danh sách phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC27 | Tìm kiếm phòng chiếu | Admin | Quản lý phòng chiếu và sơ đồ chỗ ngồi |
| UC28 | Thiết lập giá vé | Admin | Quản lý loại vé và giá vé |
| UC29 | Sửa giá vé | Admin | Quản lý loại vé và giá vé |
| UC30 | Thay đổi trạng thái giá vé | Admin | Quản lý loại vé và giá vé |
| UC31 | Thêm loại vé | Admin | Quản lý loại vé và giá vé |
| UC32 | Sửa loại vé | Admin | Quản lý loại vé và giá vé |
| UC33 | Thay đổi trạng thái loại vé | Admin | Quản lý loại vé và giá vé |
| UC34 | Thiết lập đối tượng áp dụng | Admin | Quản lý loại vé và giá vé |
| UC35 | Thêm món | Admin | Quản lý thực phẩm |
| UC36 | Sửa món | Admin | Quản lý thực phẩm |
| UC37 | Thay đổi trạng thái món | Admin | Quản lý thực phẩm |
| UC38 | Theo dõi số lượng tồn kho | Admin | Quản lý thực phẩm |
| UC39 | Xem danh sách toàn bộ đơn | Admin, Employee | Quản lý đơn hàng |
| UC40 | Kiểm tra tình trạng thanh toán của khách | Admin, Employee | Quản lý đơn hàng |
| UC41 | Thêm tài khoản | Admin | Quản lý tài khoản |
| UC42 | Khóa/ Mở tài khoản |  Admin | Quản lý tài khoản |
| UC43 | Chỉnh sửa thông tin tài khoản | Admin | Quản lý tài khoản |
| UC44 | Xem chi tiết tài khoản | Admin | Quản lý tài khoản |
| UC45 | Khởi tạo tài khoản | Admin | Quản lý tài khoản |
| UC46 | Chỉnh sửa chức vụ | Admin | Quản lý tài khoản |
| UC47 | Tìm kiếm tài khoản | Admin | Quản lý tài khoản |
| UC48 | Báo cáo doanh thu | Admin | Quản lý Báo cáo và Thống kê |
| UC49 | Báo cáo mức độ quan tâm theo phim | Admin | Quản lý Báo cáo và Thống kê |
| UC50 | Chỉnh sửa thông số rạp phim | Admin | Quản lý hệ thống |
| UC51 | Sao lưu dữ liệu | Admin | Quản lý hệ thống |
| UC52 | Giải phóng Hold hết hạn | # | # |

---

# 4. ĐẶC TẢ USE CASE

# A. CUSTOMER

## UC01 — Đăng ký tài khoản

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Người dùng mới tạo tài khoản Customer để đặt vé trực tuyến.  
**Tiền điều kiện:** Người dùng chưa có tài khoản.

### Dòng sự kiện chính

1. Customer chọn Đăng ký.
2. Customer nhập họ tên, email, số điện thoại, mật khẩu và xác nhận mật khẩu.
3. Hệ thống kiểm tra định dạng dữ liệu, mật khẩu tối thiểu 8 ký tự và mật khẩu xác nhận trùng khớp.
4. Hệ thống kiểm tra email và số điện thoại chưa được dùng cho tài khoản khác.
5. Hệ thống mã hóa mật khẩu và tạo tài khoản Customer ở trạng thái Hoạt động.
6. Hệ thống thông báo đăng ký thành công và chuyển sang màn hình đăng nhập (UC02).

### Dòng sự kiện phụ

- 3.1. Dữ liệu không hợp lệ → báo lỗi tại trường tương ứng → quay lại bước 2.
- 4.1. Email hoặc số điện thoại đã tồn tại → thông báo tài khoản đã tồn tại → quay lại bước 2.

### Hậu điều kiện

- **Thành công:** tài khoản Customer mới được tạo, mật khẩu lưu dạng mã hóa.
- **Thất bại:** không tạo tài khoản.

---

## UC02 — Đăng nhập

**Actor chính:** Customer, Employee, Admin (dùng chung một UC, không tách theo actor)  
**Quan hệ:** —  
**Tóm tắt:** Người dùng đăng nhập bằng tài khoản đã có. Hệ thống phân quyền giao diện theo vai trò sau khi xác thực thành công.  
**Tiền điều kiện:** Người dùng đã có tài khoản (Customer tự đăng ký qua UC01, Employee và Admin do Admin tạo qua UC41).

### Dòng sự kiện chính

1. Người dùng mở trang đăng nhập.
2. Người dùng nhập tài khoản và mật khẩu. Customer dùng email hoặc số điện thoại, Employee và Admin dùng mã nhân viên.
3. Hệ thống xác thực thông tin đăng nhập.
4. Hệ thống kiểm tra trạng thái tài khoản.
5. Hệ thống tạo phiên đăng nhập và gán quyền theo vai trò.
6. Hệ thống chuyển vào giao diện theo vai trò: Customer vào trang đặt vé, Employee vào Quản lý đơn hàng (UC39, UC40), Admin vào trang quản trị.

### Dòng sự kiện phụ

- 3.1. Sai tài khoản hoặc mật khẩu → thông báo lỗi → quay lại bước 2. Sau 5 lần sai liên tiếp, hệ thống tạm khóa đăng nhập 5 phút.
- 3.2. Tài khoản đang bị tạm khóa do sai nhiều lần → thông báo thời gian chờ → kết thúc.
- 4.1. Tài khoản bị khóa (UC42) → thông báo tài khoản bị khóa → kết thúc.
- Quên mật khẩu nằm ngoài phạm vi: người dùng liên hệ Admin để khởi tạo lại mật khẩu (UC45).

### Hậu điều kiện

Người dùng có phiên đăng nhập hợp lệ với quyền tương ứng vai trò.

---

## UC03 — Xem thông tin cá nhân

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer xem thông tin tài khoản của chính mình.  
**Tiền điều kiện:** Customer đã đăng nhập.

### Dòng sự kiện chính

1. Customer chọn Tài khoản của tôi.
2. Hệ thống lấy thông tin của Customer đang đăng nhập.
3. Hệ thống hiển thị họ tên, email, số điện thoại, ngày sinh, giới tính, địa chỉ và ngày đăng ký. Mật khẩu không được hiển thị.

### Dòng sự kiện phụ

- 2.1. Không tải được dữ liệu → thông báo lỗi và cho phép thử lại.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC04 — Cập nhật thông tin cá nhân

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer chỉnh sửa thông tin cá nhân hoặc đổi mật khẩu của chính mình.  
**Tiền điều kiện:** Customer đã đăng nhập.

### Dòng sự kiện chính

1. Customer chọn Chỉnh sửa tại màn hình thông tin cá nhân (UC03).
2. Customer sửa họ tên, ngày sinh, giới tính, số điện thoại, địa chỉ. Nếu muốn đổi mật khẩu, Customer nhập thêm mật khẩu hiện tại, mật khẩu mới và xác nhận mật khẩu mới.
3. Customer chọn Lưu.
4. Hệ thống kiểm tra định dạng dữ liệu và số điện thoại chưa thuộc tài khoản khác.
5. Nếu có đổi mật khẩu, hệ thống kiểm tra mật khẩu hiện tại đúng và mật khẩu mới tối thiểu 8 ký tự, khớp với phần xác nhận.
6. Hệ thống cập nhật thông tin và thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Dữ liệu không hợp lệ hoặc số điện thoại đã được dùng → báo lỗi tại trường tương ứng → quay lại bước 2.
- 5.1. Mật khẩu hiện tại sai hoặc mật khẩu mới không hợp lệ → báo lỗi → quay lại bước 2.
- Email là tên đăng nhập nên không được sửa ở UC này.

### Hậu điều kiện

- **Thành công:** thông tin (và mật khẩu nếu có đổi) được cập nhật.
- **Thất bại:** dữ liệu giữ nguyên.

---

# 4. Nhóm Đặt vé trực tuyến (UC05–UC08)

## UC05 — Xem phim và suất chiếu

**Actor chính:** Customer  
**Quan hệ:** Được UC06 include.  
**Tóm tắt:** Customer xem danh sách và chi tiết phim, tìm phim theo tên hoặc thể loại, rồi chọn một suất chiếu.  
**Tiền điều kiện:** Không bắt buộc đăng nhập.

### Dòng sự kiện chính

1. Customer mở trang Phim.
2. Hệ thống hiển thị danh sách phim ở trạng thái Đang chiếu và Sắp chiếu.
3. Customer có thể nhập từ khóa để tìm phim theo tên, hoặc lọc theo thể loại.
4. Hệ thống hiển thị kết quả phù hợp.
5. Customer chọn một phim.
6. Hệ thống hiển thị chi tiết phim gồm poster, trailer, thể loại, thời lượng, ngôn ngữ, phụ đề, độ tuổi, mô tả và danh sách suất chiếu theo ngày (phòng, giờ bắt đầu, giá từ, tình trạng còn vé hoặc hết vé).
7. Customer chọn ngày và một suất chiếu.
8. Hệ thống ghi nhận suất chiếu đã chọn.

### Dòng sự kiện phụ

- 2.1. Chưa có phim phù hợp → thông báo.
- 4.1. Không tìm thấy phim khớp từ khóa hoặc bộ lọc → thông báo Không tìm thấy phim phù hợp.
- 6.1. Phim chưa có suất chiếu mở bán → thông báo chưa có suất chiếu.
- 7.1. Suất chiếu hết vé hoặc đã bắt đầu → không cho chọn.
- 8.1. Customer chưa đăng nhập mà muốn đặt vé → chuyển sang UC02, đăng nhập xong quay lại suất chiếu đã chọn.

### Hậu điều kiện

Customer chọn được một suất chiếu. Không thay đổi dữ liệu hệ thống.

---

## UC06 — Đặt vé online

**Actor chính:** Customer

**Actor liên quan:** SystemScheduler (gián tiếp, giải phóng ghế khi hết hạn giữ)

**Quan hệ:** include UC05, include UC07. Việc chọn thực phẩm đi kèm là hành vi tùy chọn trong UC này, không tách thành UC riêng.  
**Tóm tắt:** Customer chọn suất chiếu, chọn ghế, chọn thực phẩm (nếu muốn), xác nhận đơn và chuyển sang thanh toán.  
**Tiền điều kiện:** Customer đã đăng nhập. Suất chiếu ở trạng thái Mở bán, chưa bắt đầu và còn ghế trống.

### Dòng sự kiện chính

1. Customer thực hiện UC05 để chọn một suất chiếu.
2. Hệ thống hiển thị sơ đồ ghế của suất chiếu: ghế trống, đang giữ, đã bán, ghế bảo trì, loại ghế và giá.
3. Customer chọn tối đa 8 ghế còn trống.
4. Customer chọn đối tượng cho từng vé (Người lớn, Trẻ em, Sinh viên). Mặc định là Người lớn.
5. Hệ thống giữ các ghế đã chọn bằng một thao tác cập nhật nguyên tử: các ghế chuyển sang trạng thái `HOLD`, hạn giữ là thời điểm hiện tại cộng 5 phút, đồng thời hiển thị đồng hồ đếm ngược.
6. Customer có thể chọn thực phẩm đi kèm (món Đang bán, còn tồn kho) và số lượng.
7. Hệ thống tính tổng tiền gồm giá từng vé theo quy tắc giá (UC28) và tiền thực phẩm.
8. Customer kiểm tra và xác nhận đơn.
9. Hệ thống tạo đơn ở trạng thái Chờ thanh toán và chuyển sang thanh toán (UC07).

### Dòng sự kiện phụ

- 1.1. Phiên đăng nhập hết hạn → chuyển sang UC02, đăng nhập xong quay lại bước 2.
- 3.1. Customer chọn quá 8 ghế → hệ thống chặn và thông báo số ghế tối đa.
- 5.1. Một hoặc nhiều ghế vừa bị người khác giữ hoặc mua → thông báo ghế không còn trống, nhả toàn bộ ghế đã giữ trong lần chọn này → quay lại bước 2.
- 5.2. Hệ thống không tìm thấy giá vé áp dụng cho một vé → thông báo suất chiếu chưa thể đặt, không giữ ghế.
- 6.1. Món đã hết hàng hoặc ngừng bán → không cho chọn.
- 8.1. Customer hủy đơn trước khi thanh toán → ghế chuyển về `AVAILABLE` ngay, đơn ở trạng thái Đã hủy.
- 8.2. Hết hạn giữ trước khi xác nhận → ghế về `AVAILABLE` nhờ UC52, đơn ở trạng thái Hết hạn, Customer quay lại bước 2.

### Hậu điều kiện

Khi UC07 thành công: đơn Đã thanh toán, ghế `SOLD`, mã vé được sinh. Khi hủy hoặc hết hạn: ghế về `AVAILABLE`, đơn Đã hủy hoặc Hết hạn.

---

## UC07 — Thanh toán online

**Actor chính:** Customer  
**Quan hệ:** Được UC06 include.  
**Tóm tắt:** Customer thanh toán đơn bằng MoMo, VNPay hoặc thẻ tín dụng. Cổng thanh toán có thể là bản mô phỏng khi demo.  
**Tiền điều kiện:** Đơn đang Chờ thanh toán, các ghế vẫn ở `HOLD` và còn hạn giữ.

### Dòng sự kiện chính

1. Hệ thống hiển thị thông tin đơn, tổng tiền và thời gian giữ ghế còn lại.
2. Customer chọn phương thức thanh toán: MoMo, VNPay hoặc Thẻ tín dụng.
3. Hệ thống chuyển Customer sang cổng thanh toán tương ứng.
4. Customer hoàn tất thanh toán tại cổng.
5. Cổng thanh toán trả kết quả. Hệ thống xác thực kết quả (mã giao dịch, số tiền khớp với đơn).
6. Hệ thống cập nhật đơn thành Đã thanh toán, chuyển các ghế từ `HOLD` sang `SOLD` và tạo vé điện tử cho từng ghế.
7. Hệ thống sinh mã vé 8 ký tự duy nhất cho đơn và trừ tồn kho thực phẩm đã chọn.
8. Hệ thống hiển thị vé điện tử kèm mã vé.

### Dòng sự kiện phụ

- 5.1. Thanh toán thất bại hoặc Customer hủy tại cổng → ghi nhận giao dịch thất bại, đơn vẫn Chờ thanh toán, ghế tiếp tục `HOLD` đến hết hạn → cho Customer chọn lại phương thức (về bước 2).
- 5.2. Kết quả không hợp lệ (sai số tiền hoặc sai mã giao dịch) → từ chối kết quả, đơn không đổi trạng thái.
- 5.3. Hết hạn giữ trước khi thanh toán xong → đơn Hết hạn, thông báo giao dịch không hoàn tất. Nếu cổng báo thanh toán thành công muộn, hệ thống ghi nhận để đối soát thủ công (hoàn tiền nằm ngoài phạm vi).
- 7.1. Tồn kho không đủ tại thời điểm trừ → hệ thống ghi nhận cảnh báo cho Admin xử lý, đơn vẫn hoàn tất.

### Hậu điều kiện

- **Thành công:** đơn Đã thanh toán, ghế `SOLD`, có mã vé.
- **Thất bại:** đơn chưa Đã thanh toán, ghế chưa `SOLD`.

---

## UC08 — Quản lý vé điện tử và lịch sử đặt vé

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer xem danh sách đơn đã đặt của chính mình và mở vé điện tử của từng đơn.  
**Tiền điều kiện:** Customer đã đăng nhập.

### Dòng sự kiện chính

1. Customer mở Lịch sử đặt vé.
2. Hệ thống chỉ lấy các đơn của Customer đang đăng nhập.
3. Hệ thống hiển thị danh sách theo thời gian, mới nhất trước, gồm mã đơn, phim, suất chiếu, tổng tiền và trạng thái đơn.
4. Customer có thể lọc theo trạng thái đơn (Chờ thanh toán, Đã thanh toán, Đã hủy, Hết hạn).
5. Customer chọn một đơn.
6. Hệ thống hiển thị chi tiết đơn: mã vé, phim, suất chiếu, phòng, danh sách ghế, thực phẩm, tổng tiền, phương thức thanh toán. Với đơn Đã thanh toán, hệ thống hiển thị vé điện tử.

### Dòng sự kiện phụ

- 2.1. Customer chưa có đơn nào → thông báo chưa có lịch sử đặt vé.
- 6.1. Đơn Chờ thanh toán còn hạn giữ ghế → hiển thị thời gian còn lại và nút Thanh toán để tiếp tục UC07.
- Trạng thái vé: Hợp lệ khi đơn Đã thanh toán và suất chiếu chưa kết thúc, Đã hết hạn sau khi suất chiếu kết thúc. Hủy vé và hoàn tiền nằm ngoài phạm vi.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 5. Nhóm Quản lý phim (UC09–UC13)

Trạng thái phim: Sắp chiếu, Đang chiếu, Ẩn. Thể loại là danh sách cố định nạp sẵn trong hệ thống, Admin chọn khi thêm hoặc sửa phim.

## UC09 — Thêm phim

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin thêm một phim mới vào hệ thống.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin chọn Thêm phim.
2. Hệ thống hiển thị biểu mẫu nhập phim.
3. Admin nhập tên phim, đạo diễn, diễn viên, thể loại, thời lượng (phút), ngôn ngữ, phụ đề, ngày khởi chiếu, ngày kết thúc, độ tuổi (P, C13, C16, C18), mô tả nội dung, poster và trailer.
4. Hệ thống kiểm tra các trường bắt buộc, thời lượng lớn hơn 0, ngày kết thúc không trước ngày khởi chiếu, định dạng ảnh và liên kết trailer.
5. Hệ thống kiểm tra tên phim không trùng với phim đã có.
6. Admin xác nhận lưu.
7. Hệ thống sinh mã phim và lưu phim ở trạng thái Sắp chiếu.
8. Hệ thống thông báo thêm thành công.

### Dòng sự kiện phụ

- 4.1. Dữ liệu không hợp lệ → báo lỗi tại trường tương ứng → quay lại bước 3.
- 5.1. Tên phim đã tồn tại → cảnh báo trùng → Admin sửa tên hoặc hủy.
- 6.1. Admin hủy → không lưu dữ liệu.

### Hậu điều kiện

Thành công: phim mới được lưu với trạng thái Sắp chiếu. Thất bại hoặc hủy: không thêm phim.

---

## UC10 — Sửa phim

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC12 hoặc UC13 để chọn phim.  
**Tóm tắt:** Admin chỉnh sửa thông tin của một phim đã có.  
**Tiền điều kiện:** Admin đã đăng nhập. Phim tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phim từ danh sách hoặc kết quả tìm kiếm.
2. Admin chọn Sửa phim.
3. Hệ thống hiển thị thông tin hiện tại của phim.
4. Admin chỉnh sửa các trường cần thiết (mã phim không được sửa).
5. Hệ thống kiểm tra dữ liệu như bước 4 và 5 của UC09.
6. Admin xác nhận lưu.
7. Hệ thống cập nhật phim và thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Dữ liệu không hợp lệ hoặc tên phim trùng → báo lỗi → quay lại bước 4.
- 5.2. Phim đang có suất chiếu chưa diễn ra mà Admin đổi thời lượng → hệ thống chặn thay đổi thời lượng vì ảnh hưởng giờ kết thúc, các trường khác vẫn được sửa.
- 6.1. Admin hủy → giữ nguyên dữ liệu cũ.

### Hậu điều kiện

Thành công: thông tin phim được cập nhật. Thất bại hoặc hủy: dữ liệu giữ nguyên.

---

## UC11 — Thay đổi trạng thái phim

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC12 hoặc UC13 để chọn phim.  
**Tóm tắt:** Admin chuyển phim giữa các trạng thái Sắp chiếu, Đang chiếu và Ẩn.  
**Tiền điều kiện:** Admin đã đăng nhập. Phim tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phim.
2. Admin chọn Thay đổi trạng thái.
3. Hệ thống hiển thị trạng thái hiện tại và các trạng thái có thể chuyển tới.
4. Admin chọn trạng thái mới.
5. Hệ thống kiểm tra điều kiện chuyển trạng thái.
6. Admin xác nhận.
7. Hệ thống cập nhật trạng thái phim. Phim ở trạng thái Ẩn không hiển thị cho Customer và không được dùng để tạo suất chiếu mới.
8. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Chuyển sang Ẩn khi phim có suất chiếu chưa diễn ra đã có đơn đặt → hệ thống chặn và thông báo.
- 5.2. Chuyển sang Ẩn khi phim có suất chiếu chưa diễn ra nhưng chưa có đơn → các suất chiếu đó tự chuyển sang Tạm ngưng sau khi Admin xác nhận.
- 7.1. Chuyển từ Ẩn về Đang chiếu hoặc Sắp chiếu → cho phép, các suất chiếu đã tạm ngưng không tự mở lại.

### Hậu điều kiện

Thành công: trạng thái phim được cập nhật. Thất bại hoặc hủy: giữ nguyên trạng thái.

---

## UC12 — Xem danh sách phim

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem toàn bộ phim trong hệ thống và mở chi tiết từng phim.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Quản lý phim.
2. Hệ thống hiển thị danh sách phim có phân trang gồm mã phim, poster, tên phim, thể loại, thời lượng, ngày khởi chiếu, độ tuổi và trạng thái.
3. Admin có thể sắp xếp theo tên hoặc ngày khởi chiếu.
4. Admin chọn một phim để xem chi tiết đầy đủ.
5. Từ màn hình chi tiết, Admin có thể chuyển sang Sửa phim (UC10) hoặc Thay đổi trạng thái (UC11).

### Dòng sự kiện phụ

- 2.1. Chưa có phim nào → thông báo danh sách trống và gợi ý Thêm phim (UC09).

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC13 — Tìm kiếm phim

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tìm phim theo từ khóa và bộ lọc.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin nhập từ khóa (tên phim hoặc đạo diễn) vào ô tìm kiếm.
2. Admin có thể lọc thêm theo thể loại, trạng thái, độ tuổi.
3. Hệ thống tìm các phim khớp điều kiện.
4. Hệ thống hiển thị kết quả theo định dạng như UC12.
5. Admin chọn một phim để xem chi tiết, sửa hoặc đổi trạng thái.

### Dòng sự kiện phụ

- 3.1. Không tìm thấy phim khớp → thông báo Không tìm thấy phim phù hợp.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 6. Nhóm Quản lý suất chiếu (UC14–UC18)

Trạng thái suất chiếu do Admin đặt: Mở bán, Tạm ngưng. Hệ thống tự suy ra Hết vé (không còn ghế trống) và Đã kết thúc (quá giờ kết thúc).

## UC14 — Thêm suất chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tạo một suất chiếu bằng cách gán phim vào phòng chiếu tại một khung giờ.  
**Tiền điều kiện:** Admin đã đăng nhập. Có phim ở trạng thái Sắp chiếu hoặc Đang chiếu. Có phòng chiếu Hoạt động đã thiết lập sơ đồ chỗ ngồi (UC24).

### Dòng sự kiện chính

1. Admin chọn Thêm suất chiếu.
2. Admin chọn phim, phòng chiếu, ngày chiếu và giờ bắt đầu.
3. Hệ thống tính giờ kết thúc bằng giờ bắt đầu cộng thời lượng phim.
4. Hệ thống kiểm tra ngày chiếu nằm trong khoảng ngày khởi chiếu đến ngày kết thúc của phim.
5. Hệ thống kiểm tra phòng không có suất chiếu khác chồng lấn, tính cả thời gian dọn phòng giữa hai suất (thông số ở UC50).
6. Hệ thống kiểm tra có giá vé áp dụng cho ngày và giờ đã chọn (UC28).
7. Admin xác nhận.
8. Hệ thống lưu suất chiếu ở trạng thái Mở bán và tạo trạng thái ghế theo suất chiếu (ShowtimeSeat) cho mọi ghế Hoạt động của phòng, tất cả ở trạng thái `AVAILABLE`.
9. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Ngày chiếu ngoài khoảng chiếu của phim → báo lỗi → quay lại bước 2.
- 5.1. Trùng khung giờ trong cùng phòng → cảnh báo xung đột, yêu cầu chọn lại → quay lại bước 2.
- 6.1. Chưa có giá vé hiệu lực → cảnh báo, yêu cầu thiết lập giá vé (UC28) trước.
- 7.1. Admin hủy → không lưu.

### Hậu điều kiện

Thành công: suất chiếu và toàn bộ ShowtimeSeat tương ứng được tạo. Thất bại hoặc hủy: không tạo gì.

---

## UC15 — Sửa suất chiếu

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC17 hoặc UC18 để chọn suất chiếu.  
**Tóm tắt:** Admin đổi phim, phòng, ngày hoặc giờ của một suất chiếu chưa có đơn.  
**Tiền điều kiện:** Admin đã đăng nhập. Suất chiếu tồn tại, chưa bắt đầu.

### Dòng sự kiện chính

1. Admin chọn một suất chiếu.
2. Admin chọn Sửa suất chiếu.
3. Hệ thống kiểm tra suất chiếu chưa có đơn nào (đơn Chờ thanh toán hoặc Đã thanh toán) và hiển thị thông tin hiện tại.
4. Admin sửa phim, phòng, ngày hoặc giờ bắt đầu.
5. Hệ thống tính lại giờ kết thúc và kiểm tra như bước 4 đến 6 của UC14.
6. Admin xác nhận.
7. Hệ thống cập nhật suất chiếu. Nếu đổi phòng, hệ thống tạo lại ShowtimeSeat theo sơ đồ phòng mới.
8. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Suất chiếu đã có đơn → hệ thống chặn sửa và gợi ý dùng Tạm ngưng (UC16) thay vì sửa.
- 3.2. Suất chiếu đã bắt đầu hoặc đã kết thúc → không cho sửa.
- 5.1. Kiểm tra thất bại (ngoài khoảng chiếu, trùng giờ, chưa có giá) → báo lỗi → quay lại bước 4.

### Hậu điều kiện

Thành công: suất chiếu được cập nhật. Thất bại hoặc hủy: dữ liệu giữ nguyên.

---

## UC16 — Thay đổi trạng thái suất chiếu

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC17 hoặc UC18 để chọn suất chiếu.  
**Tóm tắt:** Admin chuyển suất chiếu giữa Mở bán và Tạm ngưng.  
**Tiền điều kiện:** Admin đã đăng nhập. Suất chiếu tồn tại và chưa kết thúc.

### Dòng sự kiện chính

1. Admin chọn một suất chiếu.
2. Admin chọn Thay đổi trạng thái.
3. Hệ thống hiển thị trạng thái hiện tại.
4. Admin chọn trạng thái mới (Mở bán hoặc Tạm ngưng).
5. Admin xác nhận.
6. Hệ thống cập nhật trạng thái. Suất Tạm ngưng không hiển thị cho Customer và không nhận đơn mới. Đơn đã thanh toán và đơn đang chờ thanh toán trước đó không bị ảnh hưởng.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Suất chiếu Đã kết thúc → không cho đổi trạng thái.
- 4.1. Mở bán lại suất chiếu đã quá giờ bắt đầu → hệ thống chặn và thông báo.

### Hậu điều kiện

Thành công: trạng thái suất chiếu được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC17 — Xem danh sách suất chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem lịch chiếu theo ngày, tuần hoặc phòng chiếu.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Quản lý suất chiếu.
2. Hệ thống hiển thị danh sách suất chiếu của ngày hiện tại gồm mã suất, phim, phòng, ngày chiếu, giờ bắt đầu, giờ kết thúc, giá áp dụng, số ghế trống, số ghế đã đặt và trạng thái (Còn vé, Hết vé, Tạm ngưng, Đã kết thúc).
3. Admin có thể đổi chế độ xem theo ngày, theo tuần hoặc theo phòng chiếu.
4. Admin chọn một suất chiếu để xem chi tiết.
5. Từ chi tiết, Admin có thể chuyển sang Sửa suất chiếu (UC15) hoặc Thay đổi trạng thái (UC16).

### Dòng sự kiện phụ

- 2.1. Không có suất chiếu trong khoảng đã chọn → thông báo danh sách trống.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC18 — Tìm kiếm suất chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tìm suất chiếu theo phim, phòng, ngày và trạng thái.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin nhập điều kiện tìm kiếm: tên phim, phòng chiếu, ngày hoặc khoảng ngày, trạng thái.
2. Hệ thống tìm các suất chiếu khớp điều kiện.
3. Hệ thống hiển thị kết quả theo định dạng như UC17.
4. Admin chọn một suất chiếu để xem chi tiết, sửa hoặc đổi trạng thái.

### Dòng sự kiện phụ

- 2.1. Không tìm thấy suất chiếu khớp → thông báo Không tìm thấy suất chiếu phù hợp.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 7. Nhóm Phòng chiếu và sơ đồ chỗ ngồi (UC19–UC27)

Trạng thái phòng chiếu: Hoạt động, Bảo trì, Ẩn. Trạng thái ghế: Hoạt động, Bảo trì. Loại ghế lấy từ các loại vé đang áp dụng (UC31), ví dụ Thường, VIP, Đôi.

## UC19 — Thêm phòng chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin thêm một phòng chiếu mới. Sơ đồ chỗ ngồi được thiết lập riêng ở UC24.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin chọn Thêm phòng chiếu.
2. Admin nhập tên phòng và ghi chú.
3. Hệ thống kiểm tra tên phòng không để trống và không trùng phòng khác.
4. Admin xác nhận lưu.
5. Hệ thống sinh mã phòng, lưu phòng ở trạng thái Hoạt động, sức chứa bằng 0 cho đến khi thiết lập sơ đồ.
6. Hệ thống thông báo thành công và gợi ý thiết lập sơ đồ chỗ ngồi (UC24).

### Dòng sự kiện phụ

- 3.1. Tên phòng trống hoặc đã tồn tại → báo lỗi → quay lại bước 2.
- 4.1. Admin hủy → không lưu.

### Hậu điều kiện

Thành công: phòng mới được lưu. Phòng chưa có sơ đồ chưa thể dùng để tạo suất chiếu.

---

## UC20 — Sửa phòng chiếu

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC22 hoặc UC23 để chọn phòng.  
**Tóm tắt:** Admin chỉnh sửa tên và ghi chú của một phòng chiếu.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng chiếu tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu.
2. Admin chọn Sửa phòng chiếu.
3. Hệ thống hiển thị thông tin hiện tại.
4. Admin sửa tên phòng hoặc ghi chú (mã phòng và sơ đồ không sửa ở UC này).
5. Hệ thống kiểm tra tên phòng không trống và không trùng phòng khác.
6. Admin xác nhận.
7. Hệ thống cập nhật và thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Tên không hợp lệ hoặc trùng → báo lỗi → quay lại bước 4.
- 6.1. Admin hủy → giữ nguyên dữ liệu.

### Hậu điều kiện

Thành công: thông tin phòng được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC21 — Thay đổi trạng thái phòng chiếu

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC22 hoặc UC23 để chọn phòng.  
**Tóm tắt:** Admin chuyển phòng chiếu giữa Hoạt động, Bảo trì và Ẩn.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng chiếu tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu.
2. Admin chọn Thay đổi trạng thái.
3. Admin chọn trạng thái mới.
4. Hệ thống kiểm tra điều kiện chuyển trạng thái.
5. Admin xác nhận.
6. Hệ thống cập nhật trạng thái. Phòng Bảo trì hoặc Ẩn không được dùng để tạo suất chiếu mới.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Chuyển sang Bảo trì hoặc Ẩn khi phòng có suất chiếu chưa diễn ra đã có đơn → hệ thống chặn và thông báo.
- 4.2. Chuyển sang Bảo trì hoặc Ẩn khi phòng có suất chiếu chưa diễn ra nhưng chưa có đơn → các suất đó tự chuyển sang Tạm ngưng sau khi Admin xác nhận.

### Hậu điều kiện

Thành công: trạng thái phòng được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC22 — Xem danh sách phòng chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem toàn bộ phòng chiếu và thông tin sức chứa.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Quản lý phòng chiếu.
2. Hệ thống hiển thị danh sách phòng gồm mã phòng, tên phòng, số hàng và số cột ghế, sức chứa (chỉ tính ghế Hoạt động), số ghế theo từng loại và trạng thái.
3. Admin chọn một phòng để xem chi tiết hoặc chuyển sang xem sơ đồ chỗ ngồi (UC27).

### Dòng sự kiện phụ

- 2.1. Chưa có phòng chiếu → thông báo danh sách trống và gợi ý Thêm phòng chiếu (UC19).

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC23 — Tìm kiếm phòng chiếu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tìm phòng chiếu theo tên hoặc trạng thái.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin nhập tên phòng và chọn bộ lọc trạng thái nếu cần.
2. Hệ thống tìm các phòng khớp điều kiện.
3. Hệ thống hiển thị kết quả theo định dạng như UC22.
4. Admin chọn một phòng để xem chi tiết, sửa hoặc đổi trạng thái.

### Dòng sự kiện phụ

- 2.1. Không tìm thấy phòng khớp → thông báo Không tìm thấy phòng chiếu phù hợp.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC24 — Thiết lập sơ đồ chỗ ngồi

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tạo lưới ghế cho một phòng chiếu bằng cách nhập số hàng và số cột.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng chiếu tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu và chọn Thiết lập sơ đồ chỗ ngồi.
2. Admin nhập số hàng và số cột ghế.
3. Hệ thống kiểm tra số hàng và số cột là số nguyên dương trong giới hạn cho phép.
4. Hệ thống sinh lưới ghế xem trước. Mã ghế gồm chữ cái của hàng và số của cột (A1, A2, B1...). Mặc định mọi ghế thuộc loại Thường, trạng thái Hoạt động.
5. Admin xác nhận.
6. Hệ thống lưu sơ đồ và cập nhật sức chứa của phòng.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Số hàng hoặc số cột không hợp lệ → báo lỗi → quay lại bước 2.
- 4.1. Phòng đã có sơ đồ → hệ thống cảnh báo sơ đồ cũ và phân loại ghế cũ sẽ bị thay thế, Admin xác nhận mới tiếp tục.
- 4.2. Phòng có suất chiếu chưa diễn ra đã có đơn → hệ thống chặn thay đổi lưới ghế.

### Hậu điều kiện

Thành công: phòng có sơ đồ ghế mới, sức chứa được cập nhật. Thất bại hoặc hủy: sơ đồ giữ nguyên.

---

## UC25 — Phân loại ghế

**Actor chính:** Admin  
**Quan hệ:** Thực hiện sau UC24.  
**Tóm tắt:** Admin gán loại ghế (loại vé) cho một hoặc nhiều ghế trên sơ đồ.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng đã có sơ đồ chỗ ngồi. Có ít nhất một loại vé Đang áp dụng.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu và mở sơ đồ chỗ ngồi.
2. Hệ thống hiển thị sơ đồ ghế, mỗi loại ghế một màu.
3. Admin chọn một hoặc nhiều ghế trên sơ đồ.
4. Admin chọn loại ghế trong danh sách các loại vé Đang áp dụng.
5. Hệ thống kiểm tra việc gán hợp lệ. Với loại vé có 2 chỗ mỗi vé (ghế đôi), hai ghế được chọn phải liền kề và cùng hàng, hệ thống ghép thành một ghế đôi.
6. Admin xác nhận.
7. Hệ thống lưu loại ghế và thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Ghế đôi chọn không liền kề hoặc không cùng hàng → báo lỗi → quay lại bước 3.
- 5.2. Ghế đang có vé đã bán hoặc đang giữ cho suất chiếu chưa diễn ra → không cho đổi loại vì ảnh hưởng giá vé.

### Hậu điều kiện

Thành công: các ghế được gán loại mới. Thất bại hoặc hủy: giữ nguyên.

---

## UC26 — Thay đổi trạng thái ghế

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin đánh dấu ghế Bảo trì khi hỏng và đưa về Hoạt động khi đã sửa.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng đã có sơ đồ chỗ ngồi.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu và mở sơ đồ chỗ ngồi.
2. Admin chọn một hoặc nhiều ghế.
3. Admin chọn trạng thái mới: Hoạt động hoặc Bảo trì.
4. Hệ thống kiểm tra điều kiện.
5. Admin xác nhận.
6. Hệ thống cập nhật trạng thái ghế và tính lại sức chứa phòng. Ghế Bảo trì không được tạo trong các suất chiếu tạo sau đó.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Ghế đang có vé đã bán hoặc đang giữ cho suất chiếu chưa diễn ra → không cho chuyển sang Bảo trì.
- 6.1. Với suất chiếu đã tạo trước đó, ghế chuyển sang Bảo trì mà chưa bán hoặc giữ → ghế được đánh dấu không khả dụng trong suất đó.

### Hậu điều kiện

Thành công: trạng thái ghế và sức chứa được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC27 — Xem danh sách sơ đồ chỗ ngồi

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem sơ đồ ghế trực quan của một phòng chiếu.  
**Tiền điều kiện:** Admin đã đăng nhập. Phòng chiếu tồn tại.

### Dòng sự kiện chính

1. Admin chọn một phòng chiếu và mở sơ đồ chỗ ngồi.
2. Hệ thống hiển thị lưới ghế theo hàng và cột, màu theo loại ghế, ghế Bảo trì hiển thị màu xám.
3. Hệ thống hiển thị chú giải màu, số ghế theo từng loại và tổng sức chứa.
4. Admin có thể chuyển sang Phân loại ghế (UC25) hoặc Thay đổi trạng thái ghế (UC26) từ màn hình này.

### Dòng sự kiện phụ

- 1.1. Phòng chưa có sơ đồ → thông báo và gợi ý Thiết lập sơ đồ chỗ ngồi (UC24).

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 8. Nhóm Loại vé và giá vé (UC28–UC34)

Một quy tắc giá xác định giá một vé theo bốn điều kiện: loại vé, đối tượng, loại ngày và khung giờ.

- Loại ngày: Ngày lễ (thuộc danh sách ngày lễ ở UC50), Cuối tuần (thứ Bảy, Chủ nhật), Ngày thường. Thứ tự ưu tiên là Ngày lễ, rồi Cuối tuần, rồi Ngày thường.
- Khung giờ theo giờ bắt đầu suất chiếu: Sáng (trước 12:00), Chiều (12:00 đến trước 17:00), Tối (17:00 đến trước 22:00), Khuya (từ 22:00).
- Đối tượng: Người lớn, Trẻ em, Sinh viên.

## UC28 — Thiết lập giá vé

**Actor chính:** Admin  
**Quan hệ:** Giá vé được UC06 sử dụng khi tính tiền.  
**Tóm tắt:** Admin tạo một quy tắc giá mới cho một loại vé.  
**Tiền điều kiện:** Admin đã đăng nhập. Có ít nhất một loại vé Đang áp dụng.

### Dòng sự kiện chính

1. Admin chọn Thiết lập giá vé.
2. Admin chọn loại vé, đối tượng (trong các đối tượng đã gắn cho loại vé ở UC34), loại ngày, khung giờ.
3. Admin nhập giá, ngày bắt đầu hiệu lực và ngày kết thúc (có thể để trống nếu không giới hạn).
4. Hệ thống kiểm tra giá lớn hơn 0 và ngày kết thúc không trước ngày bắt đầu.
5. Hệ thống kiểm tra quy tắc không chồng lấn thời gian hiệu lực với quy tắc đang áp dụng có cùng loại vé, đối tượng, loại ngày và khung giờ.
6. Admin xác nhận.
7. Hệ thống lưu quy tắc ở trạng thái Đang áp dụng và ghi vào lịch sử thay đổi giá (UC30).
8. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Giá hoặc ngày không hợp lệ → báo lỗi → quay lại bước 3.
- 5.1. Chồng lấn với quy tắc đang áp dụng → báo xung đột, nêu quy tắc bị trùng → quay lại bước 3.
- 2.1. Loại vé Ngừng áp dụng hoặc đối tượng chưa gắn cho loại vé → không cho chọn.

### Hậu điều kiện

Thành công: quy tắc giá mới được lưu và áp dụng cho các đơn tạo từ thời điểm hiệu lực. Đơn đã tạo giữ nguyên giá.

---

## UC29 — Sửa giá vé

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin điều chỉnh giá, thời gian hiệu lực hoặc tạm ngưng áp dụng một quy tắc giá.  
**Tiền điều kiện:** Admin đã đăng nhập. Quy tắc giá tồn tại.

### Dòng sự kiện chính

1. Admin chọn một quy tắc giá trong danh sách.
2. Admin chọn Sửa giá vé.
3. Hệ thống hiển thị thông tin hiện tại của quy tắc.
4. Admin sửa giá, ngày bắt đầu, ngày kết thúc hoặc đổi trạng thái giữa Đang áp dụng và Ngừng áp dụng.
5. Hệ thống kiểm tra như bước 4 và 5 của UC28.
6. Admin xác nhận.
7. Hệ thống cập nhật quy tắc và ghi lịch sử gồm giá cũ, giá mới, người sửa, thời điểm sửa.
8. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Dữ liệu không hợp lệ hoặc gây chồng lấn → báo lỗi → quay lại bước 4.
- 6.1. Không có thay đổi nào so với dữ liệu cũ → thông báo không có thay đổi, không ghi lịch sử.
- Việc sửa chỉ áp dụng cho đơn tạo sau thời điểm sửa.

### Hậu điều kiện

Thành công: quy tắc được cập nhật và có bản ghi lịch sử. Thất bại hoặc hủy: giữ nguyên.

---

## UC30 — Tra cứu lịch sử thay đổi giá vé

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem lịch sử thiết lập và chỉnh sửa giá vé theo thời gian.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Lịch sử giá vé.
2. Admin chọn loại vé hoặc một quy tắc giá cụ thể và khoảng thời gian (không bắt buộc).
3. Hệ thống lấy các bản ghi lịch sử thỏa điều kiện.
4. Hệ thống hiển thị danh sách mới nhất trước gồm thời điểm, người thực hiện, thao tác (Thiết lập, Sửa, Ngừng áp dụng), giá cũ và giá mới.

### Dòng sự kiện phụ

- 3.1. Không có bản ghi trong điều kiện đã chọn → thông báo chưa có lịch sử.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC31 — Thêm loại vé

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin thêm một loại vé mới. Mỗi loại vé tương ứng một loại ghế.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin chọn Thêm loại vé.
2. Admin nhập tên loại vé (ví dụ Thường, VIP, Đôi), mô tả và số chỗ ngồi mỗi vé (1 hoặc 2).
3. Hệ thống kiểm tra tên không trống, không trùng và số chỗ hợp lệ.
4. Admin xác nhận.
5. Hệ thống sinh mã loại vé, lưu ở trạng thái Đang áp dụng. Đối tượng áp dụng mặc định là Người lớn, có thể điều chỉnh ở UC34.
6. Hệ thống thông báo thành công. Loại vé mới xuất hiện trong danh sách loại ghế ở UC25.

### Dòng sự kiện phụ

- 3.1. Tên trống hoặc đã tồn tại, số chỗ không hợp lệ → báo lỗi → quay lại bước 2.
- 4.1. Admin hủy → không lưu.

### Hậu điều kiện

Thành công: loại vé mới được lưu. Thất bại hoặc hủy: không thêm.

---

## UC32 — Sửa loại vé

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin chỉnh sửa tên và mô tả của một loại vé.  
**Tiền điều kiện:** Admin đã đăng nhập. Loại vé tồn tại.

### Dòng sự kiện chính

1. Admin chọn một loại vé.
2. Admin chọn Sửa loại vé.
3. Hệ thống hiển thị thông tin hiện tại.
4. Admin sửa tên hoặc mô tả.
5. Hệ thống kiểm tra tên không trống và không trùng.
6. Admin xác nhận.
7. Hệ thống cập nhật và thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Admin đổi số chỗ ngồi mỗi vé khi đã có ghế được gán loại này → hệ thống chặn thay đổi trường đó, các trường khác vẫn sửa được.
- 5.1. Tên không hợp lệ hoặc trùng → báo lỗi → quay lại bước 4.

### Hậu điều kiện

Thành công: loại vé được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC33 — Thay đổi trạng thái loại vé

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin chuyển loại vé giữa Đang áp dụng và Ngừng áp dụng.  
**Tiền điều kiện:** Admin đã đăng nhập. Loại vé tồn tại.

### Dòng sự kiện chính

1. Admin chọn một loại vé.
2. Admin chọn Thay đổi trạng thái.
3. Hệ thống kiểm tra điều kiện chuyển trạng thái.
4. Admin xác nhận.
5. Hệ thống cập nhật trạng thái. Loại vé Ngừng áp dụng không hiện trong danh sách loại ghế (UC25) và các quy tắc giá thuộc loại vé đó chuyển sang Ngừng áp dụng.
6. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Còn ghế đang được gán loại vé này trong phòng Hoạt động → hệ thống chặn và yêu cầu phân loại lại các ghế đó (UC25).

### Hậu điều kiện

Thành công: trạng thái loại vé được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC34 — Thiết lập đối tượng áp dụng

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin chọn các đối tượng khách hàng được mua một loại vé.  
**Tiền điều kiện:** Admin đã đăng nhập. Loại vé tồn tại.

### Dòng sự kiện chính

1. Admin chọn một loại vé.
2. Admin chọn Thiết lập đối tượng áp dụng.
3. Hệ thống hiển thị các đối tượng (Người lớn, Trẻ em, Sinh viên) và đối tượng đang được chọn.
4. Admin chọn hoặc bỏ chọn các đối tượng. Người lớn luôn được chọn.
5. Admin xác nhận.
6. Hệ thống lưu thiết lập và thông báo thành công. Ở UC06, Customer chỉ chọn được các đối tượng đã thiết lập cho loại vé của ghế.

### Dòng sự kiện phụ

- 4.1. Bỏ một đối tượng đang có quy tắc giá hiệu lực → hệ thống cảnh báo các quy tắc đó sẽ chuyển Ngừng áp dụng, Admin xác nhận mới tiếp tục.
- Điều kiện chứng minh đối tượng (ví dụ thẻ sinh viên) nằm ngoài phạm vi.

### Hậu điều kiện

Thành công: đối tượng áp dụng của loại vé được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

# 9. Nhóm Quản lý thực phẩm (UC35–UC38)

Trạng thái món: Đang bán, Ngừng bán. Món có tồn kho bằng 0 hiển thị Hết hàng và không chọn được.

## UC35 — Thêm món

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin thêm một món thực phẩm (bắp, nước, combo) để bán kèm vé.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin chọn Thêm món.
2. Admin nhập tên món, loại (Bắp, Nước, Combo), thành phần hoặc mô tả, giá bán, hình ảnh và số lượng tồn ban đầu.
3. Hệ thống kiểm tra tên không trống, giá lớn hơn 0, tồn kho không âm, định dạng hình ảnh hợp lệ.
4. Hệ thống kiểm tra tên món không trùng món đã có.
5. Admin xác nhận.
6. Hệ thống sinh mã món, lưu món ở trạng thái Đang bán.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Dữ liệu không hợp lệ → báo lỗi tại trường tương ứng → quay lại bước 2.
- 4.1. Tên món đã tồn tại → cảnh báo → Admin sửa tên hoặc hủy.
- 5.1. Admin hủy → không lưu.

### Hậu điều kiện

Thành công: món mới được lưu. Thất bại hoặc hủy: không thêm.

---

## UC36 — Sửa món

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin chỉnh sửa thông tin của một món.  
**Tiền điều kiện:** Admin đã đăng nhập. Món tồn tại.

### Dòng sự kiện chính

1. Admin chọn một món trong danh sách.
2. Admin chọn Sửa món.
3. Hệ thống hiển thị thông tin hiện tại.
4. Admin sửa tên, loại, mô tả, giá bán hoặc hình ảnh (mã món không sửa, tồn kho chỉnh ở UC38).
5. Hệ thống kiểm tra dữ liệu như bước 3 và 4 của UC35.
6. Admin xác nhận.
7. Hệ thống cập nhật và thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Dữ liệu không hợp lệ hoặc tên trùng → báo lỗi → quay lại bước 4.
- Thay đổi giá chỉ áp dụng cho đơn tạo sau, đơn đã tạo giữ nguyên giá.

### Hậu điều kiện

Thành công: món được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC37 — Thay đổi trạng thái món

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin chuyển món giữa Đang bán và Ngừng bán.  
**Tiền điều kiện:** Admin đã đăng nhập. Món tồn tại.

### Dòng sự kiện chính

1. Admin chọn một món.
2. Admin chọn Thay đổi trạng thái.
3. Admin chọn trạng thái mới.
4. Admin xác nhận.
5. Hệ thống cập nhật trạng thái. Món Ngừng bán không hiển thị cho Customer ở bước chọn thực phẩm của UC06.
6. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Món đang nằm trong đơn Chờ thanh toán → đơn đó vẫn được hoàn tất, chỉ chặn đơn mới.

### Hậu điều kiện

Thành công: trạng thái món được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC38 — Theo dõi số lượng tồn kho

**Actor chính:** Admin  
**Quan hệ:** Tồn kho được trừ tự động ở UC07.  
**Tóm tắt:** Admin xem số lượng tồn của từng món, nhận cảnh báo sắp hết và cập nhật khi nhập thêm hàng.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Tồn kho thực phẩm.
2. Hệ thống hiển thị danh sách món gồm mã món, tên món, số lượng tồn và trạng thái. Món có tồn kho thấp hơn ngưỡng cảnh báo (thông số ở UC50) được đánh dấu.
3. Admin chọn một món cần cập nhật tồn kho.
4. Admin nhập số lượng nhập thêm hoặc điều chỉnh số lượng tồn và lý do.
5. Hệ thống kiểm tra số lượng hợp lệ.
6. Admin xác nhận.
7. Hệ thống cập nhật tồn kho, ghi lịch sử điều chỉnh (người thực hiện, thời điểm, số cũ, số mới) và thông báo thành công.

### Dòng sự kiện phụ

- 2.1. Chưa có món nào → thông báo danh sách trống.
- 5.1. Số lượng không phải số nguyên hoặc làm tồn kho âm → báo lỗi → quay lại bước 4.

### Hậu điều kiện

Thành công: tồn kho được cập nhật và có bản ghi lịch sử. Thất bại hoặc hủy: giữ nguyên.

---

# 10. Nhóm Quản lý đơn hàng (UC39–UC40)

Trạng thái đơn: Chờ thanh toán, Đã thanh toán, Đã hủy (Customer hủy trước khi thanh toán), Hết hạn (quá hạn giữ ghế). Employee chỉ được xem, không sửa đơn.

## UC39 — Xem danh sách toàn bộ đơn

**Actor chính:** Admin, Employee  
**Quan hệ:** —  
**Tóm tắt:** Admin và Employee xem tất cả đơn đặt vé để theo dõi và hỗ trợ khách.  
**Tiền điều kiện:** Người dùng đã đăng nhập với vai trò Admin hoặc Employee.

### Dòng sự kiện chính

1. Người dùng mở Quản lý đơn hàng.
2. Hệ thống hiển thị danh sách đơn có phân trang, mới nhất trước, gồm mã đơn, mã vé, khách hàng, phim, suất chiếu, số vé, tổng tiền, thời gian đặt và trạng thái.
3. Người dùng có thể lọc theo trạng thái, ngày đặt, phim, hoặc tìm theo mã đơn, mã vé, tên hoặc số điện thoại khách.
4. Hệ thống hiển thị kết quả phù hợp.
5. Người dùng chọn một đơn để xem chi tiết (ghế, thực phẩm, phương thức thanh toán).

### Dòng sự kiện phụ

- 2.1. Chưa có đơn nào → thông báo danh sách trống.
- 4.1. Không có đơn khớp điều kiện → thông báo Không tìm thấy đơn phù hợp.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC40 — Kiểm tra tình trạng thanh toán của khách

**Actor chính:** Admin, Employee  
**Quan hệ:** Thường bắt đầu từ UC39.  
**Tóm tắt:** Admin và Employee tra cứu một đơn để biết khách đã thanh toán hay chưa và qua phương thức nào.  
**Tiền điều kiện:** Người dùng đã đăng nhập với vai trò Admin hoặc Employee.

### Dòng sự kiện chính

1. Người dùng nhập mã đơn, mã vé hoặc số điện thoại khách.
2. Hệ thống tìm đơn phù hợp.
3. Hệ thống hiển thị tình trạng thanh toán: trạng thái đơn, phương thức thanh toán, số tiền, mã giao dịch, thời gian thanh toán. Với đơn Chờ thanh toán, hệ thống hiển thị thêm thời gian giữ ghế còn lại.
4. Người dùng dùng thông tin này để hỗ trợ khách.

### Dòng sự kiện phụ

- 2.1. Không tìm thấy đơn → thông báo Không tìm thấy đơn phù hợp.
- 2.2. Tìm theo số điện thoại có nhiều đơn → hệ thống hiển thị danh sách để người dùng chọn một đơn.
- 2.3. Nhập sai mã vé nhiều lần liên tiếp → hệ thống tạm khóa tra cứu theo mã vé trong thời gian ngắn.
- Xác nhận hoặc hủy đơn thủ công nằm ngoài phạm vi.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 11. Nhóm Quản lý tài khoản (UC41–UC47)

Admin chỉ tạo tài khoản Employee và Admin. Customer tự đăng ký ở UC01 và được Admin quản lý ở các UC còn lại. Không xóa tài khoản, chỉ khóa. Chức vụ gồm Admin và Nhân viên. Trạng thái tài khoản: Hoạt động, Bị khóa.

## UC41 — Thêm tài khoản

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tạo tài khoản cho nhân viên hoặc quản trị viên mới.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin chọn Thêm tài khoản.
2. Admin nhập họ tên, chức vụ (Admin hoặc Nhân viên), ngày sinh, giới tính, số điện thoại, email, địa chỉ và ngày tuyển dụng.
3. Hệ thống kiểm tra các trường bắt buộc và định dạng dữ liệu.
4. Hệ thống kiểm tra email và số điện thoại chưa được dùng cho tài khoản khác.
5. Admin xác nhận.
6. Hệ thống sinh mã nhân viên, tạo mật khẩu khởi tạo ngẫu nhiên, mã hóa và lưu tài khoản ở trạng thái Hoạt động.
7. Hệ thống hiển thị mật khẩu khởi tạo một lần duy nhất để Admin chuyển cho người dùng, rồi thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Dữ liệu không hợp lệ → báo lỗi tại trường tương ứng → quay lại bước 2.
- 4.1. Email hoặc số điện thoại đã tồn tại → báo lỗi → quay lại bước 2.
- 5.1. Admin hủy → không lưu.

### Hậu điều kiện

Thành công: tài khoản mới được tạo. Thất bại hoặc hủy: không tạo.

---

## UC42 — Khóa/Mở tài khoản

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC44 hoặc UC47 để chọn tài khoản.  
**Tóm tắt:** Admin khóa tài khoản vi phạm hoặc mở lại tài khoản đã khóa (áp dụng cho cả Employee và Customer).  
**Tiền điều kiện:** Admin đã đăng nhập. Tài khoản tồn tại.

### Dòng sự kiện chính

1. Admin chọn một tài khoản.
2. Admin chọn Khóa tài khoản hoặc Mở tài khoản tương ứng trạng thái hiện tại.
3. Hệ thống kiểm tra điều kiện.
4. Admin xác nhận.
5. Hệ thống cập nhật trạng thái tài khoản. Tài khoản bị khóa không đăng nhập được (UC02) và các phiên đang mở bị hủy.
6. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 3.1. Admin khóa chính tài khoản của mình → hệ thống chặn.
- 3.2. Khóa tài khoản Admin Hoạt động cuối cùng → hệ thống chặn.
- Customer bị khóa không đặt vé mới được, đơn đã thanh toán của Customer giữ nguyên.

### Hậu điều kiện

Thành công: trạng thái tài khoản được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC43 — Chỉnh sửa thông tin tài khoản

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC44 hoặc UC47 để chọn tài khoản.  
**Tóm tắt:** Admin sửa thông tin cá nhân của Employee hoặc Customer.  
**Tiền điều kiện:** Admin đã đăng nhập. Tài khoản tồn tại.

### Dòng sự kiện chính

1. Admin chọn một tài khoản.
2. Admin chọn Chỉnh sửa thông tin.
3. Hệ thống hiển thị thông tin hiện tại.
4. Admin sửa họ tên, ngày sinh, giới tính, số điện thoại, địa chỉ (với Employee có thêm ngày tuyển dụng). Mã tài khoản, email và chức vụ không sửa ở UC này.
5. Hệ thống kiểm tra định dạng và số điện thoại chưa thuộc tài khoản khác.
6. Admin xác nhận.
7. Hệ thống cập nhật và thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Dữ liệu không hợp lệ hoặc số điện thoại đã được dùng → báo lỗi → quay lại bước 4.
- 6.1. Admin hủy → giữ nguyên.

### Hậu điều kiện

Thành công: thông tin tài khoản được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC44 — Xem chi tiết tài khoản

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC47.  
**Tóm tắt:** Admin xem thông tin đầy đủ của một tài khoản.  
**Tiền điều kiện:** Admin đã đăng nhập. Tài khoản tồn tại.

### Dòng sự kiện chính

1. Admin chọn một tài khoản từ danh sách hoặc kết quả tìm kiếm.
2. Hệ thống hiển thị thông tin chi tiết: mã tài khoản, họ tên, loại tài khoản, chức vụ (với Employee), ngày sinh, giới tính, số điện thoại, email, địa chỉ, ngày tạo, lần đăng nhập gần nhất và trạng thái. Mật khẩu không được hiển thị.
3. Với tài khoản Customer, hệ thống hiển thị thêm danh sách đơn đã đặt.
4. Từ màn hình chi tiết, Admin có thể chuyển sang Khóa/Mở (UC42), Chỉnh sửa thông tin (UC43), Khởi tạo mật khẩu (UC45) hoặc Chỉnh sửa chức vụ (UC46, chỉ với Employee).

### Dòng sự kiện phụ

- 2.1. Không tải được dữ liệu → thông báo lỗi và cho phép thử lại.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

## UC45 — Khởi tạo mật khẩu

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC44 hoặc UC47 để chọn tài khoản.  
**Tóm tắt:** Admin đặt lại mật khẩu của một tài khoản về mật khẩu tạm khi người dùng quên mật khẩu.  
**Tiền điều kiện:** Admin đã đăng nhập. Tài khoản tồn tại.

### Dòng sự kiện chính

1. Admin chọn một tài khoản.
2. Admin chọn Khởi tạo mật khẩu.
3. Hệ thống yêu cầu xác nhận.
4. Admin xác nhận.
5. Hệ thống tạo mật khẩu tạm ngẫu nhiên, mã hóa và lưu, mật khẩu cũ mất hiệu lực.
6. Hệ thống hiển thị mật khẩu tạm một lần duy nhất để Admin chuyển cho người dùng, rồi thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Admin hủy → giữ nguyên mật khẩu cũ.
- Customer có thể tự đổi mật khẩu tạm ở UC04.

### Hậu điều kiện

Thành công: mật khẩu mới có hiệu lực. Thất bại hoặc hủy: mật khẩu cũ giữ nguyên.

---

## UC46 — Chỉnh sửa chức vụ

**Actor chính:** Admin  
**Quan hệ:** Thường bắt đầu từ UC44 hoặc UC47 để chọn tài khoản.  
**Tóm tắt:** Admin đổi chức vụ (Admin hoặc Nhân viên) của một tài khoản nội bộ, từ đó thay đổi quyền truy cập.  
**Tiền điều kiện:** Admin đã đăng nhập. Tài khoản Employee hoặc Admin tồn tại.

### Dòng sự kiện chính

1. Admin chọn một tài khoản nội bộ.
2. Admin chọn Chỉnh sửa chức vụ.
3. Hệ thống hiển thị chức vụ hiện tại.
4. Admin chọn chức vụ mới.
5. Hệ thống kiểm tra điều kiện.
6. Admin xác nhận.
7. Hệ thống cập nhật chức vụ. Quyền mới có hiệu lực từ lần đăng nhập kế tiếp.
8. Hệ thống thông báo thành công.

### Dòng sự kiện phụ

- 5.1. Admin tự đổi chức vụ của chính mình → hệ thống chặn.
- 5.2. Hạ chức vụ Admin Hoạt động cuối cùng → hệ thống chặn.
- 1.1. Tài khoản Customer không có chức vụ → không áp dụng UC này.

### Hậu điều kiện

Thành công: chức vụ được cập nhật. Thất bại hoặc hủy: giữ nguyên.

---

## UC47 — Tìm kiếm tài khoản

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tìm và xem danh sách tài khoản theo từ khóa và bộ lọc. Khi không nhập điều kiện, hệ thống hiển thị toàn bộ tài khoản.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Quản lý tài khoản.
2. Admin nhập từ khóa (tên, mã tài khoản, email hoặc số điện thoại) và có thể lọc theo loại tài khoản (Admin, Nhân viên, Khách hàng) và trạng thái.
3. Hệ thống tìm các tài khoản khớp điều kiện.
4. Hệ thống hiển thị danh sách có phân trang gồm mã, họ tên, loại tài khoản, email, số điện thoại và trạng thái.
5. Admin chọn một tài khoản để xem chi tiết (UC44) hoặc thao tác tiếp.

### Dòng sự kiện phụ

- 3.1. Không tìm thấy tài khoản khớp → thông báo Không tìm thấy tài khoản phù hợp.

### Hậu điều kiện

Không thay đổi dữ liệu.

---

# 12. Nhóm Báo cáo và thống kê (UC48–UC49)

Báo cáo chỉ tính các đơn ở trạng thái Đã thanh toán.

## UC48 — Báo cáo doanh thu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem doanh thu vé và thực phẩm theo ngày, tháng, năm hoặc khoảng thời gian tùy chọn.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Báo cáo doanh thu.
2. Admin chọn kỳ báo cáo (ngày, tháng, năm hoặc khoảng ngày) và có thể lọc theo phim.
3. Hệ thống tổng hợp dữ liệu từ các đơn Đã thanh toán trong phạm vi đã chọn.
4. Hệ thống hiển thị doanh thu vé, doanh thu thực phẩm, tổng doanh thu, số đơn, số vé bán ra và biểu đồ doanh thu theo thời gian.
5. Admin có thể xuất báo cáo ra file Excel hoặc PDF.

### Dòng sự kiện phụ

- 2.1. Ngày bắt đầu sau ngày kết thúc → báo lỗi → quay lại bước 2.
- 3.1. Không có dữ liệu trong kỳ đã chọn → hiển thị báo cáo trống, không báo lỗi.
- 5.1. Xuất file thất bại → thông báo lỗi và cho phép thử lại.

### Hậu điều kiện

Không thay đổi dữ liệu nghiệp vụ.

---

## UC49 — Báo cáo mức độ quan tâm theo phim

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin xem mỗi phim được khách quan tâm đến mức nào, làm căn cứ tăng hoặc giảm số suất chiếu.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Báo cáo mức độ quan tâm.
2. Admin chọn kỳ báo cáo.
3. Hệ thống tổng hợp theo từng phim: số suất chiếu, số vé bán ra, số đơn và tỷ lệ lấp đầy ghế (số ghế đã bán chia tổng số ghế của các suất chiếu trong kỳ).
4. Hệ thống hiển thị bảng và biểu đồ xếp hạng phim theo số vé bán ra giảm dần.
5. Admin có thể xuất báo cáo ra file Excel hoặc PDF.

### Dòng sự kiện phụ

- 2.1. Khoảng thời gian không hợp lệ → báo lỗi → quay lại bước 2.
- 3.1. Không có dữ liệu trong kỳ → hiển thị báo cáo trống.
- 5.1. Xuất file thất bại → thông báo lỗi và cho phép thử lại.

### Hậu điều kiện

Không thay đổi dữ liệu nghiệp vụ.

---

# 13. Nhóm Cài đặt hệ thống (UC50–UC51)

## UC50 — Chỉnh sửa thông số rạp phim

**Actor chính:** Admin  
**Quan hệ:** Các thông số này được dùng ở UC06 (thời gian giữ ghế, số vé tối đa), UC14 (thời gian dọn phòng), UC28 (danh sách ngày lễ) và UC38 (ngưỡng cảnh báo tồn kho).  
**Tóm tắt:** Admin chỉnh thông tin rạp và các thông số vận hành chung của hệ thống.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Cài đặt hệ thống.
2. Hệ thống hiển thị các thông số hiện tại gồm: thông tin rạp (tên, địa chỉ, hotline, email); thời gian giữ ghế (mặc định 5 phút); số vé tối đa mỗi đơn (mặc định 8); thời gian dọn phòng giữa hai suất chiếu (phút); ngưỡng cảnh báo tồn kho; danh sách ngày lễ.
3. Admin sửa các thông số cần thay đổi.
4. Hệ thống kiểm tra giá trị: các thông số thời gian và số lượng là số nguyên dương, email và hotline đúng định dạng, ngày lễ không trùng nhau.
5. Admin xác nhận.
6. Hệ thống lưu thông số và thông báo thành công.

### Dòng sự kiện phụ

- 4.1. Giá trị không hợp lệ → báo lỗi tại thông số tương ứng, giữ giá trị cũ → quay lại bước 3.
- 5.1. Admin hủy → giữ nguyên thông số.
- Thay đổi thời gian giữ ghế chỉ áp dụng cho đơn tạo sau thời điểm lưu.

### Hậu điều kiện

Thành công: thông số mới có hiệu lực. Thất bại hoặc hủy: giữ nguyên.

---

## UC51 — Sao lưu dữ liệu

**Actor chính:** Admin  
**Quan hệ:** —  
**Tóm tắt:** Admin tạo bản sao lưu toàn bộ dữ liệu hệ thống để phòng sự cố. Khôi phục dữ liệu nằm ngoài phạm vi.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính

1. Admin mở Sao lưu dữ liệu.
2. Hệ thống hiển thị danh sách các lần sao lưu gần đây gồm thời điểm, người thực hiện, tên tệp và dung lượng.
3. Admin chọn Sao lưu ngay.
4. Hệ thống tạo tệp sao lưu toàn bộ cơ sở dữ liệu và lưu vào thư mục sao lưu đã cấu hình.
5. Hệ thống ghi nhật ký lần sao lưu.
6. Hệ thống thông báo thành công kèm tên tệp.

### Dòng sự kiện phụ

- 2.1. Chưa có lần sao lưu nào → thông báo danh sách trống.
- 3.1. Đang có một lần sao lưu khác chạy → hệ thống chặn và thông báo.
- 4.1. Không ghi được tệp (thư mục không tồn tại, hết dung lượng) → báo lỗi, không ghi nhật ký thành công.

### Hậu điều kiện

- **Thành công:** có tệp sao lưu mới và một bản ghi nhật ký.
- **Thất bại:** không có tệp hợp lệ.

---

# 14. Nhóm Tác vụ nền (UC52)

## UC52 — Giải phóng Hold hết hạn

**Actor chính:** SystemScheduler  
**Quan hệ:** Hỗ trợ UC06 và UC07 khi hết hạn giữ ghế.  
**Tóm tắt:** Tác vụ nền định kỳ trả các ghế đang giữ quá hạn về trạng thái trống.  
**Tiền điều kiện:** Scheduler đang hoạt động.

### Dòng sự kiện chính

1. Scheduler chạy theo chu kỳ (ví dụ mỗi 30 giây).
2. Hệ thống tìm các ShowtimeSeat có trạng thái `HOLD` và hạn giữ nhỏ hơn thời điểm hiện tại.
3. Hệ thống chuyển các ghế tìm được từ `HOLD` sang `AVAILABLE`.
4. Hệ thống chuyển các đơn Chờ thanh toán tương ứng sang Hết hạn.
5. Hệ thống ghi nhận số ghế đã giải phóng và kết thúc chu kỳ.

### Dòng sự kiện phụ

- 2.1. Không có ghế giữ quá hạn → không cập nhật gì, kết thúc chu kỳ.
- Tác vụ không thay đổi ghế `SOLD` hoặc `AVAILABLE`.

### Hậu điều kiện

Các ghế quá hạn trở về `AVAILABLE` và có thể được giữ lại.

---
# 15. Quan hệ include và cách nối tác nhân

| UC chính | Quan hệ | UC liên quan |
| --- | --- | --- |
| UC06 Đặt vé online | include | UC05 Xem phim và suất chiếu |
| UC06 Đặt vé online | include | UC07 Thanh toán online |

- UC02 Đăng nhập dùng chung: Customer, Employee, Admin nối trực tiếp vào UC02, đây là quan hệ tác nhân với UC chứ không phải include.
- UC39 và UC40: Admin và Employee cùng nối trực tiếp.
- Chọn thực phẩm đi kèm là hành vi tùy chọn trong UC06. Không tạo UC riêng cho chọn ghế, giữ ghế, tính tiền, sinh mã vé.
- Trên sơ đồ UC, các nhóm thêm, sửa, đổi trạng thái có thể vẽ generalization về UC cha theo BFD (ví dụ Cập nhật phim). UC cha chỉ là tiêu đề nhóm, không có đặc tả riêng.

---

# 16. Quy tắc nghiệp vụ cốt lõi

## Đặt vé và ghế

- Một đơn thuộc đúng một suất chiếu và có tối đa 8 vé.
- Trạng thái ghế theo suất chiếu: `AVAILABLE → HOLD → SOLD`. `HOLD` quay về `AVAILABLE` khi hết hạn (UC52) hoặc Customer hủy đơn.
- Giữ ghế bằng một thao tác cập nhật nguyên tử, không để hai yêu cầu cùng giữ một ghế. Thời gian giữ ghế mặc định 5 phút.
- Trạng thái đơn: Chờ thanh toán chuyển thành Đã thanh toán, Đã hủy (Customer hủy trước khi thanh toán) hoặc Hết hạn (quá hạn giữ ghế).

## Mã vé

- Mỗi đơn Đã thanh toán có một mã vé gồm 8 ký tự ngẫu nhiên, chọn từ bảng ký tự đã loại các ký tự dễ nhầm (0, O, 1, I) và duy nhất trong hệ thống. Nếu trùng thì sinh lại.
- Mã vé chỉ sinh sau khi thanh toán thành công, không sinh lúc giữ ghế.
- Tra cứu bằng mã vé (UC40) bị tạm khóa khi nhập sai nhiều lần liên tiếp để tránh dò mã.
- Trạng thái vé: Hợp lệ khi đơn Đã thanh toán và suất chiếu chưa kết thúc, Đã hết hạn sau khi suất chiếu kết thúc.

## Thanh toán và giá vé

- Phương thức thanh toán: MoMo, VNPay, Thẻ tín dụng. Không có tiền mặt.
- Giá một vé lấy từ quy tắc giá khớp loại vé, đối tượng, loại ngày, khung giờ và còn hiệu lực (UC28). Giá và giá món được chốt tại thời điểm tạo đơn.
- Tồn kho thực phẩm được trừ khi đơn thanh toán thành công.

## Dữ liệu

- Không xóa cứng. Dữ liệu được ẩn, ngừng hoặc khóa bằng thay đổi trạng thái.
- Thể loại phim là danh sách cố định nạp sẵn trong cơ sở dữ liệu. Admin chọn thể loại khi thêm hoặc sửa phim, không có UC quản lý thể loại.
- Bán vé tại quầy, soát vé, in vé, hủy vé sau thanh toán và hoàn tiền nằm ngoài phạm vi.

## Phân quyền

| Tác nhân | Use Case được dùng |
| --- | --- |
| Customer | UC01 đến UC08 (UC02 dùng chung) |
| Employee | UC02, UC39, UC40 |
| Admin | UC02, UC09 đến UC51 |
| SystemScheduler | UC52 |
