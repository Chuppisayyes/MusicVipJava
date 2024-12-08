package com.pro.music.fragment;
// Package chứa ContactFragment.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thư viện cần thiết cho giao diện và Intent.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
// Import các lớp Fragment và RecyclerView Layout Manager.

import com.pro.music.R;
import com.pro.music.adapter.ContactAdapter;
import com.pro.music.constant.AboutUsConfig;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentContactBinding;
import com.pro.music.model.Contact;
// Import các lớp và thư viện cần thiết cho hoạt động của Fragment.

import java.util.ArrayList;
import java.util.List;
// Import danh sách để quản lý dữ liệu liên lạc.

public class ContactFragment extends Fragment {
    // Fragment hiển thị giao diện thông tin liên hệ.

    private FragmentContactBinding mFragmentContactBinding;
    // Sử dụng View Binding để liên kết giao diện XML.

    private ContactAdapter mContactAdapter;
    // Adapter để hiển thị danh sách liên lạc.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Khởi tạo Fragment và liên kết giao diện.
        mFragmentContactBinding = FragmentContactBinding.inflate(inflater, container, false);

        initUi();       // Khởi tạo giao diện.
        initListener(); // Khởi tạo các sự kiện click.

        return mFragmentContactBinding.getRoot();
        // Trả về root view của Fragment.
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện.

        // Thiết lập tiêu đề và nội dung phần "Giới thiệu về chúng tôi".
        mFragmentContactBinding.tvAboutUsTitle.setText(AboutUsConfig.ABOUT_US_TITLE);
        mFragmentContactBinding.tvAboutUsContent.setText(AboutUsConfig.ABOUT_US_CONTENT);
        mFragmentContactBinding.tvAboutUsWebsite.setText(AboutUsConfig.ABOUT_US_WEBSITE_TITLE);

        // Thiết lập Adapter để hiển thị danh sách liên lạc.
        mContactAdapter = new ContactAdapter(getActivity(), getListContact(),
                () -> GlobalFunction.callPhoneNumber(getActivity()));
        // Gọi hàm callPhoneNumber khi người dùng chọn Hotline.

        // Sử dụng GridLayoutManager để hiển thị danh sách liên lạc theo dạng lưới.
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mFragmentContactBinding.rcvData.setNestedScrollingEnabled(false);
        mFragmentContactBinding.rcvData.setFocusable(false);
        mFragmentContactBinding.rcvData.setLayoutManager(layoutManager);
        mFragmentContactBinding.rcvData.setAdapter(mContactAdapter);
    }

    private void initListener() {
        // Xử lý sự kiện khi người dùng click vào phần website.
        mFragmentContactBinding.layoutWebsite.setOnClickListener(view
                -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AboutUsConfig.WEBSITE))));
        // Mở trình duyệt với URL từ cấu hình AboutUsConfig.
    }

    public List<Contact> getListContact() {
        // Tạo danh sách các mục liên lạc.
        List<Contact> contactArrayList = new ArrayList<>();
        contactArrayList.add(new Contact(Contact.FACEBOOK, R.drawable.ic_facebook));
        contactArrayList.add(new Contact(Contact.HOTLINE, R.drawable.ic_hotline));
        contactArrayList.add(new Contact(Contact.GMAIL, R.drawable.ic_gmail));
        contactArrayList.add(new Contact(Contact.SKYPE, R.drawable.ic_skype));
        contactArrayList.add(new Contact(Contact.YOUTUBE, R.drawable.ic_youtube));
        contactArrayList.add(new Contact(Contact.ZALO, R.drawable.ic_zalo));

        return contactArrayList;
        // Trả về danh sách các mục liên lạc, mỗi mục gồm một ID và một biểu tượng.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactAdapter.release();
        // Giải phóng tài nguyên của Adapter khi Fragment bị hủy.
    }
}
