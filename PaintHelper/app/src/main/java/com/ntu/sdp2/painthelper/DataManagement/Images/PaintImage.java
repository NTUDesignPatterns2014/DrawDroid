package com.ntu.sdp2.painthelper.DataManagement.Images;

import android.graphics.Bitmap;

import com.parse.ParseUser;

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
    private ParseUser parseUser;

    public PaintImage(String author, String name, Bitmap image, String id, List<String> category, ParseUser user){
        this.author = author;
        this.name = name;
        this.image = image;
        this.id = id;
        this.category = category;
        parseUser = user;

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

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }
}
