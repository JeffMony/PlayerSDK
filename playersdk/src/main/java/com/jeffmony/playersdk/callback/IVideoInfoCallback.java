package com.jeffmony.playersdk.callback;

import com.jeffmony.playersdk.videoinfo.M3U8Seg;

import java.util.List;

public interface IVideoInfoCallback {
  void onVideoType(String contentType, String name);
  void onMultipleVideo(List<M3U8Seg> urlList);
  void onFailed(Exception e);
}
