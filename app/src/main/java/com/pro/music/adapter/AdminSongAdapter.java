package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này kết nối dữ liệu với giao diện danh sách bài hát.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemAdminSongBinding;
// Import file binding cho layout của từng bài hát trong giao diện Admin.

import com.pro.music.listener.IOnAdminManagerSongListener;
// Import interface lắng nghe sự kiện liên quan đến bài hát từ giao diện Admin.

import com.pro.music.model.Song;
// Import lớp `Song`, đại diện cho bài hát.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách bài hát.

// *** Lớp AdminSongAdapter ***
// Adapter này quản lý danh sách bài hát trong giao diện Admin.
public class AdminSongAdapter extends RecyclerView.Adapter<AdminSongAdapter.AdminSongViewHolder> {

    private final List<Song> mListSongs;
    // Danh sách các bài hát cần hiển thị.

    public final IOnAdminManagerSongListener mListener;
    // Listener để xử lý các sự kiện liên quan đến bài hát.

    // *** Constructor ***
    public AdminSongAdapter(List<Song> list, IOnAdminManagerSongListener listener) {
        this.mListSongs = list; // Gán danh sách bài hát.
        this.mListener = listener; // Gán listener để xử lý sự kiện.
    }

    @NonNull
    @Override
    public AdminSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục bài hát.
        ItemAdminSongBinding itemAdminSongBinding = ItemAdminSongBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminSongViewHolder(itemAdminSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSongViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục bài hát trong danh sách.
        Song song = mListSongs.get(position); // Lấy bài hát tại vị trí `position`.
        if (song == null) return; // Nếu bài hát null, không làm gì.

        // Hiển thị ảnh bài hát bằng GlideUtils.
        GlideUtils.loadUrl(song.getImage(), holder.mItemAdminSongBinding.imgSong);

        // Hiển thị tiêu đề bài hát.
        holder.mItemAdminSongBinding.tvName.setText(song.getTitle());

        // Hiển thị danh mục bài hát.
        holder.mItemAdminSongBinding.tvCategory.setText(song.getCategory());

        // Hiển thị nghệ sĩ của bài hát.
        holder.mItemAdminSongBinding.tvArtist.setText(song.getArtist());

        // Kiểm tra bài hát có phải là "featured" không.
        if (song.isFeatured()) {
            holder.mItemAdminSongBinding.tvFeatured.setText("Yes");
        } else {
            holder.mItemAdminSongBinding.tvFeatured.setText("No");
        }

        // Gắn sự kiện khi nhấn vào nút chỉnh sửa bài hát.
        holder.mItemAdminSongBinding.imgEdit.setOnClickListener(v -> mListener.onClickUpdateSong(song));

        // Gắn sự kiện khi nhấn vào nút xóa bài hát.
        holder.mItemAdminSongBinding.imgDelete.setOnClickListener(v -> mListener.onClickDeleteSong(song));

        // Gắn sự kiện khi nhấn vào bài hát để xem chi tiết.
        holder.mItemAdminSongBinding.layoutItem.setOnClickListener(v -> mListener.onClickDetailSong(song));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng bài hát trong danh sách.
        return null == mListSongs ? 0 : mListSongs.size();
    }

    // *** Lớp AdminSongViewHolder ***
    // ViewHolder đại diện cho từng mục bài hát trong RecyclerView.
    public static class AdminSongViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminSongBinding mItemAdminSongBinding;
        // Binding cho từng mục bài hát.

        public AdminSongViewHolder(ItemAdminSongBinding itemAdminSongBinding) {
            super(itemAdminSongBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemAdminSongBinding = itemAdminSongBinding; // Gán binding cho ViewHolder.
        }
    }
}
