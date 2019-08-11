package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.signUpUsername);
        password = findViewById(R.id.signUpPassword);

        setTitle("Sign Up");

    }

    public void signUpButton(View view) {


        if (username.getText().toString().matches("")
                || password.getText().toString().matches("")
        ) {
            Toast.makeText
                    (this, "You need to fill in all the fields",
                            Toast.LENGTH_SHORT).show();

        } else if (username.getText().length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Yor Username should be greater than 6 characters", Toast.LENGTH_SHORT).show();


        } else if (username.getText().length() > 12) {
            Toast.makeText(getApplicationContext(),
                    "Yor Username should be less than 12 characters", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Please Wait, while we sign you up.", Toast.LENGTH_SHORT).show();

            ParseUser user = new ParseUser();

            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Log.i("Sign Up", "Successful");
                        Toast.makeText
                                (getApplicationContext(), "Welcome to Gavn " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CreateProfile.class));

                    } else {

                        Log.i("Sign Up", "Sign Up failed due to: "
                                + e.toString()
                                + ".");
                        Toast.makeText
                                (getApplicationContext(), "Please try again later. ", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }


}

