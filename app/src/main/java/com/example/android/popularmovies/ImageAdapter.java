package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by romul on 04/10/2016.
 */

//Redefine getView to show an image as item for the GridView
//Source: https://developer.android.com/guide/topics/ui/layout/gridview.html#example
public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private int mHeight;
    private int mWidth;


    public ImageAdapter(Context c){
        this.mContext = c;
        this.mMovies = new ArrayList<>();
        mHeight = Math.round(mContext.getResources().getDimension(com.example.android.popularmovies.R.dimen.poster_height));
        mWidth = Math.round(mContext.getResources().getDimension(com.example.android.popularmovies.R.dimen.poster_width));
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        Movie movie = getItem(position);
        return movie.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        ImageView imageView;
        if (convertView == null){
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mWidth,mHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //imageView.setPadding(0,4,0,4);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(movie.getPosterURL()).into(imageView);
        //imageView.setImageResource(Pics[position]);

        return imageView;
    }

    public void addAll(ArrayList<Movie> movies){
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void clearAll(){
        mMovies.clear();
        notifyDataSetChanged();
    }
}
