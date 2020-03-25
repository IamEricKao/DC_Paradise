package com.example.dc_para;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.dc_para.Adopt.AdoptActivity;
import com.example.dc_para.AdoptForm.AdoptVerifyActivity;
import com.example.dc_para.Chat.FriendsActivity;
import com.example.dc_para.Live.LiveActivity;
import com.example.dc_para.Shop.ShopActivity;
import com.example.dc_para.Util.Util;
import com.example.dc_para.viewpager.Images;
import com.example.dc_para.viewpager.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.dc_para.Util.Utils.getUserName;

public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager vp;
    private ViewPagerAdapter viewPagerAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentIndex;
    private List<News> newsList;
    private ListView lvNews;
    private static final int REQUEST_LOGIN = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_adopt:
                    intent = new Intent(SecondActivity.this, AdoptActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_shop:
                    intent = new Intent(SecondActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_live:
                    intent = new Intent(SecondActivity.this, LiveActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        // 讓Drawer開關出現三條線
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        vp = findViewById(R.id.vp);
        viewPagerAdapter = new ViewPagerAdapter(this, Images.imageArray);
        vp.setAdapter(viewPagerAdapter);
        vp.addOnPageChangeListener(new ViewPagerChangeListener());
        currentIndex = Images.imageArray.length * 1000;
        vp.setCurrentItem(Images.imageArray.length * 1000, true);

        initNews();
        findListViews();

    }
    class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    vp.setCurrentItem(currentIndex);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_chat);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(SecondActivity.this, FriendsActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                intent = new Intent(SecondActivity.this, AdoptVerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                pref.edit().putBoolean("login", false).apply();
                intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                currentIndex++;
                handler.sendEmptyMessage(1);

            }
        }, 3, 3, TimeUnit.SECONDS);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivityForResult(loginIntent, REQUEST_LOGIN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    private void initNews() {
        newsList = new ArrayList<>();
        newsList.add(new News(R.drawable.dogger1, "恭賀本網站開張，單筆滿一千免運費", "繼續閱讀"));
        newsList.add(new News(R.drawable.dogger2, "以認養代替購買，以節育代替撲殺", "繼續閱讀"));
        newsList.add(new News(R.drawable.dogger3, "預告下場直播開始時間: 2020/3/20 中午12點", "繼續閱讀"));
        newsList.add(new News(R.drawable.dogger4, "好康報報: 貓跳台大特價!!!! 數量有限趕緊搶購", "繼續閱讀"));
        newsList.add(new News(R.drawable.dogger5, "自然小貓肉泥12g*4 現時下殺66折!", "繼續閱讀"));

    }

    private void findListViews() {
        lvNews = findViewById(R.id.lvNews);
        lvNews.setAdapter(new NewsAdapter(this, newsList));
    }

private class NewsAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).getPics();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.listview_item, parent, false);
            holder.ivpics = convertView.findViewById(R.id.ivpics);
            holder.tvtitle = convertView.findViewById(R.id.tvtitle);
            holder.tvcontent = convertView.findViewById(R.id.tvcontent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        News news = newsList.get(position);
        holder.ivpics.setImageResource(news.getPics());
        holder.tvtitle.setText(news.getTitle());
        holder.tvcontent.setText(news.getContent());

        return convertView;
    }

    private class ViewHolder {
        ImageView ivpics;
        TextView tvtitle, tvcontent;

    }
}
}
