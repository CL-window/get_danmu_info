package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.cl.slack.danmu.login.LiveLoginWebView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : xiaozhuai
 * @date : 17/3/2
 */
public class QuanminLoginWebView extends LiveLoginWebView {
    public QuanminLoginWebView(Context context) {
        super(context);
    }

    public QuanminLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuanminLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "http://m.quanmin.tv/login?returnurl=/";
    }

    @Override
    protected void onLoginPageLoaded() {

    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "$('.p-login_register').remove()",  //去除注册

                "$('.p-login_mobile>input').change(function(){ webview.onFormInputChanged('.p-login_mobile>input', $(this).val()) });",
                "$('.p-login_password>input').change(function(){ webview.onFormInputChanged('.p-login_password>input', $(this).val()) });",
        };
    }

    @Override
    protected String onGetName() {
        return "quanmin";
    }

    @Override
    protected boolean hasJq() {
        return false;
    }

    @Override
    protected void onPageLoaded(String url) {

    }

    @Override
    protected boolean isMobile() {
        return true;
    }

    @Override
    protected void onObjectLoaded(String url) {
        try {
            Pattern pattern = Pattern.compile("(.*?)/avatar/(.*)");
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()){
                onLoginSuc(url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
