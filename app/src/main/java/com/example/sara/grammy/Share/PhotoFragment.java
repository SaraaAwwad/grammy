package com.example.sara.grammy.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sara.grammy.Profile.AccountSettingsActivity;
import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.Permissions;

public class PhotoFragment extends android.support.v4.app.Fragment {
    //Fragment to take from camera not gallery

    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        Log.d(TAG, "onCreateView: started.");

        Button btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera");

                if (((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){
                    if(((ShareActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG,"onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG,"onActivityResult: done taking a Photo");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen");

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            if (isRootTask()){
                //sharing photo from camera

                try{
                    Log.d(TAG, "onActivityResult: received bitmap from camera" + bitmap);

                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    startActivity(intent);

                }catch(NullPointerException e){
                    Log.d(TAG, "onActivityResult: Null pointer exception");
                }


            }else{
            //opening camera to take photo for profile photo
                try{
                    Log.d(TAG, "onActivityResult: received bitmap from camera" + bitmap);

                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();

                }catch(NullPointerException e){
                    Log.d(TAG, "onActivityResult: Null pointer exception");
                }
            }
        }
    }

    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }else {
            return false;
        }
    }
}
