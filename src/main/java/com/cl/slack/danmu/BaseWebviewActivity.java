package com.cl.slack.danmu;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by slack
 * on 17/3/2 下午3:04.
 */

public class BaseWebViewActivity extends AppCompatActivity {

    protected WebView mWebView;
    private ViewGroup mRootView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mRootView = (ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
    }

    protected void addWebView(){
        mRootView.addView(mWebView);
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
}
