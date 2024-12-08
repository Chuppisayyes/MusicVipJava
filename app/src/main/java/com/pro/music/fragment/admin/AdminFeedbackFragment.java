package com.pro.music.fragment.admin;
// Định nghĩa package chứa Fragment quản lý phản hồi.

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thành phần để tạo giao diện Fragment.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
// Import Fragment và RecyclerView để hiển thị danh sách phản hồi.

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
// Import Firebase để lấy dữ liệu phản hồi từ cơ sở dữ liệu.

import com.pro.music.MyApplication;
// Import lớp MyApplication để lấy tham chiếu cơ sở dữ liệu Firebase.

import com.pro.music.adapter.AdminFeedbackAdapter;
// Import Adapter hiển thị danh sách phản hồi.

import com.pro.music.databinding.FragmentAdminFeedbackBinding;
// Import View Binding để liên kết layout với code.

import com.pro.music.model.Feedback;
// Import lớp Feedback, đại diện cho phản hồi từ người dùng.

import java.util.ArrayList;
import java.util.List;
// Import các danh sách để quản lý phản hồi.

// *** Lớp AdminFeedbackFragment ***
// Fragment này hiển thị danh sách phản hồi từ người dùng.
public class AdminFeedbackFragment extends Fragment {

    private FragmentAdminFeedbackBinding binding;
    // Biến `binding` để liên kết với layout.

    private List<Feedback> mListFeedback;
    // Danh sách phản hồi sẽ được hiển thị trên giao diện.

    private AdminFeedbackAdapter mFeedbackAdapter;
    // Adapter để quản lý và hiển thị danh sách phản hồi trong RecyclerView.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện cho Fragment.
        binding = FragmentAdminFeedbackBinding.inflate(inflater, container, false); // Liên kết layout với binding.

        initView(); // Khởi tạo giao diện.
        loadListFeedback(); // Tải danh sách phản hồi từ Firebase.

        return binding.getRoot(); // Trả về root view của Fragment.
    }

    private void initView() {
        // Phương thức khởi tạo RecyclerView và các thành phần liên quan.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvFeedback.setLayoutManager(linearLayoutManager); // Đặt LayoutManager cho RecyclerView.

        mListFeedback = new ArrayList<>(); // Khởi tạo danh sách phản hồi trống.
        mFeedbackAdapter = new AdminFeedbackAdapter(mListFeedback); // Tạo adapter cho danh sách phản hồi.
        binding.rcvFeedback.setAdapter(mFeedbackAdapter); // Gắn adapter vào RecyclerView.
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadListFeedback() {
        // Phương thức tải danh sách phản hồi từ Firebase.
        if (getActivity() == null) return; // Kiểm tra xem Fragment có đang gắn vào Activity hay không.

        MyApplication.get(getActivity()).getFeedbackDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    // Thêm ValueEventListener để lắng nghe thay đổi dữ liệu từ Firebase.
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Được gọi khi dữ liệu trong Firebase thay đổi.
                        clearListFeedback(); // Xóa danh sách phản hồi hiện tại.

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Lặp qua từng phản hồi trong Firebase.
                            Feedback feedback = dataSnapshot.getValue(Feedback.class);
                            // Chuyển đổi dữ liệu từ Firebase thành đối tượng Feedback.
                            if (feedback != null) {
                                mListFeedback.add(0, feedback); // Thêm phản hồi mới vào danh sách.
                            }
                        }

                        if (mFeedbackAdapter != null) mFeedbackAdapter.notifyDataSetChanged();
                        // Thông báo cho adapter rằng dữ liệu đã thay đổi để cập nhật giao diện.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Được gọi khi có lỗi xảy ra trong quá trình lấy dữ liệu từ Firebase.
                    }
                });
    }

    private void clearListFeedback() {
        // Phương thức xóa danh sách phản hồi hiện tại.
        if (mListFeedback != null) {
            mListFeedback.clear(); // Xóa tất cả phản hồi trong danh sách.
        } else {
            mListFeedback = new ArrayList<>(); // Khởi tạo danh sách phản hồi mới.
        }
    }
}
