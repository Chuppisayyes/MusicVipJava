package com.pro.music.prefs;

import android.content.Context; // Import lớp Context để tương tác với tài nguyên ứng dụng

import androidx.annotation.Nullable; // Import Nullable để đánh dấu các tham số có thể là null

import com.google.gson.Gson; // Import Gson để hỗ trợ chuyển đổi giữa JSON và đối tượng
import com.pro.music.model.User; // Import lớp User để lưu trữ thông tin người dùng
import com.pro.music.utils.StringUtil; // Import StringUtil để xử lý chuỗi

// Lớp `DataStoreManager` quản lý việc lưu trữ và truy xuất dữ liệu ứng dụng bằng SharedPreferences
public class DataStoreManager {

    // *** Khóa cho thông tin người dùng trong SharedPreferences ***
    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";

    // Biến `instance` để lưu trữ phiên bản duy nhất của `DataStoreManager`
    private static DataStoreManager instance;

    // Biến `sharedPreferences` để làm việc với SharedPreferences
    private MySharedPreferences sharedPreferences;

    // *** Phương thức khởi tạo lớp DataStoreManager ***
    // Phải được gọi một lần khi ứng dụng khởi động
    public static void init(Context context) {
        instance = new DataStoreManager(); // Tạo một đối tượng DataStoreManager
        // Khởi tạo SharedPreferences thông qua lớp MySharedPreferences
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    // *** Lấy đối tượng DataStoreManager ***
    // Nếu `instance` đã được khởi tạo, trả về nó. Ngược lại, ném ngoại lệ.
    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance; // Trả về đối tượng đã khởi tạo
        } else {
            throw new IllegalStateException("Not initialized"); // Ngoại lệ nếu chưa khởi tạo
        }
    }

    // *** Lưu thông tin người dùng ***
    // Nhận vào một đối tượng User, chuyển đổi nó thành JSON và lưu trữ trong SharedPreferences
    public static void setUser(@Nullable User user) {
        String jsonUser = ""; // Khởi tạo chuỗi JSON rỗng
        if (user != null) {
            jsonUser = user.toJSon(); // Chuyển đổi đối tượng User thành chuỗi JSON
        }
        // Lưu chuỗi JSON vào SharedPreferences với khóa PREF_USER_INFOR
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    // *** Lấy thông tin người dùng ***
    // Truy xuất dữ liệu từ SharedPreferences và chuyển đổi thành đối tượng User
    public static User getUser() {
        // Lấy chuỗi JSON từ SharedPreferences
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) { // Kiểm tra chuỗi JSON không rỗng
            // Chuyển đổi chuỗi JSON thành đối tượng User bằng Gson
            return new Gson().fromJson(jsonUser, User.class);
        }
        return new User(); // Trả về một đối tượng User rỗng nếu không có dữ liệu
    }
}
