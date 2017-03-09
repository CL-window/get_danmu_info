package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.cl.slack.danmu.login.LiveLoginWebView;


/**
 * @author : xiaozhuai
 * @date : 17/3/3
 */
public class SixroomLoginWebView extends LiveLoginWebView {
    public SixroomLoginWebView(Context context) {
        super(context);
    }

    public SixroomLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SixroomLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "https://www.6.cn/";
    }

    @Override
    protected void onLoginPageLoaded() {
        this.setInitialScale((int)((float)this.getMeasuredWidth()/340f*100f));
        loadCss();
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "Login.toLogin();",
                "$('input.username').change(function(){ webview.onFormInputChanged('input.username', $(this).val()) });",
                "$('input.password').change(function(){ webview.onFormInputChanged('input.password', $(this).val()) });",
        };
    }

    @Override
    protected String onGetName() {
        return "sixroom";
    }

    @Override
    protected boolean hasJq() {
        return false;
    }

    @Override
    protected void onPageLoaded(String url) {
        if(url.equals("https://www.6.cn/")){
            onLoginSuc(url);
        }
    }

    @Override
    protected boolean isMobile() {
        return false;
    }

    @Override
    protected void onObjectLoaded(String url) {
//        //#login-userAvatar  头像
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    if(isLoginLoaded) {
//                        SixroomLoginWebView.this.evaluateJavascript("$('.user-name').html()", new ValueCallback<String>() {
//                            @Override
//                            public void onReceiveValue(String value) {
//                                Log.e("zzzzzzz", "v: "+value);
//                                if(!value.equals("undefined") && !value.equals("null")){
//                                    onLoginSuc("https://www.6.cn/");
//                                }
//                            }
//                        });
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
