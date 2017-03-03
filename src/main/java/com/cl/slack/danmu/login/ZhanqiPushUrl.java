package com.cl.slack.danmu.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;

/**
 * Created by slack
 * on 17/3/2 下午4:21.
 */

public class ZhanqiPushUrl extends PushUrl {

    private final String mZhanqiStartUrl = "https://www.zhanqi.tv/api/anchor/live_user.startlive?source=web";

    private final String mZhanqiStopUrl = "https://www.zhanqi.tv/api/anchor/live_user.stoplive?source=web";


    @Override
    public void getRoomInfo() throws Exception {

    }

    @Override
    public void closeRoom() throws Exception {
        HttpUtils.httpGet(mZhanqiStopUrl,mCookie);
    }

    @Override
    public void getRoomPushUrl() throws Exception {
        String html = HttpUtils.httpGet(mZhanqiStartUrl,mCookie);
        JSONObject data = JSON.parseObject(html).getJSONObject("data");
        onGetUrl(data.getString("rtmpUrl"),data.getString("videoId"));
    }
}
