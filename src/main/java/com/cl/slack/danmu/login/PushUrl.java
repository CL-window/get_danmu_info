package com.cl.slack.danmu.login;

/**
 * Created by slack
 * on 17/3/2 下午2:21.
 */

public abstract class PushUrl {

    /**
     * 获取直播 房间 信息
     */
    public abstract void getRoomInfo();

    /**
     * 关闭直播
     */
    public abstract void closeRoom();

    /**
     * 开启直播并获取推流地址
     */
    public abstract void getRoomPushUrl();

}
