package com.pro.music.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

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
import com.pro.music.databinding.FragmentSearchBinding;
import com.pro.music.listener.IOnClickSongItemListener;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;
import com.pro.music.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    // ViewBinding giúp truy cập trực tiếp các thành phần giao diện từ layout.
    private FragmentSearchBinding mFragmentSearchBinding;

    // Danh sách bài hát được tìm kiếm.
    private List<Song> mListSong;

    // Adapter để hiển thị danh sách bài hát trong RecyclerView.
    private SongAdapter mSongAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Khởi tạo ViewBinding để liên kết giao diện.
        mFragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);

        // Thiết lập giao diện ban đầu.
        initUi();

        // Đăng ký sự kiện người dùng.
        initListener();

        // Lấy danh sách bài hát từ Firebase với từ khóa trống ban đầu (tức là lấy tất cả).
        getListSongFromFirebase("");

        // Trả về giao diện đã được cấu hình.
        return mFragmentSearchBinding.getRoot();
    }

    // Thiết lập giao diện RecyclerView và Adapter.
    private void initUi() {
        // Sử dụng LinearLayoutManager để sắp xếp danh sách bài hát theo chiều dọc.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentSearchBinding.rcvSearchResult.setLayoutManager(linearLayoutManager);

        // Khởi tạo danh sách trống và thiết lập adapter.
        mListSong = new ArrayList<>();
        mSongAdapter = new SongAdapter(mListSong, new IOnClickSongItemListener() {
            @Override
            public void onClickItemSong(Song song) {
                // Chuyển đến màn hình phát nhạc khi chọn một bài hát.
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
        mFragmentSearchBinding.rcvSearchResult.setAdapter(mSongAdapter);
    }

    // Đăng ký các sự kiện cho các thành phần giao diện.
    private void initListener() {
        // Lắng nghe sự thay đổi văn bản trong ô tìm kiếm.
        mFragmentSearchBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không làm gì khi văn bản đang chuẩn bị thay đổi.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không làm gì khi văn bản đang thay đổi.
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Lấy danh sách bài hát khi văn bản thay đổi.
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    getListSongFromFirebase(""); // Tìm kiếm với từ khóa trống.
                }
            }
        });

        // Xử lý sự kiện khi nhấn vào biểu tượng tìm kiếm.
        mFragmentSearchBinding.imgSearch.setOnClickListener(view -> searchSong());

        // Xử lý sự kiện nhấn nút "Search" trên bàn phím.
        mFragmentSearchBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchSong(); // Thực hiện tìm kiếm khi nhấn nút Search.
                return true;
            }
            return false;
        });

        // Xử lý sự kiện phát tất cả bài hát trong danh sách kết quả tìm kiếm.
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null || activity.getActivityMainBinding() == null) {
            return;
        }
        activity.getActivityMainBinding().header.layoutPlayAll.setOnClickListener(v -> {
            if (mListSong == null || mListSong.isEmpty()) return;

            // Xóa danh sách bài hát đang phát và thêm toàn bộ kết quả tìm kiếm vào danh sách phát.
            MusicService.clearListSongPlaying();
            MusicService.mListSongPlaying.addAll(mListSong);
            MusicService.isPlaying = false;

            // Bắt đầu dịch vụ phát nhạc và chuyển đến màn hình phát nhạc.
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0);
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity.class);
        });
    }

    // Lấy danh sách bài hát từ Firebase dựa trên từ khóa tìm kiếm.
    private void getListSongFromFirebase(String key) {
        if (getActivity() == null) return;

        // Lấy tham chiếu đến cơ sở dữ liệu bài hát trong Firebase.
        MyApplication.get(getActivity()).getSongsDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xóa danh sách cũ trước khi cập nhật dữ liệu mới.
                        resetListData();

                        // Lặp qua tất cả các bài hát trong cơ sở dữ liệu.
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Song song = dataSnapshot.getValue(Song.class); // Chuyển đổi dữ liệu Firebase thành đối tượng Song.
                            if (song == null) return;

                            // Nếu từ khóa tìm kiếm trống, thêm tất cả bài hát.
                            if (StringUtil.isEmpty(key)) {
                                mListSong.add(0, song);
                            } else {
                                // Chỉ thêm bài hát nếu tiêu đề chứa từ khóa tìm kiếm.
                                if (GlobalFunction.getTextSearch(song.getTitle()).toLowerCase().trim()
                                        .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                                    mListSong.add(0, song);
                                }
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

    // Thực hiện tìm kiếm bài hát.
    private void searchSong() {
        // Lấy từ khóa từ ô nhập liệu và tìm kiếm trong Firebase.
        String strKey = mFragmentSearchBinding.edtSearchName.getText().toString().trim();
        getListSongFromFirebase(strKey);

        // Ẩn bàn phím sau khi thực hiện tìm kiếm.
        GlobalFunction.hideSoftKeyboard(getActivity());
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
}
