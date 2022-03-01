cmake_minimum_required(VERSION 3.4.1)

include(${ENSUREIT_DIR}/AndroidCMake/EnsureIT.ProtectedSharedLibrary.cmake)


# Debug flags
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -O0")

# Release flags
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -O0")

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)

find_library(
        log-lib
        log)


target_link_libraries(
        native-lib
        ${LIBS}
        ${log-lib})

ensureit_target_compile_options(native-lib)

if (ANDROID_ABI STREQUAL "arm64-v8a" OR ANDROID_ABI STREQUAL "x86_64")
    set(PATH_TO_LIBC "/system/lib64/libc.so")
else ()
    set(PATH_TO_LIBC "/system/lib/libc.so")
endif ()

# Set EnsureIT Protection Flags
ensureit_protection_flags(native-lib "-Wgs,-debug \
-Wgs,-v \
-Wcompile-gs,${PROJECT_SOURCE_DIR}/src/main/cpp/Arxan/GuardSpec.cpp \
-DNATIVEOBJECT=\"${PROJECT_SOURCE_DIR}/.externalNativeBuild/cmake/debug/${ANDROID_ABI}/libnative-lib.a\" \
-DLIBCPATH=\"${PATH_TO_LIBC}\"")