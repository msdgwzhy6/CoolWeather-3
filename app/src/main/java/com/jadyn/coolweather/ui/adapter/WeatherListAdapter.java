package com.jadyn.coolweather.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.model.Weather;

import java.util.List;

import adapter.CommonAdapter;
import adapter.ViewHolder;

/**
 * Created by JadynAi on 2016/2/29.
 */
public class WeatherListAdapter extends CommonAdapter<Weather>{
    public WeatherListAdapter(Context context, List<Weather> data, int viewResId) {
        super(context, data, viewResId);
    }

    @Override
    public void setRes(ViewHolder viewHolder, Weather weather) {
        ImageView image = viewHolder.getView(R.id.item_list_weather_image);
        TextView text = viewHolder.getView(R.id.item_list_weather_text);
        
        image.setImageResource(weather.weaImg);
        text.setText(weather.weaText);
    }
}
