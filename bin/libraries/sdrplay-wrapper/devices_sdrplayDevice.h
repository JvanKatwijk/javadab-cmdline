/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class devices_sdrplayDevice */

#ifndef _Included_devices_sdrplayDevice
#define _Included_devices_sdrplayDevice
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     devices_sdrplayDevice
 * Method:    sdrplayInit
 * Signature: (IIZ)J
 */
JNIEXPORT jlong JNICALL Java_devices_sdrplayDevice_sdrplayInit
	(JNIEnv *, jobject, jint, jint, jboolean);

/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_getSamples
 * Signature: (J[FI)I
 */
JNIEXPORT jint JNICALL Java_devices_sdrplayDevice_sdr_1getSamples
	(JNIEnv *, jobject, jlong, jfloatArray, jint);

/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_samples
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_devices_sdrplayDevice_sdr_1samples
 	(JNIEnv *, jobject, jlong);

/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_resetBuffer
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_devices_sdrplayDevice_sdr_1resetBuffer
	(JNIEnv *, jobject, jlong);

/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_restartReader
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_devices_sdrplayDevice_sdr_1restartReader
	(JNIEnv *, jobject, jlong, jint);

/*
 * Class:     devices_sdrplayDevice
 * Method:    stopReader
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_devices_sdrplayDevice_sdr_1stopReader
	(JNIEnv *, jobject, jlong);


/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_setGain
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_devices_sdrplayDevice_sdr_1setGain
	(JNIEnv *, jobject, jlong, jint);
/*
 * Class:     devices_sdrplayDevice
 * Method:    sdr_autogain
 * Signature: (JI)V
 */
JNIEXPORT void
        JNICALL Java_devices_sdrplayDevice_sdr_1autoGain
                  (JNIEnv *env, jobject obj, jlong handle, jboolean gain);

#ifdef __cplusplus
}
#endif
#endif
