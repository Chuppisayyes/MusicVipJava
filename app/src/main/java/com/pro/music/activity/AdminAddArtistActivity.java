// File: AdminAddArtistActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.ActivityAdminAddArtistBinding;
import com.pro.music.model.Artist;
import com.pro.music.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

// Lớp đại diện cho chức năng thêm hoặc chỉnh sửa nghệ sĩ
public class AdminAddArtistActivity extends BaseActivity {

    // Khai báo các biến giao diện
    private ActivityAdminAddArtistBinding binding;

    // Cờ xác định chế độ cập nhật (true) hoặc thêm mới (false)
    private boolean isUpdate;

    // Biến lưu trữ thông tin nghệ sĩ cần chỉnh sửa (nếu có)
    private Artist mArtist;

    // Phương thức onCreate - khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddArtistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Cài đặt giao diện chính

        loadDataIntent(); // Lấy dữ liệu Intent nếu có
        initToolbar();    // Cài đặt toolbar
        initView();       // Khởi tạo giao diện

        // Đặt sự kiện cho nút thêm hoặc chỉnh sửa nghệ sĩ
        binding.btnAddOrEdit.setOnClickListener(v -> addOrEditArtist());
    }

    // Phương thức lấy dữ liệu từ Intent, kiểm tra nếu ở chế độ cập nhật
    private void loadDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true; // Chế độ cập nhật
            mArtist = (Artist) bundleReceived.get(Constant.KEY_INTENT_ARTIST_OBJECT); // Lấy đối tượng nghệ sĩ từ Intent
        }
    }

    // Cài đặt toolbar (nút quay lại và tiêu đề)
    private void initToolbar() {
        binding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white); // Đặt icon quay lại
        binding.toolbar.layoutPlayAll.setVisibility(View.GONE); // Ẩn layout không cần thiết
        binding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed()); // Xử lý khi nhấn nút quay lại
    }

    // Khởi tạo giao diện và nội dung ban đầu
    private void initView() {
        if (isUpdate) { // Nếu ở chế độ cập nhật
            binding.toolbar.tvTitle.setText(getString(R.string.label_update_artist)); // Đặt tiêu đề là "Cập nhật nghệ sĩ"
            binding.btnAddOrEdit.setText(getString(R.string.action_edit)); // Nút được đặt thành "Chỉnh sửa"

            // Hiển thị thông tin nghệ sĩ cần chỉnh sửa
            binding.edtName.setText(mArtist.getName());
            binding.edtImage.setText(mArtist.getImage());
        } else { // Nếu ở chế độ thêm mới
            binding.toolbar.tvTitle.setText(getString(R.string.label_add_artist)); // Đặt tiêu đề là "Thêm nghệ sĩ"
            binding.btnAddOrEdit.setText(getString(R.string.action_add)); // Nút được đặt thành "Thêm"
        }
    }

    // Phương thức xử lý logic thêm hoặc chỉnh sửa nghệ sĩ
    private void addOrEditArtist() {
        // Lấy dữ liệu từ các ô nhập liệu
        String strName = binding.edtName.getText().toString().trim();
        String strImage = binding.edtImage.getText().toString().trim();

        // Kiểm tra dữ liệu rỗng
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_require), Toast.LENGTH_SHORT).show(); // Báo lỗi nếu tên rỗng
            return;
        }

        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_require), Toast.LENGTH_SHORT).show(); // Báo lỗi nếu hình ảnh rỗng
            return;
        }

        // Nếu ở chế độ cập nhật
        if (isUpdate) {
            showProgressDialog(true); // Hiển thị dialog chờ
            Map<String, Object> map = new HashMap<>(); // Tạo dữ liệu cập nhật
            map.put("name", strName);
            map.put("image", strImage);

            // Cập nhật dữ liệu trong Firebase
            MyApplication.get(this).getArtistDatabaseReference()
                    .child(String.valueOf(mArtist.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Tắt dialog chờ
                        Toast.makeText(AdminAddArtistActivity.this,
                                getString(R.string.msg_edit_artist_success), Toast.LENGTH_SHORT).show(); // Thông báo thành công
                        GlobalFunction.hideSoftKeyboard(this); // Ẩn bàn phím
                    });
            return;
        }

        // Nếu ở chế độ thêm mới
        showProgressDialog(true); // Hiển thị dialog chờ
        long artistId = System.currentTimeMillis(); // Tạo ID dựa trên thời gian
        Artist artist = new Artist(artistId, strName, strImage); // Tạo đối tượng nghệ sĩ mới

        // Lưu dữ liệu vào Firebase
        MyApplication.get(this).getArtistDatabaseReference()
                .child(String.valueOf(artistId)).setValue(artist, (error, ref) -> {
                    showProgressDialog(false); // Tắt dialog chờ
                    binding.edtName.setText(""); // Xóa nội dung nhập
                    binding.edtImage.setText(""); // Xóa nội dung nhập
                    GlobalFunction.hideSoftKeyboard(this); // Ẩn bàn phím
                    Toast.makeText(this, getString(R.string.msg_add_artist_success), Toast.LENGTH_SHORT).show(); // Thông báo thành công
                });
    }
}
