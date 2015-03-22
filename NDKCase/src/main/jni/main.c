#include <jni.h>
#include <android/log.h>
#include "com_ly_ndk_module_SayHello.h"

#ifndef LOG_TAG
#define LOG_TAG "ANDROID_LAB"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif


JNIEXPORT jstring JNICALL Java_com_ly_ndk_module_SayHello_getHelloFromC
  (JNIEnv * env, jobject jObj){
        return (*env)->NewStringUTF(env,"Hello From JNI!");
  }

