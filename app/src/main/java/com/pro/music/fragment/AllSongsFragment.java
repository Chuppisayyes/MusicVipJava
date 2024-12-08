package com.pro.music.fragment;
// Định nghĩa package chứa lớp Fragment.

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thành phần Android cơ bản để xử lý giao diện.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
// Import các thành phần Fragment và RecyclerView để hiển thị danh sách.

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.activity.PlayMusicActivity;
// Import các Activity liên quan đến bài hát (phát nhạc, giao diện chính).

import com.pro.music.adapter.SongAdapter;
// Import Adapter quản lý danh sách bài hát.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích.

import com.pro.music.databinding.FragmentAllSongsBinding;
// Import View Binding để liên kết layout với code.

import com.pro.music.listener.IOnClickSongItemListener;
// Import interface để xử lý sự kiện click vào bài hát.

import com.pro.music.model.Song;
// Import lớp Song đại diện cho bài hát.

import com.pro.music.service.MusicService;
// Import dịch vụ phát nhạc.

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
// Import Firebase để lấy dữ liệu danh sách bài hát từ cơ sở dữ liệu.

import java.util.ArrayList;
import java.util.List;
// Import các danh sách để quản lý dữ liệu bài hát.

// *** Lớp AllSongsFragment ***
// Fragment này hiển thị danh sách tất cả bài hát và cung cấp các chức năng phát nhạc.
public class AllSongsFragment extends Fragment {

    private FragmentAllSongsBinding mFragmentAllSongsBinding;
    // Biến `binding` để liên kết layout với Fragment.

    private List<Song> mListSong;
    // Danh sách bài hát được hiển thị trên giao diện.

    private SongAdapter mSongAdapter;
    // Adapter để quản lý và hiển thị danh sách bài hát.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện của Fragment.
        mFragmentAllSongsBinding = FragmentAllSongsBinding.inflate(inflater, container, false);
        // Liên kết layout với binding.

        initUi(); // Khởi tạo giao diện.
        initListener(); // Gắn các sự kiện cho các thành phần giao diện.
        getListAllSongs(); // Lấy danh sách tất cả bài hát từ cơ sở dữ liệu.

        return mFragmentAllSongsBinding.getRoot(); // Trả về root view của Fragment.
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện RecyclerView.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAllSongsBinding.rcvData.setLayoutManager(linearLayoutManager);
        // Đặt LayoutManager cho RecyclerView để hiển thị danh sách theo dạng dọc.

        mListSong = new ArrayList<>(); // Khởi tạo danh sách bài hát rỗng.
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            // Khởi tạo adapter và định nghĩa các sự kiện trong interface.
            @Override
            public void onClickItemSong(Song song) {
                // Sự kiện khi người dùng click vào một bài hát -> mở chi tiết bài hát.
                goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
                // Sự kiện thêm/xóa bài hát khỏi danh sách yêu thích.
                GlobalFunction.onClickFavoriteSong(getActivity(), song, favorite);
            }

            @Override
            public void onClickMoreOptions(Song song) {
                // Sự kiện hiển thị các tùy chọn khác (ví dụ: tải xuống bài hát).
                GlobalFunction.handleClickMoreOptions(getActivity(), song);
            }
        });
        mFragmentAllSongsBinding.rcvData.setAdapter(mSongAdapter); // Gắn adapter vào RecyclerView.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListAllSongs() {
        // Phương thức lấy danh sách tất cả bài hát từ Firebase.
        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xử lý khi dữ liệu trong cơ sở dữ liệu thay đổi.
                        resetListData(); // Xóa danh sách cũ.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class);
                            if (song == null) return;
                            mListSong.add(0, song); // Thêm bài hát vào danh sách.
                        }
                        if (mSongAdapter != null) mSongAdapter.notifyDataSetChanged();
                        // Cập nhật giao diện hiển thị danh sách bài hát.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi xảy ra lỗi trong quá trình lấy dữ liệu.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    private void resetListData() {
        // Xóa danh sách bài hát hiện tại để tải dữ liệu mới.
        if (mListSong == null) {
            mListSong = new ArrayList<>();
        } else {
            mListSong.clear();
        }
    }

    private void goToSongDetail(@NonNull Song song) {
        // Phương thức mở chi tiết bài hát và bắt đầu phát nhạc.
        MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát.
        MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách.
        MusicService.isPlaying = false; // Dừng phát nhạc.
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0); // Gửi yêu cầu phát nhạc.
        GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class); // Chuyển đến Activity phát nhạc.
    }

    private void initListener() {
        // Phương thức gắn sự kiện cho các thành phần giao diện.
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            // Sự kiện phát toàn bộ danh sách bài hát.
            if (mListSong == null || mListSong.isEmpty()) return;
            MusicService.clearListSongPlaying(); // Xóa danh sách bài hát đang phát.
            MusicService.mListSongPlaying.addAll(mListSong); // Thêm toàn bộ bài hát vào danh sách.
            MusicService.isPlaying = false; // Dừng phát nhạc.
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0); // Gửi yêu cầu phát nhạc.
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class); // Chuyển đến Activity phát nhạc.
        });
    }
}
