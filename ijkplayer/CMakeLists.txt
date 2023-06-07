cmake_minimum_required(VERSION 3.4.1)
enable_language(ASM)

set(-DCMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")
set(-DCMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Wall -s -Wdeprecated-declarations")

set(EXTRA_DIR ${CMAKE_CURRENT_LIST_DIR}/../extra)

add_definitions(
        "-DLIBYUV_NEON"
)
set(LIBYUV_DIR ${EXTRA_DIR}/ijkmedia/ijkyuv)

set(IJKJ4A_DIR ${EXTRA_DIR}/ijkj4a)

set(IJKPLAYER_DIR ${EXTRA_DIR}/ijkplayer)

set(IJKSDL_DIR ${EXTRA_DIR}/ijksdl)

set(IJKSOUNDTOUCH_DIR ${EXTRA_DIR}/ijksoundtouch)

include_directories(${LIBYUV_DIR}/include)

include_directories(${IJKSOUNDTOUCH_DIR}/include)




add_library(
        jm_player
        SHARED

)

target_link_libraries(
        jm_player
        android
        OpenSLES
        EGL
        GLESv2
        log
)

