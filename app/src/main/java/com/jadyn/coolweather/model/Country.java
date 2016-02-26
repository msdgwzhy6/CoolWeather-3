package com.jadyn.coolweather.model;

/**
 * Created by JadynAi on 2016/2/26.
 */
public class Country {
    /**
     * 镇数据封装类
     */

    private int cityID;
    private String countryName;
    private String countryCode;


    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
