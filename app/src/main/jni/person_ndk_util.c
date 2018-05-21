//
// Created by 宋泽嶒 on 2018/5/20.
//

#include "person_ndk_util.h"
#include "jni.h"
#include "android/log.h"

void updateInfo(JNIEnv* env, jclass type, jobject obj) {
    // 更改属性

    jclass clazz = (*env)->GetObjectClass(env, obj);
    if (clazz == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", "class为空");
        return;
    }

    jfieldID ageId = (*env)->GetFieldID(env, clazz, "age", "I"); // 获取属性id，参数列表:jni环境指针，目标类，属性名，属性类型(I为int)
    jint ageValue = (*env)->GetIntField(env, obj, ageId); // 获取int属性的值，参数列表:jni环境指针，目标对象，属性id
    ageValue++;
    (*env)->SetIntField(env, obj, ageId, ageValue);
}

jobject getPerson(JNIEnv* env, jclass type, jint age, jobject p) {
    // new对象

    jclass clazz = (*env)->GetObjectClass(env, p);
    jmethodID constructor = (*env)->GetMethodID(env, clazz, "<init>", "()V"); // 构造方法，名字被<init>替代，返回类型为void
    jobject person = (*env)->NewObject(env, clazz, constructor); // 构造一个对象

    jfieldID nameId = (*env)->GetFieldID(env, clazz, "name", "java/lang/String"); // 获取String类型的name属性
    jstring  nameValue = (*env)->GetObjectField(env, p, nameId);  // 获取对象属性的值

    jfieldID ageId = (*env)->GetFieldID(env, clazz, "age", "I");

    (*env)->SetObjectField(env, person, nameId, nameValue); // 设置属性的值
    (*env)->SetIntField(env, person, ageId, age);
    return person;
}

jobject getPerson2(JNIEnv* env, jclass type, jobject p, jint age) {
    // 调用java方法

    jclass clazz = (*env)->GetObjectClass(env, p);
    jmethodID methodId = (*env)->GetMethodID(env, clazz, "setAge", "(I)V");
    (*env)->CallVoidMethod(env, p, methodId, age);

    methodId= (*env)->GetMethodID(env, clazz, "setName", "(Ljava/lang/String;)V");
    (*env)->CallVoidMethod(env, p, methodId, (*env)->NewStringUTF(env, "Borne"));
    // 类似的还有CallVoidMethodA()和CallVoidMethodV()函数，只不过是给java方法传参方式不同
    // CallVoidMethodA()以jvalue指针的形式传参，CallVoidMethodV()以va_list方式传参，而CallVoidMethod()则以可变参数传参
    // 字符串参数要进行包装，否则会报错accessed stale global reference
    return p;
}

void callSuperMethod(JNIEnv* env, jclass type, jobject man) {
    jclass clazz = (*env)->FindClass(env, "com/example/songzeceng/myndkdemo/model/Person");
//    jclass clazz = (*env)->GetObjectClass(env, man);
    if (clazz == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", "person class not found");
        return;
    }
    jmethodID toString = (*env)->GetMethodID(env, clazz, "toString", "()Ljava/lang/String;");
    if (toString == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", "toString() method not found");
        return;
    }
    jstring str = (*env)->CallNonvirtualObjectMethod(env, man, clazz, toString);
    if (str == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", "toString() method returned null");
        return;
    }
    char* ch = (*env)->GetStringUTFChars(env, str, JNI_FALSE);
    __android_log_print(ANDROID_LOG_INFO, "person_ndk_util", ch);
}