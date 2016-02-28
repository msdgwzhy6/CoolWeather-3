package com.jadyn.coolweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolApplication;
import com.jadyn.coolweather.common.CoolLog;

/**
 * Created by JadynAi on 2016/2/26.
 * 创建省、市、镇三个表格
 */
public class CoolDatabaseHelper extends SQLiteOpenHelper {
    public CoolDatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CoolApplication.getContext().getResources().getString(R.string.province_sq));
        db.execSQL(CoolApplication.getContext().getResources().getString(R.string.city_sq));
        db.execSQL(CoolApplication.getContext().getResources().getString(R.string.country_sq));
        CoolLog.d("CoolDatabaseHelper","数据库创建成功");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
