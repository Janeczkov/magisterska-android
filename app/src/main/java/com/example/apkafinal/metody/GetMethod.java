package com.example.apkafinal.metody;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.apkafinal.interfejsy.GetCompleted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class GetMethod extends AsyncTask<String , Void ,String> {
    BufferedReader reader = null;
    private Context mContext;

    private GetCompleted mCallback;
    long roznica = 0;
    long przed = 0;
    long po = 0;

    public GetMethod(Context context){
        this.mContext = context;
        this.mCallback = (GetCompleted) context;

    }

    @Override
    protected String doInBackground(String... strings) {
        String finalJson="";

        Log.i("Before: ", String.valueOf(System.nanoTime()));
        przed = System.nanoTime();
        try {

            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(2000);
            urlConnection.setReadTimeout(2000);
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            finalJson = buffer.toString();

            inputStream.close();
            reader.close();
            urlConnection.disconnect();
            po = System.nanoTime();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            return "timeout";
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("JSON: ", finalJson);
        return finalJson;
    }


    protected void onPostExecute(String results) {
        //mProgress.dismiss();
        //This is where you return data back to caller
        roznica = po - przed;
        mCallback.onGetComplete(results);
    }


// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}