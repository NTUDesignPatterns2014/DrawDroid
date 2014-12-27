package com.ntu.sdp2.painthelper.DataManagement.Images;

import android.graphics.Bitmap;

import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;

import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public class ThumbImage extends PaintImage {
    public ThumbImage(String author, String name, Bitmap image, String id, List<String> category){
        super(author, name, image, id, category);
    }
}
