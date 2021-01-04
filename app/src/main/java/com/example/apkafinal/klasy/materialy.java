package com.example.apkafinal.klasy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apkafinal.R;
import com.example.apkafinal.interfejsy.DownloadCompleted;
import com.example.apkafinal.interfejsy.GetCompleted;
import com.example.apkafinal.interfejsy.PingCompleted;
import com.example.apkafinal.metody.DeleteMethod;
import com.example.apkafinal.metody.DownloadMethod;
import com.example.apkafinal.metody.GetMethod;
import com.example.apkafinal.metody.UpdateMethodStatistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class materialy extends AppCompatActivity implements GetCompleted, DownloadCompleted, PingCompleted {

    private TextView liczbapobrant;
    ProgressDialog bar;
    int numerpobierania, ilepobrac;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materialy);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Lista materiałów");
        actionbar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        final String ip = getString(R.string.ip);


        new GetMethod(materialy.this).execute(ip + "/file/all/");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    public void onGetComplete(final String result) {

        Intent intent = getIntent();
        final String ip = getString(R.string.ip);

        String rank = intent.getStringExtra("rank");
        Log.e("ranga", rank);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                final int fileid = jsonobject.getInt("id");
                final String fileName = jsonobject.getString("fileName");
                final Intent statystykipliku=new Intent(materialy.this,statystykiPliku.class);


                        final LinearLayout linear = (LinearLayout) getLayoutInflater().inflate(R.layout.materialy, null);
                        ((TextView) linear.findViewById(R.id.idt)).setText("Plik numer " + (i + 1) + " o nazwie " + fileName);
                        //LinearLayout linear=(LinearLayout) tableRow.findViewById(R.id.linear);
                        linearLayout.addView(linear);

                        final Button downloadb = linear.findViewById(R.id.downloadb);
                        final Button deleteb = linear.findViewById(R.id.deletefb);
                        final Button filestatsb = linear.findViewById(R.id.filestatsb);

                        if (rank.equals("1")){
                            deleteb.setVisibility(View.INVISIBLE);
                        }


                        downloadb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(materialy.this);
                                builder.setTitle("Pobieranie pliku");
                                builder.setMessage("Czy na pewno chcesz pobrac?");
                                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        liczbapobrant = linear.findViewById(R.id.liczbapobrant);
                                        final int liczbapobran = Integer.parseInt(String.valueOf(liczbapobrant.getText()));
                                        bar = new ProgressDialog(materialy.this);
                                        bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        bar.setMax(liczbapobran);
                                        bar.setProgress(0);
                                        bar.show();
                                        ilepobrac = liczbapobran;
                                        numerpobierania = 0;


                                        for (int i=1; i<=liczbapobran; i++) {
                                            new DownloadMethod(materialy.this).execute(ip + "/file/download/" + fileid, String.valueOf(fileid));
                                        }


                                    }
                                });
                                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                        deleteb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(materialy.this);
                                builder.setTitle("Usunięcie materiału");
                                builder.setMessage("Czy na pewno usunąć ten materiał?");
                                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        new DeleteMethod(materialy.this).execute(ip + "/file/delete/" + fileid);
                                        finish();
                                        startActivity(getIntent());
                                        Toast.makeText(materialy.this, "Usunięto plik", Toast.LENGTH_LONG).show();

                                    }
                                });
                                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                        filestatsb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                statystykipliku.putExtra("fileid", String.valueOf(fileid));
                                materialy.this.startActivity(statystykipliku);

                            }
                        });


            }

        //}
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPingComplete(final String result) {

    }

    @Override
    public void onDownloadComplete(final String result) {
        JSONObject obj = null;
        String done = "";
        String fileid = "";
        String folder = "";
        String filename = "";
        float avg = 0;
        try {
            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            assert obj != null;
             done = obj.getString("done");
             fileid = obj.getString("fileid");
             folder = obj.getString("folder");
             filename = obj.getString("filename");
            avg = Float.parseFloat(obj.getString("czaswykonywania"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String text = null;
        final String ip = getString(R.string.ip);
        if (done.equals("true")) {
            bar.setProgress(bar.getProgress()+(bar.getMax()/ilepobrac));
            numerpobierania ++;
            //int k = numerpobierania;

            int dotIndex = filename.lastIndexOf(".");
            String rozmiar = filename.substring(0, dotIndex);
            String koncowka = filename.substring(dotIndex+1);
            if (koncowka.equals("mp4")) {
                koncowka = "vid";
            }
            username = koncowka + rozmiar + "j";

            if (numerpobierania == ilepobrac) {
                bar.dismiss();
                text = "Plik pobrano " + ilepobrac + " razy i zapisano w folderze " + folder;
                Toast.makeText(materialy.this, text, Toast.LENGTH_LONG).show();
            }
            new UpdateMethodStatistics(materialy.this).execute(ip, "/ping/", "/file/updatestats/" + fileid, "download", getString(R.string.jezyk), String.valueOf(avg), filename, username, String.valueOf(numerpobierania));

        }
        else if (done.equals("exists")) {
            text = "Plik o danej nazwie już istnieje";
            Toast.makeText(materialy.this, text, Toast.LENGTH_LONG).show();
        }
        else {
            text = "Wystąpił błąd pobierania";
            Toast.makeText(materialy.this, text, Toast.LENGTH_LONG).show();
        }

    }
}