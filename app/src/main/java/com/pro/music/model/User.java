package com.pro.music.model;

import com.google.gson.Gson; // Import thư viện Gson để hỗ trợ chuyển đổi đối tượng sang JSON

// Lớp `User` đại diện cho thông tin người dùng trong ứng dụng
// Lớp này lưu trữ thông tin cơ bản như email, mật khẩu, và quyền quản trị viên
public class User {

    // *** Thuộc tính của lớp User ***
    private String email;    // Email của người dùng
    private String password; // Mật khẩu của người dùng
    private boolean isAdmin; // Cờ xác định người dùng có phải là Admin hay không

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `User` rỗng
    public User() {
    }

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `User` với email và mật khẩu
    public User(String email, String password) {
        this.email = email;         // Gán giá trị email
        this.password = password;   // Gán giá trị mật khẩu
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho email - Trả về email của người dùng
    public String getEmail() {
        return email;
    }

    // Setter cho email - Gán giá trị mới cho email của người dùng
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter cho mật khẩu - Trả về mật khẩu của người dùng
    public String getPassword() {
        return password;
    }

    // Setter cho mật khẩu - Gán giá trị mới cho mật khẩu của người dùng
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter cho quyền Admin - Trả về trạng thái Admin của người dùng
    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter cho quyền Admin - Gán giá trị mới cho quyền Admin của người dùng
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // *** Phương thức chuyển đổi đối tượng `User` thành chuỗi JSON ***
    // Sử dụng thư viện Gson để tạo chuỗi JSON đại diện cho đối tượng `User`
    public String toJSon() {
        Gson gson = new Gson();        // Tạo đối tượng Gson
        return gson.toJson(this);     // Trả về chuỗi JSON của đối tượng `User`
    }
}
