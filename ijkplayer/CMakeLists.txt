cmake_minimum_required(VERSION 3.4.1)
enable_language(ASM)

set(-DCMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")
set(-DCMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")

set(EXTRA_DIR ${CMAKE_CURRENT_LIST_DIR}/../extra)

add_definitions(
        "-DLIBYUV_NEON"
)
set(LIBYUV_DIR ${EXTRA_DIR}/ijkmedia/ijkyuv)

set(IJKSOUNDTOUCH_DIR ${EXTRA_DIR}/ijksoundtouch)

set(IJKJ4A_DIR ${EXTRA_DIR}/ijkj4a)

set(IJKSDL_DIR ${EXTRA_DIR}/ijksdl)

set(IJKPLAYER_DIR ${EXTRA_DIR}/ijkplayer)

set(FFMPEG_DIR ${EXTRA_DIR}/ffmpeg)

include_directories(${FFMPEG_DIR}/${ANDROID_ABI}/include)

include_directories(${LIBYUV_DIR}/include)

include_directories(${IJKSOUNDTOUCH_DIR}/include)

include_directories(${IJKJ4A_DIR})

include_directories(${IJKSDL_DIR})

include_directories(${IJKPLAYER_DIR})

file(
        ijkj4a_source
        GLOB
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


add_library(
        jm_player
        SHARED
        ${ijkj4a_source}
)

target_link_libraries(
        jm_player
        android
        OpenSLES
        EGL
        GLESv2
        log
)

