package com.jadyn.coolweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jadyn.coolweather.R;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_NAME = "cityName";

    private String cityName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        cityName = intent.getStringExtra(CITY_NAME);
    }
}
