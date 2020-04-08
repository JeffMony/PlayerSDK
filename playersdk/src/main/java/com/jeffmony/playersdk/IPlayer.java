package com.jeffmony.playersdk;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface IPlayer {

    void setDataSource(Context context, Uri uri)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(String path)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(Context context, Uri uri, Map<String, String> headers)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(FileDescriptor fd)
            throws IOException, IllegalArgumentException, IllegalStateException;

    void setDataSource(FileDescriptor fd, long offset, long length)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setSurface(Surface surface);

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    void pause() throws IllegalStateException;

    void setSpeed(float speed);

    void release();

    void seekTo(long msec) throws IllegalStateException;

    void setLooping(boolean isLooping);

    boolean isLooping();

    long getCurrentPosition();

    long getDuration();

    boolean isPlaying();

    void setOnPreparedListener(OnPreparedListener listener);

    void setOnVideoSizeChangedListener(
            OnVideoSizeChangedListener listener);

    void setOnErrorListener(OnErrorListener listener);

    interface OnPreparedListener {
        void onPrepared(IPlayer mp);
    }

    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IPlayer mp, int width, int height,
                                int rotationDegree,
                                float pixelRatio,
                                float darRatio);
    }

    interface OnErrorListener {
        void onError(IPlayer mp, int what, String msg);
    }
}
