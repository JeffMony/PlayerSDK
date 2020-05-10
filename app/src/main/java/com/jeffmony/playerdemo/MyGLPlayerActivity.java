package com.jeffmony.playerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.playersdk.glplayer.GLPlayer;

public class MyGLPlayerActivity extends AppCompatActivity {

    private GLPlayer mGLPlayer;
    private LinearLayout mParentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glplayer);

        mGLPlayer = new GLPlayer(this);
        mGLPlayer.setDataSource(this, Uri.parse("http://videoconverter.vivo.com.cn/201706/655_1498479540118.mp4.main.m3u8"));

        mParentLayout = findViewById(R.id.parent_layout);
        View glVideoSurfaceView = mGLPlayer.getVideoSurfaceView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParentLayout.addView(glVideoSurfaceView, params);
    }


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
