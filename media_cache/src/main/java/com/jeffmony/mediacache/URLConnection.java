package com.jeffmony.mediacache;

public class URLConnection implements IConnection {

    private long mHandler = 0;

    public URLConnection() {
        mHandler = createHandler();
    }

    @Override
    public void start(String url) {
        if (mHandler == 0) {
            return;
        }
        start(mHandler, url);
    }

    @Override
    public void close() {
        if (mHandler == 0) {
            return;
        }
        close(mHandler);
    }

    private native long createHandler();
    private native void start(long handler, String url);
    private native void close(long handler);

}
