package com.cl.slack.danmu.login.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.ValueCallback;

import com.cl.slack.danmu.login.LiveLoginWebView;

/**
 * @author : xiaozhuai
 * @date : 17/3/3
 */
public class HuyaLoginWebView extends LiveLoginWebView {
    public HuyaLoginWebView(Context context) {
        super(context);
    }

    public HuyaLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HuyaLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String onGetUrl() {
        return "http://www.huya.com/";
    }

    @Override
    protected void onLoginPageLoaded() {
        loadCss();
        this.setInitialScale((int)((float)this.getMeasuredWidth()/300f*100f));
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{
                "$('#nav-login').click();",

                "$($('#udbsdk_frm_normal').context).find('input.E_acct').change(function(){ console.log('fuck'); webview.onFormInputChanged('input.E_acct', $(this).val()) })",
                "$($('#udbsdk_frm_normal').context).find('input.E_passwd').change(function(){ console.log('fuck'); webview.onFormInputChanged('input.E_passwd', $(this).val()) })"

        };
    }

    @Override
    protected String onGetName() {
        return "huya";
    }

    @Override
    protected boolean hasJq() {
        return true;
    }

    @Override
    protected void onPageLoaded(String url) {
//        if(url.equals("http://www.huya.com/")){
//            onLoginSuc(url);
//        }
    }

    @Override
    protected boolean isMobile() {
        return false;
    }

    @Override
    protected void onObjectLoaded(String url) {
//        if(isLoginLoaded){
//            if(url.equals("http://www.huya.com/")){
//                onLoginSuc(url);
//            }
//        }

        //#login-userAvatar  头像
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    if(isLoginLoaded) {
                        HuyaLoginWebView.this.evaluateJavascript("$('#login-username').text()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                if(!value.equals("\"\"") && !value.equals("null")){
                                    onLoginSuc("http://www.huya.com/");
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

    protected void setFormInputValue(String input, String value){
        this.loadJs(String.format("$($('#udbsdk_frm_normal').context).find('%s').val('%s');", input, value));
        this.loadJs(String.format("$($('#udbsdk_frm_normal').context).find('%s').eq(0).change();", input));
        this.loadJs(String.format("$($('#udbsdk_frm_normal').context).find('%s').eq(0).focus();", input));
        this.loadJs(String.format("$($('#udbsdk_frm_normal').context).find('%s').eq(0).blur();", input));
    }
}
