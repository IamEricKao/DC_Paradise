package com.example.dc_para.ShopFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dc_para.Live.LiveActivity;
import com.example.dc_para.R;
import com.example.dc_para.Shop.ShopItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class CatFeedFragment extends Fragment {
    private View view;
    private List<ShopItem> shopItemList;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cat_feed, container, false);
        return view;
    }
    public void onStart(){
        super.onStart();
        initShopItem();

        RecyclerView recyclerCardView = view.findViewById(R.id.shop_recyclerview);
        recyclerCardView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerCardView.setAdapter(new RecyclerCardViewItemAdapter(getContext(), shopItemList));

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LiveActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initShopItem(){
        shopItemList = new ArrayList<>();
        shopItemList.add(new ShopItem(R.drawable.shopitem01,"貝斯比 樂活幼母貓配方", "$649"));
        shopItemList.add(new ShopItem(R.drawable.shopitem02,"瑪丁 低過敏幼貓雞肉配方", "$874"));
        shopItemList.add(new ShopItem(R.drawable.shopitem03,"希爾思 青春活力7歲成貓配方", "$689"));
        shopItemList.add(new ShopItem(R.drawable.shopitem04,"優格 零穀鮭魚貓糧", "$660"));

    }

    private class RecyclerCardViewItemAdapter extends RecyclerView.Adapter<RecyclerCardViewItemAdapter.RecyclerCardViewHolder>{
        private Context context;
        private List<ShopItem> shopItemList;

        public RecyclerCardViewItemAdapter(Context context, List<ShopItem> shopItemList){
            this.context = context;
            this.shopItemList = shopItemList;
        }


        public class RecyclerCardViewHolder extends RecyclerView.ViewHolder {
            private final ImageView ivShopPic;
            private final TextView tvShopTitle;
            private final TextView tvShopPrice;

            public RecyclerCardViewHolder(@NonNull View itemView) {
                super(itemView);
                ivShopPic   = itemView.findViewById(R.id.ivShopPic);
                tvShopTitle = itemView.findViewById(R.id.tvShopTitle);
                tvShopPrice = itemView.findViewById(R.id.tvShopPrice);
            }
        }

        @NonNull
        @Override
        public RecyclerCardViewItemAdapter.RecyclerCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.layout_cardview_shop, parent, false);
            return new RecyclerCardViewItemAdapter.RecyclerCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerCardViewItemAdapter.RecyclerCardViewHolder holder, int position) {
            final ShopItem shopItem = shopItemList.get(position);
            holder.ivShopPic.setImageResource(shopItem.getImage());
            holder.tvShopTitle.setText(shopItem.getTitle());
            holder.tvShopPrice.setText(shopItem.getPrice());
        }

        @Override
        public int getItemCount() {
            return shopItemList.size();
        }
    }
}
