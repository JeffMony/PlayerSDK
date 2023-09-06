package com.jeffmony.mediacache.listener;

public interface CacheListener {

    void onCacheProgress(String url, String filePath, float progress);
}
