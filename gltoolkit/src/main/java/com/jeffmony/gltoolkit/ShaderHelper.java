package com.jeffmony.gltoolkit;

import android.opengl.GLES30;
import android.util.Log;

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String code){
        return compileShader(GLES30.GL_VERTEX_SHADER, code);
    }

    public static int compileFragmentShader(String code){
        return compileShader(GLES30.GL_FRAGMENT_SHADER, code);
    }

    private static int compileShader(int type, String code){
        final int shaderObjectId = GLES30.glCreateShader(type);

        if (shaderObjectId == 0){
            Log.w(TAG, "Could not create new shader");
            return 0;
        }

        GLES30.glShaderSource(shaderObjectId, code);
        GLES30.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shaderObjectId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

        Log.v(TAG, "Results of compiling source: " + "\n" + code + "\n" + GLES30.glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0){
            // if it failed, delete the shader object
            GLES30.glDeleteShader(shaderObjectId);

            Log.w(TAG, "Compilation of shader failed");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        final int programObjectId = GLES30.glCreateProgram();

        if(programObjectId == 0){
            Log.w(TAG, "Could not create new program");
            return 0;
        }

        GLES30.glAttachShader(programObjectId, vertexShaderId);
        GLES30.glAttachShader(programObjectId, fragmentShaderId);

        GLES30.glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_LINK_STATUS, linkStatus, 0);
        Log.v(TAG, "Results of linking program: " + "\n" + GLES30.glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0){
            // if it failed, delete the shader object
            GLES30.glDeleteProgram(programObjectId);

            Log.w(TAG, "Linking of program failed");
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES30.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0] + "\nLog: " + GLES30.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
        int program;

        // compile the shaders
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        // link them into a shader program
        program = linkProgram(vertexShader, fragmentShader);

        validateProgram(program);

        return program;
    }
}
