package com.jeffmony.playersdk.manager;

public class PlayerConfig {

    private int mLimitCount;

    public PlayerConfig(int count) {
        mLimitCount = count;
    }

    public int limitCount() {
        return mLimitCount;
    }
}
