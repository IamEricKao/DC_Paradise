package com.example.dc_para.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dc_para.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int [] datas;

    public ViewPagerAdapter(Context context, int[] datas){
        this.context = context;
        this.datas = datas;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = layoutInflater.inflate(R.layout.viewpager_item,null);
        ImageView iv = layout.findViewById(R.id.item_iv);
        iv.setImageResource(datas[position % datas.length]);
        container.addView(layout);
        return layout;
    }
}
