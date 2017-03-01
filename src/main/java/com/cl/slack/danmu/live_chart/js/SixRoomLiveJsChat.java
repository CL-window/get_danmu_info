package com.cl.slack.danmu.live_chart.js;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;
import com.cl.slack.danmu.live_chart.StringUtils;

/**
 * Created by slack
 * on 17/2/28 下午6:54.
 */

public class SixRoomLiveJsChat extends LiveJsChat {

    private final String TAG = "6roon";
    private final String mGetRoomIdUrl = "http://v.6.cn/coop/mobile/index.php?rid=%s&padapi=coop-mobile-inroom.php";
    private final String mGetAddressUrl = "http://v.6.cn/coop/mobile/index.php?type=chat&ruid=%s&padapi=coop-mobile-chatConf.php";
    private String roomId ;
    private String address ;

    public SixRoomLiveJsChat(Context context) {
        super(context);
    }

    @Override
    protected void connectEntry() {
        try {
            getRoomInfo();
        }catch (Exception e){
            Log.e(TAG,e.toString());
            onErr("get roomid & address error !");
            return;
        }

        connectWithJs();
    }

    private void connectWithJs() {
        if(mWebView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + getAssertJS("js/6jianfang.js"));
                    mWebView.loadUrl("javascript:sixRoomListener(" + roomId + "," + address + ")");
                }
            });
        }

    }

    private void getRoomInfo() throws Exception {
        String roomNum = StringUtils.substringAfterLast(mUrl,"/");
        if(TextUtils.isEmpty(roomNum)){
            onErr("get room num error ,check 6 room url");
            return;
        }
        String html = HttpUtils.httpGet(String.format(mGetRoomIdUrl,roomNum));
        JSONObject json = JSON.parseObject(html);
        roomId = json.getJSONObject("content").getJSONObject("roominfo").getString("id");
        html = HttpUtils.httpGet(String.format(mGetAddressUrl,roomId));
        json = JSON.parseObject(html);
        address = json.getJSONObject("content").getJSONArray("websock").getString(0);

    }
}
