package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý dữ liệu danh mục (category) hiển thị trong ứng dụng.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemCategoryBinding;
// Import file binding cho layout của từng danh mục.

import com.pro.music.listener.IOnClickCategoryItemListener;
// Import interface lắng nghe sự kiện khi người dùng nhấn vào một danh mục.

import com.pro.music.model.Category;
// Import lớp `Category`, đại diện cho một danh mục.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách danh mục.

// *** Lớp CategoryAdapter ***
// Adapter này quản lý danh sách danh mục trong RecyclerView.
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> mListCategory;
    // Danh sách các danh mục cần hiển thị.

    public final IOnClickCategoryItemListener iOnClickCategoryItemListener;
    // Listener để xử lý sự kiện khi nhấn vào một danh mục.

    // *** Constructor ***
    public CategoryAdapter(List<Category> list, IOnClickCategoryItemListener listener) {
        this.mListCategory = list; // Gán danh sách danh mục.
        this.iOnClickCategoryItemListener = listener; // Gán listener để xử lý sự kiện.
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục danh mục.
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục danh mục trong danh sách.
        Category category = mListCategory.get(position); // Lấy danh mục tại vị trí `position`.
        if (category == null) {
            return; // Nếu danh mục null, không làm gì.
        }

        // Hiển thị ảnh của danh mục bằng GlideUtils.
        GlideUtils.loadUrl(category.getImage(), holder.mItemCategoryBinding.imgCategory);

        // Hiển thị tên danh mục.
        holder.mItemCategoryBinding.tvCategory.setText(category.getName());

        // Gắn sự kiện khi nhấn vào mục danh mục.
        holder.mItemCategoryBinding.layoutItem.setOnClickListener(v ->
                iOnClickCategoryItemListener.onClickItemCategory(category));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng danh mục trong danh sách.
        return null == mListCategory ? 0 : mListCategory.size();
    }

    // *** Lớp CategoryViewHolder ***
    // ViewHolder đại diện cho từng mục danh mục trong RecyclerView.
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding mItemCategoryBinding;
        // Binding cho từng mục danh mục.

        public CategoryViewHolder(ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemCategoryBinding = itemCategoryBinding; // Gán binding cho ViewHolder.
        }
    }
}
