// Copyright 2007-2014 metaio GmbH. All rights reserved.
package com.ntu.sdp2.painthelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.Camera;
import com.metaio.sdk.jni.CameraVector;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Vector3d;

public class TutorialInstantTracking extends ARViewActivity
{

	/**
	 * Tiger geometry
	 */

	private IGeometry mImagePlane;
	/**
	 * metaio SDK callback handler
	 */
	private MetaioSDKCallbackHandler mCallbackHandler;

	/**
	 * Flag to indicate proximity to the tiger
	 */
	boolean mIsCloseToTiger;

	/**
	 * Media Player to play the sound of the tiger
	 */


	private View m2DButton;
	private View m2DRectifiedButton;
	private View m3DButton;
	private View m2DSLAMButton;
	private View m2DSLAMExtrapolationButton;
	
	public Vector3d size=new Vector3d(3.0f);
	/**
	 * The flag indicating a mode of instant tracking
	 * 
	 * @see {@link com.metaio.sdk.jni.IMetaioSDKAndroid#startInstantTracking(String, String, boolean)}
	 */
	boolean mPreview = true;
    String message;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{

			mImagePlane = null;
			mCallbackHandler = new MetaioSDKCallbackHandler();
			mIsCloseToTiger = false;





			m2DButton = mGUIView.findViewById(R.id.instant2DButton);
			m2DRectifiedButton = mGUIView.findViewById(R.id.instant2DRectifiedButton);
			m3DButton = mGUIView.findViewById(R.id.instant3DButton);
			m2DSLAMButton = mGUIView.findViewById(R.id.instant2DSLAMButton);
			m2DSLAMExtrapolationButton = mGUIView.findViewById(R.id.instant2DSLAMExtrapolationButton);





            Intent intent = getIntent();
            message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		}
		catch (Exception e)
		{
            MetaioDebug.log("onCreate Error");
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mCallbackHandler.delete();
		mCallbackHandler = null;

	}

	/**
	 * This method is regularly called in the rendering loop. It calculates the distance between
	 * device and the target and performs actions based on the proximity
	 */


	/**
	 * Play sound that has been loaded
	 */



	@Override
	protected int getGUILayout()
	{
		return R.layout.tutorial_instant_tracking;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler()
	{
		return mCallbackHandler;
	}

	@Override
	public void onDrawFrame()
	{
		super.onDrawFrame();



	}

	public void onButtonClick(View v)
	{
		finish();
	}

	public void onBiggerButtonClick(View v)
	{
		
		float tmp=size.getX();
		tmp+=0.1;
		size=new Vector3d(tmp);
		mImagePlane.setScale(size);
	}public void onSmallerButtonClick(View v)
	
	{
		
		float tmp=size.getX();
		tmp-=0.1;
		size=new Vector3d(tmp);
		mImagePlane.setScale(size);
	}
	
	public void on2DButtonClicked(View v)
	{

		m2DRectifiedButton.setEnabled(!mPreview);
		m3DButton.setEnabled(!mPreview);
		m2DSLAMButton.setEnabled(!mPreview);
		m2DSLAMExtrapolationButton.setEnabled(!mPreview);
		metaioSDK.startInstantTracking("INSTANT_2D", "", mPreview);
		mPreview = !mPreview;
	}

	public void on2DRectifiedButtonClicked(View v)
	{

		m2DButton.setEnabled(!mPreview);
		m3DButton.setEnabled(!mPreview);
		m2DSLAMButton.setEnabled(!mPreview);
		m2DSLAMExtrapolationButton.setEnabled(!mPreview);
		metaioSDK.startInstantTracking("INSTANT_2D_GRAVITY", "", mPreview);
		mPreview = !mPreview;
	}

	public void on3DButtonClicked(View v)
	{

		metaioSDK.startInstantTracking("INSTANT_3D");
	}

	public void on2DSLAMButtonClicked(View v)
	{

		m2DButton.setEnabled(!mPreview);
		m2DRectifiedButton.setEnabled(!mPreview);
		m3DButton.setEnabled(!mPreview);
		m2DSLAMExtrapolationButton.setEnabled(!mPreview);
		metaioSDK.startInstantTracking("INSTANT_2D_GRAVITY_SLAM", "", mPreview);
		mPreview = !mPreview;
	}

	public void on2DSLAMExtrapolationButtonClicked(View v)
	{

		m2DButton.setEnabled(!mPreview);
		m2DRectifiedButton.setEnabled(!mPreview);
		m3DButton.setEnabled(!mPreview);
		m2DSLAMButton.setEnabled(!mPreview);
		metaioSDK.startInstantTracking("INSTANT_2D_GRAVITY_SLAM_EXTRAPOLATED", "", mPreview);
		mPreview = !mPreview;
	}
    public void onConvertButtonClick(View v)
    {
        final CameraVector cameras = metaioSDK.getCameraList();
        if (!cameras.isEmpty())
        {
            com.metaio.sdk.jni.Camera camera = cameras.get(0);

            // Try to choose the camera with desired facing
            for (int i = 0; i < cameras.size(); i++)
            {
                // TODO: Use Camera.FACE_BACK for back facing camera
                if (cameras.get(i).getFacing() == Camera.FACE_FRONT)
                {
                    camera = cameras.get(i);
                    break;
                }
            }

            metaioSDK.startCamera(camera);
        }
        else
        {
            MetaioDebug.log(Log.WARN, "No camera found on the device!");
        }


    }
	@Override
	protected void loadContents()
	{
		try
		{
			// Load tiger model


			

			mImagePlane = metaioSDK.createGeometryFromImage(message);
			mImagePlane.setVisible(true);
			// Set geometry properties and initially hide it

			//mTiger.setAnimationSpeed(60f);
			//mTiger.startAnimation("meow");
			MetaioDebug.log("Loaded geometry " + message);
		}
		catch (Exception e)
		{
			MetaioDebug.log(Log.ERROR, "Error loading geometry: " + e.getMessage());
		}
	}


	@Override
	protected void onGeometryTouched(IGeometry geometry)
	{


	}

	final class MetaioSDKCallbackHandler extends IMetaioSDKCallback
	{

		@Override
		public void onSDKReady()
		{
			// show GUI
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					mGUIView.setVisibility(View.VISIBLE);
				}
			});
		}


		@Override
		public void onInstantTrackingEvent(boolean success, String file)
		{
			if (success)
			{
				MetaioDebug.log("MetaioSDKCallbackHandler.onInstantTrackingEvent: " + file);
				metaioSDK.setTrackingConfiguration(file);

			}
			else
			{
				MetaioDebug.log(Log.ERROR, "Failed to create instant tracking configuration!");
			}
		}


	}

}
