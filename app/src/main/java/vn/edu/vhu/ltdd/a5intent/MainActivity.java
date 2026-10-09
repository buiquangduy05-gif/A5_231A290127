package vn.edu.vhu.ltdd.a5intent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "A5_231A290127";


    public static final String EXTRA_CONTACT = "extra_contact";
    public static final String EXTRA_NGUOI_GUI = "extra_nguoi_gui";

    private EditText edtHoTen, edtDienThoai, edtEmail;
    private TextView tvKetQuaTraVe;

    /** Bộ nhận kết quả trả về từ DetailActivity (thay cho onActivityResult đã lỗi thời). */
    private final ActivityResultLauncher<Intent> chiTietLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Contact daSua = IntentCompat.getParcelableExtra(
                            result.getData(), EXTRA_CONTACT, Contact.class);
                    if (daSua != null) {
                        edtHoTen.setText(daSua.getHoTen());
                        tvKetQuaTraVe.setText(getString(R.string.returned, daSua.getHoTen()));
                        Log.d(TAG, "Nhận kết quả trả về: " + daSua.getHoTen());
                    }
                } else {
                    tvKetQuaTraVe.setText(R.string.returned_cancel);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        edtHoTen = findViewById(R.id.edtHoTen);
        edtDienThoai = findViewById(R.id.edtDienThoai);
        edtEmail = findViewById(R.id.edtEmail);
        tvKetQuaTraVe = findViewById(R.id.tvKetQuaTraVe);

        Button btnChiTiet = findViewById(R.id.btnChiTiet);
        Button btnGoi = findViewById(R.id.btnGoi);
        Button btnWeb = findViewById(R.id.btnWeb);
        Button btnChiaSe = findViewById(R.id.btnChiaSe);

        btnChiTiet.setOnClickListener(v -> moManHinhChiTiet());
        btnGoi.setOnClickListener(v -> goiDien());
        btnWeb.setOnClickListener(v -> moTrangWeb());
        btnChiaSe.setOnClickListener(v -> chiaSe());
    }

    // ============ INTENT TƯỜNG MINH (explicit) ============

    private void moManHinhChiTiet() {
        String hoTen = edtHoTen.getText().toString().trim();
        if (hoTen.isEmpty()) {
            edtHoTen.setError(getString(R.string.err_empty));
            return;
        }

        // 1. Tạo đối tượng Contact từ ô nhập (yêu cầu cơ bản)
        Contact contact = new Contact(hoTen,
                edtDienThoai.getText().toString().trim(),
                edtEmail.getText().toString().trim());

        // 2. Tạo một ArrayList chứa danh sách các Contact (BÀI NÂNG CAO NC2)
        ArrayList<Contact> danhSach = new ArrayList<>();
        danhSach.add(contact); // Cho người vừa nhập vào danh sách luôn
        danhSach.add(new Contact("Duy", "099999999", "duy@gmail.com")); // Người giả lập thứ 2
        danhSach.add(new Contact("Giáo Viên", "088888888", "gv@vhu.edu.vn")); // Người thứ 3

        Intent intent = new Intent(this, DetailActivity.class);

        // 3. Đóng gói để gửi đi
        intent.putExtra(EXTRA_CONTACT, contact);           // Gửi đối tượng đơn lẻ
        intent.putExtra(EXTRA_NGUOI_GUI, TAG);             // Gửi chuỗi đơn giản

        // GỬI DANH SÁCH (BÀI NÂNG CAO NC2)
        intent.putParcelableArrayListExtra("EXTRA_LIST_CONTACT", danhSach);

        // Mở và CHỜ kết quả
        chiTietLauncher.launch(intent);
    }

    // ============ INTENT NGẦM ĐỊNH (implicit) ============

    private void goiDien() {
        String sdt = edtDienThoai.getText().toString().trim();
        if (sdt.isEmpty()) {
            edtDienThoai.setError(getString(R.string.err_empty));
            return;
        }
        // ACTION_DIAL chỉ mở màn hình gọi với số đã điền sẵn → KHÔNG cần xin quyền
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sdt));
        moAnToan(intent);
    }

    private void moTrangWeb() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.school_url)));
        moAnToan(intent);
    }

    private void chiaSe() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text,
                edtHoTen.getText().toString(), edtDienThoai.getText().toString()));
        // Bọc trong bộ chọn để người dùng tự chọn ứng dụng
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    /**
     * Từ Android 11, resolveActivity() thường trả về null do cơ chế giới hạn hiển thị gói
     * (package visibility). Cách an toàn nhất là bắt ngoại lệ ActivityNotFoundException.
     */
    private void moAnToan(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.err_no_app, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Không có ứng dụng nào xử lý: " + intent.getAction(), e);
        }
    }
}