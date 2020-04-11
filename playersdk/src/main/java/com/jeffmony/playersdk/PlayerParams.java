package com.jeffmony.playersdk;

public class PlayerParams {

    private boolean mUseOkHttp;

    public PlayerParams() { }

    public void setUseOkHttp(boolean useOkhttp) { mUseOkHttp = useOkhttp; }

    public boolean useOkHttp() { return mUseOkHttp; }
}
