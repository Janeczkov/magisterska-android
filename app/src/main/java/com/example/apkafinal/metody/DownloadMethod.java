package com.example.apkafinal.metody;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.apkafinal.interfejsy.DownloadCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadMethod extends AsyncTask<String , Void ,String> {
    String inputStream;
    BufferedReader reader = null;
    private Context mContext;
    long roznica = 0;
    long przed = 0;
    long po = 0;
    float avg = 0;

    ProgressDialog mProgress;
    private DownloadCompleted mCallback;

    public DownloadMethod(Context context){
        this.mContext = context;
        this.mCallback = (DownloadCompleted) context;

    }

    @Override
    protected String doInBackground(String... strings) {
        //String content="";
        String result = "";
        JSONObject obj = new JSONObject();
        try {
            przed = System.nanoTime();
            URL url = new URL(strings[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            //BufferedReader inputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            InputStream in = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[50];
            int current = 0;

            while((current = bis.read(data,0,data.length)) != -1){
                buffer.write(data,0,current);
            }

            String header = urlConnection.getHeaderField("Content-Disposition");
            String[] headerSplit = header.split("filename=");
            String filename = headerSplit[1].replace("filename=", "").replace("\"", "").trim();

            Log.e("filename download", filename);

            InputStream inputStream = urlConnection.getInputStream();

            String contentfolder = "Pliki";

            String path = mContext.getExternalFilesDir(null).getAbsolutePath();
            File root = new File( path, contentfolder);

            if (!root.exists()) {
                root.mkdir();
            }

            File file = new File(root, filename);

            boolean filedone = false;
            if (!file.exists()) {
                filedone = file.createNewFile();
            }
            else {
                Log.e("usuniety", String.valueOf(file.delete()));
                filedone = file.createNewFile();
                result = "Ten plik już został pobrany";
            }
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(buffer.toByteArray());

            if (filedone) {
                obj.put("done", "true");
                result = "true";
            }
            else {
                obj.put("done", "exists");
                result = "false";
            }
            stream.flush();
            stream.close();

            obj.put("folder", contentfolder);
            obj.put("filename", filename);
            obj.put("fileid", strings[1]);

            urlConnection.disconnect();
            po = System.nanoTime();
            roznica += po - przed;
            avg = roznica / 1000000;

            obj.put("czaswykonywania", avg);


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }



        return String.valueOf(obj);
    }


    protected void onPostExecute(String results) {
        //mProgress.dismiss();
        //This is where you return data back to caller
        mCallback.onDownloadComplete(results);
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