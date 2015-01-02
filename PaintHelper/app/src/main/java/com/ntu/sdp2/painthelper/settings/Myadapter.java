package com.ntu.sdp2.painthelper.settings;

/**
 * Created by zhangwenbo on 14/12/26.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.R;

import java.util.ArrayList;
import java.util.List;

public class Myadapter extends BaseAdapter implements ThumbCallBack
{
    private Context context;
    private List<Bitmap> image= new ArrayList<>();
    private List<PaintImage> paint = new ArrayList<>();

public Myadapter(Context context){
         this.context=context;
        }
public int getCount(){
        return image.size();
        }

public Object getItem(int position){
        ImageView imageView=new ImageView(context);
        imageView.setImageBitmap(image.get(position));
        return imageView;
        }

    public String getPiantImageID(int position){
        return paint.get(position).getId();
    }

public long getItemId(int id){
        return 0;
        }

public View getView(int position,View convertView,ViewGroup parent){
            Log.i("adapter00000000", "imagedone0000000");
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
//            if(position <getCount()) {
//                return convertView;
//            }
            imageView.setImageBitmap(image.get(position));
            return imageView;
        }



    @Override
    public void done(PaintImage paintImage) {
        image.add(paintImage.getImage());
        paint.add(paintImage);
        notifyDataSetChanged();
        Log.i("adapter", "imagedone");


    }
}
