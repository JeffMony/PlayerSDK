package com.jeffmony.gltoolkit;

import android.opengl.GLES30;

public class VideoRenderEngine {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private VertexArray vertexArray;

    public VideoRenderEngine(float[] vertex){
        this.vertexArray = new VertexArray(vertex);
    }

    public void bindData(VideoShaderProgram videoShaderProgram){
        vertexArray.setVertexAttribPointer(
                0,
                videoShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                videoShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw(){
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void setVertexArray(float[] vertex){
        this.vertexArray = new VertexArray(vertex);
    }
}
