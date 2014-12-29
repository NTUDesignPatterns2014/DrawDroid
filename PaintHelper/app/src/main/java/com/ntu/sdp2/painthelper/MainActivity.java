package com.ntu.sdp2.painthelper;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.ntu.sdp2.painthelper.BackButtonHandler.BackButtonHandler;
import com.ntu.sdp2.painthelper.BackButtonHandler.FragmentHandler;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CloudManagement;
import com.ntu.sdp2.painthelper.DataManagement.DataManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.DataManagement.LocalDataManagement;
import com.ntu.sdp2.painthelper.DataManagement.ParseLocalManager;
import com.ntu.sdp2.painthelper.DataManagement.ParseManager;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends FragmentActivity {
    NonSwipeableViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;
    BackButtonHandler backButtonHandler = new BackButtonHandler();
    DataManagement cloudManager;
    DataManagement localManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (NonSwipeableViewPager) findViewById(R.id.pager);
        // This make sure all fragment be alive
        Tab.setOffscreenPageLimit(3);
        Tab.setOnPageChangeListener(
                new NonSwipeableViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabReselected(ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };
        //Add New Tab
        actionBar.addTab(actionBar.newTab().setText("OAO1").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO2").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO3").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO4").setTabListener(tabListener));



        // Parse Init
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "DI2qUxaZ2sNxsc7u8D7gnLD13NzVGrCQbbsuNkzn", "AE4g2zci5ke08QFqfqRrQWy0BshRdhZNelNfwyui");
        ParseFacebookUtils.initialize("1405501183074777");
        // initialize data manager
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("First", false)){
            // not just installed
            localManager = new ParseLocalManager(false);
        }else{
            // just installed
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("First", true);
            localManager = new ParseLocalManager(true);
        }
        cloudManager = new ParseManager();

        // Generate HashKey
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.ntu.sdp2.painthelper", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    /*
        Default onBackPressed will not work if nested fragment presents.
        Only back stack of first level fragment would be popped.
        Nested fragments lie in back stack of ChildFragmentManager.
     */

    @Override
    public void onBackPressed() {
        if (backButtonHandler.popBackStack(this)) {
            // back button press handled
        } else {
            super.onBackPressed();
        }
    }

    public void addToBackStack(FragmentHandler fragmentHandler){
        backButtonHandler.addToBackStack(fragmentHandler);
    }

    public DataManagement getCloudManager(){
        return cloudManager;
    }

    public DataManagement getLocalManager() {
        return localManager;
    }
}