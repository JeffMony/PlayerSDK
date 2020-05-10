package com.jeffmony.playersdk.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jeffmony.playersdk.render.BaseVideoRenderer;

public class VideoGLSurfaceView extends GLSurfaceView {

    private BaseVideoRenderer mRenderer;

    public VideoGLSurfaceView(Context context, BaseVideoRenderer renderer) {
        super(context);

        setEGLContextClientVersion(3);
        mRenderer = renderer;
        setRenderer(mRenderer);
    }

    public BaseVideoRenderer getRenderer(){
        return mRenderer;
    }
}
