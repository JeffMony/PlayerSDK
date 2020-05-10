package com.jeffmony.playersdk.render;

import android.content.Context;

import com.jeffmony.gltoolkit.VideoRenderEngine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SplitVideoRenderer extends BaseVideoRenderer{

    private static final String TAG = "SplitVideoRenderer";
    private VideoRenderEngine mTopVideoEngine;
    private VideoRenderEngine mBottomVideoEngine;

    public SplitVideoRenderer(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);

        float[] VERTEX_DATA_TOP = {
                // X, Y, Z, U, V
                -1.0f, 0f, 0, 0.f, 0.f,
                1.0f, 0f, 0, 0.5f, 0.f,
                -1.0f,  1.0f, 0, 0.f, 1.f,
                1.0f,  1.0f, 0, 0.5f, 1.f,
        };
        mTopVideoEngine = new VideoRenderEngine(VERTEX_DATA_TOP);
        float[] VERTEX_DATA_BOTTOM = {
                // X, Y, Z, U, V
                -1.0f, -1.0f, 0, 0.5f, 0.f,
                1.0f, -1.0f, 0, 1f, 0.f,
                -1.0f,  0f, 0, 0.5f, 1.f,
                1.0f,  0f, 0, 1f, 1.f,
        };
        mBottomVideoEngine = new VideoRenderEngine(VERTEX_DATA_BOTTOM);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);

        mTopVideoEngine.bindData(mVideoShaderProgram);
        mTopVideoEngine.draw();

        mBottomVideoEngine.bindData(mVideoShaderProgram);
        mBottomVideoEngine.draw();
    }
}
