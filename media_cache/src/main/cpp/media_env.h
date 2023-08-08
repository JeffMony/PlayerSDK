//
// Created by jeffli on 2023/8/8.
//

#ifndef ANDROIDMEDIACACHE_MEDIA_ENV_H
#define ANDROIDMEDIACACHE_MEDIA_ENV_H

#include <jni.h>

int media_jni_set_java_vm(void* vm);

JavaVM* media_jni_get_java_vm();

int jni_get_env(JNIEnv** env);

void jni_detach_thread_env();

#endif //ANDROIDMEDIACACHE_MEDIA_ENV_H
