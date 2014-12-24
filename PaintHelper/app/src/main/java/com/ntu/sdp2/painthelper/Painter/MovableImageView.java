package com.ntu.sdp2.painthelper.Painter;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by JimmyPrime on 2014/12/23.
 */
public class MovableImageView extends ImageView {
    public MovableImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        float X = event.getX();
        float Y = event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                atX = this.getX();
                atY = this.getY();
                touchX = event.getX();
                touchY = event.getY();
                Log.d("density", "" + getResources().getDisplayMetrics().density);
                break;

            case MotionEvent.ACTION_MOVE:
                // this.setX((atX + X - touchX) / getResources().getDisplayMetrics().density);
                // this.setY((atY + Y - touchY) / getResources().getDisplayMetrics().density);
                this.setX(X);
                this.setY(Y);
                break;
        }

        invalidate();
        return true;
    }

    private float touchX, touchY;
    private float atX, atY;
}
