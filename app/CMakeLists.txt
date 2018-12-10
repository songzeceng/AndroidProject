cmake_minimum_required(VERSION 3.4.1)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/jni/my_ndk_util.c
             src/main/jni/person_ndk_util.c)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

set(CMAKE_C_FLAGS "-Wno-error=format-security")
set(CMAKE_CXX_FLAGS "-Wno-error=format-security")
# set(CMAKE_C_FLAGS "-isystem /Users/songzeceng/Library/Android/sdk/ndk-bundle/sysroot/usr/include/arm-linux-androideabi")
set(CMAKE_C_FLAGS "-isystem D:\\ProgramCoding\\Android\\SDK\\ndk-bundle\\sysroot\\usr\\include\\arm-linux-androideabi")
set(CMAKE_CXX_FLAGS "-isystem D:\\ProgramCoding\\Android\\SDK\\ndk-bundle\\sysroot\\usr\\include\\arm-linux-androideabi")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY /src/main/libs)

ADD_SUBDIRECTORY(src/main/jni/ndk1) # 添加子目录，加载子目录里的CMake
ADD_SUBDIRECTORY(src/main/jni/ndk2)