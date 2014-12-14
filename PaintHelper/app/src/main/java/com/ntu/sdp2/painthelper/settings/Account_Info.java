package com.ntu.sdp2.painthelper.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.ntu.sdp2.painthelper.R;

/**
 * Created by lou on 2014/12/11.
 */
public class Account_Info extends Fragment{

    private static final String TAG = "AccountInfoFragment";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG,"callback!!");
            if( state.isClosed() ){
                Log.d(TAG,"callback closed");

            }else if (state.isOpened()){
                Log.d(TAG, "callback open");

            }else{
                Log.d(TAG, "callback else");
            }
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;
    TextView userInfo;
    ProfilePictureView profilePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = (View)inflater.inflate(R.layout.account_info_frag, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(getParentFragment());
        userInfo = (TextView) view.findViewById(R.id.selection_user_name);
        profilePic = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult!");
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            profilePic.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.VISIBLE);
            if( session != null && session.isOpened()){
                makeMeRequest(session);
            }
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            profilePic.setVisibility(View.INVISIBLE);
            userInfo.setVisibility(View.INVISIBLE);
        }
    }

    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                // Set the id for the ProfilePictureView
                                // view that in turn displays the profile picture.
                                profilePic.setProfileId(user.getId());
                                // Set the Textview's text to the user's name.
                                userInfo.setText(user.getName());
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }




}