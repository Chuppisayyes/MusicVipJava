// File: AdminChangePasswordActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.music.R;
import com.pro.music.databinding.ActivityAdminChangePasswordBinding;
import com.pro.music.model.User;
import com.pro.music.prefs.DataStoreManager;
import com.pro.music.utils.StringUtil;

// Lớp xử lý thay đổi mật khẩu trong phần quản trị
public class AdminChangePasswordActivity extends BaseActivity {

    // Biến binding để truy cập các thành phần trong layout
    private ActivityAdminChangePasswordBinding binding;

    // Phương thức onCreate khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện

        initListener(); // Khởi tạo các sự kiện cho giao diện
    }

    // Phương thức khởi tạo các sự kiện
    private void initListener() {
        // Sự kiện khi nhấn nút quay lại
        binding.imgBack.setOnClickListener(v -> onBackPressed());

        // Sự kiện khi nhấn nút đổi mật khẩu
        binding.btnChangePassword
                .setOnClickListener(v -> onClickValidateChangePassword());
    }

    // Phương thức xử lý logic kiểm tra và xác nhận đổi mật khẩu
    private void onClickValidateChangePassword() {
        // Lấy dữ liệu từ giao diện
        String strOldPassword = binding.edtOldPassword.getText().toString().trim();
        String strNewPassword = binding.edtNewPassword.getText().toString().trim();
        String strConfirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        // Kiểm tra nếu mật khẩu cũ rỗng
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu mật khẩu mới rỗng
        else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu xác nhận mật khẩu rỗng
        else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu mật khẩu cũ không đúng
        else if (!DataStoreManager.getUser().getPassword().equals(strOldPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu mật khẩu mới và xác nhận mật khẩu không khớp
        else if (!strNewPassword.equals(strConfirmPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        }
        // Kiểm tra nếu mật khẩu mới trùng với mật khẩu cũ
        else if (strOldPassword.equals(strNewPassword)) {
            Toast.makeText(this,
                    getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        }
        // Nếu tất cả điều kiện hợp lệ, tiến hành đổi mật khẩu
        else {
            changePassword(strNewPassword);
        }
    }

    // Phương thức thực hiện thay đổi mật khẩu
    private void changePassword(String newPassword) {
        showProgressDialog(true); // Hiển thị hộp thoại chờ

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Lấy người dùng hiện tại
        if (user == null) return;

        // Thực hiện đổi mật khẩu trên Firebase
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false); // Ẩn hộp thoại chờ

                    if (task.isSuccessful()) {
                        // Nếu đổi mật khẩu thành công
                        Toast.makeText(this,
                                getString(R.string.msg_change_password_successfully),
                                Toast.LENGTH_SHORT).show();

                        // Cập nhật mật khẩu mới trong dữ liệu người dùng cục bộ
                        User userLogin = DataStoreManager.getUser();
                        userLogin.setPassword(newPassword);
                        DataStoreManager.setUser(userLogin);

                        // Xóa nội dung các ô nhập liệu
                        binding.edtOldPassword.setText("");
                        binding.edtNewPassword.setText("");
                        binding.edtConfirmPassword.setText("");
                    }
                });
    }
}
