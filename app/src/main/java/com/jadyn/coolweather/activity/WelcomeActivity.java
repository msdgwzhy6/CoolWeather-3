package com.jadyn.coolweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jadyn.coolweather.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this,ChooseAreaActivity.class));
        }
    };
    @Bind(R.id.welcome_image)
    ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        handler.postDelayed(runnable, 3500);
    }

    @OnClick(R.id.welcome_image)
    public void onClick() {
    }
}
