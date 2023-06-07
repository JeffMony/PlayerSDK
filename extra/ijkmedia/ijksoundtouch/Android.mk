# Copyright (c) 2017 Bilibili
# copyright (c) 2017 Raymond Zheng <raymondzheng1412@gmail.com>
#
# This file is part of ijkPlayer.
#
# ijkPlayer is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# ijkPlayer is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with ijkPlayer; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
# LOCAL_LDLIBS += -llog -landroid

# OpenMP mode : enable these flags to enable using OpenMP for parallel computation
# LOCAL_CFLAGS += -fopenmp
# LOCAL_LDFLAGS += -fopenmp

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(realpath $(LOCAL_PATH)/..)
LOCAL_C_INCLUDES += $(realpath $(LOCAL_PATH)/include/)

LOCAL_SRC_FILES += source/SoundTouch/AAFilter.cpp
LOCAL_SRC_FILES += source/SoundTouch/FIFOSampleBuffer.cpp
LOCAL_SRC_FILES += source/SoundTouch/FIRFilter.cpp
LOCAL_SRC_FILES += source/SoundTouch/cpu_detect_x86.cpp
LOCAL_SRC_FILES += source/SoundTouch/sse_optimized.cpp
LOCAL_SRC_FILES += source/SoundTouch/RateTransposer.cpp
LOCAL_SRC_FILES += source/SoundTouch/InterpolateCubic.cpp
LOCAL_SRC_FILES += source/SoundTouch/InterpolateLinear.cpp
LOCAL_SRC_FILES += source/SoundTouch/InterpolateShannon.cpp
LOCAL_SRC_FILES += source/SoundTouch/TDStretch.cpp
LOCAL_SRC_FILES += source/SoundTouch/BPMDetect.cpp
LOCAL_SRC_FILES += source/SoundTouch/PeakFinder.cpp
LOCAL_SRC_FILES += source/SoundTouch/SoundTouch.cpp
LOCAL_SRC_FILES += source/SoundTouch/mmx_optimized.cpp
LOCAL_SRC_FILES += ijksoundtouch_wrap.cpp

LOCAL_MODULE := ijksoundtouch
include $(BUILD_STATIC_LIBRARY)

$(call import-module,android/cpufeatures)
