package com.jeffmony.playersdk.videoinfo;

import android.net.Uri;
import android.text.TextUtils;

import com.jeffmony.playersdk.LogUtils;
import com.jeffmony.playersdk.callback.IVideoInfoCallback;
import com.jeffmony.playersdk.component.HttpClientManager;
import com.jeffmony.playersdk.thread.WorkerThreadManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public void parseVideoInfo(String url, IVideoInfoCallback videoInfoCallback) {
        if (videoInfoCallback == null || TextUtils.isEmpty(url)) {
            return;
        }
        WorkerThreadManager.submitRunnableTask(() -> {
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Response response = null;
            try {
                response = HttpClientManager.getInstance().getClient().newCall(builder.build()).execute();
                if (response != null) {
                    String contentType = response.header("content-type");
                    if (VideoType.isM3U8(contentType)) {
                        videoInfoCallback.onVideoType(contentType, VideoType.M3U8);
                        parseM3U8Info(url, response, videoInfoCallback);
                    }
                }
            } catch (Exception e) {
                LogUtils.w(TAG + " parseVideoInfo failed, exception="+e.getMessage());
                videoInfoCallback.onFailed(e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        });
    }

    private void parseM3U8Info(String url, Response response, IVideoInfoCallback videoInfoCallback) throws Exception {
        M3U8 m3u8 = new M3U8(url);
        String baseUrl = url.substring(0, url.lastIndexOf("/") + 1);
        String hostUrl = url.substring(0, url.indexOf((new URL(url)).getPath()) + 1);
        m3u8.setHostUrl(hostUrl);
        m3u8.setBaseUrl(baseUrl);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
        List<M3U8Seg> segList = new ArrayList<>();
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#EXT")) {
                continue;
            }
            if (line.endsWith(".m3u8")) {
                M3U8Seg seg;
                if (line.startsWith("https") || line.startsWith("http")) {
                    seg = new M3U8Seg(line);
                } else if (line.startsWith("/")) {
                    int tempIndex = line.indexOf('/', 1);
                    String tempUrl = line.substring(0, tempIndex);
                    tempIndex = url.indexOf(tempUrl);
                    tempUrl = url.substring(0, tempIndex) + line;
                    seg = new M3U8Seg(tempUrl);
                } else {
                    seg = new M3U8Seg(baseUrl + line);
                }
                segList.add(seg);
            }
        }

        convertM3U8Seg(segList);

        m3u8.setCount(segList.size());
        m3u8.setSegList(segList);
        videoInfoCallback.onMutipleVideo(segList);
        if (bufferedReader != null) {
            bufferedReader.close();
        }

    }

    private void convertM3U8Seg(List<M3U8Seg> segList) {
        if (segList.size() == 0 || segList.size() == 1) {
            return;
        }
        if (segList.size() == 2) {
            segList.get(0).setResolution(M3U8Constants.RESOLUTION_1);
            segList.get(1).setResolution(M3U8Constants.RESOLUTION_2);
        } else if (segList.size() == 3) {
            segList.get(0).setResolution(M3U8Constants.RESOLUTION_1);
            segList.get(1).setResolution(M3U8Constants.RESOLUTION_2);
            segList.get(2).setResolution(M3U8Constants.RESOLUTION_3);
        } else if (segList.size() == 4) {
            segList.get(0).setResolution(M3U8Constants.RESOLUTION_1);
            segList.get(1).setResolution(M3U8Constants.RESOLUTION_2);
            segList.get(2).setResolution(M3U8Constants.RESOLUTION_3);
            segList.get(3).setResolution(M3U8Constants.RESOLUTION_4);
        } else if (segList.size() == 5) {
            segList.get(0).setResolution(M3U8Constants.RESOLUTION_1);
            segList.get(1).setResolution(M3U8Constants.RESOLUTION_2);
            segList.get(2).setResolution(M3U8Constants.RESOLUTION_3);
            segList.get(3).setResolution(M3U8Constants.RESOLUTION_4);
            segList.get(4).setResolution(M3U8Constants.RESOLUTION_5);
        }
    }

}
