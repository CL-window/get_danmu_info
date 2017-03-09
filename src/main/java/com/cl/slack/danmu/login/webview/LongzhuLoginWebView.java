package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.cl.slack.danmu.login.LiveLoginWebView;

/**
 * @author : xiaozhuai
 * @date : 17/3/2
 */
public class LongzhuLoginWebView extends LiveLoginWebView {
    public LongzhuLoginWebView(Context context) {
        super(context);
    }

    public LongzhuLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongzhuLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "http://login.plu.cn/portable/login";
    }

    @Override
    protected void onLoginPageLoaded() {
        loadCss();
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "$('input[name=name]').change(function(){ webview.onFormInputChanged('input[name=name]', $(this).val()) });",
                "$('input[name=password]').change(function(){ webview.onFormInputChanged('input[name=password]', $(this).val()) });",
        };
    }

    @Override
    protected String onGetName() {
        return "longzhu";
    }

    @Override
    protected boolean hasJq() {
        return false;
    }

    @Override
    protected void onPageLoaded(String url) {
        if(url.contains("m.longzhu.com/index")){
            onLoginSuc(url);
        }
    }

    @Override
    protected boolean isMobile() {
        return true;
    }

    @Override
    protected void onObjectLoaded(String url) {
//        try {
//            Pattern pattern = Pattern.compile("(.*?)/sso/synclogin(.*)");
//            Matcher matcher = pattern.matcher(url);
//            if(matcher.find()){
//                onLoginSuc(url);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
