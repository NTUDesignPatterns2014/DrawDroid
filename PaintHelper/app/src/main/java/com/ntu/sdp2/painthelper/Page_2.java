package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ntu.sdp2.painthelper.utils.NativeImgProcessor;
import com.ntu.sdp2.painthelper.utils.SketchImage;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class Page_2 extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;

    Bitmap bmp0;
    Bitmap bmpbw;
    Bitmap bmpSkl;
    Bitmap bmpDil;

    SketchImage mSketchImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e("Page2", "init failed");
        }

        View page_2 = inflater.inflate(R.layout.page_2_frag, container, false);

        btn1 = (Button) page_2.findViewById(R.id.btn1);
        btn2 = (Button) page_2.findViewById(R.id.btn2);
        btn3 = (Button) page_2.findViewById(R.id.btn3);
        btn4 = (Button) page_2.findViewById(R.id.btn4);

        img1 = (ImageView) page_2.findViewById(R.id.img1);
        img2 = (ImageView) page_2.findViewById(R.id.img2);
        img3 = (ImageView) page_2.findViewById(R.id.img3);
        img4 = (ImageView) page_2.findViewById(R.id.img4);


        bmp0 = decodeBitmapFromResource(getResources(), R.drawable.test1, 400, 400);


        img3.setImageBitmap(bmp0);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testBW();
                //testSkeleton();
                //testDilate();
                //testInvert();
                testSketchImage1();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testSketchImage2();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testSketchImage3();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        return page_2;
    }


    private void testSketchImage1() {
        mSketchImg = null;
        try {
            mSketchImg = SketchImage.createFromPhoto(bmp0);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        img1.setImageBitmap(bmp0);
        img2.setImageBitmap(mSketchImg.getBitmap());
    }

    private void testSketchImage2() {
        if (mSketchImg == null) {
            return;
        }
        mSketchImg.setLineWidth(3);
        img3.setImageBitmap(mSketchImg.getBitmap());
    }

    private void testSketchImage3() {
        if (mSketchImg == null) {

        }
        mSketchImg.resize(200, 200);
        img4.setScaleType(ImageView.ScaleType.CENTER);
        img4.setImageBitmap(mSketchImg.getBitmap());
    }

    private void testSketchImage4() {
        mSketchImg = null;
        try {
            mSketchImg = SketchImage.createFromPhoto(bmp0);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        img1.setImageBitmap(bmp0);
        img2.setImageBitmap(mSketchImg.getBitmap());

        mSketchImg.setLineWidth(3);
        NativeImgProcessor pr = new NativeImgProcessor();
        //pr.blackWhiteInv(mSketchImg.getBitmap());
        //skeletonize();
        //dilate(mDilateLevel);

        img3.setImageBitmap(mSketchImg.getBitmap());


        //mSketchImg.resize(600, 600);
        img4.setScaleType(ImageView.ScaleType.CENTER);
        img4.setImageBitmap(mSketchImg.getBitmap());
    }






    private void testBW() {
        /*
        SketchImage sImg = null;
        try {
            sImg = SketchImage.createFromPhoto(bmp0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */

        bmpbw = bmp0.copy(bmp0.getConfig(), true);

        NativeImgProcessor processor = new NativeImgProcessor();
        processor.blur(bmpbw);
        processor.bilateralFilter(bmpbw);
        processor.blackWhiteInv(bmpbw);
        img1.setImageBitmap(bmpbw);
        bmp0.recycle();
    }


    private void testSkeleton() {
        bmpSkl = bmpbw.copy(bmpbw.getConfig(), true);
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.skeletonize(bmpSkl);
        img2.setImageBitmap(bmpSkl);
        bmpbw.recycle();
    }

    private void testDilate() {
        bmpDil = bmpSkl.copy(bmpSkl.getConfig(), true);
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.dilate(bmpDil, 3);
        img3.setImageBitmap(bmpDil);
        bmpSkl.recycle();
    }

    private void testInvert() {
        Bitmap bmpInv = bmpbw.copy(bmpbw.getConfig(), true);
        NativeImgProcessor processor = new NativeImgProcessor();
        processor.invert(bmpInv);
        img4.setImageBitmap(bmpInv);

        //bmpDil.recycle();
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
