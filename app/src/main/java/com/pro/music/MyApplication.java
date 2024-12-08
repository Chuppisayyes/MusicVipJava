package com.pro.music;

import android.app.Application; // Lớp cơ bản cho ứng dụng Android
import android.app.NotificationChannel; // Dùng để tạo kênh thông báo trên Android 8.0+
import android.app.NotificationManager; // Quản lý thông báo
import android.content.Context; // Context cung cấp quyền truy cập tài nguyên và thông tin ứng dụng
import android.os.Build; // Kiểm tra phiên bản Android

import com.google.firebase.FirebaseApp; // Khởi tạo Firebase
import com.google.firebase.database.DatabaseReference; // Đại diện cho tham chiếu tới Firebase Database
import com.google.firebase.database.FirebaseDatabase; // Firebase Realtime Database
import com.pro.music.prefs.DataStoreManager; // Quản lý lưu trữ cục bộ

// Lớp `MyApplication` mở rộng từ `Application` để quản lý trạng thái toàn cục của ứng dụng
public class MyApplication extends Application {

    // *** URL cơ sở của Firebase ***
    public static final String FIREBASE_URL = "https://androidmusicapp-54077-default-rtdb.firebaseio.com";

    // *** Kênh thông báo ***
    public static final String CHANNEL_ID = "channel_music_basic_id"; // ID của kênh thông báo
    private static final String CHANNEL_NAME = "channel_music_basic_name"; // Tên của kênh thông báo

    // Biến Firebase Database
    private FirebaseDatabase mFirebaseDatabase;

    // *** Phương thức lấy đối tượng MyApplication từ context ***
    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    // *** Phương thức onCreate ***
    // Được gọi khi ứng dụng được khởi tạo
    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);

        // Lấy đối tượng FirebaseDatabase với URL cụ thể
        mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);

        // Tạo kênh thông báo
        createChannelNotification();

        // Khởi tạo DataStoreManager để quản lý lưu trữ cục bộ
        DataStoreManager.init(getApplicationContext());
    }

    // *** Phương thức tạo kênh thông báo ***
    private void createChannelNotification() {
        // Kiểm tra nếu phiên bản Android từ 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo một đối tượng NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN); // Độ ưu tiên thấp nhất
            channel.setSound(null, null); // Tắt âm thanh thông báo

            // Lấy NotificationManager để tạo kênh
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel); // Tạo kênh thông báo
        }
    }

    // *** Các phương thức lấy tham chiếu Firebase Database ***
    public DatabaseReference getCategoryDatabaseReference() {
        return mFirebaseDatabase.getReference("/category"); // Tham chiếu đến "category"
    }

    public DatabaseReference getArtistDatabaseReference() {
        return mFirebaseDatabase.getReference("/artist"); // Tham chiếu đến "artist"
    }

    public DatabaseReference getSongsDatabaseReference() {
        return mFirebaseDatabase.getReference("/songs"); // Tham chiếu đến "songs"
    }

    public DatabaseReference getFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("/feedback"); // Tham chiếu đến "feedback"
    }

    public DatabaseReference getCountViewDatabaseReference(long songId) {
        return FirebaseDatabase.getInstance().getReference("/songs/" + songId + "/count"); // Tham chiếu đến lượt xem của bài hát
    }

    public DatabaseReference getSongDetailDatabaseReference(long songId) {
        return FirebaseDatabase.getInstance().getReference("/songs/" + songId); // Tham chiếu đến chi tiết bài hát
    }
}
