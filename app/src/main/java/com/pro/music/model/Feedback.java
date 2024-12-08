package com.pro.music.model;

// Lớp `Feedback` đại diện cho thông tin phản hồi từ người dùng
// Lớp này được thiết kế để lưu trữ các thông tin liên quan đến phản hồi như tên, số điện thoại, email và nội dung phản hồi
public class Feedback {

    // *** Thuộc tính của lớp Feedback ***
    private String name;    // Tên của người gửi phản hồi
    private String phone;   // Số điện thoại của người gửi phản hồi
    private String email;   // Email của người gửi phản hồi
    private String comment; // Nội dung phản hồi

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `Feedback` rỗng
    public Feedback() {}

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `Feedback` với đầy đủ thông tin
    public Feedback(String name, String phone, String email, String comment) {
        this.name = name;         // Gán giá trị tên
        this.phone = phone;       // Gán giá trị số điện thoại
        this.email = email;       // Gán giá trị email
        this.comment = comment;   // Gán giá trị nội dung phản hồi
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho tên - Trả về giá trị tên của người gửi phản hồi
    public String getName() {
        return name;
    }

    // Setter cho tên - Gán giá trị mới cho tên của người gửi phản hồi
    public void setName(String name) {
        this.name = name;
    }

    // Getter cho số điện thoại - Trả về giá trị số điện thoại của người gửi phản hồi
    public String getPhone() {
        return phone;
    }

    // Setter cho số điện thoại - Gán giá trị mới cho số điện thoại của người gửi phản hồi
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter cho email - Trả về giá trị email của người gửi phản hồi
    public String getEmail() {
        return email;
    }

    // Setter cho email - Gán giá trị mới cho email của người gửi phản hồi
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter cho nội dung phản hồi - Trả về nội dung phản hồi của người gửi
    public String getComment() {
        return comment;
    }

    // Setter cho nội dung phản hồi - Gán giá trị mới cho nội dung phản hồi của người gửi
    public void setComment(String comment) {
        this.comment = comment;
    }
}
