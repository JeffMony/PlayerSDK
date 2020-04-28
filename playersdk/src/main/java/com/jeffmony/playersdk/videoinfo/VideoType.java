package com.jeffmony.playersdk.videoinfo;

public class VideoType {

  public static String M3U8_1 = "applicationnd.apple.mpegurl";
  public static String M3U8_2 = "application/vnd.apple.mpegurl";
  public static String M3U8_3 = "application/x-mpegurl";
  public static String M3U8_4 = "vnd.apple.mpegurl";
  public static String M3U8_5 = "application/x-mpeg";

  public static boolean isM3U8(String contentType) {
    return VideoType.M3U8_1.equals(contentType) ||
            VideoType.M3U8_2.equals(contentType) ||
            VideoType.M3U8_3.equals(contentType) ||
            VideoType.M3U8_4.equals(contentType) ||
            VideoType.M3U8_5.equals(contentType);
  }

  public static String M3U8 = "m3u8";
}
