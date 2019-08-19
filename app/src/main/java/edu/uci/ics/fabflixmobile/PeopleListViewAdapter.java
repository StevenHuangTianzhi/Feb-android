package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PeopleListViewAdapter extends ArrayAdapter<JSONObject> {
    private ArrayList<JSONObject> movies;

    public PeopleListViewAdapter(ArrayList<JSONObject> movies, Context context) {
        super(context, R.layout.layout_listview_row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_listview_row, parent, false);

        JSONObject movie = movies.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        TextView subtitleView = (TextView)view.findViewById(R.id.subtitle);
        TextView starsView = (TextView)view.findViewById(R.id.stars);
        TextView genresView = (TextView)view.findViewById(R.id.genres);

        try {
            titleView.setText(movie.getString("movie_title"));
            subtitleView.setText(movie.getString("movie_year") + ", directed by " + movie.getString("movie_director"));
            String stars = "";
            String genres = "";
            JSONArray star = (JSONArray) movie.get("movie_stars");
            JSONArray genre = (JSONArray) movie.get("movie_genres");

            for (int i = 0; i < star.length();i++)
            {
                stars += star.get(i) + ", ";
            }

            for (int i = 0; i < genre.length();i++)
            {
                genres += genre.get(i) + ", ";
            }
            starsView.setText("Stars: " + stars.trim().substring(0,stars.length()-2));
            genresView.setText("Genres: " + genres.trim().substring(0,genres.length()-2));
        }
        catch (Exception e)
        {
            System.out.println("Exception when populating list view: " + e);
        }

        return view;
    }
}