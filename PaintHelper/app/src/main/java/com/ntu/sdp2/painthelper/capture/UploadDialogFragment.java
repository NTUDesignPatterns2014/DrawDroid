package com.ntu.sdp2.painthelper.capture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.DataManagement.DataManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.MainActivity;
import com.ntu.sdp2.painthelper.R;

import java.util.ArrayList;

/**
 * Created by WeiTang114 on 2014/12/29.
 */
public class UploadDialogFragment extends DialogFragment {

    private final static String TAG = "UploadDialogFragment";

    private String mName = null;
    private String mCatagory = null;
    private Bitmap mImage = null;



    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static UploadDialogFragment newInstance(Bitmap image) {
        UploadDialogFragment f = new UploadDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("image", image);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = (Bitmap) getArguments().getParcelable("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*View v = inflater.inflate(R.layout.upload_dialog, container, false);
        View tv = v.findViewById(R.id.txt_type);

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.btn_showdialog);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((Page_3)getTargetFragment()).showDialog();
            }
        });

        return v;*/
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        View v = inflator.inflate(R.layout.upload_dialog, null);
        ImageView imgView = (ImageView) v.findViewById(R.id.img_uploadimg);
        Spinner cataSpin = (Spinner) v.findViewById(R.id.spin_catagory);
        EditText editName = (EditText) v.findViewById(R.id.edit_name);
        imgView.setImageBitmap(mImage);
        cataSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCatagory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });

        AlertDialog aDialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_launcher)
                .setView(v)
                .setTitle("Upload")
                .setPositiveButton("Upload", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .create();
        aDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final AlertDialog ad = (AlertDialog) dialog;
                Button posBtn = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (doPositiveClick()) {
                            ad.dismiss();
                        }
                    }
                });
            }
        });
        return aDialog;
    }

    public boolean doPositiveClick() {
        if (mName == null || mName.length() == 0) {
            toast("Name can't be empty");
            return false;
        }
        if (mCatagory == null) {
            toast("Catagory not chosen!");
            return false;
        }
        if (mImage == null) {
            toast("No image?");
            return false;
        }
        upload(mImage, mName, mCatagory);
        return true;
    }


    private void upload(Bitmap img, String name, String catagory) {
        PaintImage paintImage = null;
        paintImage = new PaintImage(null, null, null, null, null);
        paintImage.setName(name);
        paintImage.setImage(img);
        ArrayList<String> catagories = new ArrayList<String>();
        catagories.add(catagory);
        paintImage.setCategory(catagories);
        DataManagement parseManager = ((MainActivity) getActivity()).getCloudManager();
        if (parseManager.saveImage(paintImage)) {
            Log.i(TAG, "Upload failed: saveImage return true");
            toast("Login Facebook first!");
        }
        else {
            Log.i(TAG, "Upload running.");
        }
    }


    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}