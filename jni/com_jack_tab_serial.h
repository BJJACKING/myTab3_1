/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_topeet_serial_test */

#ifndef _Included_com_topeet_serial_test
#define _Included_com_topeet_serial_test
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jack_tab_serial
 * Method:    Open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_jack_tab_serial_Open
  (JNIEnv *env, jobject obj, jint Port, jint Rate);

/*
 * Class:     com_topeet_serial_test
 * Method:    Close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_topeet_serial_test_serial_Close
  (JNIEnv *, jobject);

/*
 * Class:     com_jack_tab_serial
 * Method:    Read
 * Signature: ()I
 */
JNIEXPORT jintArray JNICALL Java_com_jack_tab_serial_Read
  (JNIEnv *, jobject);

/*
 * Class:     com_jack_tab_serial
 * Method:    Write
 * Signature: ()I
 */
JNIEXPORT jintArray JNICALL Java_com_jack_tab_serial_Write
  (JNIEnv *, jobject, jintArray, jint);

#ifdef __cplusplus
}
#endif
#endif
