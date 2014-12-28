
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>
#include <android/log.h>
#include <sstream>
#include <string>
#include "com_ntu_sdp2_painthelper_capture_NativeEdgeDetector.h"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"houghcircle.cpp",__VA_ARGS__)

using namespace cv;


const int max_lowThreshold = 100;
const int ratio = 3;
const int kernel_size = 3;

/**
 * @function CannyThreshold
 * @brief Trackbar callback - Canny thresholds input with a ratio 1:3
 */
void detectEdge(const Mat& src, Mat& dst, int lowThreshold)
{
    Mat srcGray, detectedEdges;

    /// Create a matrix of the same type and size as src (for dst)
    dst.create( src.size(), src.type() );

    /// Convert the image to grayscale
    cvtColor( src, srcGray, CV_BGR2GRAY );

    /// Reduce noise with a kernel 3x3
    blur( srcGray, detectedEdges, Size(3,3) );

    /// Canny detector
    Canny( detectedEdges, detectedEdges, lowThreshold, lowThreshold*ratio, kernel_size );

    /// Using Canny's output as a mask, we display our result
    dst = Scalar::all(0);

    src.copyTo( dst, detectedEdges);
 }


JNIEXPORT void JNICALL Java_com_ntu_sdp2_painthelper_capture_NativeEdgeDetector_nativeDetectEdge
  (JNIEnv *env, jobject clazz, jlong imgsrc, jlong imgdst)
{
    Mat& img = *(Mat*) imgsrc;
    Mat& imgOut = *(Mat*) imgdst;
    detectEdge(img, imgOut, 50);
}