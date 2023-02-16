package com.jeffmony.playerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffmony.playersdk.CommonPlayer;
import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.PlayerParams;
import com.jeffmony.playersdk.PlayerType;
import com.jeffmony.playersdk.utils.ScreenUtils;
import com.jeffmony.videorender.effect.ColorAdjustUtils;
import com.jeffmony.videorender.IRenderProcess;
import com.jeffmony.videorender.ImageUtils;
import com.jeffmony.videorender.LogTag;
import com.jeffmony.videorender.MirrorType;
import com.jeffmony.videorender.RenderSdk;
import com.jeffmony.videorender.effect.StickerUtils;
import com.jeffmony.videorender.listener.OnCaptureListener;
import com.jeffmony.videorender.listener.OnSurfaceListener;

public class TextureViewActivity extends AppCompatActivity {

    private final static int MSG_CAPTURE_FRAME_SUCCESS = 100;
    private final static int MSG_CAPTURE_FRAME_FAILED = 101;
    private final static int MSG_INIT_PLAYER = 102;
    private TextureView mVideoTextureView;
    private IRenderProcess mRenderProcess;
    private Surface mSurface;
    private CommonPlayer mPlayer;
    private String mVideoUrl;
    private Button mPlayPauseBtn;
    private LinearLayout mBottomLayout;
    private SeekBar mAdjustSeekBar;
    private TextView mAdjustTextView;
    private int mBackgroundEffectId = -1;
    private int mStickerId = -1;
    private int mHighResolutionId = -1;
    /**
     * 调节范围：-100 ~ 100
     */
    private int mBrightId = -1;
    /**
     * 调节范围：-100 ~ 100
     */
    private int mContrastId = -1;
    /**
     * 调节范围：-100 ~ 100
     */
    private int mTemperatureId = -1;
    /**
     * 调节范围：-100 ~ 100
     */
    private int mSaturationId = -1;
    /**
     * 调节范围：0 ~ 100
     */
    private int mGrainId = -1;
    /**
     * 调节范围：0 ~ 100
     */
    private int mSharpId = -1;

    private Handler mMainHandler = new Handler(msg -> {
        int what = msg.what;
        if (what == MSG_CAPTURE_FRAME_SUCCESS) {

        } else if (what == MSG_CAPTURE_FRAME_FAILED) {

        } else if (what == MSG_INIT_PLAYER) {
            initPlayer();
        }
        return false;
    });

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mBrightId != -1) {
                int level = (progress - 50) * 2;
                String brightStr = ColorAdjustUtils.getBrightEffect(level);
                mRenderProcess.updateEffect(mBrightId, brightStr);
            } else if (mContrastId != -1) {
                int level = (progress - 50) * 2;
                String contrastStr = ColorAdjustUtils.getContrastEffect(level);
                mRenderProcess.updateEffect(mContrastId, contrastStr);
            } else if (mTemperatureId != -1) {
                int level = (progress - 50) * 2;
                String temperatureStr = ColorAdjustUtils.getTemperatureEffect(level);
                mRenderProcess.updateEffect(mTemperatureId, temperatureStr);
            } else if (mSaturationId != -1) {
                int level = (progress - 50) * 2;
                String saturationStr = ColorAdjustUtils.getSaturationEffect(level);
                mRenderProcess.updateEffect(mSaturationId, saturationStr);
            } else if (mGrainId != -1) {
                int level = progress;
                String grainStr = ColorAdjustUtils.getGrainEffect(level);
                mRenderProcess.updateEffect(mGrainId, grainStr);
            } else if (mSharpId != -1) {
                int level = progress;
                String sharpStr = ColorAdjustUtils.getSharpEffect(level);
                mRenderProcess.updateEffect(mSharpId, sharpStr);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        mVideoUrl = getIntent().getStringExtra("video_uri");
        mVideoTextureView = findViewById(R.id.video_texture_view);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mAdjustSeekBar = findViewById(R.id.adjust_seek_bar);
        mAdjustSeekBar.setMax(100);
        mAdjustTextView = findViewById(R.id.adjust_text_view);
        mAdjustSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mRenderProcess = RenderSdk.createRenderProcess();
        mRenderProcess.setTextureView(mVideoTextureView);
        mRenderProcess.setOnSurfaceListener(new OnSurfaceListener() {
            @Override
            public void onSurfaceCreated(Surface surface) {
                mSurface = surface;
                if (mBackgroundEffectId == -1) {
                    mBackgroundEffectId = mRenderProcess.addEffect("{\n" +
                            "    \"effect\":[\n" +
                            "        {\n" +
                            "            \"type\":\"background\",\n" +
                            "            \"backgroundType\":1,\n" +
                            "            \"blur\":10,\n" +
                            "            \"renderFrameType\":0,\n" +
                            "            \"z_order\":1\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}");
                }
                mMainHandler.sendEmptyMessage(MSG_INIT_PLAYER);
            }

            @Override
            public void onSurfaceChanged(int width, int height) {

            }

            @Override
            public void onSurfaceDestroy() {

            }
        });
        findViewById(R.id.reset_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            if (mStickerId != -1) {
                mRenderProcess.deleteEffect(mStickerId);
                mStickerId = -1;
            }
            if (mHighResolutionId != -1) {
                mRenderProcess.deleteEffect(mHighResolutionId);
                mHighResolutionId = -1;
            }
            if (mBrightId != -1) {
                mRenderProcess.deleteEffect(mBrightId);
                mBrightId = -1;
            }
            mRenderProcess.setMirror(MirrorType.NONE);
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
            mBottomLayout.setVisibility(View.INVISIBLE);
        });
        mPlayPauseBtn = findViewById(R.id.play_pause_btn);
        mPlayPauseBtn.setOnClickListener(v-> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayPauseBtn.setText("播放");
            } else {
                mPlayer.start();
                mPlayPauseBtn.setText("暂停");
            }
        });

        findViewById(R.id.horizontal_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            if (mRenderProcess.getMirrorType() != MirrorType.NONE) {
                mRenderProcess.setMirror(MirrorType.NONE);
            } else {
                mRenderProcess.setMirror(MirrorType.HORIZONTAL);
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.vertical_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            if (mRenderProcess.getMirrorType() != MirrorType.NONE) {
                mRenderProcess.setMirror(MirrorType.NONE);
            } else {
                mRenderProcess.setMirror(MirrorType.VERTICAL);
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.capture_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mRenderProcess.captureFrame(new OnCaptureListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    Log.i(LogTag.TAG, "captureFrame onSuccess");
                    Toast.makeText(TextureViewActivity.this, "截图成功", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {
                        if (bitmap != null) {
                            ImageUtils.saveImg(bitmap, "/sdcard/DCIM/Camera/result.jpg");
                            bitmap.recycle();
                        }
                    }).start();
                }

                @Override
                public void onError(int code) {
                    Log.e(LogTag.TAG, "captureFrame onError");
                    Toast.makeText(TextureViewActivity.this, "截图失败", Toast.LENGTH_SHORT).show();
                }
            });
        });
        findViewById(R.id.sticker_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            if (mStickerId == -1) {
                String stickerStr = StickerUtils.createStickerStr("/sdcard/Pictures/icon.png", 0.1f, 0.2f, 1.0f, 0.0f);
                mStickerId = mRenderProcess.addEffect(stickerStr);
            } else {
                mRenderProcess.deleteEffect(mStickerId);
                mStickerId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.high_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.INVISIBLE);
            if (mHighResolutionId == -1) {
                String highStr = ColorAdjustUtils.getColorEffect(10, 0, 5, 20, 0, 10);
                mHighResolutionId = mRenderProcess.addEffect(highStr);
            } else {
                mRenderProcess.deleteEffect(mHighResolutionId);
                mHighResolutionId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.bright_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整亮度");
            if (mBrightId == -1) {
                String brightStr = ColorAdjustUtils.getBrightEffect(0);
                mBrightId = mRenderProcess.addEffect(brightStr);
                mAdjustSeekBar.setProgress(50);
            } else {
                mRenderProcess.deleteEffect(mBrightId);
                mBrightId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.contrast_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整对比度");
            if (mContrastId == -1) {
                String contrastStr = ColorAdjustUtils.getContrastEffect(0);
                mRenderProcess.addEffect(contrastStr);
                mAdjustSeekBar.setProgress(50);
            } else {
                mRenderProcess.deleteEffect(mContrastId);
                mContrastId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.temperature_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整色温");
            if (mTemperatureId == -1) {
                String temperatureStr = ColorAdjustUtils.getTemperatureEffect(0);
                mTemperatureId = mRenderProcess.addEffect(temperatureStr);
                mAdjustSeekBar.setProgress(50);
            } else {
                mRenderProcess.deleteEffect(mTemperatureId);
                mTemperatureId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.saturation_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整饱和度");
            if (mSaturationId == -1) {
                String saturationStr = ColorAdjustUtils.getSaturationEffect(0);
                mSaturationId = mRenderProcess.addEffect(saturationStr);
                mAdjustSeekBar.setProgress(50);
            } else {
                mRenderProcess.deleteEffect(mSaturationId);
                mSaturationId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.sharp_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整锐度");
            if (mSharpId == -1) {
                String sharpStr = ColorAdjustUtils.getSharpEffect(0);
                mSharpId = mRenderProcess.addEffect(sharpStr);
                mAdjustSeekBar.setProgress(0);
            } else {
                mRenderProcess.deleteEffect(mSharpId);
                mSharpId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
        findViewById(R.id.grain_btn).setOnClickListener(v -> {
            if (mRenderProcess == null) {
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mAdjustTextView.setText("调整颗粒度");
            if (mGrainId == -1) {
                String grainStr = ColorAdjustUtils.getGrainEffect(0);
                mGrainId = mRenderProcess.addEffect(grainStr);
                mAdjustSeekBar.setProgress(0);
            } else {
                mRenderProcess.deleteEffect(mGrainId);
                mGrainId = -1;
            }
            if (!mPlayer.isPlaying()) {
                mRenderProcess.updateFrame();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.pause();
            mPlayPauseBtn.setText("播放");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (mRenderProcess != null) {
            mRenderProcess.destroy();
        }
    }

    private void initPlayer() {
        PlayerParams params = new PlayerParams();
        params.setUseOkHttp(false);
        mPlayer = new CommonPlayer(this, PlayerType.IJK_PLAYER, params);
        try {
            mPlayer.setDataSource(this, Uri.parse(mVideoUrl));
        } catch (Exception e) {
            Log.w(LogTag.TAG, "setDataSource failed, exception = " + e.getMessage());
            return;
        }
        mPlayer.setSurface(mSurface);
        mPlayer.setOnPreparedListener(mPrepareListener);
        mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        mPlayer.prepareAsync();
    }

    private IPlayer.OnPreparedListener mPrepareListener = mp -> {
        mPlayer.start();
    };

    private IPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IPlayer mp, int width, int height, int rotationDegree, float pixelRatio, float darRatio) {
            if (mRenderProcess != null && width != 0 && height != 0) {
                mRenderProcess.setVideoSize(width, height);

                ViewGroup.LayoutParams layoutParams = mVideoTextureView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = (int) (ScreenUtils.getScreenWidth(TextureViewActivity.this) * height * 1.0f / width);
                mVideoTextureView.setLayoutParams(layoutParams);
            }
        }
    };
}