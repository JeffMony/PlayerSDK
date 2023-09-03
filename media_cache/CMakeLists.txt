cmake_minimum_required(VERSION 3.4.1)

# 开启汇编
enable_language(ASM)

set(-DCMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")
set(-DCMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--exclude-libs,ALL -O3 -flto -Wl,--gc-sections")

set(MEDIACACHE_DIR ${CMAKE_CURRENT_LIST_DIR}/../../MediaCache)

set(LIBEVENT_DIR ${MEDIACACHE_DIR}/libevent)

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

include_directories(${MEDIACACHE_DIR}/source)
include_directories(${MEDIACACHE_DIR}/source/base64)
include_directories(${MEDIACACHE_DIR}/source/cache)
include_directories(${MEDIACACHE_DIR}/source/common)
include_directories(${MEDIACACHE_DIR}/source/listener)
include_directories(${MEDIACACHE_DIR}/source/network)

file(
        GLOB
        mediacache_source
        ${MEDIACACHE_DIR}/source/base64/buffer.c
        ${MEDIACACHE_DIR}/source/base64/decode.c
        ${MEDIACACHE_DIR}/source/base64/encode.c
        ${MEDIACACHE_DIR}/source/common/proxy_settings.cc
        ${MEDIACACHE_DIR}/source/common/string_utils.cc
        ${MEDIACACHE_DIR}/source/network/request_parser.cc
        ${MEDIACACHE_DIR}/source/network/url_connection.cc
        ${MEDIACACHE_DIR}/source/proxy_cache.cc
        ${MEDIACACHE_DIR}/source/proxy_manager.cc
)

include_directories(${MEDIACACHE_DIR}/android)
include_directories(${MEDIACACHE_DIR}/android/jni)

file(
        GLOB
        android_platform_source
        ${MEDIACACHE_DIR}/android/jni/media_cache_env.c
        ${MEDIACACHE_DIR}/android/media_cache_jni.cc
        ${MEDIACACHE_DIR}/android/proxy_manager_android.cc
        ${MEDIACACHE_DIR}/android/url_connection_android.cc
)

include_directories(${MEDIACACHE_DIR}/dist/android/openssl/${ANDROID_ABI}/include)

if (USE_OPENSSL STREQUAL "true")
    add_definitions("
    -DUSE_OPENSSL
    ")
    add_library(libcrypto STATIC IMPORTED)
    set_target_properties(libcrypto PROPERTIES IMPORTED_LOCATION ${MEDIACACHE_DIR}/dist/android/openssl/${ANDROID_ABI}/lib/libcrypto.a)

    add_library(libssl STATIC IMPORTED)
    set_target_properties(libssl PROPERTIES IMPORTED_LOCATION ${MEDIACACHE_DIR}/dist/android/openssl/${ANDROID_ABI}/lib/libssl.a)
endif()

add_library(
        media_cache
        SHARED
        ${libevent_source}
        ${mediacache_source}
        ${android_platform_source}
)

if (USE_OPENSSL STREQUAL "true")
    target_link_libraries(
            media_cache
            android
            log
            z
            m
            dl
            libssl
            libcrypto
    )
else()
    target_link_libraries(
            media_cache
            android
            log
            z
            m
            dl
    )
endif()