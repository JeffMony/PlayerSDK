package com.jeffmony.playersdk.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.jeffmony.gltoolkit.VideoShaderProgram;
import com.jeffmony.playersdk.IPlayer;
import com.jeffmony.playersdk.glplayer.GLPlayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BaseVideoRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static String TAG = "BaseVideoRenderer";
    private GLPlayer mGLPlayer;
    private static int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    // Properties
    protected Context mContext;
    protected VideoShaderProgram mVideoShaderProgram;
    private int mTextureID;
    private SurfaceTexture mSurface;
    private boolean mUpdateSurface = false;

    protected float mCurHeightRatio = 1.0f;
    protected int mVideoWidth = -1;
    protected int mVideoHeight = -1;
    protected int mSurfaceWidth = -1;
    protected int mSurfaceHeight = -1;
    protected float mHeightPx = 0.0f;

    // Renderer Properties
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];

    protected float[] VERTEX_DATA = {
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0, 0.f, 0.f,
            1.0f, -1.0f, 0, 1f, 0.f,
            -1.0f,  -0.5f, 0, 0.f, 1.f,
            1.0f,  -0.5f, 0, 1f, 1.f,
    };

    public BaseVideoRenderer(Context context) {
        mContext = context;
    }

    public void setGLPlayer(GLPlayer player){
        Log.d(TAG, "setGlMediaPlayer");
        mGLPlayer = player;
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mUpdateSurface = true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated");

        mVideoShaderProgram = new VideoShaderProgram(
                mContext, VideoShaderProgram.DEFAULT_VERTEX_SHADER, VideoShaderProgram.DEFAULT_FRAGMENT_SHADER);

        int[] textures = new int[1];
        GLES30.glGenTextures(1, textures, 0);
        mTextureID = textures[0];
        GLES30.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID);

        GLES30.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        // Create SurfaceTexture that will feed this textureId and pass to MediaPlayer
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);

        Surface surface = new Surface(mSurface);
        mGLPlayer.setSurface(surface);
        Log.e("litianpeng", "----------");

        synchronized (this){
            mUpdateSurface = false;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d(TAG, "onSurfaceChanged : width " + width + " height " + height);

        mSurfaceWidth = width;
        mSurfaceHeight = height;

        // Set the OpenGL viewport to file the entire surface
//        GLES30.glViewport(0, 0, width, height);
        Matrix.setIdentityM(mMVPMatrix, 0);

        updateWidthHeight();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        synchronized (this) {
            if(mUpdateSurface){
                mSurface.updateTexImage();
                mSurface.getTransformMatrix(mSTMatrix);
                mUpdateSurface = false;
            }
        }

        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        mVideoShaderProgram.useProgram();
        mVideoShaderProgram.setUniforms(mMVPMatrix, mSTMatrix);
    }

//    @Override
//    public void onVideoSizeChanged(IPlayer mp, int width, int height, int rotationDegree, float pixelRatio, float darRatio) {
//        mVideoWidth = width;
//        mVideoHeight = height;
//        GLES30.glViewport(0, 0, width, height);
//        updateWidthHeight();
//    }

    public void updateVideoSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        GLES30.glViewport(0, 0, width, height);
        updateWidthHeight();
    }

    protected void updateWidthHeight() {
        if (mSurfaceHeight > 0 && mVideoHeight > 0) {
            mHeightPx = mVideoHeight * mSurfaceWidth / mVideoWidth;
            mCurHeightRatio = mHeightPx * 2.0f / mSurfaceHeight * 1.0f;
            updateVertexArray();
        }
    }

    protected void updateVertexArray(){
        float[] NEW_VERTEX_DATA = {
                // X, Y, Z, U, V
                -1.0f, 1.0f - mCurHeightRatio , 0, 0f, 0f,
                1.0f, 1.0f - mCurHeightRatio, 0, 1.0f, 0f,
                -1.0f,  1.0f, 0, 0f, 1.0f,
                1.0f,  1.0f, 0, 1.0f, 1.0f,
        };
        System.arraycopy(NEW_VERTEX_DATA, 0 , VERTEX_DATA, 0, NEW_VERTEX_DATA.length );
    }

    public float getSurfaceHeightPx() {
        return mHeightPx;
    }
}