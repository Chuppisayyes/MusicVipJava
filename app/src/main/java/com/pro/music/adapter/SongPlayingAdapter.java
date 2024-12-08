package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý danh sách bài hát đang được phát (Playing Songs).

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.R;
// Import các tài nguyên, ví dụ: màu sắc và biểu tượng cho trạng thái phát nhạc.

import com.pro.music.databinding.ItemSongPlayingBinding;
// Import file binding cho layout của từng bài hát đang phát.

import com.pro.music.listener.IOnClickSongPlayingItemListener;
// Import interface để lắng nghe sự kiện khi người dùng tương tác với bài hát đang phát.

import com.pro.music.model.Song;
// Import lớp `Song`, đại diện cho từng bài hát.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách bài hát.

// *** Lớp SongPlayingAdapter ***
// Adapter này quản lý danh sách bài hát đang phát trong RecyclerView.
public class SongPlayingAdapter extends RecyclerView.Adapter<SongPlayingAdapter.SongPlayingViewHolder> {

    private final List<Song> mListSongs;
    // Danh sách các bài hát đang phát.

    public final IOnClickSongPlayingItemListener iOnClickSongPlayingItemListener;
    // Listener để xử lý sự kiện khi người dùng tương tác với bài hát đang phát.

    // *** Constructor ***
    public SongPlayingAdapter(List<Song> mListSongs, IOnClickSongPlayingItemListener iOnClickSongPlayingItemListener) {
        this.mListSongs = mListSongs; // Gán danh sách bài hát đang phát.
        this.iOnClickSongPlayingItemListener = iOnClickSongPlayingItemListener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public SongPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục bài hát đang phát.
        ItemSongPlayingBinding itemSongPlayingBinding = ItemSongPlayingBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongPlayingViewHolder(itemSongPlayingBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongPlayingViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục bài hát trong danh sách.
        Song song = mListSongs.get(position); // Lấy bài hát tại vị trí `position`.
        if (song == null) {
            return; // Nếu bài hát null, không làm gì.
        }

        // *** Cập nhật giao diện dựa trên trạng thái của bài hát ***
        if (song.isPlaying()) {
            // Nếu bài hát đang được phát.
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.background_bottom); // Đổi màu nền.
            holder.mItemSongPlayingBinding.imgAction.setImageResource(R.drawable.ic_play_black); // Hiển thị biểu tượng phát.
            holder.mItemSongPlayingBinding.imgAction.setOnClickListener(null); // Không xử lý sự kiện nhấn.
        } else {
            // Nếu bài hát không được phát.
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.white); // Đổi màu nền về mặc định.
            holder.mItemSongPlayingBinding.imgAction.setImageResource(R.drawable.ic_delete_black); // Hiển thị biểu tượng xóa.
            holder.mItemSongPlayingBinding.imgAction.setOnClickListener(
                    v -> iOnClickSongPlayingItemListener.onClickRemoveFromPlaylist(holder.getAdapterPosition())); // Xóa bài hát khỏi danh sách.
        }

        // Hiển thị trạng thái ưu tiên (priority) của bài hát.
        if (song.isPriority()) {
            holder.mItemSongPlayingBinding.tvPrioritized.setVisibility(View.VISIBLE); // Hiển thị trạng thái ưu tiên.
        } else {
            holder.mItemSongPlayingBinding.tvPrioritized.setVisibility(View.GONE); // Ẩn trạng thái ưu tiên.
        }

        // Hiển thị hình ảnh bài hát bằng GlideUtils.
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongPlayingBinding.imgSong);

        // Hiển thị tên bài hát và nghệ sĩ.
        holder.mItemSongPlayingBinding.tvSongName.setText(song.getTitle());
        holder.mItemSongPlayingBinding.tvArtist.setText(song.getArtist());

        // Gắn sự kiện khi người dùng nhấn vào bài hát.
        holder.mItemSongPlayingBinding.layoutSong.setOnClickListener(v ->
                iOnClickSongPlayingItemListener.onClickItemSongPlaying(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng bài hát trong danh sách.
        return null == mListSongs ? 0 : mListSongs.size();
    }

    // *** Lớp SongPlayingViewHolder ***
    // ViewHolder đại diện cho từng mục bài hát đang phát trong RecyclerView.
    public static class SongPlayingViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongPlayingBinding mItemSongPlayingBinding;
        // Binding cho từng mục bài hát.

        public SongPlayingViewHolder(ItemSongPlayingBinding itemSongPlayingBinding) {
            super(itemSongPlayingBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemSongPlayingBinding = itemSongPlayingBinding; // Gán binding cho ViewHolder.
        }
    }
}
