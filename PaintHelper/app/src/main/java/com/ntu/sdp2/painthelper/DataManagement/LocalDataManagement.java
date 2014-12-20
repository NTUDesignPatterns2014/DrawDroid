package com.ntu.sdp2.painthelper.DataManagement;

import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public class LocalDataManagement implements DataManagement {
    LocalDataManagement manager;
    LocalDataManagement(){
        manager.getLocalManager();
    }
    public List<PaintImage> getImageByCategory(String category){
        return manager.getImageByCategory(category);
    }
    public List<PaintImage> getElementByCategory(String category){
        return manager.getElementByCategory(category);
    }
    public PaintImage getImageById(String id){
        return manager.getImageById(id);
    }
    public boolean saveImage(PaintImage image){
        return manager.saveImage(image);
    }


    public LocalDataManagement getLocalManager(){
        return new ParseDataManager();
    }

}
