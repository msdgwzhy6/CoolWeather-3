package com.jadyn.coolweather.utils;

import android.text.TextUtils;

import com.jadyn.coolweather.database.CoolWeaDB;
import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;
import com.jadyn.coolweather.model.Province;

/**
 * Created by JadynAi on 2016/2/27.
 */
public class Utility {
    /*
    * 解析服务器返回的省级数据。
    * 服务器返回的数据形式为：“代号|城市，代号|城市”
    * */
    public synchronized static boolean handleProvincesResponse(CoolWeaDB weaDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces!=null&&allProvinces.length>0) {
                for (String province : allProvinces) {
                    String[] codeAndName = province.split("\\|");
                    Province province1 = new Province();
                    province1.setProvinceCode(codeAndName[0]);
                    province1.setProvinceName(codeAndName[1]);
                    
                    weaDB.addProvince(province1);//添加到数据库
                }
                return true;
            }
        }
        return false;
    }

    /*
    * 解析服务器的城市数据
    * */
    public static boolean handleCityResponse(CoolWeaDB weaDB, String response, 
                                             int provinceID) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCitys = response.split(",");
            if (allCitys!=null&&allCitys.length>0) {
                for (String city : allCitys) {
                    String[] codeAndName = city.split("\\|");
                    City city1 = new City();
                    city1.setCityCode(codeAndName[0]);
                    city1.setCityName(codeAndName[1]);
                    city1.setProvinceID(provinceID);//省份编号

                    weaDB.addCity(city1);//添加到数据库
                }
                return true;
            }
        }
        return false;
    }
    /*
    * 解析服务器的县城数据
    * */
    public static boolean handleCountryResponse(CoolWeaDB weaDB, String response, 
                                             int cityID) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountrys = response.split(",");
            if (allCountrys!=null&&allCountrys.length>0) {
                for (String country : allCountrys) {
                    String[] codeAndName = country.split("\\|");
                    Country country1 = new Country();
                    country1.countryCode = codeAndName[0];
                    country1.countryName = codeAndName[1];
                    country1.cityID = cityID;

                    weaDB.addCountry(country1);//添加到数据库
                }
                return true;
            }
        }
        return false;
    }
}
