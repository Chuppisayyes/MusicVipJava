package com.pro.music.listener;
// Định nghĩa package chứa interface. Thư mục này tập trung các interface lắng nghe sự kiện trong ứng dụng.

import com.pro.music.model.Artist;
// Import lớp `Artist` từ package `model` để sử dụng trong các phương thức của interface.

// *** Interface IOnAdminManagerArtistListener ***
// Interface này định nghĩa các phương thức lắng nghe sự kiện liên quan đến việc quản lý nghệ sĩ (`Artist`) của Admin.
public interface IOnAdminManagerArtistListener {

    // *** Phương thức onClickUpdateArtist ***
    // Gọi khi Admin muốn cập nhật thông tin của một nghệ sĩ.
    // Tham số:
    // - artist: Đối tượng nghệ sĩ (Artist) cần được cập nhật.
    void onClickUpdateArtist(Artist artist);

    // *** Phương thức onClickDeleteArtist ***
    // Gọi khi Admin muốn xóa một nghệ sĩ khỏi hệ thống.
    // Tham số:
    // - artist: Đối tượng nghệ sĩ (Artist) cần bị xóa.
    void onClickDeleteArtist(Artist artist);

    // *** Phương thức onClickDetailArtist ***
    // Gọi khi Admin muốn xem chi tiết thông tin của một nghệ sĩ.
    // Tham số:
    // - artist: Đối tượng nghệ sĩ (Artist) mà Admin muốn xem chi tiết.
    void onClickDetailArtist(Artist artist);
}
