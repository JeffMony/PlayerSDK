//
// Created by jefflee on 2023/8/8.
//

#include "proxy_cache_manager.h"
#include "event2/event.h"
#include "event2/http.h"

namespace cache {

ProxyCacheManager::ProxyCacheManager(jobject object) {
    local_proxy_server_ = new LocalProxyServer();
}

ProxyCacheManager::~ProxyCacheManager() {
    delete local_proxy_server_;
}

std::string ProxyCacheManager::GetProxyUrl(const char *url) {
    std::string result;
    result.append("http://127.0.0.1");
    result.append(":9995");
    result.append("/test.html");
    return result;
}

}