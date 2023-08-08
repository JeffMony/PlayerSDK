#!/bin/bash

# Author : JeffMony
# E-mail : jeffmony@163.com

# download libevent source

LIBEVENT_UPSTREAM=git@github.com:JeffMony/libevent.git
BRANCH_NAME=feat/video_cache

CUR_DIR=$(pwd)

SOURCE_DIR=${CUR_DIR}/libevent

git clone ${LIBEVENT_UPSTREAM} ${SOURCE_DIR}

cd ${SOURCE_DIR}

git checkout ${BRANCH_NAME}

cd -