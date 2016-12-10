package com.example.racklin.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MyService extends Service {
    private DatagramSocket mSocket = null;
    private static String TAG = "MyService";
    public boolean bThread = true;

    public MyService() {
        try {
            mSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand Start");
        bThread = true;
        new Thread(new Runnable() {
            public void run() {
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("logcat");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String thisLine;
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                Log.d(TAG, "BufferedReader Start");
                try {
                    while ((thisLine = bufferedReader.readLine()) != null) {
                        onSendData(thisLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Waits for the command to finish.
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d(TAG, "onStartCommand done");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        bThread = false;
        super.onDestroy();
    }

    public void onSendData(String logData) {
        if (mSocket != null) {
            DatagramPacket packet = null;
            try {
                packet = new DatagramPacket(logData.getBytes(), logData.length(),
                        InetAddress.getByName("10.0.2.2"), 514);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            try {
                mSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
