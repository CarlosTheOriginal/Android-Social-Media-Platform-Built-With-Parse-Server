package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class ListViewAdapter extends BaseAdapter {

    private final Context context;
    private final List<PostRow> listPostRows;

    ListViewAdapter(Context context, List<PostRow> listPostRows) {

        this.context = context;
        this.listPostRows = listPostRows;
    }

    @Override
    public int getCount() {
        return listPostRows.size();
    }

    @Override
    public Object getItem(int position) {
        return listPostRows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ListViewAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_post_row, null);

            viewHolder = new ListViewAdapter.ViewHolder();

            viewHolder.imageViewProfile = convertView.findViewById(R.id.postProfilePhoto);

            viewHolder.textViewUsername = convertView.findViewById(R.id.postUserName);
            viewHolder.textViewDisplayName = convertView.findViewById(R.id.textViewDisplayName);
            viewHolder.textViewPostTime = convertView.findViewById(R.id.postTime);
            viewHolder.postContent = convertView.findViewById(R.id.postTextContent);
            viewHolder.postImageContent = convertView.findViewById(R.id.postImageContent);

            viewHolder.imageViewComment = convertView.findViewById(R.id.imageViewComment);

            viewHolder.imageViewLike = convertView.findViewById(R.id.imageViewLike);

            viewHolder.imageViewBookmark = convertView.findViewById(R.id.imageViewBookmark);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ListViewAdapter.ViewHolder) convertView.getTag();

        }
        PostRow postRow = listPostRows.get(position);

        Picasso.get()
                .load(postRow.getImageURL())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.default_profile_photo)
                .into(viewHolder.imageViewProfile);

        Picasso.get()
                .load(postRow.getPostImageContent())
                .placeholder(R.color.colorAccent)
                .into(viewHolder.postImageContent);

        viewHolder.textViewUsername.setText(postRow.getUsername());
        viewHolder.textViewDisplayName.setText(postRow.getDisplayName());
        viewHolder.textViewPostTime.setText(postRow.getTimeOfPost());
        viewHolder.postContent.setText(postRow.getPostTextContent());


        return convertView;
    }

    class ViewHolder {

        ImageView imageViewProfile;
        TextView textViewUsername;
        TextView textViewDisplayName;

        TextView textViewPostTime;
        TextView postContent;
        ImageView postImageContent;

        ImageView imageViewComment;
        ImageView imageViewLike;
        ImageView imageViewBookmark;

    }

}