package com.example.apkafinal.metody;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.apkafinal.interfejsy.UpdateCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateMethod extends AsyncTask<String , String ,String> {
    private Context mContext;

    private UpdateCompleted mCallback;
    long roznica = 0;
    long przed = 0;
    long po = 0;
    double avg = 0;

    public UpdateMethod(Context context){
        this.mContext = context;
        this.mCallback = (UpdateCompleted) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String status = null;
        try {
            przed = System.nanoTime();
            URL url = new URL(strings[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();

            for (int i = 1; i < strings.length; i = i+2) {
                jsonParam.put(strings[i], strings[i+1]);
                if (strings[i+1].equals("true")) {
                    jsonParam.put(strings[i], true);
                }
            }
            Log.e("JSON: ", String.valueOf(jsonParam));

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            status = String.valueOf(urlConnection.getResponseCode());
            Log.e("STATUS", status);
            Log.e("MSG" , urlConnection.getResponseMessage());

            urlConnection.disconnect();
            po = System.nanoTime();
            roznica += po - przed;
            avg = roznica / 1000000.0;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(avg);
    }
    protected void onPostExecute(String results) {
        roznica = po - przed;
        mCallback.onUpdateComplete(results);
    }
}