package com.example.dc_para.AdoptForm;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.dc_para.R;
import com.example.dc_para.Util.Util;
import com.example.dc_para.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class AdoptVerifyDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AdoptVerifyDetailActivity";
    private Button btnAdoptVerifyPass;
    private Button btnAdoptVerifyFail;
    private CommonTask verifyUpdate;
    private Adopt_List verifyList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_adopt_verify_detail);

        findViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_adopt_verifyDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verifyList = (Adopt_List) this.getIntent().getSerializableExtra("verifyList");
        if(verifyList == null){
            Util.showToast(this,R.string.msg_VerifyListNotFound);
        } else{
            showDetail(verifyList);
        }
    }

    private void findViews() {
        btnAdoptVerifyPass = findViewById(R.id.btnAdoptVerifyPass);
        btnAdoptVerifyFail = findViewById(R.id.btnAdoptVerifyFail);
        btnAdoptVerifyPass.setOnClickListener(this);
        btnAdoptVerifyFail.setOnClickListener(this);
    }

    private void showDetail(Adopt_List verifyList) {
        TextView tvAdopt_verify_info = findViewById(R.id.tvAdopt_verify_info);
        int DB_sex = verifyList.getSex();
        String sex = filterSex(DB_sex);
        String verify_Info =
                getString(R.string.col_Adopt_Project_No) + ": " + verifyList.getAdopt_Project_No() + "\n\n" +
                getString(R.string.hint_Real_Name) + ": " + verifyList.getReal_Name() + "\n\n" +
                getString(R.string.hint_Phone) + ": " + verifyList.getPhone() + "\n\n" +
                getString(R.string.col_Age) + ": " + verifyList.getAge() + "歲" + "\n\n" +
                getString(R.string.hint_ID_Card) + ": " + verifyList.getID_Card() +  "\n\n" +
                getString(R.string.hint_Address) + ": " + verifyList.getAddress() + "\n\n" +
                getString(R.string.hint_Email) + ": " + AdoptFormActivity.getMemberAccount(this) + "\n\n" +
                getString(R.string.col_Sex) + ": " + sex + "\n\n" +
                getString(R.string.col_date) + ": " + verifyList.getDate_Of_Application();
        tvAdopt_verify_info.setText(verify_Info);
    }

    public static String filterSex(int sex){
        String filterSex = null;
        switch (sex){
            case 0:
                filterSex = "男";
                break;
            case 1:
                filterSex = "女";
                break;
        }
        return filterSex;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnAdoptVerifyPass:
                verifyList.setStatus(2);
                break;
            case R.id.btnAdoptVerifyFail:
                verifyList.setStatus(1);
                break;
        }
        UpdateVerify(verifyList);
    }

    @SuppressLint("LongLogTag")
    private void UpdateVerify(Adopt_List verifyList){
        if (Util.networkConnected(this)){
            String url = Util.URL + "Android_AdoptListServlet";
            JsonObject jsonObject = new JsonObject();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            jsonObject.addProperty("action","update");
            jsonObject.addProperty("adopt_List", gson.toJson(verifyList));
            String jsonOut = jsonObject.toString();
            verifyUpdate = new CommonTask(url, jsonOut);
            Boolean count = false;
            try{
                String result = verifyUpdate.execute().get();
                count = Boolean.valueOf(result);
            } catch (Exception e){
                Log.e(TAG, e.toString());
            }
            if (!count){
                Util.showToast(this, R.string.msg_UpdateVerifyFail);
            } else{
                Util.showToast(this, R.string.msg_UpdateVerifyPass);
                finish();
            }

//            int count = 0;
//            try{
//                String result = verifyUpdate.execute().get();
//                count = Integer.valueOf(result);
//            } catch (Exception e){
//                Log.e(TAG, e.toString());
//            }
//            if (count == 0){
//                Util.showToast(this, R.string.msg_UpdateVerifyFail);
//            } else{
//                Util.showToast(this, R.string.msg_UpdateVerifyPass);
//                finish();
//            }

        }

    }
}
