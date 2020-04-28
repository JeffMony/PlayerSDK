package com.jeffmony.playerdemo;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.playersdk.CommonPlayer;
import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.PlayerParams;
import com.jeffmony.playersdk.PlayerType;
import com.jeffmony.playersdk.WeakHandler;
import com.jeffmony.playersdk.callback.IVideoInfoCallback;
import com.jeffmony.playersdk.utils.ScreenUtils;
import com.jeffmony.playersdk.utils.Utility;
import com.jeffmony.playersdk.videoinfo.M3U8Seg;
import com.jeffmony.playersdk.videoinfo.VideoInfoParserManager;

import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private static final int MSG_UPDATE_PROGRESS = 0x1;
    private static final int INTERVAL = 1000;
    private static final int MAX_PROGRESS = 1000;

    private TextureView mVideoView;
    private SeekBar mProgressView;
    private TextView mTimeView;
    private ImageButton mVideoStateBtn;
    private Spinner mSpeedSpinner;
    private Button mSpeedBtn1;
    private Button mSpeedBtn2;
    private Button mSpeedBtn3;
    private Button mSpeedBtn4;
    private Button mSpeedBtn5;

    private String mUrl;
    private int mScreenWidth;
    private long mTotalDuration;
    private int mPlayerType;
    private boolean mIsLooping;
    private boolean mUseOkHttp;
    private Surface mSurface;
    private CommonPlayer mPlayer;
    private float mSpeed = 1.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mUrl = getIntent().getStringExtra("video_url");
        mPlayerType = getIntent().getIntExtra("player_type", 1);
        mIsLooping = getIntent().getBooleanExtra("is_looping", false);
        mUseOkHttp = getIntent().getBooleanExtra("use_okhttp", false);
        mScreenWidth = ScreenUtils.getScreenWidth(this);

        VideoInfoParserManager.getInstance().parseVideoInfo(mUrl, mVideoInfoCallback);

        initViews();
    }

    private void initViews() {
        mVideoView = (TextureView) findViewById(R.id.video_view);
        mProgressView = (SeekBar) findViewById(R.id.video_progress_view);
        mVideoStateBtn = (ImageButton) findViewById(R.id.video_state_btn);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mSpeedSpinner = (Spinner) findViewById(R.id.speed_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speed_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpeedSpinner.setAdapter(adapter);




        mSpeedBtn1 = (Button) findViewById(R.id.speed_btn1);
        mSpeedBtn2 = (Button) findViewById(R.id.speed_btn2);
        mSpeedBtn3 = (Button) findViewById(R.id.speed_btn3);
        mSpeedBtn4 = (Button) findViewById(R.id.speed_btn4);
        mSpeedBtn5 = (Button) findViewById(R.id.speed_btn5);

        mVideoView.setSurfaceTextureListener(mSurfaceTextureListener);
        mProgressView.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mVideoStateBtn.setOnClickListener(this);
        mSpeedBtn1.setOnClickListener(this);
        mSpeedBtn2.setOnClickListener(this);
        mSpeedBtn3.setOnClickListener(this);
        mSpeedBtn4.setOnClickListener(this);
        mSpeedBtn5.setOnClickListener(this);
    }

    private IVideoInfoCallback mVideoInfoCallback = new IVideoInfoCallback() {
        @Override
        public void onVideoType(String contentType, String name) {

        }

        @Override
        public void onMutipleVideo(List<M3U8Seg> urlList) {

        }
    };

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurface = new Surface(surface);
            initPlayer();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return surface == null;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void initPlayer() {
        PlayerParams params = new PlayerParams();
        params.setUseOkHttp(mUseOkHttp);
        if (mPlayerType == 1) {
            mPlayer = new CommonPlayer(this, PlayerType.EXO_PLAYER, params);
        } else if (mPlayerType == 2) {
            mPlayer = new CommonPlayer(this, PlayerType.IJK_PLAYER, params);
        } else {
            return;
        }
        try {
            mPlayer.setDataSource(this, Uri.parse(mUrl));
        } catch (Exception e) {
            Log.w(TAG, "setDataSource failed, exception = " + e.getMessage());
            return;
        }
        mPlayer.setLooping(mIsLooping);
        mPlayer.setSurface(mSurface);
        mPlayer.setOnPreparedListener(mPrepareListener);
        mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        mPlayer.prepareAsync();
    }

    private IPlayer.OnPreparedListener mPrepareListener = new IPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IPlayer mp) {
            mVideoStateBtn.setClickable(true);
            mTotalDuration = mPlayer.getDuration();
            startPlayer();
        }
    };

    private IPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IPlayer mp, int width, int height, int rotationDegree, float pixelRatio, float darRatio) {
            int videoWidth = mScreenWidth;
            int videoHeight = (int)((height * videoWidth * 1.0f)/ width);
            updateVideoSize(videoWidth, videoHeight);
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (mPlayer != null) {
                mHandler.removeMessages(MSG_UPDATE_PROGRESS);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mPlayer != null) {
                int progress = mProgressView.getProgress();
                long seekPosition = (long) (progress * 1.0f / MAX_PROGRESS * mTotalDuration);
                mPlayer.seekTo(seekPosition);
                mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            }
        }
    };

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_UPDATE_PROGRESS) {
                updateVideoProgress();
            }
            return true;
        }
    });

    private void updateVideoProgress() {
        long currentPosition = mPlayer.getCurrentPosition();
        long totalDuration = mTotalDuration;

        String timeStr = Utility.getVideoTimeString(currentPosition) + "/" + Utility.getVideoTimeString(totalDuration);
        mTimeView.setText(timeStr);
        mTimeView.setVisibility(View.VISIBLE);
        int progress = (int)(currentPosition * 1.0f / totalDuration * MAX_PROGRESS);
        mProgressView.setProgress(progress);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, INTERVAL);

    }

    private void updateVideoSize(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        mVideoView.setLayoutParams(params);
    }

    private void updateVideoState() {
        if (mPlayer.isPlaying()) {
            mVideoStateBtn.setImageResource(R.mipmap.video_pause);
        } else {
            mVideoStateBtn.setImageResource(R.mipmap.video_play);
        }
    }

    private void updatePlayerState() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        } else {
            mPlayer.start();
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        }
        updateVideoState();
    }

    private void startPlayer() {
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            updateVideoState();
        }
    }

    private void pausePlayer() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
            updateVideoState();
        }
    }

    private void changeSpeed(float speed) {
        if (mSpeed == speed)
            return;
        mSpeed = speed;
        mSpeedBtn1.setTextColor(getResources().getColor(R.color.black));
        mSpeedBtn2.setTextColor(getResources().getColor(R.color.black));
        mSpeedBtn3.setTextColor(getResources().getColor(R.color.black));
        mSpeedBtn4.setTextColor(getResources().getColor(R.color.black));
        mSpeedBtn5.setTextColor(getResources().getColor(R.color.black));
        mPlayer.setSpeed(speed);
        if (speed == 0.75f) {
            mSpeedBtn1.setTextColor(getResources().getColor(R.color.red));
        } else if (speed == 1.0f) {
            mSpeedBtn2.setTextColor(getResources().getColor(R.color.red));
        } else if (speed == 1.25f) {
            mSpeedBtn3.setTextColor(getResources().getColor(R.color.red));
        } else if (speed ==1.5f) {
            mSpeedBtn4.setTextColor(getResources().getColor(R.color.red));
        } else if (speed == 2.0f) {
            mSpeedBtn5.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mVideoStateBtn) {
            updatePlayerState();
        } else if (v == mSpeedBtn1) {
            changeSpeed(0.75f);
        } else if (v == mSpeedBtn2) {
            changeSpeed(1.0f);
        } else if (v == mSpeedBtn3) {
            changeSpeed(1.25f);
        } else if (v == mSpeedBtn4) {
            changeSpeed(1.5f);
        } else if (v == mSpeedBtn5) {
            changeSpeed(2.0f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
            mPlayer.release();
            mPlayer = null;
        }
    }
}
