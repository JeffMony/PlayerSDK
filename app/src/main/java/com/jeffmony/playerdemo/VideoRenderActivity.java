package com.jeffmony.playerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class VideoRenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_render);
        EditText urlText = findViewById(R.id.video_url_text);
        Button textureViewBtn = findViewById(R.id.texture_view_btn);
        Button surfaceViewBtn = findViewById(R.id.surface_view_btn);
        urlText.setText("http://videoconverter.vivo.com.cn/201706/655_1498479540118.mp4.f40.m3u8");

        textureViewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(VideoRenderActivity.this, TextureViewActivity.class);
            intent.putExtra("video_uri", urlText.getText().toString());
            startActivity(intent);
        });

        surfaceViewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(VideoRenderActivity.this, SurfaceViewActivity.class);
            intent.putExtra("video_uri", urlText.getText().toString());
            startActivity(intent);
        });
    }

}