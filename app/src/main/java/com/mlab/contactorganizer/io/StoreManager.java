package com.mlab.contactorganizer.io;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mlab.contactorganizer.obj.UserObj;

import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

public class StoreManager {

    private static String USER_FILE = "userFile.txt";
    private static String USER_ID_PROP = "id";
    private static String USER_NAME_PROP = "name";
    private static String USER_EMAIL_PROP = "email";
    private static String USER_PASSWORD_PROP = "password";
    private static String USER_TOKEN_PROP = "token";

    public UserObj getUserData(AppCompatActivity activity) {
        FileManager fileManager = new FileManager();
        Properties properties = fileManager.readProperties(USER_FILE, activity);

        String id = properties.getProperty(USER_ID_PROP);
        String name = properties.getProperty(USER_NAME_PROP);
        String email = properties.getProperty(USER_EMAIL_PROP);
        //TODO szyfrowanie
        String password = properties.getProperty(USER_PASSWORD_PROP);
        String token = properties.getProperty(USER_TOKEN_PROP);

        Log.d("Properties file: ", properties.toString());

        Log.d("Reading user data ", "id: " + id);
        Log.d("Reading user data ", "name: " + name);
        Log.d("Reading user data ", "email: " + email);
        Log.d("Reading user data ", "password: " + password);
        Log.d("Reading user data ", "token: " + token);

        //TODO ktore pola sa obowiazkowe?
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(email)) {
            UserObj userObj = new UserObj();
            userObj.setId(id);
            userObj.setName(name);
            userObj.setPassword(password);
            userObj.setToken(token);

            return userObj;
        }

        return null;
    }

    public void saveUserData(UserObj userObj, AppCompatActivity activity) {

        Properties props = new Properties();

        addProperty(USER_ID_PROP, userObj.getId(), props);
        addProperty(USER_NAME_PROP, userObj.getName(), props);
        addProperty(USER_EMAIL_PROP, userObj.getEmail(), props);
        //TODO zaszyfruj
        addProperty(USER_PASSWORD_PROP, userObj.getPassword(), props);
        addProperty(USER_TOKEN_PROP, userObj.getToken(), props);

        FileManager fileManager = new FileManager();
        fileManager.writeProperties(USER_FILE, props, activity);


    }

    private void addProperty(String key, String value, Properties properties) {
        if(StringUtils.isNotEmpty(value)) {
            properties.setProperty(key, value);
        }
    }

}
