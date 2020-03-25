package com.example.dc_para.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.dc_para.ws.ChatWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    private final static String TAG = "Utils";
    private static final String SERVER_URI =
//            "ws://10.0.2.2:8081/DC_shop_G2MVC_V23/FriendWS/";
            "ws://bc5e36fe.ngrok.io/DC_shop_G2MVC_V38/FriendWS/";
//            "ws://c4fedee8.ngrok.io/DC_adopt_01MVC/FriendWS/";
    public static ChatWebSocketClient chatWebSocketClient;

    //建立WebSocket連線
    public static void connectServer(Context context, String userName){
        URI uri = null;
        try{
            uri = new URI(SERVER_URI + userName);
        }catch (URISyntaxException e){
            Log.e(TAG, e.toString());
        }
        if (chatWebSocketClient == null){
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();
        }
    }

    public static void disconnectServer() {
        if (chatWebSocketClient == null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }

    public static String getUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String [] memberAccount = (pref.getString("member_account", "")).split("@");
        String userName = memberAccount[0];
        return userName;
    }

    public static Bitmap downSize (Bitmap srcBitmap, int newSize){
        if (newSize <= 50){
            //如果欲縮小的尺寸過小,就直接設成128
            newSize = 128;
        }
        int srcWidth  = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        String text = "source image size = " + srcWidth + "x" + srcHeight;
        Log.d(TAG, text);
        int longer = Math.max(srcWidth, srcHeight);

        if (longer > newSize){
            double scale = longer / (double) newSize;
            int dstWidth  = (int) (srcWidth  / scale);
            int dstHeight = (int) (srcHeight / scale);
            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, false);
            System.gc();
            text = "\nscale = " + scale + "\nscaled image size = " +
                    srcBitmap.getWidth() + "x" + srcBitmap.getHeight();
            Log.d(TAG, text);

        }
        return srcBitmap;
    }
}
