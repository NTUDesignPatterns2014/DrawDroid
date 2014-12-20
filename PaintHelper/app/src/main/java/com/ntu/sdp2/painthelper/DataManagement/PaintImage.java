package com.ntu.sdp2.painthelper.DataManagement;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Set;

/**
 * Created by lou on 2014/12/20.
 */
public class PaintImage {
    private String author;
    private String name;
    private Bitmap image;
    private Set<String> category;

    public String getAuthor(){
        return author;
    }

    public String getName(){
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public Set<String> getCategory() {
        return category;
    }
}
