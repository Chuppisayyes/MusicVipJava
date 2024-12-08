package com.pro.music.utils;

// Lớp `StringUtil` chứa các phương thức tiện ích để xử lý chuỗi
public class StringUtil {

    // *** Phương thức kiểm tra chuỗi có rỗng hay không ***
    public static boolean isEmpty(String input) {
        // Trả về true nếu chuỗi:
        // - Là null
        // - Rỗng ("")
        // - Chỉ chứa các khoảng trắng sau khi được cắt bỏ khoảng trắng ở đầu và cuối
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    // *** Phương thức kiểm tra email có hợp lệ hay không ***
    public static boolean isValidEmail(CharSequence target) {
        // Nếu chuỗi email là null, trả về false (không hợp lệ)
        if (target == null)
            return false;

        // Sử dụng `android.util.Patterns.EMAIL_ADDRESS` để kiểm tra email hợp lệ
        // Trả về true nếu email phù hợp với mẫu định dạng email chuẩn
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
