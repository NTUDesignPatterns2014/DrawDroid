package com.ntu.sdp2.painthelper.DataManagement;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by lou on 2014/12/20.
 */
public class ParseLocalManager extends LocalDataManagement {
    /*--------------------------------------------------------------*
                            Local Variables
     *--------------------------------------------------------------*/
    ParseManager manager = new ParseManager();


    /*--------------------------------------------------------------*
                                Public Methods
     *--------------------------------------------------------------*/

    @Override
    public void getImageByCategory(String category){
    }
    @Override
    public void getElementByCategory(String category){
    }
    @Override
    public void getImageById(String id){
    }
    @Override
    public boolean saveImage(PaintImage image){
        return false;
    }




}
