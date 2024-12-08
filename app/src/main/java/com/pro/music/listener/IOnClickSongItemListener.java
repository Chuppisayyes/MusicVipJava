package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Song;
// Import lớp `Song` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnClickSongItemListener ***
// Interface này định nghĩa các phương thức lắng nghe sự kiện liên quan đến bài hát (`Song`) trong danh sách.
public interface IOnClickSongItemListener {

    // *** Phương thức onClickItemSong ***
    // Gọi khi người dùng nhấn vào một bài hát trong danh sách.
    // Tham số:
    // - song: Đối tượng bài hát (Song) mà người dùng đã nhấn vào.
    void onClickItemSong(Song song);

    // *** Phương thức onClickFavoriteSong ***
    // Gọi khi người dùng nhấn vào nút thích hoặc bỏ thích một bài hát.
    // Tham số:
    // - song: Đối tượng bài hát (Song) mà người dùng đã thao tác.
    // - favorite: Giá trị boolean chỉ định hành động:
    //   - `true`: Thêm bài hát vào danh sách yêu thích.
    //   - `false`: Gỡ bài hát khỏi danh sách yêu thích.
    void onClickFavoriteSong(Song song, boolean favorite);

    // *** Phương thức onClickMoreOptions ***
    // Gọi khi người dùng nhấn vào nút "Thêm tùy chọn" của một bài hát.
    // Tham số:
    // - song: Đối tượng bài hát (Song) mà người dùng đã chọn.
    void onClickMoreOptions(Song song);
}
