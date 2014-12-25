package com.ntu.sdp2.painthelper.DataManagement.Images;

import android.graphics.Bitmap;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;

/**
 * Created by lou on 2014/12/20.
 */
public class PaintImage {
    private String author;
    private String name;
    private Bitmap image;
    private String id;
    private List<String> category;

    public PaintImage(String author, String name, Bitmap image, String id, List<String> category){
        this.author = author;
        this.name = name;
        this.image = image;
        this.id = id;
        this.category = category;

    }
    public String getAuthor(){
        return author;
    }

    public String getName(){
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public List<String> getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }
}
