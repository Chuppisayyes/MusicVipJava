package com.pro.music.constant;

// Lớp chứa các hằng số được sử dụng trong toàn bộ ứng dụng
public class Constant {
    // *** Max count (Số lượng tối đa) ***
    // Số lượng tối đa các banner hiển thị
    public static final int MAX_COUNT_BANNER = 5;
    // Số lượng tối đa các bài hát phổ biến hiển thị
    public static final int MAX_COUNT_POPULAR = 5;
    // Số lượng tối đa các bài hát yêu thích hiển thị
    public static final int MAX_COUNT_FAVORITE = 5;
    // Số lượng tối đa các danh mục hiển thị
    public static final int MAX_COUNT_CATEGORY = 4;
    // Số lượng tối đa các nghệ sĩ hiển thị
    public static final int MAX_COUNT_ARTIST = 6;

    // *** Music actions (Hành động liên quan đến phát nhạc) ***
    // Hành động phát nhạc
    public static final int PLAY = 0;
    // Hành động tạm dừng phát nhạc
    public static final int PAUSE = 1;
    // Hành động chuyển tới bài hát tiếp theo
    public static final int NEXT = 2;
    // Hành động quay lại bài hát trước đó
    public static final int PREVIOUS = 3;
    // Hành động tiếp tục phát nhạc sau khi tạm dừng
    public static final int RESUME = 4;
    // Hành động hủy thông báo liên quan đến phát nhạc
    public static final int CANNEL_NOTIFICATION = 5;

    // Key để truyền hành động nhạc qua Intent
    public static final String MUSIC_ACTION = "musicAction";
    // Key để lưu vị trí bài hát hiện tại
    public static final String SONG_POSITION = "songPosition";
    // Key để lắng nghe sự thay đổi trạng thái của trình phát nhạc
    public static final String CHANGE_LISTENER = "change_listener";

    // *** Key intent (Các khóa dữ liệu truyền qua Intent) ***
    // Key để truyền ID của danh mục
    public static final String CATEGORY_ID = "category_id";
    // Key để truyền ID của nghệ sĩ
    public static final String ARTIST_ID = "artist_id";
    // Key để xác định dữ liệu có được gửi từ menu bên trái hay không
    public static final String IS_FROM_MENU_LEFT = "is_from_menu_left";
    // Key để truyền đối tượng danh mục qua Intent
    public static final String KEY_INTENT_CATEGORY_OBJECT = "category_object";
    // Key để truyền đối tượng nghệ sĩ qua Intent
    public static final String KEY_INTENT_ARTIST_OBJECT = "artist_object";
    // Key để truyền đối tượng bài hát qua Intent
    public static final String KEY_INTENT_SONG_OBJECT = "song_object";

    // *** Admin email format (Định dạng email Admin) ***
    // Định dạng email của Admin (email phải chứa "@admin.com" để xác định tài khoản Admin)
    public static final String ADMIN_EMAIL_FORMAT = "@admin.com";
}
