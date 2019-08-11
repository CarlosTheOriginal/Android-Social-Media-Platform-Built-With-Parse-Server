package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private final List<PostRow> listPostRows = new ArrayList<>();
    private ListViewAdapter listViewAdapter;

    private ImageView profilePhoto;

    private void refreshMyPostList() {

        ParseQuery<ParseObject> getPosts = ParseQuery.getQuery("Posts");

        getPosts.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

        getPosts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {

                if (e == null) {

                    listPostRows.clear();

                    for (ParseObject post : postList) {

                        ParseUser poster = (ParseUser) post.get("UserID");
                        ParseFile profileImage = (ParseFile) poster.get("Profile Photo");
                        String imageUrl = profileImage.getUrl();


                        ParseFile postImageContent = post.getParseFile("Image");
                        String postImageUrl = postImageContent.getUrl();

                        PostRow foundPost = new PostRow(
                                imageUrl,
                                postImageUrl,
                                post.getString("Content"),
                                post.getString("Time"),
                                post.getString("User"),
                                post.getString("Display Name"));

                        listPostRows.add(foundPost);
                    }
                    listViewAdapter.notifyDataSetChanged();

                } else {

                    Log.i("Content Grabber", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getProfilePhoto() {

        ParseQuery<ParseObject> findProfilePhoto = new ParseQuery<>("Profile Photo");
        findProfilePhoto.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        findProfilePhoto.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile imageFile = (ParseFile) object.get("Profile Photo");
                            imageFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {

                                        Bitmap profilePhotoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        profilePhoto.setImageBitmap(profilePhotoBitmap);

                                    }
                                }
                            });
                        }
                    }
                } else {
                    Drawable defaultProfilePhoto = getResources().getDrawable(R.drawable.default_profile_photo);
                    profilePhoto.setImageDrawable(defaultProfilePhoto);
                }
            }
        });

    }

    private void setData() {


        TextView displayName = findViewById(R.id.profileDisplayName);
        TextView profilePhotoUsername = findViewById(R.id.profilePhotoUsername);
        TextView userStory = findViewById(R.id.userStory);

        profilePhoto = findViewById(R.id.profilePhoto);

        TextView followerNumber = findViewById(R.id.followerNumber);
        TextView followingNumber = findViewById(R.id.followingNumber);


        followerNumber.setText(ParseUser.getCurrentUser().getList("isFollowed").size());
        followingNumber.setText(ParseUser.getCurrentUser().getList("isFollowing").size());


        profilePhotoUsername.setText(ParseUser.getCurrentUser().getUsername());
        displayName.setText(ParseUser.getCurrentUser().get("Display Name").toString());
        userStory.setText(ParseUser.getCurrentUser().get("Story").toString());


        getProfilePhoto();


        ListView myPostsListView = findViewById(R.id.myPostsListView);

        listViewAdapter = new ListViewAdapter(getApplicationContext(), listPostRows);

        myPostsListView.setAdapter(listViewAdapter);

        refreshMyPostList();

    }

    private void uploadNewProfilePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                uploadNewProfilePhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap imageSelected = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                Log.i("ProfilePhotoUpload", "Photo Received");

                ByteArrayOutputStream uploadStream = new ByteArrayOutputStream();
                imageSelected.compress(Bitmap.CompressFormat.PNG, 100, uploadStream);
                byte[] uploadPhotoByteArray = uploadStream.toByteArray();

                ParseFile userUploadedProfilePhoto = new ParseFile("Images.Png", uploadPhotoByteArray);
                ParseUser.getCurrentUser().put("Profile Photo", userUploadedProfilePhoto);

                userUploadedProfilePhoto.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Upload failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        setData();

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewProfilePhoto();
            }
        });

    }
}
