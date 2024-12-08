package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Artist;
// Import lớp `Artist` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnClickArtistItemListener ***
// Interface này định nghĩa phương thức lắng nghe sự kiện khi người dùng nhấn vào một nghệ sĩ (`Artist`) trong danh sách.
public interface IOnClickArtistItemListener {

    // *** Phương thức onClickItemArtist ***
    // Gọi khi người dùng nhấn vào một nghệ sĩ trong danh sách.
    // Tham số:
    // - artist: Đối tượng nghệ sĩ (Artist) mà người dùng đã nhấn vào.
    void onClickItemArtist(Artist artist);
}
