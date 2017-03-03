package com.cl.slack.danmu.live_chart;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by xiaozhuai on 17/2/19.
 */
public class HttpUtils {

    private final static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static String httpGet(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        return getRequestString(request);
    }

    /**
     * 带有 cookie 的 访问
     * @param url url
     * @param cookie cookie
     * @return
     * @throws Exception
     */
    public static String httpGet(String url,String cookie) throws Exception {
        Request request = new Request.Builder().url(url)
                .addHeader("cookie",cookie)
                .build();
        return getRequestString(request);
    }

    /**
     * post json
     */
    public static String httpPostJson(String url, String json, String cookie) throws Exception {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Request request = new Request.Builder().url(url)
                .addHeader("cookie",cookie)
                .post(body)
                .build();
        return getRequestString(request);
    }

    /**
     * post form 表单
     */
    public static String httpPostFrom(String url, HashMap<String,String> form, String cookie) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : form.keySet()) {
            builder.add(key, form.get(key));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .addHeader("cookie",cookie)
                .post(body)
                .build();
        return getRequestString(request);
    }

    private static String getRequestString(Request request) throws Exception {
        Response response = mOkHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new RuntimeException("http get request failed");
        }
        return response.body().string();
    }

    public static byte[] httpGetBytes(String url) throws Exception{
        Request request = new Request.Builder().url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new RuntimeException("http get request failed");
        }
        return response.body().bytes();
    }
}
