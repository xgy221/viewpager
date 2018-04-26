package com.njue.xgy.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by XGY on 2017/4/8.
 */

public class WelcomeActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean ans=preferences.getBoolean("firststart", true);
                if (ans) {
                    editor = preferences.edit();
                    // 将登录标志位设置为false，下次登录时不再显示引导页面
                    editor.putBoolean("firststart", false);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, GuiderActivity.class);
                    WelcomeActivity.this.startActivity(intent);
                    WelcomeActivity.this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    WelcomeActivity.this.startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}