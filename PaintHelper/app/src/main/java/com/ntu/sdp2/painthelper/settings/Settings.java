package com.ntu.sdp2.painthelper.settings;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ntu.sdp2.painthelper.BackButtonHandler.Page4Handler;
import com.ntu.sdp2.painthelper.DataManagement.CallBack.ThumbCallBack;
import com.ntu.sdp2.painthelper.DataManagement.CloudManagement;
import com.ntu.sdp2.painthelper.DataManagement.Images.PaintImage;
import com.ntu.sdp2.painthelper.DataManagement.ParseManager;
import com.ntu.sdp2.painthelper.MainActivity;
import com.ntu.sdp2.painthelper.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lou on 2014/12/12.
 */
public class Settings extends ListFragment {
    static final String[] itemList = {"Account", "Default Tab", "About", "saveImageTest", "queryTest"};
    final String TAG = "Settings";
    //static Account_Info account_info = new Account_Info();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>((Activity)getActivity(), android.R.layout.simple_list_item_1, itemList);
        setListAdapter(itemAdapter);


    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(position){
            case 0:
                Fragment fragment = new Account_Info();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(getId(), fragment, getString(R.string.account_info_frag_tag));
                fragmentTransaction.addToBackStack(null);
                // add this to BackButtonHandler to make back button work.
                ((MainActivity)getActivity()).addToBackStack(new Page4Handler(fragment));
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                //Toast.makeText((Activity)getActivity(), "add!!", Toast.LENGTH_LONG).show();
                break;

            // THIS IS FOR DEBUGGGGGG!!
            case 3:
                CloudManagement cloudManager = (CloudManagement)((MainActivity)getActivity()).getCloudManager();
                ParseUser user = ParseUser.getCurrentUser();
                if(user == null)break;
                if(!ParseManager.isInitialized()){
                    Toast.makeText(getActivity(), "init not finish", Toast.LENGTH_SHORT).show();
                    break;
                }
                List<String> list = new ArrayList<>();
                list.add("Food");
                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.img);

                PaintImage paintImage = new PaintImage(user.getUsername(),"Test" , bmp, new String(), list );
                if(cloudManager.saveImage(paintImage)){
                    Toast.makeText(getActivity(), "not loggin!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                CloudManagement cloudManager2  = (CloudManagement)((MainActivity)getActivity()).getCloudManager();
                cloudManager2.getImageByCategory("Food", new ThumbCallBack() {
                    @Override
                    public void done(PaintImage paintImage) {
                        Toast.makeText(getActivity(), "imageGet!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "imageGet" );
                    }
                });
                break;

        }

    }
}
