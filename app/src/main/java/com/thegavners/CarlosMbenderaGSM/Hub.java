package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Hub extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        Roam.gettingSearchQueryListener {

    private Toolbar toolbar;

    private ListView searchResultsListView;
    private ArrayList<String> searchResults;
    private ArrayAdapter<String> searchResultsArrayAdapter;

    @Override
    public void onSearch(final String queryText) {

        searchResults.clear();
        final ParseQuery<ParseUser> searchBarQuery = ParseUser.getQuery();
        searchBarQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        searchBarQuery.whereEqualTo("username", queryText);

        searchBarQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        searchResultsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                CheckedTextView checkedTextView = (CheckedTextView) view;

                                if (checkedTextView.isChecked()) {

                                    if (ParseUser.getCurrentUser().get("isFollowing") == null) {

                                        ArrayList<String> emptyList = new ArrayList<>();
                                        ParseUser.getCurrentUser().put("isFollowing", emptyList);
                                    }

                                    Log.i("Search Results", "Row is checked  ");
                                    ParseUser.getCurrentUser().getList("isFollowing").add(searchResults.get(position));
                                    ParseUser.getCurrentUser().saveInBackground();

                                    ParseUser.
                                            getQuery().
                                            whereEqualTo("username", searchResults.get(position)).
                                            setLimit(1).
                                            findInBackground(new FindCallback<ParseUser>() {
                                                @Override
                                                public void done(List<ParseUser> objects, ParseException e) {
                                                    objects.get(0).put("IsFollowed", ParseUser.getCurrentUser());
                                                }
                                            });

                                } else {
                                    Log.i("Search Results", "Row is not checked.");
                                    ParseUser.getCurrentUser().getList("isFollowing").remove(searchResults.get(position));
                                    ParseUser.getCurrentUser().saveInBackground();
                                }
                            }
                        });

                        for (ParseUser user : objects) {

                            searchResults.add(user.getUsername());

                            searchResultsListView.setAdapter(searchResultsArrayAdapter);

                            switchToSearchResults();

                            setTitle("Results for " + queryText);

                        }

                        searchResultsArrayAdapter.notifyDataSetChanged();

                        for (String userNames : searchResults) {
                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(userNames)) {

                                searchResultsListView.setItemChecked(searchResults.indexOf(userNames), true);

                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), ("Sorry :( \n We can't find what you're looking for."), Toast.LENGTH_SHORT).show();
                        Log.i("Search Results Error", e.toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //Bottom Nav STarts here

            case R.id.switchToEvents:
                // Bottom nav switching to Feed
                switchToEvents();
                break;

            case R.id.SwitchToMyFeed:
                // Bottom nav switching to Feed
                switchToFeed();
                break;

            case R.id.switchToRoam:
                //Switch to Roam
                switchToRoam();
                break;

        }


        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Close Gavn.")
                    .setMessage("Are sure you want to leave Gavn now?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Close App Here
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        toolbar = findViewById(R.id.toolbar);

        searchResultsListView = findViewById(R.id.searchResultsListView);

        searchResults = new ArrayList<>();

        searchResultsArrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, searchResults);

        switchToFeed();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void switchToEvents() {
        toolbar.setTitle("Events");
        FragmentManager manager = getSupportFragmentManager();
        toolbar.setNavigationIcon(null);
        manager.beginTransaction().replace(R.id.hubFragment, new Events()).commit();
    }

    private void switchToFeed() {
        toolbar.setTitle("My Feed");
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hubFragment, new Feed()).commit();
    }

    private void switchToRoam() {
        toolbar.setTitle("Roam");
        toolbar.setNavigationIcon(null);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hubFragment, new Roam()).commit();
    }

    private void switchToSearchResults() {
        FragmentManager manager = getSupportFragmentManager();
        toolbar.setNavigationIcon(null);
        manager.beginTransaction().replace(R.id.hubFragment, new SearchResults()).commit();


    }


}
