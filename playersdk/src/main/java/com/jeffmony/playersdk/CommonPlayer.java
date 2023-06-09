package com.jeffmony.playersdk;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

import com.jeffmony.playersdk.impl.ExoPlayerImpl;
import com.jeffmony.playersdk.impl.IjkPlayerImpl;
import com.jeffmony.playersdk.impl.PlayerImpl;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public class CommonPlayer implements IPlayer {

    private PlayerImpl mPlayerImpl;

    public CommonPlayer(Context context) {
        this(context, PlayerType.EXO_PLAYER);
    }

    public CommonPlayer(Context context, PlayerType type) {
        this(context, type, null);
    }

    public CommonPlayer(Context context, PlayerType type, PlayerParams params) {
        if (type == PlayerType.EXO_PLAYER) {
            mPlayerImpl = new ExoPlayerImpl(context, params);
        } else if (type == PlayerType.IJK_PLAYER) {
            mPlayerImpl = new IjkPlayerImpl(context, params);
        }
    }

    @Override
    public void setSonicVolume(float volume) {
        mPlayerImpl.setSonicVolume(volume);
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mPlayerImpl.setDataSource(path);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        mPlayerImpl.setDataSource(fd);
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mPlayerImpl.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mPlayerImpl.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mPlayerImpl.setDataSource(fd, offset, length);
    }

    @Override
    public void setSurface(Surface surface) {
        mPlayerImpl.setSurface(surface);
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        mPlayerImpl.setOnPreparedListener(listener);
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        mPlayerImpl.setOnVideoSizeChangedListener(listener);
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        mPlayerImpl.setOnErrorListener(listener);
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        mPlayerImpl.setOnCompletionListener(listener);
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        mPlayerImpl.setOnSeekCompleteListener(listener);
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mPlayerImpl.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mPlayerImpl.start();
    }

    @Override
    public void pause() throws IllegalStateException {
        mPlayerImpl.pause();
    }

    @Override
    public void setSpeed(float speed) {
        mPlayerImpl.setSpeed(speed);
    }

    @Override
    public void stop() throws IllegalStateException {
        mPlayerImpl.stop();
    }

    @Override
    public void reset() {
        mPlayerImpl.reset();
    }

    @Override
    public void release() {
        mPlayerImpl.release();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mPlayerImpl.seekTo(msec);
    }

    @Override
    public void setLooping(boolean isLooping) {
        mPlayerImpl.setLooping(isLooping);
    }

    @Override
    public boolean isLooping() {
        return mPlayerImpl.isLooping();
    }

    @Override
    public long getCurrentPosition() {
        return mPlayerImpl.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mPlayerImpl.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayerImpl.isPlaying();
    }
}
