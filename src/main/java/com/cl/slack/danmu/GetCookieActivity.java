package com.cl.slack.danmu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cl.slack.danmu.login.LiveLoginWebView;
import com.cl.slack.danmu.login.webview.DouyuLoginWebView;
import com.cl.slack.danmu.pushurl.DouyuPushUrl;
import com.cl.slack.danmu.pushurl.HuyaPushUrl;
import com.cl.slack.danmu.pushurl.LongzhuPushUrl;
import com.cl.slack.danmu.pushurl.PandaPushUrl;
import com.cl.slack.danmu.pushurl.PushUrl;
import com.cl.slack.danmu.pushurl.QuanminPushUrl;
import com.cl.slack.danmu.pushurl.ZhanqiPushUrl;

public class GetCookieActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GetCookieActivity";
    private boolean mStopPush = false;
    private PushUrl mPushUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cookie);

        findViewById(R.id.douyu_btn).setOnClickListener(this);
        findViewById(R.id.huya_btn).setOnClickListener(this);
        findViewById(R.id.longzhu_btn).setOnClickListener(this);
        findViewById(R.id.quanmin_btn).setOnClickListener(this);
        findViewById(R.id.sixroom_btn).setOnClickListener(this);
        findViewById(R.id.xiongmao_btn).setOnClickListener(this);
        findViewById(R.id.zhanqi_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GetCookieLoginActivity.class);
        intent.putExtra("platform", v.getId());
        this.startActivityForResult(intent, 0x100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x100 && resultCode == 0x101 && data != null) {
            int id = data.getIntExtra("platform", 0);
            String cookie = data.getStringExtra("cookie");
            if(TextUtils.isEmpty(cookie)){
                return;
            }
            getPushUrl(id,cookie);
        }
    }

    private void getPushUrl(int id, String cookie) {
        switch (id) {
            case R.id.douyu_btn:
                getDouyuPushUrl(cookie);
                break;
            case R.id.huya_btn:
                getHuyaPushUrl(cookie);
                break;
            case R.id.longzhu_btn:
                getLongzhuPushUrl(cookie);
                break;
            case R.id.quanmin_btn:
                getQuanminPushUrl(cookie);
                break;
            case R.id.sixroom_btn:
                getSixRoomPushUrl(cookie);
                break;
            case R.id.xiongmao_btn:
                getXiongmaoPushUrl(cookie);
                break;
            case R.id.zhanqi_btn:
                getZhanqiPushUrl(cookie);
                break;
        }
    }

    private void getDouyuPushUrl(String cookie) {
        if (mStopPush) {
            mStopPush = false;
            mPushUrl.stopLivePush();
        } else {
            mStopPush = true;
            mPushUrl = new DouyuPushUrl();
            mPushUrl.addCookie(cookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    private void getLongzhuPushUrl(String cookie) {
        if (mStopPush) {
            mStopPush = false;
            mPushUrl.stopLivePush();
        } else {
            mStopPush = true;
            mPushUrl = new LongzhuPushUrl();

            if (!TextUtils.isEmpty(cookie)) {
                mPushUrl.addCookie(cookie);
            }

            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.setParam("balabala", "127");
            mPushUrl.startLivePush();
        }
    }

    private void getXiongmaoPushUrl(String cookie){
        if (mStopPush) {
            mStopPush = false;
            mPushUrl.stopLivePush();
        } else {
            mStopPush = true;
            mPushUrl = new PandaPushUrl();
            mPushUrl.addCookie(cookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    private void getQuanminPushUrl(String cookie) {
        mPushUrl = new QuanminPushUrl();
        mPushUrl.addCookie(cookie);
        mPushUrl.setPushUrlCallback(mPushUrlCallback);
        mPushUrl.startLivePush();
    }

    private void getZhanqiPushUrl(String cookie) {
        if (mStopPush) {
            mStopPush = false;
            mPushUrl.stopLivePush();
        } else {
            mStopPush = true;
            mPushUrl = new ZhanqiPushUrl();
            mPushUrl.addCookie(cookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    private void getHuyaPushUrl(String cookie) {
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new HuyaPushUrl();
            mPushUrl.addCookie(cookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    private void getSixRoomPushUrl(String cookie) {
        //
    }

    private PushUrl.PushUrlCallback mPushUrlCallback = new PushUrl.PushUrlCallback() {
        @Override
        public void onGetUrl(String url) {
            Log.i(TAG, "onGetUrl : " + url);
        }

        @Override
        public void onError(String msg) {
            Log.e(TAG, "onError : " + msg);
        }

        @Override
        public void onClose() {
            Log.i(TAG, "onClose ...");
        }
    };
}
