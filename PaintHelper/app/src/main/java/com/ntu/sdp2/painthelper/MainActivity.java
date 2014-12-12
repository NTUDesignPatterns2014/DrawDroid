package com.ntu.sdp2.painthelper;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends FragmentActivity {
    NonSwipeableViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (NonSwipeableViewPager)findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new NonSwipeableViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
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
            }};
        //Add New Tab
        actionBar.addTab(actionBar.newTab().setText("OAO1").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO2").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO3").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("OAO4").setTabListener(tabListener));


        // Generate HashKey
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ntu.sdp2.painthelper",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
