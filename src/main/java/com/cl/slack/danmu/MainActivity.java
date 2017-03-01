package com.cl.slack.danmu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.cl.slack.danmu.live_chart.LiveChat;
import com.cl.slack.danmu.live_chart.LiveChatCallback;
import com.cl.slack.danmu.live_chart.impl.DouyuLiveChat;
import com.cl.slack.danmu.live_chart.impl.LongzhuLiveChat;
import com.cl.slack.danmu.live_chart.impl.PandaLiveChat;
import com.cl.slack.danmu.live_chart.impl.QuanminLiveChat;
import com.cl.slack.danmu.live_chart.impl.ZhanqiLiveChat;
import com.cl.slack.danmu.live_chart.js.HuYaLiveJsChat;
import com.cl.slack.danmu.live_chart.js.LiveJsChat;
import com.cl.slack.danmu.live_chart.js.SixRoomLiveJsChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * WebView 需要放在一个布局上，没法脱离 view
 * @author slack
 * @time 17/2/28 下午5:45
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LiveChart";
    private LinearLayout mRootView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = (LinearLayout) findViewById(R.id.activity_main);
    }

    public void douyu(View view) {
        LiveChat douyu = new DouyuLiveChat();
        douyu.setLiveChatCallback(mLiveChatCallback);
        douyu.connect("https://www.douyu.com/890551");
    }

    // done
    public void xiongmao(View view) {
        LiveChat xiongmao = new PandaLiveChat();
        xiongmao.setLiveChatCallback(mLiveChatCallback);
        xiongmao.connect("http://www.panda.tv/435911");
    }

    public void longzhu(View view) {
        LiveChat longzhu = new LongzhuLiveChat();
        longzhu.setLiveChatCallback(mLiveChatCallback);
        longzhu.connect("http://star.longzhu.com/101371?from=filivehot1");
    }

    // done
    public void zhanqi(View view) {
        LiveChat zhanqi = new ZhanqiLiveChat();
        zhanqi.setLiveChatCallback(mLiveChatCallback);
        zhanqi.connect("https://www.zhanqi.tv/erxi?from=index");
    }

    public void quanmin(View view) {
        LiveChat quanmin = new QuanminLiveChat();
        quanmin.setLiveChatCallback(mLiveChatCallback);
        quanmin.connect("http://www.quanmin.tv/3538443");
    }

    public void huya(View view) {
        LiveJsChat huya = new HuYaLiveJsChat(this);
        huya.setLiveChatCallback(mLiveChatCallback);

        clearWebView();
        mWebView = new WebView(getApplicationContext());
        mRootView.addView(mWebView);
        huya.initWebView(mWebView);
        huya.connect("http://www.huya.com/aikaikai");

    }

    public void sixjianfang(View view) {
        SixRoomLiveJsChat sixRoom = new SixRoomLiveJsChat(this);
        sixRoom.setLiveChatCallback(mLiveChatCallback);

        clearWebView();
        mWebView = new WebView(getApplicationContext());
        mRootView.addView(mWebView);
        sixRoom.initWebView(mWebView);
        sixRoom.connect("http://v.6.cn/1366?");
    }

    /**
     * TODO : need run on ui thread
     */
    private LiveChatCallback mLiveChatCallback = new LiveChatCallback() {
        @Override
        public void onConnected() {
            Log.i(TAG,"onConnected...");
        }

        @Override
        public void onDisconnected() {
            Log.i(TAG,"onConnected...");
        }

        @Override
        public void onErr(String errMsg) {
            Log.e(TAG,"onErr..." + errMsg);
        }

        @Override
        public void onMsg(String nickname, String content) {
            Log.i(TAG,"onMsg...    " + nickname + " : " + content);
        }

        @Override
        public void onGift(String nickname, String giftName, String giftImg, int count, int total) {
            Log.i(TAG,"onGift...   " + nickname + "  send " + giftName +
            "  giftImg: " + giftImg + " count: " + count + " total: " + total);
        }

        @Override
        public void onViewerCount(int count) {
            Log.i(TAG,"onViewerCount...count : " + count);
        }

        @Override
        public void onUserIn(String nickname, String action) {
            Log.i(TAG,"onUserIn...    " + nickname + " : " + action);
        }
    };


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
    private void clearWebView() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mRootView.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearWebView();
        System.exit(0);
    }
}
