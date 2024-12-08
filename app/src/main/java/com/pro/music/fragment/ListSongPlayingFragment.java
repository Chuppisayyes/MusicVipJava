package com.pro.music.fragment;
// Package chứa lớp `ListSongPlayingFragment`.

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thư viện cần thiết.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
// Import các công cụ hỗ trợ Fragment, RecyclerView, và Broadcast.

import com.pro.music.R;
import com.pro.music.adapter.SongPlayingAdapter;
import com.pro.music.constant.Constant;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentListSongPlayingBinding;
import com.pro.music.listener.IOnClickSongPlayingItemListener;
import com.pro.music.model.Song;
import com.pro.music.service.MusicService;
// Import các lớp Model, Service, và Listener.

public class ListSongPlayingFragment extends Fragment {
    // Fragment hiển thị danh sách bài hát đang phát.

    private FragmentListSongPlayingBinding mFragmentListSongPlayingBinding;
    // View Binding liên kết với layout XML.

    private SongPlayingAdapter mSongPlayingAdapter;
    // Adapter quản lý danh sách bài hát đang phát.

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        // BroadcastReceiver lắng nghe sự thay đổi danh sách phát.
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStatusListSongPlaying(); // Cập nhật trạng thái bài hát trong danh sách.
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Khởi tạo giao diện Fragment từ XML.
        mFragmentListSongPlayingBinding = FragmentListSongPlayingBinding.inflate(inflater, container, false);

        if (getActivity() != null) {
            // Đăng ký lắng nghe sự kiện thay đổi danh sách phát.
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                    new IntentFilter(Constant.CHANGE_LISTENER));
        }
        displayListSongPlaying(); // Hiển thị danh sách bài hát đang phát.

        return mFragmentListSongPlayingBinding.getRoot();
    }

    private void displayListSongPlaying() {
        // Hiển thị danh sách bài hát đang phát lên RecyclerView.
        if (getActivity() == null || MusicService.mListSongPlaying == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentListSongPlayingBinding.rcvData.setLayoutManager(linearLayoutManager);

        mSongPlayingAdapter = new SongPlayingAdapter(MusicService.mListSongPlaying,
                new IOnClickSongPlayingItemListener() {
                    @Override
                    public void onClickItemSongPlaying(int position) {
                        // Xử lý khi người dùng chọn bài hát để phát.
                        clickItemSongPlaying(position);
                    }

                    @Override
                    public void onClickRemoveFromPlaylist(int position) {
                        // Xử lý khi người dùng muốn xóa bài hát khỏi danh sách.
                        deleteSongFromPlaylist(position);
                    }
                });
        mFragmentListSongPlayingBinding.rcvData.setAdapter(mSongPlayingAdapter);

        updateStatusListSongPlaying(); // Cập nhật trạng thái hiển thị danh sách.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusListSongPlaying() {
        // Cập nhật trạng thái của các bài hát trong danh sách (đang phát hoặc không).
        if (getActivity() == null || MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
            return;
        }
        for (int i = 0; i < MusicService.mListSongPlaying.size(); i++) {
            MusicService.mListSongPlaying.get(i).setPlaying(i == MusicService.mSongPosition);
        }
        mSongPlayingAdapter.notifyDataSetChanged(); // Thông báo dữ liệu thay đổi.
    }

    private void clickItemSongPlaying(int position) {
        // Phát bài hát được chọn.
        MusicService.isPlaying = false; // Đặt trạng thái không phát trước khi bắt đầu bài hát mới.
        GlobalFunction.startMusicService(getActivity(), Constant.PLAY, position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteSongFromPlaylist(int position) {
        // Xóa bài hát khỏi danh sách phát.
        if (getActivity() == null) return;
        if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
            return;
        }
        Song songDelete = MusicService.mListSongPlaying.get(position);
        new AlertDialog.Builder(getActivity())
                .setTitle(songDelete.getTitle()) // Hiển thị tiêu đề bài hát.
                .setMessage(getString(R.string.msg_confirm_delete)) // Hiển thị thông báo xác nhận.
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (MusicService.isSongPlaying(songDelete.getId())) {
                        // Nếu bài hát đang phát, không cho phép xóa.
                        GlobalFunction.showToastMessage(getActivity(),
                                getActivity().getString(R.string.msg_cannot_delete_song));
                    } else {
                        // Nếu không, xóa bài hát khỏi danh sách.
                        MusicService.deleteSongFromPlaylist(songDelete.getId());
                        if (mSongPlayingAdapter != null) mSongPlayingAdapter.notifyDataSetChanged();
                        GlobalFunction.showToastMessage(getActivity(),
                                getActivity().getString(R.string.msg_delete_song_from_playlist_success));
                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), null) // Hủy bỏ thao tác.
                .show();
    }

    @Override
    public void onDestroy() {
        // Hủy đăng ký BroadcastReceiver khi Fragment bị hủy.
        super.onDestroy();
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }
}
