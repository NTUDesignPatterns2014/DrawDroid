package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.ntu.sdp2.painthelper.Painter.ImageAdapter;
import com.ntu.sdp2.painthelper.Painter.MovableImageView;

public class Page_1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View page_1 = inflater.inflate(R.layout.page_1_frag, container, false);

        final GridView gridview = (GridView)page_1.findViewById(R.id.gridView);
        final ImageAdapter imageAdapter = new ImageAdapter(this.getActivity());
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovableImageView imageView = imageAdapter.getItem(position);
                RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                relativeLayout.addView(imageView);
                relativeLayout.removeViewInLayout(gridview);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
        relativeLayout.removeViewInLayout(gridview);

        final ArcMenu cornerButton = (ArcMenu)page_1.findViewById(R.id.arcMenu);
        int[] images = {
                R.drawable.composer_camera,
                R.drawable.composer_music,
                R.drawable.composer_with};
        final int itemCount = images.length;
        for (int i=0; i<itemCount; i++) {
            final ImageView item = new ImageView(this.getActivity());
            item.setImageResource(images[i]);
            final int position = i;
            cornerButton.addItem(item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                        relativeLayout.addView(gridview);
                        relativeLayout.bringChildToFront(gridview);
                    }
                    else if (position == 1) {
                        /*
                        public Bitmap takeScreenshot() {
                            View rootView = findViewById(android.R.id.content).getRootView();
                            rootView.setDrawingCacheEnabled(true);
                            return rootView.getDrawingCache();
                        }
                         */
                        cornerButton.setVisibility(View.INVISIBLE);
                        page_1.setDrawingCacheEnabled(true);
                        MovableImageView imageView = new MovableImageView(page_1.getContext());
                        imageView.setImageBitmap(page_1.getDrawingCache());
                        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                        relativeLayout.addView(imageView);
                    }
                }
            });
        }
        return page_1;
    }
}
