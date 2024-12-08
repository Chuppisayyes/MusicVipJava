package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này kết nối dữ liệu với ViewPager2.

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
// Import các thành phần cần thiết để quản lý Fragment trong ViewPager2.

import com.pro.music.fragment.admin.AdminAccountFragment;
import com.pro.music.fragment.admin.AdminArtistFragment;
import com.pro.music.fragment.admin.AdminCategoryFragment;
import com.pro.music.fragment.admin.AdminFeedbackFragment;
import com.pro.music.fragment.admin.AdminSongFragment;
// Import các Fragment liên quan đến giao diện quản trị Admin.

// *** Lớp AdminViewPagerAdapter ***
// Adapter này quản lý các Fragment hiển thị trong ViewPager2 của giao diện Admin.
public class AdminViewPagerAdapter extends FragmentStateAdapter {

    // *** Constructor ***
    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity); // Gọi constructor của lớp cha FragmentStateAdapter.
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Phương thức này trả về Fragment tương ứng với vị trí `position` trong ViewPager2.
        switch (position) {
            case 1:
                return new AdminArtistFragment(); // Trả về Fragment quản lý nghệ sĩ.

            case 2:
                return new AdminSongFragment(); // Trả về Fragment quản lý bài hát.

            case 3:
                return new AdminFeedbackFragment(); // Trả về Fragment quản lý phản hồi.

            case 4:
                return new AdminAccountFragment(); // Trả về Fragment quản lý tài khoản.

            default:
                return new AdminCategoryFragment(); // Trả về Fragment quản lý danh mục (mặc định).
        }
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng Fragment trong ViewPager2.
        return 5; // Có 5 Fragment: danh mục, nghệ sĩ, bài hát, phản hồi, tài khoản.
    }
}
