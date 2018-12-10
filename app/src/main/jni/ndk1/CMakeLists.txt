add_library( # Sets the name of the library.
             myLib1

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             ndk1.cpp)

target_link_libraries( # Specifies the target library.
                       myLib1

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )