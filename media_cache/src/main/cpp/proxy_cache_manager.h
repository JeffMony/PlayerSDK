//
// Created by jefflee on 2023/8/8.
//

#ifndef PLAYERSDK_MEDIA_CACHE_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_
#define PLAYERSDK_MEDIA_CACHE_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_

#include <jni.h>
#include <string>
#include "local_proxy_server.h"

namespace cache {

class ProxyCacheManager {
public:
    ProxyCacheManager(jobject object);

    virtual ~ProxyCacheManager();

    std::string GetProxyUrl(const char* url);

private:
    LocalProxyServer *local_proxy_server_;
};

}

#endif //PLAYERSDK_MEDIA_CACHE_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_
