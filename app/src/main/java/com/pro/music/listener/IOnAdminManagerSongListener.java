package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Song;
// Import lớp `Song` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnAdminManagerSongListener ***
// Interface này định nghĩa các phương thức lắng nghe sự kiện liên quan đến việc quản lý bài hát (`Song`) của Admin.
public interface IOnAdminManagerSongListener {

    // *** Phương thức onClickUpdateSong ***
    // Gọi khi Admin muốn cập nhật thông tin của một bài hát.
    // Tham số:
    // - song: Đối tượng bài hát (Song) cần được cập nhật.
    void onClickUpdateSong(Song song);

    // *** Phương thức onClickDeleteSong ***
    // Gọi khi Admin muốn xóa một bài hát khỏi hệ thống.
    // Tham số:
    // - song: Đối tượng bài hát (Song) cần bị xóa.
    void onClickDeleteSong(Song song);

    // *** Phương thức onClickDetailSong ***
    // Gọi khi Admin muốn xem chi tiết thông tin của một bài hát.
    // Tham số:
    // - song: Đối tượng bài hát (Song) mà Admin muốn xem chi tiết.
    void onClickDetailSong(Song song);
}
