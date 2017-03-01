package com.cl.slack.danmu.live_chart.js;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;
import com.cl.slack.danmu.live_chart.StringUtils;

import java.net.URL;

/**
 * Created by slack
 * on 17/2/28 下午6:54.
 * TODO : 没有在线人数
 * TODO : 礼物  一种中只有id , 一种是固定img  礼物种类特别多呀
 */

public class SixRoomLiveJsChat extends LiveJsChat {

    private final String TAG = "6room";
    private final String mGetRoomIdUrl = "http://v.6.cn/coop/mobile/index.php?rid=%s&padapi=coop-mobile-inroom.php";
    private final String mGetAddressUrl = "http://v.6.cn/coop/mobile/index.php?type=chat&ruid=%s&padapi=coop-mobile-chatConf.php";
    private String roomId = "62346760";
    /**
     * 有点尴尬 42.62.28.177:4000 这样的无法作为参数传送到 js 被默认为数字
     * 使用 '42.62.28.177:4000'  表示这个参数 String
     */
    private String address = "42.62.28.177:4000";

    public SixRoomLiveJsChat(Context context) {
        super(context);
    }

    @Override
    protected void connectEntry() {
        try {
            getRoomInfo();
        }catch (Exception e){
            Log.e(TAG,e.toString());
            onErr("连接弹幕服务器失败，解析RoomId错误!");
            return;
        }
        connectWithJs();
    }

    private void connectWithJs() {
        if(mWebView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + getAssertJS("js/sixroom.js"));
                    mWebView.loadUrl("javascript:sixRoomListener(\'" + roomId + "\',\'" + address + "\')");
                }
            });
        }

    }

    private void getRoomInfo() throws Exception {
        URL url = new URL(mUrl);
        String[] tmp = url.getFile().split("/");
        String roomNum = tmp[tmp.length-1];
        String urlTemp = String.format(mGetRoomIdUrl,roomNum);
        String html = HttpUtils.httpGet(urlTemp);
        Log.i(TAG,"html: " + urlTemp + "\n" + html);
        JSONObject json = JSON.parseObject(html);
        roomId = json.getJSONObject("content").getJSONObject("roominfo").getString("id");
        html = HttpUtils.httpGet(String.format(mGetAddressUrl,roomId));
        json = JSON.parseObject(html);
        address = json.getJSONObject("content").getJSONArray("websock").getString(0);

    }
}
