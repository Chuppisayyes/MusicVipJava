package com.pro.music.fragment;
// Package chứa FavoriteFragment.

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thư viện cần thiết cho giao diện và quản lý Fragment.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
// Import các lớp Fragment và LinearLayoutManager cho RecyclerView.

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
// Import các lớp để làm việc với Firebase Realtime Database.

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.activity.PlayMusicActivity;
import com.pro.music.adapter.SongAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentFavoriteBinding;
import com.pro.music.listener.IOnClickSongItemListener;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;
// Import các lớp và thư viện cần thiết cho hoạt động của Fragment.

import java.util.ArrayList;
import java.util.List;
// Import danh sách để quản lý dữ liệu bài hát.

public class FavoriteFragment extends Fragment {
    // Fragment hiển thị danh sách bài hát yêu thích của người dùng.

    private FragmentFavoriteBinding mFragmentFavoriteBinding;
    // View Binding để liên kết với giao diện XML.

    private List<Song> mListSong;
    // Danh sách các bài hát yêu thích.

    private SongAdapter mSongAdapter;
    // Adapter để hiển thị danh sách bài hát.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Khởi tạo Fragment và liên kết giao diện.
        mFragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false);

        initUi();       // Khởi tạo giao diện.
        initListener(); // Khởi tạo sự kiện click.
        getListFavoriteSongs(); // Lấy danh sách bài hát yêu thích từ Firebase.

        return mFragmentFavoriteBinding.getRoot();
        // Trả về root view của Fragment.
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện.

        // Sử dụng LinearLayoutManager để hiển thị danh sách bài hát theo chiều dọc.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentFavoriteBinding.rcvData.setLayoutManager(linearLayoutManager);

        mListSong = new ArrayList<>(); // Khởi tạo danh sách bài hát.
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                // Xử lý khi click vào một bài hát: phát bài hát.
                goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
                // Thêm hoặc xóa bài hát khỏi danh sách yêu thích.
                GlobalFunction.onClickFavoriteSong(getActivity(), song, favorite);
            }

            @Override
            public void onClickMoreOptions(Song song) {
                // Hiển thị thêm các tùy chọn cho bài hát (ví dụ: tải về, thêm vào playlist).
                GlobalFunction.handleClickMoreOptions(getActivity(), song);
            }
        });
        mFragmentFavoriteBinding.rcvData.setAdapter(mSongAdapter);
        // Gán Adapter cho RecyclerView.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListFavoriteSongs() {
        // Lấy danh sách bài hát yêu thích từ Firebase.

        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Khi dữ liệu trong Firebase thay đổi.
                        resetListData(); // Xóa danh sách cũ.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Lặp qua từng bài hát trong Firebase.
                            Song song = dataSnapshot.getValue(Song.class);
                            if (song == null) return;
                            if (GlobalFunction.isFavoriteSong(song)) {
                                // Chỉ thêm bài hát vào danh sách nếu nó nằm trong yêu thích.
                                mListSong.add(0, song);
                            }
                        }
                        if (mSongAdapter != null) mSongAdapter.notifyDataSetChanged();
                        // Cập nhật giao diện sau khi thêm bài hát.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi khi truy vấn Firebase.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    private void resetListData() {
        // Xóa danh sách bài hát cũ trước khi tải dữ liệu mới.
        if (mListSong == null) {
            mListSong = new ArrayList<>();
        } else {
            mListSong.clear();
        }
    }

    private void goToSongDetail(@NonNull Song song) {
        // Chuyển đến màn hình phát bài hát chi tiết.
        MusicService.clearListSongPlaying();
        MusicService.mListSongPlaying.add(song);
        MusicService.isPlaying = false;
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);
        GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
    }

    private void initListener() {
        // Khởi tạo sự kiện click "Play All".
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            // Xử lý khi người dùng muốn phát toàn bộ danh sách bài hát yêu thích.
            if (mListSong == null || mListSong.isEmpty()) return;
            MusicService.clearListSongPlaying();
            MusicService.mListSongPlaying.addAll(mListSong);
            MusicService.isPlaying = false;
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }
}
