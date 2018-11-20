package com.example.sara.grammy;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    AnimationDrawable anim;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        setContentView(R.layout.login_layout);
        RelativeLayout container = findViewById(R.id.container);
        anim = (AnimationDrawable) container.getBackground();
        anim.setEnterFadeDuration(3000);
        anim.setExitFadeDuration(1000);
        signup = findViewById(R.id.signup_textview);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              //  setContentView(R.layout.register_layout);
                Intent myIntent = new Intent(v.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent,0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }




}