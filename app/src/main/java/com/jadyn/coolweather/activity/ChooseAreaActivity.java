package com.jadyn.coolweather.activity;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.activity.adapter.CityExpandAdapter;
import com.jadyn.coolweather.activity.adapter.ProvinceAdapter;
import com.jadyn.coolweather.activity.common.BaseActivity;
import com.jadyn.coolweather.database.CoolWeaDB;
import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;
import com.jadyn.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseAreaActivity extends BaseActivity {

    @Bind(R.id.choose_title)
    TextView chooseTitle;//标题文本

    @Bind(R.id.choose_list)
    ListView chooseList;//列表

    private ProgressDialog dialog;//进度对话框

    
    /*
    * =========数据库相关=========
    * */

    
    private CoolWeaDB weaDB;//数据库操作类

    private SQLiteDatabase db;

    private PopupWindow popupWindow;//悬浮窗
    
    /*
    * ==========省资料============
    * */

    List<Province> provinces;//省对象集合
    
    private ProvinceAdapter provinceAdapter;//省级适配器

    /*
    ==================扩展列表===========
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

        weaDB = new CoolWeaDB(this);
        initDataProvince();//初始化省级数据
        initProvinceList();
    }

    /*
    * 查询省级城市
    * */
    private void initDataProvince() {
        provinces = weaDB.loadProvince();
        
        if (provinces.size() > 0) 
            chooseList.setSelection(0);//设置当前选择的条目
        
    }

    //省级Listview初始化,子项弹出悬浮窗
    private void initProvinceList() {
        provinceAdapter = new ProvinceAdapter(this, provinces, R.layout.item_province);
        chooseList.setAdapter(provinceAdapter);
        //点击事件，每次点击都会生成一个悬浮窗，同时将省集合的ID和省的code传递过去
        chooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                initPopupWin(provinces.get(position).ProID);
            }
        });
    }

    //省级子项点击弹出悬浮窗，显示市级列表
    private void initPopupWin( int ProID) {
        View view = LayoutInflater.from(this).inflate(R.layout.list_city_country,
                null, false);
        //折叠列表
        expandableListView = (ExpandableListView) view.findViewById(R.id.list_city_expand);
        
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
        popupWindow.showAsDropDown(chooseTitle, 0, 0);

        //通过省集合的省ID和省code得到城市集合的数据和相应县城集合的数据
        initExpandData(ProID,expandableListView);

        setItemListen(expandableListView);
    }

    //折叠列表点击选项
    private void setItemListen(final ExpandableListView expand) {
        expand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                
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
    private void initExpandData(int ProID, ExpandableListView expandableListView) {
        //通过省ID查询相应的市ID
        cities = initDataCity(ProID);
        
        //查询所有市内相应的县城

        countriesExpand = new ArrayList<>();//初始化包含子项集合的集合
        for (int i = 0; i < cities.size(); i++) {
            countries = initDataCountry(cities.get(i).id);
            countriesExpand.add(countries);
        }
        
        expandAdapter = new CityExpandAdapter(cities,countriesExpand,this);
        expandableListView.setAdapter(expandAdapter);
    }

    
    /*
   * 根据省ID查询相应市级城市
   * */
    private List<City> initDataCity(int ProID) {
       List<City> cities = weaDB.loadCity(ProID);
        return cities;
    }
    /*
   * 根据市ID查询相应县城城市
   * */
    private List<Country> initDataCountry(int CityID) {
        List<Country> countries = weaDB.loadCountry(CityID);
        return countries;
    }


    //打开进度条对话框
    private void showProgressDialog() {
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
