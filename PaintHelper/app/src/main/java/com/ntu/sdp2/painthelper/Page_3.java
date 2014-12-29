package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.DataManagement.DataManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.capture.NativeEdgeDetector;
import com.ntu.sdp2.painthelper.utils.SketchImage;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Page_3 extends Fragment {
    private static final String TAG = "Page_3";

    private ImageView mImgView;
    private Button mBtnCapture;
    private Button mBtnUpload;
    private Button mBtnLoadTestImg;
    private SketchImage mImage;
    private Uri mImgCapturedUri;

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e(TAG, "init failed");
        }
        /*if (!mCaptured) {
            startCamera();
            mCaptured = true;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_3 = inflater.inflate(R.layout.page_3_frag, container, false);
        mBtnCapture = (Button) page_3.findViewById(R.id.btn_capture);
        mBtnUpload = (Button) page_3.findViewById(R.id.btn_upload);
        mBtnLoadTestImg = (Button) page_3.findViewById(R.id.btn_testimage);
        mImgView = (ImageView) page_3.findViewById(R.id.imgview_capture);
        mBtnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        mBtnLoadTestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTestImage();
            }
        });
        return page_3;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(TAG, "onActivityResult called, request = " + requestCode);
        if (requestCode == 0) {
            // from my camera startCamera~

            File externalCacheDir = getActivity().getExternalCacheDir();
            mImgCapturedUri = Uri.fromFile(new File(externalCacheDir, "image.jpg"));

            Bitmap bmp;
            mImage = null;

            bmp = BitmapFactory.decodeFile(mImgCapturedUri.getPath());
            mImgView.setImageBitmap(bmp);
            try {
                mImage = SketchImage.createFromPhoto(bmp);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            mImage.invert();
            BitmapDrawable result = new BitmapDrawable(getResources(), scaleBitmap(mImage.getBitmap()));

            //mImgView.setImageBitmap(bmpEdge);
            mImgView.setImageDrawable(result);
        }
        else if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // This part of code is useless shit currently.

            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().andr
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                /*f (mBmpEdge != null) {
                    writeFileContent(uri, mBmpOri);
                }*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCamera()
    {
        File externalCacheDir = getActivity().getExternalCacheDir();
        mImgCapturedUri = Uri.fromFile(new File(externalCacheDir, "image.jpg"));
        Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImgCapturedUri);
        startActivityForResult(intentCamera, 0);
    }


    private Bitmap detectEdge(Bitmap bmp) {
        NativeEdgeDetector detector = new NativeEdgeDetector();
        return detector.detectEdge(bmp);
    }

    private void upload() {
        boolean canUpload = true;
        Bitmap bmp = null;
        PaintImage paintImage = null;
        if (mImage == null) {
            canUpload = false;
            toast("Please take a picture of your painting first.");
            return;
        }

        bmp = mImage.getBitmapTransparent();
        if (bmp == null) {
            toast("Please take a picture of your painting first.");
            return;
        }

        paintImage = new PaintImage(null, null, null, null, null);
        ArrayList<String> catagories = new ArrayList<String>();
        catagories.add("Vehicle");
        paintImage.setCategory(catagories);
        paintImage.setImage(mImage.getBitmapTransparent());
        paintImage.setName("Test Image By Tang");
        DataManagement parseManager = ((MainActivity) getActivity()).getCloudManager();
        if (parseManager.saveImage(paintImage)) {
            Log.i(TAG, "Page_3 upload failed: saveImage return true");
            toast("Login Facebook first!");
        }
        else {
            Log.i(TAG, "Page_3 upload running.");
        }
    }

    private void loadTestImage() {
        Bitmap bmp;
        mImage = null;

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.captureview_test);
        mImgView.setImageBitmap(bmp);
        try {
            mImage = SketchImage.createFromPhoto(bmp);
        } catch (IOException e) {
            toast("Load failed");
            e.printStackTrace();
            return;
        }
        mImage.invert();
        BitmapDrawable result = new BitmapDrawable(getResources(), scaleBitmap(mImage.getBitmap()));
        mImgView.setImageDrawable(result);
    }


    private void writeFileContent(Uri uri, Bitmap bmp)
    {
        try{
            ParcelFileDescriptor pfd =
                    getActivity().getContentResolver().
                            openFileDescriptor(uri, "w");
            FileOutputStream fos =
                    new FileOutputStream(pfd.getFileDescriptor());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Bitmap scaleBitmap(Bitmap bmp) {
        // Get current dimensions AND the desired bounding box
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
    }


    private int dpToPx(int dp)
    {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    // Here are some examples of how you might call this method.
    // The first parameter is the MIME type, and the second parameter is the name
    // of the file you are creating:
    //
    // createFile("text/plain", "foobar.txt");
    // createFile("image/png", "mypicture.png");

    // Unique request code.
    private static final int WRITE_REQUEST_CODE = 43;
    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
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

    void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
