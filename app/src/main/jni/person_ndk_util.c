//
// Created by 宋泽嶒 on 2018/5/20.
//

#include "person_ndk_util.h"
#include "jni.h"
#include "android/log.h"

JNIEXPORT void JNICALL
updateInfo(JNIEnv* env, jclass type, jobject obj) {
    jclass clazz = (*env)->GetObjectClass(env, obj);
    if (clazz == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", "class为空");
        return;
    }

    jfieldID ageId = (*env)->GetFieldID(env, clazz, "age", "I");
    jint ageValue = (*env)->GetIntField(env, obj, ageId);
    ageValue++;
    (*env)->SetIntField(env, obj, ageId, ageValue);
}