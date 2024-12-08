package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Category;
// Import lớp `Category` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnClickCategoryItemListener ***
// Interface này định nghĩa phương thức lắng nghe sự kiện khi người dùng nhấn vào một danh mục (`Category`) trong danh sách.
public interface IOnClickCategoryItemListener {

    // *** Phương thức onClickItemCategory ***
    // Gọi khi người dùng nhấn vào một danh mục trong danh sách.
    // Tham số:
    // - category: Đối tượng danh mục (Category) mà người dùng đã nhấn vào.
    void onClickItemCategory(Category category);
}
