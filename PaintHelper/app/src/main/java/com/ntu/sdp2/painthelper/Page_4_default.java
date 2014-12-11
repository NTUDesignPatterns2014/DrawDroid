package com.ntu.sdp2.painthelper;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by lou on 2014/12/10.
 * This is the setting tab...
 */
public class Page_4_default extends ListFragment{

    // add items here
    private final String[] list = {"Account", "Set Default Tab", "About"};
    ArrayAdapter<String> arrayAdapter;
    ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
        ListView listView = getListView();
        this.setListAdapter(arrayAdapter);


    }


    @Override
    public void onListItemClick (ListView l, View v, int position, long id){
        FragmentActivity fa = getActivity();
        FragmentManager fm = fa.getSupportFragmentManager();
        ViewPager vp = (ViewPager)fa.findViewById(R.id.pager);
        Fragment f4 = fm.findFragmentByTag("android:switcher:"+vp.getId()+":"+3);
        View view = f4.getView();
        viewPager = (ViewPager)view.findViewById(R.id.settingpager);
        switch(position) {
            case 0:
                viewPager.setCurrentItem(1);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                //TODO::THIS SHOULD NOT HAPPEN
                break;
        }


    }

}
