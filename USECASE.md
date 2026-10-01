# ĐẶC TẢ USE CASE — HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM

> **Baseline đã chốt**
>
> - 01 System chính: **Hệ thống Quản lý Rạp chiếu phim**
> - 08 Phân hệ theo domain nghiệp vụ.
> - 24 Use Case chính thức.
> - Kiến trúc Domain-oriented.
> - `Đặt vé` là Aggregate Root của giao dịch đặt vé.
> - `ShowtimeSeat` quản lý trạng thái ghế theo từng suất chiếu: `AVAILABLE → HOLD → SOLD`.
> - Hold mặc định 5 phút; `SystemScheduler` giải phóng Hold hết hạn.
>
> **Template UC Specific:** ID · Actor chính/liên quan · Quan hệ · Tóm tắt · Tiền điều kiện · Dòng sự kiện chính · Dòng sự kiện phụ · Hậu điều kiện.

---

# 1. SYSTEM VÀ Phân hệ

## 1.1. System chính

### Hệ thống Quản lý Rạp chiếu phim

System cung cấp các nghiệp vụ chính:

- Quản lý phim, rạp, phòng, ghế và suất chiếu.
- Cho phép Customer xem phim và suất chiếu và đặt vé trực tuyến.
- Nhân viên bán vé và thanh toán tại quầy, soát vé, giao bắp nước.
- Quản lý tài khoản Nhân viên và Khách hàng.
- Bán vé và thanh toán tại quầy.
- Tự động giải phóng ghế Hold hết hạn.

## 1.2. Actor

| Actor | Vai trò |
|---|---|
| Customer | Đăng ký, đăng nhập, tìm phim, suất chiếu, đặt vé, thanh toán trực tuyến, xem lịch sử đặt vé |
| Employee | Bán vé, thanh toán tại quầy, soát vé, giao bắp nước, tra cứu và hủy vé |
| Admin | Đăng nhập, quản lý dữ liệu vận hành, quản lý tài khoản Employee và Customer |
| SystemScheduler | Thực hiện tác vụ nền giải phóng Hold hết hạn |

## 1.3. Phân hệ

| # | Phân hệ | UC | Actor |
|---|---|---|---|
| 1 | Đặt vé & Thanh toán | CUS-04, CUS-05, CUS-06, CUS-07, CUS-08, EMP-01, EMP-02, EMP-06 | Customer, Employee |
| 2 | Vận hành tại rạp | EMP-03, EMP-04, EMP-05 | Employee |
| 3 | Tài khoản khách hàng | AUTH-01, CUS-01, CUS-02 | Customer |
| 4 | Danh mục & Lịch chiếu | CUS-04, ADM-01 -> ADM-05 | Customer, Admin |
| 5 | Dịch vụ đi kèm | ADM-06 | Admin |
| 6 | Quản trị tài khoản | ADM-07, ADM-08 | Admin |
| 7 | Báo cáo và thống kê | ADM-09 | Admin
| 8 | Tác vụ nền | SYS-01 | SystemScheduler |

### Nguyên tắc chia Phân hệ

- Chia theo **domain nghiệp vụ**, không chia theo Actor.
- Một Actor có thể tương tác với nhiều Phân hệ.
- Phân hệ là nhóm/boundary tổ chức UC, **không phải Use Case**.
- Actor kết nối trực tiếp với UC; `include/extend` chỉ dùng giữa các UC.
- `CUS-03` và `CUS-04` là các UC xem dữ liệu; không biến các bước UI như "Chọn ghế", "Hold ghế", "Tính tiền", "Sinh QR" thành UC độc lập.
- `EMP-03 Soát vé` và `EMP-04 Giao Bắp Nước` độc lập dù cùng có thể tra cứu Đặt vé/QR.
- `EMP-05 Tra cứu Đặt vé` là UC dùng chung cho các nghiệp vụ vận hành cần tìm.
- `AUTH-01 Đăng nhập` đặt ở Phân hệ 3 vì gần với domain tài khoản nhất, dù cả 3 actor đều nối trực tiếp vào UC này (không phải qua include).

---

# 2. DANH SÁCH 23 USE CASE

## Dùng chung

| ID | Use Case |
|---|---|
| AUTH-01 | Đăng nhập |

## Customer

| ID | Use Case |
|---|---|
| CUS-01 | Đăng ký tài khoản |
| CUS-02 | Quản lý thông tin cá nhân |
| CUS-03 | Xem phim |
| CUS-04 | Xem suất chiếu |
| CUS-05 | Đặt vé online |
| CUS-06 | Thanh toán online |
| CUS-07 | Xem lịch sử đặt vé |


## Employee

| ID | Use Case |
|---|---|
| EMP-01 | Bán vé tại quầy |
| EMP-02 | Thanh toán tại quầy |
| EMP-03 | Soát vé |
| EMP-04 | Giao dịch vụ đi kèm |
| EMP-05 | Tra cứu đặt vé |
| EMP-06 | Hủy vé |

## Admin

| ID | Use Case |
|---|---|
| ADM-01 | Quản lý Danh mục phim |
| ADM-02 | Quản lý Phòng chiếu |
| ADM-03 | Quản lý Sơ đồ chỗ ngồi |
| ADM-04 | Quản lý Suất chiếu |
| ADM-05 | Quản lý Giá vé |
| ADM-06 | Quản lý Dịch vụ đi kèm |
| ADM-07 | Quản lý Nhân viên |
| ADM-08 | Quản lý Khách hàng |
| ADM-09 | Quản lý Báo cáo và Thống kê |

## System

| ID | Use Case |
|---|---|
| SYS-01 | Giải phóng Hold hết hạn |

---

# 3. ĐẶC TẢ USE CASE

## A. DÙNG CHUNG

## AUTH-01 - Đăng nhập
**Actor chính:** Customer, Employee, Admin (dùng chung một UC, không tách theo actor) 
**Quan hệ: — Tóm tắt:** Cho phép người dùng đăng nhập bằng tài khoản đã có, hệ thống phân quyền tương ứng theo vai trò sau khi xác thực thành công. 
**Tiền điều kiện:** Người dùng đã có tài khoản (Customer tự đăng ký qua CUS-01; Employee/Admin được Admin tạo qua ADM-08/ADM-09).

### Dòng sự kiện chính
1. Người dùng nhập tài khoản (email/số điện thoại với Customer, mã nhân viên với Employee/Admin) và mật khẩu.
2. Hệ thống xác thực thông tin.
3. Hệ thống kiểm tra trạng thái tài khoản.
4. Hệ thống tạo phiên đăng nhập và gán quyền theo vai trò (Customer/Employee/Admin).
5. Hệ thống chuyển người dùng vào giao diện tương ứng với vai trò.
   
### Dòng sự kiện phụ
- 2.1. Sai tài khoản/mật khẩu → thông báo lỗi → quay lại bước 1.
- 3.1. Tài khoản bị khóa/vô hiệu hóa → thông báo tài khoản bị khóa → kết thúc.
 
### Hậu điều kiện:
- Người dùng có phiên đăng nhập hợp lệ với quyền tương ứng vai trò.
  
**Ghi chú:** Đăng nhập chỉ có một luồng nghiệp vụ duy nhất cho cả 3 actor (nhập tài khoản/mật khẩu, xác thực, tạo phiên), khác nhau duy nhất ở bước gán quyền — nên không tách thành 3 UC riêng theo actor.

## A. CUSTOMER

## CUS-01 — Đăng ký tài khoản

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Cho phép người dùng mới tạo tài khoản Customer.  
**Tiền điều kiện:** Người dùng chưa có tài khoản.

### Dòng sự kiện chính
1. Customer chọn "Đăng ký".
2. Customer nhập email/số điện thoại, mật khẩu và họ tên.
3. Hệ thống kiểm định dạng và tính duy nhất của email/số điện thoại.
4. Hệ thống tạo tài khoản Customer.
5. Hệ thống thông báo đăng ký thành công.

### Dòng sự kiện phụ
- 3.1. Email/số điện thoại đã tồn tại → thông báo tài khoản đã tồn tại → quay lại bước 2.
- 3.2. Dữ liệu không hợp lệ → thông báo lỗi field tương ứng → quay lại bước 2.

### Hậu điều kiện
- **Thành công:** Customer mới được tạo.
- **Thất bại:** Không tạo tài khoản.

---

## CUS-02 — Quản lý Thông tin cá nhân

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer xem và cập nhật thông tin cá nhân.  
**Tiền điều kiện:** Customer đã đăng nhập.

### Dòng sự kiện chính
1. Customer vào "Tài khoản của tôi".
2. Hệ thống hiển thị thông tin hiện tại.
3. Customer chỉnh sửa thông tin.
4. Customer chọn "Lưu".
5. Hệ thống validate dữ liệu.
6. Hệ thống cập nhật thông tin.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ
- 5.1. Dữ liệu không hợp lệ → thông báo lỗi → quay lại bước 3.

### Hậu điều kiện
Thông tin Profile được cập nhật.

---

## CUS-03 — Xem phim

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer xem danh sách và chi tiết phim.  
**Tiền điều kiện:** Không bắt buộc đăng nhập.

### Dòng sự kiện chính
1. Customer mở trang Phim.
2. Hệ thống hiển thị danh sách phim đang/sắp chiếu.
3. Customer chọn một phim.
4. Hệ thống hiển thị chi tiết phim.

### Dòng sự kiện phụ
- 2.1. Không có phim phù hợp → thông báo.

### Hậu điều kiện
Không thay đổi dữ liệu hệ thống.

---

## CUS-04 — Xem suất chiếu

**Actor chính:** Customer  
**Quan hệ:** `<<include>>` bởi CUS-06 và EMP-01. CUS-05 có actor chính là Customer; Employee tái sử dụng qua quan hệ include vì cùng logic nghiệp vụ xem suất chiếu, không phát sinh actor chính mới.

**Tóm tắt:** Customer xem Showtime theo phim/rạp/ngày và chọn một Showtime.  
**Tiền điều kiện:** Có Showtime phù hợp với điều kiện tìm kiếm.

### Dòng sự kiện chính
1. Customer chọn phim hoặc chọn rạp/ngày trực tiếp.
2. Hệ thống tìm và hiển thị Showtime khả dụng.
3. Mỗi Showtime hiển thị rạp, phòng, thời gian và giá.
4. Customer chọn một Showtime.
5. Hệ thống chuyển sang nghiệp vụ tiếp theo.

### Dòng sự kiện phụ
- 2.1. Không có Showtime phù hợp → thông báo "Không có suất chiếu".

### Hậu điều kiện
Customer chọn được Showtime.

---

## CUS-05 — Đặt vé online

**Actor chính:** Customer  
**Actor liên quan:** SystemScheduler (gián tiếp qua timeout)  
**Quan hệ:** `<<include>>` CUS-05, CUS-07; hành vi chọn thêm Bắp Nước là phần mở rộng tùy chọn.  
**Tóm tắt:** Customer chọn Showtime, giữ ghế, thanh toán và nhận vé điện tử.  
**Tiền điều kiện:** Customer đã đăng nhập; Showtime đang mở bán.

### Dòng sự kiện chính
1. Customer chọn Showtime.
2. Hệ thống hiển thị sơ đồ `ShowtimeSeat`.
3. Customer chọn tối đa 8 ghế `AVAILABLE`.
4. Hệ thống thực hiện atomic update để chuyển các ghế sang `HOLD`.
5. Hệ thống đặt `holdExpiresAt = now + 5 phút`.
6. Customer có thể thêm Bắp và Nước.
7. Hệ thống tính tổng tiền từ Đặt vé.
8. Customer thực hiện `CUS-06`.
9. Thanh toán thành công → Đặt vé `PAID`.
10. Các ShowtimeSeat chuyển `HOLD` → `SOLD`.
11. Hệ thống tạo vé tương ứng với các ghế đã bán, vé ban đầu `VALID`.
12. Hệ thống sinh Đặt vé.maVe (mã vé) duy nhất.
13. Hệ thống hiển thị vé điện tử kèm mã vé.

### Dòng sự kiện phụ
- 4.1. Một hoặc nhiều ghế đã bị Hold/Sold bởi request khác → thông báo ghế conflict → rollback toàn bộ ghế đã Hold thành công trong request → quay lại bước 3.
- 8.1. Thanh toán thất bại → thông báo lỗi; các ghế tiếp tục `HOLD` nếu chưa hết hạn.
- 8.2. Hold hết hạn trước khi thanh toán hoàn tất → SYS-01 release về `AVAILABLE`; Customer phải thực hiện lại việc giữ ghế.

### Hậu điều kiện
- **Thành công:** Đặt vé = `PAID`, Vé = `VALID`, `ShowtimeSeat = SOLD`, QR được sinh.
- **Thất bại:** Đặt vé chưa hoàn tất; Hold được giải phóng khi timeout/cancel theo nghiệp vụ.

---

## CUS-06 — Thanh toán online

**Actor chính:** Customer  
**Quan hệ:** `<<include>>` bởi CUS-06.  
**Phương thức:** MoMo, VNPay, Credit Card. Cash không áp dụng online.  
**Tóm tắt:** Customer thanh toán Đặt vé qua phương thức online.  
**Tiền điều kiện:** Đặt vé có ShowtimeSeat đang `HOLD` và còn trong thời gian giữ.

### Dòng sự kiện chính
1. Hệ thống hiển thị tổng tiền Đặt vé.
2. Customer chọn phương thức thanh toán.
3. Hệ thống chọn PaymentStrategy tương ứng.
4. Hệ thống chuyển sang cổng thanh toán.
5. Cổng thanh toán trả kết quả.
6. Hệ thống xác nhận giao dịch.
7. Đặt vé được cập nhật `PAID`.
8. Vé được tạo.
9. ShowtimeSeat chuyển sang `SOLD`.
10. QR của Đặt vé được sinh nếu chưa có.

### Dòng sự kiện phụ
- 5.1. Thanh toán thất bại/hủy → thông báo thất bại → Hold tiếp tục nếu còn hạn.
- 5.2. Hold hết hạn trước khi thanh toán hoàn tất → thông báo hết hạn → giao dịch không hoàn tất.

### Hậu điều kiện
- **Thành công:** Đặt vé `PAID`, Vé `VALID`, ShowtimeSeat `SOLD`.
- **Thất bại:** Đặt vé chưa `PAID`.

---

## CUS-07 — Xem lịch sử Đặt vé

**Actor chính:** Customer  
**Quan hệ:** —  
**Tóm tắt:** Customer xem danh sách và chi tiết các Đặt vé của chính mình.  
**Tiền điều kiện:** Customer đã đăng nhập.

### Dòng sự kiện chính
1. Customer mở "Lịch sử đặt vé".
2. Hệ thống chỉ lấy Đặt vé của Customer hiện tại.
3. Hệ thống hiển thị danh sách theo thời gian.
4. Customer chọn một Đặt vé.
5. Hệ thống hiển thị chi tiết Đặt vé, vé, Bắp Nước, QR và trạng thái.

### Dòng sự kiện phụ
- 2.1. Không có Đặt vé → thông báo chưa có lịch sử.

### Hậu điều kiện
Không thay đổi dữ liệu.

---

# B. EMPLOYEE

## EMP-01 — Bán vé tại quầy

**Actor chính:** Employee  
**Quan hệ:** `<<include>>` CUS-05, EMP-02; Bắp Nước là hành vi tùy chọn.  
**Tóm tắt:** Employee đặt vé thay khách mua trực tiếp tại quầy.  
**Tiền điều kiện:** Employee đã đăng nhập và đang làm việc tại quầy.

### Dòng sự kiện chính
1. Employee chọn phim/Showtime.
2. Employee chọn tối đa 8 ghế.
3. Hệ thống atomic Hold các ghế.
4. Nếu thành công, các ghế chuyển `HOLD`.
5. Employee có thể thêm Bắp Nước.
6. Hệ thống tính tổng tiền.
7. Employee thực hiện EMP-02.
8. Thanh toán thành công → Đặt vé `PAID`.
9. Hệ thống tạo vé `VALID`.
10. ShowtimeSeat chuyển `SOLD`.
11. Hệ thống sinh QR.
12. Hệ thống in/hiển thị vé.

### Dòng sự kiện phụ
- 3.1. Ghế bị Hold/Sold đồng thời → thông báo conflict → rollback các ghế đã Hold trong request → quay lại bước 2.
- Thanh toán thất bại → Đặt vé chưa hoàn tất; Hold tiếp tục nếu còn hạn.

### Hậu điều kiện
Đặt vé bán tại quầy hoàn tất; Vé hợp lệ và ghế tương ứng `SOLD`.

---

## EMP-02 — Thanh toán tại quầy

**Actor chính:** Employee  
**Quan hệ:** `<<include>>` bởi EMP-01.  
**Phương thức:** Cash, Credit Card POS.  
**Tóm tắt:** Employee xử lý thanh toán cho Đặt vé tại quầy.  
**Tiền điều kiện:** Đặt vé đã được tạo và ghế đang `HOLD`.

### Dòng sự kiện chính
1. Hệ thống hiển thị tổng tiền.
2. Employee chọn Cash hoặc Credit Card.
3. Nếu Cash, Employee nhập số tiền khách đưa.
4. Hệ thống tính tiền thừa.
5. Nếu Card, hệ thống gửi giao dịch tới POS.
6. Hệ thống nhận kết quả thanh toán.
7. Đặt vé được cập nhật `PAID`.

### Dòng sự kiện phụ
- 3.1. Tiền khách đưa nhỏ hơn tổng tiền → thông báo không đủ tiền → nhập lại.
- 5.1. Card thất bại → thông báo → cho phép thực hiện lại.
- Phương thức chỉ dành cho online không được dùng ở quầy.

### Hậu điều kiện
Đặt Vé được thanh toán thành công hoặc vẫn ở trạng thái chưa thanh toán nếu giao dịch thất bại.

---

## EMP-03 — Soát vé

**Actor chính:** Employee  
**Quan hệ:** `<<include>>` EMP-05 nếu cần tra cứu Đặt Vé.  
**Tóm tắt:** Employee dùng QR của Đặt vé để kiểm tra và xác nhận từng vé.  
**Tiền điều kiện:** Đặt vé đã thanh toán và có vé.

### Dòng sự kiện chính
1. Employee nhập mã vé của Đặt vé (do khách cung cấp hoặc đọc trên vé in/điện tử).
2. Hệ thống tìm Đặt vé theo mã vé.
3. Hệ thống kiểm tra Đặt vé và Showtime.
4. Hệ thống lấy toàn bộ vé của Đặt vé.
5. Hệ thống hiển thị danh sách vé và trạng thái vé.
6. Employee chọn tất cả vé VALID hoặc chọn từng vé.
7. Hệ thống xác nhận các vé được chọn.
8. Vé được chọn chuyển `VALID` → `USED`.
9. Hệ thống hiển thị kết quả.

### Dòng sự kiện phụ
- 2.1. Mã vé không tồn tại → thông báo mã vé không hợp lệ.
- 3.1. Đặt vé chưa PAID → không cho soát.
- 3.2. Showtime không phù hợp → không cho soát.
- 6.1. Vé USED hoặc CANCELLED → không thể chọn.
- Một số vé đã USED nhưng còn vé VALID → chỉ cho xử lý vé VALID.

### Hậu điều kiện
Chỉ các vé được chọn chuyển sang `USED`; các vé còn lại không thay đổi.

> **Quyết định quan trọng:** QR thuộc Đặt vé, nhưng trạng thái sử dụng thuộc từng vé. Vì vậy cùng một QR có thể được quét lại để xử lý các vé VALID còn lại.

---

## EMP-04 — Giao Bắp và Nước

**Actor chính:** Employee  
**Quan hệ:** `<<include>>` EMP-05 nếu cần tra cứu Đặt vé. Không include EMP-03.  
**Tóm tắt:** Employee xác nhận giao Bắp và Nước thuộc Đặt Vé.  
**Tiền điều kiện:** Đặt vé tồn tại và có Bắp nước.

### Dòng sự kiện chính
1. Employee tra cứu Đặt vé.
2. Hệ thống hiển thị Bắp và Nước.
3. Employee chọn Bắp và Nước cần giao.
4. Employee xác nhận giao.
5. Hệ thống cập nhật trạng thái Bắp và Nước thành `DELIVERED`.
6. Hệ thống thông báo thành công.

### Dòng sự kiện phụ
- Không tìm thấy Đặt vé → thông báo.
- Đặt vé không có Bắp và Nước → thông báo.
- Bắp và Nước đã `DELIVERED` → không cho giao lại.

### Hậu điều kiện
Bắp Nước được đánh dấu đã giao.

---

## EMP-05 — Tra cứu Đặt Vé

**Actor chính:** Employee  
**Tóm tắt:** Employee tìm Đặt Vé để hỗ trợ các nghiệp vụ tại rạp.  
**Tiền điều kiện:** Employee đã đăng nhập.

### Dòng sự kiện chính
1. Employee nhập tiêu chí tra cứu được hỗ trợ.
2. Hệ thống tìm Đặt Vé.
3. Hệ thống hiển thị Đặt Vé phù hợp.
4. Hệ thống hiển thị trạng thái vé.
5. Hệ thống hiển thị vé, Bắp Nước, Showtime và QR nếu có.

### Dòng sự kiện phụ
- Không tìm thấy → thông báo.
- Có nhiều kết quả → Employee chọn Đặt Vé cần xem.

### Hậu điều kiện
Không thay đổi dữ liệu.

---

## EMP-06 — Hủy vé

**Actor chính:** Employee  
**Quan hệ:** `<<include>>` EMP-05.  
**Tóm tắt:** Employee hủy một hoặc nhiều vé hợp lệ theo nghiệp vụ.  
**Tiền điều kiện:** Đặt Vé tồn tại và có vé `VALID`.

### Dòng sự kiện chính
1. Employee tra cứu Đặt Vé.
2. Hệ thống hiển thị vé và `Trạng thái vé`.
3. Employee chọn vé cần hủy.
4. Hệ thống yêu cầu xác nhận.
5. Employee xác nhận hủy.
6. Vé được chọn chuyển `VALID → CANCELLED`.
7. ShowtimeSeat tương ứng chuyển `SOLD → AVAILABLE`.
8. Hệ thống ghi nhận việc hủy.

### Dòng sự kiện phụ
- 3.1. Vé `USED` → không cho hủy.
- 3.2. Vé `CANCELLED` → không cho hủy lại.
- 3.3. Không còn vé VALID → thông báo Đặt Vé không còn vé có thể hủy.
- Chính sách hoàn tiền nằm ngoài phạm vi hiện tại.

### Hậu điều kiện
Vé được chọn `CANCELLED`; ShowtimeSeat tương ứng `AVAILABLE`.

---

# C. ADMIN

## ADM-01 — Quản lý Danh mục phim

**Actor chính:** Admin  
**Tóm tắt:** Thêm/sửa/xóa/xem Movie và trạng thái Movie.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính
1. Admin mở Quản lý Phim.
2. Hệ thống hiển thị danh sách.
3. Admin chọn Thêm/Sửa/Xóa/Đổi trạng thái.
4. Admin nhập hoặc chỉnh thông tin.
5. Hệ thống validate.
6. Admin xác nhận.
7. Hệ thống cập nhật.

### Dòng sự kiện phụ
- Dữ liệu không hợp lệ → báo lỗi.
- Movie đang có Showtime chưa diễn ra → không xóa cứng; chuyển trạng thái nếu phù hợp.

### Hậu điều kiện
Thông tin Movie được cập nhật.

---

## ADM-02 — Quản lý Phòng chiếu

**Actor chính:** Admin  
**Tóm tắt:** Quản lý Room thuộc Cinema.  
**Tiền điều kiện:** Admin đã đăng nhập; Cinema tồn tại.

### Dòng sự kiện chính
1. Admin chọn Cinema.
2. Hệ thống hiển thị Room.
3. Admin Thêm/Sửa/Xóa Room.
4. Hệ thống validate.
5. Admin xác nhận.
6. Hệ thống cập nhật.

### Dòng sự kiện phụ
- Room đang có Showtime chưa diễn ra → chặn xóa.

### Hậu điều kiện
Danh sách Room được cập nhật.

---

## ADM-03 — Quản lý Sơ đồ chỗ ngồi

**Actor chính:** Admin  
**Tóm tắt:** Quản lý Seat vật lý thuộc Room.  
**Tiền điều kiện:** Admin đã đăng nhập; Room tồn tại.

### Dòng sự kiện chính
1. Admin chọn Room.
2. Hệ thống hiển thị Seat.
3. Admin Thêm/Sửa/Xóa Seat.
4. Hệ thống kiểm tra mã/vị trí ghế.
5. Admin xác nhận.
6. Hệ thống cập nhật.

### Dòng sự kiện phụ
- Mã/vị trí ghế trùng → báo lỗi.
- Seat đang có dữ liệu phụ thuộc → không xóa nếu vi phạm ràng buộc.

### Hậu điều kiện
Cấu hình Seat của Room được cập nhật.

> `Seat` chỉ quản lý cấu hình vật lý. Trạng thái `AVAILABLE/HOLD/SOLD` thuộc `ShowtimeSeat`.

---

## ADM-05 — Quản lý Suất chiếu

**Actor chính:** Admin  
**Tóm tắt:** Quản lý Showtime.  
**Tiền điều kiện:** Admin đã đăng nhập; Movie và Room tồn tại.

### Dòng sự kiện chính
1. Admin chọn Movie, Room và thời gian.
2. Admin xác nhận tạo Showtime.
3. Hệ thống kiểm tra conflict thời gian trong Room.
4. Hệ thống tạo Showtime.
5. Hệ thống tự động tạo `ShowtimeSeat` cho toàn bộ Seat của Room.
6. Các ShowtimeSeat mới có trạng thái `AVAILABLE`.
7. Hệ thống thông báo thành công.

### Dòng sự kiện phụ
- 3.1. Trùng khung giờ → cảnh báo conflict → yêu cầu chọn lại.
- Showtime đã có Đặt vé → không xóa/sửa theo cách phá vỡ Đặt vé hiện hữu.
- Hủy Showtime kèm hoàn tiền nằm ngoài phạm vi.

### Hậu điều kiện
Showtime và toàn bộ ShowtimeSeat tương ứng được tạo/cập nhật hợp lệ.

---

## ADM-06 — Quản lý Giá vé

**Actor chính:** Admin  
**Tóm tắt:** Cấu hình giá vé theo loại ghế/loại phòng/khung giờ/ngày.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính
1. Admin mở danh sách Price Rule.
2. Admin thêm/sửa/xóa rule.
3. Admin nhập điều kiện áp dụng và giá.
4. Hệ thống validate.
5. Hệ thống kiểm tra conflict/overlap.
6. Admin xác nhận.
7. Hệ thống lưu cấu hình.

### Dòng sự kiện phụ
- Giá không hợp lệ → báo lỗi.
- Rule overlap không được phép → báo conflict.
- Rule hết hiệu lực → không áp dụng cho Đặt vé mới.

### Hậu điều kiện
Cấu hình giá vé được cập nhật.

---

## ADM-08 — Quản lý Bắp và Nước

**Actor chính:** Admin  
**Tóm tắt:** Quản lý Bắp và Nước, giá và trạng thái bán.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính
1. Admin mở danh sách Bắp và Nước.
2. Admin thêm/sửa/xóa món.
3. Admin nhập tên, giá, hình ảnh và trạng thái.
4. Hệ thống validate.
5. Admin xác nhận.
6. Hệ thống cập nhật.

### Dòng sự kiện phụ
- Dữ liệu không hợp lệ → báo lỗi.
- Món không còn bán → chuyển trạng thái ngừng bán thay vì cho thêm vào Đặt vé mới.

### Hậu điều kiện
Danh mục Bắp và Nước được cập nhật.

---

## ADM-07 — Quản lý Nhân viên

**Actor chính:** Admin  
**Tóm tắt:** Quản lý thông tin và trạng thái Employee.  
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính
1. Admin mở danh sách Employee.
2. Admin chọn Thêm/Sửa/Vô hiệu hóa.
3. Admin nhập/chỉnh thông tin.
4. Hệ thống validate.
5. Admin xác nhận.
6. Hệ thống cập nhật.

### Dòng sự kiện phụ
- Tài khoản/thông tin định danh bị trùng → báo lỗi.
- Employee bị vô hiệu hóa → không được đăng nhập/sử dụng quyền Employee.

### Hậu điều kiện
Thông tin hoặc trạng thái Employee được cập nhật.

---

## ADM-08 — Quản lý Khách hàng
**Actor chính:** Admin 
**Quan hệ: — Tóm tắt:** Admin xem danh sách Customer, khóa/mở tài khoản vi phạm, điều chỉnh điểm tích lũy khi cần. 
**Tiền điều kiện:** Admin đã đăng nhập.

### Dòng sự kiện chính
1. Admin mở danh sách Customer.
2. Hệ thống hiển thị thông tin Customer: họ tên, liên hệ, ngày đăng ký, điểm tích lũy, hạng thành viên, trạng thái tài khoản.
3. Admin tìm kiếm/lọc theo tiêu chí (tên, trạng thái, hạng thành viên...).
4. Admin chọn một Customer để xem chi tiết hoặc chỉnh sửa.
5. Admin thực hiện thao tác: khóa/mở tài khoản, hoặc điều chỉnh điểm tích lũy.
6. Hệ thống yêu cầu xác nhận.
7. Admin xác nhận.
8. Hệ thống cập nhật trạng thái/điểm của Customer.
9. Hệ thống thông báo thành công.

### Dòng sự kiện phụ
• 5.1. Customer đã bị khóa → chỉ hiển thị tùy chọn mở khóa.
• 5.2. Điểm điều chỉnh âm vượt quá điểm hiện có → báo lỗi, không cho điểm âm.
• Không tìm thấy Customer phù hợp → thông báo.

### Hậu điều kiện: 
- Trạng thái tài khoản hoặc điểm tích lũy của Customer được cập nhật; không ảnh hưởng tới Đặt vé/Vé đã có của Customer đó.

### Phân biệt với ADM-08: ADM-08 quản lý tài khoản nội bộ (Employee) với dữ liệu chức vụ/phân quyền; ADM-09 quản lý tài khoản người dùng cuối (Customer) với dữ liệu điểm tích lũy/hạng thành viên — hai domain khác nhau nên tách UC riêng theo đúng nguyên tắc chia Phân hệ của tài liệu.

---

## ADM-09 — Quản lý Báo cáo và Thống kê
**Actor chính:** Admin 
**Quan hệ: — Tóm tắt:** Admin xem báo cáo doanh thu, số vé bán ra, tỷ lệ lấp đầy ghế theo phim/suất chiếu/khoảng thời gian. 
**Tiền điều kiện:** Admin đã đăng nhập; có dữ liệu Đặt vé đã PAID trong khoảng thời gian chọn.

### Dòng sự kiện chính
1. Admin mở Báo cáo và Thống kê.
2. Admin chọn loại báo cáo (doanh thu, số vé bán, tỷ lệ lấp đầy ghế) và khoảng thời gian/bộ lọc (theo phim, theo suất chiếu).
3. Hệ thống tổng hợp dữ liệu từ các Đặt vé PAID trong phạm vi đã chọn.
4. Hệ thống hiển thị báo cáo dạng bảng/biểu đồ.
5. Admin có thể xuất báo cáo ra file (Excel/PDF).

### Dòng sự kiện phụ
• 3.1. Không có dữ liệu trong khoảng thời gian chọn → hiển thị báo cáo rỗng, không báo lỗi.

### Hậu điều kiện: 
- Không thay đổi dữ liệu nghiệp vụ; chỉ đọc và tổng hợp dữ liệu hiện có.

# D. SYSTEM

## SYS-01 — Giải phóng Hold hết hạn

**Actor chính:** SystemScheduler  
**Tóm tắt:** Background job định kỳ giải phóng ShowtimeSeat đang HOLD quá hạn.  
**Tiền điều kiện:** Hệ thống Scheduler đang hoạt động.

### Dòng sự kiện chính
1. Scheduler chạy theo chu kỳ.
2. Hệ thống tìm `ShowtimeSeat` thỏa:
   `status = HOLD AND holdExpiresAt < currentTime`.
3. Hệ thống release các ghế tìm được.
4. ShowtimeSeat chuyển `HOLD → AVAILABLE`.
5. Kết thúc chu kỳ.

### Dòng sự kiện phụ
- 2.1. Không có Hold hết hạn → không thực hiện cập nhật.

### Hậu điều kiện
Các ShowtimeSeat hết hạn trở thành `AVAILABLE` và có thể được Hold lại.

> SYS-01 không thay đổi `SOLD` hoặc `AVAILABLE`.

---

# 4. QUAN HỆ INCLUDE / EXTEND ĐÃ CHỐT

| UC chính | Quan hệ | UC được gọi/mở rộng |
|---|---|---|
| CUS-06 Đặt vé online | `include` | CUS-04 Xem suất chiếu |
| CUS-06 Đặt vé online | `include` | CUS-06 Thanh toán online |
| EMP-01 Bán vé tại quầy | `include` | CUS-04 Xem suất chiếu |
| EMP-01 Bán vé tại quầy | `include` | EMP-02 Thanh toán tại quầy |
| EMP-03 Soát vé | `include` | EMP-05 Tra cứu Đặt vé |
| EMP-04 Giao Bắp Nước | `include` | EMP-05 Tra cứu Đặt vé |
| EMP-06 Hủy vé | `include` | EMP-05 Tra cứu Đặt vé |

## Lưu ý về AUTH-01: không nằm trong bảng include/extend vì cả 3 actor (Customer, Employee, Admin) nối trực tiếp tới AUTH-01 — đây là quan hệ actor–UC thông thường, không phải include/extend giữa hai UC.

### Các hành vi tùy chọn trong lúc Đặt vé

Các hành vi:

- Đặt Bắp và Nước

được xem là **optional behavior của Đặt vé**, không tính thêm vào 23 UC chính thức nếu nhóm không quyết định nâng chúng thành UC độc lập.

> Không tạo UC riêng cho: Chọn ghế, Hold ghế, Tính tiền, Sinh vé, Sinh QR. Đây là các bước/business behavior bên trong CUS-05/EMP-01.

---

# 5. CÁC QUY TẮC NGHIỆP VỤ CỐT LÕI

## Đặt vé / Ghế

- Một Đặt vé chỉ thuộc **một Showtime**.
- Một lần Đặt vé tối đa **8 vé/ghế**.
- `Đặt vé` là Aggregate Root.
- `ShowtimeSeat` là trạng thái ghế theo từng Showtime.
- `Seat` là cấu hình vật lý độc lập.
- Không cho hai request cùng bán thành công một ShowtimeSeat.
- Hold sử dụng atomic update.
- Hold mặc định 5 phút.
- `HOLD → AVAILABLE` khi timeout/cancel.
- `HOLD → SOLD` khi thanh toán thành công.

## Vé / QR

- Một lần Đặt vé có thể có nhiều vé.
- `Đặt vé.qrCodeString` là chuỗi UUID duy nhất.
- QR dùng để lookup Đặt vé.
- `Vé.Trạng thái vé` quản lý trạng thái sử dụng.
- `VALID → USED` khi soát vé.
- `VALID → CANCELLED` khi hủy vé.
- `USED` không được hủy.
- Một Đặt vé QR có thể được quét nhiều lần để xử lý các vé VALID còn lại.

## Payment

### Online
- MoMo
- VNPay
- Credit Card

### Tại quầy
- Cash
- Credit Card POS

Cash không được sử dụng trong CUS-06.

## Bắp Nước

- Đặt vé có thể chứa Bắp và Nước.
- Giao Bắp và Nước là UC độc lập với Soát vé.
- Bắp Nước có trạng thái giao để ngăn giao trùng.

## Scheduler

- Chỉ release `HOLD` đã hết hạn.
- Không thay đổi `SOLD`.
- Không thay đổi `AVAILABLE`.
- Có thể chạy định kỳ, ví dụ mỗi 30 giây.

---

# 6. STATE LIÊN QUAN TRỰC TIẾP ĐẾN USE CASE

## ShowtimeSeat

```text
AVAILABLE
    |
    | hold()
    v
  HOLD
  /   \
 /     \
timeout  paymentSuccess()
 |          |
 v          v
AVAILABLE  SOLD
              |
              | EMP-06 cancel
              v
          AVAILABLE
```

## Vé

```text
        +------+
        | VALID |
        +--+---+
           |
     +-----+-----+
     |           |
 verify       cancel
     |           |
     v           v
   USED      CANCELLED
```

---

# 7. KẾT CẤU TÀI LIỆU CHÍNH THỨC

Tài liệu UC của project được tổ chức theo thứ tự:

```text
SYSTEM
 ├── Phân hệ 1: Đặt vé & Thanh toán
 │    ├── CUS-04
 │    ├── CUS-05
 │    ├── CUS-06
 │    ├── CUS-07
 │    ├── EMP-01
 │    ├── EMP-02
 │    └── EMP-06
 │
 ├── Phân hệ 2: Vận hành tại rạp
 │    ├── EMP-03
 │    ├── EMP-04
 │    └── EMP-05
 │
 ├── Phân hệ 3: Tài khoản khách hàng
 │    ├── AUTH-01
 │    ├── CUS-01
 │    └── CUS-02
 │
 ├── Phân hệ 4: Danh mục & Lịch chiếu
 │    ├── CUS-03
 │    └── ADM-01 → ADM-05
 │
 ├── Phân hệ 5: Bắp Nước
 │    └── ADM-06
 │
 ├── Phân hệ 6: Quản trị nhân viên
 │    └── ADM-07, ADM-08
 │
 ├── Phân hệ 8: Báo cáo và thống kê
 |    └── ADM-09
 |
 └── Phân hệ 9: Tác vụ nền
      └── SYS-01
```

**Tổng: 1 System → 8 Phân hệ → 24 UC.**
