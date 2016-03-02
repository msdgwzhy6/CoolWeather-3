package com.jadyn.coolweather.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JadynAi on 2016/3/2.
 * 设置类
 */
public class Setting {
    public static final String CURRENT_HOUR = "小时";//当前时辰

    private static Setting sInstance;
    
    private SharedPreferences spf;


    
    public static Setting getsInstance(Context context) {
        if (sInstance==null) {
            sInstance = new Setting(context);
        }
        return sInstance;
    }


    private Setting(Context context) {
        spf = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }


    
    public Setting putInt(String key, int value) {
        spf.edit().putInt(key, value).apply();
        return this;
    }


    public int getInt(String key, int defValue) {
        return spf.getInt(key, defValue);
    }
}
