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

public class statystykiPliku extends AppCompatActivity implements PingCompleted, GetCompleted {


    private TextView idpliku;
    private TextView nazwapliku;
    private TextView autor;
    private TextView rozmiarbw;
    private TextView rozmiarmbw;
    private TextView pliki_j;
    private TextView pliki_c;
    private TextView czas_j;
    private TextView czas_c;
    private TextView czasraw_j;
    private TextView czasraw_c;
    private TextView czas_na_mb_j;
    private TextView czas_na_mb_c;
    private TextView czasraw_na_mb_j;
    private TextView czasraw_na_mb_c;
    private TextView ping_j;
    private TextView ping_c;

    String file_name = "";
    String author = "";
    long file_sizeB;
    double file_sizeMB;
    int number_of_downloads_java = 0;
    int number_of_downloads_csharp = 0;
    double total_time_downloaded_java = 0;
    double total_time_downloaded_csharp = 0;
    double average_time_downloaded_java = 0;
    double average_time_downloaded_csharp = 0;
    double raw_average_time_downloaded_java = 0;
    double raw_average_time_downloaded_csharp = 0;
    double time_per_megabyte_download_java = 0;
    double time_per_megabyte_download_csharp = 0;
    double raw_time_per_megabyte_download_java = 0;
    double raw_time_per_megabyte_download_csharp = 0;
    double total_latency_java = 0;
    double total_latency_csharp = 0;
    double average_latency_java = 0;
    double average_latency_csharp = 0;

    String fileid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statystyki_pliku);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Statystyki pliku");
        actionbar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final String ip = getString(R.string.ip);


        final Button zapiszb = findViewById(R.id.zapiszb);
        fileid = intent.getStringExtra("fileid");


        new GetMethod(statystykiPliku.this).execute(ip + "/file/" + fileid);

        zapiszb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss", Locale.GERMANY);
                String filenametxt = file_name + " " + format.format(now) + ".txt";
                String filenamecsv = file_name + " " + format.format(now) + ".csv";
                String result = zapiszdopliku(filenametxt, "txt");
                result = result + ", " + zapiszdopliku(filenamecsv, "csv");

                Toast.makeText(statystykiPliku.this, "Rezultat: " + result, Toast.LENGTH_LONG).show();


            }


        });


    }


    public String zapiszdopliku(String nazwapliku, String rozszerzenie) {
        String result = "Wystąpił błąd";
        String folder = "Statystyki plików";
        String path = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        File root = new File(path, folder);

        if (!root.exists()) {
            root.mkdir();
        }

        File file = new File(root, nazwapliku);

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
            bw.write(",Java,C#,,Id pliku," + idpliku.getText()); bw.newLine();
            bw.write("Liczba pobrań," + pliki_j.getText() + "," + pliki_c.getText() + ",,Nazwa pliku," + nazwapliku); bw.newLine();
            bw.write("Średni czas pobierania (ms)," + czas_j.getText() + "," + czas_c.getText() + ",,Autor pliku," + autor.getText()); bw.newLine();
            bw.write("Średni aktualny czas pobierania (ms)," + czasraw_j.getText() + "," + czasraw_c.getText() + ",,Rozmiar pliku (B)," + rozmiarbw.getText()); bw.newLine();
            bw.write("Średni czas dla 1 MB (ms)," + czas_na_mb_j.getText() + "," + czas_na_mb_c.getText() + ",,Rozmiar pliku (MB)," + rozmiarmbw.getText()); bw.newLine();
            bw.write("Średni aktualny czas dla 1 MB (ms)," + czasraw_na_mb_j.getText() + "," + czasraw_na_mb_c.getText()); bw.newLine();
            bw.write("Średnia wartość opóźnienia (ms)," + ping_j.getText() + "," + ping_c.getText()); bw.newLine();
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
        try {
            JSONObject resultobj = new JSONObject(result);

            file_name = resultobj.getString("fileName");
            author = resultobj.getString("author");
            file_sizeB = resultobj.getLong("file_sizeB");
            file_sizeMB = resultobj.getDouble("file_sizeMB");
            number_of_downloads_java = resultobj.getInt("number_of_downloads_java");
            number_of_downloads_csharp = resultobj.getInt("number_of_downloads_csharp");
            total_time_downloaded_java = resultobj.getDouble("total_time_downloaded_java");
            total_time_downloaded_csharp = resultobj.getDouble("total_time_downloaded_csharp");
            average_time_downloaded_java = resultobj.getDouble("average_time_downloaded_java");
            average_time_downloaded_csharp = resultobj.getDouble("average_time_downloaded_csharp");
            raw_average_time_downloaded_java = resultobj.getDouble("raw_average_time_downloaded_java");
            raw_average_time_downloaded_csharp = resultobj.getDouble("raw_average_time_downloaded_csharp");
            time_per_megabyte_download_java = resultobj.getDouble("time_per_megabyte_download_java");
            time_per_megabyte_download_csharp = resultobj.getDouble("time_per_megabyte_download_csharp");
            raw_time_per_megabyte_download_java = resultobj.getDouble("raw_time_per_megabyte_download_java");
            raw_time_per_megabyte_download_csharp = resultobj.getDouble("raw_time_per_megabyte_download_csharp");
            total_latency_java = resultobj.getDouble("total_latency_java");
            total_latency_csharp = resultobj.getDouble("total_latency_csharp");
            average_latency_java = resultobj.getDouble("average_latency_java");
            average_latency_csharp = resultobj.getDouble("average_latency_csharp");

            idpliku = findViewById(R.id.idpliku);
            nazwapliku = findViewById(R.id.nazwapliku);
            autor = findViewById(R.id.autor);
            rozmiarbw = findViewById(R.id.rozmiarbw);
            rozmiarmbw = findViewById(R.id.rozmiarmbw);
            pliki_j = findViewById(R.id.download_j);
            pliki_c = findViewById(R.id.download_c);
            czas_j = findViewById(R.id.czas_j);
            czas_c = findViewById(R.id.czas_c);
            czasraw_j = findViewById(R.id.czasraw_j);
            czasraw_c = findViewById(R.id.czasraw_c);
            czas_na_mb_j = findViewById(R.id.czas_na_mb_j);
            czas_na_mb_c = findViewById(R.id.czas_na_mb_c);
            czasraw_na_mb_j = findViewById(R.id.czasraw_na_mb_j);
            czasraw_na_mb_c = findViewById(R.id.czasraw_na_mb_c);
            ping_j = findViewById(R.id.ping_j);
            ping_c = findViewById(R.id.ping_c);
            idpliku.setText(fileid);
            nazwapliku.setText(file_name);
            autor.setText(author);
            rozmiarbw.setText(String.valueOf(file_sizeB));
            rozmiarmbw.setText(String.format(Locale.CANADA, "%.5f", file_sizeMB));
            pliki_j.setText(String.valueOf(number_of_downloads_java));
            pliki_c.setText(String.valueOf(number_of_downloads_csharp));
            czas_j.setText(String.format(Locale.CANADA, "%.2f", average_time_downloaded_java));
            czas_c.setText(String.format(Locale.CANADA, "%.2f", average_time_downloaded_csharp));
            czasraw_j.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_downloaded_java));
            czasraw_c.setText(String.format(Locale.CANADA, "%.2f", raw_average_time_downloaded_csharp));
            czas_na_mb_j.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_download_java));
            czas_na_mb_c.setText(String.format(Locale.CANADA, "%.2f", time_per_megabyte_download_csharp));
            czasraw_na_mb_j.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_download_java));
            czasraw_na_mb_c.setText(String.format(Locale.CANADA, "%.2f", raw_time_per_megabyte_download_csharp));
            ping_j.setText(String.format(Locale.CANADA, "%.2f", average_latency_java));
            ping_c.setText(String.format(Locale.CANADA, "%.2f", average_latency_csharp));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onDownloadComplete(final String result) {
        Toast.makeText(statystykiPliku.this, result, Toast.LENGTH_LONG).show();
    }*/
}