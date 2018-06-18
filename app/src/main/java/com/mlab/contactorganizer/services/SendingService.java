package com.mlab.contactorganizer.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mlab.contactorganizer.connect.ServerConnector;
import com.mlab.contactorganizer.connect.SslManager;
import com.mlab.contactorganizer.obj.SmsObj;
import com.mlab.contactorganizer.obj.UserObj;
import com.mlab.contactorganizer.utils.SmsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * https://stackoverflow.com/questions/34573109/how-to-make-an-android-app-to-always-run-in-background
 *
 * https://stackoverflow.com/questions/15758980/android-service-needs-to-run-always-never-pause-or-stop
 */
public class SendingService extends Service {

    public static int SLEEPTIME = 1000 * 60 * 15;
    public static String TEST_PHONE_NUMBER = "512744693";

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private PowerManager.WakeLock wakeLock;
    private Timer timer;

    private ServerConnector serverConnector;

    private UserObj userObj;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serverConnector = new ServerConnector(this.getCacheDir());

        userObj = (UserObj) intent.getSerializableExtra("userObj");

        start();
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {

        //TODO Michal za kazdym razem potrzebne?
        requestPermissions();

        //https://developer.android.com/training/scheduling/wakelock.html
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                requestSms();
            }
        }, 1000, SLEEPTIME);
            /**
             * @Override
            public void run() {
            // If you want to modify a view in your Activity
            this.runOnUiThread(new Runnable() {
            public void run(){
            //status.setText(formatDate(new Date()) + "\n");

            requestSms();
            }});
            }
            }, 1000, SLEEPTIME);
             */


    }

    public void requestSms() {

        SslManager sslManager = new SslManager();
        sslManager.trustEveryone();

        String uri = String.format(ServerConnector.TO_SEND + "?api_token=%1$s", userObj.getToken());

        StringRequest smsRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Sending service resp:", response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.get("status") != null) {
                        if (obj.get("status").equals("ok")) {

                          //  JSONObject detailsObj = obj.getJSONObject("data");

                        //    if (detailsObj != null) {

                                //SmsUtil.sendSMS("512744693", "PROOOOBA");


                        //        SmsUtil.sendSMS("512744693", "PROOOOBA  ");

                                JSONArray smsArray = obj.getJSONArray("data");


                                //SmsUtil.sendSMS("512744693", "PROOOOBA 2" + detailsObj.getJSONObject("sms").getString("body"));

                                if (smsArray != null) {
                                    for (int i = 0; i < smsArray.length(); i++) {
                                        JSONObject smsJSON = smsArray.getJSONObject(i);

                                        Log.d("SMS array ", i + " " + smsJSON.toString());

                                        String message = smsJSON.getString("body");
                                        String receivers = smsJSON.getString("to");
                                        int id = smsJSON.getInt("id");
                                        //TODO niepotrzebne
                                        String fkUser = "1";

                                        Log.d("Odczytano id", String.valueOf(id));
                                        SmsObj sms = new SmsObj(message, receivers, fkUser, id);

                                        if (sms.getReceivers() != null) {
                                            for (String receiverNumber : sms.getReceivers()) {
                                                SmsUtil.sendSMS(receiverNumber, sms.getMessage());
                                                newStatusLine("Wyslano do " + receiverNumber);
                                                Log.d("SendingService", "Wyslano do " + receiverNumber);
                                            }
                                        }


                                        Log.d("SendingService", "Mark as sent id: " + sms.getId());
                                        sendSmsSentMessage(sms);
                                    }
                                }
                            }
                        }
                    //}
                } catch (JSONException e) {
                        e.printStackTrace();
                        //onSignupFailed();
                    }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("SendingService", error.getMessage(), error);
                //username.setError("Nieprawidłowy login i/lub hasło");

                //TODO
                //navigateSms();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("api_token", userObj.getToken());
                return MyData;
            }
        };

        serverConnector.getRequestQueue().add(smsRequest);
    }

    private void sendSmsSentMessage(final SmsObj sms) {


        JSONObject obj = new JSONObject();
        try {
            obj.put("api_token", userObj.getToken());
            JSONArray smsIds = new JSONArray();

            smsIds.put(sms.getId());
               /* for(int i=0; i < list.size(); i++)
                {
                    car.put(list.get(i));
                    // create array and add items into that
                }
                params.put("car",car.toString());
                */


            obj.put("id" , smsIds);


            Log.d("POST Params", obj.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,ServerConnector.SMS_SENT,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("onresponse=" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("onerror=" + error);
                    }
                });
        serverConnector.getRequestQueue().add(jsObjRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("\"" + "api_token" + "\"", "\"" + userObj.getToken() + "\"");

        JSONArray smsIds = new JSONArray();

        smsIds.put(sms.getId());
               /* for(int i=0; i < list.size(); i++)
                {
                    car.put(list.get(i));
                    // create array and add items into that
                }
                params.put("car",car.toString());
                */


        params.put("\"" +"id" + "\"", smsIds.toString());

        //Log.d("Markuje taki sms", smsIds.toString());
        //Log.d("POST Params", params.toString());

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, ServerConnector.SMS_SENT, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                //        try {
                            //Process os success response
                  //      } catch (JSONException e) {
                      //      e.printStackTrace();
                    //    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                VolleyLog.e("Error: ", error);
            }
        });

        StringRequest smsSentRequest = new StringRequest(Request.Method.POST, ServerConnector.SMS_SENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Sms sent resp:", response);

               // try {
                    //JSONObject obj = new JSONObject(response);
/*
                    if (obj.get("status") != null) {
                        if (obj.get("status").equals(1)) {
                            //UserObj userObj = createUserObj(obj);
                            //TODO rzuc i obsluz wyjatek

                           // onSignupSuccess(userObj);
                        }

                        if (obj.get("status").equals(2)) {
                            onSmsSentFailed();
                        }
                    }
*/
               // } catch (JSONException e) {
                 //   e.printStackTrace();
                   // onSmsSentFailed();
               // }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                onSmsSentFailed();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_token", userObj.getToken());

                JSONArray smsIds = new JSONArray();

                smsIds.put(sms.getId());
               /* for(int i=0; i < list.size(); i++)
                {
                    car.put(list.get(i));
                    // create array and add items into that
                }
                params.put("car",car.toString());
                */


                params.put("id", smsIds.toString());

                Log.d("Markuje taki sms", smsIds.toString());
                Log.d("POST Params", params.toString());
                return params;
            }
        };

        //serverConnector.getRequestQueue().add(smsSentRequest);
    }

    private void onSmsSentFailed() {
        Log.d("Sending service", "sms sent failed!");
    }

    public void stop(View view) {
        if (timer != null) {
            timer.cancel();
        }

        if (wakeLock != null) {
            wakeLock.release();
        }

        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void trySms(View view) {
        requestPermissions();
        SmsUtil.sendSMS(TEST_PHONE_NUMBER, "Proba wyslania sms");
    }

    private void newStatusLine(String statusLine) {
        //status.setText(status.getText() + "\n" + statusLine);
    }

    //https://stackoverflow.com/questions/32742327/neither-user-10102-nor-current-process-has-android-permission-read-phone-state
    private void requestPermissions() {
   /*     int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

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
                }
                break;

            default:
                break;
        }
    }

    private String formatDate(Date date) {
        if (date != null) {
            return dateFormatter.format(date);
        }

        return "";
    }
}
