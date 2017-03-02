package com.cl.slack.danmu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

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

/**
 * WebView 需要放在一个布局上，没法脱离 view
 * @author slack
 * @time 17/2/28 下午5:45
 */
public class MainActivity extends BaseWebViewActivity {

    private static final String TAG = "LiveChart";

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testText();
    }

    private void testText() {
        mTextView = (TextView) findViewById(R.id.textShow);
//        mTextView.setText(Html.fromHtml("<a href='' class='u' user='28703263,39140558,大塘学校,,2'>“大塘学校”</a>进入房间</a>"));


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
        clearWebView();
        mWebView = new WebView(getApplicationContext());
        addWebView();
        LiveJsChat huya = new HuYaLiveJsChat(this);
        huya.setLiveChatCallback(mLiveChatCallback);
        huya.initWebView(mWebView);
        huya.connect("http://www.huya.com/1848069776");

    }

    public void sixjianfang(View view) {
        clearWebView();
        mWebView = new WebView(getApplicationContext());
        addWebView();
        SixRoomLiveJsChat sixRoom = new SixRoomLiveJsChat(this);
        sixRoom.setLiveChatCallback(mLiveChatCallback);
        sixRoom.initWebView(mWebView);
        sixRoom.connect("http://v.6.cn/551222");
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
            Log.i(TAG,"onMsg...    " + nickname + " : " + content + ".");
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
    public void onBackPressed() {
        super.onBackPressed();
        clearWebView();
        System.exit(0);
    }

    public void loginActivity(View view) {
        startActivity(new Intent(this,TestLoginActivity.class));
    }
}
