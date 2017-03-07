package com.cl.slack.danmu;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
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
    private final String mLongzhuCookie = "__gads=ID=054edad8cab3801f:T=1480097812:S=ALNI_Mbw26xPSdoxrCb8gwLekZYKwEFmqg; pgv_pvi=1897766912; PLULOGINSESSID=c52360452b7f00b4f8cad2537810d80e; p1u_id=28ce13c8d8fd3baad4348cb231e30e14c0f2a76f8b23f63ef34de6a1e7dd815b93c55e98f27db60a; pgv_si=s4648322048; _ma=OREN.2.1388103681.1480097814; pgv_info=ssid=s2413619159; pgv_pvid=8227009778";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        testWebView();

    }

//    String mLongzhuUrl =  "http://login.plu.cn/portable/login";
    /**
     * webview 登录 拿不到，还需要再刷新一个界面才可以
     * 此 cookie 可以 以后不用登录 还是斗鱼做的比较好 ，龙珠 登录 有问题  todo
     */
    String mLongzhuUrl =  "https://www.douyu.com/";// 斗鱼
    boolean mLoadLogin;

    @SuppressLint("SetJavaScriptEnabled")
    private void testWebView() {
        mWebView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( // 1,1);
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        addWebView();
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted url:  " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished url: " + url);
//                if(mLoadLogin ){
//                    mLoadLogin = false;
//                    mWebView.loadUrl("javascript:document.getElementById(\"topbar-login-button\").click();");
//                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.i(TAG, "onLoadResource url: " + url);
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(url);//从Html获取cookie
                if(!TextUtils.isEmpty(cookie)) {
                    /**
                     * 没有登录也有 cookie  需要排除  看是否包含 PHPSESSID   cookie 是 默认存储的
                     * _dys_lastPageCode=page_home,; _dys_refer_action_code=init_page_home; Hm_lvt_e99aee90ec1b2106afe7ec3b199020a7=1488865882; Hm_lpvt_e99aee90ec1b2106afe7ec3b199020a7=1488865882
                     */
                    Log.i(TAG, "cookie: " + cookie);
                }
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

        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAllowFileAccess(true);// 设置可以访问文件  貌似是cookie 需要
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);

        /**
         * cookie
         * set cookie first
         */
//        CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie(); // 清除 cookie  测试使用
//        cookieManager.removeAllCookie();
////        cookieManager.setCookie(mLongzhuUrl,mLongzhuCookie);
//        CookieSyncManager.getInstance().sync();

        /**
         * 龙珠 登录界面
         */
        mLoadLogin = true;
        mWebView.loadUrl(mLongzhuUrl);
    }

}
