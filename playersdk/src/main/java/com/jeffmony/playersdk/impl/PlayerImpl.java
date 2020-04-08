package com.jeffmony.playersdk.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;

import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.PlayerParams;
import com.jeffmony.playersdk.WeakHandler;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public class PlayerImpl implements IPlayer {

    private OnPreparedListener mOnPreparedListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private OnErrorListener mOnErrorListener;

    private Context mContext;
    protected String mUrl;

    protected String mOriginUrl;

    //Player settings
    protected boolean mVideoCacheSwitch = false;

    private WeakHandler mHander = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return true;
        }
    });

    public PlayerImpl(Context context, PlayerParams attributes) {
        mContext = context;
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {

    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setSurface(Surface surface) {

    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
    }

    @Override
    public void start() throws IllegalStateException {
    }

    @Override
    public void pause() throws IllegalStateException {
    }

    @Override
    public void setSpeed(float speed) {
    }

    @Override
    public void stop() throws IllegalStateException {

    }

    @Override
    public void release() {
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    protected void notifyOnPrepared() {
        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared(this);
        }
    }

    protected void notifyOnVideoSizeChanged(int width, int height,
                                            int rotationDegree,
                                            float pixelRatio,
                                            float darRatio) {
        if (mOnVideoSizeChangedListener != null) {
            mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, rotationDegree, pixelRatio, darRatio);
        }
    }

    protected void notifyOnError(int what, String msg) {
        if (mOnErrorListener != null) {
            mOnErrorListener.onError(this, what, msg);
        }
    }

}
