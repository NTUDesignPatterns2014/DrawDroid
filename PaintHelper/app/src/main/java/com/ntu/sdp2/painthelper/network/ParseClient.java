package com.ntu.sdp2.painthelper.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lou on 2014/10/14.
 * class ParseClient
 */


public class ParseClient {
    public static final String TAG = "ParseClient";

    public final static int THUMB_WIDTH = 100;
    public final static int THUMB_HEIGHT = 100;
    public final static int QUERY_LIMIT = 100;

    // constructor
    public ParseClient(Activity this_act){

        // initialize Parse
        Parse.initialize( this_act, "msUmCCBdKR3soqTtjXPoMG4xH4LKnwFwvehTjb4r", "x0Weh8Rojf7Xy7kdQlAsnYyxqHigie6vteFKeQID");

    }


    /**
     * function to upload an image
     * return: 0: success; -1:need login
     *
     */
    public int Upload(Context context, String imgname, Bitmap image){

        // user login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ){
            // user active
            Toast.makeText(context, "call performUpload..", Toast.LENGTH_SHORT).show();
            performUpload(context, currentUser, imgname, image);
        }else{
            // need login fb
            return -1;
        }


        return 0;
    }




    // fetch all thumbnails
    public List<ParseObject> getImages(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thumbnails");
        query.setLimit(QUERY_LIMIT);
        query.include("user");
        query.whereEqualTo("isThumb", true);
        List<ParseObject> result = new ArrayList<ParseObject>();
        try {
            result = query.find();

        }catch(ParseException e){
            // TODO: handle query exception
        }
        return result;

    }





    private void performUpload(Context context, ParseUser currentUser, String imgname, Bitmap image) {
        // create thumbnails
        Bitmap thumb = ThumbnailUtils.extractThumbnail(image, THUMB_WIDTH, THUMB_HEIGHT);

        // create byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] origin_byte = stream.toByteArray();
        stream.reset();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] thumb_byte = stream.toByteArray();

        // create ParseFiles for original image and thumbnail
        ParseFile parseimg_thumb = new ParseFile(imgname, thumb_byte);
        ParseFile parseimg_origin = new ParseFile(imgname, origin_byte);

        // create a class to get img id
        class myString{
            public String mystr = "";

        }
        final myString id = new myString();

        // ParseObject for thumbnail, Thunmnails objects only store ObjectID of its original image
        final ParseObject parsethumb = new ParseObject("Thumbnails");
        parsethumb.put("Name", imgname);
        parsethumb.put("isThumb", true);
        parsethumb.put("Img", parseimg_thumb);
        parsethumb.put("user", currentUser);

        final Context fContext = context;

        // ParseObject for original image
        final ParseObject parseobj_origin = new ParseObject("Img");
        parseobj_origin.put("Name", imgname);
        parseobj_origin.put("Img", parseimg_origin);
        parseobj_origin.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e == null ) {
                    // success!
                    Log.i(TAG, "upload success");
                    Toast.makeText(fContext, "Upload Succeeded", Toast.LENGTH_SHORT).show();
                    id.mystr = parseobj_origin.getObjectId();
                    parsethumb.put("ImgID", id.mystr);
                    parsethumb.saveInBackground();
                }else{
                    // failed!!
                    Log.d(TAG, "upload failed");
                    Toast.makeText(fContext, "Upload Failed", Toast.LENGTH_SHORT).show();
                    // TODO : handle exception

                }
            }
        });

        //Log.d(imgname, "log!11222122!!");

 /*       ParseObject parsethumb = new ParseObject("Thumbnails");
        parsethumb.put("Name", imgname);
        parsethumb.put("isThumb", true);
        parsethumb.put("Img", parseimg_thumb);
        if ( id.mystr.length() == 0 ){
            id.mystr = "111";
        }
        parsethumb.put("ImgID", id.mystr);
        parsethumb.saveInBackground();
*/


    }

}
