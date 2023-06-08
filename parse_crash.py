# -*- coding:UTF-8 -*-
# Author : jeffmony@163.com
# Date : 28/07/21

import sys
import os
import re

NDK_HOME = '/Users/jefflee/tools/android-ndk-r14b'

ADDRLINE = '/toolchains/aarch64-linux-android-4.9/prebuilt/darwin-x86_64/bin/aarch64-linux-android-addr2line'

ADDRLINE_PATH = NDK_HOME + ADDRLINE


SO_PATH_SUFFIX = '/ijkplayer/build/intermediates/merged_native_libs/debug/out/lib/arm64-v8a'
# SO_PATH_SUFFIX = '/library/build/intermediates/merged_native_libs/debug/out/lib/armeabi-v7a'
# SO_PATH_SUFFIX = '/packages/1.9.0/cmake/release/obj/arm64-v8a'
# SO_PATH_SUFFIX = '/packages/1.9.0/cmake/release/obj/armeabi-v7a'

file_name = sys.argv[1]
file_object = open(file_name, 'r')
file_info = ''

try:
    so_dir = os.getcwd()
    so_dir = so_dir + SO_PATH_SUFFIX
    for line in file_object:
        stack = ''
        so_name = ''
        tempStr = line.strip('\n')
        start = tempStr.find('pc')
        tempStr = tempStr[start + 3:]
        tempStr = re.sub(' +',' ', tempStr)
        end = tempStr.find(' ')
        ## 找到具体的pc地址
        stack = tempStr[:end]
        tempStr = tempStr[end + 1:]
        end = tempStr.find(' ')
        if end != -1:
            tempStr = tempStr[:end]
        ## 找到so的名称,要求必须在特定的目录下
        so_name = tempStr[tempStr.rfind('/') + 1:]
        so_path = so_dir + '/' + so_name
        # print(so_path)
        print(stack)
        result = os.popen(ADDRLINE_PATH + ' -f -e ' + so_path + ' ' + stack).read()
        print(result)
finally:
    file_object.close()
