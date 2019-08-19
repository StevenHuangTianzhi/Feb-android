package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green);

        String response = getIntent().getExtras().getString("response");
        JSONArray responseArray;
        JSONObject result;
        try {
            responseArray = (JSONArray) new JSONTokener(response).nextValue();
            result = responseArray.getJSONObject(0);


            String title = result.getString("movie_title");
            String year = result.getString("movie_year");
            String director = result.getString("movie_director");
            String stars = "";
            String genres = "";

            JSONArray star = (JSONArray) result.get("movie_stars");
            JSONArray genre = (JSONArray) result.get("movie_genres");

            for (int i = 0; i < star.length();i++)
            {
                stars += star.get(i) + ", ";
            }

            for (int i = 0; i < genre.length();i++)
            {
                genres += genre.get(i) + ", ";
            }

            ((TextView) findViewById(R.id.movie_title)).setText(title);
            ((TextView) findViewById(R.id.movie_year)).setText("Year: " + year);
            ((TextView) findViewById(R.id.movie_director)).setText("Director: " + director);
            ((TextView) findViewById(R.id.movie_stars)).setText("Stars: " + stars.trim().substring(0,stars.length()-2));
            ((TextView) findViewById(R.id.movie_genres)).setText("Genres: " + genres.trim().substring(0,genres.length()-2));
        }
        catch (Exception e){
            System.out.println("exception: " + e);
            return;
        }

        }
    }
