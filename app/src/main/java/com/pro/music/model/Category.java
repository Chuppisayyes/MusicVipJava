package com.pro.music.model;

import java.io.Serializable; // Import giao diện Serializable để hỗ trợ tuần tự hóa đối tượng

// Lớp `Category` đại diện cho danh mục trong ứng dụng âm nhạc
// Lớp này được thiết kế để lưu trữ thông tin về danh mục như ID, tên và hình ảnh
public class Category implements Serializable {
    // Thuộc tính ID của danh mục (số nguyên dài - long)
    private long id;
    // Tên của danh mục (chuỗi)
    private String name;
    // URL hoặc đường dẫn hình ảnh đại diện của danh mục
    private String image;

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `Category` rỗng
    public Category() {
    }

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `Category` với đầy đủ giá trị
    public Category(long id, String name, String image) {
        this.id = id;         // Gán giá trị ID
        this.name = name;     // Gán giá trị tên danh mục
        this.image = image;   // Gán giá trị URL hoặc đường dẫn hình ảnh
    }

    // *** Getter cho ID ***
    // Trả về giá trị ID của danh mục
    public long getId() {
        return id;
    }

    // *** Setter cho ID ***
    // Gán giá trị mới cho ID của danh mục
    public void setId(long id) {
        this.id = id;
    }

    // *** Getter cho tên danh mục ***
    // Trả về giá trị tên của danh mục
    public String getName() {
        return name;
    }

    // *** Setter cho tên danh mục ***
    // Gán giá trị mới cho tên của danh mục
    public void setName(String name) {
        this.name = name;
    }

    // *** Getter cho hình ảnh danh mục ***
    // Trả về giá trị hình ảnh đại diện của danh mục
    public String getImage() {
        return image;
    }

    // *** Setter cho hình ảnh danh mục ***
    // Gán giá trị mới cho hình ảnh đại diện của danh mục
    public void setImage(String image) {
        this.image = image;
    }
}
