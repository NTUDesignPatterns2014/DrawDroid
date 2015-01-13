package com.ntu.sdp2.painthelper.DataManagement.Images;

import android.graphics.Bitmap;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public class PaintElement extends PaintImage {
    private int size;
    private Position position;
    public PaintElement(String author, String name, Bitmap image, String id, List<String> category, ParseUser user){
        super(author, name, image, id, category, user);
    }

}
