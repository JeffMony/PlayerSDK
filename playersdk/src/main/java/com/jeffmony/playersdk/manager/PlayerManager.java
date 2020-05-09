package com.jeffmony.playersdk.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private static PlayerManager sInstance = null;
    private Map<Integer, PlayerInvokeInfo> mPlayerInvokeMap;
    private IPlayerInstanceListener mListener;
    private PlayerConfig mConfig;

    public static PlayerManager getInstance() {
        if (sInstance == null) {
            synchronized (PlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new PlayerManager();
                }
            }
        }
        return sInstance;
    }

    private PlayerManager() {
        mPlayerInvokeMap = new ConcurrentHashMap<>();
    }

    public void initConfig(PlayerConfig config) {
        mConfig = config;
    }

    public void addGlobalPlayerInstanceListener(IPlayerInstanceListener listener) {
        mListener = listener;
    }

    public synchronized void addPlayer(int hashCode, String stack) {
        PlayerInvokeInfo invokeInfo = new PlayerInvokeInfo(hashCode);
        invokeInfo.setStack(stack);
        mPlayerInvokeMap.put(hashCode, invokeInfo);

        if (mListener != null) {
            mListener.onPlayerCount(mPlayerInvokeMap.size());
        }
    }

    public synchronized void removePlayer(int hashCode) {
        mPlayerInvokeMap.remove(hashCode);
        if (mListener != null) {
            mListener.onPlayerCount(mPlayerInvokeMap.size());
        }
    }


    public static class Builder {
        private int mLimitCount;

        public Builder setLimitCount(int count) {
            mLimitCount = count;
            return this;
        }

        public PlayerConfig buildConfig() {
            return new PlayerConfig(mLimitCount);
        }

    }

}
