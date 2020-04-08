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
import android.widget.EditText;
import android.widget.Toast;

import com.jeffmony.orcode.CaptureActivity;
import com.jeffmony.orcode.Intents;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_SCAN = 0x01;
    public static final int RC_CAMERA = 0x01;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    private EditText mUrlText;
    private Button mOrcodeBtn;
    private Button mPlayBtn;

    private Class<?> mCls;
    private boolean mIsContinuousScan;

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
        mUrlText.setText("https://baidu.com-l-baidu.com/20190219/12123_e796a823/index.m3u8");

        mOrcodeBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
    }

    private void checkCameraPermissions() {
        mIsContinuousScan = false;
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限
            startScan(mCls, "扫码");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
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
                case REQUEST_CODE_SCAN:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    mUrlText.setText(result);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mOrcodeBtn) {
            mCls = CaptureActivity.class;
            checkCameraPermissions();
        } else if (v == mPlayBtn) {
            String url = mUrlText.getText().toString();
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(this, R.string.url_tip, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("video_url", url);
                startActivity(intent);
            }
        }
    }
}
