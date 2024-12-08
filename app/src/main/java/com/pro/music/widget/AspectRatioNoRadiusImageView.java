package com.pro.music.widget;

import android.annotation.SuppressLint; // Import để tắt cảnh báo từ IDE
import android.content.Context; // Import Context để tương tác với tài nguyên ứng dụng
import android.content.res.TypedArray; // Import để xử lý các thuộc tính tùy chỉnh
import android.util.AttributeSet; // Import AttributeSet để đọc các thuộc tính từ XML

import androidx.appcompat.widget.AppCompatImageView; // Kế thừa từ AppCompatImageView để sử dụng các tính năng của ImageView

import com.pro.music.R; // Import tài nguyên của ứng dụng

// Lớp `AspectRatioNoRadiusImageView` kế thừa từ AppCompatImageView
// Lớp này cho phép hiển thị hình ảnh với tỉ lệ cố định được định nghĩa trong XML hoặc mã Java
public class AspectRatioNoRadiusImageView extends AppCompatImageView {

    // Các hằng số xác định cách đo chiều rộng và chiều cao
    public static final int MEASUREMENT_WIDTH = 0; // Sử dụng chiều rộng làm cơ sở
    public static final int MEASUREMENT_HEIGHT = 1; // Sử dụng chiều cao làm cơ sở

    // Giá trị mặc định cho các thuộc tính tùy chỉnh
    private static final float DEFAULT_ASPECT_RATIO = 1f; // Tỉ lệ khung hình mặc định (1:1)
    private static final boolean DEFAULT_ASPECT_RATIO_ENABLED = false; // Mặc định không kích hoạt tỉ lệ
    private static final int DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH; // Mặc định đo theo chiều rộng

    // Các thuộc tính của lớp
    private float aspectRatio; // Tỉ lệ khung hình
    private boolean aspectRatioEnabled; // Cờ xác định có kích hoạt tỉ lệ không
    private int dominantMeasurement; // Xác định chiều nào (rộng/cao) là cơ sở

    // *** Constructor ***
    public AspectRatioNoRadiusImageView(Context context) {
        this(context, null);
    }

    public AspectRatioNoRadiusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioNoRadiusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs); // Gọi phương thức để đọc các thuộc tính từ XML
    }

    // *** Phương thức đọc thuộc tính tùy chỉnh từ XML ***
    @SuppressLint("CustomViewStyleable") // Tắt cảnh báo về việc sử dụng bộ thuộc tính tùy chỉnh
    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return; // Không làm gì nếu không có thuộc tính
        }
        TypedArray a = null; // Biến để lưu trữ các giá trị thuộc tính
        try {
            // Lấy các thuộc tính tùy chỉnh từ XML
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.AspectRatioView);
            // Đọc giá trị tỉ lệ khung hình
            aspectRatio = a.getFloat(R.styleable.AspectRatioView_aspectRatio, DEFAULT_ASPECT_RATIO);
            // Đọc cờ kích hoạt tỉ lệ
            aspectRatioEnabled = a.getBoolean(R.styleable.AspectRatioView_aspectRatioEnabled,
                    DEFAULT_ASPECT_RATIO_ENABLED);
            // Đọc chiều cơ sở để tính toán
            dominantMeasurement = a.getInt(R.styleable.AspectRatioView_dominantMeasurement,
                    DEFAULT_DOMINANT_MEASUREMENT);
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu xảy ra ngoại lệ
        } finally {
            if (a != null) {
                a.recycle(); // Giải phóng tài nguyên của TypedArray
            }
        }
    }

    // *** Phương thức tính toán kích thước theo tỉ lệ khung hình ***
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // Gọi phương thức đo mặc định
        if (!aspectRatioEnabled) return; // Nếu không kích hoạt tỉ lệ, không làm gì thêm

        int newWidth; // Biến lưu chiều rộng mới
        int newHeight; // Biến lưu chiều cao mới
        switch (dominantMeasurement) {
            case MEASUREMENT_WIDTH:
                // Nếu sử dụng chiều rộng làm cơ sở
                newWidth = getMeasuredWidth();
                newHeight = (int) (newWidth * aspectRatio); // Tính chiều cao dựa trên tỉ lệ
                break;

            case MEASUREMENT_HEIGHT:
                // Nếu sử dụng chiều cao làm cơ sở
                newHeight = getMeasuredHeight();
                newWidth = (int) (newHeight * aspectRatio); // Tính chiều rộng dựa trên tỉ lệ
                break;

            default:
                // Trường hợp không xác định, ném ra ngoại lệ
                throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
        }

        // Gán lại kích thước đo được theo tỉ lệ khung hình
        setMeasuredDimension(newWidth, newHeight);
    }
}
