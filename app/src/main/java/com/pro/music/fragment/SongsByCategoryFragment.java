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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.activity.PlayMusicActivity;
import com.pro.music.adapter.SongAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentSongsByCategoryBinding;
import com.pro.music.listener.IOnClickSongItemListener;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class SongsByCategoryFragment extends Fragment {

    // ViewBinding giúp truy cập trực tiếp các thành phần giao diện từ layout.
    private FragmentSongsByCategoryBinding mFragmentSongsByCategoryBinding;

    // Danh sách bài hát thuộc một danh mục.
    private List<Song> mListSong;

    // Adapter để hiển thị danh sách bài hát.
    private SongAdapter mSongAdapter;

    // ID của danh mục, được truyền vào từ Activity.
    private long mCategoryId;

    // Tạo một instance mới của fragment với tham số categoryId.
    public static SongsByCategoryFragment newInstance(long categoryId) {
        SongsByCategoryFragment fragment = new SongsByCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.CATEGORY_ID, categoryId); // Truyền categoryId qua bundle.
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Khởi tạo ViewBinding để liên kết giao diện.
        mFragmentSongsByCategoryBinding = FragmentSongsByCategoryBinding.inflate(inflater, container, false);

        // Lấy dữ liệu categoryId từ intent/bundle.
        getDataIntent();

        // Thiết lập giao diện ban đầu.
        initUi();

        // Đăng ký sự kiện người dùng.
        initListener();

        // Lấy danh sách bài hát theo danh mục từ Firebase.
        getListSongsByCategory();

        // Trả về giao diện đã được cấu hình.
        return mFragmentSongsByCategoryBinding.getRoot();
    }

    // Lấy ID danh mục từ bundle được truyền vào.
    private void getDataIntent() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mCategoryId = bundle.getLong(Constant.CATEGORY_ID); // Lấy giá trị categoryId từ bundle.
    }

    // Thiết lập giao diện RecyclerView và Adapter.
    private void initUi() {
        // Sử dụng LinearLayoutManager để sắp xếp danh sách bài hát theo chiều dọc.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentSongsByCategoryBinding.rcvData.setLayoutManager(linearLayoutManager);

        // Khởi tạo danh sách trống và thiết lập adapter.
        mListSong = new ArrayList<>();
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                // Chuyển đến màn hình phát nhạc khi chọn bài hát.
                goToSongDetail(song);
            }

            @Override
            public void onClickFavoriteSong(Song song, boolean favorite) {
                // Xử lý yêu thích/hủy yêu thích bài hát.
                GlobalFunction.onClickFavoriteSong(getActivity(), song, favorite);
            }

            @Override
            public void onClickMoreOptions(Song song) {
                // Xử lý các tùy chọn bổ sung khi chọn bài hát.
                GlobalFunction.handleClickMoreOptions(getActivity(), song);
            }
        });

        // Gán adapter cho RecyclerView.
        mFragmentSongsByCategoryBinding.rcvData.setAdapter(mSongAdapter);
    }

    // Lấy danh sách bài hát theo danh mục từ Firebase.
    @SuppressLint("NotifyDataSetChanged")
    private void getListSongsByCategory() {
        if (getActivity() == null) return;

        // Lấy tham chiếu đến cơ sở dữ liệu bài hát trong Firebase.
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xóa danh sách cũ trước khi cập nhật dữ liệu mới.
                        resetListData();

                        // Lặp qua tất cả các bài hát trong cơ sở dữ liệu.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class); // Chuyển đổi dữ liệu Firebase thành đối tượng Song.
                            if (song == null) return;

                            // Chỉ thêm các bài hát thuộc về danh mục có ID trùng khớp.
                            if (mCategoryId == song.getCategoryId()) {
                                mListSong.add(0, song); // Thêm bài hát vào danh sách.
                            }
                        }

                        // Cập nhật giao diện RecyclerView.
                        if (mSongAdapter != null) mSongAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Hiển thị thông báo lỗi nếu có sự cố khi lấy dữ liệu.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }

    // Xóa danh sách cũ để chuẩn bị cập nhật dữ liệu mới.
    private void resetListData() {
        if (mListSong == null) {
            mListSong = new ArrayList<>();
        } else {
            mListSong.clear();
        }
    }

    // Chuyển đến màn hình phát nhạc cho bài hát được chọn.
    private void goToSongDetail(@NonNull Song song) {
        // Xóa danh sách bài hát đang phát.
        MusicService.clearListSongPlaying();

        // Thêm bài hát được chọn vào danh sách phát.
        MusicService.mListSongPlaying.add(song);

        // Cập nhật trạng thái chưa phát nhạc.
        MusicService.isPlaying = false;

        // Bắt đầu dịch vụ phát nhạc và chuyển đến màn hình phát nhạc.
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);
        GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
    }

    // Đăng ký các sự kiện, bao gồm cả sự kiện phát tất cả bài hát.
    private void initListener() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }

        // Xử lý sự kiện phát tất cả bài hát trong danh sách của danh mục.
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            if (mListSong == null || mListSong.isEmpty()) return;

            // Xóa danh sách bài hát đang phát và thêm tất cả bài hát thuộc danh mục vào danh sách phát.
            MusicService.clearListSongPlaying();
            MusicService.mListSongPlaying.addAll(mListSong);

            // Cập nhật trạng thái chưa phát nhạc.
            MusicService.isPlaying = false;

            // Bắt đầu dịch vụ phát nhạc và chuyển đến màn hình phát nhạc.
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }
}
