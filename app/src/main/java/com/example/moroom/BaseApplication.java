package com.example.moroom;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}