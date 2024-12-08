package com.pro.music.model;

import java.io.Serializable; // Import giao diện Serializable để hỗ trợ tuần tự hóa đối tượng

// Lớp `SelectObject` đại diện cho một đối tượng chung có thể được chọn
// Lớp này được sử dụng để lưu trữ thông tin cơ bản như ID và tên
public class SelectObject implements Serializable {

    // *** Thuộc tính của lớp SelectObject ***
    private long id;   // ID của đối tượng, kiểu long (dài)
    private String name; // Tên của đối tượng, kiểu chuỗi (String)

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `SelectObject` rỗng
    public SelectObject() {}

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `SelectObject` với ID và tên
    public SelectObject(long id, String name) {
        this.id = id;       // Gán giá trị ID
        this.name = name;   // Gán giá trị tên
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho ID - Trả về giá trị ID của đối tượng
    public long getId() {
        return id;
    }

    // Setter cho ID - Gán giá trị mới cho ID của đối tượng
    public void setId(long id) {
        this.id = id;
    }

    // Getter cho tên - Trả về giá trị tên của đối tượng
    public String getName() {
        return name;
    }

    // Setter cho tên - Gán giá trị mới cho tên của đối tượng
    public void setName(String name) {
        this.name = name;
    }
}
