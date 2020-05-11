package com.jeffmony.playersdk.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.jeffmony.playersdk.render.BaseVideoRenderer;

public class VideoGLSurfaceView extends GLSurfaceView {

    private BaseVideoRenderer mRenderer;

    public VideoGLSurfaceView(Context context) {
        this(context, null);
    }

    public VideoGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setEGLContextClientVersion(3);
    }


    public void setRendererEngine(BaseVideoRenderer renderer) {
        mRenderer = renderer;
        setRenderer(renderer);
    }

    public BaseVideoRenderer getRenderer(){
        return mRenderer;
    }
}
