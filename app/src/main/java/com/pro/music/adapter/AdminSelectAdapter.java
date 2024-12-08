package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter kết nối dữ liệu với các giao diện lựa chọn trong ứng dụng.

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
// Import các thành phần cần thiết để quản lý và hiển thị danh sách tùy chọn.

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// Các annotation hỗ trợ kiểm tra và mô tả tính chất của phương thức hoặc tham số.

import com.pro.music.R;
// Import tài nguyên giao diện của ứng dụng.

import com.pro.music.model.SelectObject;
// Import lớp `SelectObject`, đại diện cho các đối tượng trong danh sách lựa chọn.

import java.util.List;
// Import List để quản lý danh sách lựa chọn.

// *** Lớp AdminSelectAdapter ***
// Adapter này dùng để quản lý danh sách các lựa chọn trong giao diện Admin.
public class AdminSelectAdapter extends ArrayAdapter<SelectObject> {

    private final Context context;
    // Biến lưu trữ context của ứng dụng.

    // *** Constructor ***
    public AdminSelectAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List<SelectObject> list) {
        super(context, resource, list);
        this.context = context; // Gán context cho adapter.
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Phương thức này trả về View hiển thị lựa chọn được chọn (được hiển thị trong ô lựa chọn).
        if (convertView == null) {
            // Nếu convertView chưa được khởi tạo, tạo mới từ layout `item_choose_option`.
            convertView = View.inflate(context, R.layout.item_choose_option, null);
            TextView tvSelected = convertView.findViewById(R.id.tv_selected); // TextView hiển thị lựa chọn.
            tvSelected.setText(this.getItem(position).getName()); // Gắn tên đối tượng được chọn.
        }
        return convertView; // Trả về View đã được thiết lập.
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // Phương thức này trả về View cho từng mục trong danh sách thả xuống.
        View view = View.inflate(context, R.layout.item_drop_down_option, null); // Tạo View từ layout.
        TextView tvName = view.findViewById(R.id.textview_name); // TextView hiển thị tên của mục.
        tvName.setText(this.getItem(position).getName()); // Gắn tên đối tượng tại vị trí `position`.
        return view; // Trả về View đã thiết lập.
    }
}
