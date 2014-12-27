
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "AdvMorph.h"
#include <stdio.h>
#include <android/log.h>
#include <string>
#include "com_ntu_sdp2_painthelper_capture_NativeEdgeDetector.h"
#include "com_ntu_sdp2_painthelper_utils_NativeImgProcessor.h"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"imgprocess.cpp",__VA_ARGS__)

using namespace cv;

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeBlur
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint kernelSize)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    Mat dst, srcGray;
    cvtColor(img, srcGray, CV_BGR2GRAY);
    dst.create(srcGray.size(), srcGray.type());

    blur(srcGray, dst, Size(kernelSize, kernelSize));
    dst.copyTo(imgOut);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeBilateralFilter
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint diameter)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    Mat dst, srcGray;
    cvtColor(img, srcGray, CV_BGR2GRAY);
    dst.create(srcGray.size(), srcGray.type());

    bilateralFilter(srcGray, dst, diameter, diameter * 2, diameter / 2);
    dst.copyTo(imgOut);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeAdaptiveThres
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst, jint maxThres, jint methodIdx, jint typeIdx, jint blockSize, jint C)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    Mat dst, srcGray;
    cvtColor(img, srcGray, CV_BGR2GRAY);
    dst.create(srcGray.size(), srcGray.type());

    int method = ((methodIdx == 0) ? ADAPTIVE_THRESH_MEAN_C : ADAPTIVE_THRESH_GAUSSIAN_C);
    int type = ((typeIdx == 0) ? THRESH_BINARY : THRESH_BINARY_INV);
    adaptiveThreshold(srcGray, dst, maxThres, method, type, blockSize, C);
    dst.copyTo(imgOut);
}

JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_utils_NativeImgProcessor_nativeSkeletonize
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;

    Mat dst, srcGray;
    cvtColor(img, srcGray, CV_BGR2GRAY);
    dst.create(srcGray.size(), srcGray.type());

    AdvMorph m;
    IplImage iplImg = IplImage(srcGray);
    IplImage iplImgOut = IplImage(dst);
    m.Thin(&iplImg, &iplImgOut);
    dst.copyTo(imgOut);
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

    Mat dst;
    dst.create(img.size(), img.type());
    bitwise_not(img, dst);
    dst.copyTo(imgOut);
}
