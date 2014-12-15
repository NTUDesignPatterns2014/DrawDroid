package com.ntu.sdp2.painthelper.BackButtonHandler;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Stack;

/**
 * Created by lou on 2014/12/15.
 * this class maintain all handler from different fragments.
 * Handler should be implemented for different pages.
 */
public class BackButtonHandler {
    private Stack<FragmentHandler> fragmentStack;
    public BackButtonHandler(){
        fragmentStack = new Stack<FragmentHandler>();
    }

    public void addToBackStack(FragmentHandler fragmentHandler){
        fragmentStack.push(fragmentHandler);
    }

    public boolean popBackStack(Activity activity){
        if(fragmentStack.empty()){
            return false;
        }else {
            FragmentHandler fragmentHandler = fragmentStack.pop();
            fragmentHandler.handlePop(activity);
            return true;
        }
    }
}
