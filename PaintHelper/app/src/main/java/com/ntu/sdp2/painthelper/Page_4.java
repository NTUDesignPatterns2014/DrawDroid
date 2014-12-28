package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.settings.Settings;

public class Page_4 extends Fragment {
    private static final String TAG = "Page4_Frag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_4 = inflater.inflate(R.layout.page_4_frag, container, false);
        return page_4;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        Settings settingsFragment = new Settings();



        // Add the fragment to the 'page_4' FrameLayout
        try{
            getChildFragmentManager().beginTransaction()
                    .add(R.id.page_4, settingsFragment).commit();
        }catch( Exception ex ){
            Toast.makeText(getActivity(), "Setting Fragment Add Failed!!", Toast.LENGTH_SHORT).show();
            Log.d("Page_4", "Settings Fragment Add Failed !!!");
            //TODO: should do something

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
            for nested fragment, onActivityResult in nested fragment will not be invoked.
            LoginButton in Account_Info fragment works correctly if invoked onActivityResult.
            LoginButton.setFragment() should set this fragment( parent fragment of Account_Info )
        */

        getChildFragmentManager().findFragmentByTag(getString(R.string.account_info_frag_tag)).onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult!");
    }
}
