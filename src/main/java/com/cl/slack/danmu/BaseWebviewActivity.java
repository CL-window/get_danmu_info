package com.cl.slack.danmu;

import android.annotation.SuppressLint;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by slack
 * on 17/3/2 下午3:04.
 */

public abstract class BaseWebViewActivity extends AppCompatActivity {

    /**
     * 假装 电脑访问 web
     */
    // phone:  Mozilla/5.0 (Linux; Android 6.0.1; MI 5 Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/51.0.2704.81 Mobile Safari/537.36
    private final String WEB_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    protected WebView mWebView;
    private ViewGroup mRootView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mRootView = (ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        /**
         * ScrollView 在webview 登录时会引起界面拉长
         */
//        if(mRootView instanceof ScrollView){
//            mRootView = (ViewGroup)mRootView.getChildAt(0);
//        }
    }

    /**
     * 加在最上层
     */
    protected void addWebView(){
        addWebView(mRootView.getChildCount());
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void addWebView(int index) {
        mRootView.addView(mWebView, index);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        // test
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("WebView","Console Message: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i("WebView","onJsAlert: " + message);
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    protected void useWebUserAgent(){
        mWebView.getSettings().setUserAgentString(WEB_USER_AGENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        clearWebView();
        super.onDestroy();
    }

    /**
     * 先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
     */
    protected void clearWebView() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mRootView.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    protected String getAssertString(String assetName) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = getAssets().open(assetName);
            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return sb.toString();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    protected void showToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseWebViewActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
