# -*- coding:UTF-8 -*-
# Author : jeffli < jeffmony@163.com >
# Data   : 2020-09-07

import sys
import os
import re

NDK_HOME = '/Users/jefflee/tools/android-ndk-r14b'

ADDRLINE = '/toolchains/aarch64-linux-android-4.9/prebuilt/darwin-x86_64/bin/aarch64-linux-android-addr2line'

ADDRLINE_PATH = NDK_HOME + ADDRLINE

SO_PATH_SUFFIX = '/ijkplayer/build/intermediates/merged_native_libs/debug/out/lib/arm64-v8a'

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
        tempStr = tempStr[tempStr.rfind('/') + 1:]
        tempStr = tempStr[:len(tempStr) - 1]
        split_index = tempStr.find('+')
        so_name = tempStr[:split_index]
        print(so_name)
        stack = tempStr[split_index + 1:]
        so_path = so_dir + '/' + so_name
        result = os.popen(ADDRLINE_PATH + ' -f -e ' + so_path + ' ' + stack).read()
        print(result)
finally:
    file_object.close()
