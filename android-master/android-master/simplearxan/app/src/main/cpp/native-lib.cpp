#include <jni.h>

static JNIEnv *mEnv;

extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_setEnv(JNIEnv *env, jclass object) {
    mEnv = env;
}

__attribute__((noinline)) __attribute__((optnone)) void root() {}

__attribute__((noinline)) __attribute__((optnone)) void rootHidingApps() {}

__attribute__((noinline)) __attribute__((optnone))  void rootManagementApps() {}

__attribute__((noinline)) __attribute__((optnone))  void rootRequiringApps() {}


extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_root(JNIEnv *env, jclass object) {

    root();
    rootHidingApps();
    rootManagementApps();
    rootRequiringApps();
}

extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_hook(JNIEnv *env, jclass object) {
}

extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_fdg(JNIEnv *env, jclass object) {
}

extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_debugger(JNIEnv *env, jclass object) {
}

extern "C" JNIEXPORT void JNICALL
Java_pet_ca_simplearxan_JNIUtils_checksum(JNIEnv *env, jclass object) {
}

__attribute__((nothrow)) void rootTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootHidingAppsTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootHidingAppsTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootHidingAppsNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootHidingAppsNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootManagementAppsTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootManagementAppsTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootManagementAppsNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootManagementAppsNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootRequiringAppsTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootRequiringAppsTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void rootRequiringAppsNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "rootRequiringAppsNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void hookTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "hookTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void hookNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "hookNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void fdgTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "fdgTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void fdgNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "fdgNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void debuggerTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "debuggerTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void debuggerNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "debuggerNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void checksumTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "checksumTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}

__attribute__((nothrow)) void checksumNonTamperAction() {
    jclass cls = mEnv->FindClass("pet/ca/simplearxan/CCFragment");
    jmethodID method = mEnv->GetStaticMethodID(cls, "checksumNonTamper", "()V");
    mEnv->CallStaticVoidMethod(cls, method);
}