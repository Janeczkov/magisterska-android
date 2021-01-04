package com.example.apkafinal.klasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apkafinal.R;
import com.example.apkafinal.interfejsy.GetCompleted;
import com.example.apkafinal.metody.GetMethod;
import com.example.apkafinal.metody.PostMethodAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class rejestracja extends AppCompatActivity implements GetCompleted {

    private EditText usernamereg;
    private EditText passwordreg;
    private Intent main;
    private Button regb;
    private Button cancelb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String ip = getString(R.string.ip);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Rejestracja");
        actionbar.setDisplayHomeAsUpEnabled(true);


        //main=new Intent(rejestracja.this,MainActivity.class);
        regb = findViewById(R.id.registerb);
        cancelb = findViewById(R.id.regcancelb);


        regb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamereg = findViewById(R.id.usernamereg);
                passwordreg = findViewById(R.id.passwordreg);
                final String userregcontent = usernamereg.getText().toString();
                final String passregcontent = passwordreg.getText().toString();
                if ((userregcontent.isEmpty())||(passregcontent.isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Poprawnie wypełnij pola",
                            Toast.LENGTH_LONG).show();
                }
                else if (userregcontent.length()>12) {
                    Toast.makeText(getApplicationContext(), "Za długa nazwa użytkownika",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    new GetMethod(rejestracja.this).execute(ip + "/accounts/");
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

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    public void onGetComplete(final String result) {
        String ip = getString(R.string.ip);

        final EditText usernamereg = findViewById(R.id.usernamereg);
        final EditText passwordreg = findViewById(R.id.passwordreg);

        final String userregcontent = usernamereg.getText().toString();
        final String passregcontent = passwordreg.getText().toString();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
            Boolean alreadyregistered = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                final String username = jsonobject.getString("username");

                if (userregcontent.equals(username)) {
                    alreadyregistered = true;
                    Toast.makeText(getApplicationContext(), "Konto o podanej nazwie już istnieje!",
                            Toast.LENGTH_LONG).show();
                    break;
                }
            }
            if (!alreadyregistered) {
                new PostMethodAccount(rejestracja.this).execute(ip + "/accounts/", userregcontent, passregcontent);
                Toast.makeText(getApplicationContext(), "Zostałes poprawnie zarejestrowany! Możesz się zalogować.",
                        Toast.LENGTH_LONG).show();

                final Intent zalogowany = new Intent(rejestracja.this, logowanie.class);
                rejestracja.this.startActivity(zalogowany);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
