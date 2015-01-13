package com.ntu.sdp2.painthelper.capture;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by WeiTang114 on 2014/12/14.
 */
public class NativeEdgeDetector {

    public Bitmap detectEdge(Bitmap bmp) {
        Bitmap bmpNew = Bitmap.createBitmap(bmp);
        Mat matSrc = new Mat();
        Mat matDst = new Mat();
        Utils.bitmapToMat(bmp, matSrc);
        nativeDetectEdge(matSrc.getNativeObjAddr(), matDst.getNativeObjAddr());
        Utils.matToBitmap(matDst, bmpNew);
        return bmpNew;
    }

    private native void nativeDetectEdge(long inputimage, long outputimage);
}
