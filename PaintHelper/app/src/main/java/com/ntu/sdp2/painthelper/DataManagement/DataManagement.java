package com.ntu.sdp2.painthelper.DataManagement;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.CallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ElementCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.OriginCallback;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.SaveCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;

/**
 * Created by lou on 2014/12/20.
 */
public interface DataManagement {
    public void getImageByCategory(String category, ThumbCallBack callBack);
    public void getElementByCategory(String category, ElementCallBack callBack);
    public void getImageById(String id, OriginCallback callBack);
    public boolean saveImage(PaintImage image, SaveCallBack saveCallBack);
}
