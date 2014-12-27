package com.ntu.sdp2.painthelper.utils;

import android.graphics.Bitmap;

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

    public Bitmap getBitmap() {
        return mBmp;
    }
    public int getWidth() {
        return mWidth;
    }
    public int getHeight() { return mHeight; }


    public void resize(int width, int height) {
        // TODO
        if (width < getWidth() || height < getHeight()) {
            //  dilate
            // TODO: check how much is necessary
            dilate(3);
        }
        mBmp = Bitmap.createScaledBitmap(mBmp, width, height, false);

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


    private void dilate(int iteration) {
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.dilate(mBmp, iteration);
    }

    private void blackWhiteInv() {
        blur();
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
        inst.mBmp.createScaledBitmap(bmp, WIDTH, HEIGHT, false);
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
        inst.mBmp = Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false);
        inst.mBmpOriginal = inst.mBmp.copy(inst.mBmp.getConfig(), true);
        inst.mWidth = WIDTH;
        inst.mHeight = HEIGHT;
        return inst;
    }
}
