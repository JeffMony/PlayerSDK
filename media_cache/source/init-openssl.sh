#!/bin/bash

# Author : JeffMony
# E-mail : jeffmony@163.com

# download openssl source

OPENSSL_UPSTREAM=git@github.com:openssl/openssl.git
TAG_NAME=OpenSSL_1_1_1

CUR_DIR=$(pwd)

SOURCE_DIR=${CUR_DIR}/openssl

git clone ${OPENSSL_UPSTREAM} ${SOURCE_DIR}

cd ${SOURCE_DIR}

git checkout -b ${TAG_NAME} ${TAG_NAME}

cd -