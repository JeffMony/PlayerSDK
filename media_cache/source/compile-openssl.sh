#!/bin/bash

# Author : JeffMony
# E-mail : jeffmony@163.com

# compile openssl

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

ARCH_PLATFORM=

ARCH=

ARCH1=

ARCH2=

ANDROID=

cd ${CURRENT_DIR}/openssl

init_arm() {
    echo "构建平台为：armeabi-v7a"
    ARCH_PLATFORM="armeabi-v7a"
    ARCH="android-arm"
    ARCH1=arm
    ARCH2=arch-arm
    ANDROID=androideabi
    CROSS_PREFIX=${TOOLCHAIN_PATH}/${ARCH1}-linux-${ANDROID}-4.9/prebuilt/${OS_TYPE}-x86_64/bin
}

init_arm64() {
    echo "构建平台为：arm64-v8a"
    ARCH_PLATFORM="arm64-v8a"
    ARCH="android-arm64"
    ARCH1=aarch64
    ARCH2=arch-arm64
    ANDROID=android
    CROSS_PREFIX=${TOOLCHAIN_PATH}/${ARCH1}-linux-${ANDROID}-4.9/prebuilt/${OS_TYPE}-x86_64/bin
}

build() {
    if [ ! -d "${CURRENT_DIR}/output" ]; then
        mkdir ${CURRENT_DIR}/output
    fi
    if [ ! -d "${CURRENT_DIR}/output/openssl" ]; then
        mkdir ${CURRENT_DIR}/output/openssl
    fi
    if [ ! -d "${CURRENT_DIR}/output/openssl/${ARCH_PLATFORM}" ]; then
        mkdir ${CURRENT_DIR}/output/openssl/${ARCH_PLATFORM}
    fi

    export PATH=${CROSS_PREFIX}:${PATH}
    rm -rf ${CURRENT_DIR}/output/openssl/${ARCH_PLATFORM}/*
    make clean

    ./Configure \
    ${ARCH} \
    no-shared \
    zlib-dynamic \
    -D__ANDROID_API__=${ANDROID_API} \
    --cross-compile-prefix=${CROSS_PREFIX}/${ARCH1}-linux-${ANDROID}- \
    --openssldir=${CURRENT_DIR}/output/openssl/${ARCH_PLATFORM} \
    --prefix=${CURRENT_DIR}/output/openssl/${ARCH_PLATFORM}


    make depend
    make
    make install_sw

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
        rm -rf ${CURRENT_DIR}/output/openssl/*
    ;;
esac

cd -