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

/**
 * Created by xiaozhuai on 17/2/19.
 * 没有新用户进入提示
 */
public class PandaLiveChat extends LiveChat {

    private static final String ROOM_INFO_URL = "http://www.panda.tv/ajax_chatroom?roomid=%s";
    private static final String CHAT_INFO_URL = "http://api.homer.panda.tv/chatroom/getinfo?rid=%s&roomid=%s&retry=0&sign=%s&ts=%s";
    private static final String LOGIN_DATA_FMT = "" +
            "u:%s@%s\n" +
            "k:1\n" +
            "t:300\n" +
            "ts:%s\n" +
            "sign:%s\n" +
            "authtype:%s";
    private static final String TAG = "Panda";

    private String sign;
    private String rid;
    private String ts;

    private String appid;
    private String authType;

    private String host;
    private int port;

    private String loginData;

    private boolean loop;


    public void getRoomInfo() throws Exception {
        String roomInfoText = HttpUtils.httpGet(String.format(ROOM_INFO_URL, roomId));
        JSONObject json = JSON.parseObject(roomInfoText);
        json = json.getJSONObject("data");
        sign = json.getString("sign");
        rid = json.getString("rid");
        ts = json.getString("ts");
    }

    public void getChatInfo() throws Exception {
        String chatInfoText = HttpUtils.httpGet(String.format(CHAT_INFO_URL, rid, roomId, sign, ts));
        JSONObject json = JSON.parseObject(chatInfoText);
        json = json.getJSONObject("data");
        rid = json.getString("rid");
        appid = json.getString("appid");
        ts = json.getString("ts");
        sign = json.getString("sign");
        authType = json.getString("authType");

        String[] tmp = json.getJSONArray("chat_addr_list").getString(0).split(":");
        host = tmp[0];
        port = Integer.parseInt(tmp[1]);

        loginData = String.format(LOGIN_DATA_FMT, rid, appid, ts, sign, authType);
    }


    public void parsePacket(byte[] packetData){
        //00 00 00 00
        //00 00 00 00
        //00 00 00 00
        //00 00 00 FA

        int offset = 0;
        int len;

        int msgIndex = 0;

        while(true){
            len = NumUtils.byteArrayToUint32(packetData, offset+12);

            String msg = new String(packetData, offset+16, len, Charset.forName("UTF-8"));

//            onRaw("msgIndex: "+msgIndex+", "+msg);
            onRaw(msg);
            offset += 16 + len;
            if(offset==packetData.length) break;

            msgIndex ++;
        }

    }


    public void mainLoop(){

        loop = true;

        while (loop && mSocketClient.isConnected()){
            // 00 06 00 xx xx是消息类型

            // 00 06 00 00 客户端心跳
            // 00 06 00 01 服务端心跳
            // 00 06 00 03 消息
            // 00 06 00 06 登录返回

            // 00 05
            // 61 63 6B 3A 30
            // 00 00 01 0A

            try {

                byte[] packetHeader = new byte[4];
                mSocketClient.forceRead(packetHeader, 0, 4);

                int len;
                switch (packetHeader[3]) {
                    case (byte) 0x01: {
                        //心跳
                        onHeartBeatReciv();
                        break;
                    }
                    case (byte) 0x06: {
                        //登录返回
                        len = mSocketClient.readUint16();
                        byte[] body = new byte[len];
                        mSocketClient.forceRead(body, 0, len);
                        String loginRes = new String(body, Charset.forName("UTF-8"));

                        try {
                            String resAppid = loginRes.split("\n")[0].split(":")[1];
                            if (resAppid.equals(appid)) {
                                onConnected();
                            }
                        } catch (Exception e) {
                            onErr("连接弹幕服务器失败，解析登录返回信息错误！");
                        }

                        break;
                    }
                    case (byte) 0x03: {
                        //消息
                        len = mSocketClient.readUint16();
                        mSocketClient.skipBytes(len);
                        len = mSocketClient.readUint32();
                        byte[] body = new byte[len];
                        mSocketClient.forceRead(body, 0, len);
                        parsePacket(body);
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
    protected byte[] getHeartBeatPacket() {
        return new byte[]{ (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x00 };
    }

    @Override
    protected boolean isSocket() {
        return true;
    }

    @Override
    public void connectEntry(){

        try {
            URL url = new URL(room);
            String[] tmp = url.getFile().split("/");
            roomId = tmp[tmp.length-1];
        } catch (MalformedURLException e) {
            onErr("连接弹幕服务器失败，解析url错误！");
            return;
        }


        try {
            getRoomInfo();
            getChatInfo();
        }catch (Exception e){
            onErr("连接弹幕服务器失败，获取登录参数错误！");
            return;
        }

        mSocketClient.setAddr(host, port);
        if(!mSocketClient.connect()){
            onErr("连接弹幕服务器失败，未能建立socket连接！");
            return;
        }

        byte[] header = { (byte)0x00, (byte)0x06, (byte)0x00, (byte)0x02 };
        byte[] loginBody = loginData.getBytes();


        try {
            mSocketClient.write(header);
            mSocketClient.writeUint16(loginBody.length);
            mSocketClient.write(loginBody);
        }catch (IOException e){
            onErr("连接弹幕服务器失败，发送登录包错误！");
            return;
        }

        mainLoop();
    }

    @Override
    public void disconnect() {
        loop = false;
    }

    /**
     * 用户 聊天 信息
     */
    private final String TypeChart = "1";

    /**
     * 在线观看人数
     */
    private final String TypeLiveCount = "207";

    /**
     * 用户 送出礼物 信息
     */
    private final String TypeGift306 = "306";


    /**
     * 没有网络图片的 礼物 竹子
     */
    private final String TypeGift206 = "206";
    /**
     * 竹子
     */
    private final String Zhuzi = "竹子";
    private final String ZhuziUrl = "http://i8.pdim.gs/d6c7588a479e657e4d589a54f6075c8b.png";

    @Override
    protected void onRaw(String raw) {
        super.onRaw(raw);
        Log.i(TAG,"raw..." + raw);
        try {
            JSONObject message = (JSONObject) JSON.parse(raw);

            String type = message.getString("type");
            JSONObject data = message.getJSONObject("data");
            if(TypeChart.endsWith(type)){
                onMsg(data.getJSONObject("from").getString("nickName"),data.getString("content"));
            } else
            if(TypeLiveCount.endsWith(type)){
                onViewerCount(data.getInteger("content"));
            } else
            if(TypeGift306.endsWith(type)){
                JSONObject content = data.getJSONObject("content");
                onGift(data.getJSONObject("from").getString("nickName"),
                        content.getString("name"),
                        content.getJSONObject("pic").getJSONObject("pc").getString("chat"),
                        content.getIntValue("count"),
                        content.getIntValue("combo"));
            }else
            if(TypeGift206.endsWith(type)){
                onGift(data.getJSONObject("from").getString("nickName"),
                        Zhuzi,
                        ZhuziUrl,
                        data.getInteger("content"),
                        1
                        );
            }

        }catch(Exception e) {
            Log.e(TAG,"raw..." + e.toString());
        }
    }
}
