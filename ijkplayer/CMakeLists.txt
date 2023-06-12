cmake_minimum_required(VERSION 3.4.1)
enable_language(ASM)

set(-DCMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")
set(-DCMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")

set(EXTRA_DIR ${CMAKE_CURRENT_LIST_DIR}/../third_party)

add_definitions("
-DLIBYUV_NEON
-DANDROID
-DNDEBUG
")

set(LIBYUV_DIR ${EXTRA_DIR}/ijkmedia/ijkyuv)

set(IJKSOUNDTOUCH_DIR ${EXTRA_DIR}/ijkmedia/ijksoundtouch)

set(IJKJ4A_DIR ${EXTRA_DIR}/ijkmedia/ijkj4a)

set(IJKSDL_DIR ${EXTRA_DIR}/ijkmedia/ijksdl)

set(IJKPLAYER_DIR ${EXTRA_DIR}/ijkmedia/ijkplayer)

set(FFMPEG_DIR ${EXTRA_DIR}/ffmpeg)

include_directories(${FFMPEG_DIR}/${ANDROID_ABI}/include)

include_directories(${EXTRA_DIR}/ijkmedia)

include_directories(${LIBYUV_DIR}/include)

include_directories(${IJKSOUNDTOUCH_DIR})

include_directories(${IJKSOUNDTOUCH_DIR}/include)

include_directories(${IJKJ4A_DIR})

include_directories(${IJKSDL_DIR})

include_directories(${IJKPLAYER_DIR})

include_directories(${EXTRA_DIR}/ijkprof/android-ndk-profiler-dummy/jni)

file(
        GLOB
        ijkj4a_source

        ${IJKJ4A_DIR}/j4a/j4a_allclasses.c
        ${IJKJ4A_DIR}/j4a/j4a_base.c
        ${IJKJ4A_DIR}/j4a/class/android/media/AudioTrack.c
        ${IJKJ4A_DIR}/j4a/class/android/media/MediaCodec.c
        ${IJKJ4A_DIR}/j4a/class/android/media/MediaFormat.c
        ${IJKJ4A_DIR}/j4a/class/android/media/PlaybackParams.c
        ${IJKJ4A_DIR}/j4a/class/android/os/Build.c
        ${IJKJ4A_DIR}/j4a/class/android/os/Bundle.c
        ${IJKJ4A_DIR}/j4a/class/java/nio/Buffer.c
        ${IJKJ4A_DIR}/j4a/class/java/nio/ByteBuffer.c
        ${IJKJ4A_DIR}/j4a/class/java/util/ArrayList.c
        ${IJKJ4A_DIR}/j4a/class/tv/danmaku/ijk/media/player/misc/IMediaDataSource.c
        ${IJKJ4A_DIR}/j4a/class/tv/danmaku/ijk/media/player/misc/IAndroidIO.c
        ${IJKJ4A_DIR}/j4a/class/tv/danmaku/ijk/media/player/IjkMediaPlayer.c
        ${IJKJ4A_DIR}/j4au/class/android/media/AudioTrack.util.c
        ${IJKJ4A_DIR}/j4au/class/java/nio/ByteBuffer.util.c
)

file(
        GLOB
        ijksdl_source

        ${IJKSDL_DIR}/ijksdl_aout.c
        ${IJKSDL_DIR}/ijksdl_audio.c
        ${IJKSDL_DIR}/ijksdl_egl.c
        ${IJKSDL_DIR}/ijksdl_error.c
        ${IJKSDL_DIR}/ijksdl_mutex.c
        ${IJKSDL_DIR}/ijksdl_stdinc.c
        ${IJKSDL_DIR}/ijksdl_thread.c
        ${IJKSDL_DIR}/ijksdl_timer.c
        ${IJKSDL_DIR}/ijksdl_vout.c
        ${IJKSDL_DIR}/ijksdl_extra_log.c
        ${IJKSDL_DIR}/gles2/color.c
        ${IJKSDL_DIR}/gles2/common.c
        ${IJKSDL_DIR}/gles2/renderer.c
        ${IJKSDL_DIR}/gles2/renderer_rgb.c
        ${IJKSDL_DIR}/gles2/renderer_yuv420p.c
        ${IJKSDL_DIR}/gles2/renderer_yuv444p10le.c
        ${IJKSDL_DIR}/gles2/shader.c
        ${IJKSDL_DIR}/gles2/fsh/rgb.fsh.c
        ${IJKSDL_DIR}/gles2/fsh/yuv420p.fsh.c
        ${IJKSDL_DIR}/gles2/fsh/yuv444p10le.fsh.c
        ${IJKSDL_DIR}/gles2/vsh/mvp.vsh.c

        ${IJKSDL_DIR}/dummy/ijksdl_vout_dummy.c

        ${IJKSDL_DIR}/ffmpeg/ijksdl_vout_overlay_ffmpeg.c
        ${IJKSDL_DIR}/ffmpeg/abi_all/image_convert.c

        ${IJKSDL_DIR}/android/android_audiotrack.c
        ${IJKSDL_DIR}/android/android_nativewindow.c
        ${IJKSDL_DIR}/android/ijksdl_android_jni.c
        ${IJKSDL_DIR}/android/ijksdl_aout_android_audiotrack.c
        ${IJKSDL_DIR}/android/ijksdl_aout_android_opensles.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediacodec_dummy.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediacodec_internal.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediacodec_java.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediacodec.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediadef.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediaformat_java.c
        ${IJKSDL_DIR}/android/ijksdl_codec_android_mediaformat.c
        ${IJKSDL_DIR}/android/ijksdl_vout_android_nativewindow.c
        ${IJKSDL_DIR}/android/ijksdl_vout_android_surface.c
        ${IJKSDL_DIR}/android/ijksdl_vout_overlay_android_mediacodec.c
)

file(
        GLOB
        ijkplayer_source

        ${IJKPLAYER_DIR}/ff_cmdutils.c
        ${IJKPLAYER_DIR}/ff_ffplay.c
        ${IJKPLAYER_DIR}/ff_ffpipeline.c
        ${IJKPLAYER_DIR}/ff_ffpipenode.c
        ${IJKPLAYER_DIR}/ijkmeta.c
        ${IJKPLAYER_DIR}/ijkplayer.c

        ${IJKPLAYER_DIR}/pipeline/ffpipeline_ffplay.c
        ${IJKPLAYER_DIR}/pipeline/ffpipenode_ffplay_vdec.c

        ${IJKPLAYER_DIR}/android/ffmpeg_api_jni.c
        ${IJKPLAYER_DIR}/android/ijkplayer_android.c
        ${IJKPLAYER_DIR}/android/ijkplayer_jni.c

        ${IJKPLAYER_DIR}/android/pipeline/ffpipeline_android.c
        ${IJKPLAYER_DIR}/android/pipeline/ffpipenode_android_mediacodec_vdec.c

        ${IJKPLAYER_DIR}/ijkavformat/allformats.c
        ${IJKPLAYER_DIR}/ijkavformat/ijklivehook.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkmediadatasource.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkio.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkiomanager.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkiocache.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkioffio.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkioandroidio.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkioprotocol.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkioapplication.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkiourlhook.c

        ${IJKPLAYER_DIR}/ijkavformat/ijkasync.c
        ${IJKPLAYER_DIR}/ijkavformat/ijkurlhook.c
        ${IJKPLAYER_DIR}/ijkavformat/ijklongurl.c
        ${IJKPLAYER_DIR}/ijkavformat/ijksegment.c

        ${IJKPLAYER_DIR}/ijkavutil/ijkdict.c
        ${IJKPLAYER_DIR}/ijkavutil/ijkutils.c
        ${IJKPLAYER_DIR}/ijkavutil/ijkthreadpool.c
        ${IJKPLAYER_DIR}/ijkavutil/ijktree.c
        ${IJKPLAYER_DIR}/ijkavutil/ijkfifo.c
        ${IJKPLAYER_DIR}/ijkavutil/ijkstl.cpp
)

file(
        GLOB
        ijksoundtouch_source

        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/AAFilter.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/FIFOSampleBuffer.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/FIRFilter.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/cpu_detect_x86.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/sse_optimized.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/RateTransposer.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/InterpolateCubic.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/InterpolateLinear.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/InterpolateShannon.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/TDStretch.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/BPMDetect.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/PeakFinder.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/SoundTouch.cpp
        ${IJKSOUNDTOUCH_DIR}/source/SoundTouch/mmx_optimized.cpp
        ${IJKSOUNDTOUCH_DIR}/ijksoundtouch_wrap.cpp
)

file(
        GLOB
        ijkyuv_common_source

        ${LIBYUV_DIR}/source/compare.cc
        ${LIBYUV_DIR}/source/compare_common.cc
        ${LIBYUV_DIR}/source/compare_posix.cc
        ${LIBYUV_DIR}/source/convert.cc
        ${LIBYUV_DIR}/source/convert_argb.cc
        ${LIBYUV_DIR}/source/convert_from.cc
        ${LIBYUV_DIR}/source/convert_from_argb.cc
        ${LIBYUV_DIR}/source/convert_to_argb.cc
        ${LIBYUV_DIR}/source/convert_to_i420.cc
        ${LIBYUV_DIR}/source/cpu_id.cc
        ${LIBYUV_DIR}/source/format_conversion.cc
        ${LIBYUV_DIR}/source/planar_functions.cc
        ${LIBYUV_DIR}/source/rotate.cc
        ${LIBYUV_DIR}/source/rotate_argb.cc
        ${LIBYUV_DIR}/source/rotate_mips.cc
        ${LIBYUV_DIR}/source/row_any.cc
        ${LIBYUV_DIR}/source/row_common.cc
        ${LIBYUV_DIR}/source/row_mips.cc
        ${LIBYUV_DIR}/source/row_posix.cc
        ${LIBYUV_DIR}/source/scale.cc
        ${LIBYUV_DIR}/source/scale_argb.cc
        ${LIBYUV_DIR}/source/scale_common.cc
        ${LIBYUV_DIR}/source/scale_mips.cc
        ${LIBYUV_DIR}/source/scale_posix.cc
        ${LIBYUV_DIR}/source/video_common.cc
)

if (${ANDROID_ABI} STREQUAL "arm64-v8a")
    file(
            GLOB
            ijkyuv_source

            ${LIBYUV_DIR}/source/compare_neon64.cc
            ${LIBYUV_DIR}/source/rotate_neon64.cc
            ${LIBYUV_DIR}/source/row_neon64.cc
            ${LIBYUV_DIR}/source/scale_neon64.cc
            ${ijkyuv_common_source}
    )
else()
    file(
            GLOB
            ijkyuv_source

            ${LIBYUV_DIR}/source/compare_neon.cc
            ${LIBYUV_DIR}/source/rotate_neon.cc
            ${LIBYUV_DIR}/source/row_neon.cc
            ${LIBYUV_DIR}/source/scale_neon.cc
            ${ijkyuv_common_source}
    )
endif()

add_library(libijkffmpeg SHARED IMPORTED)
set_target_properties(libijkffmpeg PROPERTIES IMPORTED_LOCATION ${EXTRA_DIR}/ffmpeg/${ANDROID_ABI}/libijkffmpeg.so)

add_library(
        jm_player
        SHARED
        ${ijkplayer_source}
        ${ijksdl_source}
        ${ijkj4a_source}
        ${ijksoundtouch_source}
        ${ijkyuv_source}
        ${EXTRA_DIR}/ijkmedia/media_jni.cc
        ${EXTRA_DIR}/ijkprof/android-ndk-profiler-dummy/jni/prof.c
)

target_link_libraries(
        jm_player
        android
        OpenSLES
        EGL
        GLESv2
        log
        libijkffmpeg
)

