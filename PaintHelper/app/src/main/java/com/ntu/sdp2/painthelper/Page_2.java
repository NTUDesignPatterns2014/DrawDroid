package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.graphics.Bitmap;
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
        private ImageView image1 = null;
        private ImageView image2 = null;
        private ImageView image3 = null;
        private ImageView image4 = null;
        private ImageView image5 = null;
        private ImageView image6 = null;
        private ImageView image7 = null;
        private ImageView image8 = null;
        private ImageView image9 = null;
        private int[] picture = {R.drawable.ic_launcher,R.drawable.animal,R.drawable.botany,
                               R.drawable.people,R.drawable.food,R.drawable.building,
                               R.drawable.vehicle,R.drawable.groceries,R.drawable.people};
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
            image1 =(ImageView) rootView.findViewById(R.id.image1);
            image2 =(ImageView) rootView.findViewById(R.id.image2);
            image3 =(ImageView) rootView.findViewById(R.id.image3);
            image4 =(ImageView) rootView.findViewById(R.id.image4);
            image5 =(ImageView) rootView.findViewById(R.id.image5);
            image6 =(ImageView) rootView.findViewById(R.id.image6);
            image7 =(ImageView) rootView.findViewById(R.id.image7);
            image8 =(ImageView) rootView.findViewById(R.id.image8);
            image9 =(ImageView) rootView.findViewById(R.id.image9);
            image1.setOnClickListener(new image1listener());
            image2.setOnClickListener(new image2listener());
            image3.setOnClickListener(new image3listener());
            image4.setOnClickListener(new image4listener());
            image5.setOnClickListener(new image5listener());
            image6.setOnClickListener(new image6listener());
            image7.setOnClickListener(new image7listener());
            image8.setOnClickListener(new image8listener());
            image9.setOnClickListener(new image9listener());
            Button01.setOnClickListener(new Button01listener());
            Button02.setOnClickListener(new Button02listener());

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
                picture[0]=R.drawable.ic_launcher;
                picture[1]=R.drawable.ic_launcher;
                picture[2]=R.drawable.ic_launcher;
                picture[3]=R.drawable.ic_launcher;
                picture[4]=R.drawable.ic_launcher;
                picture[5]=R.drawable.ic_launcher;
                picture[6]=R.drawable.ic_launcher;
                picture[7]=R.drawable.ic_launcher;
                picture[8]=R.drawable.ic_launcher;
            }
            if(i == 1){
                picture[0]=R.drawable.animal;
                picture[1]=R.drawable.animal;
                picture[2]=R.drawable.animal;
                picture[3]=R.drawable.animal;
                picture[4]=R.drawable.animal;
                picture[5]=R.drawable.animal;
                picture[6]=R.drawable.animal;
                picture[7]=R.drawable.animal;
                picture[8]=R.drawable.animal;
            }
            if(i == 2){
                picture[0]=R.drawable.botany;
                picture[1]=R.drawable.botany;
                picture[2]=R.drawable.botany;
                picture[3]=R.drawable.botany;
                picture[4]=R.drawable.botany;
                picture[5]=R.drawable.botany;
                picture[6]=R.drawable.botany;
                picture[7]=R.drawable.botany;
                picture[8]=R.drawable.botany;
            }
            if(i == 3){
                picture[0]=R.drawable.people;
                picture[1]=R.drawable.people;
                picture[2]=R.drawable.people;
                picture[3]=R.drawable.people;
                picture[4]=R.drawable.people;
                picture[5]=R.drawable.people;
                picture[6]=R.drawable.people;
                picture[7]=R.drawable.people;
                picture[8]=R.drawable.people;
            }
            if(i == 4){
                picture[0]=R.drawable.food;
                picture[1]=R.drawable.food;
                picture[2]=R.drawable.food;
                picture[3]=R.drawable.food;
                picture[4]=R.drawable.food;
                picture[5]=R.drawable.food;
                picture[6]=R.drawable.food;
                picture[7]=R.drawable.food;
                picture[8]=R.drawable.food;
            }
            if(i == 5){
                picture[0]=R.drawable.building;
                picture[1]=R.drawable.building;
                picture[2]=R.drawable.building;
                picture[3]=R.drawable.building;
                picture[4]=R.drawable.building;
                picture[5]=R.drawable.building;
                picture[6]=R.drawable.building;
                picture[7]=R.drawable.building;
                picture[8]=R.drawable.building;
            }
            if(i == 6){
                picture[0]=R.drawable.vehicle;
                picture[1]=R.drawable.vehicle;
                picture[2]=R.drawable.vehicle;
                picture[3]=R.drawable.vehicle;
                picture[4]=R.drawable.vehicle;
                picture[5]=R.drawable.vehicle;
                picture[6]=R.drawable.vehicle;
                picture[7]=R.drawable.vehicle;
                picture[8]=R.drawable.vehicle;
            }
            if(i == 7){
                picture[0]=R.drawable.groceries;
                picture[1]=R.drawable.groceries;
                picture[2]=R.drawable.groceries;
                picture[3]=R.drawable.groceries;
                picture[4]=R.drawable.groceries;
                picture[5]=R.drawable.groceries;
                picture[6]=R.drawable.groceries;
                picture[7]=R.drawable.groceries;
                picture[8]=R.drawable.groceries;
            }

            image1.setImageResource(picture[0]);
            image2.setImageResource(picture[1]);
            image3.setImageResource(picture[2]);
            image4.setImageResource(picture[3]);
            image5.setImageResource(picture[4]);
            image6.setImageResource(picture[5]);
            image7.setImageResource(picture[6]);
            image8.setImageResource(picture[7]);
            image9.setImageResource(picture[8]);
            return rootView;
        }

        class Button01listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                picture[0]=R.drawable.vehicle;
                picture[1]=R.drawable.vehicle;
                picture[2]=R.drawable.vehicle;
                picture[3]=R.drawable.vehicle;
                picture[4]=R.drawable.vehicle;
                picture[5]=R.drawable.vehicle;
                picture[6]=R.drawable.vehicle;
                picture[7]=R.drawable.vehicle;
                picture[8]=R.drawable.vehicle;
                image1.setImageResource(picture[0]);
                image2.setImageResource(picture[1]);
                image3.setImageResource(picture[2]);
                image4.setImageResource(picture[3]);
                image5.setImageResource(picture[4]);
                image6.setImageResource(picture[5]);
                image7.setImageResource(picture[6]);
                image8.setImageResource(picture[7]);
                image9.setImageResource(picture[8]);
            }
        }
        class Button02listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                picture[0]=R.drawable.people;
                picture[1]=R.drawable.people;
                picture[2]=R.drawable.people;
                picture[3]=R.drawable.people;
                picture[4]=R.drawable.people;
                picture[5]=R.drawable.people;
                picture[6]=R.drawable.people;
                picture[7]=R.drawable.people;
                picture[8]=R.drawable.people;
                image1.setImageResource(picture[0]);
                image2.setImageResource(picture[1]);
                image3.setImageResource(picture[2]);
                image4.setImageResource(picture[3]);
                image5.setImageResource(picture[4]);
                image6.setImageResource(picture[5]);
                image7.setImageResource(picture[6]);
                image8.setImageResource(picture[7]);
                image9.setImageResource(picture[8]);
            }
        }

        class image1listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[0]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image2listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[1]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image3listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[2]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image4listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[3]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image5listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[4]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image6listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[5]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image7listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[6]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image8listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[7]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }
        class image9listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Fragment fragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt(ImageFragment.ARG_IMAGE, picture[8]);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.hide(fragment01);
                transaction.add(R.id.content_frame, fragment);
                transaction.addToBackStack("msg_fragment");
                transaction.commit();
            }
        }

        public static class ImageFragment extends Fragment{
            public static final String ARG_IMAGE = "image";
            private ImageView image;

            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState){
                View rootView = inflater.inflate(R.layout.image, container, false);
                image = (ImageView) rootView.findViewById(R.id.image);
                image.setImageResource(getArguments().getInt(ARG_IMAGE));
                image.setOnClickListener(new imagelistener());
                return rootView;
            }

            class imagelistener implements View.OnClickListener{

                @Override
                public void onClick(View v) {
                    //FragmentManager fragmentManager = getFragmentManager();
                    //FragmentTransaction transaction = fragmentManager.beginTransaction();
                    //transaction.show(fragment01);
                    //TODO
                }
            }
        }
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