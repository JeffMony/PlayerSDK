//
// Created by jeffli on 2023/8/8.
//

#include "media_env.h"
#include <pthread.h>
#include "android_log.h"

static JavaVM* java_vm;
static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

int media_jni_set_java_vm(void *vm) {
  int ret = 0;
  pthread_mutex_lock(&lock);
  if (java_vm == NULL) {
    java_vm = vm;
  } else if (java_vm != vm) {
    ret = -1;
  }
  pthread_mutex_unlock(&lock);
  return ret;
}

JavaVM *media_jni_get_java_vm() {
  void *vm;
  pthread_mutex_lock(&lock);
  vm = java_vm;
  pthread_mutex_unlock(&lock);
  return vm;
}

int jni_get_env(JNIEnv **env) {
  JavaVM *vm = media_jni_get_java_vm();
  int ret = (*vm)->GetEnv(vm, (void **) env, JNI_VERSION_1_6);
  if (ret == JNI_EDETACHED) {
    if ((*vm)->AttachCurrentThread(vm, env, NULL) != JNI_OK) {
      LOGE("%s Failed to attach the JNI environment to the current thread", __func__);
      *env = NULL;
      return -10;
    }
  }
  return ret;
}

void jni_detach_thread_env() {
  JavaVM *vm = media_jni_get_java_vm();
  (*vm)->DetachCurrentThread(vm);
}