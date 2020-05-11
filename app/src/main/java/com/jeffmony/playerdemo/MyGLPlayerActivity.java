package com.jeffmony.playerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.glplayer.GLPlayer;
import com.jeffmony.playersdk.render.DefaultVideoRenderer;
import com.jeffmony.playersdk.view.VideoGLSurfaceView;

public class MyGLPlayerActivity extends AppCompatActivity {

    private GLPlayer mGLPlayer;
    private VideoGLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glplayer);
        mGLSurfaceView = findViewById(R.id.video_surface_view);
        mGLPlayer = new GLPlayer(this);
        mGLPlayer.setRenderer(mGLSurfaceView, new DefaultVideoRenderer(this));
        mGLPlayer.setDataSource(this, Uri.parse("http://videoconverter.vivo.com.cn/201706/655_1498479540118.mp4.main.m3u8"));
        mGLPlayer.prepareAsync();
        mGLPlayer.setOnPreparedListener(mPreparedListener);
        mGLPlayer.setOnVideoSizeChangedListener(mVideoSizeChangeListener);
    }

    private IPlayer.OnVideoSizeChangedListener mVideoSizeChangeListener = new IPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IPlayer mp, int width, int height, int rotationDegree, float pixelRatio, float darRatio) {
            mGLPlayer.updateVideoSize(width, height);
        }
    };

    private IPlayer.OnPreparedListener mPreparedListener = new IPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IPlayer mp) {
            mGLPlayer.start();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mGLPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGLPlayer.release();
    }
}
