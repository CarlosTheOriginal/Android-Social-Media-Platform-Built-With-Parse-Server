package com.thegavners.CarlosMbenderaGSM;
// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseACL;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Parse.enableLocalDatastore(this);

        // Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
        // .applicationId()
        // .clientKey()
        // .server()
        // .build());


        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        int SPLASH_DISPLAY_LENGTH = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(getApplicationContext(), SignIn.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}
