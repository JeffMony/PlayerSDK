//
// Created by jeffli on 2023/8/8.
//

#include "proxy_cache_manager.h"
#include "base64/b64.h"

namespace cache {

ProxyCacheManager::ProxyCacheManager(jobject object) {
  local_proxy_server_ = new LocalProxyServer();
}

ProxyCacheManager::~ProxyCacheManager() {
  delete local_proxy_server_;
}

void ProxyCacheManager::Start() {
  local_proxy_server_->Start();
}

std::string ProxyCacheManager::GetProxyUrl(const char *url) {
  std::string result;
  result.append(HTTP_PROTOCOL);
  result.append(LOCAL_PROXY);
  result.append(":9995");
  result.append("/");
  int length = strlen(url);
  result.append(b64_encode(reinterpret_cast<const unsigned char *>(url), length));
  return result;
}

}