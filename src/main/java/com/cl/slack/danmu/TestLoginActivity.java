package com.cl.slack.danmu;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * @author slack
 * @time 17/3/3 下午1:18
 * get web chrome User Agent :http://www.useragentstring.com/index.php
 */
public class TestLoginActivity extends BaseWebViewActivity {

    private static final String TAG = "login";
    // phone Mozilla/5.0 (Linux; Android 6.0.1; MI 5 Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36
//    private final String WEB_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        testWebView();
        /**
         * cookie
         */
        CookieSyncManager.createInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    String mLongzhuUrl = "http://www.longzhu.com/";
    boolean mLoadLogin;

    private void testWebView() {
        mWebView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( // 1,1);
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        addWebView(0);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Log.i(TAG, "onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished url: " + url);
                if(mLoadLogin ){
                    mLoadLogin = false;
                    mWebView.loadUrl("javascript:document.getElementById(\"topbar-login-button\").click();");
                }

                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(url);//从Html获取cookie
                Log.i(TAG, "cookie: " + cookie);
                CookieSyncManager.getInstance().sync();
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
//                Log.i(TAG, "onReceivedLoginRequest realm: " + realm + " account : " + account + " args : " + args);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "shouldOverrideUrlLoading url: " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                Log.i(TAG, "shouldOverrideUrlLoading request: " + request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
//                Log.i(TAG, "onLoadResource url: " + url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
//                Log.i(TAG, "onPageCommitVisible url: " + url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                Log.i(TAG, "shouldInterceptRequest url: " + url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                Log.i(TAG, "shouldInterceptRequest request: " + request.toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
                super.onTooManyRedirects(view, cancelMsg, continueMsg);
//                Log.i(TAG, "onTooManyRedirects cancelMsg: " + cancelMsg.toString() + " continueMsg :" + continueMsg.toString()) ;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                Log.i(TAG, "onReceivedError errorCode: " + errorCode + " description : " + description + " failingUrl : " + failingUrl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                Log.i(TAG, "onReceivedError request: " + request.toString() + " error : " + error.toString());
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                Log.i(TAG, "onReceivedError request: " + request.toString() + " errorResponse : " + errorResponse.toString());
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
//                Log.i(TAG, "onFormResubmission dontResend: " + dontResend.toString() + " resend : " + resend.toString());
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
//                Log.i(TAG, "doUpdateVisitedHistory url: " + url + " isReload : " + isReload);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
//                Log.i(TAG, "onReceivedSslError handler: " + handler.toString() + " error : " + error.toString());
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
//                Log.i(TAG, "onReceivedClientCertRequest request: " + request.toString() );
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
//                Log.i(TAG, "onReceivedHttpAuthRequest handler: " + handler.toString() + " host : " + host + " realm : " + realm);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//                Log.i(TAG, "shouldOverrideKeyEvent event: " + event.toString() );
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
//                Log.i(TAG, "onUnhandledKeyEvent event: " + event.toString() );
                super.onUnhandledKeyEvent(view, event);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
//                Log.i(TAG, "onScaleChanged oldScale: " + oldScale + " newScale : " + newScale);
            }
        });
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
//        Log.i(TAG,"ua " + setting.getUserAgentString());


        setting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        setting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        setting.setSupportZoom(true); // 支持缩放
        setting.setBuiltInZoomControls(true); // 支持手势缩放
        setting.setDisplayZoomControls(false); //隐藏原生的缩放控件

        /**
         * 龙珠 登录界面
         */
        mLoadLogin = true;
        mWebView.loadUrl(mLongzhuUrl);
//        mWebView.loadUrl("http://www.panda.tv/");
    }

}
