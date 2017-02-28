package com.cl.slack.danmu.live_chart.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;
import com.cl.slack.danmu.live_chart.LiveChat;
import com.cl.slack.danmu.live_chart.NumUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaozhuai on 17/2/20.
 * TODO : 礼物 img 拿不到
 * TODO ：在线人数拿不到
 * 暂时 放弃
 */
public class LongzhuLiveChat extends LiveChat {


    private final String TAG = "Longzhu";
    private boolean loop;


    private void getRoomId() throws Exception {
        String html = HttpUtils.httpGet(room);
        Pattern pattern = Pattern.compile("\"RoomId\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        roomId = matcher.group(1);
    }

    public void mainLoop(){
        onConnected();
        loop = true;

        long from = 0;
        int span = 0;

        while (loop){
            try {
                String url = String.format("http://mb.tga.plu.cn/chatroom/msgsv2/%s/%d/%d", roomId, from, span);
                JSONObject json = JSON.parseObject(HttpUtils.httpGet(url));
                from = json.getLong("from");
                span = json.getIntValue("next");
                int count = json.getIntValue("count");
                JSONArray msgs = json.getJSONArray("msgs");
                for(int i=0; i<count; i++){
                    String msgItem = msgs.getString(i);
                    onRaw(msgItem);
                }
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        onDisconnected();
    }


    @Override
    public void disconnect() {
        loop = false;
    }

    @Override
    protected void connectEntry() {

        try {
            getRoomId();
        }catch(Exception e){
            onErr("连接弹幕服务器失败，获取RoomId错误！");
            return;
        }

        mainLoop();
    }

    @Override
    protected byte[] getHeartBeatPacket() {
        return null;
    }

    @Override
    protected boolean isSocket() {
        return false;
    }

    /**
     * 用户聊天
     */
    private final String TypeChart = "chat";

    /**
     *  礼物
     */
    private final String TypeGift = "gift";


    @Override
    protected void onRaw(String raw) {
        super.onRaw(raw);
        Log.i(TAG,"raw..." + raw);
        try {
            JSONObject message = (JSONObject) JSON.parse(raw);

            String type = message.getString("type");
            JSONObject msg = message.getJSONObject("msg");
            if(TypeChart.endsWith(type)){
                onMsg(msg.getJSONObject("user").getString("username"),msg.getString("content"));
            } else
            if(TypeGift.endsWith(type)){

                onGift(msg.getJSONObject("user").getString("username"),
                        msg.getString("itemType"),
                        "no img url",
                        msg.getIntValue("number"),
                        1);
            }

        }catch(Exception e) {
            Log.e(TAG,"raw..." + e.toString());
        }
    }
}
