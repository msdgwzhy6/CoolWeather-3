package com.jadyn.coolweather.model;


public class Province {
    /**
     * 封装省的数据类
     */

    private String provinceName;
    private String provinceCode;
    private int id;

    public Province(String provinceName, String provinceCode, int id) {
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
        this.id = id;
    }

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
