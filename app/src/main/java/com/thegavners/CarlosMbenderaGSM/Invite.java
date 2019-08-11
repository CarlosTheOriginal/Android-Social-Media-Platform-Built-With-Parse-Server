package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class Invite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        setTitle("It's dangerous to go alone.");

        TextView username = findViewById(R.id.inviteUsername);
        username.setText(ParseUser.getCurrentUser().getUsername());

        Button inviteButton = findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteMethod();
            }
        });
    }

    private void inviteMethod() {
        //Sharing Implementation here

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String gavnLink = "";
        String shareBody = "Dear Awesome Person,\n " +
                "I'm using this app called Gavn and I think you'll like it. Please check it out at "
                + gavnLink
                + ". And enter my username @" + ParseUser.getCurrentUser().getUsername() + "as a reference." +
                " \n Thanks.";

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Cool App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, "Share Via "));
    }

    public void inviteView(View view) {
        inviteMethod();
    }
}
