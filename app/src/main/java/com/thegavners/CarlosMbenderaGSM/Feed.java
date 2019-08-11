package com.thegavners.CarlosMbenderaGSM;


// Copyright 2019 Carlos Mbendera

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.getApplicationContext;


class Feed extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private final List<PostRow> listPostRows = new ArrayList<>();
    private ListViewAdapter listViewAdapter;
    private ImageView navPP;

    private void uploadTextPost() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(" Write Something! Anything you want.");
        final EditText postEditText = new EditText(getContext());
        builder.setView(postEditText);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i("Post", postEditText.getText().toString());

                ParseObject post = new ParseObject("Posts");

                post.put("User", ParseUser.getCurrentUser().getUsername());

                post.put("Content", postEditText.getText().toString());

                post.put("Display Name", ParseUser.getCurrentUser().get("Display Name"));


                post.put("Comment Count", 0);

                post.put("Like Count", 0);


            }
        });

        builder.setNegativeButton("Never Mind.I'm good.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
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
                                        navPP.setImageBitmap(profilePhotoBitmap);

                                    }
                                }
                            });
                        }
                    }
                } else {
                    Drawable defaultProfilePhoto = getResources().getDrawable(R.drawable.default_profile_photo);
                    navPP.setImageDrawable(defaultProfilePhoto);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


        TextView navUserName = getActivity().findViewById(R.id.navHeaderUsername);
        TextView navDN = getActivity().findViewById(R.id.navHeaderDN);
        navPP = getActivity().findViewById(R.id.navHeaderPP);

        navUserName.setText(ParseUser.getCurrentUser().getUsername());

        navDN.setText(ParseUser.getCurrentUser().get("Display Name").toString());

        getProfilePhoto();

        FloatingActionButton uploadButton = getActivity().findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadTextPost();
            }
        });

        ListView feedPostsListView = getActivity().findViewById(R.id.feedListView);

        listViewAdapter = new ListViewAdapter(getContext(), listPostRows);

        feedPostsListView.setAdapter(listViewAdapter);

        refreshFeedList();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.switchToMyProfile:
                startActivity(new Intent(getApplicationContext(), Profile.class));

            case R.id.savedPosts:
                startActivity(new Intent(getApplicationContext(), SavedPosts.class));
                break;

            case R.id.chat:
                startActivity(new Intent(getApplicationContext(), Chats.class));
                break;

            case R.id.invite:
                startActivity(new Intent(getApplicationContext(), Invite.class));
                break;

            case R.id.feedBack:

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(" What's on your mind?");
                final EditText feedbackEditText = new EditText(getApplicationContext());
                builder.setView(feedbackEditText);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i("FeedBack", feedbackEditText.getText().toString());

                        ParseObject feedback = new ParseObject("FeedBack");

                        feedback.put("User", ParseUser.getCurrentUser().getUsername());

                        feedback.put("Content", feedbackEditText.getText().toString());

                        feedback.put("Display Name", ParseUser.getCurrentUser().get("Display Name"));


                        feedback.put("User ID", ParseUser.getCurrentUser());

                        feedback.saveEventually();

                    }
                });

                builder.setNegativeButton("Never Mind.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                break;

          /*
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), settings.class));
                break;

                */

            case R.id.logOut:
                ParseUser.logOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                break;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    private void refreshFeedList() {

        ParseQuery<ParseObject> getPosts = ParseQuery.getQuery("Posts");

        getPosts.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        getPosts.orderByDescending("createdAt");

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
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
