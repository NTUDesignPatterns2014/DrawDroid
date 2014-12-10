package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

/**
 * Setting Tab
 * using ListFragment
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class Page_4 extends ListFragment {

    // setting items
    private String[] list = {"Account", "item2", "item3"};
    private ArrayAdapter<String> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        this.setListAdapter(listAdapter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_4 = inflater.inflate(R.layout.page_4_frag, container, false);
        // ((TextView)page_4.findViewById(R.id.textView)).setText("Page 4");
        return page_4;
    }




}
