package com.jadyn.coolweather.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolDate;
import com.jadyn.coolweather.common.CoolLog;
import com.jadyn.coolweather.database.CoolWeaDB;
import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;
import com.jadyn.coolweather.model.Province;
import com.jadyn.coolweather.ui.adapter.CityExpandAdapter;
import com.jadyn.coolweather.ui.adapter.ProvinceAdapter;
import com.jadyn.coolweather.ui.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseAreaActivity extends BaseActivity {


    public static final String CITY_NAME = "cityName";
    /*
            * UI组件
            * */
    @Bind(R.id.choose_title)
    TextView chooseTitle;//标题文本

    @Bind(R.id.choose_list)
    ListView chooseList;//列表

    /*
    * =========数据库相关=========
    * */


    private CoolWeaDB weaDB;//数据库操作类

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

    private boolean isOpenAgain;

    /*
    * ========屏幕长宽============
    * */
    private int width;
    private int height;
    private int chooseListWidth;
    
    
    /*
    * =========系统时间===========
    * */

    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        ButterKnife.bind(this);

        /*
        * 屏幕长宽
        * */
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        chooseListWidth = chooseList.getLayoutParams().width;
        
        /*
        * 系统时间，根据月份设置背景
        * */
        month = CoolDate.MONTH;//获得系统当前月份
        CoolLog.d("calendar", month + "");
        setListBack(chooseList);

        weaDB = new CoolWeaDB(this);
        initDataProvince();//初始化省级数据
        initProvinceList();
    }

    //设置背景根据月份
    private void setListBack(ListView chooseList) {
        if (month >= 2 && month < 5) {
            chooseList.setBackgroundResource(R.drawable.spring);//春
        } else if (month >= 5 && month < 8) {
            chooseList.setBackgroundResource(R.drawable.summer);//夏
        } else if (month >= 8 && month < 11) {
            chooseList.setBackgroundResource(R.drawable.autumn);//秋
        } else if (month >= 11 && month < 2) {
            chooseList.setBackgroundResource(R.drawable.winter_2);//冬
        }
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
    private void initPopupWin(int ProID) {
        View view = LayoutInflater.from(this).inflate(R.layout.list_city_country,
                null, false);
        //折叠列表
        expandableListView = (ExpandableListView) view.findViewById(R.id.list_city_expand);

        //四个参数含义，加载的布局、长度、宽度、是否获得焦点
        popupWindow = new PopupWindow(view, width * 2 / 3,
                height * 2 / 3, true);

        popupWindow.setAnimationStyle(android.R.anim.slide_in_left);

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
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));

        //设置悬浮窗显示的位置，参数一次是参照View，x轴偏移量，y轴的偏移量
        //以参照view为参照物，相对于Anchor锚的偏移
        int xOff = width / 2;
        popupWindow.showAtLocation(chooseList, Gravity.CENTER, 0, 0);

        //通过省集合的省ID和省code得到城市集合的数据和相应县城集合的数据
        initExpandData(ProID, expandableListView);

        setItemListen(expandableListView);//设置折叠列表的点击事件
    }

    //折叠列表点击事件
    private void setItemListen(final ExpandableListView expand) {
        expand.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String cityName = cities.get(groupPosition).cityName;
                chooseTitle.setText(cityName);
                //打开对话框，是否继续向下查看
                if (!isOpenAgain) {//对话框只会打开一次
                    showAlertDialog(cityName);
                    isOpenAgain = true;
                }

                return false;
            }
        });

        expand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String countryName = countriesExpand.get(groupPosition).get(childPosition).countryName;
                chooseTitle.setText(countryName);

                Intent intent = new Intent();
                intent.putExtra(CITY_NAME, countryName);
                setResult(RESULT_OK,intent);
                popupWindow.dismiss();

                finish();
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
            //读取县级数据，并加入到包含集合中去
            countries = initDataCountry(cities.get(i).id);
            countriesExpand.add(countries);
        }
        //折叠列表加载适配器
        expandAdapter = new CityExpandAdapter(cities, countriesExpand, this);
        expandableListView.setAdapter(expandAdapter);
        expandableListView.setAlpha(0.8f);
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

    private void showAlertDialog(final String cityName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否查看此城市天气");
        builder.setMessage("小主是要查看此城市天气，还是继续巡游此城?");
        builder.setPositiveButton("看此城", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra(CITY_NAME, cityName);
                setResult(RESULT_OK,intent);
                popupWindow.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("继续看看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOpenAgain = false;//继续浏览县城将此值重置为false,确保重新选择城市时还会弹出对话框
            }
        });
        builder.show();
    }

}
