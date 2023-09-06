package com.jeffmony.mediacache;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.jeffmony.mediacache.listener.CacheListener;

import java.io.File;

public class ProxyManager {

    private long mHandler = 0;

    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static volatile ProxyManager sProxyManager = null;

    public static final class Builder {

        private String mCacheDir;
        private long mMaxSize = 1024 * 1024 * 1024L;
        private long mExpireTime = 2 * 24 * 60 * 60 * 1000L;
        private CacheCleanStrategy mStrategy = CacheCleanStrategy.LRU;

        public Builder() {

        }

        public Builder setMaxSize(long maxSize) {
            if (maxSize <= 0) {
                throw new RuntimeException("Cache max size must be positive");
            }
            mMaxSize = maxSize;
            return this;
        }

        public Builder setExpireTime(long expireTime) {
            if (expireTime <= 0) {
                throw new RuntimeException("Cache expire time must be positive");
            }
            mExpireTime = expireTime;
            return this;
        }

        public Builder setCacheCleanStrategy(CacheCleanStrategy strategy) {
            mStrategy = strategy;
            return this;
        }

        public Builder setCacheDir(String cacheDir) {
            if (TextUtils.isEmpty(cacheDir)) {
                throw new RuntimeException("Cache dir must not empty");
            }
            File file = new File(cacheDir);
            if (!file.exists()) {
                throw new RuntimeException("Cache dir must exist");
            }
            mCacheDir = cacheDir;
            return this;
        }

        public ProxyConfig build() {
            return new ProxyConfig(mCacheDir, mMaxSize, mExpireTime, mStrategy);
        }
    }

    public static ProxyManager getInstance() {
        if (sProxyManager == null) {
            synchronized (ProxyManager.class) {
                if (sProxyManager == null) {
                    CacheSdk.load();
                    sProxyManager = new ProxyManager();
                }
            }
        }
        return sProxyManager;
    }

    private ProxyManager() {
        mHandler = createHandler();
    }

    public void initConfig(ProxyConfig config) {
        if (config == null) {
            throw new RuntimeException("ProxyConfig must be set");
        }
        initConfig(mHandler, config);
    }

    public void startProxy() {
        startProxy(mHandler);
    }

    public void close() {
        close(mHandler);
    }

    public String getProxyUrl(String url) {
        return getProxyUrl(mHandler, url);
    }

    public void stop(String url) {
        stop(mHandler, url);
    }

    public void addCacheListener(String url, CacheListener listener) {
        addCacheListener(mHandler, url, listener);
    }

    public void removeCacheListener(String url) {
        removeCacheListener(mHandler, url);
    }

    /**
     * C++ 回调Java层代码
     */
    private void onCacheProgress(String url, String filePath, float progress, CacheListener listener) {
        if (listener == null) {
            return;
        }
        mMainHandler.post(() -> {
            if (listener != null) {
                listener.onCacheProgress(url, filePath, progress);
            }
        });
    }


    /**
     * Java层调用C++函数
     */
    private native long createHandler();
    private native void initConfig(long handler, ProxyConfig config);
    private native void startProxy(long handler);
    private native void close(long handler);
    private native String getProxyUrl(long handler, String url);
    private native void stop(long handler, String url);
    private native void addCacheListener(long handler, String url, CacheListener listener);
    private native void removeCacheListener(long handler, String url);
}
