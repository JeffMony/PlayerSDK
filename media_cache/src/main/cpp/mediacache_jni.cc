//
// Created by jefflee on 2023/8/8.
//

#include <jni.h>
#include <android/log.h>
#include "proxy_cache_manager.h"

extern "C" {
#include "media_env.h"
}

#ifdef __cplusplus
extern "C" {
#endif

#ifndef NELEM
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#endif

#define PROXY_CACHE_MANAGER "com/jeffmony/mediacache/ProxyCacheManager"

static jlong PROXY_CACHE_MANAGER_CREATE(JNIEnv *env, jobject object) {
    auto proxy_cache_manager = new cache::ProxyCacheManager(object);
    return reinterpret_cast<jlong>(proxy_cache_manager);
}

static void PROXY_CACHE_MANAGER_START(JNIEnv *env, jobject object, jlong id) {
    auto proxy_cache_manager = reinterpret_cast<cache::ProxyCacheManager *>(id);
    proxy_cache_manager->Start();
}

static jstring PROXY_CACHE_MANAGER_GET_PROXY_URL(JNIEnv *env, jobject object, jlong id, jstring j_url) {
    auto proxy_cache_manager = reinterpret_cast<cache::ProxyCacheManager *>(id);
    auto url = env->GetStringUTFChars(j_url, JNI_FALSE);
    auto proxy_url = proxy_cache_manager->GetProxyUrl(url);
    jstring j_proxy_url = env->NewStringUTF(proxy_url.c_str());
    env->ReleaseStringUTFChars(j_url, url);
    return j_proxy_url;
}

static JNINativeMethod proxyCacheManagerMethods[] = {
    { "createHandler", "()J", (void **) PROXY_CACHE_MANAGER_CREATE },
    { "start", "(J)V", (void **) PROXY_CACHE_MANAGER_START },
    { "getProxyUrl", "(JLjava/lang/String;)Ljava/lang/String;", (void **) PROXY_CACHE_MANAGER_GET_PROXY_URL },
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    if ((vm)->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    media_jni_set_java_vm(vm);

    auto proxy_cache_manager = env->FindClass(PROXY_CACHE_MANAGER);
    jint proxy_cache_manager_result = env->RegisterNatives(proxy_cache_manager, proxyCacheManagerMethods, NELEM(proxyCacheManagerMethods));
    __android_log_print(ANDROID_LOG_ERROR, "media_cache", "proxy_cache_manager_result=%d", (proxy_cache_manager_result == JNI_OK));
    env->DeleteLocalRef(proxy_cache_manager);

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    if ((vm)->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
}

#ifdef __cplusplus
}
#endif