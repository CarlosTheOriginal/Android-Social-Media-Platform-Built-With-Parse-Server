package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DirectChats extends AppCompatActivity {


    private final ArrayList<String> messages = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    public void sendChat(View view) {

        final EditText userMessage = findViewById(R.id.chatEditText);

        ParseObject message = new ParseObject("Message");

        final String messageContent = userMessage.getText().toString();

        message.put("Sender", ParseUser.getCurrentUser().getUsername());
        String activeUser = "";
        message.put("Receiver", activeUser);
        message.put("Message", messageContent);

        userMessage.setText("");

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    messages.add(userMessage.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_chats);


        Intent intent = getIntent();
        String activeUser = intent.getStringExtra("username");
        Log.i("Info", activeUser);

        setTitle("Chat with " + activeUser);


        ListView chatsListView = findViewById(R.id.chatsListView);
        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, messages);
        chatsListView.setAdapter(arrayAdapter);

        ParseQuery<ParseObject> messageQuery1 = new ParseQuery<>("Message");

        messageQuery1.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());
        messageQuery1.whereEqualTo("Receiver", activeUser);

        ParseQuery<ParseObject> messageQuery2 = new ParseQuery<>("Message");

        messageQuery2.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
        messageQuery2.whereEqualTo("Sender", activeUser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<>();

        queries.add(messageQuery1);
        queries.add(messageQuery2);

        final ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 1) {

                        for (ParseObject message : objects) {

                            String messageContent = message.getString("Message");

                            if (!message.getString("Sender").equals(ParseUser.getCurrentUser().getUsername())) {

                                messageContent = "> " + messageContent;
                            }
                            messages.add(messageContent);

                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
