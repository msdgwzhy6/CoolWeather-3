package com.jadyn.coolweather.model;


public class Province {
    /**
     * 封装省的数据类
     */

    public String provinceName;
    public int ProID;
    

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }


    public void setId(int id) {
        this.ProID = id;
    }

    public String getProvinceName() {
        return provinceName;
    }


    public int getId() {
        return ProID;
    }
}
