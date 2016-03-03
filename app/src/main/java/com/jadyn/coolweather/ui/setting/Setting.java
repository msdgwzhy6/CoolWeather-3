package com.jadyn.coolweather.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JadynAi on 2016/3/2.
 * 设置类
 */
public class Setting {
    public static final String CITY_NAME="城市名";
    
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


    public Setting putString(String key, String value) {
        spf.edit().putString(key, value).apply();
        return this;
    }
    

    public String getString(String key, String defValue) {
        return spf.getString(key, defValue);
    }
}
