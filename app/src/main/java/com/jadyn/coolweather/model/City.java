package com.jadyn.coolweather.model;

/**
 * Created by JadynAi on 2016/2/26.
 */
public class City {
    /**
     * 封装省的数据类
     */

    private String cityName;
    private String cityCode;
    private int provinceID;
    private int id;

   

    public void setCityName(String provinceName) {
        this.cityName = cityName;
    }

    public void setCityCode(String provinceCode) {
        this.cityCode = cityCode;
    }


    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
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
