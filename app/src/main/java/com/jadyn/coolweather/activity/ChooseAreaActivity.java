package com.jadyn.coolweather.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.activity.adapter.CityExpandAdapter;
import com.jadyn.coolweather.activity.adapter.ProvinceAdapter;
import com.jadyn.coolweather.database.CoolWeaDB;
import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;
import com.jadyn.coolweather.model.Province;
import com.jadyn.coolweather.utils.HttpCallbackListener;
import com.jadyn.coolweather.utils.HttpUtil;
import com.jadyn.coolweather.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseAreaActivity extends AppCompatActivity {

    @Bind(R.id.choose_title)
    TextView chooseTitle;//标题文本

    @Bind(R.id.choose_list)
    ListView chooseList;//列表

    private ProgressDialog dialog;//进度对话框

    private CoolWeaDB weaDB;//数据库操作类

    List<Province> provinces;//省对象集合
    
    private ProvinceAdapter provinceAdapter;//省级适配器

    private PopupWindow popupWindow;//悬浮窗


    /*
    扩展列表
    * */
    private ArrayList<List<Country>> countriesExpand;//扩展列表的子项县城集合
    List<City> cities;//城市集合
    List<Country> countries;//县城集合
    private ExpandableListView expandableListView;//扩展列表
    private CityExpandAdapter expandAdapter;//扩展适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        ButterKnife.bind(this);

        weaDB = CoolWeaDB.getInstance(this);
        initDataProvince();//初始化省级数据
        initProvinceList();
    }

    /*
    * 查询省级城市
    * */
    private void initDataProvince() {
        provinces = weaDB.loadProvince();
        //先从数据库查询，查询不到再从网络查询
        if (provinces.size() > 0) {
            chooseList.setSelection(0);//设置当前选择的条目
        } else {
            queryFromServer(null, "province",0);
        }
    }

    //省级Listview初始化
    private void initProvinceList() {
        provinceAdapter = new ProvinceAdapter(this, provinces, R.layout.item_province);
        chooseList.setAdapter(provinceAdapter);
        //点击事件，每次点击都会生成一个悬浮窗，同时将省集合的ID和省的code传递过去
        chooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                initPopupWin(view, provinces.get(position).getId(),
                        provinces.get(position).getProvinceCode());
            }
        });
    }

    //省级子项点击弹出悬浮窗，显示市级列表
    private void initPopupWin(View view, int id, String provinceCode) {
        View view1 = LayoutInflater.from(this).inflate(R.layout.list_city_country,
                null, false);
        ExpandableListView expand = (ExpandableListView) view1.findViewById(R.id.list_city_expand);
        //通过省集合的省ID和省code得到城市集合的数据和相应县城集合的数据
        initExpandData(id,provinceCode);
        
        //四个参数含义，加载的布局、长度、宽度、是否获得焦点
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(android.R.anim.slide_in_left);

        ///这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                //如果返回true的话，touch事件会被拦截
                //拦截后Popupwindow的onTouchEvent不被调用，点击外部区域无法dismiss
            }
        });

        //设置一个背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));

        //设置悬浮窗显示的位置，参数一次是参照View，x轴偏移量，y轴的偏移量
        //以参照view为参照物，相对于Anchor锚的偏移
        popupWindow.showAsDropDown(view, 0, 0);

        setItemListen(expand);
    }

    //折叠列表点击选项
    private void setItemListen(final ExpandableListView expand) {
        expand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                /*initDataCountry(cities.get(groupPosition).getId(),
                        cities.get(groupPosition).getCityCode());*/
                return false;
            }
        });
        
        expand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                chooseTitle.setText(countriesExpand.get(groupPosition).get(childPosition).countryName);
                popupWindow.dismiss();
                return false;
            }
        });
    }

    //折叠列表初始化
    private void initExpandData(int id, String provinceCode) {
        initDataCity(id,provinceCode);
        for (int i = 0; i < cities.size(); i++) {
            initDataCountry(cities.get(i).getId(),cities.get(i).getCityCode());
            countriesExpand.add(countries);
        }
        
        expandableListView = (ExpandableListView) findViewById(R.id.list_city_expand);
        expandAdapter = new CityExpandAdapter(cities,countriesExpand,this);
        expandableListView.setAdapter(expandAdapter);
    }

    
    /*
   * 查询市级城市
   * */
    private void initDataCity(int provinceID,String provinceCode) {
        cities = weaDB.loadCity(provinceID);
        //先从数据库查询，查询不到再从网络查询
        if (!(cities.size() > 0)) 
            queryFromServer(provinceCode,"city",provinceID);
    }
    /*
   * 查询县城城市
   * */
    private void initDataCountry(int cityID,String cityCode) {
        countries = weaDB.loadCountry(cityID);
        //先从数据库查询，查询不到再从网络查询
        if (!(cities.size() > 0)) 
            queryFromServer(cityCode,"city",cityID);
    }

    //从网络查询
    private void queryFromServer(String code, final String type, final int ID) {
        String address;//url地址
        //如果有编码的话
        if (!TextUtils.isEmpty(code)) {
            address = getResources().getString(R.string.address) + code + ".xml";
        } else {
            //如果没有code的话
            address = getResources().getString(R.string.address_total);
        }
        shouProgressDialog();//显示进度条对话框

        //在子线程
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //成功访问
                boolean result = false;
                //省、市、县区分开
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(weaDB, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(weaDB, response,
                            ID);
                } else if ("country".equals(type)) {
                    result = Utility.handleCountryResponse(weaDB, response,
                            ID);
                }
                /*if (result) {
                    //回到主线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {

                            } else if ("city".equals(type)) {

                            } else if ("country".equals(type)) {

                            }
                        }
                    });
                }*/
            }

            @Override
            public void onError(Exception e) {
                //回到主线程，获取服务器资料失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    //打开进度条对话框
    private void shouProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this, 0);
            dialog.setMessage("正在努力加载中……");
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    //关闭进度条对话框
    private void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    
}
