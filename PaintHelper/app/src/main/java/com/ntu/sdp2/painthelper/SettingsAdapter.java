package com.ntu.sdp2.painthelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by lou on 2014/12/10.
 */
public class SettingsAdapter extends FragmentPagerAdapter {

    enum pages{
        DEFAULT,
        ACCOUNT,
        INFO,
        DEFAULT_PAGE

    }

    public SettingsAdapter(FragmentManager fm) {
        super(fm);

    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new Page_4_default();
            //case 1:
                //return new Account_Info();

            default:
                return new Page_4_default();
        }


    }

    @Override
    public int getCount() {
        return 1;
    }
}
