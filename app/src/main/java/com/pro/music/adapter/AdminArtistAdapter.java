package com.pro.music.adapter;
// Định nghĩa package chứa lớp adapter. Thư mục này tập trung các adapter phục vụ RecyclerView.

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.music.databinding.ItemAdminArtistBinding;
// Import lớp binding được tự động sinh ra từ file XML `item_admin_artist.xml`.

import com.pro.music.listener.IOnAdminManagerArtistListener;
// Import interface để xử lý sự kiện từ các mục trong danh sách.

import com.pro.music.model.Artist;
// Import lớp `Artist` đại diện cho dữ liệu nghệ sĩ.

import com.pro.music.utils.GlideUtils;
// Import công cụ hỗ trợ tải và hiển thị hình ảnh từ URL.

import java.util.List;
// Import thư viện hỗ trợ danh sách.

public class AdminArtistAdapter extends RecyclerView.Adapter<AdminArtistAdapter.AdminArtistViewHolder> {
    // Lớp adapter này được thiết kế để hiển thị danh sách nghệ sĩ trong giao diện quản trị.

    private final List<Artist> mListArtist; // Danh sách nghệ sĩ cần hiển thị.
    private final IOnAdminManagerArtistListener mListener; // Listener xử lý sự kiện từ người dùng.

    // *** Constructor ***
    // Khởi tạo adapter với danh sách nghệ sĩ và listener.
    public AdminArtistAdapter(List<Artist> list, IOnAdminManagerArtistListener listener) {
        this.mListArtist = list; // Gán danh sách nghệ sĩ.
        this.mListener = listener; // Gán listener để xử lý sự kiện.
    }

    // *** Phương thức tạo ViewHolder ***
    @NonNull
    @Override
    public AdminArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate file XML layout `item_admin_artist.xml` để tạo ViewHolder.
        ItemAdminArtistBinding binding = ItemAdminArtistBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminArtistViewHolder(binding); // Trả về một ViewHolder mới.
    }

    // *** Phương thức liên kết dữ liệu với ViewHolder ***
    @Override
    public void onBindViewHolder(@NonNull AdminArtistViewHolder holder, int position) {
        Artist artist = mListArtist.get(position); // Lấy nghệ sĩ tại vị trí `position`.
        if (artist == null) return; // Nếu dữ liệu null, không làm gì.

        // Hiển thị hình ảnh nghệ sĩ bằng GlideUtils.
        GlideUtils.loadUrl(artist.getImage(), holder.itemBinding.imgArtist);

        // Hiển thị tên nghệ sĩ.
        holder.itemBinding.tvName.setText(artist.getName());

        // Đặt sự kiện nhấn vào nút "Edit" (Cập nhật nghệ sĩ).
        holder.itemBinding.imgEdit.setOnClickListener(v -> mListener.onClickUpdateArtist(artist));

        // Đặt sự kiện nhấn vào nút "Delete" (Xóa nghệ sĩ).
        holder.itemBinding.imgDelete.setOnClickListener(v -> mListener.onClickDeleteArtist(artist));

        // Đặt sự kiện nhấn vào toàn bộ mục (Xem chi tiết nghệ sĩ).
        holder.itemBinding.layoutItem.setOnClickListener(v -> mListener.onClickDetailArtist(artist));
    }

    // *** Phương thức trả về số lượng mục trong danh sách ***
    @Override
    public int getItemCount() {
        if (mListArtist != null) {
            return mListArtist.size(); // Trả về kích thước danh sách nếu không null.
        }
        return 0; // Nếu danh sách null, trả về 0.
    }

    // *** Lớp AdminArtistViewHolder ***
    // Lớp này giữ các view trong mỗi mục của RecyclerView.
    public static class AdminArtistViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminArtistBinding itemBinding; // Binding cho layout `item_admin_artist`.

        public AdminArtistViewHolder(@NonNull ItemAdminArtistBinding binding) {
            super(binding.getRoot()); // Khởi tạo ViewHolder với root view của layout.
            this.itemBinding = binding; // Gán binding để truy cập các view trong layout.
        }
    }
}
