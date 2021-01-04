package com.example.apkafinal.klasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.apkafinal.R;

public class zalogowany extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.zalogowany);
        Intent intent=getIntent();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Zostałeś zalogowany");
        actionbar.setDisplayHomeAsUpEnabled(true);

        final String username=intent.getStringExtra("username");
        final String rank=intent.getStringExtra("rank");

        final Intent admin=new Intent(zalogowany.this,admin.class);

        final Button usersb = findViewById(R.id.usersb);
        final Button dodajb = findViewById(R.id.dodajb);
        final Button materialyb = findViewById(R.id.materialyb);
        final Button adminb = findViewById(R.id.adminb);

        Log.e("ranga", rank);
        if (rank.equals("3")){
            adminb.setVisibility(View.VISIBLE);
        }


        materialyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent materialy=new Intent(zalogowany.this,materialy.class);
                materialy.putExtra("username", username);
                materialy.putExtra("rank", rank);
                zalogowany.this.startActivity(materialy);
            }
        });

        dodajb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent dodajplik=new Intent(zalogowany.this, dodajPlik.class);
                dodajplik.putExtra("username", username);
                dodajplik.putExtra("rank", rank);
                zalogowany.this.startActivity(dodajplik);
            }
        });

        adminb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.putExtra("username", username);
                admin.putExtra("rank", rank);
                zalogowany.this.startActivity(admin);
            }
        });

        usersb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent staty=new Intent(zalogowany.this,statystykiUzytkownika.class);
                staty.putExtra("username", username);
                staty.putExtra("rank", rank);
                zalogowany.this.startActivity(staty);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        /*Intent myIntent = new Intent(getApplicationContext(), logowanie.class);
        startActivityForResult(myIntent, 0);*/
        finish();
        return true;
    }
}
