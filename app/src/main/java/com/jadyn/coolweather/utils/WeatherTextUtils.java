package com.jadyn.coolweather.utils;

/**
 * Created by JadynAi on 2016/2/29.
 */
public class WeatherTextUtils {
    /*处理解析出来的json数据*/
    public static String processText(String msgOfWeather) {
        String newMsg = "";
        if (msgOfWeather!=null) {
           newMsg= msgOfWeather.replace("{","").
                    replace("}","").
                    replace("\"","")
                    .replace(",","\n")
                    .replace("fengxiang","风向为")
                    .replace("fengli","风力是")
                    .replace("high","最高温度")
                    .replace("type","天气状况")
                    .replace("low","最低温度")
                    .replace("data","日期为").trim();
        }
        return newMsg;
    }

    //处理得到的数据
    public static String getCityName(String c) {
        String cityName = "";
        if (c.contains("市")) {
            cityName = c.replace("市", "");
        } else if (c.contains("县")) {
            cityName = c.replace("市", "");
        }
        return cityName;
    }
}
