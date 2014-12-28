package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;

public class Page_2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_2 = inflater.inflate(R.layout.page_2_frag, container, false);
        ((TextView)page_2.findViewById(R.id.textView)).setText("Page 2");
        return page_2;
    }

    public void func(){
        ((MainActivity)getActivity()).getCloudManager().getImageByCategory("1", new ThumbCallBack() {
            @Override
            public void done(PaintImage paintImage) {

            }
        });
    }
}
