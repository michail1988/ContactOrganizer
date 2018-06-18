package com.mlab.contactorganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.io.StoreManager;
import com.mlab.contactorganizer.obj.UserObj;
import com.mlab.contactorganizer.connect.ServerConnector;
import com.mlab.contactorganizer.connect.SslManager;
import com.mlab.contactorganizer.utils.NavigationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * https://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
 */
public class SignupActivity extends AppCompatActivity {

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;

    private ServerConnector serverConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = (EditText)findViewById(R.id.input_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);

        serverConnector = new ServerConnector(this.getCacheDir());
    }

    public void login(View view) {
        navigateLogin();
    }

    private void navigateLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void signup(View view) {
        signup();
    }

    public void signup() {
        //Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        SslManager sslManager = new SslManager();
        sslManager.trustEveryone();

        StringRequest loginRequest = new StringRequest(Request.Method.POST, ServerConnector.CREATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("SignupActivity resp:", response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.get("status") != null) {
                        if (obj.get("status").equals(1)) {
                            UserObj userObj = createUserObj(obj);
                            //TODO rzuc i obsluz wyjatek

                            onSignupSuccess(userObj);
                        }

                        if (obj.get("status").equals(2)) {
                            onSignupFailed();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    onSignupFailed();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                onSignupFailed();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", name);
                MyData.put("email", email);
                MyData.put("password", password);
                return MyData;
            }
        };

        serverConnector.getRequestQueue().add(loginRequest);
        /*
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
                */
    }

    ////{"status":1,"message":"User created","error_code":0,"details":{"user_id":2}}
    private UserObj createUserObj(JSONObject obj) {
        UserObj userObj = new UserObj();

        userObj.setName(_nameText.getText().toString());
        userObj.setPassword(_passwordText.getText().toString());
        userObj.setEmail(_emailText.getText().toString());

        try {
            JSONObject detailsObj = obj.getJSONObject("details");

            if (detailsObj != null) {
                userObj.setId(detailsObj.getString("user_id"));
                userObj.setToken(detailsObj.getString("token"));
            } else {
                //TODO oblsuga bledu
            }

            userObj.setToken(obj.getString("token"));
        } catch (JSONException e) {
            //TODO obsluga bledu
            Log.e("SignupActivity", e.getMessage(), e);
        }
        return userObj;
    }


    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void onSignupSuccess(UserObj userObj) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();


        StoreManager storeManager = new StoreManager();
        storeManager.saveUserData(userObj, this);

        NavigationUtil.navigateSms(this);
    }

}
