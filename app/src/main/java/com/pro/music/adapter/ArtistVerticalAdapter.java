package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý dữ liệu nghệ sĩ hiển thị dọc (vertical).

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.databinding.ItemArtistVerticalBinding;
// Import file binding cho layout của từng nghệ sĩ hiển thị dọc.

import com.pro.music.listener.IOnClickArtistItemListener;
// Import interface lắng nghe sự kiện khi người dùng nhấn vào nghệ sĩ.

import com.pro.music.model.Artist;
// Import lớp `Artist`, đại diện cho nghệ sĩ.

import com.pro.music.utils.GlideUtils;
// Import tiện ích GlideUtils để tải và hiển thị hình ảnh.

import java.util.List;
// Import List để quản lý danh sách nghệ sĩ.

// *** Lớp ArtistVerticalAdapter ***
// Adapter này quản lý danh sách nghệ sĩ hiển thị theo chiều dọc (vertical).
public class ArtistVerticalAdapter extends RecyclerView.Adapter<ArtistVerticalAdapter.ArtistVerticalViewHolder> {

    private final List<Artist> mListArtist;
    // Danh sách các nghệ sĩ cần hiển thị.

    public final IOnClickArtistItemListener iOnClickArtistItemListener;
    // Listener để xử lý sự kiện khi nhấn vào một nghệ sĩ.

    // *** Constructor ***
    public ArtistVerticalAdapter(List<Artist> list, IOnClickArtistItemListener listener) {
        this.mListArtist = list; // Gán danh sách nghệ sĩ.
        this.iOnClickArtistItemListener = listener; // Gán listener xử lý sự kiện.
    }

    @NonNull
    @Override
    public ArtistVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục nghệ sĩ hiển thị dọc.
        ItemArtistVerticalBinding itemArtistVerticalBinding = ItemArtistVerticalBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistVerticalViewHolder(itemArtistVerticalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistVerticalViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục nghệ sĩ trong danh sách.
        Artist artist = mListArtist.get(position); // Lấy nghệ sĩ tại vị trí `position`.
        if (artist == null) return; // Nếu nghệ sĩ null, không làm gì.

        // Hiển thị ảnh nghệ sĩ bằng GlideUtils.
        GlideUtils.loadUrl(artist.getImage(), holder.mItemArtistVerticalBinding.imgArtist);

        // Hiển thị tên nghệ sĩ.
        holder.mItemArtistVerticalBinding.tvArtist.setText(artist.getName());

        // Gắn sự kiện khi nhấn vào mục nghệ sĩ.
        holder.mItemArtistVerticalBinding.layoutItem.setOnClickListener(v ->
                iOnClickArtistItemListener.onClickItemArtist(artist));
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng nghệ sĩ trong danh sách.
        return null == mListArtist ? 0 : mListArtist.size();
    }

    // *** Lớp ArtistVerticalViewHolder ***
    // ViewHolder đại diện cho từng mục nghệ sĩ hiển thị dọc trong RecyclerView.
    public static class ArtistVerticalViewHolder extends RecyclerView.ViewHolder {

        private final ItemArtistVerticalBinding mItemArtistVerticalBinding;
        // Binding cho từng mục nghệ sĩ.

        public ArtistVerticalViewHolder(ItemArtistVerticalBinding itemArtistVerticalBinding) {
            super(itemArtistVerticalBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemArtistVerticalBinding = itemArtistVerticalBinding; // Gán binding cho ViewHolder.
        }
    }
}
