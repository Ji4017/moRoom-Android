package com.moroom.android.base;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.kakao.vectormap.KakaoMapSdk;
import com.moroom.android.BuildConfig;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Kakao Map SDK 초기화
        KakaoMapSdk.init(this, BuildConfig.KAKAO_SDK_APPKEY);
    }
}