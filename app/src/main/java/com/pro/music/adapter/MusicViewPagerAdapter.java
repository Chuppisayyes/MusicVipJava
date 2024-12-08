package com.pro.music.adapter;
// Định nghĩa package chứa adapter. Adapter này quản lý các Fragment trong ViewPager2.

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
// Import các thành phần cần thiết để quản lý Fragment trong ViewPager2.

import com.pro.music.fragment.ListSongPlayingFragment;
import com.pro.music.fragment.PlaySongFragment;
// Import các Fragment được hiển thị trong ViewPager2.

// *** Lớp MusicViewPagerAdapter ***
// Adapter này quản lý các Fragment trong ViewPager2 của trình phát nhạc.
public class MusicViewPagerAdapter extends FragmentStateAdapter {

    // *** Constructor ***
    public MusicViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity); // Gọi constructor của lớp cha FragmentStateAdapter.
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Phương thức này trả về Fragment tương ứng với vị trí `position` trong ViewPager2.
        if (position == 0) {
            // Nếu `position` là 0, trả về Fragment danh sách bài hát đang phát.
            return new ListSongPlayingFragment();
        }
        // Nếu không, trả về Fragment giao diện phát bài hát.
        return new PlaySongFragment();
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng Fragment trong ViewPager2.
        return 2; // Có 2 Fragment: danh sách bài hát và giao diện phát bài hát.
    }
}
