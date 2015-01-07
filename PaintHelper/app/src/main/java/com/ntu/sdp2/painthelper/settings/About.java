package com.ntu.sdp2.painthelper.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntu.sdp2.painthelper.R;

/**
 * Created by lou on 2015/1/6.
 */
public class About extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View about = inflater.inflate(R.layout.about_frag, container, false);
        return about;
    }
}
