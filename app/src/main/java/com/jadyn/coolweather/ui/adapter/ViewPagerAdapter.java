package com.jadyn.coolweather.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by JadynAi on 2016/2/28.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> listData;

    public ViewPagerAdapter(List<View> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //摧毁这一项
        container.removeView(listData.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //初始化这一项
        container.addView(listData.get(position));
        return listData.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
