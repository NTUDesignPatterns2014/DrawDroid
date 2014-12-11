package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

/**
 * Setting Tab
 * This is just a container...
 */

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Page_4 extends Fragment {

    private SettingsAdapter settingsAdapter;
    private ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_4 = inflater.inflate(R.layout.page_4_frag, container, false);
        // ((TextView)page_4.findViewById(R.id.textView)).setText("Page 4");
        // get view first
        viewPager = (ViewPager)page_4.findViewById(R.id.settingpager);
        viewPager.setAdapter(settingsAdapter);
        viewPager.setCurrentItem(0);
        return page_4;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        FragmentActivity fragmentActivity = this.getActivity();
        settingsAdapter = new SettingsAdapter(fragmentActivity.getSupportFragmentManager());
    }



}
