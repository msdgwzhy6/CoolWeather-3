package com.jadyn.coolweather.ui.common;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JadynAi on 2016/2/16.
 */
public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<>();

    public static void addActivity(AppCompatActivity appCompatActivity) {
        activities.add(appCompatActivity);
    }

    public static void removeActivity(AppCompatActivity appCompatActivity) {
        activities.remove(appCompatActivity);
    }

    public static void finish(AppCompatActivity appCompatActivity) {
        appCompatActivity.finish();
    }

    public static void finishAll() {
        for (AppCompatActivity appCompatActivity : activities) {
            if (!appCompatActivity.isFinishing()) {
                appCompatActivity.finish();
            }
        }
    }
}
