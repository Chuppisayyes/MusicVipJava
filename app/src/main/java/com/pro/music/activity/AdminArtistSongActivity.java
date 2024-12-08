// File: AdminArtistSongActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.adapter.AdminSongAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.ActivityAdminArtistSongBinding;
import com.pro.music.listener.IOnAdminManagerSongListener;
import com.pro.music.model.Artist;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;

import java.util.ArrayList;
import java.util.List;

// Lớp quản lý danh sách bài hát của một nghệ sĩ trong phần quản trị
public class AdminArtistSongActivity extends BaseActivity {

    // Biến binding để truy cập các thành phần trong layout
    private ActivityAdminArtistSongBinding binding;

    // Danh sách bài hát
    private List<Song> mListSong;

    // Adapter cho RecyclerView
    private AdminSongAdapter mAdminSongAdapter;

    // Nghệ sĩ hiện tại
    private Artist mArtist;

    // Phương thức onCreate khởi tạo hoạt động
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminArtistSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện

        loadDataIntent(); // Lấy dữ liệu từ Intent
        initToolbar();    // Khởi tạo thanh công cụ
        initView();       // Khởi tạo giao diện
        loadListSong();   // Tải danh sách bài hát
    }

    // Phương thức lấy dữ liệu từ Intent
    private void loadDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mArtist = (Artist) bundleReceived.get(Constant.KEY_INTENT_ARTIST_OBJECT); // Lấy đối tượng nghệ sĩ
        }
    }

    // Khởi tạo thanh công cụ (toolbar)
    private void initToolbar() {
        binding.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white); // Đặt icon quay lại
        binding.toolbar.layoutPlayAll.setVisibility(View.GONE); // Ẩn layout không cần thiết
        binding.toolbar.imgLeft.setOnClickListener(v -> onBackPressed()); // Xử lý khi nhấn nút quay lại
        binding.toolbar.tvTitle.setText(mArtist.getName()); // Đặt tiêu đề là tên nghệ sĩ
    }

    // Khởi tạo giao diện
    private void initView() {
        // Thiết lập layout cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvSong.setLayoutManager(linearLayoutManager);

        mListSong = new ArrayList<>(); // Khởi tạo danh sách bài hát

        // Tạo adapter và xử lý sự kiện cho từng bài hát
        mAdminSongAdapter = new AdminSongAdapter(mListSong, new IOnAdminManagerSongListener() {
            @Override
            public void onClickUpdateSong(Song song) {
                onClickEditSong(song); // Chỉnh sửa bài hát
            }

            @Override
            public void onClickDeleteSong(Song song) {
                deleteSongItem(song); // Xóa bài hát
            }

            @Override
            public void onClickDetailSong(Song song) {
                goToSongDetail(song); // Xem chi tiết bài hát
            }
        });

        binding.rcvSong.setAdapter(mAdminSongAdapter); // Gắn adapter cho RecyclerView
    }

    // Chuyển đến chi tiết bài hát và phát nhạc
    private void goToSongDetail(@NonNull Song song) {
        MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát
        MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách phát
        MusicService.isPlaying = false; // Đặt trạng thái không phát
        GlobalFunction.startMusicService(this, Constant.PLAY, 0); // Bắt đầu dịch vụ phát nhạc
        GlobalFunction.startActivity(this, PlayMusicActivity.class); // Mở Activity phát nhạc
    }

    // Chuyển đến màn hình chỉnh sửa bài hát
    private void onClickEditSong(Song song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_SONG_OBJECT, song); // Truyền đối tượng bài hát
        GlobalFunction.startActivity(this, AdminAddSongActivity.class, bundle); // Mở Activity thêm/chỉnh sửa bài hát
    }

    // Xóa bài hát khỏi danh sách và cơ sở dữ liệu
    private void deleteSongItem(Song song) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_delete_title)) // Tiêu đề thông báo
                .setMessage(getString(R.string.msg_confirm_delete)) // Nội dung xác nhận xóa
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i)
                        -> MyApplication.get(this).getSongsDatabaseReference()
                        .child(String.valueOf(song.getId())).removeValue((error, ref) ->
                                Toast.makeText(this,
                                        getString(R.string.msg_delete_song_successfully),
                                        Toast.LENGTH_SHORT).show())) // Thực hiện xóa và thông báo
                .setNegativeButton(getString(R.string.action_cancel), null) // Nút hủy
                .show(); // Hiển thị dialog
    }

    // Đặt lại danh sách bài hát
    private void resetListSong() {
        if (mListSong != null) {
            mListSong.clear(); // Xóa danh sách nếu không null
        } else {
            mListSong = new ArrayList<>(); // Tạo mới danh sách nếu null
        }
    }

    // Tải danh sách bài hát của nghệ sĩ từ Firebase
    @SuppressLint("NotifyDataSetChanged")
    public void loadListSong() {
        MyApplication.get(this).getSongsDatabaseReference()
                .orderByChild("artistId").equalTo(mArtist.getId()) // Lọc bài hát theo artistId
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        resetListSong(); // Đặt lại danh sách
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class); // Lấy bài hát từ snapshot
                            if (song == null) return;
                            mListSong.add(0, song); // Thêm bài hát vào đầu danh sách
                        }
                        if (mAdminSongAdapter != null) mAdminSongAdapter.notifyDataSetChanged(); // Cập nhật giao diện
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi
                    }
                });
    }
}
