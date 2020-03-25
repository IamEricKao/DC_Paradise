package com.example.dc_para.AdoptForm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dc_para.ChatModel.Member;
import com.example.dc_para.R;
import com.example.dc_para.Util.Util;
import com.example.dc_para.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Date;

public class AdoptFormActivity extends AppCompatActivity {
    private static final String TAG = "AdoptFormActivity";
    TextView adopt_baseInfo;
    private CommonTask memberNameTask;
    private String adopt_project_no;
    private String member_account;
    private String Adopter_No;
    private java.sql.Date Day;
    private EditText etReal_name;
    private EditText etPhone;
    private EditText etAge;
    private EditText etID_card;
    private EditText etAddress;
    private Date date;
    private CommonTask adopt_listTask;
    private EditText etEmail;
    private RadioGroup radioGroup;
    private TextView tvFormEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_form);
        findView();
        adopt_project_no = getIntent().getStringExtra("Adopt_Project_No");
        date = showRightNow();
        member_account = getMemberAccount(this);
        Adopter_No = getmemberNo(member_account);

        String baseinfo =
                getString(R.string.col_date) + ": " + date + "\n\n" +
                getString(R.string.col_Adopt_Project_No) + ": " + adopt_project_no + "\n\n" +
                getString(R.string.col_memberNo) + ": " + Adopter_No;
        adopt_baseInfo.setText(baseinfo);

    }

    private void findView() {
        adopt_baseInfo = findViewById(R.id.adopt_baseInfo);
        etReal_name = findViewById(R.id.etReal_Name);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        etID_card = findViewById(R.id.etID_Card);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        radioGroup = findViewById(R.id.radioGroup);
        tvFormEmpty = findViewById(R.id.tvFormEmpty);
    }

    private Date showRightNow() {
        Date date = new Date();
        long Long_Date = date.getTime();
        Day = new java.sql.Date(Long_Date);
        return Day;
    }

    private String getmemberNo(String member_account){
        String memberNo= null;
        if(Util.networkConnected(this)){
            String url = Util.URL + "MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByMemberNo");
            jsonObject.addProperty("member_account", member_account);
            String jsonOut = jsonObject.toString();
            memberNameTask = new CommonTask(url, jsonOut);
            Member member = null;

            try{
                String result = memberNameTask.execute().get();
                member = new Gson().fromJson(result, Member.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            assert member != null;
            memberNo = member.getMember_no();
            Log.d(TAG, "memberNo = " + memberNo);
        }
        return memberNo;
    }

    public void onAdoptFormClick(View view) {
        String Real_Name = etReal_name.getText().toString().trim();
        String Phone = etPhone.getText().toString().trim();
        String ID_Card = etID_card.getText().toString().trim();
        String Address = etAddress.getText().toString().trim();
        String Email = etEmail.getText().toString().trim();
        int Status = 0;
        int Age;

        try {
            Age = Integer.parseInt(etAge.getText().toString().trim());
        }catch (NumberFormatException e){
            Age = 0;
            e.toString();
        }

        String message = "";
        boolean isInputValid = true;
        if (Real_Name.isEmpty()){
            message += getString(R.string.hint_Real_Name) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (Phone.isEmpty()){
            message += getString(R.string.hint_Phone) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (Age == 0){
            message += getString(R.string.col_Age) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (ID_Card.isEmpty()){
            message += getString(R.string.hint_ID_Card) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (Address.isEmpty()){
            message += getString(R.string.hint_Address) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (Email.isEmpty()){
            message += getString(R.string.hint_Email) + " "
                    +getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        tvFormEmpty.setText(message);


        Adopt_List adopt_list = new Adopt_List ();
        adopt_list.setAdopt_Project_No(adopt_project_no);
        adopt_list.setAdopter_No(Adopter_No);
        adopt_list.setReal_Name(Real_Name);
        adopt_list.setPhone(Phone);
        adopt_list.setAge(Age);
        adopt_list.setID_Card(ID_Card);
        adopt_list.setAddress(Address);
        adopt_list.setEmail(Email);
        adopt_list.setSex(getSex());
        adopt_list.setDate_Of_Application((java.sql.Date)date);
        adopt_list.setStatus(Status);

        if (isInputValid) {
            if (Util.networkConnected(this)) {
                String url = Util.URL + "Android_AdoptListServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "add");
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                jsonObject.addProperty("adopt_list", gson.toJson(adopt_list));
                String jsonOut = jsonObject.toString();
                adopt_listTask = new CommonTask(url, jsonOut);
                boolean isSuccess = false;
                try {
                    String result = adopt_listTask.execute().get();
                    isSuccess = Boolean.valueOf(result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (isSuccess) {
                    Util.showToast(this, R.string.msg_AdoptListSuccess);
                    finish();
                } else {
                    Util.showToast(this, R.string.msg_AdoptListFail);
                }
            }
        }
    }

    public static String getMemberAccount(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        return pref.getString("member_account", "");
    }

    public int getSex(){
        int sex = 0;
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.rbWoman:
                sex = 1;
                break;
            case R.id.rbMen:
                sex = 0;
                break;
        }
        return sex;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (memberNameTask != null) {
            memberNameTask.cancel(true);
        }
        if (adopt_listTask != null) {
            adopt_listTask.cancel(true);
        }
    }

    public void onAutoFillForm(View view) {
        etReal_name.setText("麵包超人");
        etPhone.setText("033345678");
        etAge.setText("20");
        etID_card.setText("J223456789");
        etAddress.setText("110台北市信義區松高路12號4樓");
        etEmail.setText("Anpanman@JP.com");
        radioGroup.check(R.id.rbMen);
    }
}
