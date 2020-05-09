package com.jeffmony.playersdk.manager;

import androidx.annotation.Nullable;

public class PlayerInvokeInfo {

    private int mHashCode;
    private String mStack;
    private long mTime;

    public PlayerInvokeInfo(int hashCode) {
        mHashCode = hashCode;
        mTime = System.currentTimeMillis();
    }

    public void setStack(String stack) {
        mStack = stack;
    }

    public int hashCode() {
        return mHashCode;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null && obj instanceof PlayerInvokeInfo) {
            int hashCode = ((PlayerInvokeInfo) obj).hashCode();
            if (hashCode == mHashCode) {
                return true;
            }
        }
        return false;
    }
}
