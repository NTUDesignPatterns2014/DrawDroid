package com.ntu.sdp2.painthelper.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.IOException;

/**
 * Created by WeiTang114 on 2014/12/22.
 */
public class SketchImage {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    private Bitmap mBmpOriginal = null;
    private Bitmap mBmp = null;

    /**
     * The dilation level in non-negative integer. default: 0.
     */
    private int mDilateLevel = 0;
    private int mWidth = 0;
    private int mHeight = 0;

    public final Bitmap getBitmap() {
        return mBmp;
    }
    public int getWidth() {
        return mWidth;
    }
    public int getHeight() { return mHeight; }

    public final Bitmap getBitmapTransparent() {
        invert();
        Bitmap bmp = changeWhiteToTrans(mBmp);
        invert();
        return bmp;
    }


    public void resize(int width, int height) {
        // TODO

        if (width < mBmpOriginal.getWidth() || height < mBmpOriginal.getHeight()) {
            mBmp = Bitmap.createScaledBitmap(mBmpOriginal, mBmpOriginal.getWidth(), mBmpOriginal.getHeight(), false);

            //  dilate
            // TODO: check how much is necessary
            //dilate(1);
            mBmp = Bitmap.createScaledBitmap(mBmp, width, height, false);
        }
        else {
            mBmp = Bitmap.createScaledBitmap(mBmpOriginal, width, height, false);
        }

        invert();

        // black white
        blackWhiteInv();

        //  skeletonize
        skeletonize();

        // dilate
        dilate(mDilateLevel);

        mWidth = width;
        mHeight = height;
    }
    public void setLineWidth(int widthLevel) {
        int dilateLv = widthLevel;
        if (dilateLv == mDilateLevel) {
            return;
        }
        mDilateLevel = dilateLv;
        invert();
        blackWhiteInv();
        skeletonize();
        dilate(mDilateLevel);
    }

    public SketchImage copy() {
        SketchImage inst = new SketchImage();
        inst.mBmpOriginal = mBmpOriginal.copy(mBmpOriginal.getConfig(), true);
        inst.mBmp = mBmp.copy(mBmp.getConfig(), true);
        inst.mDilateLevel = mDilateLevel;
        return inst;
    }

    public void save(String path) {
        save(path, false);
    }

    /**
     *
     * @param path
     * @param bwInv true: bg=black, fg=white
     *              false: bg=white, fg=black
     */
    public void save(String path, boolean bwInv) {
        // TODO
    }


    public void invert() {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.invert(mBmp);
    }

    private static void changeColor(Bitmap bmp, int clrSrc, int clrDst) {
        if(bmp == null) {
            return;
        }
        // Source image size
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        //get pixels
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int x = 0; x < pixels.length; ++x) {
            pixels[x] = (pixels[x] == clrSrc) ? clrDst : pixels[x];
        }
        // create result bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, bmp.getConfig());
        //set pixels
        result.setPixels(pixels, 0, width, 0, 0, width, height);
    }


    private static Bitmap changeWhiteToTrans(Bitmap bmp) {
        Bitmap bmpOut = bmp.copy(bmp.getConfig(), true);
        changeColor(bmpOut, Color.WHITE, Color.TRANSPARENT);
        return bmpOut;
    }

    private static Bitmap changeTransToWhite(Bitmap bmp) {
        Bitmap bmpOut = bmp.copy(bmp.getConfig(), true);
        changeColor(bmp, Color.TRANSPARENT, Color.WHITE);
        return bmpOut;
    }


    private void dilate(int iteration) {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.dilate(mBmp, iteration);
    }


    private void blackWhiteInv() {
        adaptiveThreshold();
    }

    private void blur() {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.blur(mBmp);
        processor.bilateralFilter(mBmp);
    }

    private void adaptiveThreshold() {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.blackWhiteInv(mBmp);
    }

    private void skeletonize() {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.skeletonize(mBmp);
    }


    public static SketchImage createFromPhoto(Bitmap bmp) throws IOException {
        // TODO
        SketchImage inst = new SketchImage();
        inst.mBmp = Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false);
        inst.blur();
        inst.blackWhiteInv();
        inst.skeletonize();
        inst.mBmpOriginal = inst.mBmp.copy(inst.mBmp.getConfig(), true);
        inst.mWidth = WIDTH;
        inst.mHeight = HEIGHT;
        return inst;
    }

    public static SketchImage createFromSketch(Bitmap bmp) throws IOException {
        // TODO
        SketchImage inst = new SketchImage();
        inst.mBmp = changeTransToWhite(Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false));
        inst.invert();
        inst.mBmpOriginal = inst.mBmp.copy(inst.mBmp.getConfig(), true);
        inst.mWidth = WIDTH;
        inst.mHeight = HEIGHT;
        return inst;
    }
}
