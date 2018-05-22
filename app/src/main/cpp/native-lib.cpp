#include <jni.h>
#include <string.h>
#include "android/log.h"
#include "stdio.h"
#include "stdlib.h"

const char *key = "101010101";
int keyLen = strlen(key);
int fileIndex = 1;

jstring fileEncrypt(JNIEnv *env, jclass type, jstring filePath);
jstring fileDecrypt(JNIEnv *env, jclass type, jstring filePath);

// com.example.songzeceng.fileencryptanddecryptndk

JNINativeMethod method[] = {
        {"fileEncrypt", "(Ljava/lang/String;)Ljava/lang/String;", (void *) fileEncrypt},
        {"fileDecrypt", "(Ljava/lang/String;)Ljava/lang/String;", (void *) fileDecrypt}
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *other) {
    JNIEnv *env = NULL;

    if ((*vm).GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        __android_log_print(ANDROID_LOG_INFO, "native", "get env failed..");
        return -1;
    }

    char *className = "com/example/songzeceng/fileencryptanddecryptndk/FileUtil";
    jclass clazz = (*env).FindClass(className);
    if (clazz == NULL) {
        return -1;
    }

    if ((*env).RegisterNatives(clazz, method, sizeof(method) / sizeof(method[0])) < 0) {
        return -1;
    }

    return JNI_VERSION_1_6;
}

char *getNewName(const char *oldName) {
    int len = strlen(oldName);
    char *newName = new char[len + 1];
    int i = 0;

    for (; i < len && oldName[i] != '.'; ++i) {
        newName[i] = oldName[i];
    }

    newName[i] = '1' + fileIndex;
    fileIndex++;

    for (; i < len; ++i) {
        newName[i+1] = oldName[i];
    }

    return newName;
}


jstring fileEncrypt(JNIEnv *env, jclass type, jstring filePath) {
    __android_log_print(ANDROID_LOG_INFO, "native", "进入加密过程");
    fileIndex = 1;

    const char *path = (*env).GetStringUTFChars(filePath, JNI_FALSE);
    const char *newPath = getNewName(path);

    __android_log_print(ANDROID_LOG_INFO, "native", "new file path:%s", newPath);

    FILE *frp = fopen(path, "rb");
    FILE *fwp = fopen(newPath, "wb");

    if (frp == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "native", "%s:此文件不存在或没有读权限", path);
        return NULL;
    }

    if (fwp == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "native", "%s:没有写权限", newPath);
        return NULL;
    }

    int buf;
    int i = 0;

    while ((buf = fgetc(frp)) != EOF) {
        fputc(buf ^ key[i % keyLen], fwp);
        i++;
    }

    fclose(frp);
    fclose(fwp);
    free((void *) path);

    __android_log_print(ANDROID_LOG_INFO, "native", "文件加密结束");
    return env->NewStringUTF(newPath);
}

jstring fileDecrypt(JNIEnv *env, jclass type, jstring filePath) {
    __android_log_print(ANDROID_LOG_INFO, "native", "进入解密过程");
    fileIndex = 1;

    const char* path = env->GetStringUTFChars(filePath, JNI_FALSE);
    const char* newPath = getNewName(path);

    __android_log_print(ANDROID_LOG_INFO, "native", "new file path:%s", newPath);

    FILE* frp = fopen(path, "rb");
    FILE* fwp = fopen(newPath, "wb");

    if (frp == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "native", "%s:此文件不存在或没有读权限", path);
        return NULL;
    }

    if (fwp == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "native", "%s:没有写权限", newPath);
        return NULL;
    }

    int buf;
    int i = 0;

    while ((buf = fgetc(frp)) != EOF) {
        fputc(buf ^ key[i % keyLen], fwp);
        i++;
    }

    fclose(frp);
    fclose(fwp);
    free((void *) path);

    __android_log_print(ANDROID_LOG_INFO, "native", "文件解密结束");
    return env->NewStringUTF(newPath);
}