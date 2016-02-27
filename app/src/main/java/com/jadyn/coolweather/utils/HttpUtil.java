package com.jadyn.coolweather.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JadynAi on 2016/2/25.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String urlPath,
                                       final HttpCallbackListener callbackListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;//必须初始化给与一个值
                try {
                    URL url = new URL(urlPath);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(6000);
                    connection.setReadTimeout(4000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        inputStream = connection.getInputStream();
                        BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = bis.readLine()) != null) {
                            response.append(line);
                        }
                        if (callbackListener != null) {
                            //回调
                            callbackListener.onFinish(response.toString());
                        }
                    }

                } catch (Exception e) {
                    if (callbackListener != null) {
                        //回调
                        callbackListener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }
}
