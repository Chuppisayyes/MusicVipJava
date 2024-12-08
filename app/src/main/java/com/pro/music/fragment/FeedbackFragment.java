package com.pro.music.fragment;
// Package chứa lớp `FeedbackFragment`.

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import các thư viện để xử lý giao diện và Fragment.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// Import lớp cơ sở Fragment.

import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.constant.GlobalFunction;
import com.pro.music.databinding.FragmentFeedbackBinding;
import com.pro.music.model.Feedback;
import com.pro.music.prefs.DataStoreManager;
import com.pro.music.utils.StringUtil;
// Import các lớp và thư viện hỗ trợ.

public class FeedbackFragment extends Fragment {
    // Fragment cho phép người dùng gửi phản hồi.

    private FragmentFeedbackBinding mFragmentFeedbackBinding;
    // Sử dụng View Binding để liên kết giao diện XML.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Khởi tạo giao diện Fragment từ tệp XML.
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false);

        // Đặt email mặc định từ thông tin người dùng đã đăng nhập.
        mFragmentFeedbackBinding.edtEmail.setText(DataStoreManager.getUser().getEmail());
        // Gán sự kiện cho nút gửi phản hồi.
        mFragmentFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());

        return mFragmentFeedbackBinding.getRoot();
        // Trả về root view của Fragment.
    }

    private void onClickSendFeedback() {
        // Xử lý khi người dùng nhấn gửi phản hồi.

        if (getActivity() == null) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();

        // Lấy dữ liệu từ các trường nhập liệu.
        String strName = mFragmentFeedbackBinding.edtName.getText().toString();
        String strPhone = mFragmentFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mFragmentFeedbackBinding.edtEmail.getText().toString();
        String strComment = mFragmentFeedbackBinding.edtComment.getText().toString();

        // Kiểm tra dữ liệu nhập vào.
        if (StringUtil.isEmpty(strName)) {
            // Nếu tên để trống, hiển thị thông báo lỗi.
            GlobalFunction.showToastMessage(activity, getString(R.string.name_require));
        } else if (StringUtil.isEmpty(strComment)) {
            // Nếu nội dung phản hồi để trống, hiển thị thông báo lỗi.
            GlobalFunction.showToastMessage(activity, getString(R.string.comment_require));
        } else {
            // Nếu dữ liệu hợp lệ, tiến hành gửi phản hồi.
            activity.showProgressDialog(true); // Hiển thị thanh tiến trình.
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment);
            // Tạo đối tượng `Feedback` với dữ liệu vừa nhập.

            MyApplication.get(getActivity()).getFeedbackDatabaseReference()
                    .child(String.valueOf(System.currentTimeMillis()))
                    .setValue(feedback, (databaseError, databaseReference) -> {
                        activity.showProgressDialog(false); // Ẩn thanh tiến trình.
                        sendFeedbackSuccess(); // Gửi phản hồi thành công.
                    });
        }
    }

    public void sendFeedbackSuccess() {
        // Xử lý khi phản hồi được gửi thành công.

        GlobalFunction.hideSoftKeyboard(getActivity());
        // Ẩn bàn phím nếu đang mở.

        GlobalFunction.showToastMessage(getActivity(),
                getString(R.string.msg_send_feedback_success));
        // Hiển thị thông báo thành công.

        // Xóa dữ liệu trong các trường nhập liệu.
        mFragmentFeedbackBinding.edtName.setText("");
        mFragmentFeedbackBinding.edtPhone.setText("");
        mFragmentFeedbackBinding.edtComment.setText("");
    }
}
