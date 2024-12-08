package com.pro.music.constant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pro.music.MyApplication;
import com.pro.music.R;
import com.pro.music.activity.MainActivity;
import com.pro.music.activity.PlayMusicActivity;
import com.pro.music.databinding.LayoutBottomSheetOptionBinding;
import com.pro.music.model.Song;
import com.pro.music.model.UserInfor;
import com.pro.music.prefs.DataStoreManager;
import com.pro.music.service.MusicReceiver;
import com.pro.music.service.MusicService;
import com.pro.music.utils.GlideUtils;
import com.pro.music.utils.StringUtil;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Lớp tiện ích dùng để cung cấp các hàm dùng chung trong ứng dụng
public class GlobalFunction {

    // Phương thức để chuyển đổi giữa các Activity (không có dữ liệu bổ sung)
    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        // Gắn cờ xóa ngăn xếp Activity cũ và tạo mới
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent); // Bắt đầu Activity
    }

    // Phương thức để chuyển đổi giữa các Activity (kèm dữ liệu dạng Bundle)
    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle); // Đính kèm dữ liệu
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent); // Bắt đầu Activity
    }

    // Ẩn bàn phím khi không còn cần thiết
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            // Ẩn bàn phím từ cửa sổ hiện tại
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace(); // Xử lý lỗi nếu xảy ra
        }
    }

    // Mở ứng dụng Gmail để gửi email đến địa chỉ được cấu hình
    public static void onClickOpenGmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", AboutUsConfig.GMAIL, null));
        // Mở trình chọn ứng dụng để gửi email
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    // Mở ứng dụng Skype để trò chuyện
    public static void onClickOpenSkype(Context context) {
        try {
            // Xác định URI Skype
            Uri skypeUri = Uri.parse("skype:" + AboutUsConfig.SKYPE_ID + "?chat");
            context.getPackageManager().getPackageInfo("com.skype.raider", 0); // Kiểm tra Skype có được cài đặt không
            Intent skypeIntent = new Intent(Intent.ACTION_VIEW, skypeUri);
            skypeIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            context.startActivity(skypeIntent); // Mở ứng dụng Skype
        } catch (Exception e) {
            openSkypeWebview(context); // Nếu lỗi, mở Skype trên trình duyệt
        }
    }

    // Mở Skype trên trình duyệt nếu ứng dụng không khả dụng
    private static void openSkypeWebview(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("skype:" + AboutUsConfig.SKYPE_ID + "?chat")));
        } catch (Exception exception) {
            // Nếu cả Skype app và Skype web đều không khả dụng, mở Play Store
            String skypePackageName = "com.skype.raider";
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + skypePackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                // Nếu không mở được Play Store, mở trình duyệt với link Play Store
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + skypePackageName)));
            }
        }
    }

    // Mở Facebook bằng ứng dụng hoặc trình duyệt
    public static void onClickOpenFacebook(Context context) {
        Intent intent;
        try {
            String urlFacebook = AboutUsConfig.PAGE_FACEBOOK; // Lấy URL trang Facebook
            PackageManager packageManager = context.getPackageManager();
            // Kiểm tra phiên bản Facebook app
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { // Nếu phiên bản mới hơn
                urlFacebook = "fb://facewebmodal/f?href=" + AboutUsConfig.LINK_FACEBOOK; // Dùng giao thức fb://
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook));
        } catch (Exception e) {
            // Nếu không mở được ứng dụng Facebook, dùng trình duyệt
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AboutUsConfig.LINK_FACEBOOK));
        }
        context.startActivity(intent); // Mở Facebook
    }

    // Mở kênh YouTube bằng trình duyệt hoặc ứng dụng YouTube
    public static void onClickOpenYoutubeChannel(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AboutUsConfig.LINK_YOUTUBE)));
    }

    // Mở liên kết Zalo
    public static void onClickOpenZalo(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AboutUsConfig.ZALO_LINK)));
    }

    // Gọi điện thoại sau khi yêu cầu quyền truy cập
    public static void callPhoneNumber(Activity activity) {
        try {
            // Kiểm tra quyền gọi điện
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 101);
                return;
            }
            // Tạo Intent gọi điện thoại
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + AboutUsConfig.PHONE_NUMBER));
            activity.startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace(); // Xử lý lỗi nếu có
        }
    }

    // Hiển thị thông báo Toast
    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Chuẩn hóa chuỗi tìm kiếm bằng cách loại bỏ dấu tiếng Việt
    public static String getTextSearch(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll(""); // Loại bỏ các ký tự dấu
    }

    // Bắt đầu dịch vụ phát nhạc với hành động và vị trí bài hát
    public static void startMusicService(Context ctx, int action, int songPosition) {
        Intent musicService = new Intent(ctx, MusicService.class);
        musicService.putExtra(Constant.MUSIC_ACTION, action); // Hành động phát nhạc
        musicService.putExtra(Constant.SONG_POSITION, songPosition); // Vị trí bài hát
        ctx.startService(musicService); // Khởi động dịch vụ
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    public static PendingIntent openMusicReceiver(Context ctx, int action) {
        // Tạo Intent để gửi Broadcast cho MusicReceiver
        Intent intent = new Intent(ctx, MusicReceiver.class);
        // Gắn thêm thông tin hành động nhạc (play, pause, next, ...)
        intent.putExtra(Constant.MUSIC_ACTION, action);
        // Sử dụng PendingIntent để truyền Intent vào hệ thống Android
        int pendingFlag = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        // Trả về PendingIntent dùng cho Broadcast
        return PendingIntent.getBroadcast(ctx.getApplicationContext(), action, intent, pendingFlag);
    }

    public static boolean isFavoriteSong(Song song) {
        // Kiểm tra nếu danh sách yêu thích của bài hát trống thì trả về false
        if (song.getFavorite() == null || song.getFavorite().isEmpty()) return false;
        // Lấy danh sách người dùng đã thêm bài hát vào yêu thích
        List<UserInfor> listUsersFavorite = new ArrayList<>(song.getFavorite().values());
        if (listUsersFavorite.isEmpty()) return false;
        // Duyệt danh sách người dùng, kiểm tra nếu email trùng khớp với người dùng hiện tại
        for (UserInfor userInfor : listUsersFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userInfor.getEmailUser())) {
                return true; // Bài hát nằm trong danh sách yêu thích
            }
        }
        return false; // Bài hát không nằm trong danh sách yêu thích
    }

    public static UserInfor getUserFavoriteSong(Song song) {
        UserInfor userInfor = null;
        // Kiểm tra nếu danh sách yêu thích của bài hát trống thì trả về null
        if (song.getFavorite() == null || song.getFavorite().isEmpty()) return null;
        List<UserInfor> listUsersFavorite = new ArrayList<>(song.getFavorite().values());
        if (listUsersFavorite.isEmpty()) return null;
        // Duyệt danh sách người dùng yêu thích, tìm người dùng hiện tại
        for (UserInfor userObject : listUsersFavorite) {
            if (DataStoreManager.getUser().getEmail().equals(userObject.getEmailUser())) {
                userInfor = userObject; // Lấy thông tin người dùng yêu thích bài hát
                break;
            }
        }
        return userInfor; // Trả về thông tin người dùng
    }

    public static void onClickFavoriteSong(Context context, Song song, boolean isFavorite) {
        if (context == null) return; // Không làm gì nếu context null
        if (isFavorite) {
            // Nếu thêm vào yêu thích
            String userEmail = DataStoreManager.getUser().getEmail(); // Lấy email người dùng hiện tại
            UserInfor userInfor = new UserInfor(System.currentTimeMillis(), userEmail); // Tạo thông tin yêu thích
            // Thêm thông tin yêu thích vào Firebase Realtime Database
            MyApplication.get(context).getSongsDatabaseReference()
                    .child(String.valueOf(song.getId()))
                    .child("favorite")
                    .child(String.valueOf(userInfor.getId()))
                    .setValue(userInfor);
        } else {
            // Nếu xóa khỏi yêu thích
            UserInfor userInfor = getUserFavoriteSong(song); // Lấy thông tin người dùng
            if (userInfor != null) {
                // Xóa thông tin yêu thích từ Firebase Realtime Database
                MyApplication.get(context).getSongsDatabaseReference()
                        .child(String.valueOf(song.getId()))
                        .child("favorite")
                        .child(String.valueOf(userInfor.getId()))
                        .removeValue();
            }
        }
    }

    @SuppressLint("InflateParams")
    public static void handleClickMoreOptions(Activity context, Song song) {
        if (context == null || song == null) return; // Không làm gì nếu context hoặc bài hát null

        // Khởi tạo View Binding cho BottomSheet
        LayoutBottomSheetOptionBinding binding = LayoutBottomSheetOptionBinding
                .inflate(LayoutInflater.from(context));

        // Tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        // Gắn thông tin bài hát vào BottomSheet
        GlideUtils.loadUrl(song.getImage(), binding.imgSong); // Load ảnh bài hát
        binding.tvSongName.setText(song.getTitle()); // Tên bài hát
        binding.tvArtist.setText(song.getArtist()); // Tên nghệ sĩ

        // Kiểm tra bài hát có trong danh sách phát hiện tại không
        if (MusicService.isSongExist(song.getId())) {
            binding.layoutRemovePlaylist.setVisibility(View.VISIBLE); // Hiển thị nút xóa khỏi danh sách phát
            binding.layoutPriority.setVisibility(View.VISIBLE); // Hiển thị nút ưu tiên
            binding.layoutAddPlaylist.setVisibility(View.GONE); // Ẩn nút thêm vào danh sách phát
        } else {
            binding.layoutRemovePlaylist.setVisibility(View.GONE); // Ẩn nút xóa
            binding.layoutPriority.setVisibility(View.GONE); // Ẩn nút ưu tiên
            binding.layoutAddPlaylist.setVisibility(View.VISIBLE); // Hiển thị nút thêm vào danh sách phát
        }

        // Xử lý sự kiện khi nhấn nút tải bài hát
        binding.layoutDownload.setOnClickListener(view -> {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.downloadSong(song); // Gọi hàm tải bài hát
            bottomSheetDialog.hide(); // Đóng BottomSheet
        });

        // Xử lý sự kiện khi ưu tiên bài hát
        binding.layoutPriority.setOnClickListener(view -> {
            if (MusicService.isSongPlaying(song.getId())) {
                // Hiển thị thông báo nếu bài hát đang phát
                showToastMessage(context, context.getString(R.string.msg_song_playing));
            } else {
                // Đặt ưu tiên bài hát
                for (Song songEntity : MusicService.mListSongPlaying) {
                    songEntity.setPriority(songEntity.getId() == song.getId());
                }
                showToastMessage(context, context.getString(R.string.msg_setting_priority_successfully));
            }
            bottomSheetDialog.hide(); // Đóng BottomSheet
        });

        // Xử lý sự kiện thêm bài hát vào danh sách phát
        binding.layoutAddPlaylist.setOnClickListener(view -> {
            if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying.isEmpty()) {
                MusicService.clearListSongPlaying(); // Xóa danh sách phát hiện tại
                MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách phát
                MusicService.isPlaying = false; // Tạm dừng phát nhạc
                GlobalFunction.startMusicService(context, Constant.PLAY, 0); // Bắt đầu phát nhạc
                GlobalFunction.startActivity(context, PlayMusicActivity.class); // Chuyển đến màn hình phát nhạc
            } else {
                MusicService.mListSongPlaying.add(song); // Thêm bài hát vào danh sách phát hiện tại
                showToastMessage(context, context.getString(R.string.msg_add_song_playlist_success));
            }
            bottomSheetDialog.hide(); // Đóng BottomSheet
        });

        // Xử lý sự kiện xóa bài hát khỏi danh sách phát
        binding.layoutRemovePlaylist.setOnClickListener(view -> {
            if (MusicService.isSongPlaying(song.getId())) {
                // Hiển thị thông báo nếu bài hát đang phát và không thể xóa
                showToastMessage(context, context.getString(R.string.msg_cannot_delete_song));
            } else {
                MusicService.deleteSongFromPlaylist(song.getId()); // Xóa bài hát khỏi danh sách phát
                showToastMessage(context, context.getString(R.string.msg_delete_song_from_playlist_success));
            }
            bottomSheetDialog.hide(); // Đóng BottomSheet
        });

        bottomSheetDialog.show(); // Hiển thị BottomSheet
    }

    public static void startDownloadFile(Activity activity, Song song) {
        if (activity == null || song == null || StringUtil.isEmpty(song.getUrl())) return; // Không làm gì nếu thiếu dữ liệu
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(song.getUrl())); // Tạo yêu cầu tải xuống
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI); // Chỉ tải qua WiFi hoặc mạng di động
        request.setTitle(activity.getString(R.string.title_download)); // Tiêu đề thông báo tải xuống
        request.setDescription(activity.getString(R.string.message_download)); // Nội dung thông báo tải xuống

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // Hiển thị thông báo khi hoàn tất
        String fileName = song.getTitle() + ".mp3"; // Tên file bài hát
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); // Đường dẫn lưu file

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request); // Bắt đầu tải xuống
        }
    }

}