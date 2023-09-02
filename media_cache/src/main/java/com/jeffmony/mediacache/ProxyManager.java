package com.jeffmony.mediacache;

public class ProxyManager {

    private long mHandler = 0;

    private static volatile ProxyManager sProxyManager = null;

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
    private native void start(long handler);
    private native void close(long handler);
    private native String getProxyUrl(long handler, String url);
}
