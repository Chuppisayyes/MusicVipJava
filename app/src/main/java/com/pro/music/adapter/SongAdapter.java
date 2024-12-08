package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý danh sách bài hát hiển thị trong RecyclerView.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.R;
// Import các tài nguyên trong ứng dụng, ví dụ: hình ảnh cho trạng thái yêu thích.

import com.pro.music.constant.GlobalFunction;
// Import các hàm tiện ích toàn cục để xử lý các hành động liên quan đến bài hát.

import com.pro.music.databinding.ItemSongBinding;
// Import file binding cho layout của từng bài hát.

import com.pro.music.listener.IOnClickSongItemListener;
// Import interface để lắng nghe sự kiện khi người dùng tương tác với bài hát.

import com.pro.music.model.Song;
// Import lớp `Song`, đại diện cho từng bài hát.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách bài hát.

// *** Lớp SongAdapter ***
// Adapter này quản lý danh sách bài hát trong RecyclerView.
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> mListSongs;
    // Danh sách các bài hát cần hiển thị.

    public final IOnClickSongItemListener iOnClickSongItemListener;
    // Listener để xử lý sự kiện khi người dùng tương tác với bài hát.

    // *** Constructor ***
    public SongAdapter(List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mListSongs = mListSongs; // Gán danh sách bài hát.
        this.iOnClickSongItemListener = iOnClickSongItemListener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục bài hát.
        ItemSongBinding itemSongBinding = ItemSongBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục bài hát trong danh sách.
        Song song = mListSongs.get(position); // Lấy bài hát tại vị trí `position`.
        if (song == null) {
            return; // Nếu bài hát null, không làm gì.
        }

        // Hiển thị ảnh bài hát bằng GlideUtils.
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongBinding.imgSong);

        // Hiển thị tên bài hát.
        holder.mItemSongBinding.tvSongName.setText(song.getTitle());

        // Hiển thị tên nghệ sĩ.
        holder.mItemSongBinding.tvArtist.setText(song.getArtist());

        // Hiển thị số lượt nghe.
        holder.mItemSongBinding.tvCountListen.setText(String.valueOf(song.getCount()));

        // Kiểm tra bài hát có trong danh sách yêu thích hay không.
        boolean isFavorite = GlobalFunction.isFavoriteSong(song);
        if (isFavorite) {
            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_favorite); // Hiển thị icon yêu thích.
        } else {
            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite); // Hiển thị icon không yêu thích.
        }

        // Gắn sự kiện khi người dùng nhấn vào biểu tượng yêu thích.
        holder.mItemSongBinding.imgFavorite.setOnClickListener(v ->
                iOnClickSongItemListener.onClickFavoriteSong(song, !isFavorite));

        // Gắn sự kiện khi người dùng nhấn vào biểu tượng tùy chọn thêm.
        holder.mItemSongBinding.imgMoreOption.setOnClickListener(v ->
                iOnClickSongItemListener.onClickMoreOptions(song));

        // Gắn sự kiện khi người dùng nhấn vào thông tin bài hát.
        holder.mItemSongBinding.layoutSongInfo.setOnClickListener(v ->
                iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng bài hát trong danh sách.
        return null == mListSongs ? 0 : mListSongs.size();
    }

    // *** Lớp SongViewHolder ***
    // ViewHolder đại diện cho từng mục bài hát trong RecyclerView.
    public static class SongViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongBinding mItemSongBinding;
        // Binding cho từng mục bài hát.

        public SongViewHolder(ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemSongBinding = itemSongBinding; // Gán binding cho ViewHolder.
        }
    }
}
