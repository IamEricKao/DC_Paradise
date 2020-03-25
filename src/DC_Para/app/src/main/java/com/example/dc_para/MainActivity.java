package com.example.dc_para;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dc_para.Util.Util;
import com.google.gson.JsonObject;

import com.example.dc_para.task.CommonTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tvMessage;
    private CommonTask isMemberTask;
    private EditText etUser;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessage = findViewById(R.id.tvMessage);
        setResult(RESULT_CANCELED);
    }
    @Override
    protected void onStart() { //檢查是否登錄過
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            String member_account = preferences.getString("member_account", "");
            String member_password = preferences.getString("member_password", "");
            if (isUserValid(member_account, member_password)) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    public void onLoginClick(View view) {
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        String member_account = etUser.getText().toString().trim();
        String member_password = etPassword.getText().toString().trim();
        if (member_account.length() <= 0 || member_password.length() <= 0) { //沒輸入
            showMessage(R.string.msg_InvalidUserOrPassword);
            return;
        }

        if (isUserValid(member_account, member_password)) {
            SharedPreferences preferences = getSharedPreferences(
                    Util.PREF_FILE, MODE_PRIVATE);
            preferences.edit().putBoolean("login", true)
                    .putString("member_account", member_account)
                    .putString("member_password", member_password).apply();
            setResult(RESULT_OK);
            finish();
        } else {
            showMessage(R.string.msg_InvalidUserOrPassword);
        }
    }

    private boolean isUserValid(final String member_account, final String member_password) {
        boolean isUserValid = false;
        if (Util.networkConnected(this)) {
            String url = Util.URL + "MemberServlet";
            Log.d("URL", url);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isUserValid");
            jsonObject.addProperty("member_account", member_account);
            jsonObject.addProperty("member_password", member_password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();
                isUserValid = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isUserValid = false;
            }
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
        return isUserValid;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }

    public void onAutoLoginClick(View view) {
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        etUser.setText("ping123@gmail.com");
        etPassword.setText("aaa123456");
    }

    public void onAutoLoginClick2(View view) {
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        etUser.setText("da105g2@gmail.com");
        etPassword.setText("abc123456");
    }
}
