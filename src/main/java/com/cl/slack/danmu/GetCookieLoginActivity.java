package com.cl.slack.danmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cl.slack.danmu.login.LiveLoginWebView;
import com.cl.slack.danmu.login.webview.*;


public class GetCookieLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie_login);

        Intent intent = getIntent();
        int id = intent.getIntExtra("platform", 0);

        LiveLoginWebView webView = null;

        switch (id){
            case R.id.douyu_btn:
                webView = new DouyuLoginWebView(this);
                break;
            case R.id.huya_btn:
                webView = new HuyaLoginWebView(this);
                break;
            case R.id.longzhu_btn:
                webView = new LongzhuLoginWebView(this);
                break;
            case R.id.quanmin_btn:
                webView = new QuanminLoginWebView(this);
                break;
            case R.id.sixroom_btn:
                webView = new SixroomLoginWebView(this);
                break;
            case R.id.xiongmao_btn:
                webView = new PandaLoginWebView(this);
                break;
            case R.id.zhanqi_btn:
                webView = new ZhanqiLoginWebView(this);
                break;
        }

        ViewGroup loginWebViewContainer = (ViewGroup) findViewById(R.id.webview_container);

        loginWebViewContainer.addView(webView);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.webview_progress);

        webView.setProgressBar(progressBar);
        webView.setOnCookieCallback(new LiveLoginWebView.OnCookieCallback() {
            @Override
            public void onCookie(String cookie) {
                Intent intent = getIntent();
                intent.putExtra("cookie",cookie);
                setResult(0x101,intent);
                finish();
            }
        });
        webView.load();


    }
}
