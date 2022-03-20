cmake_minimum_required(VERSION 3.4.1)

# debug和release的编译选项
SET (CMAKE_C_FLAGS_DEBUG          "-g")
SET (CMAKE_C_FLAGS_RELEASE        "-O2 -DNDEBUG")

set(EXTERN_DIR ../Clogan)

#添加一个子目录并构建该子目录
add_subdirectory(${EXTERN_DIR} clogan.out)
#将指定目录添加到头文件的搜索路径中
include_directories(${EXTERN_DIR})

#设置链接库搜索目录
link_directories(clogan.out)


find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

add_library(logan SHARED src/main/jni/clogan_protocol.c)
target_link_libraries(logan ${log-lib} z clogan)
