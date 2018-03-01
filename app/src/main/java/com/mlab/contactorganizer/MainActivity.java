package com.mlab.contactorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.connect.ServerConnector;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    //EditText username = (EditText)findViewById(R.id.editText);
    //EditText password = (EditText)findViewById(R.id.editText2);

    EditText username;
    EditText password;

    private ServerConnector serverConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);

        serverConnector = new ServerConnector(this.getCacheDir());
    }

    public void login(View view) {

        final String email = username.getText().toString();
        final String pass = password.getText().toString();

        trustEveryone();
        StringRequest loginRequest = new StringRequest(Request.Method.GET, ServerConnector.LOGIN_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                navigateSms();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                username.setError("Nieprawidłowy login i/lub hasło");

                //TODO
                navigateSms();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("email", email);
                MyData.put("password", pass);
                return MyData;
            }
        };

        serverConnector.getRequestQueue().add(loginRequest);

        //TODO Michal
        //navigateSms();
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    public void register(View view) {
        navigateSignup();
    }

    private void navigateSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);

    }

    private void navigateSms() {
        Intent intent = new Intent(this, SmsActivity.class);
        startActivity(intent);

    }

    }
