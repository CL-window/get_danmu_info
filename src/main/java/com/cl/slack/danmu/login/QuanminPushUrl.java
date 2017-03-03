package com.cl.slack.danmu.login;

import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;

/**
 * Created by slack
 * on 17/3/2 下午5:00.
 */

public class QuanminPushUrl extends PushUrl {

    final String mQuanminStartUrl = "http://www.quanmin.tv/api/v1?p=5";

    @Override
    public void getRoomInfo() throws Exception{

    }

    @Override
    public void closeRoom() throws Exception{

    }

    @Override
    public void getRoomPushUrl() throws Exception{
        String html = HttpUtils.httpGet(mQuanminStartUrl,"{\"m\": \"room.viewSecretKey\"}",mCookie);
        JSONObject data = JSONObject.parseObject(html).getJSONObject("data");
        onGetUrl(data.getString("server"),data.getString("secretKey"));
    }

}
