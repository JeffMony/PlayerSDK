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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jeffmony.playersdk.CommonPlayer;
import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.LogUtils;
import com.jeffmony.playersdk.PlayerParams;
import com.jeffmony.playersdk.PlayerType;
import com.jeffmony.playersdk.WeakHandler;
import com.jeffmony.playersdk.callback.IVideoInfoCallback;
import com.jeffmony.playersdk.utils.ScreenUtils;
import com.jeffmony.playersdk.utils.Utility;
import com.jeffmony.playersdk.videoinfo.M3U8Seg;
import com.jeffmony.playersdk.videoinfo.VideoInfoParserManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private static final int MSG_UPDATE_PROGRESS = 1;
    private static final int MSG_MULTIPLE_VIDEO = 2;
    private static final int INTERVAL = 1000;
    private static final int MAX_PROGRESS = 1000;

    private TextureView mVideoView;
    private SeekBar mProgressView;
    private TextView mTimeView;
    private ImageButton mVideoStateBtn;
    private Spinner mSpeedSpinner;
    private Spinner mResolutionSpinner;
    private Spinner mVolumeSpinner;

    private String mUrl;
    private int mScreenWidth;
    private long mTotalDuration;
    private int mPlayerType;
    private boolean mIsLooping;
    private boolean mUseOkHttp;
    private Surface mSurface;
    private CommonPlayer mPlayer;
    private float mSpeed = 1.0f;
    private float mVolume = 1.0f;
    private List<M3U8Seg> mSegList;
    private int mResolutionPosition = -1;

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
        mVideoView = findViewById(R.id.video_view);
        mProgressView = findViewById(R.id.video_progress_view);
        mVideoStateBtn = findViewById(R.id.video_state_btn);
        mTimeView = findViewById(R.id.time_view);
        mResolutionSpinner = findViewById(R.id.resolution_spinner);
        mResolutionSpinner.setVisibility(View.GONE);

        mVolumeSpinner = findViewById(R.id.volume_spinner);
        ArrayAdapter<CharSequence> volumeAdapter = ArrayAdapter.createFromResource(this, R.array.volume_array, android.R.layout.simple_spinner_item);
        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVolumeSpinner.setAdapter(volumeAdapter);
        mVolumeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String volumeString = parent.getItemAtPosition(position).toString();
                changeVolume(volumeString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpeedSpinner = findViewById(R.id.speed_spinner);
        ArrayAdapter<CharSequence> speedAdapter = ArrayAdapter.createFromResource(this,
                R.array.speed_array, android.R.layout.simple_spinner_item);
        speedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpeedSpinner.setAdapter(speedAdapter);
        mSpeedSpinner.setOnItemSelectedListener(this);


        mVideoView.setSurfaceTextureListener(mSurfaceTextureListener);
        mProgressView.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mVideoStateBtn.setOnClickListener(this);
    }

    private void changeVolume(String volumeString) {
        if ("0.5 X".equals(volumeString)) {
            mVolume = 0.5f;
        } else if ("1.0 X".equals(volumeString)) {
            mVolume = 1.0f;
        } else if ("2.0 X".equals(volumeString)) {
            mVolume = 2.0f;
        } else if ("3.0 X".equals(volumeString)) {
            mVolume = 3.0f;
        } else if ("4.0 X".equals(volumeString)) {
            mVolume = 4.0f;
        } else {
            mVolume = 1.0f;
        }
        mPlayer.setSonicVolume(mVolume);
    }

    private void initVideoResolutions(List<M3U8Seg> urlList) {
        if (urlList.size() == 0 || urlList.size() == 1) {
            mResolutionSpinner.setVisibility(View.GONE);
        } else {
            mResolutionSpinner.setVisibility(View.VISIBLE);
            mSegList = urlList;
            List<String> resolutionList = new ArrayList<>();
            resolutionList.add("清晰度");
            for(int index = 0; index < urlList.size(); index++) {
                resolutionList.add(urlList.get(index).getResolution());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resolutionList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mResolutionSpinner.setAdapter(adapter);
            mResolutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0 && position <= mSegList.size()) {
                        changeResolution(position);
                    }
                 }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void changeResolution(int position) {
        if (mResolutionPosition != position) {
            mResolutionPosition = position;
            String url = mSegList.get(position - 1).getUrl();
            if (mPlayer.isPlaying()) {
                mPlayer.reset();
                try {
                    mPlayer.setDataSource(this, Uri.parse(url));
                } catch (Exception e) {
                    return;
                }
                mPlayer.setSurface(mSurface);
                mPlayer.prepareAsync();
            }
        } else {
            Toast.makeText(this, "正在观看", Toast.LENGTH_SHORT).show();
        }
    }

    private IVideoInfoCallback mVideoInfoCallback = new IVideoInfoCallback() {
        @Override
        public void onVideoType(String contentType, String name) {

        }

        @Override
        public void onMultipleVideo(List<M3U8Seg> urlList) {
            LogUtils.e("onMultipleVideo : size="+urlList.size());
            Message message = Message.obtain();
            message.what = MSG_MULTIPLE_VIDEO;
            message.obj = urlList;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFailed(Exception e) {
            e.printStackTrace();
            LogUtils.e("onFailed, e="+e.getMessage());
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
        mPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
        mPlayer.setOnAccurateSeekCompleteListener(mOnAccurateSeekCompleteListener);
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

    private IPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IPlayer mp) {
            LogUtils.e("player_sdk seek complete");
        }
    };

    private IPlayer.OnAccurateSeekCompleteListener mOnAccurateSeekCompleteListener = new IPlayer.OnAccurateSeekCompleteListener() {
        @Override
        public void onSeekComplete(IPlayer mp) {
            LogUtils.e("player_sdk accurate seek complete");
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
                LogUtils.e("player_sdk seekTo");
                mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
            }
        }
    };

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_UPDATE_PROGRESS) {
                updateVideoProgress();
            } else if (msg.what == MSG_MULTIPLE_VIDEO) {
                List<M3U8Seg> urlList = (List<M3U8Seg>) msg.obj;
                initVideoResolutions(urlList);
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

    private void updatePlayerState() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mVideoStateBtn.setImageResource(R.mipmap.video_play);
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        } else {
            mPlayer.start();
            mVideoStateBtn.setImageResource(R.mipmap.video_pause);
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        }
    }

    private void startPlayer() {
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            mVideoStateBtn.setImageResource(R.mipmap.video_pause);
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        }
    }

    private void pausePlayer() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mVideoStateBtn.setImageResource(R.mipmap.video_play);
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        }
    }

    private void changeSpeed(String speedString) {
        if ("0.75X".equals(speedString)) {
            mSpeed = 0.75f;
        } else if ("1.0 X".equals(speedString)) {
            mSpeed = 1.0f;
        } else if ("1.25X".equals(speedString)) {
            mSpeed = 1.25f;
        } else if ("1.5 X".equals(speedString)) {
            mSpeed = 1.5f;
        } else if ("2.0 X".equals(speedString)) {
            mSpeed = 2.0f;
        }
        mPlayer.setSpeed(mSpeed);
    }

    @Override
    public void onClick(View v) {
        if (v == mVideoStateBtn) {
            updatePlayerState();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemString = parent.getItemAtPosition(position).toString();
        if ("0.75X".equals(itemString) ||
                "1.0 X".equals(itemString) ||
                "1.25X".equals(itemString) ||
                "1.5 X".equals(itemString) ||
                "1.5 X".equals(itemString) ||
                "2.0 X".equals(itemString)) {
            changeSpeed(itemString);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
