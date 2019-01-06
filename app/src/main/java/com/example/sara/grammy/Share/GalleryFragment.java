package com.example.sara.grammy.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sara.grammy.Profile.AccountSettingsActivity;
import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.FilePaths;
import com.example.sara.grammy.Utils.FileSearch;
import com.example.sara.grammy.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GalleryFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int NUM_GRID_COLUMNS = 3;

    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    private ArrayList<String> directories, fullImagePaths, directoryPaths;
    private String mAppend = "file:/";
    private String mSelectedImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery,container,false);
        galleryImage = (ImageView)view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.galleryGridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        directories = new ArrayList<>();
        fullImagePaths = new ArrayList<>();
        directoryPaths = new ArrayList<>();

        Log.d(TAG, "onCreateView: started.");

        ImageView closeShare = (ImageView) view.findViewById(R.id.ivcloseShare);
        closeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment");
                getActivity().finish();

            }
        });

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen");

                if (isRootTask()){
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                }


            }
        });
        init();

        return view;
    }

    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }else {
            return false;
        }
    }

    private void init(){
        //"ex: /storage/0/emulated/camera/blabla.jpg"

       fullImagePaths = FilePaths.getAllShownImagesPath(getActivity());

/*
        FilePaths filePaths = new FilePaths();

        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            Log.d(TAG, "Directories exist");
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }else{
            Log.d(TAG, "Directories doesnt exist");
        }
                for(int i = 0; i<directories.size();i++){
            int index =  directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index).replace("/", " ");
            directoryNames.add(string);

        }

        directories.add(filePaths.CAMERA);
       */

        ArrayList<String> directoryNames = new ArrayList<>();
        fullImagePaths = FilePaths.getAllShownImagesPath(getActivity());

        Log.d(TAG, "Full= "+fullImagePaths);

        String last = "";

        int count = 0;

        for(int i=0; i<fullImagePaths.size();i++){

            int index = fullImagePaths.get(i).lastIndexOf("/");
            String string = fullImagePaths.get(i).substring(0,index);

            int index2 = string.lastIndexOf("/");
            String st2 = string.substring(index2).replace("/"," ");

            for (String temp : directoryNames) {
                if(temp.equals(st2)){
                        //count += 1;
                    count = 1;
                    break;
                }
            }

            if(count==0){
                directories.add(string);
                directoryNames.add(st2);
               // last = st2;
            }

            count = 0;

        }

      /*

*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: selected: " + directories.get(position));
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imgWidth);

        //use the grid adapter to adapt images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //select first image
        try{
            setImage(imgURLs.get(0), galleryImage, mAppend);
            mSelectedImage = imgURLs.get(0);
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG,"setupGridView: ArrayIndexOutOfBoundsException "+e.getMessage());
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));
                setImage(imgURLs.get(position), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(position);
            }
        });

    }

    private void setImage(String imgURL, ImageView image, String append){

        Log.d(TAG, "setImage: setting image");
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

}
