// File: AdminMainActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pro.music.R;
import com.pro.music.adapter.AdminViewPagerAdapter;
import com.pro.music.databinding.ActivityAdminMainBinding;

// Lớp chính dành cho giao diện quản trị viên
public class AdminMainActivity extends BaseActivity {

    // Biến binding để truy cập các thành phần trong layout
    private ActivityAdminMainBinding mActivityAdminMainBinding;

    // Phương thức onCreate khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminMainBinding.getRoot()); // Thiết lập giao diện

        setToolBar(); // Khởi tạo thanh công cụ (toolbar)

        // Cấu hình ViewPager2
        mActivityAdminMainBinding.viewpager2.setUserInputEnabled(false); // Tắt thao tác vuốt
        mActivityAdminMainBinding.viewpager2.setOffscreenPageLimit(5); // Giữ 5 trang trong bộ nhớ

        // Gắn adapter cho ViewPager2
        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        mActivityAdminMainBinding.viewpager2.setAdapter(adminViewPagerAdapter);

        // Sự kiện thay đổi trang của ViewPager2
        mActivityAdminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Đặt trạng thái lựa chọn của BottomNavigationView theo trang
                switch (position) {
                    case 0:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_category).setChecked(true);
                        break;

                    case 1:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_artist).setChecked(true);
                        break;

                    case 2:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_song).setChecked(true);
                        break;

                    case 3:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;

                    case 4:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
        });

        // Sự kiện khi người dùng nhấn vào các mục trong BottomNavigationView
        mActivityAdminMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // Đặt trang hiện tại của ViewPager2 dựa trên mục được chọn
            if (id == R.id.nav_category) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_artist) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_song) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(2);
            } else if (id == R.id.nav_feedback) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(3);
            } else if (id == R.id.nav_account) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(4);
            }
            return true;
        });
    }

    // Ghi đè phương thức xử lý khi nhấn nút quay lại
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showConfirmExitApp(); // Hiển thị xác nhận thoát ứng dụng
    }

    // Hiển thị hộp thoại xác nhận thoát ứng dụng
    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name)) // Tiêu đề
                .content(getString(R.string.msg_exit_app)) // Nội dung thông báo
                .positiveText(getString(R.string.action_ok)) // Nút "OK"
                .onPositive((dialog, which) -> finishAffinity()) // Thoát ứng dụng khi nhấn "OK"
                .negativeText(getString(R.string.action_cancel)) // Nút "Hủy"
                .cancelable(false) // Không cho phép hủy bằng cách nhấn bên ngoài
                .show(); // Hiển thị hộp thoại
    }

    // Cài đặt thanh công cụ (toolbar)
    public void setToolBar() {
        mActivityAdminMainBinding.header.imgLeft.setVisibility(View.GONE); // Ẩn nút quay lại
        mActivityAdminMainBinding.header.tvTitle.setText(getString(R.string.app_name)); // Đặt tiêu đề là tên ứng dụng
    }
}
