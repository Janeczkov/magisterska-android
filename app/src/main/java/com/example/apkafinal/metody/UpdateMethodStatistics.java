package com.example.apkafinal.metody;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class UpdateMethodStatistics extends AsyncTask<String, String, String> {
    long roznica = 0;
    long przed = 0;
    long po = 0;
    float avg = 0;

    public UpdateMethodStatistics(Context context) {

    }

    @Override
    protected String doInBackground(String... strings) {
        float i;
        for (i = 1; i <= 10; i++) {
            przed = System.nanoTime();

            try {
                URL url = new URL(strings[0] + strings[1]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(1000);
                urlConnection.setReadTimeout(1000);
                po = System.nanoTime();
                InputStream inputStream = urlConnection.getInputStream();


            /*reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            finalJson = buffer.toString();
            reader.close();
*/
                inputStream.close();
                urlConnection.disconnect();
                roznica += po - przed;

            } catch (SocketTimeoutException e) {
                return "timeout";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log.i("Ping: ", String.valueOf(timeDifference));
        avg = roznica / i / 1000000;

        //Log.i("Suma w ms", String.valueOf(roznica/1000000));
        //Log.e("Avg", String.valueOf(avg));

        try {
            URL url = new URL(strings[0] + strings[2]);

            HttpURLConnection urlConnection2 = (HttpURLConnection) url.openConnection();
            urlConnection2.setConnectTimeout(1000);
            urlConnection2.setReadTimeout(1000);
            urlConnection2.setRequestMethod("PUT");
            urlConnection2.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection2.setRequestProperty("Accept", "application/json");
            urlConnection2.setDoOutput(true);


            JSONObject jsonParam = new JSONObject();
            jsonParam.toString();
            if (strings[3].equals("download")) {
                jsonParam.put("temp_download_time", strings[5]);
            } else if (strings[3].equals("upload")) {
                jsonParam.put("temp_upload_time", strings[5]);
            }
            jsonParam.put("temp_platform", strings[4]);
            jsonParam.put("temp_avg_latency", avg);
            jsonParam.put("temp_filename", strings[6]);
            jsonParam.put("temp_username", strings[7]);
            DataOutputStream os = new DataOutputStream(urlConnection2.getOutputStream());
            os.writeBytes(jsonParam.toString());
            os.flush();
            os.close();
            //Log.e("JSON: ", String.valueOf(jsonParam));
            Log.i("STATUS", String.valueOf(urlConnection2.getResponseCode()));
            Log.i("MSG", urlConnection2.getResponseMessage());
            Log.i("czas", strings[5]);
            Log.i("numer pobierania", String.valueOf(strings[8]));

            urlConnection2.disconnect();


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}