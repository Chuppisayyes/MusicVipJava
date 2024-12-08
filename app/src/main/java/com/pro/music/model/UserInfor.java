package com.pro.music.model;

import java.io.Serializable; // Import giao diện Serializable để hỗ trợ tuần tự hóa đối tượng

// Lớp `UserInfor` đại diện cho thông tin cơ bản của một người dùng
// Lớp này được sử dụng trong các trường hợp cần lưu trữ hoặc truyền tải thông tin đơn giản về người dùng
public class UserInfor implements Serializable {

    // *** Thuộc tính của lớp UserInfor ***
    private long id;           // ID của người dùng (dạng số nguyên dài - long)
    private String emailUser;  // Email của người dùng (dạng chuỗi - String)

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `UserInfor` rỗng
    public UserInfor() {}

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `UserInfor` với ID và email
    public UserInfor(long id, String emailUser) {
        this.id = id;               // Gán giá trị ID
        this.emailUser = emailUser; // Gán giá trị email của người dùng
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho ID - Trả về giá trị ID của người dùng
    public long getId() {
        return id;
    }

    // Setter cho ID - Gán giá trị mới cho ID của người dùng
    public void setId(long id) {
        this.id = id;
    }

    // Getter cho email của người dùng - Trả về giá trị email của người dùng
    public String getEmailUser() {
        return emailUser;
    }

    // Setter cho email của người dùng - Gán giá trị mới cho email của người dùng
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}
