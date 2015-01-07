package com.ntu.sdp2.painthelper.capture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.SaveCallBack;
import com.ntu.sdp2.painthelper.DataManagement.DataManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.MainActivity;
import com.ntu.sdp2.painthelper.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by WeiTang114 on 2014/12/29.
 */
public class UploadDialogFragment extends DialogFragment {

    private final static String TAG = "UploadDialogFragment";

    private String mName = null;
    private String mCatagory = null;
    private Bitmap mImage = null;

    private Handler mToastHandler = null;

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
        mToastHandler = new Handler() {
            final Context context = getActivity();
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return v;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflator = LayoutInflater.from(getActivity());
        View v = inflator.inflate(R.layout.upload_dialog, null);
        ImageView imgView = (ImageView) v.findViewById(R.id.img_uploadimg);
        final Spinner cataSpin = (Spinner) v.findViewById(R.id.spin_catagory);
        final EditText editName = (EditText) v.findViewById(R.id.edit_name);
        imgView.setImageBitmap(mImage);

        String[] oriCategories = getResources().getStringArray(R.array.catagories);
        String[] categories = Arrays.copyOfRange(oriCategories, 1, oriCategories.length - 1);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(categories));

        final Handler spinnerOpenHandler = new Handler();
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cataSpin.setAdapter(spinAdapter);
        cataSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCatagory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });
        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editName.clearFocus();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // Just wait for the input method closing then show the spinner at the right position
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            spinnerOpenHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cataSpin.performClick();
                                }
                            }, 100);
                        }
                    }).start();

                    return false;  // the system will keep doing its default action, which is to close the keyboard
                }
                return false;
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
        editName.requestFocus();
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
        paintImage = new PaintImage(null, null, null, null, null, null);
        paintImage.setName(name);
        paintImage.setImage(img);
        ArrayList<String> catagories = new ArrayList<String>();
        catagories.add(catagory);
        paintImage.setCategory(catagories);
        DataManagement parseManager = ((MainActivity) getActivity()).getCloudManager();
        if (parseManager.saveImage(paintImage, new SaveCallBack() {
            @Override
            public void done() {
                mToastHandler.sendMessage(Message.obtain(mToastHandler, 0, "Img Upload Success"));
            }
        })) {
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