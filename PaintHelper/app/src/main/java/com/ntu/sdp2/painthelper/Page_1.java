package com.ntu.sdp2.painthelper;

/**
 * Created by JimmyPrime on 2014/10/26.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.OriginCallback;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.Painter.MovableImageView;
import com.ntu.sdp2.painthelper.settings.Myadapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Page_1 extends Fragment {
    Calendar rightNow;
    int counter=0;
    int image_id;
    String message="/sdcard/111222.jpg";

    boolean Sd_write=false;
    /* Checks if external storage is available for read and write */
    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);

        return file;
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void save_image(Bitmap bit) {
        String path="111";
        String Tag="save_image";
        Log.v(Tag,path);
        rightNow= Calendar.getInstance();
        String time=Integer.toString(rightNow.get(Calendar.MONTH))+Integer.toString(rightNow.get(Calendar.DATE))+Integer.toString(rightNow.get(Calendar.HOUR))+Integer.toString(rightNow.get(Calendar.SECOND))+counter;
        Toast.makeText(getActivity(),time,Toast.LENGTH_LONG).show();
        counter++;
        path=Environment.getExternalStorageDirectory().getPath();
        Log.v(Tag,path);
        if(Sd_write)  {
            File file=getAlbumStorageDir(getActivity().getApplicationContext(),"saved_img");

            path=file.getPath();
            File image_file=new File(path+"/"+time+".png");
            Log.e(Tag,"image_file :"+image_file.getPath());

            if(!file.exists()) file.mkdirs();
            else    Log.v(Tag,"dirExisted");
            if(!image_file.exists()) {
                try {
                    image_file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bit!=null) {

                Bitmap tmp = bit;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(image_file);
                    tmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (FileNotFoundException g) {
                    Log.e(Tag, "FileOouputStream Error");
                } catch (IOException m) {
                    Log.e(Tag, "IOException");
                }
            }
            else {
                Log.e(Tag,"image_view is null");
            }
            message=image_file.getPath();
        }
    }

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;

    private View page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View page_1 = inflater.inflate(R.layout.page_1_frag, container, false);

        mTitles = getResources().getStringArray(R.array.catagories);
        mDrawerLayout = (DrawerLayout) page_1.findViewById(R.id.drawerContainer);
        mDrawerList = (ListView) page_1.findViewById(R.id.leftDrawer);
/*
        final GridView gridview = (GridView)page_1.findViewById(R.id.gridView);
        final ImageAdapter imageAdapter = new ImageAdapter(this.getActivity());
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovableImageView imageView = imageAdapter.getItem(position);
                imageView.genSketchImage();
                imageView.setLineWidth(3);

                RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                relativeLayout.addView(imageView);
                relativeLayout.removeViewInLayout(gridview);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
        relativeLayout.removeViewInLayout(gridview);
*/
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
                        mDrawerLayout.openDrawer(mDrawerList);
                    }
                    else if (position == 1) {
                        cornerButton.setVisibility(View.INVISIBLE);
                        page_1.setDrawingCacheEnabled(true);
                        //MovableImageView imageView = new MovableImageView(page_1.getContext());
                        //imageView.setImageBitmap(page_1.getDrawingCache());
                        //RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                        //relativeLayout.addView(imageView);/
                        
                        save_image(page_1.getDrawingCache());

                        Intent intent = new Intent(getActivity(),TutorialInstantTracking.class);

                        intent.putExtra(MainActivity.EXTRA_MESSAGE,message);

                        startActivity(intent);
                    }
                }
            });

        }
        if(isExternalStorageWritable()) {

            Sd_write=true;
        }
        else {
            Toast.makeText(getActivity(),"SDCard_Removed!",Toast.LENGTH_LONG).show();
            Sd_write=false;
        }

        page = page_1;
        return page_1;
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
            FrameLayout frameLayout = (FrameLayout)page.findViewById(R.id.frameContainer);
            frameLayout.setVisibility(View.INVISIBLE);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment01 = new SubFragment();
        Bundle args = new Bundle();
        args.putInt(SubFragment.ARG_SORT_NUMBER, position);
        fragment01.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameContainer, fragment01).commit();

        // update selected item , then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

        FrameLayout frameLayout = (FrameLayout)page.findViewById(R.id.frameContainer);
        frameLayout.setVisibility(View.VISIBLE);
    }

    public static class SubFragment extends Fragment {

        public static final String ARG_SORT_NUMBER = "sort_number";
        private int i;
        private String category;
        private String imageid;
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sort, container, false);
            final GridView gridview = (GridView)rootView.findViewById(R.id.gridView);
            final Myadapter adapter= new Myadapter(this.getActivity());
            gridview.setAdapter(adapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageid = adapter.getPiantImageID(position);

                    ((MainActivity)getActivity()).getCloudManager().getImageById(imageid,new OriginCallback() {
                        @Override
                        public void done(PaintImage paintImage) {
                            MovableImageView imageView = new MovableImageView(container.getContext());
                            imageView.setImageBitmap(paintImage.getImage());
                            imageView.genSketchImage();
                            imageView.setLineWidth(3);

                            DrawerLayout drawerLayout = (DrawerLayout)container.getParent();
                            if (drawerLayout != null) {
                                drawerLayout.addView(imageView, 1);
                                container.setVisibility(View.INVISIBLE);
                            }
                            else {
                                Log.i("Painter", "layout null");
                                Log.i("Painter", container.getClass().toString());
                            }
                        }
                    });

                }
            });

            i = getArguments().getInt(ARG_SORT_NUMBER);
            category = getResources().getStringArray(R.array.catagories)[i];
            Log.i("Painter", "getImageByCategory");
            ((MainActivity)getActivity()).getCloudManager().getImageByCategory(category,adapter);

            return rootView;
        }
    }
}
