package com.jadyn.coolweather.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolDate;
import com.jadyn.coolweather.common.CoolLog;
import com.jadyn.coolweather.model.Weather;
import com.jadyn.coolweather.ui.adapter.WeatherListAdapter;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String CITY_NAME = "cityName";

    DrawerLayout drawerLayout;

    @Bind(R.id.main_image)
    ImageView mainImage;
    @Bind(R.id.main_list)
    ListView mainList;
    @Bind(R.id.main_navi)
    NavigationView mainNavi;

    private View topbar;


    private List<Weather> data;
    private WeatherListAdapter listAdapter;

    private String path;//路径

    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        path = getResources().getString(R.string.weather_url);

        Intent intent = getIntent();
        String name = intent.getStringExtra(CITY_NAME);
        getCityName(name);

        initView();

        data = new ArrayList<>();//listview数据源

        WeatherAsynTask asynTask = new WeatherAsynTask();
        asynTask.execute();

        initList();
    }

    //初始化DrawerLayout以及一些控件
    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        topbar = findViewById(R.id.main_top);
        TextView topTitle = (TextView) topbar.findViewById(R.id.topbar_text);
        topTitle.setText(cityName);

        CoolLog.i("Time",CoolDate.YEAR+""+CoolDate.MONTH+""+CoolDate.HOUR+"");
        if (CoolDate.HOUR > 6 && CoolDate.HOUR < 18) {
            mainImage.setImageResource(R.drawable.sunrise);
        } else {
            mainImage.setImageResource(R.drawable.sunset);
        }
    }

    //得到城市名字
    public void getCityName(String name) {
        if (name.contains("市")) {
            cityName = name.replace("市", "");
        } else if (name.contains("县")) {
            cityName = name.replace("市", "");
        }
    }
    //初始化ListView
    private void initList() {
        listAdapter = new WeatherListAdapter(MainActivity.this, data,
                R.layout.item_list_weather);
        mainList.setAdapter(listAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navi_city:
                startActivity(new Intent(MainActivity.this, ChooseAreaActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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
            HttpURLConnection conn = null;
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
                    in.close();
                    conn.disconnect();
                } else {
                    Toast.makeText(MainActivity.this, "抱歉！我的小主，暂时无法从服务器获得数据", 
                            Toast.LENGTH_SHORT).show();
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
            if (jsonArray != null) {
                try {
                    CoolLog.i("MainActivity1", jsonArray.getJSONObject(0) + "");
                    CoolLog.i("MainActivity1", jsonArray.getJSONObject(1) + "");
                    CoolLog.i("MainActivity1", jsonArray.getJSONObject(2) + "");
                    Weather weather = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String msgWea = jsonArray.getJSONObject(i) + "";
                        if (msgWea.contains("晴")) {
                            weather = new Weather(msgWea, R.drawable.sunshine);
                        } else if (msgWea.contains("多云")) {
                            weather = new Weather(msgWea, R.drawable.cloudy);
                        }
                        data.add(weather);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "无网络连接，并不能获取数据，请您联网吧！", 
                        Toast.LENGTH_LONG).show();
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
