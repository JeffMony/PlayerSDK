package com.jeffmony.mediacache;

public class ProxyCacheManager {

    private long mHandler = 0;

    private static volatile ProxyCacheManager sProxyCacheManager = null;

    private static volatile boolean sLoadLibrary = false;

    public static ProxyCacheManager getInstance() {
        if (sProxyCacheManager == null) {
            synchronized (ProxyCacheManager.class) {
                if (sProxyCacheManager == null) {
                    loadLibrary();
                    sProxyCacheManager = new ProxyCacheManager();
                }
            }
        }
        return sProxyCacheManager;
    }

    private ProxyCacheManager() {
        mHandler = createHandler();
    }

    private static void loadLibrary() {
        if (sLoadLibrary) {
            return;
        }
        System.loadLibrary("c++_shared");
        System.loadLibrary("mediacache");
        sLoadLibrary = true;
    }

    public String getProxyUrl(String url) {
        return getProxyUrl(mHandler, url);
    }

    private native long createHandler();
    private native String getProxyUrl(long handler, String url);
}
