package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.List;

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
        private int i;
        private String categroy;
        List<Bitmap> gridviewitems;
        private Bitmap bp=null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sort, container, false);
            Button01 = (Button) rootView.findViewById(R.id.Button01);
            Button02 = (Button) rootView.findViewById(R.id.Button02);
            Button01.setOnClickListener(new Button01listener());
            Button02.setOnClickListener(new Button02listener());
            final GridView gridview = (GridView)rootView.findViewById(R.id.gridView);
            final Myadapter adapter= new Myadapter(this.getActivity(),gridviewitems);
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

            i = getArguments().getInt(ARG_SORT_NUMBER);
            categroy = getResources().getStringArray(R.array.sort_array)[i];
                ((MainActivity)getActivity()).getCloudManager().getImageByCategory(categroy,new ThumbCallBack() {
                    @Override
                    public void done(PaintImage paintImage) {
                        //TODO
                    }
                });

            return rootView;
        }

        class Button01listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getCloudManager().getImageByCategory(categroy,new ThumbCallBack() {
                    @Override
                    public void done(PaintImage paintImage) {
                        //TODO
                    }
                });
            }
        }
        class Button02listener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getCloudManager().getImageByCategory(categroy,new ThumbCallBack() {
                    @Override
                    public void done(PaintImage paintImage) {
                        //TODO
                    }
                });
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
                setHasOptionsMenu(true);
                return rootView;
            }

            public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
                menu.add(0,1,1,"save");
                menu.add(0,2,2,"detail");
                super.onCreateOptionsMenu(menu,inflater);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if(item.getItemId() == 1){
                    //TODO
                }
                if(item.getItemId() == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("detail");
                    //builder.setMessage(Message);
                    builder.setPositiveButton("Exit",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int which){
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }
                return super.onOptionsItemSelected(item);
            }

            class imagelistener implements View.OnClickListener{

                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        }

    //public void func(){
       // ((MainActivity)getActivity()).getCloudManager().getImageByCategory("1", new ThumbCallBack() {
        //    @Override
         //   public void done(PaintImage paintImage) {

        //    }
       // });

    }
}



