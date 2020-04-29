package com.jeffmony.playersdk.videoinfo;

public class M3U8Seg {
  private String mUrl;
  private String mResolution;

  public M3U8Seg(String url) {
    mUrl = url;
  }

  public M3U8Seg(String url, String resolution) {
    mUrl = url;
    mResolution = resolution;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setResolution(String resolution) {
    mResolution = resolution;
  }

  public String getResolution() {
    return mResolution;
  }
}
