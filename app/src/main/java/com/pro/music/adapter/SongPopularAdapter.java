package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý danh sách bài hát phổ biến (Popular Songs).

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.R;
// Import các tài nguyên, ví dụ: chuỗi và biểu tượng.

import com.pro.music.constant.GlobalFunction;
// Import các hàm tiện ích toàn cục để xử lý các hành động liên quan đến bài hát.

import com.pro.music.databinding.ItemSongPopularBinding;
// Import file binding cho layout của từng bài hát phổ biến.

import com.pro.music.listener.IOnClickSongItemListener;
// Import interface để lắng nghe sự kiện khi người dùng tương tác với bài hát.

import com.pro.music.model.Song;
// Import lớp `Song`, đại diện cho từng bài hát.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách bài hát.

// *** Lớp SongPopularAdapter ***
// Adapter này quản lý danh sách bài hát phổ biến trong RecyclerView.
public class SongPopularAdapter extends RecyclerView.Adapter<SongPopularAdapter.SongPopularViewHolder> {

    private Context mContext;
    // Context dùng để xử lý tài nguyên chuỗi.

    private final List<Song> mListSongs;
    // Danh sách các bài hát phổ biến.

    public final IOnClickSongItemListener iOnClickSongItemListener;
    // Listener để xử lý sự kiện khi người dùng tương tác với bài hát.

    // *** Constructor ***
    public SongPopularAdapter(Context context, List<Song> mListSongs, IOnClickSongItemListener iOnClickSongItemListener) {
        this.mContext = context; // Gán context.
        this.mListSongs = mListSongs; // Gán danh sách bài hát phổ biến.
        this.iOnClickSongItemListener = iOnClickSongItemListener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public SongPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục bài hát phổ biến.
        ItemSongPopularBinding itemSongPopularBinding = ItemSongPopularBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongPopularViewHolder(itemSongPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongPopularViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục bài hát trong danh sách.
        Song song = mListSongs.get(position); // Lấy bài hát tại vị trí `position`.
        if (song == null) {
            return; // Nếu bài hát null, không làm gì.
        }

        // Hiển thị hình ảnh bài hát bằng GlideUtils.
        GlideUtils.loadUrl(song.getImage(), holder.mItemSongPopularBinding.imgSong);

        // Hiển thị tên bài hát.
        holder.mItemSongPopularBinding.tvSongName.setText(song.getTitle());

        // Hiển thị tên nghệ sĩ.
        holder.mItemSongPopularBinding.tvArtist.setText(song.getArtist());

        // Hiển thị số lượt nghe bài hát.
        String strListen = mContext.getString(R.string.label_listen); // Lấy chuỗi "listen".
        if (song.getCount() > 1) {
            strListen = mContext.getString(R.string.label_listens); // Nếu nhiều lượt, đổi thành "listens".
        }
        String strCountListen = song.getCount() + " " + strListen; // Ghép số lượt nghe với chuỗi.
        holder.mItemSongPopularBinding.tvCountListen.setText(strCountListen);

        // Kiểm tra trạng thái yêu thích của bài hát.
        boolean isFavorite = GlobalFunction.isFavoriteSong(song);
        if (isFavorite) {
            holder.mItemSongPopularBinding.imgFavorite.setImageResource(R.drawable.ic_favorite); // Hiển thị biểu tượng yêu thích.
        } else {
            holder.mItemSongPopularBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite); // Hiển thị biểu tượng không yêu thích.
        }

        // Gắn sự kiện khi người dùng nhấn vào biểu tượng yêu thích.
        holder.mItemSongPopularBinding.imgFavorite.setOnClickListener(v ->
                iOnClickSongItemListener.onClickFavoriteSong(song, !isFavorite));

        // Gắn sự kiện khi người dùng nhấn vào biểu tượng tùy chọn thêm.
        holder.mItemSongPopularBinding.imgMoreOption.setOnClickListener(v ->
                iOnClickSongItemListener.onClickMoreOptions(song));

        // Gắn sự kiện khi người dùng nhấn vào thông tin bài hát.
        holder.mItemSongPopularBinding.layoutSongInfo.setOnClickListener(v ->
                iOnClickSongItemListener.onClickItemSong(song));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng bài hát trong danh sách.
        return null == mListSongs ? 0 : mListSongs.size();
    }

    // *** Phương thức release ***
    // Giải phóng context để tránh rò rỉ bộ nhớ.
    public void release() {
        if (mContext != null) {
            mContext = null;
        }
    }

    // *** Lớp SongPopularViewHolder ***
    // ViewHolder đại diện cho từng mục bài hát phổ biến trong RecyclerView.
    public static class SongPopularViewHolder extends RecyclerView.ViewHolder {

        private final ItemSongPopularBinding mItemSongPopularBinding;
        // Binding cho từng mục bài hát.

        public SongPopularViewHolder(ItemSongPopularBinding itemSongPopularBinding) {
            super(itemSongPopularBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemSongPopularBinding = itemSongPopularBinding; // Gán binding cho ViewHolder.
        }
    }
}
