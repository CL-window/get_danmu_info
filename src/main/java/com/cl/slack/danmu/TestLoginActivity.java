package com.cl.slack.danmu;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class TestLoginActivity extends BaseWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        testWebView();
    }


    private void testWebView() {
        mWebView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( // 1,1);
                FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        addWebView(0);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://www.panda.tv/");
    }



}
