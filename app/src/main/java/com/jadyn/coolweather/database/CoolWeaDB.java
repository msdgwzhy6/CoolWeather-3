package com.jadyn.coolweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jadyn.coolweather.model.Province;

/**
 * Created by JadynAi on 2016/2/26.
 * 封装数据库的一些操作，采用单例模式
 */
public class CoolWeaDB {
    
    //数据库名
    public static final String DB_NAME = "cool_weather";
    
    public static final int VERSION=1;//数据库版本
    public static final String PROVINCE = "Province";

    private static CoolWeaDB weaDB;//单例

    private SQLiteDatabase db;
    
    private CoolWeaDB(Context context){
        CoolDatabaseHelper coolDatabaseHelper = new CoolDatabaseHelper(context,
                DB_NAME,null,VERSION);
        db = coolDatabaseHelper.getWritableDatabase();
    }

    public static CoolWeaDB getInstance(Context context) {
        return new CoolWeaDB(context);//懒汉模式
    }

    //添加省份资料
    public void addProvince(Province province) {
        if (province!=null) {
            ContentValues values = new ContentValues();
            values.put(PROVINCE,province.getProvinceName());
            values.put(PROVINCE,province.getProvinceCode());
            db.insert(PROVINCE, null, values);
            values.clear();
            db.close();
        }
    }
    
    //删除省份资料
    public int deleteProvince(String provicneName){
        int rowID=db.delete(PROVINCE, "province_name = ?", new String[]{provicneName});
        return rowID;
    }
}
