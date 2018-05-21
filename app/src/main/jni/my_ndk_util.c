//
// Created by 宋泽嶒 on 2018/5/18.
//

#include "jni.h"
#include "string.h"
#include "stdlib.h"
#include "android/log.h"
#include "assert.h"
#include "person_ndk_util.h"

const char *tag = "my_ndk_util";

jstring transfer(JNIEnv *env, jclass type, jstring str);
jint getNumber(JNIEnv* env, jclass type);

/*
 * 基本数据类型+void:后面不加;
 * 引用类型:后面加;
 */
static JNINativeMethod method[] = {
        {"getAppKey", "(Ljava/lang/String;)Ljava/lang/String;", (void*)transfer},
        {"updatePersonInfo", "(Lcom/example/songzeceng/myndkdemo/model/Person;)V", (void*)updateInfo},
        {"getPerson", "(ILcom/example/songzeceng/myndkdemo/model/Person;)Lcom/example/songzeceng/myndkdemo/model/Person;", (void*)getPerson},
        {"getPerson2", "(Lcom/example/songzeceng/myndkdemo/model/Person;I)Lcom/example/songzeceng/myndkdemo/model/Person;", (void*)getPerson2},
        {"callSuper", "(Lcom/example/songzeceng/myndkdemo/model/Man;)V", (void*)callSuperMethod}
        // jni方法数组，每一个元素表示一个java方法和native函数的对应关系
        // 元素的组成部分：java方法名，java方法签名((方法参数;)方法返回值类型;)，对应的native函数名
};

static int registNatives(JNIEnv* jniEnv) {
    char* className = "com/example/songzeceng/myndkdemo/MyNdkUtil"; // java方法所在的类名
    jclass clazz = (*jniEnv)->FindClass(jniEnv, className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }

    int methodNumber = sizeof(method) / sizeof(method[0]);
    __android_log_print(ANDROID_LOG_INFO, tag, "Jni method number:%d\n", methodNumber);

    if ((*jniEnv)->RegisterNatives(jniEnv, clazz, method, methodNumber) < 0) {
        // 真正的动态注册
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *javaVM, void *other) {
    // 在java文件里调用loadLibrary()时自动调用此方法

    __android_log_print(ANDROID_LOG_INFO, tag, "Jni onload...\n");
    JNIEnv *jniEnv = NULL;

    if ((*javaVM)->AttachCurrentThread(javaVM, (void**)&jniEnv, JNI_VERSION_1_6) != JNI_OK) {
        // 注意第二个参数，必须要转换成void**，否则会报错jni UnsatisfiedLinkError: JNI_ERR returned from JNI_OnLoad
        return -1;
    }

    assert(jniEnv != NULL);

    jint versionCode = (*jniEnv)->GetVersion(jniEnv);
    __android_log_print(ANDROID_LOG_INFO, tag, "Jni version:%d\n", versionCode);

    if (registNatives(jniEnv) != JNI_TRUE) {
        return -1;
    }

    return JNI_VERSION_1_6;
}

 char* getResult(const char *content) {
    if (content == NULL || strlen(content) > 100) {
        __android_log_print(ANDROID_LOG_INFO, tag, "内容无效");
        return NULL;
    }

    int i;
    int len = strlen(content);
    char *result = (char *) malloc((len + 1) * sizeof(char));

    memset(result, 0, sizeof(result));

    for (i = 0; i < len; ++i) {
        int offset = 0;
        if (content[i] >= 'a' && content[i] <= 'z') {
            offset = -32;
        } else if (content[i] >= 'A' && content[i] <= 'Z') {
            offset = 32;
        }
        result[i] = content[i] + offset;
    }

    result[i] = '\0';
    return result;
}

jstring transfer(JNIEnv *env, jclass type, jstring str) {
    const char *content = (*env)->GetStringUTFChars(env, str, JNI_FALSE);
    char *result = getResult(content);
    jstring resultJ = (*env)->NewStringUTF(env, result);

    free(content);
    free(result);
    return resultJ;
}

jint getNumber(JNIEnv* env, jclass type) {
    return 1;
}
