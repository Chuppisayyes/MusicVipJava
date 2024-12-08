package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý danh sách các thông tin liên lạc.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
// Import các thành phần của RecyclerView để quản lý danh sách.

import com.pro.music.R;
// Import các tài nguyên trong ứng dụng, ví dụ: chuỗi và hình ảnh.

import com.pro.music.constant.GlobalFunction;
// Import các hàm tiện ích toàn cục để xử lý các hành động liên quan đến liên lạc.

import com.pro.music.databinding.ItemContactBinding;
// Import file binding cho layout của từng thông tin liên lạc.

import com.pro.music.model.Contact;
// Import lớp `Contact`, đại diện cho từng mục liên lạc.

import java.util.List;
// Import List để quản lý danh sách các thông tin liên lạc.

// *** Lớp ContactAdapter ***
// Adapter này quản lý danh sách các thông tin liên lạc hiển thị trong RecyclerView.
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    // Biến lưu trữ context để sử dụng trong các hành động liên quan đến giao diện.

    private final List<Contact> listContact;
    // Danh sách các thông tin liên lạc cần hiển thị.

    private final ICallPhone iCallPhone;
    // Interface để xử lý sự kiện gọi điện thoại.

    // *** Interface ICallPhone ***
    // Interface được sử dụng để xử lý sự kiện khi nhấn vào mục Hotline.
    public interface ICallPhone {
        void onClickCallPhone();
    }

    // *** Constructor ***
    public ContactAdapter(Context context, List<Contact> listContact, ICallPhone iCallPhone) {
        this.context = context; // Gán context.
        this.listContact = listContact; // Gán danh sách thông tin liên lạc.
        this.iCallPhone = iCallPhone; // Gán interface xử lý sự kiện gọi điện thoại.
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo ViewHolder cho từng mục liên lạc.
        ItemContactBinding itemContactBinding = ItemContactBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(itemContactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Gắn dữ liệu cho từng mục liên lạc trong danh sách.
        Contact contact = listContact.get(position); // Lấy mục liên lạc tại vị trí `position`.
        if (contact == null) {
            return; // Nếu mục liên lạc null, không làm gì.
        }

        // Hiển thị hình ảnh của mục liên lạc.
        holder.mItemContactBinding.imgContact.setImageResource(contact.getImage());

        // Hiển thị tiêu đề của mục liên lạc dựa trên `id`.
        switch (contact.getId()) {
            case Contact.FACEBOOK:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_facebook));
                break;

            case Contact.HOTLINE:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_call));
                break;

            case Contact.GMAIL:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_gmail));
                break;

            case Contact.SKYPE:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_skype));
                break;

            case Contact.YOUTUBE:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_youtube));
                break;

            case Contact.ZALO:
                holder.mItemContactBinding.tvContact.setText(context.getString(R.string.label_zalo));
                break;
        }

        // Gắn sự kiện khi nhấn vào mục liên lạc.
        holder.mItemContactBinding.layoutItem.setOnClickListener(v -> {
            switch (contact.getId()) {
                case Contact.FACEBOOK:
                    GlobalFunction.onClickOpenFacebook(context); // Mở Facebook.
                    break;

                case Contact.HOTLINE:
                    iCallPhone.onClickCallPhone(); // Gọi điện thoại.
                    break;

                case Contact.GMAIL:
                    GlobalFunction.onClickOpenGmail(context); // Mở Gmail.
                    break;

                case Contact.SKYPE:
                    GlobalFunction.onClickOpenSkype(context); // Mở Skype.
                    break;

                case Contact.YOUTUBE:
                    GlobalFunction.onClickOpenYoutubeChannel(context); // Mở YouTube.
                    break;

                case Contact.ZALO:
                    GlobalFunction.onClickOpenZalo(context); // Mở Zalo.
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng mục liên lạc trong danh sách.
        return null == listContact ? 0 : listContact.size();
    }

    // *** Phương thức release ***
    // Giải phóng context khi không còn sử dụng adapter.
    public void release() {
        context = null;
    }

    // *** Lớp ContactViewHolder ***
    // ViewHolder đại diện cho từng mục liên lạc trong RecyclerView.
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private final ItemContactBinding mItemContactBinding;
        // Binding cho từng mục liên lạc.

        public ContactViewHolder(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot()); // Gọi constructor cha với root view của binding.
            this.mItemContactBinding = itemContactBinding; // Gán binding cho ViewHolder.
        }
    }
}
