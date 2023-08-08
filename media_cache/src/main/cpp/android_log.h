//
// Created by jeffli on 2023/3/3.
//

#ifndef ANDROIDIMAGERENDER_LIBRARY_SRC_MAIN_CPP_ANDROID_LOG_H_
#define ANDROIDIMAGERENDER_LIBRARY_SRC_MAIN_CPP_ANDROID_LOG_H_

#include <android/log.h>
#define TAG "media_cache"

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#endif //ANDROIDIMAGERENDER_LIBRARY_SRC_MAIN_CPP_ANDROID_LOG_H_
