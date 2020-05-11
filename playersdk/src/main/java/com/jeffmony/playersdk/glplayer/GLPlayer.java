package com.jeffmony.playersdk.glplayer;

import android.content.Context;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.PlayerParams;
import com.jeffmony.playersdk.PlayerType;
import com.jeffmony.playersdk.impl.ExoPlayerImpl;
import com.jeffmony.playersdk.impl.IjkPlayerImpl;
import com.jeffmony.playersdk.render.BaseVideoRenderer;
import com.jeffmony.playersdk.view.VideoGLSurfaceView;

import java.io.FileDescriptor;
import java.util.Map;

public class GLPlayer {

    private IPlayer mPlayer;
    private BaseVideoRenderer mRenderer;

    public GLPlayer(Context context) {
        this(context, PlayerType.EXO_PLAYER);
    }

    public GLPlayer(Context context, PlayerType type) {
        this(context, type, null);
    }

    public GLPlayer(Context context, PlayerType type, PlayerParams params) {
        if (type == PlayerType.EXO_PLAYER) {
            mPlayer = new ExoPlayerImpl(context, params);
        } else if (type == PlayerType.IJK_PLAYER) {
            mPlayer = new IjkPlayerImpl(context, params);
        } else {
            return;
        }
    }

    public void setRenderer(VideoGLSurfaceView glSurfaceView, BaseVideoRenderer renderer) {
        glSurfaceView.setRendererEngine(renderer);
        mRenderer = renderer;
        renderer.setGLPlayer(this);
    }

    public void setDataSource(Context context, Uri uri) {
        try {
            mPlayer.setDataSource(context, uri);
        } catch (Exception e) {

        }
    }

    public void setDataSource(String path) {
        try {
            mPlayer.setDataSource(path);
        } catch (Exception e) {

        }
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> headers) {
        try {
            mPlayer.setDataSource(context, uri, headers);
        } catch (Exception e) {

        }
    }

    public void setDataSource(FileDescriptor fd) {
        try {
            mPlayer.setDataSource(fd);
        } catch (Exception e) {

        }
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) {
        try {
            mPlayer.setDataSource(fd, offset, length);
        } catch (Exception e) {

        }
    }

    public void setSurface(Surface surface) {
        mPlayer.setSurface(surface);
    }

    public void prepareAsync() {
        mPlayer.prepareAsync();
    }

    public void start() {
        mPlayer.start();
    }

    public void setOnVideoSizeChangedListener(IPlayer.OnVideoSizeChangedListener listener) {
        mPlayer.setOnVideoSizeChangedListener(listener);
    }

    public void updateVideoSize(int width, int height) {
        mRenderer.updateVideoSize(width, height);
    }

    public void setOnPreparedListener(IPlayer.OnPreparedListener listener) {
        mPlayer.setOnPreparedListener(listener);
    }

    public void pause() {
        mPlayer.pause();
    }

    public void stop() {
        mPlayer.stop();
    }

    public void reset() {
        mPlayer.reset();
    }

    public void release() {
        mPlayer.release();
    }

    public float getSurfaceHeightPx() {
        return mRenderer.getSurfaceHeightPx();
    }

    public IPlayer getPlayer() {
        return mPlayer;
    }
}
