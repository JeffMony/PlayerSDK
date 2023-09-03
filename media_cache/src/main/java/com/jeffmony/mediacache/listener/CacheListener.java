package com.jeffmony.mediacache.listener;

import java.io.File;

public interface CacheListener {

    void onCacheProgress(String url, File file, float progress);
}
