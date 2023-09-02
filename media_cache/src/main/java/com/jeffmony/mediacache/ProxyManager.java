package com.jeffmony.mediacache;

import android.text.TextUtils;

import java.io.File;

public class ProxyManager {

    private long mHandler = 0;

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

    public void start() {
        start(mHandler);
    }

    public void close() {
        close(mHandler);
    }

    public String getProxyUrl(String url) {
        return getProxyUrl(mHandler, url);
    }

    private native long createHandler();
    private native void initConfig(long handler, ProxyConfig config);
    private native void start(long handler);
    private native void close(long handler);
    private native String getProxyUrl(long handler, String url);
}
