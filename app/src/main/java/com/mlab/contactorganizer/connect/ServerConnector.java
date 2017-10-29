package com.mlab.contactorganizer.connect;

import org.apache.http.client.methods.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

    private final String SERVER_ADDRESS = "http://localhost:3000/users";
    private final HttpPost post;

    public ServerConnector() {
        post = new HttpPost(SERVER_ADDRESS);
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
}
