package com.cl.slack.danmu.pushurl;

import com.alibaba.fastjson.JSON;
import com.cl.slack.danmu.live_chart.HttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slack
 * on 17/3/2 下午5:24.
 * Jsoup 直接解析 html
 */

public class LongzhuPushUrl extends PushUrl {

    final String mLongzhuRoomUrl = "http://recruit.longzhu.com/recruit/my";
    final String mLongzhuStartUrl = "http://setting.longzhu.com/%s/Play/Live";
    private String mRoomId;
    private HashMap<String,String> mLongZhuParam = new HashMap<String,String>(){
        {
            put("liveStreamType","12");
            put("lines","12");
        }
    };

    @Override
    public void getRoomInfo() throws Exception{
        String html = mCookie==null ?
                HttpUtils.httpGet(mLongzhuRoomUrl) : HttpUtils.httpGet(mLongzhuRoomUrl,mCookie);
        Pattern pattern = Pattern.compile("location\\s*?=\\s*?['|\"].*?://star.longzhu.com/(.*)['|\"]");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        mRoomId = matcher.group(1);
    }

    @Override
    public void closeRoom() throws Exception{
        getLongZhuHtml();
    }

    @Override
    public void getRoomPushUrl() throws Exception{
        String html = getLongZhuHtml();
        Document doc = Jsoup.parse(html);
        String host = doc.getElementById("up_stream_host").attr("value");
        String key = doc.getElementById("up_stream_key").attr("value");
        onGetUrl(host,key);
    }

    private String getLongZhuHtml() throws Exception{
        return HttpUtils.httpPostJson(
                String.format(mLongzhuStartUrl,mRoomId),
                JSON.toJSONString(mLongZhuParam),
                mCookie
        );
    }

    /**
     * TODO 龙珠直播的话 开启之前要填标题和选择分类
     * @param params 房间标题 , 分类
     */
    @Override
    public void setParam(String... params) {
        try{
            mLongZhuParam.put("BoardCastTitle",params[0]);
            mLongZhuParam.put("gameId",params[1]);
        }catch (Exception e){
            onErr("设置龙珠房间标题和分类出错");
        }
    }
}
