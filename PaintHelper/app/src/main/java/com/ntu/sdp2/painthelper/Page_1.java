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
import android.util.Log;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Page_1 extends Fragment {
    Calendar rightNow;
    int counter=0;
    int image_id;
    ImageView imageView_tosave;
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

    public void save_image(){
        String path="111";
        String Tag="save_image";
        Log.v(Tag,path);
        rightNow= Calendar.getInstance();
        String time=Integer.toString(rightNow.get(Calendar.MONTH))+Integer.toString(rightNow.get(Calendar.DATE))+Integer.toString(rightNow.get(Calendar.HOUR))+Integer.toString(rightNow.get(Calendar.SECOND))+counter;
        Toast.makeText(getActivity(),time,Toast.LENGTH_LONG).show();
        counter++;
        path=Environment.getExternalStorageDirectory().getPath();
        Log.v(Tag,path);
        if(Sd_write)
        {

            File file=getAlbumStorageDir(getActivity().getApplicationContext(),"saved_img");


            path=file.getPath();
            File image_file=new File(path+"/"+time+".png");
            Log.e(Tag,"image_file :"+image_file.getPath());



            if(!file.exists()) file.mkdirs();
            else    Log.v(Tag,"dirExisted");
            if(!image_file.exists())
            {
                try
                {
                    image_file.createNewFile();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(imageView_tosave!=null) {
                imageView_tosave.setDrawingCacheEnabled(true);
                Bitmap tmp = imageView_tosave.getDrawingCache();
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
            else
            {
                Log.e(Tag,"image_view is null");
            }
            message=image_file.getPath();

        }

    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View page_1 = inflater.inflate(R.layout.page_1_frag, container, false);

        final GridView gridview = (GridView)page_1.findViewById(R.id.gridView);
        final ImageAdapter imageAdapter = new ImageAdapter(this.getActivity());
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(page_1.getContext(), "" + position, Toast.LENGTH_SHORT).show();
                ImageView imageView = (ImageView) imageAdapter.getItem(position);
                RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                relativeLayout.addView(imageView);
                relativeLayout.removeViewInLayout(gridview);
                imageView_tosave=imageView;

            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
        relativeLayout.removeViewInLayout(gridview);

        final ArcMenu cornerButton = (ArcMenu)page_1.findViewById(R.id.arcMenu);
        int[] images = {
                R.drawable.composer_camera,
                R.drawable.composer_music,
                R.drawable.composer_place,
                R.drawable.composer_sleep,
                R.drawable.composer_with};
        final int itemCount = images.length;



        for (int i=0; i<itemCount; i++) {
            ImageView item = new ImageView(this.getActivity());
            item.setImageResource(images[i]);
            final int position = i;
            cornerButton.addItem(item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(page_1.getContext(), "" + position, Toast.LENGTH_SHORT).show();
                    if (position == 0) {
                        RelativeLayout relativeLayout = (RelativeLayout) page_1.findViewById(R.id.relativeLayout);
                        relativeLayout.addView(gridview);
                        relativeLayout.bringChildToFront(gridview);
                    }
                    if(position==1){
                        Intent intent = new Intent(getActivity(),TutorialInstantTracking.class);


                        intent.putExtra(MainActivity.EXTRA_MESSAGE,message);

                        startActivity(intent);

                    }
                    if(position==2)
                    {
                        save_image();
                    }
                }
            });

        }
        if(isExternalStorageWritable())
        {

            Toast.makeText(getActivity(),"SDCard_Ready!",Toast.LENGTH_LONG).show();
            Sd_write=true;
        }
        else
        {
            Toast.makeText(getActivity(),"SDCard_Removed!",Toast.LENGTH_LONG).show();
            Sd_write=false;
        }

        return page_1;
    }


}
