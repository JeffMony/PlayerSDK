//
// Created by jefflee on 2023/8/9.
//

#include "local_proxy_server.h"
#include "android_log.h"
#include <string.h>
#include <arpa/inet.h>
#include <netinet/in.h>


namespace cache {

static void *StartSocketThread(void *user_data);

static void listener_cb(struct evconnlistener *, evutil_socket_t,
                        struct sockaddr *, int socket_len, void *);
static void conn_write_cb(struct bufferevent *, void *);
static void conn_read_cb(struct bufferevent *, void *);
static void conn_event_cb(struct bufferevent *, short, void *);

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
        LOGE("Could not initialize libevent!");
        return;
    }
    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = inet_addr("127.0.0.1");
    sin.sin_port = htons(9995); /// 固定一个端口号

    event_conn_listener_ = evconnlistener_new_bind(event_base_, listener_cb, (void *) event_base_,
                                                   LEV_OPT_REUSEABLE | LEV_OPT_CLOSE_ON_FREE, -1,
                                                   (struct sockaddr *) &sin, sizeof(sin));

    if (!event_conn_listener_) {
        LOGE("Could not create a listener!");
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
    is_running_ = true;
    event_base_dispatch(event_base_);
    is_running_ = false;
}

static void listener_cb(struct evconnlistener *listener, evutil_socket_t fd,
                        struct sockaddr *sa, int socket_len, void *user_data) {
    struct event_base *base = (struct event_base*)user_data;
    struct bufferevent *bev;
    bev = bufferevent_socket_new(base, fd, BEV_OPT_CLOSE_ON_FREE);
    if (!bev) {
        LOGE("Error constructing bufferevent!");
        event_base_loopbreak(base);
        return;
    }

    /// 绑定读事件回调函数、写事件回调函数、错误事件回调函数
    bufferevent_setcb(bev, conn_read_cb, conn_write_cb, conn_event_cb, NULL);

    bufferevent_enable(bev, EV_WRITE);
    bufferevent_enable(bev, EV_READ);

    const char *szMsg = "hi client!";
    bufferevent_write(bev, szMsg, strlen(szMsg));
}

static void conn_write_cb(struct bufferevent *bev, void *user_data) {

}

static void conn_read_cb(struct bufferevent *bev, void *user_data) {

}

static void conn_event_cb(struct bufferevent *bev, short events, void *user_data) {
    bufferevent_free(bev);
}

}