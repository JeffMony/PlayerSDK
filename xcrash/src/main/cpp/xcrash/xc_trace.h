// Copyright (c) 2019-present, iQIYI, Inc. All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

// Created by caikelun on 2019-08-13.

#ifndef XC_TRACE_H
#define XC_TRACE_H 1

#include <stdint.h>
#include <setjmp.h>
#include <sys/types.h>
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    XC_TRACE_DUMP_NOT_START = 0,
    XC_TRACE_DUMP_ON_GOING,
    XC_TRACE_DUMP_ART_CRASH,
    XC_TRACE_DUMP_END
} xc_trace_dump_status_t;

extern xc_trace_dump_status_t xc_trace_dump_status;
extern sigjmp_buf      jmpenv;

int xc_trace_init(JNIEnv *env,
                  int rethrow,
                  unsigned int logcat_system_lines,
                  unsigned int logcat_events_lines,
                  unsigned int logcat_main_lines,
                  int dump_fds,
                  int dump_network_info);

#ifdef __cplusplus
}
#endif

#endif
