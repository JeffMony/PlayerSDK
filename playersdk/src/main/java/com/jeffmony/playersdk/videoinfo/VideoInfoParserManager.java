package com.jeffmony.playersdk.videoinfo;

import com.jeffmony.playersdk.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoInfoParserManager {

    private static final String TAG = "VideoInfoParserManager";

    private static VideoInfoParserManager sInstance;

    public static VideoInfoParserManager getInstance() {
        if (sInstance == null) {
            synchronized (VideoInfoParserManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoInfoParserManager();
                }
            }
        }
        return sInstance;
    }

    public void parseVideoInfo(String url) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES));
        OkHttpClient client = clientBuilder.build();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Response response = null;
        try {
            response = client.newCall(builder.build()).execute();
            LogUtils.i("parseVideoInfo code="+response.code());
        } catch (Exception e) {
            LogUtils.w(TAG + " parseVideoInfo failed, exception="+e.getMessage());
        }

    }

}
