package com.cl.slack.danmu.live_chart;

import com.cl.slack.danmu.BaseChart;

import java.io.IOException;

/**
 * Created by xiaozhuai on 17/2/19.
 */
public abstract class LiveChat extends BaseChart{

    protected String room;
    protected String roomId;
    protected SocketClient mSocketClient =  new SocketClient();

    protected Thread mHeartBeatThread;
    protected boolean connected = false;

    protected long mHeartBeatTimeSpan = 40*1000;


    protected Runnable mHeartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            while(connected) {
                heartBeat();
                try {
                    Thread.sleep(mHeartBeatTimeSpan);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void connect(String chatRoom){
        room = chatRoom;
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectEntry();
            }
        }).start();
    }

    public abstract void disconnect();

    protected abstract void connectEntry();

    protected abstract byte[] getHeartBeatPacket();

    protected abstract boolean isSocket();

    protected void heartBeat(){
        try {
            mSocketClient.write(getHeartBeatPacket());
        }catch (IOException e){
            onErr("弹幕服务器连接已断开！");
            disconnect();
        }
        System.out.println("heart beat send");
    }

    protected void onHeartBeatReciv(){
        System.out.println("heart beat reciv");
    }


    protected void onConnected(){
        super.onConnected();
        connected = true;
//        System.out.println("connected");
        if(isSocket() && getHeartBeatPacket()!=null){
            mHeartBeatThread =  new Thread(mHeartBeatRunnable);
            mHeartBeatThread.start();
        }
    }

    protected void onDisconnected(){
        super.onDisconnected();
        connected = false;
//        System.out.println("disconnected");
    }

    protected void onErr(String errMsg){
        super.onErr(errMsg);
//        System.out.println("err: " + errMsg);
    }

    protected void onMsg(String nickname, String content){
        super.onMsg(nickname,content);
//        System.out.println("err: " + errMsg);
    }

    protected void onGift(String nickname, String giftName, String giftImg, int count, int total){
        super.onGift(nickname, giftName, giftImg, count, total);
    }

    protected void onViewerCount(int count){
        super.onViewerCount(count);
    }

    protected void onNewUserIn(String nickname,String action){
        super.onNewUserIn(nickname, action);
    }

    protected void onRaw(String raw){
//        System.out.println("raw msg: " + raw);
    }

}
