package com.pro.music.fragment.admin;
// Định nghĩa package chứa fragment. Fragment này quản lý danh sách nghệ sĩ trong giao diện quản trị.

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần để tạo giao diện Fragment, RecyclerView, và xử lý sự kiện.

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
// Import Firebase để quản lý dữ liệu nghệ sĩ.

import com.pro.music.MyApplication;
import com.pro.music.R;
// Import các tài nguyên như chuỗi và layout.

import com.pro.music.activity.AdminAddArtistActivity;
import com.pro.music.activity.AdminArtistSongActivity;
// Import các Activity liên quan đến nghệ sĩ (thêm, sửa, xem bài hát của nghệ sĩ).

import com.pro.music.adapter.AdminArtistAdapter;
// Import Adapter để hiển thị danh sách nghệ sĩ.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích.

import com.pro.music.databinding.FragmentAdminArtistBinding;
// Import View Binding cho layout của Fragment này.

import com.pro.music.listener.IOnAdminManagerArtistListener;
// Import interface để xử lý sự kiện trên các mục nghệ sĩ.

import com.pro.music.model.Artist;
// Import lớp `Artist` đại diện cho nghệ sĩ.

import com.pro.music.utils.StringUtil;
// Import tiện ích xử lý chuỗi.

import java.util.ArrayList;
import java.util.List;
// Import danh sách để quản lý nghệ sĩ.

// *** Lớp AdminArtistFragment ***
// Fragment này quản lý danh sách nghệ sĩ, cho phép thêm, sửa, xóa và tìm kiếm nghệ sĩ.
public class AdminArtistFragment extends Fragment {

    private FragmentAdminArtistBinding binding;
    // Biến binding để liên kết layout với code.

    private List<Artist> mListArtist;
    // Danh sách nghệ sĩ hiển thị trên RecyclerView.

    private AdminArtistAdapter mAdminArtistAdapter;
    // Adapter để hiển thị danh sách nghệ sĩ.

    private ChildEventListener mChildEventListener;
    // Listener để lắng nghe thay đổi dữ liệu từ Firebase.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện cho Fragment.
        binding = FragmentAdminArtistBinding.inflate(inflater, container, false); // Liên kết layout với binding.
        initView(); // Khởi tạo giao diện.
        initListener(); // Gắn các sự kiện.
        loadListArtist(""); // Tải danh sách nghệ sĩ ban đầu.

        return binding.getRoot(); // Trả về root view của Fragment.
    }

    private void initView() {
        // Phương thức khởi tạo RecyclerView và các thành phần liên quan.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvArtist.setLayoutManager(linearLayoutManager); // Đặt LayoutManager cho RecyclerView.

        mListArtist = new ArrayList<>(); // Khởi tạo danh sách trống.
        mAdminArtistAdapter = new AdminArtistAdapter(mListArtist, new IOnAdminManagerArtistListener() {
            @Override
            public void onClickUpdateArtist(Artist artist) {
                // Xử lý sự kiện chỉnh sửa nghệ sĩ.
                onClickEditArtist(artist);
            }

            @Override
            public void onClickDeleteArtist(Artist artist) {
                // Xử lý sự kiện xóa nghệ sĩ.
                deleteArtistItem(artist);
            }

            @Override
            public void onClickDetailArtist(Artist artist) {
                // Xử lý sự kiện xem chi tiết nghệ sĩ.
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_INTENT_ARTIST_OBJECT, artist); // Truyền dữ liệu nghệ sĩ.
                GlobalFunction.startActivity(getActivity(), AdminArtistSongActivity.class, bundle); // Chuyển đến Activity chi tiết nghệ sĩ.
            }
        });

        binding.rcvArtist.setAdapter(mAdminArtistAdapter); // Gán adapter cho RecyclerView.

        binding.rcvArtist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // Ẩn/hiện nút thêm nghệ sĩ khi cuộn RecyclerView.
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.btnAddArtist.hide(); // Ẩn nút khi cuộn xuống.
                } else {
                    binding.btnAddArtist.show(); // Hiện nút khi cuộn lên.
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initListener() {
        // Gắn sự kiện cho các thành phần giao diện.
        binding.btnAddArtist.setOnClickListener(v -> onClickAddArtist()); // Thêm nghệ sĩ.
        binding.imgSearch.setOnClickListener(view1 -> searchArtist()); // Tìm kiếm nghệ sĩ.

        binding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            // Xử lý sự kiện tìm kiếm bằng bàn phím.
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchArtist();
                return true;
            }
            return false;
        });

        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            // Xử lý khi người dùng nhập văn bản tìm kiếm.
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchArtist(); // Tự động tìm kiếm khi nhập trống.
                }
            }
        });
    }

    private void onClickAddArtist() {
        // Chuyển đến Activity thêm nghệ sĩ.
        GlobalFunction.startActivity(getActivity(), AdminAddArtistActivity.class);
    }

    private void onClickEditArtist(Artist artist) {
        // Chuyển đến Activity chỉnh sửa nghệ sĩ.
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ARTIST_OBJECT, artist);
        GlobalFunction.startActivity(getActivity(), AdminAddArtistActivity.class, bundle);
    }

    private void deleteArtistItem(Artist artist) {
        // Xác nhận và xóa nghệ sĩ khỏi Firebase.
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) return;
                    MyApplication.get(getActivity()).getArtistDatabaseReference()
                            .child(String.valueOf(artist.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_artist_successfully),
                                            Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchArtist() {
        // Tìm kiếm nghệ sĩ theo từ khóa.
        String strKey = binding.edtSearchName.getText().toString().trim();
        resetListArtist(); // Xóa danh sách cũ.
        if (getActivity() != null) {
            MyApplication.get(getActivity()).getArtistDatabaseReference()
                    .removeEventListener(mChildEventListener); // Xóa listener cũ.
        }
        loadListArtist(strKey); // Tải danh sách nghệ sĩ mới theo từ khóa.
        GlobalFunction.hideSoftKeyboard(getActivity()); // Ẩn bàn phím.
    }

    private void resetListArtist() {
        // Xóa danh sách nghệ sĩ hiện tại.
        if (mListArtist != null) {
            mListArtist.clear();
        } else {
            mListArtist = new ArrayList<>();
        }
    }

    public void loadListArtist(String keyword) {
        // Tải danh sách nghệ sĩ từ Firebase.
        if (getActivity() == null) return;
        mChildEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                if (artist == null || mListArtist == null) return;
                if (StringUtil.isEmpty(keyword)) {
                    mListArtist.add(0, artist);
                } else {
                    if (GlobalFunction.getTextSearch(artist.getName()).toLowerCase().trim()
                            .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                        mListArtist.add(0, artist);
                    }
                }
                if (mAdminArtistAdapter != null) mAdminArtistAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                if (artist == null || mListArtist == null || mListArtist.isEmpty()) return;
                for (int i = 0; i < mListArtist.size(); i++) {
                    if (artist.getId() == mListArtist.get(i).getId()) {
                        mListArtist.set(i, artist);
                        break;
                    }
                }
                if (mAdminArtistAdapter != null) mAdminArtistAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Artist artist = dataSnapshot.getValue(Artist.class);
                if (artist == null || mListArtist == null || mListArtist.isEmpty()) return;
                for (Artist artistObject : mListArtist) {
                    if (artist.getId() == artistObject.getId()) {
                        mListArtist.remove(artistObject);
                        break;
                    }
                }
                if (mAdminArtistAdapter != null) mAdminArtistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        MyApplication.get(getActivity()).getArtistDatabaseReference()
                .addChildEventListener(mChildEventListener);
    }
}
