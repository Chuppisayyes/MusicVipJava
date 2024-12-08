package com.pro.music.utils;

import android.widget.ImageView; // Import lớp ImageView để hiển thị hình ảnh

import com.bumptech.glide.Glide; // Import thư viện Glide để tải hình ảnh từ URL
import com.pro.music.R; // Import tài nguyên của ứng dụng (đường dẫn hình ảnh thay thế)

// Lớp `GlideUtils` chứa các phương thức tiện ích để tải và hiển thị hình ảnh từ URL
public class GlideUtils {

    // *** Phương thức tải và hiển thị banner từ URL ***
    public static void loadUrlBanner(String url, ImageView imageView) {
        // Kiểm tra nếu URL rỗng hoặc null
        if (StringUtil.isEmpty(url)) {
            // Hiển thị hình ảnh mặc định nếu URL không hợp lệ
            imageView.setImageResource(R.drawable.img_no_image);
            return; // Kết thúc phương thức
        }
        // Sử dụng Glide để tải hình ảnh từ URL và hiển thị trên ImageView
        Glide.with(imageView.getContext()) // Lấy Context từ ImageView
                .load(url) // URL của hình ảnh cần tải
                .error(R.drawable.img_no_image) // Hình ảnh thay thế nếu xảy ra lỗi tải
                .dontAnimate() // Tắt hiệu ứng chuyển động khi tải ảnh
                .into(imageView); // Gắn hình ảnh vào ImageView
    }

    // *** Phương thức tải và hiển thị hình ảnh từ URL (phổ biến) ***
    public static void loadUrl(String url, ImageView imageView) {
        // Kiểm tra nếu URL rỗng hoặc null
        if (StringUtil.isEmpty(url)) {
            // Hiển thị hình ảnh mặc định nếu URL không hợp lệ
            imageView.setImageResource(R.drawable.image_no_available);
            return; // Kết thúc phương thức
        }
        // Sử dụng Glide để tải hình ảnh từ URL và hiển thị trên ImageView
        Glide.with(imageView.getContext()) // Lấy Context từ ImageView
                .load(url) // URL của hình ảnh cần tải
                .error(R.drawable.image_no_available) // Hình ảnh thay thế nếu xảy ra lỗi tải
                .dontAnimate() // Tắt hiệu ứng chuyển động khi tải ảnh
                .into(imageView); // Gắn hình ảnh vào ImageView
    }
}
