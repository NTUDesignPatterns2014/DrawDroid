package com.ntu.sdp2.painthelper.DataManagement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lou on 2014/12/20.
 */
public class ParseManager extends CloudManagement {

    /*--------------------------------------------------------------*
                            Local Variables
     *--------------------------------------------------------------*/
    public static final String TAG = "ParseManager";

    private static boolean initialized = false;
    private static Map<String,ParseObject> categoryMap = new HashMap<String, ParseObject>() ;
    List<ParseObject> objectList = new ArrayList<ParseObject>();

    public final static int THUMB_WIDTH = 100;
    public final static int THUMB_HEIGHT = 100;
    public final static int QUERY_LIMIT = 100;


    /*--------------------------------------------------------------*
                                Public Methods
     *--------------------------------------------------------------*/
    // constructor
    public ParseManager(){

        // Download all categories. Image cannot be uploaded before all category objects have been downloaded.
        if( !initialized ) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Category");
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        categoryMap.put(parseObject.getString("category"), parseObject);
                        initialized = true;
                    }
                });
        }
    }



    @Override
    public void getImageByCategory(String category) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Thumbnails");
        query.whereEqualTo("Category", category);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                onCategoryImageGet(parseObjects);
            }
        });
    }

    // This method can only be used to get original image but not thumbnail.
    @Override
    public void getImageById(String id) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Img");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    onIdImageGet(object);
                } else {
                    // TODO:something went wrong
                }
            }
        });

    }

    @Override
    public boolean saveImage(PaintImage image) {
        if(!initialized){
            Log.i(TAG, "Cannot save Image before Manager has initialized!");
            return true;
        }else{
            if( uploadImage(image) != 0){
                Log.i(TAG, "Not Logged in !");
                return true;
            }
        }
        return false;
    }

    @Override
    public void getElementByCategory(String category) {

    }


    /*--------------------------------------------------------------*
                            Private Methods
     *--------------------------------------------------------------*/
    // fetch all thumbnails
    private List<ParseObject> getImages(){
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




    /**
     * function to upload an image
     * return: 0: success; -1:need login
     *
     */
    private int uploadImage(PaintImage paintImage){

        // user login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ){
            // user active
            performUpload(paintImage);
        }else{
            // need login fb
            return -1;
        }


        return 0;
    }


    // perform upload, User must have logged in or error!!!
    private void performUpload(PaintImage paintImage) {
        Bitmap image = paintImage.getImage();
        String imgName = paintImage.getName();
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
        ParseFile parseImg_thumb = new ParseFile(imgName, thumb_byte);
        ParseFile parseImg_origin = new ParseFile(imgName, origin_byte);

        // create a class to get img id
        class myString {
            public String myStr = "";

        }
        final myString id = new myString();

        // ParseObject for thumbnail, Thumbnails objects only store ObjectID of its original image
        final ParseObject parseThumb = new ParseObject("Thumbnails");
        addDetails(parseThumb, paintImage);
        parseThumb.put("Img", parseImg_thumb);

        // ParseObject for original image
        final ParseObject parseObject_origin = new ParseObject("Img");
        parseObject_origin.put("Name", imgName);
        parseObject_origin.put("Img", parseImg_origin);
        parseObject_origin.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // success!
                    Log.i(TAG, "Img upload success");
                    id.myStr = parseObject_origin.getObjectId();
                    parseThumb.put("ImgID", id.myStr);
                    parseThumb.saveInBackground();
                } else {
                    // failed!!
                    Log.i(TAG, "upload failed");
                    // TODO : handle exception

                }
            }
        });
    }

    // each ParseObject has a list of categories it belong to.
    private void addDetails(ParseObject parseObject, PaintImage paintImage){
        parseObject.put("Name", paintImage.getName());
        parseObject.put("user", ParseUser.getCurrentUser());
        ArrayList<ParseObject> categoryList = new ArrayList<>();
        for(String category : paintImage.getCategory()){
            categoryList.add(categoryMap.get(category));
        }
        parseObject.put("Category", categoryList);
    }

    private void onCategoryImageGet(List<ParseObject> parseObjects){
        // TODO
    }
    private void onIdImageGet(ParseObject parseObject){

        // TODO
    }
}
