

#include <jni.h>
#include <android/log.h>
#include <assert.h>

extern "C" {
#include "ijkplayer/android/ijkplayer_jni.h"
#include "ijksdl/android/ijksdl_android_jni.h"
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  JNIEnv* env = NULL;

  if ((vm)->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
    return -1;
  }
  assert(env != NULL);

  int sdl_ret = SDL_OnLoad(vm, env);

  int ijkplayer_ret = IjkMediaPlayer_OnLoad(env);

  if (sdl_ret == JNI_VERSION_1_4 && ijkplayer_ret == JNI_VERSION_1_4) {
    return JNI_VERSION_1_4;
  }
  return -1;
}

JNIEXPORT void JNI_OnUnLoad(JavaVM *vm, void *reserved) {
  IjkMediaPlayer_OnUnload();
}

#ifdef __cplusplus
}
#endif