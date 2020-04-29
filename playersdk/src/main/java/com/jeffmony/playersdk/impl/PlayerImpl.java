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
    private OnCompletionListener mOnCompleteListener;
    private PlayerParams mParams;

    protected String mUrl;

    public PlayerImpl(Context context, PlayerParams params) {
        mParams = params;
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
        mOnPreparedListener = listener;
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mOnVideoSizeChangedListener = listener;
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompleteListener = listener;
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
    public void reset() {

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
