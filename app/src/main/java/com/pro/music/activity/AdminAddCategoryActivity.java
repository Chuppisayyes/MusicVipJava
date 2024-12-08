// File: AdminAddCategoryActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Các thư viện cần thiết được import
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.ActivityAdminAddCategoryBinding;
import com.pro.music.model.Category;
import com.pro.music.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

// Lớp hoạt động cho việc thêm/chỉnh sửa danh mục
public class AdminAddCategoryActivity extends BaseActivity {

    // Biến lưu giữ các thành phần giao diện
    private ActivityAdminAddCategoryBinding binding;

    // Cờ xác định chế độ cập nhật (true) hoặc thêm mới (false)
    private boolean isUpdate;

    // Biến lưu trữ thông tin danh mục nếu đang ở chế độ cập nhật
    private Category mCategory;

    // Phương thức lifecycle onCreate, khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện chính

        loadDataIntent(); // Lấy dữ liệu từ Intent (nếu có)
        initToolbar();    // Cài đặt thanh toolbar
        initView();       // Khởi tạo giao diện và trạng thái ban đầu

        // Đặt sự kiện cho nút thêm hoặc chỉnh sửa danh mục
        binding.btnAddOrEdit.setOnClickListener(v -> addOrEditCategory());
    }

    // Phương thức lấy dữ liệu Intent, kiểm tra nếu là chế độ cập nhật
    private void loadDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true; // Chuyển sang chế độ cập nhật
            mCategory = (Category) bundleReceived.get(Constant.KEY_INTENT_CATEGORY_OBJECT);
        }
    }

    // Cài đặt toolbar: nút quay lại và tiêu đề
    private void initToolbar() {
        binding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white); // Đặt icon quay lại
        binding.toolbar.layoutPlayAll.setVisibility(View.GONE); // Ẩn layout không cần thiết
        binding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed()); // Xử lý khi nhấn nút quay lại
    }

    // Khởi tạo giao diện và nội dung cho chế độ thêm hoặc cập nhật
    private void initView() {
        if (isUpdate) { // Nếu đang ở chế độ cập nhật
            binding.toolbar.tvTitle.setText(getString(R.string.label_update_category)); // Đặt tiêu đề
            binding.btnAddOrEdit.setText(getString(R.string.action_edit)); // Đổi nút sang "Chỉnh sửa"

            // Hiển thị thông tin danh mục cần chỉnh sửa
            binding.edtName.setText(mCategory.getName());
            binding.edtImage.setText(mCategory.getImage());
        } else { // Nếu ở chế độ thêm mới
            binding.toolbar.tvTitle.setText(getString(R.string.label_add_category));
            binding.btnAddOrEdit.setText(getString(R.string.action_add)); // Đổi nút sang "Thêm"
        }
    }

    // Phương thức thêm hoặc chỉnh sửa danh mục
    private void addOrEditCategory() {
        // Lấy thông tin từ các ô nhập liệu
        String strName = binding.edtName.getText().toString().trim();
        String strImage = binding.edtImage.getText().toString().trim();

        // Kiểm tra nếu tên danh mục rỗng
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra nếu URL hình ảnh rỗng
        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu là chế độ cập nhật
        if (isUpdate) {
            showProgressDialog(true); // Hiển thị dialog chờ
            Map<String, Object> map = new HashMap<>(); // Chuẩn bị dữ liệu cập nhật
            map.put("name", strName);
            map.put("image", strImage);

            // Thực hiện cập nhật trong cơ sở dữ liệu Firebase
            MyApplication.get(this).getCategoryDatabaseReference()
                    .child(String.valueOf(mCategory.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Tắt dialog chờ
                        Toast.makeText(AdminAddCategoryActivity.this,
                                getString(R.string.msg_edit_category_success), Toast.LENGTH_SHORT).show();
                        GlobalFunction.hideSoftKeyboard(this); // Ẩn bàn phím
                    });
            return;
        }

        // Nếu là chế độ thêm mới
        showProgressDialog(true); // Hiển thị dialog chờ
        long categoryId = System.currentTimeMillis(); // Tạo ID mới dựa trên thời gian
        Category category = new Category(categoryId, strName, strImage); // Tạo đối tượng danh mục mới
        MyApplication.get(this).getCategoryDatabaseReference()
                .child(String.valueOf(categoryId)).setValue(category, (error, ref) -> {
                    showProgressDialog(false); // Tắt dialog chờ
                    // Xóa dữ liệu trong các ô nhập liệu
                    binding.edtName.setText("");
                    binding.edtImage.setText("");
                    GlobalFunction.hideSoftKeyboard(this); // Ẩn bàn phím
                    Toast.makeText(this, getString(R.string.msg_add_category_success), Toast.LENGTH_SHORT).show();
                });
    }
}
