package com.ntu.sdp2.painthelper.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.ntu.sdp2.painthelper.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by lou on 2014/12/11.
 */
public class Account_Info extends Fragment{


    private static final String TAG = "AccountInfoFragment";
    private static final boolean LOG_OUT = false;
    private static final boolean LOG_IN = true;

    TextView userInfo;
    ProfilePictureView profilePic;
    ParseUser user;
    Button authButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = (View)inflater.inflate(R.layout.account_info_frag, container, false);

        // initialize login button
        authButton = (Button) view.findViewById(R.id.authButton);
        buttonInitialize(authButton);
        userInfo = (TextView) view.findViewById(R.id.selection_user_name);
        profilePic = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        user = ParseUser.getCurrentUser();
        if(user == null){
            // not logged in
            authButton.setText("Log in with Facebook");
            setUi(LOG_OUT);
        }else{
            authButton.setText("Logout");
            setUi(LOG_IN);
        }

        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button Clicked");
                if(user == null){
                    // Log in
                    ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            user = parseUser;
                            if(parseUser == null){
                                Log.i(TAG, "User cancels login or login failed");
                                setUi(LOG_OUT);
                            }else{
                                Log.i(TAG, "User Logged in");
                                setUi(LOG_IN);

                            }
                        }
                    });
                }else{
                    // Log out
                    String message = "Currently logged in as: \n" + userInfo.getText() + "\nLog out?";
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Log out")
                            .setMessage(message)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    user = null;
                                    setUi(LOG_OUT);
                                    ParseFacebookUtils.getSession().closeAndClearTokenInformation();
                                    ParseUser.logOut();
                                    Log.i(TAG, "User Logged out");
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();


                }
            }
        });

        //whenever the fragment view is set up and a user session is open, get the user's data.
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            // Get the user's data
            Log.d(TAG, "MeReq in onCreateView");
            makeMeRequest();
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            // Get the user's data
            Log.d(TAG, "MeReq in onResume");
            makeMeRequest();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }




    private void makeMeRequest() {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (user != null) {
                            // Set the id for the ProfilePictureView
                            // view that in turn displays the profile picture.
                            profilePic.setProfileId(user.getId());
                            // Set the TextView's text to the user's name.
                            userInfo.setText(user.getName());
                            ParseUser parseUser = ParseUser.getCurrentUser();
                            if(parseUser == null){
                                Log.d(TAG, "Try to login in MeReq");
                                ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser parseUser, ParseException e) {
                                        setUi(LOG_IN);
                                    }
                                });
                                return;
                            }
                            try {
                                ParseUser.getCurrentUser().fetchIfNeeded();
                            }catch (Exception e){
                                Log.e(TAG, "fetch user info failed!!");
                            }
                            ParseUser.getCurrentUser().setUsername(user.getName());
                            ParseUser.getCurrentUser().saveEventually();
                        }else if (response.getError() != null) {
                            if ((response.getError().getCategory() ==
                                    FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                    (response.getError().getCategory() ==
                                            FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION))
                            {
                                Log.d(TAG,
                                        "The facebook session was invalidated.");
                                authButton.callOnClick();
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

    private void buttonInitialize(Button authButton){
        authButton.setBackgroundResource(com.facebook.android.R.drawable.com_facebook_button_blue);
        authButton.setCompoundDrawablesWithIntrinsicBounds(com.facebook.android.R.drawable.com_facebook_inverse_icon, 0, 0, 0);
        authButton.setCompoundDrawablePadding(
                getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_loginview_compound_drawable_padding));
        authButton.setPadding(getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_loginview_padding_left),
                getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_loginview_padding_top),
                getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_loginview_padding_right),
                getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_loginview_padding_bottom));
        authButton.setTextSize(getResources().getColor(com.facebook.android.R.color.com_facebook_loginview_text_color));
        authButton.setGravity(Gravity.CENTER);
        authButton.setTextColor(getResources().getColor(com.facebook.android.R.color.com_facebook_loginview_text_color));
        authButton.setTypeface(Typeface.DEFAULT_BOLD);
    }


    private void setUi(boolean isLogIn){
        if(isLogIn == LOG_IN){
            authButton.setText("Log Out");
            profilePic.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.VISIBLE);
            Log.d(TAG, "MeReq in setUi");
            makeMeRequest();
        }else if(isLogIn == LOG_OUT){
            authButton.setText("Log in with Facebook");
            profilePic.setVisibility(View.INVISIBLE);
            userInfo.setVisibility(View.INVISIBLE);
        }
    }

}