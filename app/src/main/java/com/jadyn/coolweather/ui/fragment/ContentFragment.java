package com.jadyn.coolweather.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolApplication;
import com.jadyn.coolweather.common.CoolLog;
import com.jadyn.coolweather.model.Weather;
import com.jadyn.coolweather.ui.adapter.WeatherListAdapter;
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

/**
 * Created by JadynAi on 2016/3/3.
 */
public class ContentFragment extends Fragment {
    private List<Weather> data;

    private WeatherListAdapter listAdapter;

    private ListView mainList;

    private static final String PATH = "http://wthrcdn.etouch.cn/weather_mini?city";

    private static final String KEY = "city";

    private String city_name = null;

    ProgressDialog dialog;

    public ContentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        data = new ArrayList<>();

        mainList = (ListView) view.findViewById(R.id.frag_content_list);
        Bundle bundle = getArguments();

        if (bundle != null) {
            city_name = bundle.getString(KEY, "深圳");
            getWeaFromUrl(city_name);
        } else {
            getWeaFromUrl("深圳");
        }

        return view;
    }


    public void getWeaFromUrl(String cityOfName) {
        WeatherAsynTask asynTask = new WeatherAsynTask();
        asynTask.execute(cityOfName);
    }

    //初始化ListView,必须要从网络获取到值才会初始化此ListView
    private void initList() {
        listAdapter = new WeatherListAdapter(getActivity(), data,
                R.layout.item_list_weather);
        mainList.setAdapter(listAdapter);
    }

    class WeatherAsynTask extends AsyncTask<String, Integer, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }


        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            JSONArray array = null;
            HttpURLConnection conn = null;
            String address = params[0];
            try {
                url = new URL(PATH + "=" + URLEncoder.encode(address, "utf-8"));
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
                    Toast.makeText(CoolApplication.getContext(), "抱歉！我的小主，无网络暂时无法从服务器获得数据",
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
                    Weather weather = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String msgWea = jsonArray.getJSONObject(i) + "";
                        msgWea = WeatherTextUtils.processText(msgWea);
                        CoolLog.i("MainActivity", msgWea);
                        if (msgWea.contains("晴")) {
                            weather = new Weather(msgWea, R.drawable.sunny);
                        } else if (msgWea.contains("多云")) {
                            weather = new Weather(msgWea, R.drawable.cloudy);
                        }
                        data.add(weather);
                    }
                    initList();
                    closeProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                closeProgress();
                Toast.makeText(CoolApplication.getContext(), "暂时没有此城市的数据，我们会尽快弥补！",
                        Toast.LENGTH_LONG).show();
            }

        }


    }

    public void showProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
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