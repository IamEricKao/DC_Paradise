package com.example.dc_para.Live;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dc_para.Adopt.AdoptActivity;
import com.example.dc_para.MainActivity;
import com.example.dc_para.R;
import com.example.dc_para.SecondActivity;
import com.example.dc_para.Shop.ShopActivity;
import com.example.dc_para.Util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.example.dc_para.Util.Utils.getUserName;

public class LiveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(LiveActivity.this, SecondActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_adopt:
                    intent = new Intent(LiveActivity.this, AdoptActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_shop:
                    intent = new Intent(LiveActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };
    private String liveRoomUrl;
    private WebView webView;


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        Toolbar toolbar_live = findViewById(R.id.toolbar_live);
        setSupportActionBar(toolbar_live);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        DrawerLayout drawer = findViewById(R.id.live_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar_live,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // 讓Drawer開關出現三條線
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_live);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mail = pref.getString("member_account", "");

        String Account = null;
        try {
            Account = getUserName(this);
            Log.d("SecondActivity", "Account = " + Account);
        } catch (NullPointerException e) {
            Log.e("SecondActivity", "username is null");
            e.getMessage();
        }


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navEmail = (TextView) headerView.findViewById(R.id.mail);
        navUsername.setText(Account);
        navEmail.setText(mail);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_live);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        webView = findViewById(R.id.web_view);
        liveRoomUrl = "https://www.youtube.com/watch?v=Jf1m6lsHTts";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(liveRoomUrl);
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
                break;

            case R.id.nav_logout:

                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                pref.edit().putBoolean("login", false).apply();
                intent = new Intent(LiveActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.live_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}
