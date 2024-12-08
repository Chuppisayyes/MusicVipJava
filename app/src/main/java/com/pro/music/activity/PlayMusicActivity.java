package com.pro.music.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pro.music.R; // Import file tài nguyên R, chứa các id của resource trong project
import com.pro.music.adapter.MusicViewPagerAdapter; // Import adapter cho ViewPager
import com.pro.music.constant.Constant; // Import file chứa các hằng số dùng trong ứng dụng
import com.pro.music.constant.GlobalFunction; // Import file chứa các phương thức toàn cục (tiện ích)
import com.pro.music.databinding.ActivityPlayMusicBinding; // Import lớp binding của layout activity_play_music
import com.pro.music.model.Song; // Import model bài hát
import com.pro.music.prefs.DataStoreManager; // Import lớp quản lý dữ liệu người dùng
import com.pro.music.service.MusicService; // Import dịch vụ phát nhạc

public class PlayMusicActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION_CODE = 10; // Mã yêu cầu quyền ghi bộ nhớ
    private Song mSong; // Biến lưu bài hát hiện tại cần tải xuống
    private ActivityPlayMusicBinding mActivityPlayMusicBinding; // Biến sử dụng View Binding cho giao diện

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo View Binding để kết nối layout
        mActivityPlayMusicBinding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(mActivityPlayMusicBinding.getRoot()); // Đặt layout cho Activity

        initToolbar(); // Thiết lập giao diện Toolbar
        initUI(); // Thiết lập giao diện ViewPager và Indicator
    }

    private void initToolbar() {
        // Đặt hình ảnh cho nút quay lại trên Toolbar
        mActivityPlayMusicBinding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white);
        // Đặt tiêu đề cho Toolbar là "Music Player"
        mActivityPlayMusicBinding.toolbar.tvTitle.setText(R.string.music_player);
        // Ẩn layout Play All vì không cần thiết
        mActivityPlayMusicBinding.toolbar.layoutPlayAll.setVisibility(View.GONE);
        // Gắn sự kiện khi nhấn nút quay lại
        mActivityPlayMusicBinding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        // Nếu người dùng là Admin, khởi động dịch vụ phát nhạc
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startMusicService(
                    PlayMusicActivity.this, // Context hiện tại
                    Constant.CANNEL_NOTIFICATION, // ID kênh thông báo
                    MusicService.mSongPosition // Vị trí bài hát hiện tại
            );
        }
        super.onBackPressed(); // Thực hiện hành động quay lại mặc định
    }

    private void initUI() {
        // Tạo adapter cho ViewPager
        MusicViewPagerAdapter musicViewPagerAdapter = new MusicViewPagerAdapter(this);
        // Gắn adapter cho ViewPager2
        mActivityPlayMusicBinding.viewpager2.setAdapter(musicViewPagerAdapter);
        // Gắn Indicator3 với ViewPager2
        mActivityPlayMusicBinding.indicator3.setViewPager(mActivityPlayMusicBinding.viewpager2);
        // Đặt trang mặc định trong ViewPager2 là trang thứ hai (index 1)
        mActivityPlayMusicBinding.viewpager2.setCurrentItem(1);
    }

    public void downloadSong(Song song) {
        // Gán bài hát cần tải xuống vào biến toàn cục
        mSong = song;
        // Kiểm tra quyền truy cập trước khi tải
        checkPermission();
    }

    private void checkPermission() {
        // Nếu thiết bị chạy Android phiên bản thấp hơn TIRAMISU
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // Kiểm tra quyền ghi bộ nhớ ngoài
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // Nếu chưa được cấp quyền, yêu cầu người dùng cấp quyền
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            } else {
                // Nếu đã có quyền, bắt đầu tải bài hát
                GlobalFunction.startDownloadFile(this, mSong);
            }
        } else {
            // Với Android TIRAMISU trở lên, tải bài hát mà không cần quyền ghi bộ nhớ ngoài
            GlobalFunction.startDownloadFile(this, mSong);
        }
    }

    @SuppressLint("MissingSuperCall") // Bỏ qua cảnh báo thiếu super trong override
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Kiểm tra mã yêu cầu quyền
        if (requestCode == REQUEST_PERMISSION_CODE) {
            // Nếu người dùng cấp quyền
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Bắt đầu tải bài hát
                GlobalFunction.startDownloadFile(this, mSong);
            } else {
                // Nếu từ chối quyền, hiển thị thông báo
                Toast.makeText(this, getString(R.string.msg_permission_denied),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
