package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Category;
// Import lớp `Category` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnAdminManagerCategoryListener ***
// Interface này định nghĩa các phương thức lắng nghe sự kiện liên quan đến việc quản lý danh mục (`Category`) của Admin.
public interface IOnAdminManagerCategoryListener {

    // *** Phương thức onClickUpdateCategory ***
    // Gọi khi Admin muốn cập nhật thông tin của một danh mục.
    // Tham số:
    // - category: Đối tượng danh mục (Category) cần được cập nhật.
    void onClickUpdateCategory(Category category);

    // *** Phương thức onClickDeleteCategory ***
    // Gọi khi Admin muốn xóa một danh mục khỏi hệ thống.
    // Tham số:
    // - category: Đối tượng danh mục (Category) cần bị xóa.
    void onClickDeleteCategory(Category category);

    // *** Phương thức onClickDetailCategory ***
    // Gọi khi Admin muốn xem chi tiết thông tin của một danh mục.
    // Tham số:
    // - category: Đối tượng danh mục (Category) mà Admin muốn xem chi tiết.
    void onClickDetailCategory(Category category);
}
