package com.cl.slack.danmu.live_chart.impl;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.live_chart.HttpUtils;
import com.cl.slack.danmu.live_chart.LiveChat;
import com.cl.slack.danmu.live_chart.NumUtils;
import com.cl.slack.danmu.live_chart.TextEncoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaozhuai on 17/2/20.
 */
public class ZhanqiLiveChat extends LiveChat {

    private final String TAG = "Zhanqi";
    private boolean loop;
    private String host;
    private int port;

    private String sid;
    private String gid;
    private String ts;
    private String ssid;

    private byte[] loginBody;


    private void getRoomIdAndSocket() throws Exception{
        String html = HttpUtils.httpGet(room);

        Pattern pattern = Pattern.compile("\"RoomId\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        roomId = matcher.group(1);

        pattern = Pattern.compile("\"Servers\"\\s*:\\s*\"(.+?)\"");
        matcher = pattern.matcher(html);
        matcher.find();
        String base64ServList = matcher.group(1);

        JSONObject servs = JSON.parseObject(TextEncoder.base64Decode(base64ServList));
        host = servs.getJSONArray("list").getJSONObject(0).getString("ip");
        port = servs.getJSONArray("list").getJSONObject(0).getIntValue("port");

    }

    private void generateLoginBody() throws Exception {
        JSONObject json = JSON.parseObject(HttpUtils.httpGet("https://www.zhanqi.tv/api/public/room.viewer?sid="));
        json = json.getJSONObject("data");
        gid = json.getString("gid");
        sid = json.getString("sid");
        ts = json.getString("timestamp");

        ssid = "0" + gid + "www.zhanqi.tvWY|qZJYcX(K4zj^%" + ts;
        ssid = TextEncoder.md5Str(ssid);
        ssid = TextEncoder.base64Encode(ssid);

        loginBody = String.format(
                "{\"uid\":0,\"thirdaccount\":\"\",\"roomid\":%s,\"sid\":\"%s\",\"cmdid\":\"loginreq\",\"nickname\":\"\",\"timestamp\":%s,\"hideslv\":0,\"fhost\":\"\",\"ssid\":\"%s\",\"t\":0,\"gid\":%s,\"fx\":0,\"roomdata\":{\"vlevel\":0,\"slevel\":[],\"vdesc\":\"\"},\"ver\":12}",
                roomId, sid, ts, ssid, gid
                ).getBytes();
    }

    private void mainLoop(){

        onConnected();

        loop = true;

        while (loop && mSocketClient.isConnected()){

            // bb cc 00 00 00 00 38 01 00 00 10 27 收到消息
            // bb cc 00 00 00 00 16 00 00 00 e8 03 心跳

            try {

                byte[] packetHeader = new byte[12];
                mSocketClient.forceRead(packetHeader, 0, 12);
                NumUtils.reverseBytes(packetHeader, 6, 2);
                int len = NumUtils.byteArrayToUint16(packetHeader, 6);
                int type = NumUtils.byteArrayToUint16(packetHeader, 10);

                switch (type) {
                    case 0xe803: {
                        onHeartBeatReciv();
                        mSocketClient.skipBytes(len);
                        break;
                    }
                    case 0x1027: {
                        byte[] buffer = new byte[len];
                        mSocketClient.forceRead(buffer, 0, len);
                        String body = new String(buffer, 0, len, Charset.forName("UTF-8"));
                        onRaw(body);
                        break;
                    }
                }

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
            getRoomIdAndSocket();
        } catch (Exception e) {
            onErr("连接弹幕服务器失败，解析RoomId和服务器列表错误！");
            return;
        }

        try {
            generateLoginBody();
        } catch (Exception e) {
            onErr("连接弹幕服务器失败，构建登录包失败！");
            return;
        }

        mSocketClient.setAddr(host, port);
        if(!mSocketClient.connect()){
            onErr("连接弹幕服务器失败，未能建立socket连接！");
            return;
        }

        byte[] header = new byte[]{(byte)0xbb, (byte)0xcc, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x38, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x10, (byte)0x27};
        int len = loginBody.length;
        NumUtils.uint16ToByteArray(len, header, 6);
        NumUtils.reverseBytes(header, 6, 2);

        try {
            mSocketClient.write(header);
            mSocketClient.write(loginBody);
        }catch (IOException e){
            onErr("连接弹幕服务器失败，发送登录包错误！");
            return;
        }

        mainLoop();
    }

    @Override
    protected byte[] getHeartBeatPacket() {
        return new byte[]{(byte)0xbb, (byte)0xcc, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x16, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xE8, (byte)0x03, (byte)0x7B, (byte)0x22, (byte)0x63, (byte)0x6D, (byte)0x64, (byte)0x69, (byte)0x64, (byte)0x22, (byte)0x3A, (byte)0x22, (byte)0x73, (byte)0x76, (byte)0x72, (byte)0x69, (byte)0x73, (byte)0x6F, (byte)0x6B, (byte)0x72, (byte)0x65, (byte)0x71, (byte)0x22, (byte)0x7D};
    }

    @Override
    protected boolean isSocket() {
        return true;
    }

    /**
     * 用户 聊天 信息
     */
    private final String TypeChart = "chatmessage";

    /**
     * 在线观看人数
     */
    private final String TypeLiveCount = "getuc";

    /**
     * 用户 送出礼物 信息
     */
    private final String TypeGift = "Gift.Use";

    /**
     * 用户 进入房间
     */
    private final String TypeUserIn = "Car.Show";

    @Override
    protected void onRaw(String raw) {
        super.onRaw(raw);
        Log.i(TAG,"raw..." + raw);
        try {

            JSONObject message = ((JSONObject) JSON.parse(raw));

            String type = message.getString("cmdid");

            if(TypeChart.equals(type)){
                onMsg(message.getString("fromname"),
                        message.getString("content"));
            }else if(TypeLiveCount.equals(type)){
                onViewerCount(message.getIntValue("count"));
            }else if(TypeGift.equals(type)){
                JSONObject data = message.getJSONObject("data");
                onGift(data.getString("nickname"),
                        data.getString("name"),
                        data.getString("icon"),
                        data.getIntValue("count"),// count 就是一个的数量
                        1);
            }else if(TypeUserIn.equals(type)){
                JSONObject data = message.getJSONObject("data");
                onNewUserIn(data.getString("name"),
                        data.getString("action"));
            }

        }catch(Exception e) {
            Log.e(TAG,"raw..." + e.toString());
        }
    }
}
