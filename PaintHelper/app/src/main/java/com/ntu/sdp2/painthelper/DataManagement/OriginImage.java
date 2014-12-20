package com.ntu.sdp2.painthelper.DataManagement;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public class OriginImage extends PaintImage{
    OriginImage(String author, String name, Bitmap image, String id, List<String> category){
        super(author, name, image, id, category);
    }
}
