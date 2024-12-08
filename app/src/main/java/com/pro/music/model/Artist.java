package com.pro.music.model;

import java.io.Serializable; // Import giao diện Serializable để hỗ trợ tuần tự hóa đối tượng

// Lớp `Artist` đại diện cho nghệ sĩ trong ứng dụng âm nhạc
// Lớp này được sử dụng để lưu trữ thông tin cơ bản về nghệ sĩ, bao gồm ID, tên và hình ảnh
public class Artist implements Serializable {
    // Thuộc tính ID của nghệ sĩ (dạng số nguyên dài - long)
    private long id;
    // Tên của nghệ sĩ (dạng chuỗi)
    private String name;
    // URL hoặc đường dẫn hình ảnh đại diện của nghệ sĩ
    private String image;

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `Artist` rỗng
    public Artist() {}

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `Artist` với đầy đủ giá trị
    public Artist(long id, String name, String image) {
        this.id = id;         // Gán giá trị ID cho nghệ sĩ
        this.name = name;     // Gán giá trị tên nghệ sĩ
        this.image = image;   // Gán giá trị URL hoặc đường dẫn hình ảnh nghệ sĩ
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho ID - Trả về giá trị ID của nghệ sĩ
    public long getId() {
        return id;
    }

    // Setter cho ID - Gán giá trị mới cho ID của nghệ sĩ
    public void setId(long id) {
        this.id = id;
    }

    // Getter cho tên nghệ sĩ - Trả về tên của nghệ sĩ
    public String getName() {
        return name;
    }

    // Setter cho tên nghệ sĩ - Gán giá trị mới cho tên của nghệ sĩ
    public void setName(String name) {
        this.name = name;
    }

    // Getter cho hình ảnh nghệ sĩ - Trả về URL hoặc đường dẫn hình ảnh nghệ sĩ
    public String getImage() {
        return image;
    }

    // Setter cho hình ảnh nghệ sĩ - Gán giá trị mới cho URL hoặc đường dẫn hình ảnh nghệ sĩ
    public void setImage(String image) {
        this.image = image;
    }
}
