package com.ntu.sdp2.painthelper;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by WeiTang114 on 2014/12/28.
 */
public class GlobalState extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "DI2qUxaZ2sNxsc7u8D7gnLD13NzVGrCQbbsuNkzn", "AE4g2zci5ke08QFqfqRrQWy0BshRdhZNelNfwyui");
    }
}
