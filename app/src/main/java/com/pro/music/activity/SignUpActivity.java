package com.pro.music.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth; // Firebase Authentication
import com.google.firebase.auth.FirebaseUser; // Đối tượng người dùng Firebase
import com.pro.music.R; // Import tài nguyên
import com.pro.music.constant.Constant; // File chứa các hằng số
import com.pro.music.constant.GlobalFunction; // Các hàm tiện ích
import com.pro.music.databinding.ActivitySignUpBinding; // View Binding cho giao diện
import com.pro.music.model.User; // Model User
import com.pro.music.prefs.DataStoreManager; // Quản lý dữ liệu người dùng
import com.pro.music.utils.StringUtil; // Tiện ích xử lý chuỗi

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding mActivitySignUpBinding; // View Binding để thao tác giao diện

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo View Binding và gắn layout vào Activity
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

        initListener(); // Thiết lập các sự kiện
    }

    private void initListener() {
        // Mặc định chọn radio button User
        mActivitySignUpBinding.rdbUser.setChecked(true);
        // Xử lý khi nhấn nút "Back"
        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        // Xử lý khi nhấn "Sign In" để trở về màn hình đăng nhập
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        // Xử lý khi nhấn nút "Sign Up"
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        // Lấy email và mật khẩu từ giao diện
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();

        // Kiểm tra tính hợp lệ của dữ liệu nhập
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            // Phân biệt đăng ký Admin/User dựa trên email
            if (mActivitySignUpBinding.rdbAdmin.isChecked()) {
                // Nếu là Admin nhưng email không đúng định dạng Admin
                if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(strEmail, strPassword); // Đăng ký Admin
                }
                return;
            }

            // Nếu User nhập email chứa định dạng Admin
            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(strEmail, strPassword); // Đăng ký User
            }
        }
    }

    private void signUpUser(String email, String password) {
        // Hiển thị ProgressDialog trong quá trình đăng ký
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // Lấy instance FirebaseAuth

        // Tạo tài khoản bằng email và mật khẩu
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Ẩn ProgressDialog khi hoàn tất
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        // Nếu đăng ký thành công, lấy thông tin người dùng Firebase
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            // Đánh dấu Admin nếu email chứa định dạng Admin
                            if (user.getEmail() != null && user.getEmail().contains(Constant.ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject); // Lưu thông tin người dùng
                            goToMainActivity(); // Chuyển tới màn hình chính
                        }
                    } else {
                        // Thông báo lỗi nếu đăng ký thất bại
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        // Kiểm tra loại người dùng để chuyển tới giao diện tương ứng
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(SignUpActivity.this, AdminMainActivity.class); // Giao diện Admin
        } else {
            GlobalFunction.startActivity(SignUpActivity.this, MainActivity.class); // Giao diện User
        }
        finishAffinity(); // Xóa toàn bộ các Activity trong ngăn xếp
    }
}
