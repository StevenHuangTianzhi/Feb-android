package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class BlueActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Bundle bundle = getIntent().getExtras();
        //Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        /*
        String msg = bundle.getString("message");
        if (msg != null && !"".equals(msg)) {
            ((TextView) findViewById(R.id.last_page_msg_container)).setText(msg);
        }
        */

    }
    /*
    public void goToRed(View view) {
        String msg = ((EditText) findViewById(R.id.blue_2_red_message)).getText().toString();

        Intent goToIntent = new Intent(this, RedActivity.class);

        goToIntent.putExtra("last_activity", "blue");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
    */


    public void goToListview(String response)
    {
        Intent goToIntent = new Intent(this, ListViewActivity.class);
        goToIntent.putExtra("response", response);
        startActivity(goToIntent);
    }

    public void connectToTomcat(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator

        String keyword = ((EditText) findViewById(R.id.keywords)).getText().toString();
        if(keyword.equals(""))
        {
            Toast.makeText(this, "Please enter the keyword of the movie title", Toast.LENGTH_LONG).show();
            return;
        }
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, "https://ec2-52-53-125-21.us-west-1.compute.amazonaws.com:8443/2019w-project2-login-cart-example/api/movie-list?title="+keyword+"&year=&director=&starname=&genre=&browse=False",
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
                        goToListview(response);

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
                String title = ((EditText) findViewById(R.id.keywords)).getText().toString();
                final Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("year", "");
                params.put("director", "");
                params.put("genre", "");
                params.put("starname", "");
                params.put("browse", "False");

                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(searchRequest);

    }

}
