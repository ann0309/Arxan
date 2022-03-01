#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_arxan_simplesimon_CongratsActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Simple Simon met a pieman going to the fair; \n"
            "Said Simple Simon to the pie man, let me taste your ware \n"
            "Said the pie man to Simple Simon, show me first your penny\n"
            "Said Simple Simon to the pie man, Sir, I have not any!\n"
            "\n"
            "The End.";
    return env->NewStringUTF(hello.c_str());
}
