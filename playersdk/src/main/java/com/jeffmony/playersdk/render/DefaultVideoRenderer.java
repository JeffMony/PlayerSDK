package com.jeffmony.playersdk.render;

import android.content.Context;

import com.jeffmony.gltoolkit.VideoRenderEngine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class DefaultVideoRenderer extends BaseVideoRenderer{

    private static final String TAG = "DefaultVideoRenderer";
    private VideoRenderEngine mVideoEngine;

    public DefaultVideoRenderer(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);
        mVideoEngine = new VideoRenderEngine(VERTEX_DATA);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);
        mVideoEngine.bindData(mVideoShaderProgram);
        mVideoEngine.draw();
    }

    @Override
    protected void updateVertexArray() {
        super.updateVertexArray();
        mVideoEngine.setVertexArray(VERTEX_DATA);
    }
}
