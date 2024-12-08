// File: AdminAddSongActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.adapter.AdminSelectAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.ActivityAdminAddSongBinding;
import com.pro.music.model.Artist;
import com.pro.music.model.Category;
import com.pro.music.model.SelectObject;
import com.pro.music.model.Song;
import com.pro.music.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Lớp xử lý thêm hoặc chỉnh sửa bài hát
public class AdminAddSongActivity extends BaseActivity {

    // Biến giao diện binding
    private ActivityAdminAddSongBinding binding;

    // Cờ kiểm tra chế độ cập nhật (true) hoặc thêm mới (false)
    private boolean isUpdate;

    // Đối tượng bài hát cần chỉnh sửa
    private Song mSong;

    // Các đối tượng lựa chọn danh mục và nghệ sĩ
    private SelectObject mCategorySelected;
    private SelectObject mArtistSelected;

    // Phương thức onCreate khởi tạo giao diện và logic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện

        loadDataIntent(); // Lấy dữ liệu từ Intent nếu có
        initToolbar();    // Cài đặt thanh toolbar
        initView();       // Khởi tạo giao diện và trạng thái ban đầu

        // Xử lý sự kiện khi nhấn nút Thêm hoặc Sửa
        binding.btnAddOrEdit.setOnClickListener(v -> addOrEditSong());
    }

    // Lấy dữ liệu từ Intent, kiểm tra nếu là chế độ cập nhật
    private void loadDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true; // Chế độ cập nhật
            mSong = (Song) bundleReceived.get(Constant.KEY_INTENT_SONG_OBJECT); // Lấy bài hát từ Intent
        }
    }

    // Cài đặt toolbar
    private void initToolbar() {
        binding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white); // Đặt icon quay lại
        binding.toolbar.layoutPlayAll.setVisibility(View.GONE); // Ẩn layout không cần thiết
        binding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed()); // Xử lý khi nhấn nút quay lại
    }

    // Khởi tạo giao diện và nội dung ban đầu
    private void initView() {
        if (isUpdate) { // Nếu ở chế độ cập nhật
            binding.toolbar.tvTitle.setText(getString(R.string.label_update_song)); // Đặt tiêu đề là "Cập nhật bài hát"
            binding.btnAddOrEdit.setText(getString(R.string.action_edit)); // Nút là "Chỉnh sửa"

            // Hiển thị dữ liệu bài hát cần chỉnh sửa
            binding.edtName.setText(mSong.getTitle());
            binding.edtImage.setText(mSong.getImage());
            binding.edtLink.setText(mSong.getUrl());
            binding.chbFeatured.setChecked(mSong.isFeatured());
        } else { // Nếu ở chế độ thêm mới
            binding.toolbar.tvTitle.setText(getString(R.string.label_add_song)); // Đặt tiêu đề là "Thêm bài hát"
            binding.btnAddOrEdit.setText(getString(R.string.action_add)); // Nút là "Thêm"
        }

        loadListCategory(); // Tải danh sách danh mục
        loadListArtist();   // Tải danh sách nghệ sĩ
    }

    // Tải danh sách danh mục từ Firebase
    private void loadListCategory() {
        MyApplication.get(this).getCategoryDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<SelectObject> list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Category category = dataSnapshot.getValue(Category.class); // Lấy từng danh mục
                            if (category == null) return;
                            list.add(0, new SelectObject(category.getId(), category.getName()));
                        }
                        // Cài đặt adapter cho Spinner
                        AdminSelectAdapter adapter = new AdminSelectAdapter(AdminAddSongActivity.this,
                                R.layout.item_choose_option, list);
                        binding.spnCategory.setAdapter(adapter);
                        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mCategorySelected = adapter.getItem(position); // Lấy danh mục được chọn
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                        // Đặt lựa chọn nếu ở chế độ cập nhật
                        if (mSong != null && mSong.getCategoryId() > 0) {
                            binding.spnCategory.setSelection(getPositionSelected(list, mSong.getCategoryId()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    // Tải danh sách nghệ sĩ từ Firebase
    private void loadListArtist() {
        MyApplication.get(this).getArtistDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<SelectObject> list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Artist artist = dataSnapshot.getValue(Artist.class); // Lấy từng nghệ sĩ
                            if (artist == null) return;
                            list.add(0, new SelectObject(artist.getId(), artist.getName()));
                        }
                        // Cài đặt adapter cho Spinner
                        AdminSelectAdapter adapter = new AdminSelectAdapter(AdminAddSongActivity.this,
                                R.layout.item_choose_option, list);
                        binding.spnArtist.setAdapter(adapter);
                        binding.spnArtist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mArtistSelected = adapter.getItem(position); // Lấy nghệ sĩ được chọn
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                        // Đặt lựa chọn nếu ở chế độ cập nhật
                        if (mSong != null && mSong.getArtistId() > 0) {
                            binding.spnArtist.setSelection(getPositionSelected(list, mSong.getArtistId()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    // Tìm vị trí lựa chọn trong danh sách dựa trên ID
    private int getPositionSelected(List<SelectObject> list, long id) {
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    // Thêm hoặc chỉnh sửa bài hát
    private void addOrEditSong() {
        // Lấy dữ liệu từ giao diện
        String strName = binding.edtName.getText().toString().trim();
        String strImage = binding.edtImage.getText().toString().trim();
        String strUrl = binding.edtLink.getText().toString().trim();

        // Kiểm tra dữ liệu nhập
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_require), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_require), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(strUrl)) {
            Toast.makeText(this, getString(R.string.msg_url_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu ở chế độ cập nhật
        if (isUpdate) {
            showProgressDialog(true); // Hiển thị dialog chờ
            Map<String, Object> map = new HashMap<>();
            map.put("title", strName);
            map.put("image", strImage);
            map.put("url", strUrl);
            map.put("featured", binding.chbFeatured.isChecked());
            map.put("categoryId", mCategorySelected.getId());
            map.put("category", mCategorySelected.getName());
            map.put("artistId", mArtistSelected.getId());
            map.put("artist", mArtistSelected.getName());

            // Cập nhật Firebase
            MyApplication.get(this).getSongsDatabaseReference()
                    .child(String.valueOf(mSong.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false); // Ẩn dialog
                        Toast.makeText(AdminAddSongActivity.this,
                                getString(R.string.msg_edit_song_success), Toast.LENGTH_SHORT).show();
                        GlobalFunction.hideSoftKeyboard(this);
                    });
            return;
        }

        // Nếu ở chế độ thêm mới
        showProgressDialog(true); // Hiển thị dialog chờ
        long songId = System.currentTimeMillis(); // Tạo ID dựa trên thời gian
        Song song = new Song(songId, strName, strImage, strUrl, mArtistSelected.getId(),
                mArtistSelected.getName(), mCategorySelected.getId(), mCategorySelected.getName(),
                binding.chbFeatured.isChecked());

        // Lưu bài hát mới vào Firebase
        MyApplication.get(this).getSongsDatabaseReference()
                .child(String.valueOf(songId)).setValue(song, (error, ref) -> {
                    showProgressDialog(false); // Ẩn dialog chờ
                    binding.edtName.setText(""); // Reset giao diện
                    binding.edtImage.setText("");
                    binding.edtLink.setText("");
                    binding.chbFeatured.setChecked(false);
                    binding.spnCategory.setSelection(0);
                    binding.spnArtist.setSelection(0);
                    GlobalFunction.hideSoftKeyboard(this); // Ẩn bàn phím
                    Toast.makeText(this, getString(R.string.msg_add_song_success), Toast.LENGTH_SHORT).show();
                });
    }
}
