package com.jeffmony.playersdk.utils;

import android.text.TextUtils;

public class HttpUtils {

  public static boolean isLocalFile(String url) {
    if (TextUtils.isEmpty(url))
      return false;
    if (url.startsWith("https") || url.startsWith("http"))
      return false;
    if (url.startsWith("/sdcard") || url.startsWith("file:///") || url.startsWith("/storage"))
      return true;
    return false;
  }
}
