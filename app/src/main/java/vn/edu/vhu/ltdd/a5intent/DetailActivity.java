package vn.edu.vhu.ltdd.a5intent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "A5_231A290127";

    private Contact contact;
    private EditText edtHoTenMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // 1. Ánh xạ các View từ activity_detail.xml
        TextView tvThongTin = findViewById(R.id.tvThongTin);
        TextView tvNguoiGui = findViewById(R.id.tvNguoiGui);
        edtHoTenMoi = findViewById(R.id.edtHoTenMoi);
        Button btnLuu = findViewById(R.id.btnLuu);
        Button btnHuy = findViewById(R.id.btnHuy);

        // 2. Lấy dữ liệu do MainActivity gửi sang
        contact = IntentCompat.getParcelableExtra(getIntent(),
                MainActivity.EXTRA_CONTACT, Contact.class);
        String nguoiGui = getIntent().getStringExtra(MainActivity.EXTRA_NGUOI_GUI);
// --- BẮT ĐẦU CODE BÀI NÂNG CAO NC2 ---
        ArrayList<Contact> danhSachNhanDuoc;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            danhSachNhanDuoc = getIntent().getParcelableArrayListExtra("EXTRA_LIST_CONTACT", Contact.class);
        } else {
            danhSachNhanDuoc = getIntent().getParcelableArrayListExtra("EXTRA_LIST_CONTACT");
        }

// Kiểm tra xem danh sách có nhận được không bằng cách hiện một thông báo nhỏ (Toast)
        if (danhSachNhanDuoc != null && danhSachNhanDuoc.size() > 0) {
            Toast.makeText(this, "NC2: Đã nhận thành công danh sách " + danhSachNhanDuoc.size() + " người!", Toast.LENGTH_LONG).show();
        }
// --- KẾT THÚC CODE BÀI NÂNG CAO NC2 ---
        // 3. Luôn kiểm tra null phòng trường hợp Activity được mở mà không có dữ liệu
        if (contact == null) {
            tvThongTin.setText(R.string.no_data);
            Log.w(TAG, "Không nhận được Contact từ Intent");
            return;
        }

        // 4. Hiển thị thông tin lên giao diện
        tvThongTin.setText(getString(R.string.detail_format,
                contact.getHoTen(), contact.getDienThoai(), contact.getEmail()));
        tvNguoiGui.setText(getString(R.string.sent_by, nguoiGui));
        edtHoTenMoi.setText(contact.getHoTen());

        // 5. Gán sự kiện click cho các nút
        btnLuu.setOnClickListener(v -> luuVaQuayLai());
        btnHuy.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);              // Báo cho MainActivity biết là người dùng đã hủy
            finish();                                // Đóng màn hình hiện tại
        });
    }

    /** Trả dữ liệu đã sửa về màn hình gọi (MainActivity). */
    private void luuVaQuayLai() {
        String hoTenMoi = edtHoTenMoi.getText().toString().trim();
        // Kiểm tra rỗng
        if (hoTenMoi.isEmpty()) {
            edtHoTenMoi.setError(getString(R.string.err_empty));
            return;
        }
        // Cập nhật họ tên mới vào đối tượng contact
        contact.setHoTen(hoTenMoi);

        // Đóng gói đối tượng vào Intent kết quả
        Intent ketQua = new Intent();
        ketQua.putExtra(MainActivity.EXTRA_CONTACT, contact);

        // Thiết lập mã thành công kèm dữ liệu (BẮT BUỘC TRƯỚC finish())
        setResult(RESULT_OK, ketQua);
        Log.d(TAG, "Trả kết quả về: " + hoTenMoi);

        // Đóng màn hình chi tiết để quay về MainActivity
        finish();
    }
}