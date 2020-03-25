package com.example.dc_para.Adopt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dc_para.AdoptForm.AdoptFormActivity;
import com.example.dc_para.Chat.FriendsActivity;
import com.example.dc_para.R;
import com.example.dc_para.Util.Util;
import com.example.dc_para.task.ImageTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AdoptDetailActivity extends AppCompatActivity {

    private static final String TAG = "AdoptDetailActivity";
    private ImageTask adoptImageTask;
    private TextView tvfounder_location;
    private String locationName;
    private static Adopt adopt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_detail);

        adopt = (Adopt) this.getIntent().getSerializableExtra("adopt");
        if (adopt == null) {
            Util.showToast(this, R.string.msg_AdoptNotFound);
        } else {
            showDetail(adopt);
        }
    }

    private void showDetail(Adopt adopt) {
        ImageView imageView = findViewById(R.id.adopt_detail_img);
        String PetCategory = PetCategory(adopt.getPet_Category());
        String Sex = Sex(adopt.getSex());
        String Chip = Chip(adopt.getChip());
        String BirthControl = BirthControl(adopt.getBirth_Control());
        String Adopter_NO = Adopter_No(adopt.getAdopter_No());

        String url = Util.URL + "AdoptServlet_An";
        String Adopt_Project_No = adopt.getAdopt_Project_No();
        int imageSize = getResources().getDisplayMetrics().widthPixels;

        Bitmap bitmap = null;
        try {
            adoptImageTask = new ImageTask(url, Adopt_Project_No, imageSize);
            bitmap = adoptImageTask.execute().get();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            imageView.setImageResource(R.drawable.default_image);
        }

        TextView textView = findViewById(R.id.adopt_detail_info);
        String adoptInfo =
                getString(R.string.col_Adopt_Project_No) + ":  " + adopt.getAdopt_Project_No() + "\n\n" +
                getString(R.string.col_Adopt_Project_Name) + ":  " + adopt.getAdopt_Project_Name()+ "\n\n" +
                getString(R.string.col_Breed) + ":  " + adopt.getBreed()+ "\n\n" +
                getString(R.string.col_Founder_No) + ":  " + adopt.getFounder_No()+ "\n\n" +
                getString(R.string.col_Adopter_No) + ":  " + Adopter_NO + "\n\n" +
                getString(R.string.col_Age) + ":  " + adopt.getAge()+ "\n\n" +
                PetCategory + Sex + Chip + BirthControl;
        textView.setText(adoptInfo);

        tvfounder_location = findViewById(R.id.founder_location);

        String Founder_Location = adopt.getFounder_Location();
        tvfounder_location.setText(Founder_Location);
    }

    private String Adopter_No(String Adopter_No){
        String Adopter_No_info = null;
        if (Adopter_No == null){
            Adopter_No_info = "";
        } else{
            Adopter_No_info = adopt.getAdopter_No();
        }

        return Adopter_No_info;
    }

    private String BirthControl(Integer birth_control) {
        String birthcontrol_info = null;
        switch(birth_control) {
            case 0:
                birthcontrol_info = getString(R.string.col_Birth_Control) + ":  有";
                break;
            case 1:
                birthcontrol_info = getString(R.string.col_Birth_Control) + ":  無";
                break;
        }
        return birthcontrol_info;
    }

    private String Chip(Integer chip) {
        String Chip_info = null;
        switch(chip) {
            case 0:
                Chip_info = getString(R.string.col_Chip) + ":  有 \n\n";
                break;
            case 1:
                Chip_info = getString(R.string.col_Chip) + ":  無 \n\n";
                break;
        }
        return Chip_info;
    }

    private String Sex(Integer sex) {
        String Sex_info = null;
        switch(sex) {
            case 0:
                Sex_info = getString(R.string.col_Sex) + ":  公 \n\n";
                break;
            case 1:
                Sex_info = getString(R.string.col_Sex) + ":  母 \n\n";
                break;
        }
        return Sex_info;
    }

    private String PetCategory(Integer pet_category) {
        String pet_category_info = null;
        switch(pet_category) {
            case 0:
                pet_category_info = getString(R.string.col_Pet_Category) + ":  貓 \n\n";
                break;
            case 1:
                pet_category_info = getString(R.string.col_Pet_Category) + ":  狗 \n\n";
                break;

        }
        return pet_category_info;
    }

    // 導航功能
    public void OnFlMakerClick(View view) {

        locationName = tvfounder_location.getText().toString().trim();
        if (locationName.isEmpty())
            return;

        Address address = getAddress(locationName);
        if (address == null) {
            Toast.makeText(this, getString(R.string.msg_LastLocationNotAvailable), Toast.LENGTH_SHORT).show();
            return;
        }
        // 取得使用者輸入位置的緯經度
        double toLat = address.getLatitude();
        double toLng = address.getLongitude();

        direct(toLat, toLng);
    }

    // 將使用者輸入的地名或地址轉成Address物件
    private Address getAddress(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
            // 解譯地名/地址後可能產生多筆位置資訊，但限定回傳1筆
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException ie) {
            Log.e(TAG, ie.toString());
        }

        if (addressList == null || addressList.isEmpty())
            return null;
        else
            // 因為當初限定只回傳1筆，所以只要取得第1個Address物件即可
            return addressList.get(0);
    }

    // 開啟Google地圖應用程式來完成導航要求
    private void direct(double toLat, double toLng) {
        // 設定欲前往的Uri，saddr-出發地緯經度；daddr-目的地緯經度
        String uriStr = String.format(Locale.TAIWAN,
                "http://maps.google.com/maps?daddr=%f,%f",
                toLat, toLng);
        Intent intent = new Intent();
        // 指定交由Google地圖應用程式接手
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        // ACTION_VIEW-呈現資料給使用者觀看
        intent.setAction(Intent.ACTION_VIEW);
        // 將Uri資訊附加到Intent物件上
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (adoptImageTask != null) {
            adoptImageTask.cancel(true);
        }
    }

    public void onChatClick(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onFormClick(View view) {
        String Adopt_Project_No = adopt.getAdopt_Project_No();
        Intent intent = new Intent(AdoptDetailActivity.this, AdoptFormActivity.class);
        intent.putExtra("Adopt_Project_No", Adopt_Project_No);
        startActivity(intent);
    }
}

