package com.pro.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.activity.PlayMusicActivity;
import com.pro.music.adapter.SongAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentPopularSongsBinding;
import com.pro.music.listener.IOnClickSongItemListener;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PopularSongsFragment extends Fragment {

    // Biến binding giúp truy cập các thành phần giao diện trong layout.
    private FragmentPopularSongsBinding mFragmentPopularSongsBinding;

    // Danh sách các bài hát sẽ được hiển thị trong RecyclerView.
    private List<Song> mListSong;

    // Adapter quản lý dữ liệu và hiển thị cho RecyclerView.
    private SongAdapter mSongAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Khởi tạo binding để kết nối với layout.
        mFragmentPopularSongsBinding = FragmentPopularSongsBinding.inflate(inflater, container, false);

        // Thiết lập giao diện ban đầu.
        initUi();

        // Đăng ký các sự kiện click.
        initListener();

        // Lấy danh sách bài hát phổ biến từ Firebase.
        getListPopularSongs();

        // Trả về giao diện đã thiết lập.
        return mFragmentPopularSongsBinding.getRoot();
    }

    // Thiết lập RecyclerView và Adapter.
    private void initUi() {
        // Sử dụng LinearLayoutManager để sắp xếp danh sách theo chiều dọc.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentPopularSongsBinding.rcvData.setLayoutManager(linearLayoutManager);

        // Khởi tạo danh sách bài hát trống.
        mListSong = new ArrayList<>();

        // Khởi tạo adapter và xử lý các sự kiện click.
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                // Xử lý khi người dùng chọn bài hát.
                goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
                // Xử lý khi người dùng nhấn yêu thích/hủy yêu thích bài hát.
                GlobalFunction.onClickFavoriteSong(getActivity(), song, favorite);
            }

            @Override
            public void onClickMoreOptions(Song song) {
                // Xử lý khi người dùng nhấn vào menu "More Options".
                GlobalFunction.handleClickMoreOptions(getActivity(), song);
            }
        });

        // Gán adapter cho RecyclerView.
        mFragmentPopularSongsBinding.rcvData.setAdapter(mSongAdapter);
    }

    // Lấy danh sách bài hát phổ biến từ Firebase.
    @SuppressLint("NotifyDataSetChanged")
    private void getListPopularSongs() {
        if (getActivity() == null) return;

        // Lấy tham chiếu đến cơ sở dữ liệu bài hát trong Firebase.
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xóa dữ liệu cũ để chuẩn bị tải dữ liệu mới.
                        resetListData();

                        // Lặp qua tất cả các bài hát trong cơ sở dữ liệu.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class); // Chuyển đổi dữ liệu từ Firebase thành đối tượng Song.
                            if (song == null) return;

                            // Chỉ thêm bài hát có lượt phát (count > 0).
                            if (song.getCount() > 0) {
                                mListSong.add(song);
                            }
                        }

                        // Sắp xếp danh sách bài hát theo lượt phát giảm dần.
                        Collections.sort(mListSong, (song1, song2) -> song2.getCount() - song1.getCount());

                        // Thông báo RecyclerView cập nhật dữ liệu.
                        if (mSongAdapter != null) mSongAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Hiển thị thông báo lỗi nếu không thể lấy dữ liệu.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    // Xóa danh sách cũ để chuẩn bị cập nhật dữ liệu mới.
    private void resetListData() {
        if (mListSong == null) {
            // Nếu danh sách chưa được khởi tạo, khởi tạo mới.
            mListSong = new ArrayList<>();
        } else {
            // Nếu danh sách đã có dữ liệu, xóa tất cả phần tử.
            mListSong.clear();
        }
    }

    // Chuyển đến màn hình phát nhạc với bài hát đã chọn.
    private void goToSongDetail(@NonNull Song song) {
        // Xóa danh sách bài hát đang phát.
        MusicService.clearListSongPlaying();

        // Thêm bài hát được chọn vào danh sách phát.
        MusicService.mListSongPlaying.add(song);

        // Cập nhật trạng thái: chưa phát nhạc.
        MusicService.isPlaying = false;

        // Bắt đầu dịch vụ phát nhạc.
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);

        // Mở màn hình phát nhạc.
        GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
    }

    // Đăng ký sự kiện click vào nút "Play All".
    private void initListener() {
        // Lấy MainActivity để truy cập các thành phần giao diện chính.
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }

        // Đăng ký sự kiện click vào nút "Play All".
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            if (mListSong == null || mListSong.isEmpty()) return;

            // Xóa danh sách bài hát đang phát và thêm toàn bộ danh sách mới.
            MusicService.clearListSongPlaying();
            MusicService.mListSongPlaying.addAll(mListSong);

            // Cập nhật trạng thái: chưa phát nhạc.
            MusicService.isPlaying = false;

            // Bắt đầu dịch vụ phát nhạc.
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);

            // Mở màn hình phát nhạc.
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }
}
