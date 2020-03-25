package com.example.dc_para.Shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dc_para.Adopt.AdoptActivity;
import com.example.dc_para.Live.LiveActivity;
import com.example.dc_para.MainActivity;
import com.example.dc_para.R;
import com.example.dc_para.SecondActivity;
import com.example.dc_para.ShopFragment.CatFeedFragment;
import com.example.dc_para.Util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import static com.example.dc_para.Util.Utils.getUserName;

public class ShopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_catfeed,
            R.drawable.ic_catsnack,
            R.drawable.ic_dogfeed,

    };

    private Fragment[] mFragmentList = {
            new CatFeedFragment(), new CatFeedFragment(), new CatFeedFragment()
    };

    private String[] mFragmentTitleList = {
            "貓飼料","貓零食","狗飼料"
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ShopActivity.this, SecondActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_adopt:
                    intent = new Intent(ShopActivity.this, AdoptActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_live:
                    intent = new Intent(ShopActivity.this, LiveActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar_shop = findViewById(R.id.toolbar_shop);
        setSupportActionBar(toolbar_shop);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        DrawerLayout drawer = findViewById(R.id.shop_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar_shop,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // 讓Drawer開關出現三條線
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_shop);
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
        navigation.setSelectedItemId(R.id.navigation_shop);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setTabPager();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTabPager() {
        viewPager = findViewById(R.id.shopViewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentList[position];
            }

            @Override
            public int getCount() {
                return mFragmentList.length;
            }

            public CharSequence getPageTitle(int position){ return mFragmentTitleList[position]; }
        });

        tabLayout = findViewById(R.id.shopTabLayout);
        tabLayout.setupWithViewPager(viewPager);
        for(int position = 0 ; position < tabIcons.length; position++){
           //自訂icon位置
           TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.shop_tab_form, null);
           tabOne.setText(mFragmentTitleList[position]);
           tabOne.setCompoundDrawablesRelativeWithIntrinsicBounds(tabIcons[position],0, 0, 0);
           tabLayout.getTabAt(position).setCustomView(tabOne);
        }
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
                intent = new Intent(ShopActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.shop_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
