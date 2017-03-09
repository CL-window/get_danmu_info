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
public class DouyuLoginWebView extends LiveLoginWebView {
    public DouyuLoginWebView(Context context) {
        super(context);
    }

    public DouyuLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DouyuLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected String onGetUrl() {
        return "https://passport.douyu.com/iframe/index";
    }


    @Override
    protected void onLoginPageLoaded() {
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        mWebSettings.setUseWideViewPort(true);
        loadCss();
        this.setInitialScale(1);
    }

    @Override
    protected String[] onInjectJs() {
        return new String[]{

                /**
                 * form输入值改变回调
                */

                "$('input[name=username]').change(function(){ webview.onFormInputChanged('input[name=username]', $(this).val()) });",
                "$('input[name=password]').change(function(){ webview.onFormInputChanged('input[name=password]', $(this).val()) });",
                "$('input[name=phoneNum]').change(function(){ webview.onFormInputChanged('input[name=phoneNum]', $(this).val()) });"

        };
    }

    @Override
    protected String onGetName() {
        return "douyu";
    }

    @Override
    protected boolean hasJq() {
        return true;
    }

    @Override
    protected void onPageLoaded(String url) {
        // https://www.douyu.com/api/passport/login?code=4bb83fc4226da431087cf4b104f1d9eb&uid=108429254&client_id=1
        try {
            Pattern pattern = Pattern.compile("login.*&uid=(.*?)[&|$]");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                if(!matcher.group(1).equals("")){
                    onLoginSuc(url);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isMobile() {
        return false;
    }

    @Override
    protected void onObjectLoaded(String url) {

    }


}
