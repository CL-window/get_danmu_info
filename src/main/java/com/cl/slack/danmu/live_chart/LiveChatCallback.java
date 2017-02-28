package com.cl.slack.danmu.live_chart;

/**
 * Created by xiaozhuai on 17/2/19.
 */
public interface LiveChatCallback {
    void onConnected();
    void onDisconnected();
    void onErr(String errMsg);
    void onMsg(String nickname, String content);
    void onGift(String nickname, String giftName, String giftImg, int count, int total);
    void onViewerCount(int count);
    void onUserIn(String nickname,String action);
}
