package com.jeffmony.playersdk.manager;

public interface IPlayerInstanceListener {
    void onPlayerCount(int count);
    void onExceedLimit();
}
