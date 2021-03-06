/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class devices_airspyDevice */

#ifndef _Included_devices_airspyDevice
#define _Included_devices_airspyDevice
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     devices_airspyDevice
 * Method:    airspyInit
 * Signature: (IIZ)J
 */
JNIEXPORT jlong JNICALL Java_devices_airspyDevice_airspyInit
  (JNIEnv *, jobject, jint, jint, jboolean);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_getSamples
 * Signature: (J[FI)I
 */
JNIEXPORT jint JNICALL Java_devices_airspyDevice_airspy_1getSamples
  (JNIEnv *, jobject, jlong, jfloatArray, jint);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_samples
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_devices_airspyDevice_airspy_1samples
  (JNIEnv *, jobject, jlong);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_resetBuffer
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_devices_airspyDevice_airspy_1resetBuffer
  (JNIEnv *, jobject, jlong);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_restartReader
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_devices_airspyDevice_airspy_1restartReader
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_stopReader
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_devices_airspyDevice_airspy_1stopReader
  (JNIEnv *, jobject, jlong);

/*
 * Class:     devices_airspyDevice
 * Method:    airspy_setGain
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_devices_airspyDevice_airspy_1setGain
  (JNIEnv *, jobject, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
