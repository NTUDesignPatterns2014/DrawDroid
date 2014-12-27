package com.ntu.sdp2.painthelper.DataManagement.CallBack;

import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;

/**
 * Created by lou on 2014/12/25.
 */
public interface OriginCallback extends CallBack{
    @Override
    void done(PaintImage paintImage);
}
