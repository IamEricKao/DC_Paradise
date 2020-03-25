package com.example.dc_para.AdoptForm;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dc_para.R;
import com.example.dc_para.Util.Util;
import com.example.dc_para.task.CommonTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AdoptVerifyActivity extends AppCompatActivity {
    private static final String TAG = "AdoptVerifyActivity";
    private RecyclerView rvAdopt_verify;
    private CommonTask getVerifytListTask;
    private List<Adopt_List> adoptVerifyList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_adopt_verify);

        Toolbar toolbar = findViewById(R.id.toolbar_adopt_verify);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewS();
        rvAdopt_verify.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void findViewS() {
        rvAdopt_verify = findViewById(R.id.rvAdopt_verify);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.networkConnected(this)){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findBy_Adopt_Project_No");
            jsonObject.addProperty("Adopt_Project_No","AP0007");
            String jsonOut = jsonObject.toString();
            updateUI(jsonOut);
        }else{
            Util.showToast(this,R.string.msg_NoNetwork);
        }
    }

    private void updateUI(String jsonOut) {
        getVerifytListTask = new CommonTask(Util.URL + "Android_AdoptListServlet", jsonOut);
        try {
            String jsonIn = getVerifytListTask.execute().get();
            Type listType = new TypeToken<List<Adopt_List>>(){}.getType();
            adoptVerifyList = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonIn, listType);
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
        if (adoptVerifyList == null || adoptVerifyList.isEmpty()){
            Util.showToast(this, "尚無申請");
        }else{
            rvAdopt_verify.setAdapter(new AdoptVerifyListAdapter(this, adoptVerifyList));
        }
    }

    private class AdoptVerifyListAdapter extends RecyclerView.Adapter<AdoptVerifyListAdapter.MyViewHolder>{
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Adopt_List> adoptVerifyList;

        AdoptVerifyListAdapter(Context context,  List<Adopt_List> adoptVerifyList){
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.adoptVerifyList = adoptVerifyList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tvApplyDate, tvAdoptProjectNo, tvRealName, tvAge, tvStatus;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvApplyDate = itemView.findViewById(R.id.tvApplyDate);
                tvAdoptProjectNo = itemView.findViewById(R.id.tvAdoptProjectNo);
                tvRealName = itemView.findViewById(R.id.tvRealName);
                tvAge = itemView.findViewById(R.id.tvAge);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.layout_cardview_adopt_verify, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Adopt_List verifyList = adoptVerifyList.get(position);
            String status = "申請狀態: " +  filter_status(verifyList.getStatus());
            String applyDate = (verifyList.getDate_Of_Application()).toString();

            holder.tvApplyDate.setText(applyDate);
            holder.tvAdoptProjectNo.setText(verifyList.getAdopt_Project_No());
            holder.tvRealName.setText(verifyList.getReal_Name());
            holder.tvAge.setText(String.valueOf(verifyList.getAge()).concat("歲"));
            holder.tvStatus.setText(status);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdoptVerifyActivity.this,
                            AdoptVerifyDetailActivity.class);
                    intent.putExtra("verifyList", verifyList);
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return adoptVerifyList.size();
        }
    }

    public String filter_status(int status){
        String filter_status = null;
        switch (status){
            case 0:
                filter_status = "待處理";
                break;
            case 1:
                filter_status = "失敗";
                break;
            case 2:
                filter_status = "成功";
                break;
        default:
            break;
        }
        return filter_status;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getVerifytListTask != null){
            getVerifytListTask.cancel(true);
        }
    }
}
