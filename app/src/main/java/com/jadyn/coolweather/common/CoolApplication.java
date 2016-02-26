package com.jadyn.coolweather.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by JadynAi on 2016/2/25.
 * 全局获得context对象的，自定义Application类
 */
public class CoolApplication extends Application{
    private static Context context=null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
