package com.cl.slack.danmu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.slack.danmu.login.DouyuPushUrl;
import com.cl.slack.danmu.login.HuyaPushUrl;
import com.cl.slack.danmu.login.LongzhuPushUrl;
import com.cl.slack.danmu.login.PandaPushUrl;
import com.cl.slack.danmu.login.PushUrl;
import com.cl.slack.danmu.login.QuanminPushUrl;
import com.cl.slack.danmu.login.ZhanqiPushUrl;

import java.util.Map;

public class TestPushUrlActivity extends BaseWebViewActivity {
    private static final String TAG = "PushUrl";
    private boolean mStopPush = false;
    private PushUrl mPushUrl;
    private final String mZhanqiCookie = "gid=1774650720; BF_UID=www.baidu.com_15a828fe9b4_be; AD_BF_UID=15a82b75d93_479971; __gads=ID=090e109b7d346214:T=1488251740:S=ALNI_Mb7_jDJwNDVi_-svfPB75PdpPMZ3A; _VID=www.baidu.com_15a8db8219c_cedb; _TID=www.baidu.com_15a8db8219c_cedb; ZQ_GUID=9C91C826-933F-B3DF-16D5-01D2A3A77E86; ZQ_GUID_C=9C91C826-933F-B3DF-16D5-01D2A3A77E86; tj_uid=110970179; Hm_lvt_299cfc89fdba155674e083d478408f29=1488253215,1488436339,1488436373,1488436536; Hm_lpvt_299cfc89fdba155674e083d478408f29=1488436536";
//    private final String mQuanminCookie = "qm_di=fort5zm15d3tr1m2ny4ww1oxj5scbp1sim6z4rwf; sid=61fe54c2a8b0f50bebd1f7f45b497b77; token=fgq9SY8816b739; uid=1488513056; Hm_lvt_63d2da29d5f9eb3559db793b4f7b0283=1488247623,1488254849,1488254899,1488437687; Hm_lpvt_63d2da29d5f9eb3559db793b4f7b0283=1488437726";
//    private final String mLongzhuCookie = "__gads=ID=054edad8cab3801f:T=1480097812:S=ALNI_Mbw26xPSdoxrCb8gwLekZYKwEFmqg; pgv_pvi=1897766912; PLULOGINSESSID=c52360452b7f00b4f8cad2537810d80e; p1u_id=28ce13c8d8fd3baad4348cb231e30e14c0f2a76f8b23f63ef34de6a1e7dd815b93c55e98f27db60a; pgv_si=s4648322048; _ma=OREN.2.1388103681.1480097814; pgv_info=ssid=s2413619159; pgv_pvid=8227009778";
    private final String mHuyaCookie = "hiido_ui=0.4719331785305796; hd_newui=0.8684650934874634; __yamid_tt1=0.5016889985909294; __yamid_new=C76A924DA22000017F605E003C9081B0; first_username_flag=1943046018yy_first_1; h_pr=1; yyuid=1704206903; username=1943046018yy; password=BDC2BFDCFDFCFFD20391978D8900FF60F8319492; osinfo=E1147DFD5D4738F0254F93FB14B42D065E88F64F; udb_l=DAAxOTQzMDQ2MDE4eXnsYK5YBXQA9VgHs-x9YAporf1DLZYrjsYS1CqZIRT8gawodbTBAWgXiY0cF-zFh1l4KcKmsQAyCW2EEZizO_naG9RUqAp-Cyk-y_gDbeVRDy07yepbBoXguqRQOKarXW9pUNE90oJFMtcPSaCHAoZ8jSiKZhEIHlNOBl8AAAAAAwAAAAAAAAAMADQ3LjkwLjUyLjIxOQQANTIxNg==; udb_n=5be1078a59bf2e1b85ccbfd14adb9a2b21a63fa36df3ad9e9a2d7a0ba999f0b4a95fcfafd9c4024025589bab89971739; udb_c=AEAWBlBqAAJgAOObG4OjfuSQWhPJXsK-XKCD4xqRbndpjIWT0pjNvbERLL3v8DKY-LrRrGx9431zN8ADrNtBQT9k8am-iDU0Cm9cffmPIi6zUTjcRPKv0LhnkiJdpN5UakJECOoXCwmzVw==; udb_oar=5FE3A3F7763CEA01F672E8E6AE50118365343BDF41948C2ACB812D749A50429CE4B8E8C3B50D13499E2B642ABA438D73F6A2F3E0B81675DD4202F565687708E669D903FA51FE2A96E91567E3A2B6480B51971DE1794AF1D3CB062C9AA8E9489AC3C7700D54AE549FBC760CE35AC46756F839E912F9D0D50241134D8738806DAEC8E58B8A6082A8B6A3BA23355634ECB371AFA7EFF8070AF8BD3FD3D31A6EAC82ABEC3737CDA6D6B4F87DFD53ACD3D0D63F4FAC78901546B48C8C43B14ED37A38CE2692550A7A48B77BF3882A59DAAF9AC34A0C8C76CD537EEA56BF5BE490F937C0904A5039FC2BF5F89560AE4E05C077E00620B20D1C7DDB35892019CCF2305495AD2955179F04E0FABE89978DD0E2B5F0820C45EB006EB7C9A0701800686190168FDDB4DA1142D2734C79B135159604F32702E34BFB9A2E831CEF030DC07E43; guid=b72475589bf5a3584f09b827b9275d28; __yasmid=0.5016889985909294; SoundValue=1.00; isInLiveRoom=; __yaoldyyuid=1704206903; _yasids=__rootsid%3DC76F18BBA370000134D0917613701071; PHPSESSID=1emqebb7h00udmq4vfje7dp6s2; h_unt=1488354945; undefined=undefined; Hm_lvt_51700b6c722f5bb4cf39906a596ea41f=1487674695,1487823054,1487919189,1488354940; Hm_lpvt_51700b6c722f5bb4cf39906a596ea41f=1488354956";
    private final String mPandaCookie = "__guid=96554777.717254311122803200.1488182372781.3662; SESSCYPHP=b9a8b49afea2919a3657a4d76af49af4; I=r%3D73046252; R=r%3D73046252%26u%3DCnaqnGi73046252%26n%3D%25R5%2590%25OR%25R7%259N%2587%25R4%25O8%2587%25R5%25O2%2581%25R5%2593%25N6%25R5%2593%2588%25R5%2593%2588%25R5%2593%2588%26le%3DZwL5BQZ5ZGVkWGDjpKRhL29g%26m%3DZGZkBQL3BQH4ZQx%3D%26im%3DnUE0pPHmDFHlEvHlEzx3YaOxnJ0hM3ZyZxLjZwH3ZQDjZmtjLzD3MQV5ATR4ZQR3AGZlLmN3MGZ5Lv5dpTp%3D; M=t%3D1488509575%26v%3D1.0%26mt%3D1488509575%26s%3Ddc66eda19860276dd45d6bf86fa5ddda; Hm_lvt_204071a8b1d0b2a04c782c44b88eb996=1488254220,1488254256,1488436714,1488509407; Hm_lpvt_204071a8b1d0b2a04c782c44b88eb996=1488509599; smid=0b7dfeb7-26e0-4aa6-ac4a-07756ba345a9";
//    private final String mDouyuCookie = "acf_did=F2DEB6F6B3F793D6200B26181FF6BD35; PHPSESSID=9tq3ioqfimbhmfvqho578lo265; acf_auth=e4cctyY1N5kEspxhvofSpOuF9UWNvGvBRPmiVsaFhSYYlOqpkA4WAghtfzafukJnlV8L4B8AZjivaQC6yzDzHrCMDLPvpi6RryIC059SvpRAVV5da3mgdpA; wan_auth37wan=a6b8b52491e3InLjNCv7YDut8Fjw6D6T4MbPXi96mS95wPY4nd%2FFc6PJ6W62sMGPqgyiLotNxWcvMwCLWjskaiX1a6yf92bLRDi2YZGlcBePwyZWEXQ; acf_uid=108429254; acf_username=108429254; acf_nickname=rr585; acf_own_room=1; acf_groupid=1; acf_phonestatus=1; acf_avatar=https%3A%2F%2Fapic.douyucdn.cn%2Fupload%2Favatar%2Fdefault%2F05_; acf_ct=0; acf_ltkid=28691108; acf_biz=1; acf_stk=62ca97794c4f8bd5; Hm_lvt_e99aee90ec1b2106afe7ec3b199020a7=1488255133,1488262036,1488263248,1488437399; Hm_lpvt_e99aee90ec1b2106afe7ec3b199020a7=1488437424; acf_ccn=7262ff5735a1bb6ce1814707c3fa9a11; _dys_lastPageCode=page_home,page_home; _dys_refer_action_code=init_page_home";
    /**
     * cookie webview 登录 拿到
     */
    private final String mDouyuCookie = "PHPSESSID=okmiqs0mcl6bjv4ecmfuajnsi6; acf_auth=b950XVr1ElqITelnYjR51bzqD%2FyQb%2BlEEcbAbNpC%2BhgZLalpTKe2tyhhnNzyakWb9Za0b0RT1As3Tr%2B4TEMeoMtnWCDYGs%2BPGSNhOnpRrOBt%2FDk56G3iYIM; wan_auth37wan=6708a6c37196PPEX5beAhGKETMn1RbW3JFLTBDcF6gw7WEfYydpqwXVhbVKjuMc2zbAmCdHuC0Pj8VUxabfNPfWpNsDk0S4xldLD5whvGDoirncnw2A; acf_uid=108429254; acf_username=108429254; acf_nickname=rr585; acf_own_room=1; acf_groupid=1; acf_phonestatus=1; acf_avatar=https%3A%2F%2Fapic.douyucdn.cn%2Fupload%2Favatar%2Fdefault%2F05_; acf_ct=0; acf_ltkid=28691180; acf_biz=1; acf_stk=7497c35eb153e822; acf_devid=96ac8d333e255d90cd8be526bfdf9bc0; acf_ccn=06dd93bdbb8223ddd19e0c929a7e3815; Hm_lvt_e99aee90ec1b2106afe7ec3b199020a7=1488874345,1488874383,1488874682,1488874695; Hm_lpvt_e99aee90ec1b2106afe7ec3b199020a7=1488874715; _dys_lastPageCode=page_home,page_home; _dys_refer_action_code=init_page_home";
    private final String mLongzhuCookie = "PLULOGINSESSID=7496f88c6472a295e766d77a804e380b; _ma=OREN.2.86103154.1488880618; p1u_id=76c0ea661939c048ebd19f480d5eaa8b93e6406a4460e0c2eb51a49c8c00ef0603a350df74cc5fe9";
    private final String mQuanminCookie = "qm_di=q4thffekdl82r1xhf7933bm5d2efip3uboxpzux9; Hm_lvt_1c2f113a074ad9bddc186ae851f94d0e=1488881485; Hm_lpvt_1c2f113a074ad9bddc186ae851f94d0e=1488881864; geetestcaptcha=58be88e562eac; sid=bb5317c4653b626ded6089a73398e614; token=fck2SYe8d23756; uid=1488513056";



    private final String mDouYuLoginUrl = "http://passport.douyu.com/iframe/index";
    private final String mLongZhuLoginUrl = "http://login.plu.cn/portable/login";
    private final String mQuanMinLoginUrl = "http://m.quanmin.tv/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_push_url);

    }

    /**
     * 斗鱼 done
     */
    public void douyu(View view) {
        getDouyuPushUrl(mDouyuCookie);
    }

    public void douyuWithLogin(View view) {
        getLocalCookie(mDouYuLoginUrl);
    }

    private void getDouyuPushUrl(String cookie){
        Log.i(TAG, "getDouyuPushUrl ...");
        clearWebView();
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new DouyuPushUrl();
            mPushUrl.addCookie(cookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    /**
     * 龙珠 都是走的这一个 就像开关一样 done
     */
    public void longzhu(View view) {
        getLongzhuPushUrl(mLongzhuCookie);
    }

    public void longzhuWithLogin(View view) {
        getLocalCookie(mLongZhuLoginUrl);
    }

    private void getLongzhuPushUrl(String cookie){
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new LongzhuPushUrl();

            if(!TextUtils.isEmpty(cookie)){
                mPushUrl.addCookie(cookie);
            }

            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.setParam("balabala","127");
            mPushUrl.startLivePush();
        }
    }

    /**
     * TODO : 关闭的地址是不对的
     * 貌似 是因为 网页上拿到的 cookie 不能使用？
     * @param view
     */
    public void xiongmao(View view) {
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new PandaPushUrl();
            mPushUrl.addCookie(mPandaCookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    /**
     * 不需要开启或关闭直播  done
     */
    public void quanmin(View view) {
        getQuanminPushUrl(mQuanminCookie);
    }

    public void quanminWithLogin(View view) {
        getLocalCookie(mQuanMinLoginUrl);
    }

    private void getQuanminPushUrl(String cookie){
        mPushUrl = new QuanminPushUrl();
        mPushUrl.addCookie(cookie);
        mPushUrl.setPushUrlCallback(mPushUrlCallback);
        mPushUrl.startLivePush();
    }

    public void zhanqi(View view) {
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new ZhanqiPushUrl();
            mPushUrl.addCookie(mZhanqiCookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    public void huya(View view) {
        if(mStopPush){
            mStopPush = false;
            mPushUrl.stopLivePush();
        }else {
            mStopPush = true;
            mPushUrl = new HuyaPushUrl();
            mPushUrl.addCookie(mHuyaCookie);
            mPushUrl.setPushUrlCallback(mPushUrlCallback);
            mPushUrl.startLivePush();
        }
    }

    public void sixjianfang(View view) {

    }

    public void testOther(View view) {
        testWebView();
//        new LongZhuInitDialog(this).show();
        //showAlert();
    }

    private void testWebView() {
        startActivity(new Intent(this,TestLoginActivity.class));
    }

    public void clearCookie(View view) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieManager.removeExpiredCookie();
        mLoginCallback.clearTempData();
        clearCookie();
        showToast("清除成功");
    }

    private PushUrl.PushUrlCallback mPushUrlCallback = new PushUrl.PushUrlCallback() {
        @Override
        public void onGetUrl(String url) {
            showToast("获取直播地址成功");
            Log.i(TAG,"onGetUrl : " + url);
            mLoginCallback.clearTempData();
        }

        @Override
        public void onError(String msg) {
            Log.e(TAG,"onError : " + msg);
        }

        @Override
        public void onClose() {
            showToast("关闭直播成功");
            Log.i(TAG,"onClose ...");
            mLoginCallback.clearTempData();
        }
    };


    private void showAlert(){
        try {
            String json = getAssertString("longzhuType.json");
            JSONObject o = JSON.parseObject(json);
            for (Map.Entry<String, Object> entry : o.entrySet()) {
                Log.i("slack",entry.getKey() + " , " +  entry.getValue());
            }

        }catch (Exception e){
            Log.e(TAG,"json error : " + e.toString());
        }
    }

    private final String mCookieFile = "live_login_cookie";
    private String getCookie(String url){
        SharedPreferences cookieSP = getSharedPreferences(mCookieFile, MODE_PRIVATE);
        return cookieSP.getString(url,null);
    }

    /**
     * TODO : 获取到可用的 cookie url  不是登录的地址
     */
    private void saveCookie(String url,String cookie){
        SharedPreferences cookieSP = getSharedPreferences(mCookieFile, MODE_PRIVATE);
        cookieSP.edit().putString(url, cookie).apply();
    }

    private void clearCookie(String url){
        SharedPreferences cookieSP = getSharedPreferences(mCookieFile, MODE_PRIVATE);
        cookieSP.edit().putString(url,null).apply();
    }

    private void clearCookie(){
        SharedPreferences cookieSP = getSharedPreferences(mCookieFile, MODE_PRIVATE);
        cookieSP.edit().clear().commit();
    }

    private LoginCallback mLoginCallback = new LoginCallback() {

        /**
         * 防止多次调用函数
         */
        private boolean needLogin;
        private boolean loginSuccess;

        @Override
        public void onLoginSuccess(String url,String cookie) {
            if(!loginSuccess){
                loginSuccess = true;
                /**
                 * 存储 cookie
                 */
                saveCookie(url,cookie);
                switch (url) {
                    case mDouYuLoginUrl:
                        getDouyuPushUrl(cookie);
                        break;
                    case mLongZhuLoginUrl:
                        getLongzhuPushUrl(cookie);
                        break;
                    case mQuanMinLoginUrl:
                        getQuanminPushUrl(cookie);
                        break;

                    default:break;
                }
            }
        }

        @Override
        public void onNeedLogin(String url) {
            if(!needLogin){
                needLogin = true;
                login(url);
            }
        }

        @Override
        public void clearTempData() {
            needLogin = false;
            loginSuccess = false;
        }
    };

    private void login(String url) {
        showToast("正在转到登录...");
        Log.i(TAG, "login :" + url);
        clearWebView();
        mWebView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( // 1,1);
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        addWebView();
        /**
         * 全民不需要使用 电脑 版
         */
        if(!url.equals(mQuanMinLoginUrl)){
            useWebUserAgent();
        }
        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 暂时发现 在  onPageFinished 和 onLoadResource 都可以获取到想要的数据
             * 全民 不行
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(url);
                if(!TextUtils.isEmpty(cookie))
                    Log.i(TAG, "onPageFinished url: " + url + "  cookie:" +cookie);

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                checkCookie(url);

            }

        });
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
//        Log.i(TAG,"ua " + setting.getUserAgentString());

        setting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        setting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        setting.setSupportZoom(true); // 支持缩放
        setting.setBuiltInZoomControls(true); // 支持手势缩放
        setting.setDisplayZoomControls(false); //隐藏原生的缩放控件

        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAllowFileAccess(true);// 设置可以访问文件  貌似是cookie 需要
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);

        mWebView.loadUrl(url);
    }

    /**
     * TODO : cookie 失效
     * 判断 是否有 cookie
     * 1.本地
     * 2.CookieManager
     *          如果本地有，判断是否失效 ---没有或者失效了---->登录
     *                  ｜没有失效                        ｜
     *                  ｜                               ｜CookieManager 获取cookie
     *                使用Cookie <-------------------------|
     * @param url url
     * @return
     */
    private void getLocalCookie(String url){
        String cookie = getCookie(url);
        if(TextUtils.isEmpty(cookie)){
            if(mLoginCallback != null)
                mLoginCallback.onNeedLogin(url);
        }else {
            if(mLoginCallback != null)
                mLoginCallback.onLoginSuccess(url,cookie);
        }
    }

    /**
     * 从Html获取cookie
     * @param url url
     */
    private void checkCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);
        if(!TextUtils.isEmpty(cookie)) {
            /**
             * douyu :
             * 测试 发现 cookie 携带信息不全，后面获取地址不会成功
             * 可以使用的 cookie  1. start with  PHPSESSID:xxxxxxxxxx
             *                  2. _dys_lastPageCode＝xxxxxx 是有值的
             * longzhu: 包含 p1u_id  就 OK
             * quanming : sid token
             *
             */
            Log.i(TAG,"checkCookie  url :"+  url +" cookie:"+  cookie);
            SharedPreferences cookieSP = getSharedPreferences(mCookieFile, MODE_PRIVATE);
            if(url.contains("douyu.com") && cookie.startsWith("PHPSESSID")){
                if(mLoginCallback != null)
                    mLoginCallback.onLoginSuccess(mDouYuLoginUrl,cookie);
                return;
            }else if(url.contains("longzhu.com") && cookie.contains("p1u_id")){
                if(mLoginCallback != null)
                    mLoginCallback.onLoginSuccess(mLongZhuLoginUrl,cookie);
                return;
            }else if(url.contains("quanmin.tv") && cookie.contains("token")){
                if(mLoginCallback != null)
                    mLoginCallback.onLoginSuccess(mQuanMinLoginUrl,cookie);
                return;
            }

        }
        if(mLoginCallback != null)
            mLoginCallback.onNeedLogin(url);
    }


    interface LoginCallback{
        /**
         *
         * @param url 平台 登录 URL
         * @param cookie cookie
         */
        void onLoginSuccess(String url,String cookie);
        void onNeedLogin(String url);
        void clearTempData();
    }

}
