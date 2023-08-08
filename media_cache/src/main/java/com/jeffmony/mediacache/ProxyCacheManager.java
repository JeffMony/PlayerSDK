package com.jeffmony.mediacache;

public class ProxyCacheManager {

    private long mHandler = 0;

    private static volatile ProxyCacheManager sProxyCacheManager = null;

    public static ProxyCacheManager getInstance() {
        if (sProxyCacheManager == null) {
            synchronized (ProxyCacheManager.class) {
                if (sProxyCacheManager == null) {
                    sProxyCacheManager = new ProxyCacheManager();
                }
            }
        }
        return sProxyCacheManager;
    }

    private ProxyCacheManager() {
        mHandler = createHandler();
    }

    public String getProxyUrl(String url) {
        return getProxyUrl(mHandler, url);
    }

    private native long createHandler();
    private native String getProxyUrl(long handler, String url);
}
