package com.cl.slack.danmu;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.cl.slack.danmu.live_chart.LiveChatCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by slack
 * on 17/2/28 下午3:34.
 */

public abstract class LiveJsChat extends BaseChart{

    private WeakReference<Context> mContext;
    protected String mUrl;
    protected WebView mWebView;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    public LiveJsChat(Context context) {
        mContext = new WeakReference<Context>(context);

    }

    protected void initWebView(WebView webView){
        mWebView = webView;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebInterface(),"LiveJsChat");
    }

    public void connect(String url){
        mUrl = url;
        connectEntry();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                connectEntry();
//            }
//        }).start();
    }

    protected abstract void connectEntry();

    protected Context getContext(){
        if(mContext == null){
            return null;
        }
        return mContext.get();
    }

    protected String getAssertJS(String assetName) {
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;
        try {
            Context c = getContext();
            if(c == null){
                return sb.toString();
            }
            is = c.getAssets().open(assetName);
            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
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

    class WebInterface{

        /**
         * 登录 的 反馈 信息
         */
        @JavascriptInterface
        public void onLogin(String str) {

        }

        /**
         * userIn
         */
        @JavascriptInterface
        public void onLogin() {
            onConnected();
        }

        /**
         * 1400 : 聊天信息
         * @param nick  nickname
         * @param msg message
         */
        @JavascriptInterface
        public void onGetChart(String nick,String msg) {
            onMsg(nick,msg);
        }

        /**
         * 6501 : tanmu
         * 弹幕 信息暂时获取不到详情
         * nick: n.sSenderNick,
         * propName: c.tanmu.propsInfo[t].propName,
         * icon: c.tanmu.propsInfo[t].propIcon,
         * count: n.iItemCount
         */
        @JavascriptInterface
        public void onGetGifts(String nick,String count) {
            onGift(nick,"","",Integer.parseInt(count),1);
        }

        /**
         * 在线人数 liveCount
         * @param count
         */
        @JavascriptInterface
        public void onLiveCount(String count) {
            onViewerCount(Integer.parseInt(count));
        }

        /**
         * 用户 in
         */
        @JavascriptInterface
        public void onUserIn(String nick,String action) {
            onNewUserIn(nick,action);
        }
    }


}
