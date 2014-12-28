package com.ntu.sdp2.painthelper.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by WeiTang114 on 2014/12/22.
 */
public class NativeImgProcessor {

    public void blur(Bitmap bmp) {
        blur(bmp, 3);
    }
    public void blur(Bitmap bmp, int kernelSize) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeBlur(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr(), kernelSize);
        Utils.matToBitmap(matDst, bmp);
    }

    public void bilateralFilter(Bitmap bmp) {
        bilateralFilter(bmp, 16);
    }

    public void bilateralFilter(Bitmap bmp, int diameter) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeBilateralFilter(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr(), diameter);
        Utils.matToBitmap(matDst, bmp);
    }

    public void blackWhite(Bitmap bmp) {
        blackWhite(bmp, false);
    }

    public void blackWhiteInv(Bitmap bmp) {
        blackWhite(bmp, true);
    }

    public void blackWhite(Bitmap bmp, boolean inverse) {
        adaptiveThres(bmp, 512, 1, inverse, 41, 8);
    }


    public void adaptiveThres(Bitmap bmp, int maxThres, int methodIdx, boolean inverse, int blockSize, int C) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeAdaptiveThres(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr(), maxThres, methodIdx, (inverse?1:0), blockSize, C);
        Utils.matToBitmap(matDst, bmp);
    }

    public void skeletonize(Bitmap bmp) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeSkeletonize(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr());
        Utils.matToBitmap(matDst, bmp);
    }

    public void dilate(Bitmap bmp, int iteration) {
        dilate(bmp, iteration, 0, 1);
    }

    public void dilate(Bitmap bmp, int iteration, int kernelIdx, int kernelN) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeDilate(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr(), iteration, kernelIdx, kernelN);
        Utils.matToBitmap(matDst, bmp);
    }

    public void invert(Bitmap bmp) {
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeInvert(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr());
        Utils.matToBitmap(matDst, bmp);
    }

    private native void nativeBlur(long inputImg, long outputImg, int kernelSize);
    private native void nativeBilateralFilter(long inputImg, long outputImg, int diameter);
    private native void nativeAdaptiveThres(long inputImg, long outputImg, int maxThres, int methodIdx, int typeIdx, int blockSize, int C);
    private native void nativeSkeletonize(long inputImg, long outputImg);
    private native void nativeDilate(long inputImg, long outputImg, int iteration, int kernelIdx, int kernelN);
    private native void nativeInvert(long inputImg, long outputImg);
}