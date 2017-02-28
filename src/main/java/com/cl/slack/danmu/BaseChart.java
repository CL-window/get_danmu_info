package com.cl.slack.danmu;

import com.cl.slack.danmu.live_chart.LiveChatCallback;

/**
 * Created by slack
 * on 17/2/28 下午4:54.
 */

public class BaseChart {

    protected LiveChatCallback mLiveChatCallback;

    public void setLiveChatCallback(LiveChatCallback liveChatCallback){
        mLiveChatCallback = liveChatCallback;
    }

    protected void onConnected(){
        if(mLiveChatCallback!=null) mLiveChatCallback.onConnected();
    }

    protected void onDisconnected(){
        if(mLiveChatCallback!=null) mLiveChatCallback.onDisconnected();
    }

    protected void onErr(String errMsg){
        if(mLiveChatCallback!=null) mLiveChatCallback.onErr(errMsg);
//        System.out.println("err: " + errMsg);
    }

    protected void onMsg(String nickname, String content){
        if(mLiveChatCallback!=null) mLiveChatCallback.onMsg(nickname, content);

    }

    protected void onGift(String nickname, String giftName, String giftImg, int count, int total){
        if(mLiveChatCallback!=null) mLiveChatCallback.onGift(nickname, giftName, giftImg, count, total);

    }

    protected void onViewerCount(int count){
        if(mLiveChatCallback!=null) mLiveChatCallback.onViewerCount(count);

    }

    protected void onNewUserIn(String nickname,String action){
        // like : xxx 开着高尔夫进入直播间！
        if(mLiveChatCallback!=null) mLiveChatCallback.onUserIn(nickname,action);
    }
}
