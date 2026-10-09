# Lab A5: Danh bạ mini - Chuyển màn hình & Truyền dữ liệu bằng Intent

## 👤 Thông tin sinh viên
- **Họ và tên:** Bùi Quang Duy
- **MSSV:** 231A290126
- **Lớp học phần:** Lập trình trên thiết bị di động
- **Repo GitHub:** A5_231A290127

## 📝 Tổng quan dự án
Ứng dụng "Danh bạ mini" được xây dựng trên Android Studio bằng ngôn ngữ Java và giao diện XML. Dự án nhằm thực hành các kỹ thuật cơ bản và nâng cao về `Intent` trong Android, bao gồm:
- Phân biệt và sử dụng **Intent tường minh** (Explicit Intent) và **Intent ngầm định** (Implicit Intent).
- Truyền dữ liệu qua lại giữa các `Activity` bằng cách đóng gói đối tượng với `Parcelable`.
- Nhận kết quả trả về sử dụng `ActivityResultLauncher`.
- Xử lý các ngoại lệ (như `ActivityNotFoundException`) khi mở các ứng dụng bên ngoài.
- Hiểu rõ cơ chế của ngăn xếp màn hình (Back Stack) và vòng đời (Lifecycle) của Activity.

## 🚀 Các chức năng chính
1. **Truyền dữ liệu (Explicit Intent):**
   - Nhập thông tin liên hệ (Họ tên, SĐT, Email) ở Màn hình 1.
   - Chuyển sang Màn hình 2, truyền toàn bộ đối tượng `Contact` qua Intent (sử dụng Parcelable).
2. **Nhận kết quả trả về:**
   - Tại Màn hình 2, người dùng có thể chỉnh sửa "Họ tên" và bấm "Lưu & quay lại".
   - Màn hình 1 sẽ bắt được kết quả (mã `RESULT_OK`) và cập nhật giao diện.
   - Xử lý nhánh người dùng bấm phím Back/Hủy (mã `RESULT_CANCELED`).
3. **Intent ngầm định (Implicit Intent):**
   - **Gọi điện:** Bấm nút Gọi để chuyển số điện thoại đã nhập sang màn hình gọi điện hệ thống (`ACTION_DIAL`).
   - **Web trường:** Mở liên kết đến website của trường Đại học Văn Hiến bằng trình duyệt mặc định (`ACTION_VIEW`).
   - **Chia sẻ:** Chia sẻ thông tin liên hệ cho các ứng dụng khác thông qua Share Sheet (`ACTION_SEND`).

## 🌟 Các tính năng nâng cao (Đã hoàn thành)
Dự án đã tích hợp thêm 2 tính năng mở rộng theo yêu cầu của bài Lab:
1. **Bài NC2 (Truyền danh sách đối tượng):** 
   - Thay vì chỉ truyền 1 đối tượng đơn lẻ, ứng dụng đóng gói và truyền toàn bộ một danh sách (`ArrayList<Contact>`) thông qua `putParcelableArrayListExtra`.
2. **Bài NC4 (Nhận văn bản chia sẻ từ ứng dụng khác):** 
   - Ứng dụng khai báo bộ lọc `intent-filter` để xuất hiện trong danh sách nhận văn bản chia sẻ (`ACTION_SEND`, mimeType `text/plain`).
   - Khi người dùng bôi đen văn bản ở app khác và chọn Chia sẻ vào app này, văn bản sẽ tự động được điền vào ô "Họ tên".

## 🔬 Thí nghiệm vòng đời (Lifecycle)
Đã thực hiện gắn Log (`Log.d`) và phân tích 5 kịch bản vòng đời khi chuyển đổi qua lại giữa hai màn hình (được đính kèm chi tiết trong báo cáo Word/PDF).

---
*Dự án này là bài tập thực hành môn Lập trình thiết bị di động.*
