package com.ntu.sdp2.painthelper.capture;

import android.app.Activity;
import android.os.Bundle;

import com.ntu.sdp2.painthelper.R;

public class DummyCropFailActivity extends Activity {


    private int mRequestCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        //mRequestCode = savedInstanceState.getInt(getString(R.string.request_code_key));

        setResult(RESULT_FIRST_USER);
        finish();
    }



}
