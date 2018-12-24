package com.example.sara.grammy.Profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.UniversalImageLoader;


public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private ImageView mProfilePhoto;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile,container,false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);

        setProfileImage();
        //BackArrow Navigation
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }



    private void setProfileImage(){
        String imgURL = "www.gstatic.com/webp/gallery/5.jpg";
        UniversalImageLoader.setImage(imgURL,mProfilePhoto,null,"https://");
    }
}
