package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.LogInCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CloudManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.DataManagement.ParseManager;
import com.ntu.sdp2.painthelper.capture.DummyCropFailActivity;
import com.ntu.sdp2.painthelper.capture.UploadDialogFragment;
import com.ntu.sdp2.painthelper.utils.SketchImage;
import com.parse.ParseUser;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Page_3 extends Fragment {
    private static final String TAG = "Page_3";
    public static final int UPLOAD_DIALOG_REQUEST_CODE = 888;
    public static final int CROP_REQUEST_CODE = 9153;

    private ImageView mImgView;
    private Button mBtnCapture;
    private Button mBtnUpload;
    private Button mBtnLoadTestImg;
    private SketchImage mImage;
    private Uri mImgCapturedUri;
    private Uri mCroppedUri;
    private int mStackLevel = 0;

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
                showDialog();
            }
        });
        mBtnLoadTestImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTestImage();
            }
        });


        File externalCacheDir = getActivity().getExternalCacheDir();
        mImgCapturedUri = Uri.fromFile(new File(externalCacheDir, "image.jpg"));
        mCroppedUri = Uri.fromFile(new File(externalCacheDir, "imagecropped.jpg"));


        return page_3;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        Log.i(TAG, "onActivityResult called, request = " + requestCode);
        // for parse login
        //ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

        if (requestCode == 0) {
            // from my camera startCamera~
            performCrop(mImgCapturedUri, mCroppedUri);
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
        else if (requestCode == CROP_REQUEST_CODE ) {
            Bitmap bmp;
            mImage = null;
            bmp = BitmapFactory.decodeFile(mImgCapturedUri.getPath());

            if (resultCode == Activity.RESULT_OK) {
                toast("Cropped, show Image:" + mCroppedUri.toString());
            }
            else {
                toast("Crop failed, do myLowClassCrop");
                bmp = myLowClassCrop(bmp);
            }

            mImgView.setImageBitmap(bmp);
            try {
                mImage = SketchImage.createFromPhoto(bmp);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            BitmapDrawable result = new BitmapDrawable(getResources(), scaleBitmap(mImage.getBitmapTransparent()));
            mImgView.setImageDrawable(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCamera()
    {
        Intent intentCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImgCapturedUri);
        startActivityForResult(intentCamera, 0);
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
        BitmapDrawable result = new BitmapDrawable(getResources(), scaleBitmap(mImage.getBitmapTransparent()));
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


    public void showDialog() {
        ParseUser user = ParseUser.getCurrentUser();
        if(user == null) {
            Log.i(TAG, "Not Logged in, Prompt for login");
            showLoginDialog();
        }
        else {
            mStackLevel++;

            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = UploadDialogFragment.newInstance(mImage.getBitmapTransparent());
            newFragment.setTargetFragment(this, UPLOAD_DIALOG_REQUEST_CODE);
            newFragment.show(ft, "dialog");
        }
    }


    void showLoginDialog() {
        String message = "Need Login to continue. \nLog In?";
        final CloudManagement cloudManager = (CloudManagement)((MainActivity)getActivity()).getCloudManager();
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log In")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseManager.logIn(getActivity(), new LogInCallBack() {
                            @Override
                            public void done(ParseUser parseUser) {
                                if (parseUser == null) {
                                    Log.i(TAG, "Log in unsuccessful");
                                } else {
                                    Log.i(TAG, "Log in successful");
                                    List<String> list = new ArrayList<>();
                                    list.add("Food");
                                    Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                                    Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.img);

                                    PaintImage paintImage = new PaintImage(parseUser.getUsername(), "Test", bmp, new String(), list, parseUser);
                                    if (cloudManager.saveImage(paintImage)) {
                                        Toast.makeText(getActivity(), "not loggin!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        Log.i(TAG, "User Logged out");
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    private void performCrop(Uri picUri, Uri outputUri) {
        try {
            int aspectX = 1200;
            int aspectY = 1200;

            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(picUri, "image/*");
            intent.putExtra("scale", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            startActivityForResult(intent, CROP_REQUEST_CODE);
        }
        catch (ActivityNotFoundException anfe) {
            Toast.makeText(getActivity(), "Your device doesn't support cropping, so.. do myLowClassCrop!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(getActivity(), DummyCropFailActivity.class);
            startActivityForResult(intent, CROP_REQUEST_CODE);
        }
    }


    /**
     *
     * @param bmp
     * @return a squared bitmap. May be the same object as bmp if no crop is needed.
     */
    private Bitmap myLowClassCrop(Bitmap bmp) {
        if (bmp == null) {
            throw new NullPointerException();
        }
        Bitmap out = null;
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (w == h) {
            out = bmp;
        }
        else if (w > h) {
            int x = (w - h) / 2;
            out = Bitmap.createBitmap(bmp, x, 0, h, h);
        }
        else {
            int y = (h - w) / 2;
            out = Bitmap.createBitmap(bmp, 0, y, w, w);
        }

        return out;
    }

    void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
