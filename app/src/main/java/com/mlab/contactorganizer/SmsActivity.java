package com.mlab.contactorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mlab.contactorganizer.connect.ServerConnector;

public class SmsActivity extends AppCompatActivity {

    EditText smsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        smsText = (EditText)findViewById(R.id.smsText);
    }


    public void send(View view) {
        if(smsText.getText().length() != 0){

            //TODO przekaz username w parametrze
            ServerConnector serverConnector = new ServerConnector(this.getCacheDir());
            serverConnector.sendSms("username", smsText.getText().toString());
            //sendSms();
        }else{
            smsText.setError("Wpisz tekst smsa.");
        }
    }

    public void navigateList(View view) {
        navigateSmsList();
    }

    private void navigateSmsList() {
        Intent intent = new Intent(this, SmsListActivity.class);
        startActivity(intent);

    }
}
