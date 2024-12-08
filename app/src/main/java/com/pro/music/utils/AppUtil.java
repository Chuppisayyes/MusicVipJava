package com.pro.music.utils;

import android.annotation.SuppressLint; // Import annotation để tắt cảnh báo lint từ IDE

// Lớp `AppUtil` chứa các phương thức tiện ích dùng chung trong ứng dụng
public class AppUtil {

    // *** Phương thức chuyển đổi thời gian từ mili giây sang định dạng "MM:SS" ***
    @SuppressLint("DefaultLocale") // Tắt cảnh báo liên quan đến định dạng mặc định
    public static String getTime(int millis) {
        // Tính số giây còn lại sau khi lấy phần dư của mili giây chia 1000
        long second = (millis / 1000) % 60;

        // Tính số phút từ tổng số mili giây
        long minute = millis / (1000 * 60);

        // Trả về chuỗi thời gian được định dạng theo kiểu "MM:SS"
        return String.format("%02d:%02d", minute, second);
    }
}
