package com.jadyn.coolweather.model;

/**
 * Created by JadynAi on 2016/2/26.
 */
public class City {
    /**
     * 封装市的数据类
     */

    public String cityName;
    public int provinceID;//所属省的编号
    public int id;

    public String getCityName() {
        return cityName;
    }


    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
