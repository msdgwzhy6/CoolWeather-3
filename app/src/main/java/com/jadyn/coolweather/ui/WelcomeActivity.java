package com.jadyn.coolweather.ui;

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


    
    @Bind(R.id.welcome_image)
    ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        new SwitchHandler().sendEmptyMessageDelayed(1, 3500);//发送一个空消息
    }

    @OnClick(R.id.welcome_image)
    public void onClick() {
        welcomeImage.setImageResource(R.drawable.welcome_feather);
    }

    class SwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            WelcomeActivity.this.startActivity(intent);
            //欢迎界面动画效果，淡入淡出
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            WelcomeActivity.this.finish();
        }
    }
    
}
