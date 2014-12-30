package com.ntu.sdp2.painthelper.DataManagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.ntu.sdp2.painthelper.DataManagement.CallBack.ElementCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.OriginCallback;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.OriginImage;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintElement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.DataManagement.Images.ThumbImage;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lou on 2014/12/20.
 */
public class ParseLocalManager implements LocalDataManagement {
    /*--------------------------------------------------------------*
                            Local Variables
     *--------------------------------------------------------------*/
    public static final String TAG = "ParseLocalManager";


    /*--------------------------------------------------------------*
                                Public Methods
     *--------------------------------------------------------------*/
    public ParseLocalManager() {
    }

    @Override
    public void getImageByCategory(String category, final ThumbCallBack callBack) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Thumbnails");
        query.fromLocalDatastore();
        if(!category.equals("")) {
            query.whereEqualTo("Category", category);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null) {
                    //categoryCount = parseObjects.size();
                    //categoryResult = new ArrayList<PaintImage>();
                    for(final ParseObject objects:parseObjects) {
                        objects.getParseFile("Img").getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                onImageGet(objects, bytes, callBack);
                            }
                        });
                    }
                }else{
                    onImageGet(null, null, callBack);
                }
            }
        });
    }

    // This method can only be used to get original image but not thumbnail.
    @Override
    public void getImageById(String id, final OriginCallback callBack) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Img");
        query.fromLocalDatastore();
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                final ParseObject newObject = object;
                if (e == null) {
                    object.getParseFile("Img").getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            onImageGet(newObject, bytes, callBack);
                        }
                    });


                } else {
                    // TODO:something went wrong
                    onImageGet(null, null, callBack);
                }
            }
        });

    }
    @Override
    public void getElementByCategory(String category, final ElementCallBack callBack) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Thumbnails");
        query.fromLocalDatastore();
        query.whereEqualTo("Category", category);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null) {
                    //categoryCount = parseObjects.size();
                    //categoryResult = new ArrayList<PaintImage>();
                    for(final ParseObject objects:parseObjects) {
                        objects.getParseFile("Img").getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                onImageGet(objects, bytes, callBack);
                            }
                        });
                    }
                }else{
                    onImageGet(null, null, callBack);
                }
            }
        });

    }

    @Override
    public boolean saveImage(PaintImage paintImage){
        ParseObject parseObject = new ParseObject("Img");
        addDetails(parseObject, paintImage);
        // create byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        paintImage.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image_byte = stream.toByteArray();

        // create ParseFiles for original image
        ParseFile parseImg = new ParseFile(paintImage.getName(), image_byte);
        parseObject.put("Img", parseImg);


        parseObject.pinInBackground();
        return false;
    }
    /*--------------------------------------------------------------*
                                Private Methods
     *--------------------------------------------------------------*/
    private void onImageGet(ParseObject parseObject, byte[] bytes, OriginCallback callBack){
        if(parseObject != null) {
            PaintImage paintImage;
            paintImage = parseToPaint(parseObject, bytes, "Origin");
            callBack.done(paintImage);
        }
    }
    private void onImageGet(ParseObject parseObject, byte[] bytes, ElementCallBack callBack){
        if(parseObject != null) {
            PaintImage paintImage;
            paintImage = parseToPaint(parseObject, bytes, "Element");
            callBack.done(paintImage);
        }
    }
    private void onImageGet(ParseObject parseObject, byte[] bytes, ThumbCallBack callBack){
        if(parseObject != null) {
            PaintImage paintImage;
            paintImage = parseToPaint(parseObject, bytes, "Thumb");
            callBack.done(paintImage);
        }
    }


    // convert ParseObject to PaintImage
    private PaintImage parseToPaint(ParseObject parseObject, byte[] bytes, String type){
        String name = parseObject.getString("Name");
        //String author = parseObject.getParseUser("user").getUsername();
        ParseUser user = parseObject.getParseUser("user");
        String author = "Temp Local Author";
        String id = parseObject.getObjectId();
        List<String> categoryList = parseObject.getList("Category");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        switch (type){
            case "Origin":
                return new OriginImage(author, name, bitmap, id, categoryList, user);
            case "Thumb":
                return new ThumbImage(author, name, bitmap, id, categoryList, user);
            case "Element":
                return new PaintElement(author, name, bitmap, id, categoryList, user);
            default:
                return new PaintImage(author, name, bitmap, id, categoryList, user);
        }

    }

    // Add paintImage info to parseObject
    private void addDetails(ParseObject parseObject, PaintImage paintImage){
        parseObject.put("Name", paintImage.getName());
        parseObject.put("user", paintImage.getParseUser());
        parseObject.put("Category", paintImage.getCategory());
        parseObject.setObjectId(paintImage.getId());
    }

}
