package com.pro.music.fragment;
// Định nghĩa package chứa lớp Fragment.

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thành phần cơ bản của Android để xử lý giao diện.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
// Import các thành phần Fragment và RecyclerView để hiển thị danh sách nghệ sĩ.

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
// Import Firebase để lấy danh sách nghệ sĩ từ cơ sở dữ liệu.

import com.pro.music.MyApplication;
import com.pro.music.R;
// Import các lớp cơ sở của dự án.

import com.pro.music.activity.MainActivity;
import com.pro.music.adapter.ArtistVerticalAdapter;
// Import Activity chính và Adapter để quản lý danh sách nghệ sĩ.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích.

import com.pro.music.databinding.FragmentArtistBinding;
// Import View Binding để liên kết layout với Fragment.

import com.pro.music.model.Artist;
// Import lớp `Artist` đại diện cho đối tượng nghệ sĩ.

import java.util.ArrayList;
import java.util.List;
// Import danh sách để quản lý dữ liệu nghệ sĩ.

// *** Lớp ArtistFragment ***
// Fragment này hiển thị danh sách các nghệ sĩ dưới dạng lưới.
public class ArtistFragment extends Fragment {

    private FragmentArtistBinding mFragmentArtistBinding;
    // Biến `binding` để liên kết layout với Fragment.

    private List<Artist> mListArtist;
    // Danh sách nghệ sĩ hiển thị trên giao diện.

    private ArtistVerticalAdapter mArtistVerticalAdapter;
    // Adapter để quản lý và hiển thị danh sách nghệ sĩ.

    public boolean mIsFromMenuLeft;
    // Biến kiểm tra xem Fragment có được gọi từ menu trái hay không.

    public static ArtistFragment newInstance(boolean isFromMenuLeft) {
        // Phương thức tạo mới Fragment với tham số đầu vào.
        ArtistFragment fragment = new ArtistFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_FROM_MENU_LEFT, isFromMenuLeft);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Phương thức khởi tạo giao diện Fragment.
        mFragmentArtistBinding = FragmentArtistBinding.inflate(inflater, container, false);
        // Liên kết layout với binding.

        getDataIntent(); // Lấy dữ liệu truyền vào Fragment.
        initUi(); // Khởi tạo giao diện RecyclerView.
        getListAllArtist(); // Lấy danh sách tất cả nghệ sĩ từ Firebase.

        return mFragmentArtistBinding.getRoot(); // Trả về root view của Fragment.
    }

    private void getDataIntent() {
        // Lấy dữ liệu truyền vào Fragment qua Bundle.
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mIsFromMenuLeft = bundle.getBoolean(Constant.IS_FROM_MENU_LEFT);
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện RecyclerView hiển thị danh sách nghệ sĩ.
        if (getActivity() == null) return;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        // Sử dụng GridLayoutManager để hiển thị danh sách nghệ sĩ dạng lưới với 2 cột.

        mFragmentArtistBinding.rcvData.setLayoutManager(gridLayoutManager);
        // Gán LayoutManager cho RecyclerView.

        mListArtist = new ArrayList<>(); // Khởi tạo danh sách nghệ sĩ rỗng.
        mArtistVerticalAdapter = new ArtistVerticalAdapter(mListArtist, artist -> {
            // Khởi tạo Adapter và định nghĩa sự kiện click vào nghệ sĩ.
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.clickOpenSongsByArtist(artist);
                // Gọi phương thức trong MainActivity để hiển thị bài hát của nghệ sĩ.
            }
        });
        mFragmentArtistBinding.rcvData.setAdapter(mArtistVerticalAdapter);
        // Gắn Adapter vào RecyclerView.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListAllArtist() {
        // Phương thức lấy danh sách tất cả nghệ sĩ từ cơ sở dữ liệu Firebase.
        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getArtistDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xử lý khi dữ liệu trong cơ sở dữ liệu thay đổi.
                        if (mListArtist == null) {
                            mListArtist = new ArrayList<>();
                        } else {
                            mListArtist.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Artist artist = dataSnapshot.getValue(Artist.class);
                            if (artist == null) return;
                            mListArtist.add(0, artist);
                            // Thêm nghệ sĩ mới vào danh sách.
                        }
                        if (mArtistVerticalAdapter != null) mArtistVerticalAdapter.notifyDataSetChanged();
                        // Cập nhật giao diện sau khi thêm dữ liệu.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi khi không thể lấy dữ liệu từ Firebase.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }
}
