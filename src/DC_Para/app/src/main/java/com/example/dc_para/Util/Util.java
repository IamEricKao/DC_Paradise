package com.example.dc_para.Util;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {
    // 模擬器連Tomcat
//    public static String URL = "http://10.0.2.2:8081/DC_shop_G2MVC_V23/";
    public static String URL = "http://bc5e36fe.ngrok.io/DC_shop_G2MVC_V38/";
//    public static String URL = "http://c4fedee8.ngrok.io/DC_adopt_01MVC/";
//    public static String URL = "http://172.20.10.6:8081/BookStoreWebi/";

    // 偏好設定檔案名稱
    public final static String PREF_FILE = "preference";

     // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }


    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
