package com.ntu.sdp2.painthelper.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ntu.sdp2.painthelper.R;

/**
 * Created by lou on 2014/12/12.
 */
public class Settings extends ListFragment {
    static final String[] itemList = {"Account", "Default Tab", "About"};
    //static Account_Info account_info = new Account_Info();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>((Activity)getActivity(), android.R.layout.simple_list_item_1, itemList);
        setListAdapter(itemAdapter);


    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(position){
            case 0:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(getId(), new Account_Info(), getString(R.string.account_info_frag_tag));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                //Toast.makeText((Activity)getActivity(), "add!!", Toast.LENGTH_LONG).show();

        }

    }
}
