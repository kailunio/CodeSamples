#include <jni.h>
#include "invertColor.h"

JNIEXPORT void JNICALL Java_com_example_NDKDemo_InvertColor_executeByC
  (JNIEnv *env, jclass c, jintArray arr)
{
	jint *img = env->GetIntArrayElements(arr, false);
	jsize size = env->GetArrayLength(arr);

	for(int i=0; i<size; i++){
		img[i] = (~img[i] & 0xffffff00);
	}
}


