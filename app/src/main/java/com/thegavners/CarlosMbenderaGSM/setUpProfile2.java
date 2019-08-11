package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;


class setUpProfile2 extends Fragment {


    private ImageView setUpProfilePhoto;
    private EditText userBio;
    private TextView userBioLength;
    private final TextWatcher bioWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userBioLength.setText(String.valueOf(s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private Boolean photoUploaded;
    private Boolean userBioContent;
    private Boolean bothPhotoAndBio;
    private byte[] uploadPhotoByteArray;

    private void uploadProfilePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent photoUpload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoUpload, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadProfilePhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {

                Bitmap imageSelected = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                Log.i("ProfilePhotoUpload", "Photo Received");

                // setUpProfilePhoto.setImageBitmap(imageSelected);

                Picasso.get()
                        .load(selectedImage)
                        .transform(new CropCircleTransformation())
                        .placeholder(R.drawable.default_profile_photo)
                        .into(setUpProfilePhoto);

                ByteArrayOutputStream uploadStream = new ByteArrayOutputStream();
                imageSelected.compress(Bitmap.CompressFormat.PNG, 100, uploadStream);
                uploadPhotoByteArray = uploadStream.toByteArray();

                photoUploaded = true;

                if (userBioContent = true) {
                    bothPhotoAndBio = true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {

        getActivity().setTitle("Who is " + ParseUser.getCurrentUser().getUsername() + "?");

        setUpProfilePhoto = getActivity().findViewById(R.id.setUpProfilePhoto);
        photoUploaded = false;
        userBioContent = false;
        bothPhotoAndBio = false;

        userBioLength = getActivity().findViewById(R.id.bioLength);

        userBio = getActivity().findViewById(R.id.setUpProfileBio);
        userBio.addTextChangedListener(bioWatcher);

        setUpProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePhoto();
            }
        });

        Button doneButton = getActivity().findViewById(R.id.setUpProfile2Done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfile2();
            }
        });

        super.onStart();
    }

    private void uploadProfile2() {

        if (userBio.getText().length() > 144) {
            Toast.makeText(getApplicationContext(), "Your Bio is too long.", Toast.LENGTH_SHORT).show();
        } else if (photoUploaded) {


            ParseFile userUploadedProfilePhoto = new ParseFile("Image.png", uploadPhotoByteArray);
            ParseUser.getCurrentUser().put("Profile Photo", userUploadedProfilePhoto);

            userUploadedProfilePhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        uploadReference();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please try again later.", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } else if (userBioContent) {

            ParseUser.getCurrentUser().put("Bio", userBio.getText().toString());

            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();

            uploadReference();
        } else if (bothPhotoAndBio) {

            ParseUser.getCurrentUser().put("Bio", userBio.getText().toString());

            ParseFile userUploadedProfilePhoto = new ParseFile("Images.Png", uploadPhotoByteArray);
            ParseUser.getCurrentUser().put("Profile Photo", userUploadedProfilePhoto);

            userUploadedProfilePhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        uploadReference();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please try again later.", Toast.LENGTH_SHORT).show();
                        uploadReference();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "If you change your mind.\n" +
                    "You can enter your details later.", Toast.LENGTH_SHORT).show();
            uploadReference();

        }
    }

    private void uploadReference() {

        final CheckBox friendReferenceBox = new CheckBox(getContext());
        friendReferenceBox.setText("If a friend recommended us. Tap this box and enter their username. Otherwise, just type in how you found us.");


        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText referenceEditText = new EditText(getApplicationContext());
        linearLayout.addView(referenceEditText);
        linearLayout.addView(friendReferenceBox);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setTitle(" How did you find us?");
        builder.setView(linearLayout);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Log.i("Reference", referenceEditText.getText().toString());

                ParseObject reference = new ParseObject("Reference");

                if (friendReferenceBox.isChecked()) {

                    reference.put("User", ParseUser.getCurrentUser().getUsername());

                    reference.put("Referee", referenceEditText.getText().toString());

                    reference.put("Display Name", ParseUser.getCurrentUser().get("Display Name"));

                    reference.put("User ID", ParseUser.getCurrentUser());

                } else {
                    reference.put("User", ParseUser.getCurrentUser().getUsername());

                    reference.put("Content", referenceEditText.getText().toString());

                    reference.put("Display Name", ParseUser.getCurrentUser().get("Display Name"));

                    reference.put("User ID", ParseUser.getCurrentUser());

                    reference.saveEventually();
                }
                startActivity(new Intent(getApplicationContext(), Hub.class));

            }
        });

        builder.setNegativeButton("Found It Myself.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), Hub.class));
                dialog.cancel();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_up_profile2, container, false);
    }

}
