package com.jeffmony.mediacache;

public class CacheSdk {

    public static volatile boolean sLibraryLoad = false;

    public static void load() {
        if (sLibraryLoad) {
            return;
        }
        System.loadLibrary("c++_shared");
        System.loadLibrary("media_cache");
        sLibraryLoad = true;
    }

    public static IConnection createURLConnection() {
        load();
        return new URLConnection();
    }

}
