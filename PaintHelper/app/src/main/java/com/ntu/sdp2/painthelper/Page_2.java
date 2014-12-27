package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ntu.sdp2.painthelper.settings.Myadapter;


import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;

public class Page_2 extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_2 = inflater.inflate(R.layout.page_2_frag, container, false);
        //((TextView)page_2.findViewById(R.id.textView)).setText("Page 2");
        mTitles = getResources().getStringArray(R.array.sort_array);
        mDrawerLayout = (DrawerLayout) page_2.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) page_2.findViewById(R.id.left_drawer);

        return page_2;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment01 = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_SORT_NUMBER, position);
        fragment01.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment01).commit();

        // update selected item , then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }



    /**
     * Fragment that appears in the "content_frame", shows a photo
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_SORT_NUMBER = "sort_number";
        private Button Button01 = null;
        private Button Button02 = null;
        private Bitmap bp=null;
        private float scaleWidth;
        private float scaleHeight;
        private boolean num=false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sort, container, false);
            Button01 = (Button) rootView.findViewById(R.id.Button01);
            Button02 = (Button) rootView.findViewById(R.id.Button02);
            Button01.setOnClickListener(new Button01listener());
            Button02.setOnClickListener(new Button02listener());
            final GridView gridview = (GridView)rootView.findViewById(R.id.gridView);
            final Myadapter adapter= new Myadapter(this.getActivity());
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView imageView = (ImageView) adapter.getItem(position);
                    Fragment fragment = new ImageFragment();
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    ((ImageFragment)fragment).setImage(bitmap);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack("msg_fragment");
                    transaction.commit();

                }
            });

            //DisplayMetrics dm = new DisplayMetrics();
            //getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            //bp=BitmapFactory.decodeResource(getResources(),R.drawable.people);
            //int width=bp.getWidth();
            //int height=bp.getHeight();
            //int w=dm.widthPixels;
            //int h=dm.heightPixels;
            //scaleWidth=((float)w)/width;
            //scaleHeight=((float)h)/height;

            int i = getArguments().getInt(ARG_SORT_NUMBER);
            if(i == 0){
            //TODO
            }
            if(i == 1){
                //TODO
            }
            if(i == 2){
//TODO
            }
            if(i == 3){
//TODO
            }
            if(i == 4){
//TODO
            }
            if(i == 5){
//TODO
            }
            if(i == 6){
//TODO
            }
            if(i == 7){
//TODO
            }


            return rootView;
        }



        class Button01listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
            //TODO
            }
        }
        class Button02listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
            //TODO
            }
        }


        public static class ImageFragment extends Fragment{
            public static final String ARG_IMAGE = "image";
            private ImageView image;
            private Bitmap mybitmap;
            public void setImage(Bitmap bitmap){
                mybitmap = bitmap;
            }

            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState){
                View rootView = inflater.inflate(R.layout.image, container, false);
                image = (ImageView) rootView.findViewById(R.id.image);
                image.setImageBitmap(mybitmap);
                image.setOnClickListener(new imagelistener());
                return rootView;
            }

            class imagelistener implements View.OnClickListener{

                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        }

    public void func(){
        ((MainActivity)getActivity()).getCloudManager().getImageByCategory("1", new ThumbCallBack() {
            @Override
            public void done(PaintImage paintImage) {

            }
        });

    }
}







        /**
         *String planet = getResources().getStringArray(R.array.planets_array)[i];
          *int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
          *"drawable", getActivity().getPackageName());
          *((ImageView) rootView.findViewById(R.id.image1)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image2)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image3)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image4)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image5)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image6)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image7)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image8)).setImageResource(imageId);
          *((ImageView) rootView.findViewById(R.id.image9)).setImageResource(imageId);
         */