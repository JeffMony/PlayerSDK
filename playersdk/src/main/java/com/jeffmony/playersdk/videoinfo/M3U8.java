package com.jeffmony.playersdk.videoinfo;

import java.util.List;

public class M3U8 {
  private String mUrl;
  private String mHostUrl;
  private String mBaseUrl;
  private int mCount;
  private List<M3U8Seg> mSegList;

  public M3U8(String url) {
    mUrl = url;
  }

  public void setHostUrl(String hostUrl) {
    mHostUrl = hostUrl;
  }

  public void setBaseUrl(String baseUrl) {
    mBaseUrl = baseUrl;
  }

  public void setCount(int count) {
    mCount = count;
  }

  public void setSegList(List<M3U8Seg> segList) {
    mSegList = segList;
  }
}
