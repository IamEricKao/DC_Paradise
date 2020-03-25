package com.example.dc_para.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dc_para.ChatModel.State;
import com.example.dc_para.R;
import com.example.dc_para.Util.Utils;
import com.google.gson.Gson;


import java.util.LinkedList;
import java.util.List;

import static com.example.dc_para.Util.Util.showToast;
import static com.example.dc_para.Util.Utils.getUserName;

public class FriendsActivity extends AppCompatActivity {
    private static final String TAG = "FriendsActivity";

    private RecyclerView rvFriends;
    private String user;
    private List<String> friendsList;
    private LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerFriendStateReceiver();

        user = getUserName(this);
        Log.d(TAG, "user = " + user);
        setTitle("在線列表");

        friendsList = new LinkedList<>();

        rvFriends = findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setAdapter(new FriendsAdapter(this));

        Utils.connectServer(this, user);

    }


    private void registerFriendStateReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        FriendStateReceiver friendStateReceiver = new FriendStateReceiver();
        broadcastManager.registerReceiver(friendStateReceiver, openFilter);
        broadcastManager.registerReceiver(friendStateReceiver, closeFilter);

    }

    private class FriendStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            State stateMessage = new Gson().fromJson(message, State.class);
            String type = stateMessage.getType();
            String friend = stateMessage.getUser();
            switch (type) {
                //有user連線
                case "open":
                    //如果是自己連線
                    if (friend.equals(user)) {
                        //取得server上所有的user
                        friendsList = new LinkedList<>(stateMessage.getUsers());
                        //將自己從聊天清單中清除, 否則會看到自己在聊天清單上
                        friendsList.remove(user);
                    } else {
                        //如果其他user連線且尚未加入聊天清單,就新增user
                        if (!friendsList.contains(friend)) {
                            friendsList.add(friend);
                        }
                        showToast(FriendsActivity.this, friend + " is online");
                    }
                    //重刷聊天清單
                    rvFriends.getAdapter().notifyDataSetChanged();
                    break;
                //有user斷線
                case "close":
                    //將斷線的user從聊天清單中移除
                    friendsList.remove(friend);
                    rvFriends.getAdapter().notifyDataSetChanged();
                    showToast(FriendsActivity.this, friend + " is offline");
            }
            Log.d(TAG, message);
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHoler> {
        Context context;

        public FriendsAdapter(Context context) {
            this.context = context;
        }

        public class FriendsViewHoler extends RecyclerView.ViewHolder {
            TextView tvFriendName;

            public FriendsViewHoler(@NonNull View itemView) {
                super(itemView);
                tvFriendName = itemView.findViewById(R.id.tvFriendName);
            }
        }

        @NonNull
        @Override
        public FriendsViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.layout_cardview_friends, parent, false);
            return new FriendsViewHoler(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsViewHoler holder, int position) {
            final String friend = friendsList.get(position);
            holder.tvFriendName.setText(friend);
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //開啟聊天室
                    Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                    intent.putExtra("friend", friend);
                    intent.putExtra("user", user);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.disconnectServer();
    }
}
