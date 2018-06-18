package com.mlab.contactorganizer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.connect.ServerConnector;
import com.mlab.contactorganizer.connect.SslManager;
import com.mlab.contactorganizer.obj.UserObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailText;

    private ServerConnector serverConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        emailText = (EditText)findViewById(R.id.emailText);

        serverConnector = new ServerConnector(this.getCacheDir());
    }

    public void sendPassword(View view) {

        final String email = emailText.getText().toString();

        SslManager sslManager = new SslManager();
        sslManager.trustEveryone();

        String uri = String.format(ServerConnector.PASSWORD_FORGOT + "?email=%1$s",
                email);

        StringRequest passwordRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Sending service resp:", response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.get("status") != null) {
                        if (obj.get("status").equals(1)) {


                            //UserObj userObj = createUserObj(obj);
                            //TODO rzuc i obsluz wyjatek

                            //onLoginSuccess(userObj);
                        }
                    }

                    //TODO status 2 uzytkownik nie istnieje
                } catch (JSONException e) {
                    e.printStackTrace();
                    //onSignupFailed();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {


                //TODO
                //navigateSms();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("email", email);
                return MyData;
            }
        };

        serverConnector.getRequestQueue().add(passwordRequest);

        //TODO Michal
        //navigateSms();


        //navigateMainNavigation();
    }

}
