package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.parse.ParseUser;

import static com.parse.Parse.getApplicationContext;

class SetUpProfile extends Fragment {

    //Set the bio character change
    private EditText displayName;
    private EditText email;


    private void uploadCompleteProfile() {


        if (displayName.getText().toString().matches("")
                || email.getText().toString().matches("")
        ) {
            Toast.makeText(getApplicationContext(), "Both a Cool Name and Email is required.", Toast.LENGTH_SHORT).show();

        } else {
            ParseUser.getCurrentUser().put("Display Name", displayName.getText().toString());
            ParseUser.getCurrentUser().put("Email", email.getText().toString());


            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.signUpFragment, new setUpProfile2()).commit();

        }


    }


    @Override
    public void onStart() {


        displayName = getActivity().findViewById(R.id.setUpDisplayName);
        email = getActivity().findViewById(R.id.signUpEmail);

        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_up_profile, container, false);
    }

}
