package com.mlab.contactorganizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.connect.SslManager;
import com.mlab.contactorganizer.io.StoreManager;
import com.mlab.contactorganizer.obj.UserObj;
import com.mlab.contactorganizer.connect.ServerConnector;
import com.mlab.contactorganizer.services.SendingService;
import com.mlab.contactorganizer.utils.NavigationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//TODO internacjonalizacja https://www.oneskyapp.com/academy/android-app-localization-tutorial-basics/
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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            Log.d("MainActivity", "Sprawdzam logout:" + bundle.getBoolean("logout"));

            if (!bundle.getBoolean("logout")) {
                checkUserData();
            } //TODO delete user data?

        } else {
            Log.d("MainActivity", "Bundle null");
            checkUserData();
        }


    }

    private void checkUserData() {
        StoreManager storeManager = new StoreManager();
        UserObj userObj = storeManager.getUserData(this);


        if(userObj != null) {
            NavigationUtil.navigateMainNavigation(this, userObj);
        }
    }

    public void login(View view) {

        final String email = username.getText().toString();
        final String pass = password.getText().toString();

        SslManager sslManager = new SslManager();
        sslManager.trustEveryone();

        String uri = String.format(ServerConnector.LOGIN_ADDRESS + "?email=%1$s&password=%2$s",
                email,
                pass);

        StringRequest loginRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Sending service resp:", response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.get("status") != null) {
                        if (obj.get("status").equals("ok")) {


                            UserObj userObj = createUserObj(obj, email, pass);
                            //TODO rzuc i obsluz wyjatek

                            onLoginSuccess(userObj);
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
                username.setError("Nieprawidłowy login i/lub hasło");

                //TODO
                //navigateSms();
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


        //navigateMainNavigation();
    }

    public void onLoginSuccess(UserObj userObj) {

        StoreManager storeManager = new StoreManager();
        storeManager.saveUserData(userObj, this);

        //TODO
        requestPermissions();
        NavigationUtil.navigateMainNavigation(this, userObj);
    }

    public void register(View view) {
        NavigationUtil.navigateSignup(this);
    }

    public void forgotPassword(View view) {
        NavigationUtil.navigateForgotPassword(this);
    }

    private void navigateSms() {
        Intent intent = new Intent(this, SmsActivity.class);
        startActivity(intent);

    }





    private void requestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }

        //https://stackoverflow.com/questions/8442079/keep-the-screen-awake-throughout-my-activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /** Android 6.0 Marshmallow
         * // prevent from doze state
         Intent intent = new Intent();
         String packageName = this.getPackageName();
         PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
         //if (pm.isIgnoringBatteryOptimizations(packageName))
         intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
         // else {
         //intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
         //intent.setData(Uri.parse("package:" + packageName));
         // }
         this.startActivity(intent);
         */

    }

    public static int REQUEST_READ_PHONE_STATE=12;

    //@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Main Activity", "Uzyskano pozwolenie");

                    //Intent service = new Intent(this, SendingService.class);
                    //startService(service);
                }
                break;

            default:
                break;
        }
    }

    ////{"status":1,"message":"User created","error_code":0,"details":{"user_id":2}}
    private UserObj createUserObj(JSONObject obj, String email, String password) {
        UserObj userObj = new UserObj();

        try {
            JSONObject detailsObj = obj.getJSONObject("data");

            if (detailsObj != null) {

                //JSONObject userJSONObj = detailsObj.getJSONObject("user");

                //TODO walidacja jesli nie ma
                //userObj.setId(userJSONObj.getString("id"));
                userObj.setName(email);
                userObj.setEmail(email);
                userObj.setPassword(password);
                userObj.setToken(detailsObj.getString("api_token"));
            } else {
                //TODO oblsuga bledu
            }
        } catch (JSONException e) {
            //TODO obsluga bledu
            Log.e("SignupActivity", e.getMessage(), e);
        }
        return userObj;
    }
    }
