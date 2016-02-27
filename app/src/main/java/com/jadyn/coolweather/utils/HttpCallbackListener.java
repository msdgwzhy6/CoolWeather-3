package com.jadyn.coolweather.utils;

/**
 * Created by JadynAi on 2016/2/25.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    
    void onError(Exception e);
}
