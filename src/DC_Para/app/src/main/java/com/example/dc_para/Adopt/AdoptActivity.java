package com.example.dc_para.Adopt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dc_para.AdoptForm.AdoptVerifyActivity;
import com.example.dc_para.Live.LiveActivity;
import com.example.dc_para.R;
import com.example.dc_para.SecondActivity;
import com.example.dc_para.Shop.ShopActivity;
import com.example.dc_para.Util.Util;
import com.example.dc_para.task.CommonTask;
import com.example.dc_para.task.ImageTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.example.dc_para.Util.Utils.getUserName;

public class AdoptActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "AdoptActivity";
    private RecyclerView rvAdopts;
    private List<Adopt> adoptlist;
    private CommonTask getAdoptTask;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(AdoptActivity.this, SecondActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_shop:
                    intent = new Intent(AdoptActivity.this, ShopActivity.class);
                    startActivity(intent);

                    break;
                case R.id.navigation_live:
                    intent = new Intent(AdoptActivity.this, LiveActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };
    private ImageTask adoptImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt);

        rvAdopts = findViewById(R.id.rvAdopts);

        Toolbar toolbar_adopt = findViewById(R.id.toolbar_adopt);
        setSupportActionBar(toolbar_adopt);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        DrawerLayout drawer = findViewById(R.id.adopt_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar_adopt,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.adopt_view_adopt);
        navigationView.setNavigationItemSelectedListener(this);

        String Account = null;
        try {
            Account = getUserName(this);
            Log.d("SecondActivity", "Account = " + Account);
        } catch (NullPointerException e) {
            Log.e("SecondActivity", "username is null");
            e.getMessage();
        }

        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mail = pref.getString("member_account", "");

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navEmail = (TextView) headerView.findViewById(R.id.mail);
        navUsername.setText(Account);
        navEmail.setText(mail);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_adopt);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvAdopts.setLayoutManager(layoutManager);
        rvAdopts.setAdapter(new AdoptListAdapter(this,adoptlist));


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.networkConnected(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            updateUI(jsonOut);
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

    private void updateUI(String jsonOut) {
        getAdoptTask = new CommonTask(Util.URL + "AdoptServlet_An", jsonOut);
        try {
            String jsonIn = getAdoptTask.execute().get();
            Type listType = new TypeToken<List<Adopt>>() {
            }.getType();
            adoptlist = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (adoptlist == null || adoptlist.isEmpty()) {
            Util.showToast(this, R.string.msg_AdoptNotFound);
        } else {
            rvAdopts.setAdapter(new AdoptListAdapter(this, adoptlist));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_profile:
                // do something here!
                break;
            case R.id.nav_point:
                // 切換Fragment或是Activity
                break;
            case R.id.nav_order:
                break;
            case R.id.nav_adopt:
                intent = new Intent(AdoptActivity.this, AdoptVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.adopt_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private class AdoptListAdapter extends RecyclerView.Adapter<AdoptListAdapter.ViewHolder>{
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Adopt> adoptlist;
        private int imageSize;

        private AdoptListAdapter (Context context,List<Adopt> adoptlist) {
            this.context = context;
            this.adoptlist = adoptlist;
            layoutInflater = LayoutInflater.from(context);
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;

        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView ivAdoptPics;
            private TextView tvAdoptTitle, tvAdoptContent;

            private ViewHolder(View view){
                super(view);
                ivAdoptPics = view.findViewById(R.id.ivAdoptPics);
                tvAdoptTitle = view.findViewById(R.id.tvAdoptTitle);
                tvAdoptContent= view.findViewById(R.id.tvAdoptContent);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.layout_cardview_adopt,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Adopt adopt = adoptlist.get(position);
            String url = Util.URL + "AdoptServlet_An";
            String Adopt_Project_No = adopt.getAdopt_Project_No();
            adoptImageTask = new ImageTask(url, Adopt_Project_No, imageSize, holder.ivAdoptPics);
            adoptImageTask.execute();

            holder.tvAdoptTitle.setText(adopt.getAdopt_Project_Name());
            holder.tvAdoptContent.setText(adopt.getBreed());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdoptActivity.this,
                            AdoptDetailActivity.class);
                    intent.putExtra("adopt", adopt);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return adoptlist.size();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (getAdoptTask != null) {
            getAdoptTask.cancel(true);
        }

        if (adoptImageTask != null) {
            adoptImageTask.cancel(true);
        }
    }
}
