package com.pro.music.fragment;
// Định nghĩa package chứa Fragment.

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
// Import các thư viện Android để làm việc với giao diện và Toast.

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// Import lớp Fragment để quản lý giao diện.

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// Import Firebase Authentication để thay đổi mật khẩu.

import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.databinding.FragmentChangePasswordBinding;
// Import các thành phần dự án như MainActivity và View Binding.

import com.pro.music.model.User;
import com.pro.music.prefs.DataStoreManager;
import com.pro.music.utils.StringUtil;
// Import các lớp User, DataStoreManager và StringUtil để xử lý logic.

public class ChangePasswordFragment extends Fragment {
    // Fragment để xử lý thay đổi mật khẩu.

    private FragmentChangePasswordBinding mFragmentChangePasswordBinding;
    // Biến View Binding để liên kết giao diện XML.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Phương thức khởi tạo giao diện Fragment.
        mFragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(inflater,
                container, false);
        // Liên kết giao diện XML với View Binding.

        initListener(); // Khởi tạo các sự kiện listener.

        return mFragmentChangePasswordBinding.getRoot();
        // Trả về root view của Fragment.
    }

    private void initListener() {
        // Khởi tạo sự kiện click vào nút "Thay đổi mật khẩu".
        mFragmentChangePasswordBinding.btnChangePassword
                .setOnClickListener(v -> onClickValidateChangePassword());
    }

    private void onClickValidateChangePassword() {
        // Phương thức xử lý logic kiểm tra khi người dùng nhấn nút thay đổi mật khẩu.
        if (getActivity() == null) return;

        // Lấy các giá trị mật khẩu từ các ô nhập liệu.
        String strOldPassword = mFragmentChangePasswordBinding.edtOldPassword.getText().toString().trim();
        String strNewPassword = mFragmentChangePasswordBinding.edtNewPassword.getText().toString().trim();
        String strConfirmPassword = mFragmentChangePasswordBinding.edtConfirmPassword.getText().toString().trim();

        // Kiểm tra các trường hợp nhập liệu.
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        } else if (!DataStoreManager.getUser().getPassword().equals(strOldPassword)) {
            // Kiểm tra mật khẩu cũ có khớp với dữ liệu không.
            Toast.makeText(getActivity(),
                    getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (!strNewPassword.equals(strConfirmPassword)) {
            // Kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp nhau không.
            Toast.makeText(getActivity(),
                    getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (strOldPassword.equals(strNewPassword)) {
            // Kiểm tra xem mật khẩu mới có khác mật khẩu cũ không.
            Toast.makeText(getActivity(),
                    getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        } else {
            changePassword(strNewPassword);
            // Nếu mọi điều kiện đều thỏa mãn, tiến hành thay đổi mật khẩu.
        }
    }

    private void changePassword(String newPassword) {
        // Phương thức thực hiện thay đổi mật khẩu.
        if (getActivity() == null) return;

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showProgressDialog(true);
        // Hiển thị Progress Dialog để thông báo đang xử lý.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Lấy người dùng hiện tại từ Firebase Authentication.
        if (user == null) {
            return;
        }

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    mainActivity.showProgressDialog(false);
                    if (task.isSuccessful()) {
                        // Nếu thay đổi mật khẩu thành công:
                        Toast.makeText(mainActivity,
                                getString(R.string.msg_change_password_successfully),
                                Toast.LENGTH_SHORT).show();

                        // Cập nhật mật khẩu mới trong thông tin người dùng lưu trữ.
                        User userLogin = DataStoreManager.getUser();
                        userLogin.setPassword(newPassword);
                        DataStoreManager.setUser(userLogin);

                        // Xóa nội dung trong các ô nhập liệu.
                        mFragmentChangePasswordBinding.edtOldPassword.setText("");
                        mFragmentChangePasswordBinding.edtNewPassword.setText("");
                        mFragmentChangePasswordBinding.edtConfirmPassword.setText("");
                    }
                });
    }
}
