package com.pro.music.model;

// Lớp `Contact` đại diện cho thông tin liên hệ trong ứng dụng âm nhạc
// Lớp này được thiết kế để lưu trữ các thông tin như loại liên hệ (Facebook, Gmail, v.v.) và hình ảnh tương ứng
public class Contact {

    // *** Các loại liên hệ được định nghĩa dưới dạng hằng số ***
    public static final int FACEBOOK = 0; // Loại liên hệ Facebook
    public static final int HOTLINE = 1;  // Loại liên hệ Hotline
    public static final int GMAIL = 2;    // Loại liên hệ Gmail
    public static final int SKYPE = 3;    // Loại liên hệ Skype
    public static final int YOUTUBE = 4;  // Loại liên hệ YouTube
    public static final int ZALO = 5;     // Loại liên hệ Zalo

    // Thuộc tính ID để định danh loại liên hệ (một trong các giá trị hằng số trên)
    private int id;
    // Thuộc tính hình ảnh đại diện cho loại liên hệ (đại diện bằng `int` - thường là ID của resource)
    private int image;

    // *** Constructor đầy đủ ***
    // Được sử dụng để khởi tạo đối tượng `Contact` với ID và hình ảnh
    public Contact(int id, int image) {
        this.id = id;       // Gán giá trị ID liên hệ
        this.image = image; // Gán giá trị hình ảnh đại diện
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho ID - Trả về giá trị ID của liên hệ
    public int getId() {
        return id;
    }

    // Setter cho ID - Gán giá trị mới cho ID của liên hệ
    public void setId(int id) {
        this.id = id;
    }

    // Getter cho hình ảnh - Trả về giá trị hình ảnh của liên hệ
    public int getImage() {
        return image;
    }

    // Setter cho hình ảnh - Gán giá trị mới cho hình ảnh của liên hệ
    public void setImage(int image) {
        this.image = image;
    }
}
