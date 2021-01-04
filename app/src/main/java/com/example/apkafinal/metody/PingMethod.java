package com.example.apkafinal.metody;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.apkafinal.interfejsy.GetCompleted;
import com.example.apkafinal.interfejsy.PingCompleted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class PingMethod extends AsyncTask<String , Void ,String> {
    BufferedReader reader = null;
    private Context mContext;

    private PingCompleted mCallback;
    long roznica = 0;
    long przed = 0;
    long po = 0;
    float avg = 0;

    long roznica2 = 0;
    long przed2 = 0;
    long po2 = 0;
    long mid2 = 0;
    float roznicamid2 = 0;
    float avg2 = 0;
    float avgmid2 = 0;

    public PingMethod(Context context){
        this.mContext = context;
        this.mCallback = (PingCompleted) context;

    }

    @Override
    protected String doInBackground(String... strings) {
        float i;
        for (i=1; i<=10; i++) {
            przed = System.nanoTime();
            try {

                URL url = new URL(strings[0]);
                przed2 = System.nanoTime();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                mid2 = System.nanoTime();
                urlConnection.setConnectTimeout(1000);
                urlConnection.setReadTimeout(1000);
                InputStream inputStream = urlConnection.getInputStream();
                po2 = System.nanoTime();


                inputStream.close();
                urlConnection.disconnect();
                po = System.nanoTime();
                roznica += po - przed;
                roznicamid2 += mid2 - przed2;
                roznica2 += po2 - mid2;

            } catch (SocketTimeoutException e) {
                return "timeout";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.i("Ping: ", String.valueOf(timeDifference));
        avg = roznica / i / 1000000;
        avgmid2 = roznicamid2 / i / 1000000;
        avg2 = roznica2 / i / 1000000;
        Log.e("czasy", avgmid2 + " / " + avg2);

        return String.valueOf(avg);
    }


    protected void onPostExecute(String results) {;
        roznica = po - przed;
        mCallback.onPingComplete(results);
    }



}