package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý dữ liệu bài hát hiển thị dưới dạng banner.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemBannerSongBinding;
// Import file binding cho layout của từng bài hát hiển thị dưới dạng banner.

import com.pro.music.listener.IOnClickSongItemListener;
// Import interface lắng nghe sự kiện khi người dùng nhấn vào bài hát.

import com.pro.music.model.Song;
// Import lớp `Song`, đại diện cho bài hát.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách bài hát.

// *** Lớp BannerSongAdapter ***
// Adapter này quản lý danh sách bài hát hiển thị dưới dạng banner.
public class BannerSongAdapter extends RecyclerView.Adapter<BannerSongAdapter.BannerSongViewHolder> {

    private final List<Song> mListSongs;
    // Danh sách các bài hát cần hiển thị.

    public final IOnClickSongItemListener iOnClickSongItemListener;
    // Listener để xử lý sự kiện khi nhấn vào một bài hát.

    // *** Constructor ***
    public BannerSongAdapter(List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mListSongs = mListSongs; // Gán danh sách bài hát.
        this.iOnClickSongItemListener = iOnClickSongItemListener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public BannerSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục bài hát dưới dạng banner.
        ItemBannerSongBinding itemBannerSongBinding = ItemBannerSongBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerSongViewHolder(itemBannerSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerSongViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục bài hát trong danh sách.
        Song song = mListSongs.get(position); // Lấy bài hát tại vị trí `position`.
        if (song == null) {
            return; // Nếu bài hát null, không làm gì.
        }

        // Hiển thị ảnh bài hát dưới dạng banner bằng GlideUtils.
        GlideUtils.loadUrlBanner(song.getImage(), holder.mItemBannerSongBinding.imageBanner);

        // Gắn sự kiện khi nhấn vào banner bài hát.
        holder.mItemBannerSongBinding.layoutItem.setOnClickListener(v ->
                iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng bài hát trong danh sách.
        if (mListSongs != null) {
            return mListSongs.size();
        }
        return 0;
    }

    // *** Lớp BannerSongViewHolder ***
    // ViewHolder đại diện cho từng mục bài hát hiển thị dưới dạng banner trong RecyclerView.
    public static class BannerSongViewHolder extends RecyclerView.ViewHolder {

        private final ItemBannerSongBinding mItemBannerSongBinding;
        // Binding cho từng mục bài hát.

        public BannerSongViewHolder(@NonNull ItemBannerSongBinding itemBannerSongBinding) {
            super(itemBannerSongBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemBannerSongBinding = itemBannerSongBinding; // Gán binding cho ViewHolder.
        }
    }
}
