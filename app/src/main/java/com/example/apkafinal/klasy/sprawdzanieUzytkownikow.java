package com.example.apkafinal.klasy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apkafinal.R;
import com.example.apkafinal.interfejsy.GetCompleted;
import com.example.apkafinal.metody.DeleteMethod;
import com.example.apkafinal.metody.GetMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sprawdzanieUzytkownikow extends AppCompatActivity implements GetCompleted {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_uzytkownikow);
        Intent intent = getIntent();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Lista użytkowników");
        actionbar.setDisplayHomeAsUpEnabled(true);
        final String ip = getString(R.string.ip);


        final String username = intent.getStringExtra("username");
        final String rank = intent.getStringExtra("rank");


        new GetMethod(sprawdzanieUzytkownikow.this).execute(ip + "/accounts/");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        /*Intent myIntent = new Intent(getApplicationContext(), logowanie.class);
        startActivityForResult(myIntent, 0);*/
        finish();
        return true;
    }

    void rysuj(String result){
        final String ip = getString(R.string.ip);
        final Intent statystykiUzytkownika = new Intent(sprawdzanieUzytkownikow.this,statystykiUzytkownika.class);


        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                final int userid = jsonobject.getInt("id");
                final String username = jsonobject.getString("username");
                final String rank = jsonobject.getString("rank");
                final String platform = jsonobject.getString("app_language");
//jak to dziala? korzystam z zadeklarowanego activity dla stworzenia bazy layoutu a potem korzystajac z idkow wypelniam owe activity
                final LinearLayout linear=(LinearLayout) getLayoutInflater().inflate(R.layout.wypelnij_uzytkownikow,null);
                ((TextView) linear.findViewById(R.id.usernamet)).setText("Użytkownik:\r\n" + username);
                if(rank.equals(1)) {
                    ((TextView) linear.findViewById(R.id.rangt)).setText("Ranga: uzytkownik");
                }
                else {
                    ((TextView) linear.findViewById(R.id.rangt)).setText("Ranga: administrator");
                }
                ((TextView) linear.findViewById(R.id.platformt)).setText("Platforma: " + platform);
                linearLayout.addView(linear);

                final Button statsb = (Button) linear.findViewById(R.id.statsb);
                final Button deleteb = (Button) linear.findViewById(R.id.deleteb);

                statsb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        statystykiUzytkownika.putExtra("username", username);
                        statystykiUzytkownika.putExtra("rank", rank);
                        sprawdzanieUzytkownikow.this.startActivity(statystykiUzytkownika);
                    }
                });


                deleteb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(sprawdzanieUzytkownikow.this);
                        builder.setTitle("Usunięcie użytkownika");
                        builder.setMessage("Czy na pewno chcesz go usunąć?");
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                new DeleteMethod(sprawdzanieUzytkownikow.this).execute(ip + "/accounts/" + userid);
                                finish();
                                startActivity(getIntent());
                                //wykonuje delete usera

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



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    @Override
    public void onGetComplete(final String result) {
        Log.e("rezulcik", result);
        rysuj(result);
    }

}