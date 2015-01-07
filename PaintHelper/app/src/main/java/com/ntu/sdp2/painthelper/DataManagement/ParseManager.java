package com.ntu.sdp2.painthelper.DataManagement;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ElementCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.LogInCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.OriginCallback;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.SaveCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.Images.OriginImage;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintElement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.DataManagement.Images.ThumbImage;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lou on 2014/12/20.
 */
public class ParseManager implements CloudManagement {

    /*--------------------------------------------------------------*
                            Local Variables
     *--------------------------------------------------------------*/
    public static final String TAG = "ParseManager";


    public final static int THUMB_WIDTH = 100;
    public final static int THUMB_HEIGHT = 100;
    public final static int QUERY_LIMIT = 100;





    /*--------------------------------------------------------------*
                                Public Methods
     *--------------------------------------------------------------*/
    // constructor
    public ParseManager(){
    }


    // empty category string means all objects
    @Override
    public void getImageByCategory(String category, final ThumbCallBack callBack) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Thumbnails");
        if( !category.equals("") ){
            query.whereEqualTo("Category", category);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (final ParseObject objects : parseObjects) {
                        objects.getParseFile("Img").getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                onImageGet(objects, bytes, callBack);
                            }
                        });
                    }
                } else {
                    onImageGet(null, null, callBack);
                }
            }
        });

    }

    // This method can only be used to get original image but not thumbnail.
    @Override
    public void getImageById(String id, final OriginCallback callback) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Img");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                Log.i(TAG, "IdImageDone");
                final ParseObject newObject = object;
                if (e == null) {
                    object.getParseFile("Img").getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            onImageGet(newObject, bytes, callback);
                        }
                    });


                } else {
                    // TODO:something went wrong
                    onImageGet(null, null, callback);
                }
            }
        });

    }

    @Override
    public boolean saveImage(PaintImage image, SaveCallBack saveCallBack) {
        if(ParseUser.getCurrentUser() == null){
            Log.w(TAG, "saveImage/Not Logged in");
            return true;
        }
        performUpload(image, saveCallBack);

        return false;
    }

    @Override
    public void getElementByCategory(String category, final ElementCallBack callBack) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Thumbnails");
        query.whereEqualTo("Category", category);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null) {
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

    static public void logIn(final Activity activity, final LogInCallBack callBack){
        ParseFacebookUtils.logIn(activity, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                        new Request.GraphUserCallback() {
                            @Override
                            public void onCompleted(final GraphUser user, Response response) {
                                // If the response is successful
                                if (user != null) {
                                    if(ParseUser.getCurrentUser() == null){
                                        ParseFacebookUtils.logIn(activity, new LogInCallback() {
                                            @Override
                                            public void done(ParseUser parseUser, ParseException e) {
                                                ParseUser.getCurrentUser().setUsername(user.getName());
                                                ParseUser.getCurrentUser().saveEventually();
                                                callBack.done(ParseUser.getCurrentUser());
                                            }
                                        });
                                    }else {
                                        ParseUser.getCurrentUser().setUsername(user.getName());
                                        ParseUser.getCurrentUser().saveEventually();
                                        callBack.done(ParseUser.getCurrentUser());
                                    }
                                }else if (response.getError() != null) {
                                    if ((response.getError().getCategory() ==
                                            FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                            (response.getError().getCategory() ==
                                                    FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION))
                                    {
                                        Log.d(TAG,
                                                "The facebook session was invalidated.");
                                    } else {
                                        Log.d(TAG,
                                                "Some other error: "
                                                        + response.getError()
                                                        .getErrorMessage());
                                    }
                                }
                            }
                        });
                request.executeAsync();
            }

        });
    }


    /*--------------------------------------------------------------*
                                Private Methods
     *--------------------------------------------------------------*/
    // fetch all thumbnails
    // This is a old version function
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

    // perform upload, User must have logged in or error!!!
    private void performUpload(PaintImage paintImage, final SaveCallBack saveCallBack) {
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
        addDetails(parseObject_origin, paintImage);
        parseObject_origin.put("Img", parseImg_origin);
        parseObject_origin.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // success!
                    Log.i(TAG, "Img upload success");
                    id.myStr = parseObject_origin.getObjectId();
                    parseThumb.put("ImgId", id.myStr);
                    parseThumb.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i(TAG, "Thumb upload success");
                            saveCallBack.done();
                        }
                    });
                } else {
                    // failed!!
                    Log.i(TAG, "upload failed");
                    // TODO : handle exception

                }
            }
        });
    }

    // each ParseObject has a list of categories it belong to.
    // this function should be called when user has logged in.
    private void addDetails(ParseObject parseObject, PaintImage paintImage){
        parseObject.put("Name", paintImage.getName());
        if( ParseUser.getCurrentUser() != null ) {
            parseObject.put("user", ParseUser.getCurrentUser());
            Log.i(TAG, "addDetails: put 'user':" + ParseUser.getCurrentUser().getUsername());
        }else{
            Log.i(TAG, "addDetails has no user logged in!!");
        }

        parseObject.put("Category", paintImage.getCategory());
    }

    private void onImageGet(ParseObject parseObject, byte[] bytes, OriginCallback callBack){
        if(parseObject != null) {
            PaintImage paintImage;
            paintImage = parseToPaint(parseObject, bytes, "Origin");
            Log.i(TAG, "onIdImageGet");
            callBack.done(paintImage);
        }
        Log.i(TAG, "onIdImageNOTGet");
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
            Log.i(TAG, "onImageGet");
            callBack.done(paintImage);
        }
        Log.i(TAG, "onImageNOTGet");
    }
    // convert ParseObject to PaintImage
    private PaintImage parseToPaint(ParseObject parseObject, byte[] bytes, String type){
        Log.i(TAG, "parseToPaint, parseObject:" + parseObject.toString() + parseObject.keySet().toString());
        String name = parseObject.getString("Name");
        try {
            parseObject.getParseUser("user").fetchIfNeeded();
        }catch (Exception e){
            Log.i(TAG, "fetch userinfo failed!");
        }
        String author = parseObject.getParseUser("user").getUsername();
        //String author = "Temp Author";
        ParseUser user = parseObject.getParseUser("user");
        String id = parseObject.getString("ImgId");
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
}
