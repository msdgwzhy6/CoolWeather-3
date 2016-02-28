package com.jadyn.coolweather.database;

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
    
    public static final String PROVINCE = "T_Province";//省表格
    public static final String CITY = "T_City";
    public static final String COUNTRY = "T_Zone";

    private Context context;
    private SQLiteDatabase db;
    public CoolWeaDB(Context context) {
        this.context = context;
        DBManager dbManager = new DBManager(context);
        dbManager.openDatabase();
        db = dbManager.getDatabase();
    }

    //查询省资料
    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query(PROVINCE, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Province province = new Province();
            
            int id = cursor.getInt(cursor.getColumnIndex("ProSort"));
            province.ProID = id;
            
            String name = cursor.getString(cursor.getColumnIndex("ProName"));
            province.provinceName = name;
            
            list.add(province);
        }
        
        cursor.close();
        
        return list;
    }
   
    //查城市资料
    public List<City> loadCity(int ProID) {
        List<City> list = new ArrayList<>();
        
        Cursor cursor = db.query(CITY, null, "ProID = ?", 
                new String[]{String.valueOf(ProID)}, null, null, null);

        while (cursor.moveToNext()) {
            City city = new City();

            city.provinceID = ProID;
            
            int id = cursor.getInt(cursor.getColumnIndex("CitySort"));
            city.id = id;
            
            String name = cursor.getString(cursor.getColumnIndex("CityName"));
            city.cityName = name;

            list.add(city);
        }
        
        cursor.close();
        
        return list;
    }
   
    //查城市内的县城
    public List<Country> loadCountry(int CityID) {
        List<Country> list = new ArrayList<>();
        //查询特殊城市编号的县城资料
        Cursor cursor = db.query(COUNTRY, null, "CityID = ?", 
                new String[]{String.valueOf(CityID)}, null, null, null);

        while (cursor.moveToNext()) {
            Country country = new Country();
            country.cityID = CityID;
            
            country.id = cursor.getInt(cursor.getColumnIndex("ZoneID"));
            
            country.countryName = cursor.getString(cursor.getColumnIndex("ZoneName"));

            list.add(country);
        }
        
        cursor.close();
        
        return list;
    }

    //删除城市资料
    public int deleteCity(SQLiteDatabase db,String cityName){
        int rowID=db.delete(CITY, "city_name = ?", new String[]{cityName});
        return rowID;
    }
    
    //删除县城资料
    public int deleteCountry(SQLiteDatabase db,String countryName){
        int rowID=db.delete(CITY, "country_name = ?", new String[]{countryName});
        return rowID;
    }
    public void closeDB(SQLiteDatabase db) {
        db.close();
    }
}
