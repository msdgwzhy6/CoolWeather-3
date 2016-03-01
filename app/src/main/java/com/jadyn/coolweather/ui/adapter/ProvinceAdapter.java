package com.jadyn.coolweather.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.model.Province;

import java.util.List;

import adapter.CommonAdapter;
import adapter.ViewHolder;

/**
 * Created by JadynAi on 2016/2/27.
 */
public class ProvinceAdapter extends CommonAdapter<Province>{
    public ProvinceAdapter(Context context, List<Province> data, int viewResId) {
        super(context, data, viewResId);
    }

    @Override
    public void setRes(ViewHolder viewHolder, Province province) {
        TextView province1 = viewHolder.getView(R.id.item_prov);
        province1.setText(province.getProvinceName());
    }
}
