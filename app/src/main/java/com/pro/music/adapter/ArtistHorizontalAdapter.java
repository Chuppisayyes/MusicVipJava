package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý dữ liệu nghệ sĩ hiển thị ngang.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemArtistHorizontalBinding;
// Import file binding cho layout của từng nghệ sĩ hiển thị ngang.

import com.pro.music.listener.IOnClickArtistItemListener;
// Import interface lắng nghe sự kiện khi người dùng nhấn vào nghệ sĩ.

import com.pro.music.model.Artist;
// Import lớp `Artist`, đại diện cho nghệ sĩ.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách nghệ sĩ.

// *** Lớp ArtistHorizontalAdapter ***
// Adapter này quản lý danh sách nghệ sĩ hiển thị theo chiều ngang (horizontal).
public class ArtistHorizontalAdapter extends RecyclerView.Adapter<ArtistHorizontalAdapter.ArtistHorizontalViewHolder> {

    private final List<Artist> mListArtist;
    // Danh sách các nghệ sĩ cần hiển thị.

    public final IOnClickArtistItemListener iOnClickArtistItemListener;
    // Listener để xử lý sự kiện khi nhấn vào một nghệ sĩ.

    // *** Constructor ***
    public ArtistHorizontalAdapter(List<Artist> list, IOnClickArtistItemListener listener) {
        this.mListArtist = list; // Gán danh sách nghệ sĩ.
        this.iOnClickArtistItemListener = listener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public ArtistHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục nghệ sĩ hiển thị ngang.
        ItemArtistHorizontalBinding itemArtistHorizontalBinding = ItemArtistHorizontalBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistHorizontalViewHolder(itemArtistHorizontalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHorizontalViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục nghệ sĩ trong danh sách.
        Artist artist = mListArtist.get(position); // Lấy nghệ sĩ tại vị trí `position`.
        if (artist == null) return; // Nếu nghệ sĩ null, không làm gì.

        // Hiển thị ảnh nghệ sĩ bằng GlideUtils.
        GlideUtils.loadUrl(artist.getImage(), holder.mItemArtistHorizontalBinding.imgArtist);

        // Hiển thị tên nghệ sĩ.
        holder.mItemArtistHorizontalBinding.tvArtist.setText(artist.getName());

        // Gắn sự kiện khi nhấn vào mục nghệ sĩ.
        holder.mItemArtistHorizontalBinding.layoutItem.setOnClickListener(v ->
                iOnClickArtistItemListener.onClickItemArtist(artist));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng nghệ sĩ trong danh sách.
        return null == mListArtist ? 0 : mListArtist.size();
    }

    // *** Lớp ArtistHorizontalViewHolder ***
    // ViewHolder đại diện cho từng mục nghệ sĩ hiển thị ngang trong RecyclerView.
    public static class ArtistHorizontalViewHolder extends RecyclerView.ViewHolder {

        private final ItemArtistHorizontalBinding mItemArtistHorizontalBinding;
        // Binding cho từng mục nghệ sĩ.

        public ArtistHorizontalViewHolder(ItemArtistHorizontalBinding itemArtistHorizontalBinding) {
            super(itemArtistHorizontalBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemArtistHorizontalBinding = itemArtistHorizontalBinding; // Gán binding cho ViewHolder.
        }
    }
}
