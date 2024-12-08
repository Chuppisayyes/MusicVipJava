package com.pro.music.fragment.admin;
// Định nghĩa package của Fragment này.

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
// Import các thành phần cần thiết như Fragment, RecyclerView, AlertDialog, Toast.

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
// Import Firebase để lắng nghe thay đổi dữ liệu trong cơ sở dữ liệu.

import com.pro.music.MyApplication;
import com.pro.music.R;
// Import tài nguyên ứng dụng như chuỗi và layout.

import com.pro.music.activity.AdminAddCategoryActivity;
import com.pro.music.activity.AdminCategorySongActivity;
// Import các Activity liên quan đến danh mục (thêm, xem bài hát theo danh mục).

import com.pro.music.adapter.AdminCategoryAdapter;
// Import Adapter hiển thị danh sách danh mục.

import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
// Import các hằng số và hàm tiện ích toàn cục.

import com.pro.music.databinding.FragmentAdminCategoryBinding;
// Import View Binding cho layout của Fragment.

import com.pro.music.listener.IOnAdminManagerCategoryListener;
// Import interface xử lý sự kiện quản lý danh mục.

import com.pro.music.model.Category;
// Import lớp Category, đại diện cho mỗi danh mục.

import com.pro.music.utils.StringUtil;
// Import tiện ích xử lý chuỗi.

import java.util.ArrayList;
import java.util.List;
// Import List để quản lý danh sách danh mục.

// *** Lớp AdminCategoryFragment ***
public class AdminCategoryFragment extends Fragment {

    private FragmentAdminCategoryBinding binding;
    // Biến `binding` để liên kết với layout.

    private List<Category> mListCategory;
    // Danh sách các danh mục sẽ được hiển thị.

    private AdminCategoryAdapter mAdminCategoryAdapter;
    // Adapter để quản lý danh sách danh mục trong RecyclerView.

    private ChildEventListener mChildEventListener;
    // Listener lắng nghe thay đổi dữ liệu từ Firebase.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện cho Fragment.
        binding = FragmentAdminCategoryBinding.inflate(inflater, container, false); // Liên kết layout với binding.

        initView(); // Khởi tạo giao diện.
        initListener(); // Gắn các sự kiện cho các thành phần giao diện.
        loadListCategory(""); // Tải danh sách danh mục ban đầu (không có từ khóa tìm kiếm).

        return binding.getRoot(); // Trả về root view của Fragment.
    }

    private void initView() {
        // Phương thức khởi tạo RecyclerView và danh sách danh mục.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvCategory.setLayoutManager(linearLayoutManager); // Đặt LayoutManager cho RecyclerView.

        mListCategory = new ArrayList<>(); // Khởi tạo danh sách trống.
        mAdminCategoryAdapter = new AdminCategoryAdapter(mListCategory, new IOnAdminManagerCategoryListener() {
            // Khởi tạo adapter và định nghĩa các sự kiện trong interface.
            @Override
            public void onClickUpdateCategory(Category category) {
                // Sự kiện chỉnh sửa danh mục.
                onClickEditCategory(category);
            }

            @Override
            public void onClickDeleteCategory(Category category) {
                // Sự kiện xóa danh mục.
                deleteCategoryItem(category);
            }

            @Override
            public void onClickDetailCategory(Category category) {
                // Sự kiện xem chi tiết danh mục.
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_INTENT_CATEGORY_OBJECT, category); // Truyền đối tượng danh mục.
                GlobalFunction.startActivity(getActivity(), AdminCategorySongActivity.class, bundle); // Chuyển đến Activity xem chi tiết danh mục.
            }
        });

        binding.rcvCategory.setAdapter(mAdminCategoryAdapter); // Gắn adapter vào RecyclerView.

        binding.rcvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // Ẩn/hiện nút "Thêm danh mục" khi cuộn RecyclerView.
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.btnAddCategory.hide(); // Ẩn nút khi cuộn xuống.
                } else {
                    binding.btnAddCategory.show(); // Hiện nút khi cuộn lên.
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initListener() {
        // Phương thức gắn sự kiện cho các thành phần giao diện.

        binding.btnAddCategory.setOnClickListener(v -> onClickAddCategory()); // Sự kiện thêm danh mục.

        binding.imgSearch.setOnClickListener(view1 -> searchCategory()); // Sự kiện tìm kiếm danh mục.

        binding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            // Xử lý sự kiện tìm kiếm bằng bàn phím.
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCategory(); // Tìm kiếm danh mục.
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
                    searchCategory(); // Tự động tìm kiếm khi xóa hết từ khóa.
                }
            }
        });
    }

    private void onClickAddCategory() {
        // Chuyển đến Activity thêm danh mục.
        GlobalFunction.startActivity(getActivity(), AdminAddCategoryActivity.class);
    }

    private void onClickEditCategory(Category category) {
        // Chuyển đến Activity chỉnh sửa danh mục.
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_CATEGORY_OBJECT, category);
        GlobalFunction.startActivity(getActivity(), AdminAddCategoryActivity.class, bundle);
    }

    private void deleteCategoryItem(Category category) {
        // Hiển thị hộp thoại xác nhận xóa danh mục.
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    MyApplication.get(getActivity()).getCategoryDatabaseReference()
                            .child(String.valueOf(category.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_category_successfully),
                                            Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchCategory() {
        // Tìm kiếm danh mục theo từ khóa.
        String strKey = binding.edtSearchName.getText().toString().trim();
        resetListCategory(); // Xóa danh sách cũ.
        if (getActivity() != null) {
            MyApplication.get(getActivity()).getCategoryDatabaseReference()
                    .removeEventListener(mChildEventListener); // Xóa listener cũ.
        }
        loadListCategory(strKey); // Tải danh sách danh mục mới.
        GlobalFunction.hideSoftKeyboard(getActivity()); // Ẩn bàn phím.
    }

    private void resetListCategory() {
        // Xóa danh sách danh mục hiện tại.
        if (mListCategory != null) {
            mListCategory.clear();
        } else {
            mListCategory = new ArrayList<>();
        }
    }

    public void loadListCategory(String keyword) {
        // Tải danh sách danh mục từ Firebase.
        if (getActivity() == null) return;
        mChildEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                // Thêm danh mục vào danh sách khi dữ liệu thay đổi.
                Category category = dataSnapshot.getValue(Category.class);
                if (category == null || mListCategory == null) return;
                if (StringUtil.isEmpty(keyword)) {
                    mListCategory.add(0, category);
                } else if (GlobalFunction.getTextSearch(category.getName()).toLowerCase().trim()
                        .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                    mListCategory.add(0, category);
                }
                if (mAdminCategoryAdapter != null) mAdminCategoryAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // Cập nhật danh mục khi dữ liệu thay đổi.
                Category category = dataSnapshot.getValue(Category.class);
                if (category == null || mListCategory == null || mListCategory.isEmpty()) return;
                for (int i = 0; i < mListCategory.size(); i++) {
                    if (category.getId() == mListCategory.get(i).getId()) {
                        mListCategory.set(i, category);
                        break;
                    }
                }
                if (mAdminCategoryAdapter != null) mAdminCategoryAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Xóa danh mục khỏi danh sách khi dữ liệu thay đổi.
                Category category = dataSnapshot.getValue(Category.class);
                if (category == null || mListCategory == null || mListCategory.isEmpty()) return;
                for (Category categoryObject : mListCategory) {
                    if (category.getId() == categoryObject.getId()) {
                        mListCategory.remove(categoryObject);
                        break;
                    }
                }
                if (mAdminCategoryAdapter != null) mAdminCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        MyApplication.get(getActivity()).getCategoryDatabaseReference()
                .addChildEventListener(mChildEventListener);
    }
}
