package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Chats extends AppCompatActivity {

    private final ArrayList<String> users = new ArrayList<>();
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        setTitle("Chats");

        ListView userListView = findViewById(R.id.chatsListView);
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), DirectChats.class);
                intent.putExtra("username", users.get(position));
                startActivity(intent);
            }
        });

        users.clear();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        userListView.setAdapter(arrayAdapter);

        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {

                            users.add(user.getUsername());

                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }
}
