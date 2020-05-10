package com.jeffmony.gltoolkit;

import android.content.Context;
import android.opengl.GLES30;

public class VideoShaderProgram extends ShaderProgram {

    public static final int DEFAULT_VERTEX_SHADER = R.raw.default_vertex_shader;
    public static final int DEFAULT_FRAGMENT_SHADER = R.raw.default_fragment_shader;

    // Uniform locations
    private int uMVPMatrixLocation;
    private int uSTMatrixLocation;

    // Attribute locations
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public VideoShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        super(context, vertexShaderResourceId, fragmentShaderResourceId);

        aPositionLocation = GLES30.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = GLES30.glGetAttribLocation(program, A_TEXTURE_COORDINATES);

        uMVPMatrixLocation = GLES30.glGetUniformLocation(program, U_MVPMATRIX);
        uSTMatrixLocation = GLES30.glGetUniformLocation(program, U_STMATRIX);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
    public void setUniforms(float[] mvpMatrix, float[] stMatrix){
        GLES30.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0 );
        GLES30.glUniformMatrix4fv(uSTMatrixLocation, 1, false, stMatrix, 0 );
    }
}
