package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ListViewActivity extends Activity {

    private ArrayList<JSONObject> movies = new ArrayList<JSONObject>();
    private int itemPerPage = 4;
    private int currentPage = 1;
    private int lastPage = 1;
    private String selectId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        String response = getIntent().getExtras().getString("response");
        JSONArray responseArray;
        try {
            responseArray = (JSONArray) new JSONTokener(response).nextValue();
        }
        catch (Exception e){
            System.out.println("exception: " + e);
            return;
        }

        final ArrayList<JSONObject> movies = new ArrayList<>();

        for(int i = 0; i < responseArray.length(); i++)
        {
            try {
                movies.add(responseArray.getJSONObject(i));
            }
            catch (Exception e)
            {
                System.out.println("Exception when adding JSONObject" + e);
                return;
            }
        }

        this.movies = movies;

        int numOfPage = this.movies.size()/itemPerPage + 1;
        this.lastPage = numOfPage;


        final ArrayList<JSONObject> moviesToDis = new ArrayList<>();
        for (int i = 0; i < itemPerPage; i++)
        {
            moviesToDis.add(this.movies.get(i));
        }
        PeopleListViewAdapter adapter = new PeopleListViewAdapter(moviesToDis, this);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int indexOnSelect = 4 * currentPage + position - 4;
                JSONObject movie = movies.get(indexOnSelect);
                try{
                    String movieId = movie.getString("movie_id");
                    selectId = movieId;
                    //String message = String.format("Clicked on position: %d, index: %d, movie_id: %s", position,indexOnSelect,selectId);
                    //System.out.println(message);

                    connectToTomcat(view);
                }
                catch(Exception e)
                {
                    System.out.println("Error in set on click Listener");
                }

            }
        });

    }

    public void connectToTomcat(View view)
    {
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest searchRequest = new StringRequest(Request.Method.GET, "https://ec2-52-53-125-21.us-west-1.compute.amazonaws.com:8443/2019w-project2-login-cart-example/api/single-movie?id=" + selectId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Search Result", response);
                        System.out.println("response: "+ response);

                        JSONArray responseArray;
                        try {
                            responseArray = (JSONArray) new JSONTokener(response).nextValue();
                        }
                        catch (Exception e){
                            System.out.println("exception: " + e);
                            return;
                        }
                        goToSingleMovie(response);

                        /*
                        try {
                            if (responseObj.getString("status").equals("success"))
                                goToBlue();
                        }
                        catch( Exception e) {
                            System.out.println(e);
                            return;
                        }
                        */

                        // Add the request to the RequestQueue.
                        //queue.add(afterLoginRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }
        )


        {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data

                final Map<String, String> params = new HashMap<String, String>();
                params.put("id",selectId);

                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(searchRequest);



    }

    public void goToSingleMovie(String response)
    {
        Intent goToIntent = new Intent(this, GreenActivity.class);
        goToIntent.putExtra("response", response);
        startActivity(goToIntent);
    }

    public void onClickPrev(View view)
    {
        if(this.currentPage == 1)
        {
            String message = String.format("This is the first page.");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            this.currentPage -= 1;
            final ArrayList<JSONObject> moviesToDis = new ArrayList<>();

            int startIndex = 4 * this.currentPage - 4;
            int endIndex = 4 * this.currentPage - 1;


            for (int i = startIndex; i <=endIndex; i++)
            {
                moviesToDis.add(this.movies.get(i));
            }

            PeopleListViewAdapter adapter = new PeopleListViewAdapter(moviesToDis, this);
            ListView listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(adapter);
        }
    }

    public void onClickNext(View view)
    {
        if(this.currentPage == this.lastPage)
        {
            String message = String.format("This is the last page.");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            this.currentPage += 1;
            final ArrayList<JSONObject> moviesToDis = new ArrayList<>();

            int startIndex = 4 * this.currentPage - 4;
            int endIndex = 4 * this.currentPage - 1;
            int endBound = Math.min(endIndex,this.movies.size()-1);

            for (int i = startIndex; i <=endBound; i++)
            {
                moviesToDis.add(this.movies.get(i));
            }

            PeopleListViewAdapter adapter = new PeopleListViewAdapter(moviesToDis, this);
            ListView listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(adapter);

        }
    }
}
