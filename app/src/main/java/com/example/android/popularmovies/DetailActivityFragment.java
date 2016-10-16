package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.example.android.popularmovies.R.layout.fragment_detail, container, false);

        Intent detailIntent = getActivity().getIntent();
        if (detailIntent != null){
            TextView textTitle = (TextView) rootView.findViewById(com.example.android.popularmovies.R.id.textTitle);
            textTitle.setText(detailIntent.getStringExtra(getString(com.example.android.popularmovies.R.string.extraTitle)));

            TextView textVote = (TextView) rootView.findViewById(com.example.android.popularmovies.R.id.textVote);
            double voteAverage = detailIntent.getDoubleExtra(getString(com.example.android.popularmovies.R.string.extraVote),0);
            textVote.setText("" + voteAverage + " / 10");

            ImageView imagePoster = (ImageView) rootView.findViewById(com.example.android.popularmovies.R.id.imageView);
            String posterPath = detailIntent.getStringExtra(getString(com.example.android.popularmovies.R.string.extraPoster));
            Picasso.with(getContext()).load(posterPath).into(imagePoster);

            TextView textRelease = (TextView) rootView.findViewById(com.example.android.popularmovies.R.id.textRelease);
            textRelease.setText("Release date: " + detailIntent.getStringExtra(getString(com.example.android.popularmovies.R.string.extraRelease)));

            TextView textSynopsis = (TextView) rootView.findViewById(com.example.android.popularmovies.R.id.textSynopsis);
            textSynopsis.setText(detailIntent.getStringExtra("extraSynopsis"));


        }

        return rootView;
    }
}
