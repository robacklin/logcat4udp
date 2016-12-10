package com.example.racklin.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonStart = (Button) findViewById(R.id.button);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick Start");
                startService(view);
            }
        });
        final Button buttonStop = (Button) findViewById(R.id.button2);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick Stop");
                stopService(view);
            }
        });
    }

    public void startService(View view) {
        startService(new Intent(this, MyService.class)
                .setData(Uri.parse("http://127.0.0.1")));
    }
    public void stopService(View view) {
        stopService(new Intent(this, MyService.class)
                .setData(Uri.parse("http://127.0.0.1")));
    }

}
