# 删除 jniLibs中asan相关的代码
# 同时修改 gradle.properties中的use_asan=false

CUR_DIR=$(pwd)

rm -rf ${CUR_DIR}/../src/main/jniLibs/arm64-v8a/libclang_rt.asan-aarch64-android.so
rm -rf ${CUR_DIR}/../src/main/jniLibs/armeabi-v7a/libclang_rt.asan-arm-android.so

rm -rf ${CUR_DIR}/../src/main/resources

