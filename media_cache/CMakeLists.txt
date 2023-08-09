cmake_minimum_required(VERSION 3.4.1)

# 开启汇编
enable_language(ASM)

set(-DCMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")
set(-DCMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--exclude-libs,ALL -O3 -flto -Wl,--gc-sections")

set(LIBEVENT_DIR ${CMAKE_CURRENT_LIST_DIR}/source/libevent)
set(MEDIACACHE_SOURCE_DIR ${CMAKE_CURRENT_LIST_DIR}/src/main/cpp)

set(THIRD_PARTY_DIR ${CMAKE_CURRENT_LIST_DIR}/source/output)

include_directories(${LIBEVENT_DIR})
include_directories(${LIBEVENT_DIR}/include)

file(
        GLOB
        libevent_source
        ${LIBEVENT_DIR}/buffer_locp.c
        ${LIBEVENT_DIR}/buffer.c
        ${LIBEVENT_DIR}/bufferevent_filter.c
        ${LIBEVENT_DIR}/bufferevent_openssl.c
        ${LIBEVENT_DIR}/bufferevent_pair.c
        ${LIBEVENT_DIR}/bufferevent_ratelim.c
        ${LIBEVENT_DIR}/bufferevent_sock.c
        ${LIBEVENT_DIR}/bufferevent.c
        ${LIBEVENT_DIR}/devpoll.c
        ${LIBEVENT_DIR}/epoll_sub.c
        ${LIBEVENT_DIR}/epoll.c
        ${LIBEVENT_DIR}/evdns.c
        ${LIBEVENT_DIR}/event_tagging.c
        ${LIBEVENT_DIR}/event.c
        ${LIBEVENT_DIR}/evmap.c
        ${LIBEVENT_DIR}/evport.c
        ${LIBEVENT_DIR}/evrpc.c
        ${LIBEVENT_DIR}/evthread_pthread.c
        ${LIBEVENT_DIR}/evthread.c
        ${LIBEVENT_DIR}/evutil_rand.c
        ${LIBEVENT_DIR}/evutil_time.c
        ${LIBEVENT_DIR}/evutil.c
        ${LIBEVENT_DIR}/http.c
        ${LIBEVENT_DIR}/kqueue.c
        ${LIBEVENT_DIR}/listener.c
        ${LIBEVENT_DIR}/log.c
        ${LIBEVENT_DIR}/poll.c
        ${LIBEVENT_DIR}/select.c
        ${LIBEVENT_DIR}/signal.c
        ${LIBEVENT_DIR}/strlcpy.c
)

include_directories(${MEDIACACHE_SOURCE_DIR})

file(
        GLOB
        mediacache_source
        ${MEDIACACHE_SOURCE_DIR}/local_proxy_server.cc
        ${MEDIACACHE_SOURCE_DIR}/proxy_cache_manager.cc
        ${MEDIACACHE_SOURCE_DIR}/media_env.c
        ${MEDIACACHE_SOURCE_DIR}/mediacache_jni.cc
)

include_directories(${THIRD_PARTY_DIR}/openssl/${ANDROID_ABI}/include)

add_library(libcrypto STATIC IMPORTED)
set_target_properties(libcrypto PROPERTIES IMPORTED_LOCATION ${THIRD_PARTY_DIR}/openssl/${ANDROID_ABI}/lib/libcrypto.a)

add_library(libssl STATIC IMPORTED)
set_target_properties(libssl PROPERTIES IMPORTED_LOCATION ${THIRD_PARTY_DIR}/openssl/${ANDROID_ABI}/lib/libssl.a)

add_library(
        mediacache
        SHARED
        ${libevent_source}
        ${mediacache_source}
)

target_link_libraries(
        mediacache
        android
        log
        z
        m
        dl
        libssl
        libcrypto
)