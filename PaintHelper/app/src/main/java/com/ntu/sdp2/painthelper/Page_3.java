package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntu.sdp2.painthelper.capture.NativeEdgeDetector;

import org.opencv.android.OpenCVLoader;

public class Page_3 extends Fragment {
    private static final String TAG = "Page_3";

    private ImageView mImgView;
    private boolean mCaptured;

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e(TAG, "init failed");
        }
        if (!mCaptured) {
            startCamera();
            mCaptured = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_3 = inflater.inflate(R.layout.page_3_frag, container, false);
        ((TextView)page_3.findViewById(R.id.textView)).setText("Page 3");
        mImgView = (ImageView) page_3.findViewById(R.id.imgview_capture);
        return page_3;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(TAG, "onActivityResult called, request = " + requestCode);
        if (requestCode == 0) {
            // from my camera startCamera~
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            mImgView.setImageBitmap(bmp);
            Bitmap bmpEdge = detectEdge(bmp);
            mImgView.setImageBitmap(bmpEdge);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCamera()
    {
        Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_camera, 0);
    }

    private Bitmap detectEdge(Bitmap bmp) {
        NativeEdgeDetector detector = new NativeEdgeDetector();
        return detector.detectEdge(bmp);
    }




    /* Below is for OpenCV */
    /*
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    // Load native library after(!) OpenCV initialization

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    */
}
