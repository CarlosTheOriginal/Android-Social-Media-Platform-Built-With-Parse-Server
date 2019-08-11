package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

class PostRow {

    private String imageURL;
    private String postTextContent;
    private String postImageContent;
    private String timeOfPost;

    private String username;
    private String displayName;

    PostRow(String rowImageURL, String rowPostImageContent, String rowPostTextContent, String rowTimeOfPost, String rowUsername,
            String rowDisplayName) {

        imageURL = rowImageURL;
        postImageContent = rowPostImageContent;
        postTextContent = rowPostTextContent;
        timeOfPost = rowTimeOfPost;

        username = rowUsername;
        displayName = rowDisplayName;


    }

    //getters and setters

    String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    String getPostImageContent() {
        return postImageContent;
    }

    public void setPostImageContent(String postImageContent) {
        this.postImageContent = postImageContent;
    }

    String getPostTextContent() {
        return postTextContent;
    }

    public void setPostTextContent(String postTextContent) {
        this.postTextContent = postTextContent;
    }


    String getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        this.timeOfPost = timeOfPost;
    }


    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


}