package com.cl.slack.danmu.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slack
 * on 17/3/3 上午10:30.
 */

public class PandaPushUrl extends PushUrl{

    final String mPandaTokenUrl = "http://www.panda.tv/setting";
    final String mPandaStartUrl = "http://www.panda.tv/live_status_set";
    String mToekn;

    HashMap<String,String> mPandaParam = new HashMap<>();

    @Override
    public void getRoomInfo() throws Exception {
        String html = HttpUtils.httpGet(mPandaTokenUrl,mCookie);
        Pattern pattern = Pattern.compile("TOKEN\\s*?=\\s*?['|\"](.*)['|\"]");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        mToekn = matcher.group(1);
    }

    @Override
    public void closeRoom() throws Exception {
        HttpUtils.httpPostJson(mPandaStartUrl,
                String.format("{\"status\": \"3\",\"token\": \"%s\"}",mToekn),
                mCookie);
    }

    @Override
    public void getRoomPushUrl() throws Exception {
        mPandaParam.put("status","2");
        mPandaParam.put("token",mToekn);
        String html = HttpUtils.httpPostJson(mPandaStartUrl,
                JSON.toJSONString(mPandaParam),
                mCookie);
        JSONObject data = JSON.parseObject(html).getJSONObject("data");
        onGetUrl(data.getString("rtmp"),data.getString("room_key"));
    }
}
