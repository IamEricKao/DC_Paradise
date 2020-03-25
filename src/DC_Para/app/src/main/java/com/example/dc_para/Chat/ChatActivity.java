package com.example.dc_para.Chat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.dc_para.ChatModel.ChatMessage;
import com.example.dc_para.R;
import com.example.dc_para.Util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static com.example.dc_para.Util.Util.showToast;
import static com.example.dc_para.Util.Utils.chatWebSocketClient;
import static com.example.dc_para.Util.Utils.getUserName;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_CROP_PICTURE = 1;
    private static final int REQ_PERMISSIONS_STORAGE = 101;
    private LocalBroadcastManager broadcastManager;
    private EditText chat_etMessage;
    private ScrollView chat_ScrollView;
    private String friend;
    private LinearLayout layout;
    private Uri contentUri, croppedImageUri;
    private ImageView ivCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViews();

        //初始化 LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        //取得前頁傳來的聊天對象
        friend = getIntent().getStringExtra("friend");
        setTitle("@" + friend);
        Utils.connectServer(this, getUserName(this));
//        getHistoryMessage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission_Storage();
    }

    private void findViews() {
        chat_etMessage = findViewById(R.id.chat_etMessage);
        chat_ScrollView = findViewById(R.id.chat_ScrollView);
        layout = findViewById(R.id.layout);
        ivCamera = findViewById(R.id.ivCamera);
    }

    private void getHistoryMessage() {
        //因為是歷史紀錄,所以第四個參數空白
        ChatMessage chatMessage = new ChatMessage("history", getUserName(this), friend, "", "");
        String chatMessageJson = new Gson().toJson(chatMessage);
        chatWebSocketClient.send(chatMessageJson);
    }

    private void registerChatReceiver() {
        IntentFilter openFilter = new IntentFilter("chat");
        IntentFilter closeFilter = new IntentFilter("history");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, openFilter);
        broadcastManager.registerReceiver(chatReceiver, closeFilter);
    }

    private class ChatReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            String sender = chatMessage.getSender();
            String messageType = chatMessage.getMessageType();

            if ("history".equals(chatMessage.getType())) {
                Type type = new TypeToken<List<String>>() {}.getType();
                List<String> historyMsg = new Gson().fromJson(chatMessage.getMessage(), type);

                for (String str : historyMsg) {
                    ChatMessage cm = new Gson().fromJson(str, ChatMessage.class);
                    String historyMsg_sender = cm.getSender();

                    switch (cm.getMessageType()) {
                        case "text":
                            if (historyMsg_sender.equals(friend)) {
                                showMessage(cm.getSender(), cm.getMessage(), true);
                            }else{
                                showMessage(cm.getSender(), cm.getMessage(), false);
                            }
                            break;

                        case "image":
                            byte[] image = Base64.decode(cm.getMessage(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                            if (historyMsg_sender.equals(friend)) {
                                showImage(historyMsg_sender, bitmap, true);
                            }else{
                                showImage(historyMsg_sender, bitmap, false);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            if (sender.equals(friend)) {
                switch (messageType){
                    case "text":
                    showMessage(sender, chatMessage.getMessage(), true);
                    break;
                    case "image":
                        byte[] image = Base64.decode(chatMessage.getMessage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0, image.length);
                        showImage(sender, bitmap, true);
                        break;
                        default:
                            break;
                }
            }
            Log.d(TAG, "received message" + message);
        }
    }

    public void onClickSend(View view) {
        String message = chat_etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            showToast(this, R.string.text_MsgEmpty);
            return;
        }
        String sender = getUserName(this);
        showMessage(sender, message, false);
        chat_etMessage.setText(null);

        ChatMessage chatMessage = new ChatMessage("chat", sender, friend, message, "text");
        String chatMessageJson = new Gson().toJson(chatMessage);
        chatWebSocketClient.send(chatMessageJson);
        Log.d(TAG, "output" + chatMessageJson);
    }


    public void onCameraClick(View view) {
        takePicture();
    }

    private void takePicture() {
        Intent intent;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        contentUri = FileProvider.getUriForFile(
                this, getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } else {
            showToast(this, R.string.text_NoCameraApp);
        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                packageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            int newSize = 400;
            //手機拍照app拍照完成後可以取得照片圖檔
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    Log.d(TAG, "REQ_TAKE_PICTURE: " + contentUri.toString());
                    crop(contentUri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE" + croppedImageUri.toString());
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(croppedImageUri));
                        Bitmap downsizedImage = Utils.downSize(bitmap, newSize);
                        String sender= getUserName(this);
                        showImage(sender, downsizedImage, false);
                        String message = Base64.encodeToString(bitmapToPNG(downsizedImage), Base64.DEFAULT);
                        ChatMessage chatMessage = new ChatMessage("chat", sender, friend, message, "image");
                        String chatMessageJson = new Gson().toJson(chatMessage);
                        chatWebSocketClient.send(chatMessageJson);
                        Log.d(TAG, "output: " + chatMessageJson);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //取得圖片路徑的存取權限
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }catch (ActivityNotFoundException anfe){
            showToast(this,"手機不支援圖片裁切");
        }
    }

    private byte[] bitmapToPNG(Bitmap srcBitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //轉成PNG不會失真,所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void showMessage(String sender, String message, boolean left) {
        String text = message;
        View view;
        //準備左右兩種layout給不同發訊者(傳送者/接收者)使用
        if (left) {
            view = View.inflate(this, R.layout.message_left, null);
            TextView name = view.findViewById(R.id.name);
            name.setText(sender);
        } else {
            view = View.inflate(this, R.layout.message_right, null);
        }

        TextView textView = view.findViewById(R.id.message_body);
        textView.setText(text);
        layout.addView(view);
        chat_ScrollView.post(new Runnable() {
            @Override
            public void run() {
                chat_ScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void showImage(String sender, Bitmap bitmap, boolean left){
        View view;
        if (left) {
            view = View.inflate(this, R.layout.image_left, null);
            TextView name = view.findViewById(R.id.name);
            name.setText(sender);
        } else {
            view = View.inflate(this, R.layout.image_right, null);
        }

        ImageView imageView = view.findViewById(R.id.image_body);
        imageView.setImageBitmap(bitmap);
        layout.addView(view);
        chat_ScrollView.post(new Runnable() {
            @Override
            public void run() {
                chat_ScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    private void requestPermission_Storage(){
        String[] permissions ={
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this, permissions, REQ_PERMISSIONS_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case REQ_PERMISSIONS_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ivCamera.setEnabled(true);
                }else{
                    ivCamera.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }
}
