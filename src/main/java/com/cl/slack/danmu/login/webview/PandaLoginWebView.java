package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.ValueCallback;

import com.cl.slack.danmu.login.LiveLoginWebView;

/**
 * @author : xiaozhuai
 * @date : 17/3/3
 */
public class PandaLoginWebView extends LiveLoginWebView {
    public PandaLoginWebView(Context context) {
        super(context);
    }

    public PandaLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PandaLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "http://www.panda.tv/";
    }

    @Override
    protected void onLoginPageLoaded() {
        loadCss();
        this.setInitialScale((int)((float)this.getMeasuredWidth()/390f*100f));  //手机屏幕宽度(px) / 390 * 100
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "$('.header-login-btn').click()", //显示登录

                "$('.ruc-input-login-name').change(function(){ webview.onFormInputChanged('.ruc-input-login-name', $(this).val()) });",
                "$('.ruc-input-login-passport').change(function(){ webview.onFormInputChanged('.ruc-input-login-passport', $(this).val()) });",
        };
    }

    @Override
    protected String onGetName() {
        return "panda";
    }

    @Override
    protected boolean hasJq() {
        return true;
    }

    @Override
    protected void onPageLoaded(String url) {
        //https://login.pandatv.com/sso?data=
//        if(url.equals("http://www.panda.tv/")){
//            onLoginSuc("http://www.panda.tv/");
//        }
    }

    @Override
    protected boolean isMobile() {
        return false;
    }

    @Override
    protected void onObjectLoaded(String url) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isLoginLoaded) {
                        PandaLoginWebView.this.evaluateJavascript("$('.header-pic').length", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                try {
                                    if (Integer.parseInt(value) > 0) {
                                        onLoginSuc("http://www.panda.tv/");
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
