// File: ForgotPasswordActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.music.R;
import com.pro.music.databinding.ActivityForgotPasswordBinding;
import com.pro.music.utils.StringUtil;

// Lớp quản lý chức năng quên mật khẩu
public class ForgotPasswordActivity extends BaseActivity {

    // Biến binding để truy cập các thành phần trong layout
    private ActivityForgotPasswordBinding mActivityForgotPasswordBinding;

    // Phương thức onCreate khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityForgotPasswordBinding.getRoot()); // Thiết lập giao diện

        initListener(); // Khởi tạo các sự kiện cho giao diện
    }

    // Phương thức khởi tạo các sự kiện
    private void initListener() {
        // Sự kiện khi nhấn nút quay lại
        mActivityForgotPasswordBinding.imgBack.setOnClickListener(v -> onBackPressed());

        // Sự kiện khi nhấn nút "Đặt lại mật khẩu"
        mActivityForgotPasswordBinding.btnResetPassword.setOnClickListener(v -> onClickValidateResetPassword());
    }

    // Phương thức kiểm tra dữ liệu và xác nhận gửi email đặt lại mật khẩu
    private void onClickValidateResetPassword() {
        // Lấy địa chỉ email từ ô nhập liệu
        String strEmail = mActivityForgotPasswordBinding.edtEmail.getText().toString().trim();

        // Kiểm tra nếu email rỗng
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this,
                    getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu email không hợp lệ
        else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this,
                    getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        }
        // Nếu email hợp lệ, thực hiện đặt lại mật khẩu
        else {
            resetPassword(strEmail);
        }
    }

    // Phương thức thực hiện gửi email đặt lại mật khẩu
    private void resetPassword(String email) {
        showProgressDialog(true); // Hiển thị hộp thoại chờ

        FirebaseAuth auth = FirebaseAuth.getInstance(); // Lấy đối tượng FirebaseAuth

        // Gửi email đặt lại mật khẩu
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false); // Ẩn hộp thoại chờ

                    // Nếu gửi thành công
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this,
                                getString(R.string.msg_reset_password_successfully),
                                Toast.LENGTH_SHORT).show();

                        // Xóa nội dung ô nhập email
                        mActivityForgotPasswordBinding.edtEmail.setText("");
                    }
                });
    }
}
