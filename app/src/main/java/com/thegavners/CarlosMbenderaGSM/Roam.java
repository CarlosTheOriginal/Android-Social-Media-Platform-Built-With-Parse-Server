package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class Roam extends Fragment {

    private final List<PostRow> listPostsRows = new ArrayList<>();
    private ListViewAdapter listViewAdapter;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private gettingSearchQueryListener activityCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityCallback = (gettingSearchQueryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement gettingSearchQueryListener");
        }
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roam, container, false);
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchItem != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    Log.i("OnQueryTextSubmit", query);

                    activityCallback.onSearch(query);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("OnQueryTextChange", newText);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {//not implemented here
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        ListView roamListView = getActivity().findViewById(R.id.roamContentListView);

        listViewAdapter = new ListViewAdapter(getContext(), listPostsRows);

        roamListView.setAdapter(listViewAdapter);

        refreshPostList();


    }

    private void refreshPostList() {

        ParseQuery<ParseObject> getPosts = ParseQuery.getQuery("Posts");

        getPosts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {

                if (e == null) {

                    listPostsRows.clear();

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

                        listPostsRows.add(foundPost);
                    }
                    listViewAdapter.notifyDataSetChanged();

                } else {

                    Log.i("Content Grabber", e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public interface gettingSearchQueryListener {
        void onSearch(String queryText);

    }


}


