package com.ntu.sdp2.painthelper.DataManagement;

import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public interface DataManagement {
    public void getImageByCategory(String category);
    public void getElementByCategory(String category);
    public void getImageById(String id);
    public boolean saveImage(PaintImage image);
}
