package com.pro.music.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.pro.music.constant.AboutUsConfig; // Chứa các thông tin cấu hình cho phần "About Us"
import com.pro.music.constant.GlobalFunction; // Các hàm tiện ích
import com.pro.music.databinding.ActivitySplashBinding; // View Binding cho giao diện SplashActivity
import com.pro.music.prefs.DataStoreManager; // Quản lý dữ liệu người dùng
import com.pro.music.utils.StringUtil; // Tiện ích xử lý chuỗi

@SuppressLint("CustomSplashScreen") // Bỏ qua cảnh báo về việc sử dụng SplashScreen không chính thức
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding mActivitySplashBinding; // Biến để thao tác với View Binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Yêu cầu không hiển thị tiêu đề (No Title Bar)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt Activity ở chế độ toàn màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Khởi tạo View Binding và gắn layout vào Activity
        mActivitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());

        initUi(); // Thiết lập nội dung giao diện

        // Tạo một Handler để chuyển màn hình sau 2 giây
        Handler handler = new Handler();
        handler.postDelayed(this::goToActivity, 2000); // Gọi `goToActivity()` sau 2000ms
    }

    private void initUi() {
        // Thiết lập nội dung tiêu đề và khẩu hiệu từ `AboutUsConfig`
        mActivitySplashBinding.tvAboutUsTitle.setText(AboutUsConfig.ABOUT_US_TITLE);
        mActivitySplashBinding.tvAboutUsSlogan.setText(AboutUsConfig.ABOUT_US_SLOGAN);
    }

    private void goToActivity() {
        // Kiểm tra nếu người dùng đã đăng nhập (dữ liệu người dùng không rỗng)
        if (DataStoreManager.getUser() != null
                && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            // Nếu người dùng là Admin, chuyển tới màn hình AdminMainActivity
            if (DataStoreManager.getUser().isAdmin()) {
                GlobalFunction.startActivity(this, AdminMainActivity.class);
            } else {
                // Nếu là User thông thường, chuyển tới màn hình MainActivity
                GlobalFunction.startActivity(this, MainActivity.class);
            }
        } else {
            // Nếu chưa đăng nhập, chuyển tới màn hình SignInActivity
            GlobalFunction.startActivity(this, SignInActivity.class);
        }
        finish(); // Đóng SplashActivity để không quay lại được
    }
}
