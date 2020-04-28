package com.jeffmony.playersdk.callback;

import com.jeffmony.playersdk.videoinfo.M3U8Seg;

import java.util.List;

public interface IVideoInfoCallback {
  void onVideoType(String contentType, String name);
  void onMutipleVideo(List<M3U8Seg> urlList);
}
