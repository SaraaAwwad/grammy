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

        LikesFragment fragment = new LikesFragment();
        android.support.v4.app.FragmentTransaction transaction = LikesActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(getString(R.string.notification_fragment));
        transaction.commit();

        Log.d(TAG, "onCreate:Started");;

    }


}
