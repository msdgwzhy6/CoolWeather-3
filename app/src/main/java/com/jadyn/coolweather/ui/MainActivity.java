package com.jadyn.coolweather.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.common.CoolLog;
import com.jadyn.coolweather.ui.fragment.ContentFragment;
import com.jadyn.coolweather.utils.WeatherTextUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String CITY_NAME = "cityName";
    private static final String KEY = "city";

    @Bind(R.id.main_image)
    ImageView mainImage;
    @Bind(R.id.main_navi)
    NavigationView mainNavi;
    @Bind(R.id.main_fab)
    FloatingActionButton mainFab;

    @Bind(R.id.main_drawer)
    DrawerLayout mainDrawer;
    Toolbar mainTooBar;
    @Bind(R.id.toolbar_text)
    TextView toolbarText;
    @Bind(R.id.main_frame)
    FrameLayout mainFrame;

    private String name = "深圳";

    private Calendar calendar;

    private ContentFragment contentFragment;
    
    FragmentManager fragmentManager;
    
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainTooBar = (Toolbar) findViewById(R.id.main_toolbar);

        initView();
    }


    /*在resume方法内添加fragment*/
    @Override
    protected void onResume() {
        ContentFragment fragment = new ContentFragment();
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_frame, fragment).commit();
        super.onResume();
    }
    

    //初始化DrawerLayout以及一些控件
    private void initView() {
        mainTooBar.setTitle("");
        toolbarText.setText(name);
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

        mainImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                CoolLog.e("Time", hour + "");
                if (hour > 18 || hour < 6) {
                    mainImage.setBackgroundResource(R.drawable.sunset);
                } else {
                    mainImage.setBackgroundResource(R.drawable.sunrise);
                }
            }
        }, 1000);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String resultName = data.getStringExtra(CITY_NAME);
            
            CoolLog.i("MainActivity", resultName);
            
            resultName = WeatherTextUtils.getCityName(resultName);
            
            CoolLog.i("MainActivity", resultName);
            toolbarText.setText(resultName);

            /*一旦从选择城市中得到数据，就重写new一个FragmentTransaction，再次提交*/
            Bundle bundle = new Bundle();
            ContentFragment contentFragment = new ContentFragment();
            bundle.getString(KEY, resultName);
            contentFragment.setArguments(bundle);
            
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_frame, contentFragment).commit();
        }
    }
    

    @Override
    protected void onDestroy() {
        if (calendar != null) {
            calendar = null;
        }
        super.onDestroy();
    }
}


