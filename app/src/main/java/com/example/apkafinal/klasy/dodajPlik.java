package com.example.apkafinal.klasy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apkafinal.R;
import com.example.apkafinal.interfejsy.UploadCompleted;
import com.example.apkafinal.metody.TaskParamsHelper;
import com.example.apkafinal.metody.UpdateMethodStatistics;
import com.example.apkafinal.metody.UploadMethod;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class dodajPlik extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UploadCompleted {


    private Button przegladajb;
    private Button wyslijplikb;
    private TextView wybrany;
    private static final int READ_REQUEST_CODE = 42;
    Uri uri = null;
    String displayName;
    String size = null;
    ProgressDialog bar;
    int numerwysylania, ilewyslac;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodajplik2);
        Intent intent = getIntent();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Dodawanie pliku");
        actionbar.setDisplayHomeAsUpEnabled(true);

        username = intent.getStringExtra("username");

        przegladajb = findViewById(R.id.pobraneb);
        wyslijplikb = findViewById(R.id.wyslijplikb);
        final String ip = getString(R.string.ip);

        final Button cancelb = findViewById(R.id.cancelb);


        przegladajb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(dodajPlik.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(dodajPlik.this, new String[]
                                {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        performFileSearch();
                    }
                } else {
                    performFileSearch();
                }

            }
        });

        wyslijplikb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uri == null) {
                    Toast.makeText(getApplicationContext(), "Proszę wybrać plik", Toast.LENGTH_LONG).show();
                } else if (getFileName(uri).length() > 40) {
                    Toast.makeText(getApplicationContext(), "Nazwa pliku jest za długa", Toast.LENGTH_LONG).show();
                } else {
                    byte[] zawartosc = null;
                    try {

                        zawartosc = readTextFromUri(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final byte[] zawartoscfin = zawartosc;
                    EditText liczbawysylan = findViewById(R.id.liczbawysylan);
                    final int ilewysylan = Integer.parseInt(String.valueOf(liczbawysylan.getText()));
                    bar = new ProgressDialog(dodajPlik.this);
                    bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    bar.setMax(ilewysylan);
                    bar.setProgress(0);
                    bar.show();
                    ilewyslac = ilewysylan;
                    numerwysylania = 0;

                    String filename = getFileName(uri);
                    int dotIndex = filename.lastIndexOf(".");
                    String rozmiar = filename.substring(0, dotIndex);
                    String koncowka = filename.substring(dotIndex + 1);
                    if (koncowka.equals("mp4")) {
                        koncowka = "vid";
                    }
                    username = koncowka + rozmiar + "j";
                    //Handler handler1 = new Handler();
                    for (int i = 1; i <= ilewysylan; i++) {
                        TaskParamsHelper params = new TaskParamsHelper(ip + "/file/upload/", zawartoscfin,
                                filename,
                                username);
                        new UploadMethod(dodajPlik.this).execute(params);
                    }
                }
            }
        });

        cancelb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onUploadComplete(final String result) {
        double avg = 0;
        int id = 0;
        String text = "";

        final String ip = getString(R.string.ip);
        String filename = "";
        try {
            JSONObject resultobj = new JSONObject(result);
            avg = resultobj.getDouble("avg");
            id = resultobj.getInt("id");
            filename = resultobj.getString("fileName");
            bar.setProgress(bar.getProgress() + (bar.getMax() / ilewyslac));
            numerwysylania++;
            if (numerwysylania == ilewyslac) {
                bar.dismiss();
                text = "Plik wyslano " + ilewyslac + " razy";
                Toast.makeText(dodajPlik.this, text, Toast.LENGTH_LONG).show();
            }
            new UpdateMethodStatistics(dodajPlik.this).execute(ip, "/ping/", "/file/updatestats/" + id, "upload", getString(R.string.jezyk), String.valueOf(avg), filename, username, String.valueOf(numerwysylania));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            performFileSearch();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        wybrany = findViewById(R.id.wybranyt);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                uri = resultData.getData();
                String[] projection = {MediaStore.Images.ImageColumns.DATA};
                Cursor cursor = dodajPlik.this.getContentResolver()
                        .query(uri, projection, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE) + 1;

                        if (!cursor.isNull(sizeIndex)) {
                            size = cursor.getString(sizeIndex);
                        } else {
                            size = "Unknown";
                        }
                        size = cursor.getString(sizeIndex);
                        wybrany.setText(getFileName(uri));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
    }

    private byte[] readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        byte[] data = IOUtils.toByteArray(inputStream);

        return data;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}