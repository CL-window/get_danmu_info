package com.cl.slack.danmu.live_chart.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;
import com.cl.slack.danmu.live_chart.LiveChat;
import com.cl.slack.danmu.live_chart.NumUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaozhuai on 17/2/20.
 * TODO : 全民 在线人数拿不到
 * TODO : 聊天消息 表情使用 getString 出不来
 * TODO : 测试时只测出来三种礼物，还需要再测试，礼物都是固定的url
 */
public class QuanminLiveChat extends LiveChat {

    private final String TAG = "Quanmin";
    private String host;
    private int port;

    private boolean loop;


    private void getRoomId() throws Exception {
        URL url = new URL(room);
        String[] tmp = url.getFile().split("/");
        String roomName = tmp[tmp.length-1];

        JSONObject json = JSON.parseObject(HttpUtils.httpGet(String.format("http://www.quanmin.tv/json/rooms/%s/noinfo.json", roomName)));
        roomId = json.getString("uid");
    }

    private void getHostAndPort() throws Exception {
        byte[] data = HttpUtils.httpGetBytes("http://www.quanmin.tv/site/route?time="+(System.currentTimeMillis()/1000));

        int[] raw = new int[4];

        for(int i=0; i<4; i++){
            raw[i] = NumUtils.byteArrayToUint32(data, i*4);
            raw[i] = raw[i] ^ 172;
        }



        host = raw[0] + "." + raw[1] + "." + raw[2] + "." + raw[3];
        port = NumUtils.byteArrayToUint32(data, 16);
        port = (int)((long)port ^ 172);
    }

    private void mainLoop(){

        onConnected();

        loop = true;

        while (loop && mSocketClient.isConnected()){
            // 00 00 00 00 消息长度
            // 后面是消息内容

            try {

                mSocketClient.skipBytes(2);
                int len = mSocketClient.readUint16();
                byte[] body = new byte[len];
                mSocketClient.forceRead(body, 0, len);
                String msg = new String(body, Charset.forName("UTF-8"));

                onRaw(msg);

//                byte[] buffer = new byte[1024*100];
//                int count = mSocketClient.read(buffer);
//                onRaw(new String(buffer, 0, count, Charset.forName("UTF-8")));
            }catch (IOException e){
                onErr("弹幕服务器连接已断开！");
                break;
            }
        }

        onDisconnected();

        mSocketClient.disconnected();

    }

    @Override
    public void disconnect() {
        loop = false;
    }

    @Override
    protected void connectEntry() {

        try {
            getRoomId();
        } catch (Exception e) {
            onErr("连接弹幕服务器失败，解析RoomId错误!");
            return;
        }

        try {
            getHostAndPort();
        }catch (Exception e){
            onErr("连接弹幕服务器失败，获取弹幕服务器错误！");
            return;
        }

        mSocketClient.setAddr(host, port);
        if(!mSocketClient.connect()){
            onErr("连接弹幕服务器失败，未能建立socket连接！");
            return;
        }

        byte[] loginBody = String.format("{\n\"os\":135,\n\"pid\":10003,\n\"rid\":\"%s\",\n\"timestamp\":78,\n\"ver\":147\n}", roomId).getBytes();

        try {
            mSocketClient.writeUint32(loginBody.length);
            mSocketClient.write(loginBody);
        }catch (IOException e){
            onErr("连接弹幕服务器失败，发送登录包错误！");
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
        return true;
    }


    /**
     * 礼物 100种子
     */
    private final String GiftZhongziUrl = "http://i.img.shouyintv.cn/FnxgOewRTwWC9NnpLOP3wdQddCQv?png-22-22";

    /**
     * 礼物 我爱你
     */
    private final String GiftWoainiUrl = "http://udata-10009275.image.myqcloud.com//1469103466/7e4ee.png";

    /**
     * 礼物 去污丸
     */
    private final String GiftQuwuwanUrl = "http://udata-10009275.image.myqcloud.com//1469104090/9dd52.png";

    @Override
    protected void onRaw(String raw) {
        super.onRaw(raw);
        Log.i(TAG,"raw..." + raw);
        try {

            JSONObject chat = ((JSONObject) JSON.parse(raw)).getJSONObject("chat");
            // 此处的判断 change null
            JSONObject json = (JSONObject) JSON.parse(chat.getString("json"));
            int type = json.getIntValue("type");
            switch (type) {
                case -1:
                    // 新用户 进入
                    onNewUserIn(json.getJSONObject("user").getString("nick"),"");
                    break;
                case 0:
                    // 聊天
                    onMsg(json.getJSONObject("user").getString("nick"),
                            json.getString("text"));
                    break;
                case 1:
                    // 礼物 100种子
                    onGift(json.getJSONObject("user").getString("nick"),
                            json.getString("text"),
                            GiftZhongziUrl,
                            json.getIntValue("number"),
                            json.getIntValue("count"));
                    break;
                case 2:
                    // 礼物 我爱你
                    onGift(json.getJSONObject("user").getString("nick"),
                            json.getString("text"),
                            GiftWoainiUrl,
                            json.getIntValue("number"),
                            json.getIntValue("count"));
                    break;
                case 3:
                    // 礼物 去污丸
                    onGift(json.getJSONObject("user").getString("nick"),
                            json.getString("text"),
                            GiftQuwuwanUrl,
                            json.getIntValue("number"),
                            json.getIntValue("count"));
                    break;
                default:break;
            }

        }catch(Exception e) {
            Log.e(TAG,"raw..." + e.toString());
        }
    }
}
