package com.example.sara.grammy.Likes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.sara.grammy.Home.MainActivity;
import com.example.sara.grammy.R;
import com.google.firebase.auth.FirebaseAuth;


public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesAcitivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = LikesActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

}
