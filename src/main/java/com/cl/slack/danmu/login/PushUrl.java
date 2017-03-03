package com.cl.slack.danmu.login;

/**
 * Created by slack
 * on 17/3/2 下午2:21.
 * 暂时 cookies 是手动设置进入的
 * cookies 来源 ： 电脑网页登录账号，chrome-->开发者-->network-->随便找一个item
 *                  request headers : cookie
 */

public abstract class PushUrl {

    protected PushUrlCallback mPushUrlCallback;
    protected String mCookie;

    public void addCookie(String cookie){
        mCookie = cookie;
    }

    /**
     * 开始推流 ，获取推流地址
     */
    public void startLivePush() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getRoomInfo();
                    getRoomPushUrl();
                }catch (Exception e){
                    onErr("获取直播间推流地址失败");
                }
            }
        }).start();

    }

    /**
     * 结束推流
     */
    public void stopLivePush(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    closeRoom();
                }catch (Exception e){
                    onErr("关闭直播间失败");
                    return;
                }
                onClose();
            }
        }).start();
    }

    public void setParam(String... params){

    }

    public void setPushUrlCallback(PushUrlCallback l){
        mPushUrlCallback = l;
    }

    protected void onErr(String msg){
        if(mPushUrlCallback != null) mPushUrlCallback.onError(msg);
    }

    protected void onGetUrl(String prefix,String suffix){
        String url ;
        if(prefix.endsWith("/")){
            url = prefix + suffix;
        }else {
            url = prefix + "/" + suffix;
        }
        if(mPushUrlCallback != null) mPushUrlCallback.onGetUrl(url);
    }

    protected void onClose(){
        if(mPushUrlCallback != null) mPushUrlCallback.onClose();
    }

    /**
     * 获取直播 房间 信息
     */
    public abstract void getRoomInfo() throws Exception;

    /**
     * 关闭直播
     */
    public abstract void closeRoom() throws Exception;

    /**
     * 开启直播并获取推流地址
     */
    public abstract void getRoomPushUrl() throws Exception;

    public interface PushUrlCallback{
        void onGetUrl(String url);
        void onError(String msg);
        void onClose();
    }

}
