package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.cl.slack.danmu.login.LiveLoginWebView;

/**
 * @author : xiaozhuai
 * @date : 17/3/3
 */
public class ZhanqiLoginWebView extends LiveLoginWebView {
    public ZhanqiLoginWebView(Context context) {
        super(context);
    }

    public ZhanqiLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhanqiLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "https://www.zhanqi.tv/";
    }

    @Override
    protected void onLoginPageLoaded() {
        loadCss();
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "$('#js-btnLogin').click();",

                "$('input[name=account]').change(function(){ webview.onFormInputChanged('input[name=account]', $(this).val()) });",
                "$('input[name=password]').change(function(){ webview.onFormInputChanged('input[name=password]', $(this).val()) });",
        };
    }

    @Override
    protected String onGetName() {
        return "zhanqi";
    }

    @Override
    protected boolean hasJq() {
        return true;
    }

    @Override
    protected void onPageLoaded(String url) {

    }

    @Override
    protected boolean isMobile() {
        return false;
    }

    @Override
    protected void onObjectLoaded(String url) {
        //https://www.zhanqi.tv/api/public/validate.nickname
        if(url.contains("validate.nickname")){
            onLoginSuc("https://www.zhanqi.tv/");
        }
    }
}
