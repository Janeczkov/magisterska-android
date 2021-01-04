package com.example.apkafinal.klasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.apkafinal.R;

public class admin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Intent intent=getIntent();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Panel administratora");
        actionbar.setDisplayHomeAsUpEnabled(true);

        final String username=intent.getStringExtra("username");
        final String rank=intent.getStringExtra("rank");

        final Intent listaUzytkownikow=new Intent(admin.this,sprawdzanieUzytkownikow.class);
        final Intent statystykiSerwera=new Intent(admin.this,statystykiSerwera.class);
        final Intent statystykiUzytkownika=new Intent(admin.this,statystykiUzytkownika.class);

        final Button usersb = findViewById(R.id.usersb);
        final Button materialyb = findViewById(R.id.materialyb);
        final Button dodajb = findViewById(R.id.dodajb);
        final Button uzytkownicyb = findViewById(R.id.uzytkownicyb);
        final Button serwersb = findViewById(R.id.serwersb);
        usersb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statystykiUzytkownika.putExtra("username", username);
                statystykiUzytkownika.putExtra("rank", rank);
                admin.this.startActivity(statystykiUzytkownika);
            }
        });
        serwersb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.this.startActivity(statystykiSerwera);
            }
        });
        materialyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent materialy=new Intent(admin.this,materialy.class);
                materialy.putExtra("username", username);
                materialy.putExtra("rank", rank);
                admin.this.startActivity(materialy);
            }
        });

        dodajb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent dodajplik=new Intent(admin.this, dodajPlik.class);
                dodajplik.putExtra("username", username);
                dodajplik.putExtra("rank", rank);
                admin.this.startActivity(dodajplik);
            }
        });

        uzytkownicyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.this.startActivity(listaUzytkownikow);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
