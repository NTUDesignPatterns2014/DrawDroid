
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "AdvMorph.h"
#include <stdio.h>
#include <android/log.h>
#include <string>
#include "com_ntu_sdp2_painthelper_capture_NativeEdgeDetector.h"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"houghcircle.cpp",__VA_ARGS__)

using namespace cv;

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeBlur
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint kernelSize)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;
    blur(img, imgOut, Size(kernelSize, kernelSize));
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeBilateralFilter
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint diameter)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;
    bilateralFilter(img, imgOut, diameter, diameter * 2, diameter / 2);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeAdaptiveThres
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint maxThres, jint methodIdx, jint typeIdx, jint blockSize, jint C)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    int method = ((methodIdx == 0) ? ADAPTIVE_THRESH_MEAN_C : ADAPTIVE_THRESH_GAUSSIAN_C);
    int type = ((typeIdx == 0) ? THRESH_BINARY : THRESH_BINARY_INV);
    adaptiveThreshold(img, imgOut, maxThres, method, type, blockSize, C);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeSkeletonize
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    AdvMorph m;
    IplImage iplImg = IplImage(img);
    IplImage iplImgOut = IplImage(imgOut);
    m.Thin(&iplImg, &iplImgOut);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeDilate
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint iteration, jint kernelIdx, jint kernelN)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;
    Mat tmp;
    int dilateType;
    switch (kernelIdx) {
	case 0: dilateType = MORPH_RECT; break;
	case 1: dilateType = MORPH_CROSS; break;
	case 2: dilateType = MORPH_ELLIPSE; break;
	default: dilateType = MORPH_RECT;
    }

    Mat kernel = getStructuringElement(dilateType,
            Size(2 * kernelN + 1, 2 * kernelN + 1),
            Point(kernelN, kernelN));

    img.copyTo(tmp);
    for (int i = 0; i < iteration; i++) {
        dilate(tmp, imgOut, kernel);
        imgOut.copyTo(tmp);
    }
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeInvert
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;
    invert(img, imgOut);
}
