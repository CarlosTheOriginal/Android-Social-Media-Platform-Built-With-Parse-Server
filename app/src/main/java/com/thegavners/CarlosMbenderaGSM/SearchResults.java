package com.thegavners.CarlosMbenderaGSM;

// Copyright 2019 Carlos Mbendera

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;


class SearchResults extends Fragment {


    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            Roam.gettingSearchQueryListener activityCallback = (Roam.gettingSearchQueryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement gettingSearchQueryListener");
        }
    }

    @Nullable


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

    }


    public interface gettingSearchQueryListener {
        void onSearch(String queryText);

    }


}
