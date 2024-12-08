package com.pro.music.fragment.admin;
// Định nghĩa package chứa fragment. Fragment này quản lý thông tin tài khoản quản trị viên.

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
// Import các thành phần cơ bản của Fragment.

import com.google.firebase.auth.FirebaseAuth;
// Import FirebaseAuth để xử lý việc đăng xuất.

import com.pro.music.activity.AdminChangePasswordActivity;
import com.pro.music.activity.SignInActivity;
// Import các Activity để chuyển hướng sau khi thực hiện hành động.

import com.pro.music.constant.GlobalFunction;
// Import các hàm tiện ích toàn cục.

import com.pro.music.databinding.FragmentAdminAccountBinding;
// Import View Binding cho layout của fragment này.

import com.pro.music.prefs.DataStoreManager;
// Import DataStoreManager để quản lý thông tin người dùng.

// *** Lớp AdminAccountFragment ***
// Fragment này hiển thị và quản lý thông tin tài khoản của quản trị viên.
public class AdminAccountFragment extends Fragment {

    private FragmentAdminAccountBinding binding;
    // Biến binding để liên kết layout với code.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Phương thức tạo giao diện cho Fragment.
        binding = FragmentAdminAccountBinding.inflate(inflater, container, false); // Liên kết layout với binding.

        initUi(); // Khởi tạo giao diện người dùng.

        return binding.getRoot(); // Trả về root view của Fragment.
    }

    private void initUi() {
        // Phương thức khởi tạo giao diện người dùng.

        // Hiển thị email của người dùng trong TextView.
        binding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        // Gắn sự kiện khi người dùng nhấn vào "Change Password".
        binding.tvChangePassword.setOnClickListener(v -> onClickChangePassword());

        // Gắn sự kiện khi người dùng nhấn vào "Sign Out".
        binding.tvSignOut.setOnClickListener(v -> onClickSignOut());
    }

    private void onClickChangePassword() {
        // Phương thức xử lý sự kiện nhấn vào "Change Password".
        GlobalFunction.startActivity(getActivity(), AdminChangePasswordActivity.class);
        // Chuyển đến Activity đổi mật khẩu.
    }

    private void onClickSignOut() {
        // Phương thức xử lý sự kiện nhấn vào "Sign Out".
        if (getActivity() == null) return; // Nếu Fragment không gắn với Activity, thoát khỏi phương thức.

        FirebaseAuth.getInstance().signOut(); // Đăng xuất người dùng khỏi Firebase.
        DataStoreManager.setUser(null); // Xóa thông tin người dùng khỏi bộ nhớ cục bộ.

        // Chuyển hướng người dùng đến màn hình đăng nhập.
        GlobalFunction.startActivity(getActivity(), SignInActivity.class);

        getActivity().finishAffinity(); // Xóa toàn bộ stack của Activity để ngăn quay lại màn hình trước.
    }
}
