package com.jadyn.coolweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jadyn.coolweather.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView weclome_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        weclome_image = (ImageView) findViewById(R.id.welcome_image);
        new SwitchHandler().sendEmptyMessageDelayed(1, 1500);//发送一个空消息
    }

    public void click(View view) {
        weclome_image.setBackgroundResource(R.drawable.welcome_feather);
    }

    class SwitchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            WelcomeActivity.this.startActivity(intent);
            //欢迎界面动画效果，淡入淡出
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            WelcomeActivity.this.finish();
        }
    }

}
