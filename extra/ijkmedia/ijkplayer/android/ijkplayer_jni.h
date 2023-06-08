//
// Created by jefflee on 2023/6/8.
//

#ifndef PLAYERSDK_EXTRA_IJKMEDIA_IJKPLAYER_ANDROID_IJKPLAYER_JNI_H_
#define PLAYERSDK_EXTRA_IJKMEDIA_IJKPLAYER_ANDROID_IJKPLAYER_JNI_H_

#include <jni.h>

static void
IjkMediaPlayer_setDataSourceAndHeaders(
    JNIEnv *env, jobject thiz, jstring path,
    jobjectArray keys, jobjectArray values);

static void
IjkMediaPlayer_setDataSourceFd(JNIEnv *env, jobject thiz, jint fd);

static void
IjkMediaPlayer_setDataSourceCallback(JNIEnv *env, jobject thiz, jobject callback);

static void
IjkMediaPlayer_setAndroidIOCallback(JNIEnv *env, jobject thiz, jobject callback);

static void
IjkMediaPlayer_setVideoSurface(JNIEnv *env, jobject thiz, jobject jsurface);

static void
IjkMediaPlayer_prepareAsync(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_start(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_stop(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_pause(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_seekTo(JNIEnv *env, jobject thiz, jlong msec);

static jboolean
IjkMediaPlayer_isPlaying(JNIEnv *env, jobject thiz);
static jlong
IjkMediaPlayer_getCurrentPosition(JNIEnv *env, jobject thiz);

static jlong
IjkMediaPlayer_getDuration(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_release(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_reset(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_setLoopCount(JNIEnv *env, jobject thiz, jint loop_count);

static jint
IjkMediaPlayer_getLoopCount(JNIEnv *env, jobject thiz);

static jfloat
ijkMediaPlayer_getPropertyFloat(JNIEnv *env, jobject thiz, jint id, jfloat default_value);

static void
ijkMediaPlayer_setPropertyFloat(JNIEnv *env, jobject thiz, jint id, jfloat value);

static jlong
ijkMediaPlayer_getPropertyLong(JNIEnv *env, jobject thiz, jint id, jlong default_value);

static void
ijkMediaPlayer_setPropertyLong(JNIEnv *env, jobject thiz, jint id, jlong value);

static void
ijkMediaPlayer_setStreamSelected(JNIEnv *env, jobject thiz, jint stream, jboolean selected);

static void
IjkMediaPlayer_setVolume(JNIEnv *env, jobject thiz, jfloat leftVolume, jfloat rightVolume);

static jint
IjkMediaPlayer_getAudioSessionId(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_setOption(JNIEnv *env, jobject thiz, jint category, jobject name, jobject value);

static void
IjkMediaPlayer_setOptionLong(JNIEnv *env, jobject thiz, jint category, jobject name, jlong value);

static jstring
IjkMediaPlayer_getColorFormatName(JNIEnv *env, jclass clazz, jint mediaCodecColorFormat);

static jstring
IjkMediaPlayer_getVideoCodecInfo(JNIEnv *env, jobject thiz);

static jstring
IjkMediaPlayer_getAudioCodecInfo(JNIEnv *env, jobject thiz);

static jobject
IjkMediaPlayer_getMediaMeta(JNIEnv *env, jobject thiz);

static void
IjkMediaPlayer_native_init(JNIEnv *env);

static void
IjkMediaPlayer_native_setup(JNIEnv *env, jobject thiz, jobject weak_this);

static void
IjkMediaPlayer_native_finalize(JNIEnv *env, jobject thiz, jobject name, jobject value);

static void
IjkMediaPlayer_native_profileBegin(JNIEnv *env, jclass clazz, jstring libName);

static void
IjkMediaPlayer_native_profileEnd(JNIEnv *env, jclass clazz);

static void
IjkMediaPlayer_native_setLogLevel(JNIEnv *env, jclass clazz, jint level);

static void
IjkMediaPlayer_setFrameAtTime(JNIEnv *env, jobject thiz, jstring path, jlong start_time, jlong end_time, jint num, jint definition);

int IjkMediaPlayer_OnLoad(JNIEnv *env);

void IjkMediaPlayer_OnUnload();

// ----------------------------------------------------------------------------
static JNINativeMethod g_methods[] = {
    { "_setDataSource", "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V", (void *) IjkMediaPlayer_setDataSourceAndHeaders },
    { "_setDataSourceFd",       "(I)V",     (void *) IjkMediaPlayer_setDataSourceFd },
    { "_setDataSource",         "(Ltv/danmaku/ijk/media/player/misc/IMediaDataSource;)V", (void *) IjkMediaPlayer_setDataSourceCallback },
    { "_setAndroidIOCallback",  "(Ltv/danmaku/ijk/media/player/misc/IAndroidIO;)V", (void *) IjkMediaPlayer_setAndroidIOCallback },
    { "_setVideoSurface",       "(Landroid/view/Surface;)V", (void *) IjkMediaPlayer_setVideoSurface },
    { "_prepareAsync",          "()V",      (void *) IjkMediaPlayer_prepareAsync },
    { "_start",                 "()V",      (void *) IjkMediaPlayer_start },
    { "_stop",                  "()V",      (void *) IjkMediaPlayer_stop },
    { "seekTo",                 "(J)V",     (void *) IjkMediaPlayer_seekTo },
    { "_pause",                 "()V",      (void *) IjkMediaPlayer_pause },
    { "isPlaying",              "()Z",      (void *) IjkMediaPlayer_isPlaying },
    { "getCurrentPosition",     "()J",      (void *) IjkMediaPlayer_getCurrentPosition },
    { "getDuration",            "()J",      (void *) IjkMediaPlayer_getDuration },
    { "_release",               "()V",      (void *) IjkMediaPlayer_release },
    { "_reset",                 "()V",      (void *) IjkMediaPlayer_reset },
    { "setVolume",              "(FF)V",    (void *) IjkMediaPlayer_setVolume },
    { "getAudioSessionId",      "()I",      (void *) IjkMediaPlayer_getAudioSessionId },
    { "native_init",            "()V",      (void *) IjkMediaPlayer_native_init },
    { "native_setup",           "(Ljava/lang/Object;)V", (void *) IjkMediaPlayer_native_setup },
    { "native_finalize",        "()V",      (void *) IjkMediaPlayer_native_finalize },
    { "_setOption",             "(ILjava/lang/String;Ljava/lang/String;)V", (void *) IjkMediaPlayer_setOption },
    { "_setOption",             "(ILjava/lang/String;J)V",                  (void *) IjkMediaPlayer_setOptionLong },
    { "_getColorFormatName",    "(I)Ljava/lang/String;",    (void *) IjkMediaPlayer_getColorFormatName },
    { "_getVideoCodecInfo",     "()Ljava/lang/String;",     (void *) IjkMediaPlayer_getVideoCodecInfo },
    { "_getAudioCodecInfo",     "()Ljava/lang/String;",     (void *) IjkMediaPlayer_getAudioCodecInfo },
    { "_getMediaMeta",          "()Landroid/os/Bundle;",    (void *) IjkMediaPlayer_getMediaMeta },
    { "_setLoopCount",          "(I)V",                     (void *) IjkMediaPlayer_setLoopCount },
    { "_getLoopCount",          "()I",                      (void *) IjkMediaPlayer_getLoopCount },
    { "_getPropertyFloat",      "(IF)F",                    (void *) ijkMediaPlayer_getPropertyFloat },
    { "_setPropertyFloat",      "(IF)V",                    (void *) ijkMediaPlayer_setPropertyFloat },
    { "_getPropertyLong",       "(IJ)J",                    (void *) ijkMediaPlayer_getPropertyLong },
    { "_setPropertyLong",       "(IJ)V",                    (void *) ijkMediaPlayer_setPropertyLong },
    { "_setStreamSelected",     "(IZ)V",                    (void *) ijkMediaPlayer_setStreamSelected },
    { "native_profileBegin",    "(Ljava/lang/String;)V",    (void *) IjkMediaPlayer_native_profileBegin },
    { "native_profileEnd",      "()V",                      (void *) IjkMediaPlayer_native_profileEnd },
    { "native_setLogLevel",     "(I)V",                     (void *) IjkMediaPlayer_native_setLogLevel },
    { "_setFrameAtTime",        "(Ljava/lang/String;JJII)V", (void *) IjkMediaPlayer_setFrameAtTime },
};
#endif //PLAYERSDK_EXTRA_IJKMEDIA_IJKPLAYER_ANDROID_IJKPLAYER_JNI_H_
