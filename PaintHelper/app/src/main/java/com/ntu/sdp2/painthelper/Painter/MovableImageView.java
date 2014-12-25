package com.ntu.sdp2.painthelper.Painter;

import android.content.Context;
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
        float X = event.getRawX();
        float Y = event.getRawY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                atX = this.getX();
                atY = this.getY();
                startX = X;
                startY = Y;
                break;

            case MotionEvent.ACTION_MOVE:
                this.setX(atX + X - startX);
                this.setY(atY + Y - startY);
                break;
        }

        invalidate();
        return true;
    }

    private float atX, atY;
    private float startX, startY;
}
