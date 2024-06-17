package com.moroom.android.base;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.kakao.sdk.common.KakaoSdk;
import com.moroom.android.BuildConfig;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_APPKEY);
    }
}