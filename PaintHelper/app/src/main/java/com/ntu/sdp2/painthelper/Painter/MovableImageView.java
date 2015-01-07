package com.ntu.sdp2.painthelper.Painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.ntu.sdp2.painthelper.utils.SketchImage;

import java.io.IOException;

/**
 * Created by JimmyPrime on 2014/12/23.
 */
public class MovableImageView extends ImageView {
    public MovableImageView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        thisView = this;

    }

    public void genSketchImage() {
        Bitmap bitmap = ((BitmapDrawable)this.getDrawable()).getBitmap();
        try {
            sketchImage = SketchImage.createFromPhoto(bitmap);
        } catch (IOException e) {
            Log.d("Sketch Image", "Create Fail");
            sketchImage = new SketchImage();
        }
    }

    public void setLineWidth(int width) {
        sketchImage.setLineWidth(width);
        this.setImageBitmap(sketchImage.getBitmapTransparent());
    }

    private boolean isPrimaryUpButNotUp = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        float X = event.getRawX();
        float Y = event.getRawY();

        Log.i("onTouchEvent", "motion event = " + eventAction);
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                atX = this.getX();
                atY = this.getY();
                startX = X;
                startY = Y;
                this.setBackgroundColor(Color.rgb(192, 192, 192));
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isPrimaryUpButNotUp) {
                    this.setX(atX + X - startX);
                    this.setY(atY + Y - startY);
                }
                break;

            case MotionEvent.ACTION_UP:
                this.setBackgroundColor(Color.TRANSPARENT);
                isPrimaryUpButNotUp = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isPrimaryUpButNotUp = true;
                break;
            default:
                Log.d("default", "default eventAction, action=" + eventAction);
                return false;
        }

        invalidate();
        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ((DrawerLayout)thisView.getParent()).removeView(thisView);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i("Scaling", "zomm end");
            thisView.setBackgroundColor(Color.TRANSPARENT);
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i("Scaling", "zomm start");
            thisView.setBackgroundColor(Color.rgb(192, 192, 192));
            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("Scaling", "zoom ongoing, scale: " + detector.getScaleFactor());
            thisView.setScaleX(thisView.getScaleX() * detector.getScaleFactor());
            thisView.setScaleY(thisView.getScaleY() * detector.getScaleFactor());
            thisView.invalidate();
            return false;
        }
    }

    private float atX, atY;
    private float startX, startY;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private MovableImageView thisView;
    private SketchImage sketchImage;
}
