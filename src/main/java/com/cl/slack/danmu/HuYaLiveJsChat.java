package com.cl.slack.danmu;

import android.content.Context;
import android.util.Log;

import com.cl.slack.danmu.live_chart.HttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slack
 * on 17/2/28 下午3:35.
 */

public class HuYaLiveJsChat extends LiveJsChat {

    private final String TAG = "Huya";
    private String topId = "";
    private String subId = "";

    public HuYaLiveJsChat(Context context) {
        super(context);
    }

    @Override
    protected void connectEntry() {
        try {
            getRoomInfo();
        }catch (Exception e){
            Log.e(TAG,e.toString());
            onErr("get topid & subid error !");
            return;
        }

        connectWithJs();
    }

    private void connectWithJs() {
        if(mWebView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + getAssertJS("js/huya.js"));//注入js函数
                    mWebView.loadUrl("javascript:HuYaListener(" + topId + "," + subId + ")");//调用js函数
                }
            });
        }

    }

    /**
     * \s  包括空格、制表符、换页符等空白字符的其中任意一个
     * *   表达式不出现或出现任意次
     * \d  任意一个数字，0~9 中的任意一个
     * +   表达式至少出现1次
     * ( )  在被修饰匹配次数的时候，括号中的表达式可以作为整体被修饰
     *      取匹配结果的时候，括号中的表达式匹配到的内容可以被单独得到
     * .   小数点可以匹配除了换行符（\n）以外的任意一个字符
     * ?   匹配表达式0次或者1次
     */
    private void getRoomInfo() throws Exception {
        String html = HttpUtils.httpGet(mUrl);
        Pattern pattern = Pattern.compile("\"chTopId\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        topId = matcher.group(1);

        pattern = Pattern.compile("\"subChId\"\\s*:\\s*\"(.*?)\"");
        matcher = pattern.matcher(html);
        matcher.find();
        subId = matcher.group(1);
    }
}

