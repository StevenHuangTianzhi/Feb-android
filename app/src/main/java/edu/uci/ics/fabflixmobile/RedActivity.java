package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class RedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);
/**
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("last_activity") != null) {
                Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();
            }
            String msg = bundle.getString("message");
            if (msg != null && !"".equals(msg)) {
                ((TextView) findViewById(R.id.last_page_msg_container)).setText(msg);
            }
        }
 */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_red, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connectToTomcat(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        /*
        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/2019w-project4-login-example/api/username",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("username.reponse", response);
                        //((TextView) findViewById(R.id.http_response)).setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("username.error", error.toString());
                    }
                }
        );
        */

        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://ec2-52-53-125-21.us-west-1.compute.amazonaws.com:8443/2019w-project2-login-cart-example/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);


                        JSONObject responseObj;
                        try {
                            responseObj = (JSONObject) new JSONTokener(response).nextValue();
                        }
                        catch (Exception e){
                            System.out.println(e);
                            return;
                        }
                        try {
                            if (responseObj.getString("status").equals("success"))
                                goToBlue();
                            else if(responseObj.getString("status").equals("fail1"))
                            {
                                ((TextView) findViewById(R.id.http_response)).setText("The username does not exist.");
                            }
                            else if(responseObj.getString("status").equals("fail2"))
                            {
                                ((TextView) findViewById(R.id.http_response)).setText("Incorrect password.");
                            }
                        }
                        catch( Exception e) {
                            System.out.println(e);
                            return;
                        }

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
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                String username =
                params.put("username", ((EditText) findViewById(R.id.username_entry)).getText().toString());
                params.put("password", ((EditText) findViewById(R.id.password_entry)).getText().toString());

                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }

    public void goToBlue() {
        String msg = ((EditText) findViewById(R.id.username_entry)).getText().toString();

        Intent goToIntent = new Intent(this, BlueActivity.class);

        goToIntent.putExtra("last_activity", "red");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }

    public void goToGreen() {
        String msg = ((EditText) findViewById(R.id.username_entry)).getText().toString();

        Intent goToIntent = new Intent(this, GreenActivity.class);

        goToIntent.putExtra("last_activity", "red");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
}
