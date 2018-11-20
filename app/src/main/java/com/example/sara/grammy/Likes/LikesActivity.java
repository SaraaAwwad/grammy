package com.example.sara.grammy.Likes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sara.grammy.R;


public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesAcitivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate:Started");
    }
}
