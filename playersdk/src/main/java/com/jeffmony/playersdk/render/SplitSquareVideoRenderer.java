package com.jeffmony.playersdk.render;

import android.content.Context;

import com.jeffmony.gltoolkit.VideoRenderEngine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class SplitSquareVideoRenderer extends BaseVideoRenderer{

    private static final String TAG = "SplitSquareRenderer";
    private VideoRenderEngine mLeftVideoEngine;
    private VideoRenderEngine mRightVideoEngine;

    private float curLeft = 0.1f;
    private float curWidth = 0.25f;

    public SplitSquareVideoRenderer(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);

        mLeftVideoEngine = new VideoRenderEngine(getVideoLeftVertexData());
        mRightVideoEngine = new VideoRenderEngine(getVideoRightVertexData());
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);

        mLeftVideoEngine.bindData(mVideoShaderProgram);
        mLeftVideoEngine.draw();

        mRightVideoEngine.bindData(mVideoShaderProgram);
        mRightVideoEngine.draw();
    }
    public float[] getVideoLeftVertexData(){

        float[] VERTEX_DATA = {
                // X, Y, Z, U, V
                -1.0f,-mCurHeightRatio / 2, 0, curLeft, 0.f,
                0.0f, -mCurHeightRatio / 2, 0, curLeft + curWidth, 0.f,
                -1.0f, mCurHeightRatio / 2, 0, curLeft, 1.f,
                0.0f,  mCurHeightRatio / 2, 0, curLeft + curWidth, 1.f,
        };
        return VERTEX_DATA;
    }
    public float[] getVideoRightVertexData(){
        float[] VERTEX_DATA = {
                // X, Y, Z, U, V
                0.0f, -mCurHeightRatio / 2, 0, curLeft + 0.5f, 0.f,
                1.0f, -mCurHeightRatio / 2, 0, curLeft + curWidth + 0.5f, 0.f,
                0.0f,  mCurHeightRatio / 2, 0, curLeft + 0.5f, 1.f,
                1.0f,  mCurHeightRatio / 2, 0, curLeft + curWidth + 0.5f, 1.f,
        };
        return VERTEX_DATA;
    }

    @Override
    protected void updateWidthHeight(){
        if (mSurfaceHeight > 0 && mVideoHeight > 0) {

            float new_width = mVideoHeight * 2;
            float HEIGHT_IN_PX = mVideoHeight * mSurfaceWidth / new_width;
            mCurHeightRatio = HEIGHT_IN_PX * 2.0f / mSurfaceHeight * 1.0f;

            if ( mVideoWidth > 2 * mVideoHeight){
                // 3D side by side video
                curWidth = mVideoHeight * 1.0f / (mVideoWidth * 1.0f);
            } else {
                // other video
                curWidth = 0.1f;
            }
            updateVertexArray();
        }
    }

    @Override
    protected void updateVertexArray(){
        mLeftVideoEngine.setVertexArray(getVideoLeftVertexData());
        mRightVideoEngine.setVertexArray(getVideoRightVertexData());
    }
    public float getCurLeft(){
        return curLeft;
    }

    public void adjustLeftPosition(float delta) {
        if(mLeftVideoEngine != null && mRightVideoEngine != null) {
            float value = getCurLeft();
            value += delta;
            value = Math.min(Math.max(value, 0.0f), 0.5f - curWidth);

            curLeft = value;

            updateVertexArray();
        }
    }
}
