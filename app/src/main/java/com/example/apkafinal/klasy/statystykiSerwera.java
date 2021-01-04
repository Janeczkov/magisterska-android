package com.example.apkafinal.klasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apkafinal.R;
import com.example.apkafinal.interfejsy.GetCompleted;
import com.example.apkafinal.interfejsy.PingCompleted;
import com.example.apkafinal.metody.GetMethod;
import com.example.apkafinal.metody.PingMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class statystykiSerwera extends AppCompatActivity implements PingCompleted, GetCompleted {


    private TextView download_j;
    private TextView upload_j;
    private TextView mbpobrane_j;
    private TextView mbwyslane_j;
    private TextView rozmiarmbw;
    private TextView czaspobierania_j;
    private TextView czaswysylania_j;
    private TextView raw_czas_pobierania_j;
    private TextView raw_czas_wysylania_j;
    private TextView czas_na_mb_pobierania_j;
    private TextView czas_na_mb_wysylania_j;
    private TextView rawczas_na_mb_pobierania_j;
    private TextView rawczas_na_mb_wysylania_j;
    private TextView ping_pobierania_j;
    private TextView ping_wysylania_j;
    private TextView download_c;
    private TextView upload_c;
    private TextView mbpobrane_c;
    private TextView mbwyslane_c;
    private TextView czaspobierania_c;
    private TextView czaswysylania_c;
    private TextView raw_czas_pobierania_c;
    private TextView raw_czas_wysylania_c;
    private TextView czas_na_mb_pobierania_c;
    private TextView czas_na_mb_wysylania_c;
    private TextView rawczas_na_mb_pobierania_c;
    private TextView rawczas_na_mb_wysylania_c;
    private TextView ping_pobierania_c;
    private TextView ping_wysylania_c;

    private String platform;
    private int total_files_uploaded;
    private int total_files_downloaded;
    private long total_B_size_of_files_uploaded;
    private double total_MB_size_of_files_uploaded;
    private long total_B_size_of_files_downloaded;
    private double total_MB_size_of_files_downloaded;
    private double total_time_uploaded;
    private double total_time_downloaded;
    private double average_time_uploaded;
    private double average_time_downloaded;
    private double raw_average_time_uploaded;
    private double raw_average_time_downloaded;
    private double time_per_megabyte_upload;
    private double time_per_megabyte_download;
    private double raw_time_per_megabyte_upload;
    private double raw_time_per_megabyte_download;
    private double total_latency_upload;
    private double average_latency_upload;
    private double total_latency_download;
    private double average_latency_download;

    String fileid = "";
    String rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statystyki_serwera);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Statystyki serwera");
        actionbar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final String ip = getString(R.string.ip);


        final Button zapiszb = findViewById(R.id.zapiszb);



        new GetMethod(statystykiSerwera.this).execute(ip + "/server/stats/");

        zapiszb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss", Locale.GERMANY);
                String filename = "Serwer";
                String filenametxt = filename + " " + format.format(now) + ".txt";
                String filenamecsv = filename + " " + format.format(now) + ".csv";
                String result = zapiszdopliku(filenametxt, "txt");
                result = result + ", " + zapiszdopliku(filenamecsv, "csv");

                Toast.makeText(statystykiSerwera.this, "Rezultat: " + result, Toast.LENGTH_LONG).show();


            }


        });


    }

    public String zapiszdopliku(String nazwapliku, String rozszerzenie) {
        String result = "Wystąpił błąd";
        String folder = "Statystyki serwera";
        String path = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        File root = new File(path, folder);

        if (!root.exists()) {
            root.mkdir();
        }

        File file = new File(root, nazwapliku);

        //File filecsv = new File(root, filenamecsv);

        boolean filedone = false;
        if (!file.exists()) {
            try {
                filedone = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                filedone = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = "Plik stworzony ponownie";
        }
        if (filedone) {
            result = "stworzono plik";
        } else {
            result = "Wystąpił błąd";
        }


        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(",Java,C#"); bw.newLine();
                bw.write("Liczba pobrań," + download_j.getText() + "," + download_c.getText()); bw.newLine();
                bw.write("Liczba udostępnień," + upload_j.getText() + "," + upload_c.getText()); bw.newLine();
                bw.write("Rozmiar plików pobranych," + mbpobrane_j.getText() + "," + mbpobrane_c.getText()); bw.newLine();
                bw.write("Rozmiar plików wysłanych," + mbwyslane_j.getText() + "," + mbwyslane_c.getText()); bw.newLine();
                bw.write("Średni czas pobierania (ms)," + czaspobierania_j.getText() + "," + czaspobierania_c.getText()); bw.newLine();
                bw.write("Średni czas wysyłania (ms)," + czaswysylania_j.getText() + "," + czaswysylania_c.getText()); bw.newLine();
                bw.write("Aktualny średni czas pobierania (ms)," + raw_czas_pobierania_j.getText() + "," + raw_czas_pobierania_c.getText()); bw.newLine();
                bw.write("Aktualny średni czas wysyłania (ms)," + raw_czas_wysylania_j.getText() + "," + raw_czas_wysylania_c.getText()); bw.newLine();
                bw.write("Średni czas pobierania dla 1 MB (ms)," + czas_na_mb_pobierania_j.getText() + "," + czas_na_mb_pobierania_c.getText()); bw.newLine();
                bw.write("Średni czas wysyłania dla 1 MB (ms)," + czas_na_mb_wysylania_j.getText() + "," + czas_na_mb_wysylania_c.getText()); bw.newLine();
                bw.write("Aktualny średni czas pobierania dla 1 MB (ms)," + rawczas_na_mb_pobierania_j.getText() + "," + rawczas_na_mb_pobierania_c.getText()); bw.newLine();
                bw.write("Aktualny średni czas wysyłania dla 1 MB (ms)," + rawczas_na_mb_wysylania_j.getText() + "," + rawczas_na_mb_wysylania_c.getText()); bw.newLine();
                bw.write("Średnia wartość opóźnienia dla pobierania (ms)," + ping_pobierania_j.getText() + "," + ping_pobierania_c.getText()); bw.newLine();
                bw.write("Średnia wartość opóźnienia dla wysyłania (ms)," + ping_wysylania_j.getText() + "," + ping_wysylania_c.getText()); bw.newLine();
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result + " " + rozszerzenie;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent myIntent = new Intent(getApplicationContext(), logowanie.class);
        startActivityForResult(myIntent, 0);*/
        finish();
        return true;
    }

    @Override
    public void onPingComplete(final String result) {
        float avg = Float.parseFloat(result);
        Log.e("Sredni czas odpowiedzi", String.valueOf(avg));
    }

    @Override
    public void onGetComplete(final String result) {

        final String ip = getString(R.string.ip);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonobject = jsonArray.getJSONObject(i);
                platform = jsonobject.getString("platform");


                total_files_uploaded = jsonobject.getInt("total_files_uploaded");
                total_files_downloaded = jsonobject.getInt("total_files_downloaded");
                total_B_size_of_files_uploaded = jsonobject.getLong("total_B_size_of_files_uploaded");
                total_MB_size_of_files_uploaded = jsonobject.getDouble("total_MB_size_of_files_uploaded");
                total_B_size_of_files_downloaded = jsonobject.getLong("total_B_size_of_files_downloaded");
                total_MB_size_of_files_downloaded = jsonobject.getDouble("total_MB_size_of_files_downloaded");
                total_time_uploaded = jsonobject.getDouble("total_time_uploaded");
                total_time_downloaded = jsonobject.getDouble("total_time_downloaded");
                average_time_uploaded = jsonobject.getDouble("average_time_uploaded");
                average_time_downloaded = jsonobject.getDouble("average_time_downloaded");
                raw_average_time_uploaded = jsonobject.getDouble("raw_average_time_uploaded");
                raw_average_time_downloaded = jsonobject.getDouble("raw_average_time_downloaded");
                time_per_megabyte_upload = jsonobject.getDouble("time_per_megabyte_upload");
                time_per_megabyte_download = jsonobject.getDouble("time_per_megabyte_download");
                raw_time_per_megabyte_upload = jsonobject.getDouble("raw_time_per_megabyte_upload");
                raw_time_per_megabyte_download = jsonobject.getDouble("raw_time_per_megabyte_download");
                total_latency_upload = jsonobject.getDouble("total_latency_upload");
                average_latency_upload = jsonobject.getDouble("average_latency_upload");
                total_latency_download = jsonobject.getDouble("total_latency_download");
                average_latency_download = jsonobject.getDouble("average_latency_download");

                download_j = findViewById(R.id.download_j);
                upload_j = findViewById(R.id.upload_j);
                mbpobrane_j = findViewById(R.id.mbpobrane_j);
                mbwyslane_j = findViewById(R.id.mbwyslane_j);
                czaspobierania_j = findViewById(R.id.czaspobierania_j);
                czaswysylania_j = findViewById(R.id.czaswysylania_j);
                raw_czas_pobierania_j = findViewById(R.id.raw_czas_pobierania_j);
                raw_czas_wysylania_j = findViewById(R.id.raw_czas_wysylania_j);
                czas_na_mb_pobierania_j = findViewById(R.id.czas_na_mb_pobierania_j);
                czas_na_mb_wysylania_j = findViewById(R.id.czas_na_mb_wysylania_j);
                rawczas_na_mb_pobierania_j = findViewById(R.id.rawczas_na_mb_pobierania_j);
                rawczas_na_mb_wysylania_j = findViewById(R.id.rawczas_na_mb_wysylania_j);
                ping_pobierania_j = findViewById(R.id.ping_pobierania_j);
                ping_wysylania_j = findViewById(R.id.ping_wysylania_j);

                download_c = findViewById(R.id.download_c);
                upload_c = findViewById(R.id.upload_c);
                mbpobrane_c = findViewById(R.id.mbpobrane_c);
                mbwyslane_c = findViewById(R.id.mbwyslane_c);
                czaspobierania_c = findViewById(R.id.czaspobierania_c);
                czaswysylania_c = findViewById(R.id.czaswysylania_c);
                raw_czas_pobierania_c = findViewById(R.id.raw_czas_pobierania_c);
                raw_czas_wysylania_c = findViewById(R.id.raw_czas_wysylania_c);
                czas_na_mb_pobierania_c = findViewById(R.id.czas_na_mb_pobierania_c);
                czas_na_mb_wysylania_c = findViewById(R.id.czas_na_mb_wysylania_c);
                rawczas_na_mb_pobierania_c = findViewById(R.id.rawczas_na_mb_pobierania_c);
                rawczas_na_mb_wysylania_c = findViewById(R.id.rawczas_na_mb_wysylania_c);
                ping_pobierania_c = findViewById(R.id.ping_pobierania_c);
                ping_wysylania_c = findViewById(R.id.ping_wysylania_c);

                if (platform.equals("java")) {
                    download_j.setText(String.valueOf(total_files_downloaded));
                    upload_j.setText(String.valueOf(total_files_uploaded));
                    mbpobrane_j.setText(String.format(Locale.CANADA, "%.2f", total_MB_size_of_files_downloaded));
                    mbwyslane_j.setText(String.format(Locale.CANADA, "%.2f", total_MB_size_of_files_uploaded));
                    czaspobierania_j.setText(String.format(Locale.CANADA, "%.2f", average_time_downloaded));
                    czaswysylania_j.setText(String.format(Locale.CANADA, "%.2f", average_time_uploaded));
                    raw_czas_pobierania_j.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_downloaded));
                    raw_czas_wysylania_j.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_uploaded));
                    czas_na_mb_pobierania_j.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_download));
                    czas_na_mb_wysylania_j.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_upload));
                    rawczas_na_mb_pobierania_j.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_download));
                    rawczas_na_mb_wysylania_j.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_upload));
                    ping_pobierania_j.setText(String.format(Locale.CANADA, "%.2f", average_latency_download));
                    ping_wysylania_j.setText(String.format(Locale.CANADA, "%.2f", average_latency_upload));
                } else if (platform.equals("csharp")) {
                    download_c.setText(String.valueOf(total_files_downloaded));
                    upload_c.setText(String.valueOf(total_files_uploaded));
                    mbpobrane_c.setText(String.format(Locale.CANADA, "%.2f", total_MB_size_of_files_downloaded));
                    mbwyslane_c.setText(String.format(Locale.CANADA, "%.2f", total_MB_size_of_files_uploaded));
                    czaspobierania_c.setText(String.format(Locale.CANADA, "%.2f", average_time_downloaded));
                    czaswysylania_c.setText(String.format(Locale.CANADA, "%.2f", average_time_uploaded));
                    raw_czas_pobierania_c.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_downloaded));
                    raw_czas_wysylania_c.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_uploaded));
                    czas_na_mb_pobierania_c.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_download));
                    czas_na_mb_wysylania_c.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_upload));
                    rawczas_na_mb_pobierania_c.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_download));
                    rawczas_na_mb_wysylania_c.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_upload));
                    ping_pobierania_c.setText(String.format(Locale.CANADA, "%.2f", average_latency_download));
                    ping_wysylania_c.setText(String.format(Locale.CANADA, "%.2f", average_latency_upload));

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    }