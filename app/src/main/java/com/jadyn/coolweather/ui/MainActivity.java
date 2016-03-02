package com.jadyn.coolweather.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolDate;
import com.jadyn.coolweather.common.CoolLog;
import com.jadyn.coolweather.model.Weather;
import com.jadyn.coolweather.ui.adapter.WeatherListAdapter;
import com.jadyn.coolweather.ui.setting.Setting;
import com.jadyn.coolweather.utils.WeatherTextUtils;

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

    @Bind(R.id.main_image)
    ImageView mainImage;
    @Bind(R.id.main_list)
    ListView mainList;
    @Bind(R.id.main_navi)
    NavigationView mainNavi;
    @Bind(R.id.main_fab)
    FloatingActionButton mainFab;

    
    @Bind(R.id.main_appbar)
    AppBarLayout mainAppbar;

    @Bind(R.id.main_drawer)
    DrawerLayout mainDrawer;
    @Bind(R.id.main_coorddinatorLayout)
    CoordinatorLayout mainCoorddinatorLayout;
    Toolbar mainTooBar;


    private ProgressDialog dialog;

    private List<Weather> data;
    private WeatherListAdapter listAdapter;

    private String path;//路径

    private String name = "深圳";

    private Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainTooBar = (Toolbar) findViewById(R.id.main_toolbar);

        path = getResources().getString(R.string.weather_url);

        setting = Setting.getsInstance(this);

        initView();

        getWeaFromUrl(name);
    }


    private void getWeaFromUrl(String cityOfName) {
        WeatherAsynTask asynTask = new WeatherAsynTask();
        asynTask.execute(cityOfName);
    }


    //初始化DrawerLayout以及一些控件
    private void initView() {
        mainTooBar.setTitle("");
        setSupportActionBar(mainTooBar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawer, mainTooBar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        mainDrawer.setDrawerListener(drawerToggle);

        mainNavi.setNavigationItemSelectedListener(this);//导航栏点击

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("鼓励一下")
                        .setMessage("给作者点个赞吧，小主")
                        .setPositiveButton("准了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "谢赏",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("跪安吧", null).show();
            }
        });

        CoolLog.i("Time", CoolDate.YEAR + "" + CoolDate.MONTH + "" + CoolDate.HOUR + "");

        setting.putInt(Setting.CURRENT_HOUR, CoolDate.HOUR);
        if (setting.getInt(Setting.CURRENT_HOUR, 0) > 18 || setting.getInt(Setting.CURRENT_HOUR, 0) < 6) {
            Glide.with(this).load(R.mipmap.sunset).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mainImage);
        }

    }


    //初始化ListView,必须要从网络获取到值才会初始化此ListView
    private void initList() {
        closeProgress();
        listAdapter = new WeatherListAdapter(MainActivity.this, data,
                R.layout.item_list_weather);
        mainList.setAdapter(listAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navi_city:
                startActivityForResult(new Intent(MainActivity.this,
                        ChooseAreaActivity.class), 1);
                break;
        }
        mainDrawer.closeDrawer(GravityCompat.START);
        return false;
    }


    //异步任务处理类
    class WeatherAsynTask extends AsyncTask<String, Integer, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = new ArrayList<>();//listview数据源
            shouProgress();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            JSONArray array = null;
            HttpURLConnection conn = null;
            String address = params[0];
            try {
                url = new URL(path + "=" + URLEncoder.encode(address, "utf-8"));
                CoolLog.i("MainActivity", address);
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
                    Toast.makeText(MainActivity.this, "抱歉！我的小主，无网络暂时无法从服务器获得数据",
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
                    CoolLog.i("MainActivity", jsonArray.getJSONObject(0) + "");
                    CoolLog.i("MainActivity", jsonArray.getJSONObject(1) + "");
                    CoolLog.i("MainActivity", jsonArray.getJSONObject(2) + "");
                    Weather weather = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String msgWea = jsonArray.getJSONObject(i) + "";
                        msgWea = WeatherTextUtils.processText(msgWea);
                        CoolLog.i("MainActivity", msgWea);
                        if (msgWea.contains("晴")) {
                            weather = new Weather(msgWea, R.drawable.sunshine);
                        } else if (msgWea.contains("多云")) {
                            weather = new Weather(msgWea, R.drawable.cloudy);
                        }
                        data.add(weather);
                    }
                    initList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                closeProgress();
                Toast.makeText(MainActivity.this, "暂时没有此城市的数据，我们会尽快弥补！",
                        Toast.LENGTH_LONG).show();
            }

        }

    }


    public void shouProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("小主稍后，通通正在努力加载中……");
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }


    public void closeProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String resultName = data.getStringExtra(CITY_NAME);
            CoolLog.i("MainActivity", resultName);
            resultName = WeatherTextUtils.getCityName(resultName);
            CoolLog.i("MainActivity", resultName);
            getWeaFromUrl(resultName);
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
