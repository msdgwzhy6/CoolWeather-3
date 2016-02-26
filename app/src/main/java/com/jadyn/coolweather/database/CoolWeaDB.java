package com.jadyn.coolweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;
import com.jadyn.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JadynAi on 2016/2/26.
 * 封装数据库的一些操作，采用单例模式
 */
public class CoolWeaDB {
    
    //数据库名
    public static final String DB_NAME = "cool_weather";
    
    public static final int VERSION=1;//数据库版本
    public static final String PROVINCE = "Province";
    public static final String CITY = "City";
    public static final String COUNTRY = "Country";

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
//            db.close();只有一个db对象，不需要关闭
        }
    }
    
    //删除省份资料
    public int deleteProvince(String provicneName){
        int rowID=db.delete(PROVINCE, "province_name = ?", new String[]{provicneName});
        return rowID;
    }
    
    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query(PROVINCE, null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("province_name"));
            String code = cursor.getString(cursor.getColumnIndex("province_code"));

            Province province = new Province(name, code, id);
            list.add(province);
        }
        
        cursor.close();
        
        return list;
    }
    //添加城市资料
    public void addCity(City city) {
        if (city!=null) {
            ContentValues values = new ContentValues();
            values.put(CITY,city.getCityName());
            values.put(CITY,city.getCityCode());
            values.put(CITY,city.getProvinceID());
            db.insert(CITY, null, values);
            values.clear();
//            db.close();只有一个db对象，不需要关闭
        }
    }
    
    //删除城市资料
    public int deleteCity(String cityName){
        int rowID=db.delete(CITY, "city_name = ?", new String[]{cityName});
        return rowID;
    }
    
    //查省内的城市资料
    public List<City> loadCity(int provinceID) {
        List<City> list = new ArrayList<>();
        //查询特殊省编号的城市资料
        Cursor cursor = db.query(CITY, null, "province_id = ?", 
                new String[]{String.valueOf(provinceID)}, null, null, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("city_name"));
            String code = cursor.getString(cursor.getColumnIndex("city_code"));

            City city = new City(name, code, provinceID, id);
            list.add(city);
        }
        
        cursor.close();
        
        return list;
    }
    //添加县城资料
    public void addCountry(Country country) {
        if (country!=null) {
            ContentValues values = new ContentValues();
            values.put(COUNTRY,country.cityID);
            values.put(COUNTRY,country.countryCode);
            values.put(COUNTRY,country.countryName);
            db.insert(COUNTRY, null, values);
            values.clear();
//            db.close();只有一个db对象，不需要关闭
        }
    }
    
    //删除县城资料
    public int deleteCountry(String countryName){
        int rowID=db.delete(CITY, "country_name = ?", new String[]{countryName});
        return rowID;
    }
    
    //查城市内的线程资料
    public List<Country> loadCountry(int cityID) {
        List<Country> list = new ArrayList<>();
        //查询特殊城市编号的县城资料
        Cursor cursor = db.query(CITY, null, "city_id = ?", 
                new String[]{String.valueOf(cityID)}, null, null, null);

        while (cursor.moveToNext()) {
            Country country = new Country();
            country.cityID = cityID;
            country.id = cursor.getInt(cursor.getColumnIndex("_id"));
            country.countryCode = cursor.getString(cursor.getColumnIndex("country_code"));
            country.countryName = cursor.getString(cursor.getColumnIndex("country_name"));

            list.add(country);
        }
        
        cursor.close();
        
        return list;
    }

    public void closeDB() {
        db.close();
    }
}
