package com.pro.music.model;

import java.io.Serializable; // Import giao diện Serializable để hỗ trợ tuần tự hóa đối tượng
import java.util.HashMap; // Import HashMap để lưu danh sách người dùng yêu thích

// Lớp `Song` đại diện cho bài hát trong ứng dụng âm nhạc
// Lớp này được sử dụng để lưu trữ thông tin về bài hát, bao gồm ID, tiêu đề, hình ảnh, URL, nghệ sĩ, danh mục, v.v.
public class Song implements Serializable {

    // *** Thuộc tính cơ bản của bài hát ***
    private long id;           // ID của bài hát
    private String title;      // Tiêu đề của bài hát
    private String image;      // URL hoặc đường dẫn hình ảnh bài hát
    private String url;        // URL phát nhạc của bài hát
    private long artistId;     // ID của nghệ sĩ biểu diễn bài hát
    private String artist;     // Tên nghệ sĩ
    private long categoryId;   // ID của danh mục bài hát
    private String category;   // Tên danh mục bài hát
    private boolean featured;  // Đánh dấu bài hát nổi bật
    private int count;         // Số lượt phát bài hát
    private boolean isPlaying; // Trạng thái bài hát đang phát hay không
    private boolean isPriority;// Trạng thái bài hát có ưu tiên phát hay không

    // Danh sách người dùng yêu thích bài hát, lưu dưới dạng HashMap
    // Key là chuỗi (ID của người dùng), Value là thông tin của người dùng (UserInfor)
    private HashMap<String, UserInfor> favorite;

    // *** Constructor mặc định ***
    // Được sử dụng khi cần tạo một đối tượng `Song` rỗng
    public Song() {
    }

    // *** Constructor đầy đủ ***
    // Được sử dụng khi cần khởi tạo đối tượng `Song` với đầy đủ thông tin
    public Song(long id, String title, String image, String url, long artistId, String artist,
                long categoryId, String category, boolean featured) {
        this.id = id;             // Gán giá trị ID
        this.title = title;       // Gán giá trị tiêu đề bài hát
        this.image = image;       // Gán giá trị hình ảnh bài hát
        this.url = url;           // Gán giá trị URL bài hát
        this.artistId = artistId; // Gán ID nghệ sĩ
        this.artist = artist;     // Gán tên nghệ sĩ
        this.categoryId = categoryId; // Gán ID danh mục
        this.category = category;     // Gán tên danh mục
        this.featured = featured;    // Gán trạng thái nổi bật
    }

    // *** Getter và Setter cho các thuộc tính ***
    // Các phương thức này tuân thủ nguyên tắc đóng gói (encapsulation)

    // Getter cho ID
    public long getId() {
        return id;
    }

    // Setter cho ID
    public void setId(long id) {
        this.id = id;
    }

    // Getter cho tiêu đề bài hát
    public String getTitle() {
        return title;
    }

    // Setter cho tiêu đề bài hát
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter cho hình ảnh bài hát
    public String getImage() {
        return image;
    }

    // Setter cho hình ảnh bài hát
    public void setImage(String image) {
        this.image = image;
    }

    // Getter cho URL bài hát
    public String getUrl() {
        return url;
    }

    // Setter cho URL bài hát
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter cho ID nghệ sĩ
    public long getArtistId() {
        return artistId;
    }

    // Setter cho ID nghệ sĩ
    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    // Getter cho tên nghệ sĩ
    public String getArtist() {
        return artist;
    }

    // Setter cho tên nghệ sĩ
    public void setArtist(String artist) {
        this.artist = artist;
    }

    // Getter cho ID danh mục
    public long getCategoryId() {
        return categoryId;
    }

    // Setter cho ID danh mục
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    // Getter cho tên danh mục
    public String getCategory() {
        return category;
    }

    // Setter cho tên danh mục
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter cho trạng thái nổi bật
    public boolean isFeatured() {
        return featured;
    }

    // Setter cho trạng thái nổi bật
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    // Getter cho số lượt phát bài hát
    public int getCount() {
        return count;
    }

    // Setter cho số lượt phát bài hát
    public void setCount(int count) {
        this.count = count;
    }

    // Getter cho trạng thái bài hát đang phát
    public boolean isPlaying() {
        return isPlaying;
    }

    // Setter cho trạng thái bài hát đang phát
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    // Getter cho trạng thái ưu tiên phát
    public boolean isPriority() {
        return isPriority;
    }

    // Setter cho trạng thái ưu tiên phát
    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    // Getter cho danh sách người dùng yêu thích bài hát
    public HashMap<String, UserInfor> getFavorite() {
        return favorite;
    }

    // Setter cho danh sách người dùng yêu thích bài hát
    public void setFavorite(HashMap<String, UserInfor> favorite) {
        this.favorite = favorite;
    }
}
