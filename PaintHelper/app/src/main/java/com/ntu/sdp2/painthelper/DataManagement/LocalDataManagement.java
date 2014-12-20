package com.ntu.sdp2.painthelper.DataManagement;

/**
 * Created by lou on 2014/12/20.
 */
public class LocalDataManagement implements DataManagement {
    public LocalDataManagement(){
    }
    public void getImageByCategory(String category){
    }
    public void getElementByCategory(String category){
    }
    public void getImageById(String id){
    }
    public boolean saveImage(PaintImage image){
        return false;
    }


    public LocalDataManagement getLocalManager(){
        return new ParseLocalManager();
    }
}
