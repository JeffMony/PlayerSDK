package com.jeffmony.playerdemo;

import android.app.Application;

import com.jeffmony.playersdk.LogUtils;
import com.jeffmony.playersdk.manager.IPlayerInstanceListener;
import com.jeffmony.playersdk.manager.PlayerConfig;
import com.jeffmony.playersdk.manager.PlayerManager;

import xcrash.XCrash;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XCrash.init(this);
        PlayerConfig config = new PlayerManager.Builder().setLimitCount(6).buildConfig();
        PlayerManager.getInstance().initConfig(config);
        PlayerManager.getInstance().addGlobalPlayerInstanceListener(mListener);
    }

    private IPlayerInstanceListener mListener = new IPlayerInstanceListener() {
        @Override
        public void onPlayerCount(int count) {
            LogUtils.e("onPlayerCount count=" + count);
        }

        @Override
        public void onExceedLimit() {
            LogUtils.e("onExceedLimit report info");
        }
    };

}
