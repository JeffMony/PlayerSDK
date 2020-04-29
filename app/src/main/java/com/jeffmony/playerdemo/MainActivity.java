package com.jeffmony.playerdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jeffmony.orcode.CaptureActivity;
import com.jeffmony.orcode.Intents;
import com.jeffmony.playersdk.LogUtils;
import com.jeffmony.playersdk.PlayerType;
import com.jeffmony.playersdk.callback.IVideoInfoCallback;
import com.jeffmony.playersdk.utils.HttpUtils;
import com.jeffmony.playersdk.videoinfo.M3U8Seg;
import com.jeffmony.playersdk.videoinfo.VideoInfoParserManager;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final int REQUEST_CODE_SCAN = 1;
    public static final int RC_CAMERA = 2;
    public static final int RC_STORAGE = 3;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    private EditText mUrlText;
    private Button mOrcodeBtn;
    private Button mPlayBtn;
    private Button mLoopBtn;
    private RadioButton mExoBtn;
    private RadioButton mIjkBtn;
    private CheckBox mOkHttpBox;

    private Class<?> mCls;
    private boolean mIsContinuousScan;
    private PlayerType mPlayerType = PlayerType.EXO_PLAYER;
    private boolean mUseOkHttp = false;
    private boolean mIsLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mUrlText = (EditText) findViewById(R.id.video_url_text);
        mOrcodeBtn = (Button) findViewById(R.id.orcode_btn);
        mPlayBtn = (Button) findViewById(R.id.play_btn);
        mLoopBtn = (Button) findViewById(R.id.loop_btn);
        mExoBtn = (RadioButton) findViewById(R.id.exo_btn);
        mIjkBtn = (RadioButton) findViewById(R.id.ijk_btn);
        mOkHttpBox = (CheckBox) findViewById(R.id.okhttp_box);
        mUrlText.setText("http://videoconverter.vivo.com.cn/201706/655_1498479540118.mp4.main.m3u8");

        mExoBtn.setOnClickListener(this);
        mIjkBtn.setOnClickListener(this);
        mOrcodeBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
        mLoopBtn.setOnClickListener(this);
        mOkHttpBox.setOnCheckedChangeListener(this);
    }

    private void checkCameraPermissions() {
        mIsContinuousScan = false;
        String[] permssions = { Manifest.permission.CAMERA };
        if (EasyPermissions.hasPermissions(this, permssions)) {//有权限
            startScan(mCls, "扫码");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera), RC_CAMERA, permssions);
        }
    }

    private void checkStoragePermissions() {
        String[] permssions = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permssions)) {
            playVideo();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage), RC_STORAGE, permssions);
        }
    }

    private void startScan(Class<?> cls, String title) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_IS_CONTINUOUS, mIsContinuousScan);
        ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SCAN, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null) {
            switch (requestCode) {
                case RC_CAMERA:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    mUrlText.setText(result);
                    break;
                case RC_STORAGE:
                    playVideo();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mExoBtn) {
            mExoBtn.setChecked(true);
            mIjkBtn.setChecked(false);
            mPlayerType = PlayerType.EXO_PLAYER;
        } else if (v == mIjkBtn) {
            mExoBtn.setChecked(false);
            mIjkBtn.setChecked(true);
            mPlayerType = PlayerType.IJK_PLAYER;
        } else if (v == mOrcodeBtn) {
            mCls = CaptureActivity.class;
            checkCameraPermissions();
        } else if (v == mPlayBtn) {
            playWithLooping(false);
        } else if (v == mLoopBtn) {
            playWithLooping(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == mOkHttpBox) {
            mUseOkHttp = isChecked;
        }
    }

    private void playWithLooping(boolean isLooping) {
        String url = mUrlText.getText().toString();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, R.string.url_tip, Toast.LENGTH_SHORT).show();
        } else {
            mIsLoop = isLooping;
            if (HttpUtils.isLocalFile(url)) {
                checkStoragePermissions();
            } else {
                playVideo();
            }
        }
    }

    private void playVideo() {
        String url = mUrlText.getText().toString();
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("video_url", url);
        int type = 0;
        if (mPlayerType == PlayerType.EXO_PLAYER) {
            type = 1;
        } else if (mPlayerType == PlayerType.IJK_PLAYER) {
            type = 2;
        }
        intent.putExtra("player_type", type);
        intent.putExtra("is_looping", mIsLoop);
        intent.putExtra("use_okhttp", mUseOkHttp);
        startActivity(intent);
    }
}
