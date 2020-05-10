package com.jeffmony.gltoolkit;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexArray {
    private final FloatBuffer floatBuffer;


    public VertexArray(float[] vertexData){
        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer( int dataOffset, int attributeLocation, int componentCount, int stride){
        floatBuffer.position(dataOffset);
        GLES30.glVertexAttribPointer(attributeLocation, componentCount, GLES30.GL_FLOAT, false, stride, floatBuffer);
        GLES30.glEnableVertexAttribArray(attributeLocation);

        floatBuffer.position(0);
    }
}
