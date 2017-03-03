package com.cl.slack.danmu.login;

import com.cl.slack.danmu.live_chart.HttpUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slack
 * on 17/3/3 上午11:09.
 */

public class DouyuPushUrl extends PushUrl{

    final String mDouyuStopUrl = "https://www.douyu.com/room/my/close_show";
    final String mDouyuStartUrl = "https://www.douyu.com/room/my/first_show";
    final String mDouyuCTNUrl = "https://www.douyu.com/room/my";
    HashMap<String,String> mDouyuParam = new HashMap<>();

    String mCtn;

    @Override
    public void getRoomInfo() throws Exception {
        String html = HttpUtils.httpGet(mDouyuCTNUrl,mCookie);
        Pattern pattern = Pattern.compile("verify\\s*:\\s*['|\"](.*?)['|\"]");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        mCtn = matcher.group(1);
        mDouyuParam.put("ctn",mCtn);

    }

    @Override
    public void closeRoom() throws Exception {
        HttpUtils.httpPostFrom(mDouyuStopUrl,mDouyuParam,mCookie);
    }

    @Override
    public void getRoomPushUrl() throws Exception {
        HttpUtils.httpPostFrom(mDouyuStartUrl,mDouyuParam,mCookie);
        String html = HttpUtils.httpGet(mDouyuCTNUrl,mCookie);
        Document doc = Jsoup.parse(html);
        String url = doc.getElementById("rtmp_url").attr("value");
        String val = doc.getElementById("rtmp_val").attr("value");
        onGetUrl(url,val);
    }
}
