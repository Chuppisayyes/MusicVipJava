package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter giúp kết nối dữ liệu với RecyclerView.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemAdminCategoryBinding;
// Import file binding cho layout của từng danh mục (Category) trong giao diện Admin.

import com.pro.music.listener.IOnAdminManagerCategoryListener;
// Import interface lắng nghe sự kiện quản lý danh mục từ Admin.

import com.pro.music.model.Category;
// Import lớp `Category`, đại diện cho danh mục.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách danh mục.

// *** Lớp AdminCategoryAdapter ***
// Adapter quản lý danh sách danh mục trong giao diện quản trị Admin.
public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.AdminCategoryViewHolder> {

    private final List<Category> mListCategory;
    // Danh sách các danh mục được hiển thị.

    private final IOnAdminManagerCategoryListener mListener;
    // Lắng nghe các sự kiện thao tác trên danh mục từ Admin.

    // *** Constructor ***
    public AdminCategoryAdapter(List<Category> list, IOnAdminManagerCategoryListener listener) {
        this.mListCategory = list; // Gán danh sách danh mục.
        this.mListener = listener; // Gán listener để xử lý sự kiện.
    }

    @NonNull
    @Override
    public AdminCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho mỗi mục trong danh sách danh mục.
        ItemAdminCategoryBinding binding = ItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục trong danh sách danh mục.
        Category category = mListCategory.get(position); // Lấy danh mục tại vị trí `position`.
        if (category == null) return; // Nếu danh mục null, không làm gì.

        // Hiển thị ảnh của danh mục bằng GlideUtils.
        GlideUtils.loadUrl(category.getImage(), holder.itemBinding.imgCategory);

        // Hiển thị tên danh mục.
        holder.itemBinding.tvName.setText(category.getName());

        // Gắn sự kiện khi nhấn vào nút chỉnh sửa.
        holder.itemBinding.imgEdit.setOnClickListener(v -> mListener.onClickUpdateCategory(category));

        // Gắn sự kiện khi nhấn vào nút xóa.
        holder.itemBinding.imgDelete.setOnClickListener(v -> mListener.onClickDeleteCategory(category));

        // Gắn sự kiện khi nhấn vào mục để xem chi tiết danh mục.
        holder.itemBinding.layoutItem.setOnClickListener(v -> mListener.onClickDetailCategory(category));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng danh mục trong danh sách.
        if (mListCategory != null) {
            return mListCategory.size();
        }
        return 0;
    }

    // *** Lớp AdminCategoryViewHolder ***
    // ViewHolder đại diện cho từng mục danh mục trong RecyclerView.
    public static class AdminCategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminCategoryBinding itemBinding;
        // Binding cho từng mục danh mục.

        public AdminCategoryViewHolder(@NonNull ItemAdminCategoryBinding binding) {
            super(binding.getRoot()); // Gọi constructor cha với root view của binding.
            this.itemBinding = binding; // Gán binding cho ViewHolder.
        }
    }
}
