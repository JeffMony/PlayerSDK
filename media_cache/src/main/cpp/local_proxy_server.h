//
// Created by jeffli on 2023/8/9.
//

#ifndef ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_LOCAL_PROXY_SERVER_H_
#define ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_LOCAL_PROXY_SERVER_H_

#include <pthread.h>
#include "common.h"
#include "event2/event.h"
#include "event2/listener.h"
#include "event2/bufferevent.h"

namespace cache {

class LocalProxyServer {
 public:
  LocalProxyServer();

  virtual ~LocalProxyServer();

  void Start();

  void Stop();

  void StartInternal();

 private:
  void Init();

 private:
  pthread_t socket_thread_;
  struct event_base *event_base_;
  struct evconnlistener *event_conn_listener_;
  bool is_running_;

};

}

#endif //ANDROIDMEDIACACHE_LIBRARY_SRC_MAIN_CPP_LOCAL_PROXY_SERVER_H_
