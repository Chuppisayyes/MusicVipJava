// File: BaseActivity.java

// Khai báo package của ứng dụng
package com.pro.music.activity;

// Import các thư viện cần thiết
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pro.music.R;

// Lớp trừu tượng BaseActivity, dùng làm lớp cha cho các Activity khác
public abstract class BaseActivity extends AppCompatActivity {
    // Hai hộp thoại: một để hiển thị trạng thái chờ (progress), một để hiển thị thông báo (alert)
    protected MaterialDialog progressDialog, alertDialog;

    // Phương thức onCreate khởi tạo các thành phần cần thiết khi Activity được tạo
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog(); // Tạo hộp thoại chờ
        createAlertDialog();    // Tạo hộp thoại thông báo
    }

    // Tạo hộp thoại chờ (ProgressDialog)
    public void createProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.msg_please_waiting) // Nội dung "Vui lòng chờ"
                .progress(true, 0) // Hộp thoại hiển thị trạng thái chờ
                .build(); // Xây dựng hộp thoại
    }

    // Hiển thị hoặc ẩn hộp thoại chờ
    public void showProgressDialog(boolean value) {
        if (value) { // Nếu giá trị là true, hiển thị hộp thoại chờ
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false); // Không cho phép hủy hộp thoại khi nhấn bên ngoài
            }
        } else { // Nếu giá trị là false, ẩn hộp thoại chờ
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    // Ẩn cả hộp thoại chờ và hộp thoại thông báo nếu chúng đang hiển thị
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    // Tạo hộp thoại thông báo (AlertDialog)
    public void createAlertDialog() {
        alertDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name) // Tiêu đề là tên ứng dụng
                .positiveText(R.string.action_ok) // Nút "OK"
                .cancelable(false) // Không cho phép hủy hộp thoại
                .build(); // Xây dựng hộp thoại
    }

    // Hiển thị hộp thoại thông báo với thông điệp dạng chuỗi
    public void showAlertDialog(String errorMessage) {
        alertDialog.setContent(errorMessage); // Thiết lập nội dung thông báo
        alertDialog.show(); // Hiển thị hộp thoại
    }

    // Hiển thị hộp thoại thông báo với thông điệp từ tài nguyên chuỗi
    public void showAlertDialog(@StringRes int resourceId) {
        alertDialog.setContent(resourceId); // Thiết lập nội dung từ tài nguyên
        alertDialog.show(); // Hiển thị hộp thoại
    }

    // Cài đặt cho phép hoặc không cho phép hủy hộp thoại chờ
    public void setCancelProgress(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel); // Cho phép hủy nếu isCancel = true
        }
    }

    // Phương thức onDestroy để dọn dẹp khi Activity bị hủy
    @Override
    protected void onDestroy() {
        // Ẩn hộp thoại chờ nếu nó đang hiển thị
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        // Ẩn hộp thoại thông báo nếu nó đang hiển thị
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy(); // Gọi phương thức hủy của lớp cha
    }
}
