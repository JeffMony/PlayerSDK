package com.jeffmony.video.process;

public class FFmpegRemux {

    static {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("jeffmony");
    }

    public static native int remux(String inputPath, String outputPath, int width, int height);
}
