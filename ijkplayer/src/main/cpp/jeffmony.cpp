#include <jni.h>
#include <string>

extern "C"
JNIEXPORT int JNICALL
Java_com_jeffmony_video_process_FFmpegRemux_remux(JNIEnv *env, jclass clazz, jstring input_path,
                                                   jstring output_path) {
    return 0;
}