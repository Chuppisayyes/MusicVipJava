package com.pro.music.fragment;
// Định nghĩa package của Fragment.

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thư viện cơ bản để làm việc với Fragment và giao diện.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
// Import các lớp Fragment và RecyclerView để hiển thị danh sách.

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
// Import Firebase để truy cập dữ liệu thể loại nhạc.

import com.pro.music.MyApplication;
import com.pro.music.R;
// Import các thành phần chính của dự án.

import com.pro.music.activity.MainActivity;
import com.pro.music.adapter.CategoryAdapter;
// Import `MainActivity` và `CategoryAdapter` để quản lý danh sách thể loại.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích.

import com.pro.music.databinding.FragmentCategoryBinding;
// Import `View Binding` để liên kết giao diện với Fragment.

import com.pro.music.model.Category;
// Import lớp `Category` đại diện cho đối tượng thể loại nhạc.

import java.util.ArrayList;
import java.util.List;
// Import danh sách để lưu trữ dữ liệu thể loại.

public class CategoryFragment extends Fragment {
    // Fragment để hiển thị danh sách thể loại nhạc.

    private FragmentCategoryBinding mFragmentCategoryBinding;
    // Biến binding để liên kết layout với Fragment.

    private List<Category> mListCategory;
    // Danh sách các thể loại nhạc.

    private CategoryAdapter mCategoryAdapter;
    // Adapter để quản lý hiển thị các thể loại nhạc.

    public boolean mIsFromMenuLeft;
    // Biến kiểm tra xem Fragment có được gọi từ menu trái không.

    public static CategoryFragment newInstance(boolean isFromMenuLeft) {
        // Phương thức tạo mới Fragment với tham số đầu vào.
        CategoryFragment fragment = new CategoryFragment();
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
        mFragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater, container, false);
        // Liên kết layout với View Binding.

        getDataIntent(); // Lấy dữ liệu truyền vào Fragment.
        initUi(); // Khởi tạo giao diện RecyclerView.
        getListAllCategory(); // Lấy danh sách tất cả thể loại từ Firebase.

        return mFragmentCategoryBinding.getRoot();
        // Trả về root view của Fragment.
    }

    private void getDataIntent() {
        // Lấy dữ liệu truyền vào Fragment qua Bundle.
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mIsFromMenuLeft = bundle.getBoolean(Constant.IS_FROM_MENU_LEFT);
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện RecyclerView.
        if (getActivity() == null) return;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        // Sử dụng `GridLayoutManager` để hiển thị danh sách thể loại dạng lưới (2 cột).

        mFragmentCategoryBinding.rcvData.setLayoutManager(gridLayoutManager);
        // Gán LayoutManager cho RecyclerView.

        mListCategory = new ArrayList<>(); // Khởi tạo danh sách thể loại rỗng.
        mCategoryAdapter = new CategoryAdapter(mListCategory, category -> {
            // Khởi tạo Adapter và định nghĩa sự kiện click vào thể loại.
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.clickOpenSongsByCategory(category);
                // Gọi phương thức trong `MainActivity` để mở danh sách bài hát theo thể loại.
            }
        });
        mFragmentCategoryBinding.rcvData.setAdapter(mCategoryAdapter);
        // Gắn Adapter vào RecyclerView.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getListAllCategory() {
        // Phương thức lấy danh sách tất cả thể loại từ Firebase.
        if (getActivity() == null) return;
        MyApplication.get(getActivity()).getCategoryDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xử lý khi dữ liệu trong Firebase thay đổi.
                        if (mListCategory == null) {
                            mListCategory = new ArrayList<>();
                        } else {
                            mListCategory.clear();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Category category = dataSnapshot.getValue(Category.class);
                            if (category == null) return;
                            mListCategory.add(0, category);
                            // Thêm thể loại mới vào danh sách.
                        }
                        if (mCategoryAdapter != null) mCategoryAdapter.notifyDataSetChanged();
                        // Cập nhật RecyclerView sau khi dữ liệu thay đổi.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi khi không thể truy cập dữ liệu từ Firebase.
                        GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
                    }
                });
    }
}
