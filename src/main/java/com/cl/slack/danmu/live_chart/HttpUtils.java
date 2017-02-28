package com.cl.slack.danmu.live_chart;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by xiaozhuai on 17/2/19.
 */
public class HttpUtils {

    public static String httpGet(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new RuntimeException("http get request failed");
        }
        return response.body().string();
    }

    public static byte[] httpGetBytes(String url) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new RuntimeException("http get request failed");
        }
        return response.body().bytes();
    }
}
