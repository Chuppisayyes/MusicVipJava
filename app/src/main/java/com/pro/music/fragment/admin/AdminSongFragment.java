package com.pro.music.fragment.admin;
// Định nghĩa package chứa Fragment quản lý bài hát.

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
// Import các thành phần Android cần thiết cho giao diện và xử lý sự kiện.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// Import Fragment và RecyclerView để hiển thị danh sách bài hát.

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
// Import Firebase để lắng nghe thay đổi dữ liệu từ cơ sở dữ liệu.

import com.pro.music.MyApplication;
// Import lớp MyApplication để lấy tham chiếu cơ sở dữ liệu Firebase.

import com.pro.music.R;
import com.pro.music.activity.AdminAddSongActivity;
import com.pro.music.activity.PlayMusicActivity;
// Import các Activity liên quan đến bài hát (thêm, phát nhạc).

import com.pro.music.adapter.AdminSongAdapter;
// Import Adapter quản lý danh sách bài hát.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích.

import com.pro.music.databinding.FragmentAdminSongBinding;
// Import View Binding để liên kết layout với code.

import com.pro.music.listener.IOnAdminManagerSongListener;
// Import interface xử lý sự kiện quản lý bài hát.

import com.pro.music.model.Song;
// Import lớp Song, đại diện cho bài hát.

import com.pro.music.service.MusicService;
// Import dịch vụ phát nhạc.

import com.pro.music.utils.StringUtil;
// Import tiện ích xử lý chuỗi.

import java.util.ArrayList;
import java.util.List;
// Import các danh sách để quản lý bài hát.

// *** Lớp AdminSongFragment ***
// Fragment này hiển thị danh sách bài hát và cung cấp các chức năng quản lý bài hát.
public class AdminSongFragment extends Fragment {

    private FragmentAdminSongBinding binding;
    // Biến `binding` để liên kết với layout.

    private List<Song> mListSong;
    // Danh sách bài hát sẽ được hiển thị trên giao diện.

    private AdminSongAdapter mAdminSongAdapter;
    // Adapter để quản lý và hiển thị danh sách bài hát trong RecyclerView.

    private ChildEventListener mChildEventListener;
    // Listener lắng nghe thay đổi dữ liệu từ Firebase.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện cho Fragment.
        binding = FragmentAdminSongBinding.inflate(inflater, container, false); // Liên kết layout với binding.

        initView(); // Khởi tạo giao diện.
        initListener(); // Gắn các sự kiện cho các thành phần giao diện.
        loadListSong(""); // Tải danh sách bài hát ban đầu (không có từ khóa tìm kiếm).

        return binding.getRoot(); // Trả về root view của Fragment.
    }

    private void initView() {
        // Phương thức khởi tạo RecyclerView và các thành phần liên quan.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvSong.setLayoutManager(linearLayoutManager); // Đặt LayoutManager cho RecyclerView.

        mListSong = new ArrayList<>(); // Khởi tạo danh sách bài hát trống.
        mAdminSongAdapter = new AdminSongAdapter(mListSong, new IOnAdminManagerSongListener() {
            // Khởi tạo adapter và định nghĩa các sự kiện trong interface.
            @Override
            public void onClickUpdateSong(Song song) {
                // Sự kiện chỉnh sửa bài hát.
                onClickEditSong(song);
            }

            @Override
            public void onClickDeleteSong(Song song) {
                // Sự kiện xóa bài hát.
                deleteSongItem(song);
            }

            @Override
            public void onClickDetailSong(Song song) {
                // Sự kiện phát bài hát.
                goToSongDetail(song);
            }
        });

        binding.rcvSong.setAdapter(mAdminSongAdapter); // Gắn adapter vào RecyclerView.

        binding.rcvSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // Ẩn/hiện nút "Thêm bài hát" khi cuộn RecyclerView.
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.btnAddSong.hide(); // Ẩn nút khi cuộn xuống.
                } else {
                    binding.btnAddSong.show(); // Hiện nút khi cuộn lên.
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initListener() {
        // Phương thức gắn sự kiện cho các thành phần giao diện.
        binding.btnAddSong.setOnClickListener(v -> onClickAddSong()); // Sự kiện thêm bài hát.

        binding.imgSearch.setOnClickListener(view1 -> searchSong()); // Sự kiện tìm kiếm bài hát.

        binding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            // Xử lý sự kiện tìm kiếm bằng bàn phím.
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchSong(); // Tìm kiếm bài hát.
                return true;
            }
            return false;
        });

        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            // Lắng nghe khi người dùng nhập vào ô tìm kiếm.
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchSong(); // Tự động tìm kiếm khi xóa hết từ khóa.
                }
            }
        });
    }

    private void goToSongDetail(@NonNull Song song) {
        // Phương thức phát bài hát.
        MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát.
        MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách.
        MusicService.isPlaying = false; // Dừng phát nhạc.
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0); // Bắt đầu phát nhạc.
        GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class); // Chuyển đến Activity phát nhạc.
    }

    private void onClickAddSong() {
        // Chuyển đến Activity thêm bài hát.
        GlobalFunction.startActivity(getActivity(), AdminAddSongActivity.class);
    }

    private void onClickEditSong(Song song) {
        // Chuyển đến Activity chỉnh sửa bài hát.
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_SONG_OBJECT, song);
        GlobalFunction.startActivity(getActivity(), AdminAddSongActivity.class, bundle);
    }

    private void deleteSongItem(Song song) {
        // Hiển thị hộp thoại xác nhận xóa bài hát.
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) return;
                    MyApplication.get(getActivity()).getSongsDatabaseReference()
                            .child(String.valueOf(song.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_song_successfully),
                                            Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchSong() {
        // Tìm kiếm bài hát theo từ khóa.
        String strKey = binding.edtSearchName.getText().toString().trim();
        resetListSong(); // Xóa danh sách bài hát hiện tại.
        if (getActivity() != null) {
            MyApplication.get(getActivity()).getSongsDatabaseReference()
                    .removeEventListener(mChildEventListener); // Xóa listener cũ.
        }
        loadListSong(strKey); // Tải danh sách bài hát mới.
        GlobalFunction.hideSoftKeyboard(getActivity()); // Ẩn bàn phím.
    }

    private void resetListSong() {
        // Xóa danh sách bài hát hiện tại.
        if (mListSong != null) {
            mListSong.clear();
        } else {
            mListSong = new ArrayList<>();
        }
    }

    public void loadListSong(String keyword) {
        // Tải danh sách bài hát từ Firebase.
        if (getActivity() == null) return;
        mChildEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                if (song == null || mListSong == null) return;
                if (StringUtil.isEmpty(keyword)) {
                    mListSong.add(0, song);
                } else {
                    if (GlobalFunction.getTextSearch(song.getTitle()).toLowerCase().trim()
                            .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                        mListSong.add(0, song);
                    }
                }
                if (mAdminSongAdapter != null) mAdminSongAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                if (song == null || mListSong == null || mListSong.isEmpty()) return;
                for (int i = 0; i < mListSong.size(); i++) {
                    if (song.getId() == mListSong.get(i).getId()) {
                        mListSong.set(i, song);
                        break;
                    }
                }
                if (mAdminSongAdapter != null) mAdminSongAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Song song = dataSnapshot.getValue(Song.class);
                if (song == null || mListSong == null || mListSong.isEmpty()) return;
                for (Song songObject : mListSong) {
                    if (song.getId() == songObject.getId()) {
                        mListSong.remove(songObject);
                        break;
                    }
                }
                if (mAdminSongAdapter != null) mAdminSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addChildEventListener(mChildEventListener);
    }
}
