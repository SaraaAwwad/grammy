package com.example.sara.grammy.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sara.grammy.R;
import com.example.sara.grammy.Utils.BottomNavigationViewHelper;
import com.example.sara.grammy.Utils.GridImageAdapter;
import com.example.sara.grammy.Utils.UniversalImageLoader;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ProfileActivity.this;
    private ImageView profilePhoto;
    private static final int NUM_GRID_COLUMNS = 3;

    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate:Started");

        setUpBottomNav();
        setupToolbar();
        setupActivityWidgets();
        setProfileImage();
        tempGridSetup();
    }

    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://www.gstatic.com/webp/gallery/2.jpg");
        imgURLs.add("https://www.gstatic.com/webp/gallery/3.jpg");
        imgURLs.add("https://www.gstatic.com/webp/gallery/4.jpg");
        imgURLs.add("https://www.gstatic.com/webp/gallery/1.jpg");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridview = findViewById(R.id.gridView);

        //To give it 3 columns only.
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridview.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext,R.layout.layout_grid_imageview,"",imgURLs);
        gridview.setAdapter(adapter);

    }

    private void setProfileImage(){
        String imgURL = "www.gstatic.com/webp/gallery/1.jpg";
        UniversalImageLoader.setImage(imgURL,profilePhoto,mProgressBar,"https://");

    }

    private void setupActivityWidgets(){
        mProgressBar = findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);
    }

    private void setupToolbar(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setUpBottomNav() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.enableNav(mContext, this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
