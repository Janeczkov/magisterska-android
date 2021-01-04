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

public class statystykiUzytkownika extends AppCompatActivity implements PingCompleted, GetCompleted {

    private TextView test;

    private TextView uzytkownikw;
    private TextView rangaw;
    private TextView jezykw;
    private TextView sredni_czas_pingw;
    private TextView pliki_up;
    private TextView pliki_down;
    private TextView bajty_up;
    private TextView bajty_down;
    private TextView megabajty_up;
    private TextView megabajty_down;
    private TextView sredni_czas_up;
    private TextView sredni_czas_down;
    private TextView sredni_czas_aktualny_up;
    private TextView sredni_czas_aktualny_down;
    private TextView czas_na_mb_up;
    private TextView czas_na_mb_down;
    private TextView aktualny_czas_na_mb_up;
    private TextView aktualny_czas_na_mb_down;

    int number_of_files_uploaded = 0;
    int number_of_files_downloaded = 0;
    String app_language = "";
    double total_time_of_uploading = 0;
    double total_time_of_downloading = 0;
    long bytes_uploaded = 0;
    long bytes_downloaded = 0;
    double megabytes_uploaded = 0;
    double megabytes_downloaded = 0;
    double average_time_of_uploading = 0;
    double average_time_of_downloading = 0;
    double raw_average_time_of_uploading = 0;
    double raw_average_time_of_downloading = 0;
    double time_per_megabyte_upload = 0;
    double raw_time_per_megabyte_upload = 0;
    double time_per_megabyte_download = 0;
    double raw_time_per_megabyte_download = 0;
    double total_latency = 0;
    double average_latency = 0;

    String username;
    String rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statystyki_uzytkownika);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Statystyki użytkownika");
        actionbar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final String ip = getString(R.string.ip);


        final Button zapiszb = findViewById(R.id.zapiszb);
        username=intent.getStringExtra("username");
        rank=intent.getStringExtra("rank");
        actionbar.setTitle("Statystyki użytkownika");

        new GetMethod(statystykiUzytkownika.this).execute(ip + "/accounts/username/" + username);
        zapiszb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss", Locale.GERMANY);
                String filenametxt = username + " " + format.format(now) + ".txt";
                String filenamecsv = username + " " + format.format(now) + ".csv";
                String result = zapiszdopliku(filenametxt, "txt");
                result = result + ", " + zapiszdopliku(filenamecsv, "csv");

                Toast.makeText(statystykiUzytkownika.this, "Rezultat: " + result, Toast.LENGTH_LONG).show();


            }


        });

    }

    public String zapiszdopliku(String nazwapliku, String rozszerzenie) {
        String result = "Wystąpił błąd";
        String folder = "Statystyki użytkownika";
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
            bw.write("," + "Wysyłanie" + "," + "Pobieranie" + ",,Nazwa użytkownika," + username); bw.newLine();
            bw.write("Liczba plików," + pliki_up.getText() + "," + pliki_down.getText() + ",,Język aplikacji," + app_language); bw.newLine();
            bw.write("Ilość danych (B)," + bajty_up.getText() + "," + bajty_down.getText() + ",,Średnia wartość opóźnienia," + sredni_czas_pingw.getText()); bw.newLine();
            bw.write("Ilość danych (MB)," + megabajty_up.getText() + "," + megabajty_down.getText()); bw.newLine();
            bw.write("Średni czas (ms)," + sredni_czas_up.getText() + "," + sredni_czas_down.getText()); bw.newLine();
            bw.write("Średni czas aktualny (ms)," + sredni_czas_aktualny_up.getText() + "," + sredni_czas_aktualny_down.getText()); bw.newLine();
            bw.write("Średni czas dla 1 MB (ms)," + czas_na_mb_up.getText() + "," + czas_na_mb_down.getText()); bw.newLine();
            bw.write("Aktualny średni czas dla 1 MB (ms)," + aktualny_czas_na_mb_up.getText() + "," + aktualny_czas_na_mb_down.getText()); bw.newLine();

            bw.close();
            //result = ""


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result + " " + rozszerzenie;
    }

    public boolean onOptionsItemSelected(MenuItem item){
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
        double avg = 0;
        int id = 0;

        final String ip = getString(R.string.ip);
        try {
            JSONObject resultobj = new JSONObject(result);

            number_of_files_uploaded = resultobj.getInt("number_of_files_uploaded");
            number_of_files_downloaded = resultobj.getInt("number_of_files_downloaded");
            app_language = resultobj.getString("app_language");
            bytes_uploaded = resultobj.getLong("bytes_uploaded");
            bytes_downloaded = resultobj.getLong("bytes_downloaded");
            megabytes_uploaded = resultobj.getDouble("megabytes_uploaded");
            megabytes_downloaded = resultobj.getDouble("megabytes_downloaded");
            average_time_of_uploading = resultobj.getDouble("average_time_of_uploading");
            average_time_of_downloading = resultobj.getDouble("average_time_of_downloading");
            raw_average_time_of_uploading = resultobj.getDouble("raw_average_time_of_uploading");
            raw_average_time_of_downloading = resultobj.getDouble("raw_average_time_of_downloading");
            time_per_megabyte_upload = resultobj.getDouble("time_per_megabyte_upload");
            raw_time_per_megabyte_upload = resultobj.getDouble("raw_time_per_megabyte_upload");
            time_per_megabyte_download = resultobj.getDouble("time_per_megabyte_download");
            raw_time_per_megabyte_download = resultobj.getDouble("raw_time_per_megabyte_download");
            total_latency = resultobj.getDouble("total_latency");
            average_latency = resultobj.getDouble("average_latency");

            uzytkownikw = findViewById(R.id.uzytkownikw);
            uzytkownikw.setText(username);
            rangaw = findViewById(R.id.rangaw);
            if (rank.equals("1")) {
                rangaw.setText("użytkownik");
            }
            else if (rank.equals("3")) {
                rangaw.setText("administrator");
            }
            jezykw = findViewById(R.id.jezykw);
            sredni_czas_pingw = findViewById(R.id.sredni_czas_pingw);
            pliki_up = findViewById(R.id.pliki_up);
            pliki_down = findViewById(R.id.pliki_down);
            bajty_up = findViewById(R.id.bajty_up);
            bajty_down = findViewById(R.id.bajty_down);
            megabajty_up = findViewById(R.id.megabajty_up);
            megabajty_down = findViewById(R.id.megabajty_down);
            sredni_czas_up = findViewById(R.id.sredni_czas_up);
            sredni_czas_down = findViewById(R.id.sredni_czas_down);
            sredni_czas_aktualny_up = findViewById(R.id.sredni_czas_aktualny_up);
            sredni_czas_aktualny_down = findViewById(R.id.sredni_czas_aktualny_down);
            czas_na_mb_up = findViewById(R.id.czas_na_mb_up);
            czas_na_mb_down = findViewById(R.id.czas_na_mb_down);
            aktualny_czas_na_mb_up = findViewById(R.id.aktualny_czas_na_mb_up);
            aktualny_czas_na_mb_down = findViewById(R.id.aktualny_czas_na_mb_down);

            jezykw.setText(app_language);
            sredni_czas_pingw.setText(String.format(Locale.CANADA, "%.2f",average_latency));
            pliki_up.setText(String.valueOf(number_of_files_uploaded));
            pliki_down.setText(String.valueOf(number_of_files_downloaded));
            bajty_up.setText(String.valueOf(bytes_uploaded));
            bajty_down.setText(String.valueOf(bytes_downloaded));
            megabajty_up.setText(String.format(Locale.CANADA, "%.2f",megabytes_uploaded));
            megabajty_down.setText(String.format(Locale.CANADA, "%.2f",megabytes_downloaded));
            sredni_czas_up.setText(String.format(Locale.CANADA, "%.2f",average_time_of_uploading));
            sredni_czas_down.setText(String.format(Locale.CANADA, "%.2f",average_time_of_downloading));
            sredni_czas_aktualny_up.setText(String.format(Locale.CANADA, "%.2f",raw_average_time_of_uploading));
            sredni_czas_aktualny_down.setText(String.format(Locale.CANADA, "%.2f",raw_average_time_of_downloading));
            czas_na_mb_up.setText(String.format(Locale.CANADA, "%.2f",time_per_megabyte_upload));
            czas_na_mb_down.setText(String.format(Locale.CANADA, "%.2f",time_per_megabyte_download));
            aktualny_czas_na_mb_up.setText(String.format(Locale.CANADA, "%.2f",raw_time_per_megabyte_upload));
            aktualny_czas_na_mb_down.setText( String.format(Locale.CANADA, "%.2f",raw_time_per_megabyte_download));
;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}