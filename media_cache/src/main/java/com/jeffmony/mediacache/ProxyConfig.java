package com.jeffmony.mediacache;

public class ProxyConfig {

    private String mCacheDir;
    private long mMaxSize;
    private long mExpireTime;
    private CacheCleanStrategy mStrategy;

    public ProxyConfig(String cacheDir, long maxSize, long expireTime, CacheCleanStrategy strategy) {
        mCacheDir = cacheDir;
        mMaxSize = maxSize;
        mExpireTime = expireTime;
        mStrategy = strategy;
    }

    public String getCacheDir() {
        return mCacheDir;
    }

    public long getExpireTime() {
        return mExpireTime;
    }

    public long getMaxSize() {
        return mMaxSize;
    }

    public int getCacheCleanStrategy() {
        return mStrategy.ordinal();
    }
}
