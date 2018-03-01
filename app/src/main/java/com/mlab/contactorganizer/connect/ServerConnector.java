package com.mlab.contactorganizer.connect;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.MainActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by labud on 22.10.2017.
 *
 * https://hc.apache.org/httpcomponents-client-4.5.x/android-port.html
 */

public class ServerConnector {

    public static final String SERVER_ADDRESS = "http://10.0.2.2:2000/users";
    public static final String SMS_ADDRESS = "http://192.168.1.6:3000/sms";

    //public static final String LOGIN_ADDRESS = "http://77.55.218.181:2000/login";

    //public static final String CREATE_USER = "http://77.55.218.181:2000/user";

    public static final String CREATE_USER = "https://papamobile.cc/smsgateway/api/user";
    public static final String LOGIN_ADDRESS = "https://papamobile.cc/smsgateway/api/user";

    private final HttpPost post;

    private final int TIMEOUT_MS = 10000;

    private RequestQueue requestQueue;

    public ServerConnector(File cacheDir) {

        post = new HttpPost(SERVER_ADDRESS);
        createRequestQueue(cacheDir);
    }

    public void connect() {
        List<NameValuePair> myArgs = new ArrayList<NameValuePair>();
        myArgs.add(new BasicNameValuePair("username", "priyanka"));
        myArgs.add(new BasicNameValuePair("password", "pass"));
        try {
            post.setEntity(new UrlEncodedFormEntity(myArgs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void createRequestQueue(File cacheDir) {
        ;

// Instantiate the cache
        //TODO getCache Dir
        Cache cache = new DiskBasedCache(cacheDir, 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

    }
    public boolean tryLogin(String username, String password) {

        boolean logged = false;

        Map<String, String> params = new HashMap();
        params.put("username", username);
        params.put("password", password);

        JSONObject parameters = new JSONObject(params);

        /*
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SERVER_ADDRESS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ServerConnector", "Signup");
                        Log.d("ServerConnector", response.toString());
                        //mTxtDisplay.setText("Response: " + response.toString());

                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ServerConnector", "Error response");
                    }
                });
*/
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, SERVER_ADDRESS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ServerConnector", "Signup");
                        Log.d("ServerConnector", response.toString());
                        //mTxtDisplay.setText("Response: " + response.toString());

                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ServerConnector", "Error response");
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //TODO
        requestQueue.add(jsObjRequest);

        return true;
    }

    public boolean sendSms(String username, String smsText) {
        Map<String, String> params = new HashMap();
        params.put("username", username);
        params.put("sms", smsText);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SMS_ADDRESS, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ServerConnector", "Signup");
                        Log.d("ServerConnector", response.toString());
                        //mTxtDisplay.setText("Response: " + response.toString());

                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ServerConnector", "Error response");
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //TODO
        requestQueue.add(jsObjRequest);

        return true;

    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }


}
