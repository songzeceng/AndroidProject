//
// Created by 宋泽嶒 on 2018/5/20.
//

#ifndef MYNDKDEMO_PERSON_NDK_UTIL_H
#define MYNDKDEMO_PERSON_NDK_UTIL_H

#include <jni.h>
void updateInfo(JNIEnv* env, jclass type, jobject obj);
jobject getPerson(JNIEnv* env, jclass type, jint age, jobject p);
jobject getPerson2(JNIEnv* env, jclass type, jobject p, jint age);
void callSuperMethod(JNIEnv* env, jclass type, jobject man);

#endif //MYNDKDEMO_PERSON_NDK_UTIL_H

