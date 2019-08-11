package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SavedPosts extends AppCompatActivity {

    private final List<PostRow> listPostRows = new ArrayList<>();
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        setTitle("Posts you've saved.");

        ListView postsListView = findViewById(R.id.savedPostListView);

        listViewAdapter = new ListViewAdapter(getApplicationContext(), listPostRows);

        postsListView.setAdapter(listViewAdapter);

        getPostList();
    }

    private void getPostList() {

        ParseQuery<ParseObject> getPosts = ParseQuery.getQuery("Posts");

        getPosts.whereContainedIn("username", ParseUser.getCurrentUser().getList("saved"));
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

                        String postImageUrl;

                        ParseFile postImageContent = post.getParseFile("Image");
                        if (postImageContent != null) {
                            postImageUrl = postImageContent.getUrl();
                        } else {
                            postImageUrl = null;
                        }

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

}
