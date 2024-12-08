package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter kết nối dữ liệu với RecyclerView.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemAdminFeedbackBinding;
// Import file binding cho layout của từng phản hồi (Feedback) trong giao diện Admin.

import com.pro.music.model.Feedback;
// Import lớp `Feedback`, đại diện cho phản hồi của người dùng.

import java.util.List;
// Import List để quản lý danh sách phản hồi.

// *** Lớp AdminFeedbackAdapter ***
// Adapter quản lý danh sách phản hồi của người dùng trong giao diện quản trị Admin.
public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.AdminFeedbackViewHolder> {

    private final List<Feedback> mListFeedback;
    // Danh sách các phản hồi của người dùng.

    // *** Constructor ***
    public AdminFeedbackAdapter(List<Feedback> list) {
        this.mListFeedback = list; // Gán danh sách phản hồi.
    }

    @NonNull
    @Override
    public AdminFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho mỗi mục trong danh sách phản hồi.
        ItemAdminFeedbackBinding binding = ItemAdminFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminFeedbackViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFeedbackViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục trong danh sách phản hồi.
        Feedback feedback = mListFeedback.get(position); // Lấy phản hồi tại vị trí `position`.
        if (feedback == null) return; // Nếu phản hồi null, không làm gì.

        // Hiển thị email của người dùng gửi phản hồi.
        holder.mItemAdminFeedbackBinding.tvEmail.setText(feedback.getEmail());

        // Hiển thị nội dung phản hồi của người dùng.
        holder.mItemAdminFeedbackBinding.tvFeedback.setText(feedback.getComment());
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng phản hồi trong danh sách.
        if (mListFeedback != null) {
            return mListFeedback.size();
        }
        return 0;
    }

    // *** Lớp AdminFeedbackViewHolder ***
    // ViewHolder đại diện cho từng mục phản hồi trong RecyclerView.
    public static class AdminFeedbackViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminFeedbackBinding mItemAdminFeedbackBinding;
        // Binding cho từng mục phản hồi.

        public AdminFeedbackViewHolder(@NonNull ItemAdminFeedbackBinding binding) {
            super(binding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemAdminFeedbackBinding = binding; // Gán binding cho ViewHolder.
        }
    }
}
