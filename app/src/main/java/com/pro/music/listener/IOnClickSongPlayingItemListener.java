package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

// *** Interface IOnClickSongPlayingItemListener ***
// Interface này định nghĩa các phương thức lắng nghe sự kiện liên quan đến bài hát đang phát trong danh sách phát.
public interface IOnClickSongPlayingItemListener {

    // *** Phương thức onClickItemSongPlaying ***
    // Gọi khi người dùng nhấn vào một bài hát trong danh sách phát.
    // Tham số:
    // - position: Vị trí của bài hát trong danh sách phát.
    void onClickItemSongPlaying(int position);

    // *** Phương thức onClickRemoveFromPlaylist ***
    // Gọi khi người dùng muốn xóa một bài hát khỏi danh sách phát.
    // Tham số:
    // - position: Vị trí của bài hát cần xóa trong danh sách phát.
    void onClickRemoveFromPlaylist(int position);
}
