package com.pro.music.service;

import android.content.BroadcastReceiver; // Import lớp BroadcastReceiver để xử lý broadcast
import android.content.Context; // Import Context để truy cập các tài nguyên và dịch vụ của ứng dụng
import android.content.Intent; // Import Intent để nhận dữ liệu từ broadcast

import com.pro.music.constant.Constant; // Import lớp Constant chứa các hằng số
import com.pro.music.constant.GlobalFunction; // Import lớp GlobalFunction để gọi các hàm tiện ích

// Lớp `MusicReceiver` kế thừa từ `BroadcastReceiver`
// Dùng để xử lý các broadcast liên quan đến hành động điều khiển nhạc
public class MusicReceiver extends BroadcastReceiver {

    // Phương thức `onReceive` sẽ được gọi khi nhận một broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Lấy hành động nhạc từ Intent được gửi tới BroadcastReceiver
        int action = intent.getExtras().getInt(Constant.MUSIC_ACTION);

        // Gọi phương thức tiện ích để khởi động lại MusicService
        // Với hành động và vị trí bài hát hiện tại
        GlobalFunction.startMusicService(context, action, MusicService.mSongPosition);
    }
}
