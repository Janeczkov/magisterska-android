package com.example.apkafinal.metody;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.apkafinal.interfejsy.UploadCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadMethod extends AsyncTask<TaskParamsHelper, String ,String> {
    private Context mContext;

    private UploadCompleted mCallback;
    private long roznica = 0;
    long przed = 0;
    long po = 0;
    double avg = 0;
    String id = null;
    JSONObject returnobj = new JSONObject();

    public UploadMethod(Context context){
        this.mContext = context;
        this.mCallback = (UploadCompleted) context;
    }

    @Override
    protected String doInBackground(TaskParamsHelper... params) {
        try {
            przed = System.nanoTime();
            URL url = new URL(params[0].ip);
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            byte[] content = params[0].content;
            String urisegment = params[0].filename;

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            DataOutputStream outputStream;
            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"reference\""+ lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes("my_refrence_text");
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            outputStream.writeBytes("Content-Disposition: form-data; name=\"content\";filename=\"" + urisegment +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.write(content);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"autor\"" + lineEnd + lineEnd + params[0].username);
            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);




            int serverResponseCode = urlConnection.getResponseCode();
            po = System.nanoTime();
            String result = null;
            if (serverResponseCode == 200) {
                StringBuilder s_buffer = new StringBuilder();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    s_buffer.append(inputLine);
                }
                result = s_buffer.toString();
                is.close();
                br.close();
            }
            //fileInputStream.close();

            outputStream.flush();
            outputStream.close();
            if (result != null) {
                Log.d("result_for upload", result);
            }

            Log.e("STATUS", String.valueOf(urlConnection.getResponseCode()));
            Log.e("MSG" , urlConnection.getResponseMessage());
            urlConnection.disconnect();
            roznica += po - przed;
            avg = roznica / 1000000.0;
            //Log.e("roznica", String.valueOf(avg));
            Log.e("czas wysylania", String.valueOf(avg));

            assert result != null;
            JSONObject resultobj = new JSONObject(result);
            id = resultobj.getString("id");
            String filename = resultobj.getString("fileName");
            returnobj.put("avg", avg);
            returnobj.put("id", id);
            returnobj.put("fileName", filename);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return String.valueOf(returnobj);
    }
    protected void onPostExecute(String results) {
        roznica = po - przed;
        mCallback.onUploadComplete(results);
    }
}
