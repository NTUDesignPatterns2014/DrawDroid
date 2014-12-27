package com.ntu.sdp2.painthelper.settings;

/**
 * Created by zhangwenbo on 14/12/26.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.os.Bundle;

import com.ntu.sdp2.painthelper.R;

public class Myadapter extends BaseAdapter {
    private Context context;
    private Integer[] imgs = {R.drawable.ic_launcher, R.drawable.animal, R.drawable.botany,
            R.drawable.people, R.drawable.food, R.drawable.building,
            R.drawable.vehicle, R.drawable.groceries, R.drawable.people,
    R.drawable.ic_launcher,R.drawable.animal,R.drawable.botany,
    R.drawable.people,R.drawable.food,R.drawable.building,
    R.drawable.vehicle,R.drawable.groceries,R.drawable.people,
            R.drawable.ic_launcher, R.drawable.animal, R.drawable.botany,
            R.drawable.people};
 public Myadapter(Context context){
         this.context=context;
        }
public int getCount(){
        return imgs.length;
        }

public Object getItem(int position){
        ImageView imageView=new ImageView(context);
        imageView.setImageResource(imgs[position]);
        return imageView;
        }

public long getItemId(int id){
        return 0;
        }

public View getView(int position,View convertView,ViewGroup parent){
        ImageView imageView;
        if(convertView==null){  // if it's not recycled, initialize some attributes
        imageView=new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(190,190));
        imageView.setAdjustViewBounds(false);//设置边界对齐
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(4,4,4,4);
        }else{
        imageView=(ImageView)convertView;
        }

        imageView.setImageResource(imgs[position]);
        return imageView;
        }
        }
