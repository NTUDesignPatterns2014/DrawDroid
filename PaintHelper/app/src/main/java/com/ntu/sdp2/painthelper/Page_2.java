package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.OriginCallback;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.SaveCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.settings.Myadapter;

import java.util.List;

public class Page_2 extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View page_2 = inflater.inflate(R.layout.page_2_frag, container, false);
        mTitles = getResources().getStringArray(R.array.catagories);
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
        private int i;
        private String categroy;
        private String imageid;
        private PaintImage localpicture;
        private PaintImage cloudpicture;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sort, container, false);
            i = getArguments().getInt(ARG_SORT_NUMBER);
            categroy = getResources().getStringArray(R.array.catagories)[i];
            final GridView gridview = (GridView)rootView.findViewById(R.id.gridView);
            final Myadapter adapter= new Myadapter(this.getActivity());
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageid = adapter.getPiantImageID(position);
                    if(i==0){
                        ((MainActivity)getActivity()).getLocalManager().getImageById(imageid,new OriginCallback() {
                            @Override
                            public void done(PaintImage paintImage) {
                                localpicture = paintImage;
                                Fragment fragment = new ImageLocalFragment();
                                ((ImageLocalFragment) fragment).setImage(localpicture);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.content_frame, fragment);
                                transaction.addToBackStack("msg_fragment");
                                transaction.commit();
                            }
                        });
                    }
                    if(i!=0) {
                        ((MainActivity) getActivity()).getCloudManager().getImageById(imageid, new OriginCallback() {
                            @Override
                            public void done(PaintImage paintImage) {
                                cloudpicture = paintImage;
                                Fragment fragment = new ImageCloudFragment();
                                ((ImageCloudFragment) fragment).setImage(cloudpicture);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.content_frame, fragment);
                                transaction.addToBackStack("msg_fragment");
                                transaction.commit();
                            }
                        });
                    }
                }
            });

            if(i==0){
                ((MainActivity)getActivity()).getLocalManager().getImageByCategory("",adapter);
            }
            if(i!=0) {
                ((MainActivity) getActivity()).getCloudManager().getImageByCategory(categroy, adapter);
            }
            return rootView;
        }

        public static class ImageLocalFragment extends Fragment{
            private ImageView image;
            private PaintImage mpaintImage;

            public void setImage(PaintImage paintImage){
                mpaintImage = paintImage;
            }

            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState){
                View rootView = inflater.inflate(R.layout.image, container, false);
                image = (ImageView) rootView.findViewById(R.id.image);
                image.setImageBitmap(mpaintImage.getImage());
                image.setOnClickListener(new imagelistener());
                setHasOptionsMenu(true);
                return rootView;
            }

            public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
                menu.add(0,1,1,"delete");
                menu.add(0,2,2,"detail");
                super.onCreateOptionsMenu(menu,inflater);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if(item.getItemId() == 1){
                    ((MainActivity)getActivity()).getCloudManager().saveImage(mpaintImage);
                }
                if(item.getItemId() == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("detail");
                    String author=mpaintImage.getAuthor();
                    String name=mpaintImage.getName();
                    String id=mpaintImage.getId();
                    List<String> category=mpaintImage.getCategory();
                    String msg = "";
                    msg += "Author:"+author+"\n"+"Name:"+name+"\n"+"ID:"+id+"\n"+"Category:";
                    for (String cat : category) {
                        msg += (cat + " ");
                    }
                    builder.setMessage(msg);
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

        public static class ImageCloudFragment extends Fragment{
            private ImageView image;
            private PaintImage mpaintImage;

            public void setImage(PaintImage paintImage){
                mpaintImage = paintImage;
            }

            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState){
                View rootView = inflater.inflate(R.layout.image, container, false);
                image = (ImageView) rootView.findViewById(R.id.image);
                image.setImageBitmap(mpaintImage.getImage());
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
                    ((MainActivity)getActivity()).getCloudManager().saveImage(mpaintImage, new SaveCallBack() {
                        @Override
                        public void done() {
                            Toast.makeText(getActivity(), "Upload success!!", Toast.LENGTH_SHORT);
                        }
                    });
                }
                if(item.getItemId() == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("detail");
                    String author=mpaintImage.getAuthor();
                    String name=mpaintImage.getName();
                    String id=mpaintImage.getId();
                    List<String> category=mpaintImage.getCategory();
                    String msg = "";
                    msg += "Author:"+author+"\n"+"Name:"+name+"\n"+"ID:"+id+"\n"+"Category:";
                    for (String cat : category) {
                        msg += (cat + " ");
                    }
                    builder.setMessage(msg);
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
    }
}



