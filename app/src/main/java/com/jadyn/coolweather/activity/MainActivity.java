package com.jadyn.coolweather.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.activity.adapter.ViewPagerAdapter;
import com.jadyn.coolweather.common.CoolLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_NAME = "cityName";

    private String path;//路径

    private String cityName;


    private ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter;

    private List<View> data;
    
    TextView text1;
    TextView text2;
    TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = getResources().getString(R.string.weather_url);

        Intent intent = getIntent();
        String name = intent.getStringExtra(CITY_NAME);
        getCityName(name);

        initData();
        initViewPager();

        WeatherAsynTask asynTask = new WeatherAsynTask();
        asynTask.execute();
    }
    //初始化viewpager
    private void initViewPager() {
        
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        pagerAdapter = new ViewPagerAdapter(data);
        viewPager.setAdapter(pagerAdapter);
    }
    //初始化viewpager使用的数据源
    private void initData() {
        data=new ArrayList<>();
        View today = LayoutInflater.from(this).inflate(R.layout.weather_1, null, false);
        View tommorw = LayoutInflater.from(this).inflate(R.layout.weather_2, null, false);
        View afterTom = LayoutInflater.from(this).inflate(R.layout.weather_3, null, false);

        text1 = (TextView) today.findViewById(R.id.weather1_text);
        text2 = (TextView) tommorw.findViewById(R.id.weather2_text);
        text3 = (TextView) afterTom.findViewById(R.id.weather3_text);
        data.add(today);
        data.add(tommorw);
        data.add(afterTom);
    }
    //得到城市名字
    public void getCityName(String name) {
        if (name.contains("省")) {
            cityName=name.replace("省","");
        } else if (name.contains("市")) {
            cityName=name.replace("市","");
        } else if (name.contains("县")) {
            cityName=name.replace("市","");
        }
    }
    //异步任务处理类
    class WeatherAsynTask extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            JSONArray array = null;
            HttpURLConnection conn=null;
            try {
                url = new URL(path + "=" + URLEncoder.encode(cityName, "utf-8"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                int code = conn.getResponseCode();

                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    String jsonData = StreamTool.decodeStream(in);

                    JSONObject jsonObje = new JSONObject(jsonData);

                    String desc = jsonObje.getString("desc");
                    if ("OK".equals(desc)) {

                        JSONObject dataObj = jsonObje.getJSONObject("data");
                        array = dataObj.getJSONArray("forecast");
                    } 
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return array;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            //设置今天、明天、后天的天气咨询文本内容
            try {
                CoolLog.i("MainActivity1",jsonArray.getJSONObject(0)+"");
                CoolLog.i("MainActivity1",jsonArray.getJSONObject(1)+"");
                CoolLog.i("MainActivity1",jsonArray.getJSONObject(2)+"");
                text1.setText(jsonArray.getJSONObject(0)+"");
                text2.setText(jsonArray.getJSONObject(1)+"");
                text3.setText(jsonArray.getJSONObject(2)+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
//数据流处理类
class StreamTool {

    //解析 流的数据,返回字符串 
    public static String decodeStream(InputStream in) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            baos.write(buf, 0, len);
        }

        in.close();
        baos.close();

        return baos.toString();
    }


}
