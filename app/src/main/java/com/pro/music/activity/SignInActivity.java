package com.pro.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn; // API Google Sign-In
import com.google.android.gms.auth.api.signin.GoogleSignInAccount; // Đối tượng tài khoản Google
import com.google.android.gms.auth.api.signin.GoogleSignInClient; // Client để đăng nhập Google
import com.google.android.gms.auth.api.signin.GoogleSignInOptions; // Tùy chọn đăng nhập Google
import com.google.android.gms.common.api.ApiException; // Exception cho Google Sign-In
import com.google.android.gms.tasks.Task; // Task cho Firebase hoặc Google APIs
import com.google.firebase.auth.FirebaseAuth; // Firebase Authentication
import com.google.firebase.auth.FirebaseUser; // Đối tượng người dùng Firebase
import com.google.firebase.auth.GoogleAuthProvider; // Provider Google trong Firebase
import com.google.firebase.auth.AuthCredential; // Thông tin đăng nhập (credential)
import com.pro.music.R; // Import tài nguyên
import com.pro.music.constant.Constant; // Hằng số
import com.pro.music.constant.GlobalFunction; // Hàm tiện ích chung
import com.pro.music.databinding.ActivitySignInBinding; // View Binding
import com.pro.music.model.User; // Model User
import com.pro.music.prefs.DataStoreManager; // Quản lý dữ liệu người dùng
import com.pro.music.utils.StringUtil; // Tiện ích xử lý chuỗi

public class SignInActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 100; // Mã request để nhận kết quả đăng nhập Google
    private ActivitySignInBinding mActivitySignInBinding; // Biến View Binding
    private GoogleSignInClient mGoogleSignInClient; // Client Google Sign-In
    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo View Binding và kết nối layout
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // Lấy instance của FirebaseAuth
        configureGoogleSignIn(); // Cấu hình đăng nhập Google
        initListener(); // Khởi tạo các sự kiện
    }

    private void configureGoogleSignIn() {
        // Cấu hình các tùy chọn đăng nhập Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Lấy ID token từ strings.xml
                .requestEmail() // Yêu cầu quyền truy cập email
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); // Tạo client Google Sign-In
    }

    private void initListener() {
        // Mặc định chọn User trong radio button
        mActivitySignInBinding.rdbUser.setChecked(true);
        // Chuyển tới màn hình đăng ký khi nhấn vào nút "Sign Up"
        mActivitySignInBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(SignInActivity.this, SignUpActivity.class));
        // Xử lý đăng nhập
        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        // Chuyển tới màn hình quên mật khẩu
        mActivitySignInBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
        // Xử lý đăng nhập bằng Google
        mActivitySignInBinding.btnSignInGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void onClickForgotPassword() {
        // Chuyển tới màn hình quên mật khẩu
        GlobalFunction.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickValidateSignIn() {
        // Lấy email và mật khẩu từ giao diện
        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();

        // Kiểm tra điều kiện nhập liệu
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            // Phân biệt đăng nhập Admin/User dựa trên email
            if (mActivitySignInBinding.rdbAdmin.isChecked()) {
                if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signInUser(strEmail, strPassword); // Đăng nhập Admin
                }
                return;
            }

            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signInUser(strEmail, strPassword); // Đăng nhập User
            }
        }
    }

    private void signInUser(String email, String password) {
        // Hiển thị ProgressDialog trong khi đăng nhập
        showProgressDialog(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Ẩn ProgressDialog
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        // Lấy thông tin người dùng sau khi đăng nhập thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            // Đánh dấu Admin nếu email chứa định dạng admin
                            if (user.getEmail() != null && user.getEmail().contains(Constant.ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject); // Lưu thông tin người dùng
                            goToMainActivity(); // Chuyển tới màn hình chính
                        }
                    } else {
                        // Thông báo lỗi nếu đăng nhập thất bại
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        // Lấy intent đăng nhập Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // Bắt đầu Activity kết quả
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Xử lý kết quả đăng nhập Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Lấy thông tin tài khoản Google
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken()); // Đăng nhập Firebase bằng Google
            }
        } catch (Exception e) {
            Log.w("SignInActivity", "Google sign in failed", e); // Ghi log khi thất bại
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // Xác thực tài khoản Google với Firebase
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Nếu thành công, lấy thông tin người dùng
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), "");
                            DataStoreManager.setUser(userObject); // Lưu thông tin
                            goToMainActivity(); // Chuyển tới màn hình chính
                        }
                    } else {
                        // Thông báo lỗi nếu thất bại
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        // Phân biệt Admin và User để chuyển tới giao diện tương ứng
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(SignInActivity.this, AdminMainActivity.class);
        } else {
            GlobalFunction.startActivity(SignInActivity.this, MainActivity.class);
        }
        finishAffinity(); // Xóa toàn bộ ngăn xếp Activity
    }
}
