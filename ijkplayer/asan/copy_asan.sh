
# copy asan resource
# 同时修改 gradle.properties中的use_asan=true

CUR_DIR=$(pwd)

cp -rf ${CUR_DIR}/jniLibs ${CUR_DIR}/../src/main/
cp -rf ${CUR_DIR}/resources ${CUR_DIR}/../src/main/