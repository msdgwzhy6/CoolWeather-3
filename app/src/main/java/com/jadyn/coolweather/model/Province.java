package com.jadyn.coolweather.model;

/**
 * Created by JadynAi on 2016/2/26.
 */
public class Province {
    /**
     * 封装省的数据类
     */

    private String provinceName;
    private String provinceCode;
    private int id;

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public int getId() {
        return id;
    }
}
