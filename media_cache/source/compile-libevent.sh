#!/bin/bash

# Author : JeffMony
# E-mail : jeffmony@163.com

# compile libevent

:<<EOF
  执行结果为构建出 arm64-v8a、armeabi-v7a、x86_64、x86 架构的库文件
  参数说明：对应的数字表示构建出的平台架构的库文件
  1：arm64-v8a
  2：armeabi-v7a
  3：x86_64
  4：x86
EOF

# 构建的最低支持 API 等级
ANDROID_API=21
# 在什么系统上构建，mac：darwin，linux：linux，windows：windows
OS_TYPE=darwin
# 自己本机 NDK 所在目录
export ANDROID_NDK=/Users/jefflee/tools/android-ndk-r16b
# 交叉编译工具链所在目录
TOOLCHAIN_PATH=${ANDROID_NDK}/toolchains
CROSS_PREFIX=

# 当前目录
CURRENT_DIR=$(pwd)

ARCH=

ARCH_PREFIX=

ARCH_PLATFORM=

ARCH1=

ANDROID=

OPENSSL_LIB_DIR=

CC=

STRIP=

EXTRA_CFLAGS=

EXTRA_LDFLAGS=

cd ${CURRENT_DIR}/libevent

init_arm() {
    echo "构建平台为：armeabi-v7a"
    ARCH="arch-arm"
    ARCH_PREFIX="armeabi-v7a"
    ARCH1=arm
    ANDROID=androideabi
    CROSS_PREFIX=${TOOLCHAIN_PATH}/${ARCH1}-linux-${ANDROID}-4.9/prebuilt/${OS_TYPE}-x86_64/bin
    CC=${CROSS_PREFIX}/${ARCH1}-linux-${ANDROID}-gcc
    STRIP=${CROSS_PREFIX}/${ARCH1}-linux-${ANDROID}-strip
}

init_arm64() {
    echo "构建平台为：arm64-v8a"
    ARCH="arch-arm64"
    ARCH_PREFIX="arm64-v8a"
    ARCH1=aarch64
    ANDROID=android
    CROSS_PREFIX=${TOOLCHAIN_PATH}/${ARCH1}-linux-${ANDROID}-4.9/prebuilt/${OS_TYPE}-x86_64/bin
    CC=${CROSS_PREFIX}/${ARCH1}-linux-${ANDROID}-gcc
    STRIP=${CROSS_PREFIX}/${ARCH1}-linux-${ANDROID}-strip
}

build() {

    if [ ! -d "${CURRENT_DIR}/output" ]; then
        mkdir ${CURRENT_DIR}/output
    fi

    if [ ! -d "${CURRENT_DIR}/output/libevent" ]; then
        mkdir ${CURRENT_DIR}/output/libevent
    fi

    if [ ! -d "${CURRENT_DIR}/output/libevent/${ARCH_PREFIX}" ]; then
        mkdir ${CURRENT_DIR}/output/libevent/${ARCH_PREFIX}
    fi

    PREFIX=${CURRENT_DIR}/output/libevent/${ARCH_PREFIX}

    rm -rf ${PREFIX}

    OPENSSL_LIB_DIR=${CURRENT_DIR}/output/openssl/${ARCH_PREFIX}

    make clean

    EXTRA_CFLAGS=""
    EXTRA_LDFLAGS=""

    if [ -f "${OPENSSL_LIB_DIR}/lib/libssl.a" ]; then
        echo "OpenSSL detected"
        EXTRA_CFLAGS="${EXTRA_CFLAGS} -I${OPENSSL_LIB_DIR}/include"
        EXTRA_LDFLAGS="${EXTRA_LDFLAGS} -L${OPENSSL_LIB_DIR}/lib -lssl -lcrypto"
    fi

    EXTRA_CFLAGS="${EXTRA_CFLAGS} -I${ANDROID_NDK}/sysroot/usr/include -I${ANDROID_NDK}/sysroot/usr/include/${ARCH1}-linux-${ANDROID}"
    EXTRA_LDFLAGS="${EXTRA_LDFLAGS} -L${ANDROID_NDK}/platforms/android-${ANDROID_API}/${ARCH}/usr/lib -lc -ldl"

    # ./autogen.sh

    # export CC=${CC}

    ./configure \
    --disable-shared \
    --enable-static \
    --prefix=${PREFIX} \
    --host=${ARCH1}-linux-${ANDROID} \
    --with-sysroot=${ANDROID_NDK}/sysroot \
    --disable-samples \
    CFLAGS="" \
    CC=${CC} \
    STRIP=${STRIP} \
    CPPFLAGS="${EXTRA_CFLAGS}" \
    LDFLAGS="${EXTRA_LDFLAGS}"

    make

    make install

    echo "Done"
}


case "$1" in
    armv7a)
        init_arm
        build
    ;;
    arm64)
        init_arm64
        build
    ;;
    clean)
         rm -rf ${CURRENT_DIR}/output/libevent/*
    ;;
esac

cd -