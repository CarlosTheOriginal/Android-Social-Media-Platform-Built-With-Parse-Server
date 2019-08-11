package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.switchToSignUp) {
            Log.i("Switch To Sign Up", "User is switching to Sign Up Activity");
            Intent switchToRegister = new Intent(getApplicationContext(), SignUp.class);
            startActivity(switchToRegister);
        } else if (view.getId() == R.id.forgotPassword) {
            Log.i("Password Reset", "User is resetting password");
            Intent resetPasswordSwitch = new Intent(this, ResetPassword.class);
            startActivity(resetPasswordSwitch);
        }
    }

    public void logIn(View view) {

        EditText userName = findViewById(R.id.logInUsername);
        EditText password = findViewById(R.id.logInPassword);

        if (userName.getText().toString().matches("") || password.getText().toString().matches("")) {

            Toast.makeText(this, "Both your Username and Password are required.", Toast.LENGTH_SHORT).show();

        } else {
            ParseUser.logInInBackground(
                    userName.getText().toString(),
                    password.getText().toString(),
                    new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Log.i("LogIn", "Success");
                                Toast.makeText(getApplicationContext(), "Welcome Back " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Hub.class));
                            } else {
                                Log.i("LogIn", "Failed due to error " + e.toString());
                                Toast.makeText(getApplicationContext(), "Log In failed because of " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setTitle("Sign In");


        TextView switchToSignUp = findViewById(R.id.switchToSignUp);
        switchToSignUp.setOnClickListener(this);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        //forgotPassword.setVisibility(View.INVISIBLE);


    }
}
