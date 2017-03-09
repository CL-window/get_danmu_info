package com.cl.slack.danmu.login;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaozhuai
 * @date : 17/3/2
 */

public abstract class LiveLoginWebView extends WebView {

    private Context mContext;
    protected Handler mHandler = new Handler();

    public LiveLoginWebView(Context context) {
        super(context);
        init(context);
    }

    public LiveLoginWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiveLoginWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){
        mContext = context;
        this.addJavascriptInterface(this, "webview");
    }

    public interface OnCookieCallback {
        void onCookie(String cookie);
    }


    private static final String USER_AGENT_PC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private static final String USER_AGENT_MOBILE = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Mobile Safari/537.36";
    protected String mUrl;
    protected String cookie;
    private OnCookieCallback mOnCookieCallback;
    protected ProgressBar mProgressBar;
    protected WebSettings mWebSettings;
    protected List<String[]> mFormInputValueList;
    protected boolean isLoginLoaded;
    protected boolean isLoginSuc;
    private WebChromeClient mWebChromeClient = new WebChromeClient(){

    };
    private WebViewClient mWebViewClient = new WebViewClient(){

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.e("bbbbbbb", "url: "+url);
            onObjectLoaded(url);
            return super.shouldInterceptRequest(view, url);
        }




        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("aaaaa", "url: "+url);
            if(url.equals(mUrl) && !isLoginLoaded){
                isLoginLoaded = true;
                if(!LiveLoginWebView.this.hasJq()){
                    LiveLoginWebView.this.loadJq();
                }
                LiveLoginWebView.this.loadJs(LiveLoginWebView.this.onInjectJs());
                LiveLoginWebView.this.setFormInputValues();
                onLoginPageLoaded();
                LiveLoginWebView.this.setProgressBarVisable(View.GONE);
//                LiveLoginWebView.this.setVisibility(View.VISIBLE);
            }else{
                onPageLoaded(url);
            }
        }
    };

    public void setOnCookieCallback(OnCookieCallback cb){
        mOnCookieCallback = cb;
    }

    public void setProgressBar(ProgressBar pb){
        mProgressBar = pb;
    }

    protected void setProgressBarVisable(int visable){
        if(mProgressBar!=null) mProgressBar.setVisibility(visable);
    }



    public void load(){

        isLoginLoaded = false;
        isLoginSuc = false;

        //enable javascript
        mWebSettings = this.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setUserAgentString(isMobile()?USER_AGENT_MOBILE: USER_AGENT_PC);

        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setAllowFileAccess(true); // 允许访问文件
        mWebSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        mWebSettings.setSupportZoom(false); // 支持缩放
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAllowUniversalAccessFromFileURLs(true);



//        mWebSettings.setSavePassword(true);
//        mWebSettings.setSaveFormData(true);

        loadFormInputValueList();

        //set some action
        this.setWebViewClient(mWebViewClient);
        this.setWebChromeClient(mWebChromeClient);

        mUrl = onGetUrl();

//        this.setVisibility(View.INVISIBLE);
        this.setProgressBarVisable(View.VISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().removeSessionCookies(null);
            LiveLoginWebView.this.loadUrl(mUrl);
        }else{
            CookieManager.getInstance().removeSessionCookie();
            CookieManager.getInstance().removeAllCookie();
            LiveLoginWebView.this.loadUrl(mUrl);
        }

    }

    protected abstract String onGetUrl();

    protected abstract void onLoginPageLoaded();
    protected abstract String[] onInjectJs();

    protected abstract String onGetName();

    protected void loadFormInputValueList(){
        mFormInputValueList = new ArrayList<>();
        try{
            String dataFileDir = mContext.getFilesDir().getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(dataFileDir + "/live_account/" + onGetName()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line= br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            String content = sb.toString();
            JSONArray list = new JSONArray(content);
            for(int i=0; i<list.length(); i++){
                JSONArray item = list.getJSONArray(i);
                mFormInputValueList.add(new String[]{
                        item.getString(0),
                        item.getString(1)
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onFormInputChanged(String input, String value){
        //更新到list中，注意处理重复的问题
        int index = -1;
        for(int i=0; i<mFormInputValueList.size(); i++){
            if(mFormInputValueList.get(i)[0].equals(input)){ index = i; break; }
        }
        if(index==-1){
            mFormInputValueList.add(new String[]{input, value});
        }else{
            mFormInputValueList.get(index)[1] = value;
        }
        saveFormInputValueList();
    }

    protected void saveFormInputValueList(){
        try {
            String dataFileDir = mContext.getFilesDir().getAbsolutePath();
            File dir = new File(dataFileDir + "/live_account");
            if(!dir.exists()){
                dir.mkdirs();
            }

            FileOutputStream outStream = new FileOutputStream(dataFileDir + "/live_account/" + onGetName());
            JSONArray list = new JSONArray();

            for(int i=0; i<mFormInputValueList.size(); i++){
                JSONArray item = new JSONArray();
                item.put(mFormInputValueList.get(i)[0]);
                item.put(mFormInputValueList.get(i)[1]);
                list.put(item);
            }

            outStream.write(list.toString().getBytes());
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadJs(String... js){
        for(int i=0; i<js.length; i++){
            this.loadUrl("javascript:"+js[i]);
        }
    }

    protected void setFormInputValues(){
        for(int i=0; i<mFormInputValueList.size(); i++){
            this.setFormInputValue(mFormInputValueList.get(i)[0], mFormInputValueList.get(i)[1]);
        }
    }

    protected void setFormInputValue(String input, String value){
        this.loadJs(String.format("$('%s').val('%s');", input, value));
        this.loadJs(String.format("$('%s').eq(0).change();", input));
        this.loadJs(String.format("$('%s').eq(0).focus();", input));
        this.loadJs(String.format("$('%s').eq(0).blur();", input));
    }

    protected void onLoginSuc(final String url){
        if(!isLoginSuc) {
            isLoginSuc = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!isLoginLoaded) {
                        LiveLoginWebView.this.setProgressBarVisable(View.GONE);
                    }
                    LiveLoginWebView.this.setVisibility(View.INVISIBLE);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookie = cookieManager.getCookie(url);
                    Log.e("aaaa", "aaa: "+cookie);
                    if (mOnCookieCallback != null) mOnCookieCallback.onCookie(cookie);
                }
            });
        }
    }

    protected void loadJq(){
        try {
            InputStream is = mContext.getAssets().open("jquery.js");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "UTF-8");
            this.loadJs(text);
        } catch (Exception e) {
            // Should never happen!
            throw new RuntimeException(e);
        }
    }

    protected void loadCss(){
        try {
            InputStream is = mContext.getAssets().open("live_login_css/"+onGetName()+".css");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "UTF-8");
            String js = "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    "style.appendChild(document.createTextNode('" + text +"'));" +
                    "document.getElementsByTagName('head')[0].appendChild(style);";
            loadJs(js);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected abstract boolean hasJq();

    protected abstract void onPageLoaded(String url);

    protected abstract boolean isMobile();

    protected abstract void onObjectLoaded(String url);
}
