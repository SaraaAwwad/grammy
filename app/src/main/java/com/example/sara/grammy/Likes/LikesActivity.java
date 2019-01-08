package com.example.sara.grammy.Likes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sara.grammy.R;


public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesAcitivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = LikesActivity.this;

    //widgets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        Log.d(TAG, "onCreate:Started");;

    }


}
