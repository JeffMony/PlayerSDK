//
// Created by jeffli on 2023/8/8.
//

#ifndef ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_
#define ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_

#include <jni.h>
#include <string>
#include "local_proxy_server.h"

namespace cache {

class ProxyCacheManager {
 public:
  ProxyCacheManager(jobject object);

  virtual ~ProxyCacheManager();

  void Start();

  std::string GetProxyUrl(const char* url);

 private:
  LocalProxyServer *local_proxy_server_;
};

}

#endif //ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_PROXY_CACHE_MANAGER_H_
