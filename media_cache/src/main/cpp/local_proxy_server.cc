//
// Created by jeffli on 2023/8/9.
//

#include "local_proxy_server.h"
#include "log.h"
#include <string.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include "event2/buffer.h"
#include "request_parser.h"

namespace cache {

static void *StartSocketThread(void *user_data);

static void listener_cb(struct evconnlistener *, evutil_socket_t,
    struct sockaddr *, int socket_len, void *);
static void conn_write_cb(struct bufferevent *, void *);
static void conn_read_cb(struct bufferevent *, void *);
static void conn_event_cb(struct bufferevent *, short, void *);

static RequestParser *request_parser_;

LocalProxyServer::LocalProxyServer()
  : socket_thread_()
  , event_base_(nullptr)
  , event_conn_listener_(nullptr)
  , is_running_(false) {
  Init();
}

LocalProxyServer::~LocalProxyServer() {
}

static void *StartSocketThread(void *user_data) {
  auto proxy_server = reinterpret_cast<cache::LocalProxyServer *>(user_data);
  proxy_server->StartInternal();
  pthread_exit(nullptr);
}

void LocalProxyServer::Init() {
  event_base_ = event_base_new();
  if (!event_base_) {
    LOGI("%s %s %d Could not initialize libevent!", __FILE_NAME__, __func__ , __LINE__);
    return;
  }
  struct sockaddr_in sin;
  memset(&sin, 0, sizeof(sin));
  sin.sin_family = AF_INET;
  sin.sin_addr.s_addr = inet_addr(LOCAL_PROXY);
  sin.sin_port = htons(9995); /// 固定一个端口号

  event_conn_listener_ = evconnlistener_new_bind(event_base_, listener_cb, (void *) event_base_,
      LEV_OPT_REUSEABLE | LEV_OPT_CLOSE_ON_FREE, -1,
      (struct sockaddr *) &sin, sizeof(sin));

  if (!event_conn_listener_) {
    LOGI("%s %s %d Could not create a listener!", __FILE_NAME__, __func__ , __LINE__);
    return;
  }
}

void LocalProxyServer::Start() {
  pthread_attr_t attr;
  pthread_attr_init(&attr);
  pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
  pthread_create(&socket_thread_, &attr, StartSocketThread, this);
}

void LocalProxyServer::Stop() {
  if (event_base_ == nullptr) {
    return;
  }
  while (is_running_) {
    event_base_loopbreak(event_base_);
  }
  evconnlistener_free(event_conn_listener_);
  event_base_free(event_base_);
}

void LocalProxyServer::StartInternal() {
  if (event_base_ == nullptr) {
    return;
  }
  if (event_conn_listener_ == nullptr) {
    return;
  }
  LOGI("%s %s %d", __FILE_NAME__, __func__ , __LINE__);
  is_running_ = true;
  event_base_dispatch(event_base_);
  is_running_ = false;
}

static void listener_cb(struct evconnlistener *listener, evutil_socket_t fd,
                        struct sockaddr *sa, int socket_len, void *user_data) {
  LOGI("%s %s %d", __FILE_NAME__, __func__ , __LINE__);
  struct event_base *base = (struct event_base*)user_data;
  struct bufferevent *bev;
  bev = bufferevent_socket_new(base, fd, BEV_OPT_CLOSE_ON_FREE);
  if (!bev) {
    LOGI("%s %s %d Error constructing bufferevent!", __FILE_NAME__, __func__ , __LINE__);
    event_base_loopbreak(base);
    return;
  }

  /// 绑定读事件回调函数、写事件回调函数、错误事件回调函数
  bufferevent_setcb(bev, conn_read_cb, conn_write_cb, conn_event_cb, NULL);

  bufferevent_enable(bev, EV_WRITE);
  bufferevent_enable(bev, EV_READ);
}

static void conn_write_cb(struct bufferevent *bev, void *user_data) {
  LOGI("%s %s %d", __FILE_NAME__, __func__ , __LINE__);
}

static void conn_read_cb(struct bufferevent *bev, void *user_data) {
  LOGI("%s %s %d", __FILE_NAME__, __func__ , __LINE__);
  struct evbuffer *input_buffer = bufferevent_get_input(bev);
  int length = evbuffer_get_length(input_buffer);
  const char *buffer_body = (const char *) evbuffer_pullup(input_buffer, length);
  if (request_parser_) {
    delete request_parser_;
    request_parser_ = nullptr;
  }
  request_parser_ = new RequestParser(buffer_body);
  std::string url = request_parser_->GetUrl();
  LOGI("%s %s %d url=%s", __FILE_NAME__, __func__ , __LINE__, url.c_str());

}

static void conn_event_cb(struct bufferevent *bev, short events, void *user_data) {
  if (BEV_EVENT_CONNECTED == events) {
    LOGI("%s %s %d connect finish", __FILE_NAME__, __func__ , __LINE__);
  }
  bufferevent_free(bev);
}

}