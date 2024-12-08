package com.pro.music.prefs;

import android.content.Context; // Import Context để truy cập các tài nguyên và dịch vụ ứng dụng

// Lớp `MySharedPreferences` hỗ trợ lưu trữ và truy xuất dữ liệu cục bộ bằng `SharedPreferences`
public class MySharedPreferences {

    // Tên của tệp SharedPreferences
    private static final String MOVIE_PRO_PREFERENCES = "MOVIE_PRO_PREFERENCES";

    // Biến mContext lưu tham chiếu tới Context của ứng dụng
    private final Context mContext;

    // *** Constructor ***
    // Khởi tạo lớp với một đối tượng Context
    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    // *** Phương thức lưu giá trị kiểu long ***
    public void putLongValue(String key, long n) {
        // Truy xuất SharedPreferences
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        // Tạo Editor để chỉnh sửa SharedPreferences
        android.content.SharedPreferences.Editor editor = pref.edit();
        // Lưu giá trị long vào SharedPreferences với key tương ứng
        editor.putLong(key, n);
        editor.apply(); // Áp dụng thay đổi (không đồng bộ)
    }

    // *** Phương thức lấy giá trị kiểu long ***
    public long getLongValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        // Lấy giá trị kiểu long với key. Trả về 0 nếu không tồn tại
        return pref.getLong(key, 0);
    }

    // *** Phương thức lưu giá trị kiểu int ***
    public void putIntValue(String key, int n) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n); // Lưu giá trị int
        editor.apply();
    }

    // *** Phương thức lấy giá trị kiểu int ***
    public int getIntValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        return pref.getInt(key, 0); // Trả về giá trị int hoặc 0 nếu không tồn tại
    }

    // *** Phương thức lưu giá trị kiểu String ***
    public void putStringValue(String key, String s) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s); // Lưu giá trị String
        editor.apply();
    }

    // *** Phương thức lấy giá trị kiểu String ***
    public String getStringValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        return pref.getString(key, ""); // Trả về String hoặc chuỗi rỗng nếu không tồn tại
    }

    // *** Phương thức lấy giá trị kiểu String với giá trị mặc định ***
    public String getStringValue(String key, String defaultValue) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        return pref.getString(key, defaultValue); // Trả về giá trị mặc định nếu không tồn tại
    }

    // *** Phương thức lưu giá trị kiểu boolean ***
    public void putBooleanValue(String key, Boolean b) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b); // Lưu giá trị boolean
        editor.apply();
    }

    // *** Phương thức lấy giá trị kiểu boolean ***
    public boolean getBooleanValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        return pref.getBoolean(key, false); // Trả về giá trị false nếu không tồn tại
    }

    // *** Phương thức lưu giá trị kiểu float ***
    public void putFloatValue(String key, float f) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f); // Lưu giá trị float
        editor.apply();
    }

    // *** Phương thức lấy giá trị kiểu float ***
    public float getFloatValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                MOVIE_PRO_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f); // Trả về giá trị 0.0 nếu không tồn tại
    }
}
