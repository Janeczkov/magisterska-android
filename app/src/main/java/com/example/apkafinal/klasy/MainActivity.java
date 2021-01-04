package com.example.apkafinal.klasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.apkafinal.R;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
       actionbar.setTitle("Menu główne");

        final Button mainloginb = findViewById(R.id.mainloginb);
        final Button mainregisterb = findViewById(R.id.mainregisterb);

        final Intent atlogin = new Intent(MainActivity.this, logowanie.class);
        final Intent atregister=new Intent(MainActivity.this,rejestracja.class);






        mainloginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(atlogin);
            }
        });

        mainregisterb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(atregister);
            }
        });
    }
}