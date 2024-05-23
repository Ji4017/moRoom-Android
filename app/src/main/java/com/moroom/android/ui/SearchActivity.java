package com.moroom.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moroom.android.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 최초 웹뷰 로드 완료시 Android에서 javascript 함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        // 최초 웹뷰 로드
        webView.loadUrl("https://moroomtest.web.app");
    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            // 다음 주소 검색 API의 결과 값이 브릿지 통로를 통해 자바스크립트로 전달 받음
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}