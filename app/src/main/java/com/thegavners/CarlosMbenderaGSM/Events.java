package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


class Events extends Fragment {


    private final List<PostRow> listEventsRows = new ArrayList<>();
    private ListViewAdapter listViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ListView eventsListView = getActivity().findViewById(R.id.eventsListView);

        listViewAdapter = new ListViewAdapter(getContext(), listEventsRows);

        eventsListView.setAdapter(listViewAdapter);

        refreshEventsList();


    }

    private void refreshEventsList() {

        ParseQuery<ParseObject> getPosts = ParseQuery.getQuery("Events");

        getPosts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {

                if (e == null) {

                    listEventsRows.clear();

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

                        listEventsRows.add(foundPost);
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
