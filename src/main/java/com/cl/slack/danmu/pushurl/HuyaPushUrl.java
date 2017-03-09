package com.cl.slack.danmu.pushurl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;

/**
 * Created by slack
 * on 17/3/2 下午2:22.
 */

public class HuyaPushUrl extends PushUrl {

    final String mHuyaRoomInfoUrl = "http://i.huya.com/index.php?m=ProfileSetting&do=ajaxGetGameInfo";
    final String mHuyaStartUrl = "http://i.huya.com/index.php?m=ProfileSetting&do=ajaxGetOpenRtmpAddr&" +
            "game_id=%s&live_desc=%s&game_name=%s&live_flag=%s&read_only=0";
    final String mHuyaStopUrl = "http://i.huya.com/index.php?m=ProfileSetting&do=ajaxOpenRtmpEndLive";

    String gameId;
    String liveDesc;
    String gameName;
    String liveFlag;

    @Override
    public void getRoomInfo() throws Exception{
        String html = HttpUtils.httpGet(mHuyaRoomInfoUrl,mCookie);
        JSONObject info = JSON.parseObject(html).getJSONObject("data").getJSONObject("rtmpInfo");
        gameId = info.getString("game_id");
        gameName = info.getString("game_name");
        liveDesc = info.getString("live_desc");
        liveFlag = info.getString("live_flag");
    }

    @Override
    public void closeRoom() throws Exception{
        HttpUtils.httpGet(mHuyaStopUrl,mCookie);
    }

    @Override
    public void getRoomPushUrl() throws Exception{
        String html = HttpUtils.httpGet(
                String.format(mHuyaStartUrl,gameId,liveDesc,gameName,liveFlag),
                mCookie);
        JSONObject data = JSON.parseObject(html).getJSONObject("data");
        onGetUrl(data.getString("rtmpAddr"),data.getString("rtmpKey"));
    }

}
