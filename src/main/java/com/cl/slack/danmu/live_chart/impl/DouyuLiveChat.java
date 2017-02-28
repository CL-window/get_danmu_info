package com.cl.slack.danmu.live_chart.impl;


import android.util.Log;

import com.cl.slack.danmu.live_chart.LiveChat;
import com.cl.slack.danmu.live_chart.NumUtils;
import com.cl.slack.danmu.live_chart.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaozhuai on 17/2/20.
 * TODO : 礼物太多 ，拿不到在线人数
 */
public class DouyuLiveChat extends LiveChat {

    private final String TAG = "Douyu";

    private boolean loop;
    private String host;
    private int port;

    private void mainLoop(){
        loop = true;

        while (loop && mSocketClient.isConnected()){

            try {

                byte[] packetHeader = new byte[12];
                mSocketClient.forceRead(packetHeader, 0, 12);
                NumUtils.reverseBytes(packetHeader, 0, 4);
                int len = NumUtils.byteArrayToUint32(packetHeader, 0) - 9;


                byte[] buffer = new byte[len];
                mSocketClient.forceRead(buffer, 0, len);
                mSocketClient.skipBytes(1);
                String msg = new String(buffer, 0, len, Charset.forName("UTF-8"));

                if(msg.contains("type@=keeplive")) onHeartBeatReciv();
                else onRaw(msg);

            }catch (IOException e){
                onErr("弹幕服务器连接已断开！");
                break;
            }

        }

        onDisconnected();

        mSocketClient.disconnected();
    }

    private byte[] pack(byte[] data){
        byte[] pack = new byte[data.length+12+1];  // 12 头  1 结尾的 0
        pack[pack.length-1] = 0;
        pack[8] = (byte) 0xb1;
        pack[9] = (byte) 0x02;

        int len = data.length+9;
        NumUtils.uint32ToByteArray(len, pack, 0);
        NumUtils.reverseBytes(pack, 0, 4);

        NumUtils.uint32ToByteArray(len, pack, 4);
        NumUtils.reverseBytes(pack, 4, 4);

        for(int i=0; i<data.length; i++){
            pack[12+i] = data[i];
        }

        return pack;
    }

    @Override
    public void disconnect() {
        loop = false;
    }

    @Override
    protected void connectEntry() {

        host = "openbarrage.douyutv.com";
        port = 8601;

        try {
            URL url = new URL(room);
            String[] tmp = url.getFile().split("/");
            roomId = tmp[tmp.length-1];
        } catch (MalformedURLException e) {
            onErr("连接弹幕服务器失败，解析RoomId错误！");
            return;
        }

        mSocketClient.setAddr(host, port);
        if(!mSocketClient.connect()){
            onErr("连接弹幕服务器失败，未能建立socket连接！");
            return;
        }

        try {
            String loginMsg = String.format("type@=loginreq/roomid@=%s/", roomId);
            byte[] loginPack = pack(loginMsg.getBytes());
            mSocketClient.write(loginPack);

            byte[] packetHeader = new byte[12];
            mSocketClient.forceRead(packetHeader, 0, 12);
            NumUtils.reverseBytes(packetHeader, 0, 4);
            int len = NumUtils.byteArrayToUint32(packetHeader, 0) - 9;


            byte[] buffer = new byte[len];
            mSocketClient.forceRead(buffer, 0, len);
            mSocketClient.skipBytes(1);
            String msg = new String(buffer, 0, len, Charset.forName("UTF-8"));

            if(!msg.contains("type@=loginres")){
                onErr("连接弹幕服务器失败，登录弹幕服务器错误！");
                return;
            }

        }catch (IOException e){
            onErr("连接弹幕服务器失败，登录弹幕服务器错误！");
            return;
        }

        onConnected();

        try{
            String joinMsg = String.format("type@=joingroup/rid@=%s/gid@=-9999/", roomId);
            byte[] joinRoomPack = pack(joinMsg.getBytes());
            mSocketClient.write(joinRoomPack);
        }catch (IOException e){
            onErr("连接弹幕服务器失败，加入聊天室错误！");
            return;
        }

        mainLoop();

    }

    @Override
    protected byte[] getHeartBeatPacket() {
        String heartBeatMsg = String.format("type@=keeplive/tick@=%d/", System.currentTimeMillis()/1000);
        return pack(heartBeatMsg.getBytes());
    }

    @Override
    protected boolean isSocket() {
        return true;
    }

    @Override
    protected void onRaw(String raw) {
        super.onRaw(raw);
//        Log.i(TAG,"raw..." + raw);
        parseServerMsg(new DouYuMessage(raw).getMessageList());
    }

    /**
     * 聊天
     */
    private final String TypeChart = "chatmsg";

    /**
     * 礼物
     */
    private final String TypeGift = "dgb";

    /**
     * 房间内礼物广播
     */
    private final String TypeGiftBroad = "spbc";

    /**
     * 新用户 进入房间
     */
    private final String TypeUserIn = "uenter";

    /**
     * 解析从服务器接受的协议 以及根据需要进行业务操作
     * TODO : gift img some problem
     *  gfid 查询 礼物 name img
     */
    private void parseServerMsg(Map<String, Object> msg){
        try {
            Log.i(TAG,"msg " + msg.toString());

            Object type = msg.get("type");

            if(type != null){

                /**
                 * 服务器反馈错误信息
                 * 结束心跳和获取弹幕线程
                 */
                if(type.equals("error")){
                    onErr("get douyu server msg type error");
                    return;
                }

                if(TypeChart.equals(type)){//弹幕消息
                    onMsg(msg.get("nn").toString(),msg.get("txt").toString());
                } else if(TypeGift.equals(type)){//赠送礼物信息
                    int gfid = Integer.parseInt(msg.get("gfid").toString());
                    String giftName = "";
                    String giftPic = "";
                    switch (gfid) {
                        case 192:
                            giftName = "赞";
                            giftPic = "https://staticlive.douyucdn.cn/upload/dygift/1606/09d957288d2dfb811de4c5784ec74414.gif";
                            break;
                        case 191:
                            giftName = "100鱼丸";
                            giftPic = "https://staticlive.douyucdn.cn/upload/dygift/1606/c3226805223b413ff84fa972b4ebdb13.png";
                            break;

                        case 497:
                            giftName = "仙女棒";
                            giftPic = "https://staticlive.douyucdn.cn/upload/dygift/1612/39b0cb85da36ffe4ef04a8d1b813315f.gif";
                            break;
                        case 519:
                            giftName = "呵呵";
                            giftPic = "https://staticlive.douyucdn.cn/upload/dygift/1612/61414e3b96e9e6112ee6cdb8bc7f4f3a.gif";
                            break;
                        case 520:
                            giftName = "稳";
                            giftPic = "https://staticlive.douyucdn.cn/upload/dygift/1612/ab8d2f5b9cb715c3b56fc803a79bc8db.gif";
                            break;

                    }
                    onGift(msg.get("nn").toString(),
                            giftName,
                            giftPic,
                            msg.get("gfcnt")==null?1:Integer.parseInt(msg.get("gfcnt").toString()),
                            msg.get("hits")==null?1:Integer.parseInt(msg.get("hits").toString()));
                }else if(TypeGiftBroad.equals(type)){
                    String gfid = msg.get("gfid").toString();
                    onGift(msg.get("sn").toString(),
                            msg.get("gn").toString(),
                            "",
                            Integer.parseInt(msg.get("gc").toString()),
                            1);
                }
                else if(TypeUserIn.equals(type)){
                    onNewUserIn(msg.get("nn").toString(),"来到本直播间");
                }
            }
        }catch (Exception e){
            onErr(e.toString());
        }
    }

    private static class  DouYuMessage{
        Map<String, Object> messageList;
        DouYuMessage(String data) {
            this.messageList = parseRespond(data);
        }

        /**
         * 获取弹幕信息对象
         */
        Map<String, Object> getMessageList() {
            return messageList;
        }

        /**
         * 解析弹幕服务器接收到的协议数据
         * @param data stringData
         * @return
         */
        private Map<String, Object> parseRespond(String data){
            Map<String, Object> rtnMsg = new HashMap<String, Object>();

            /**
             * 处理数据字符串末尾的'/0字符'
             */
            data = StringUtils.substringBeforeLast(data, "/");

            /**
             * 对数据字符串进行拆分
             */
            String[] buff = data.split("/");

            /**
             * 分析协议字段中的key和value值
             */
            for(String tmp : buff){
                //获取key值
                String key = StringUtils.substringBefore(tmp, "@=");
                //获取对应的value值
                Object value = StringUtils.substringAfter(tmp, "@=");

                //如果value值中包含子序列化值，则进行递归分析
                if(StringUtils.contains((String)value, "@A")){
                    value = ((String)value).replaceAll("@S", "/").replaceAll("@A", "@");
                    value = this.parseRespond((String)value);
                }

                //将分析后的键值对添加到信息列表中
                rtnMsg.put(key, value);
            }

            return rtnMsg;

        }
    }



}
