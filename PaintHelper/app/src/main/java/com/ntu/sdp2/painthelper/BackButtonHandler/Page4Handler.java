package com.ntu.sdp2.painthelper.BackButtonHandler;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ntu.sdp2.painthelper.R;

/**
 * Created by lou on 2014/12/15.
 * Handler for Page 4
 * This only work for 1 level nested fragment.
 */
public class Page4Handler extends FragmentHandler {

    private Fragment mfragment;
    public Page4Handler(Fragment fragment){
        mfragment = fragment;
    }
    @Override
    void handlePop(Activity activity) {
        mfragment.getFragmentManager().popBackStackImmediate();
    }
}
