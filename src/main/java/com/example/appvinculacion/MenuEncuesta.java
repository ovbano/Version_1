package com.example.appvinculacion;

import android.content.Intent;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuEncuesta extends AppCompatActivity implements View.OnClickListener{

    private Button encuesta1,encuesta2,buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_encuesta);
        init();
    }
    private void init(){
        encuesta1=(Button) findViewById(R.id.encuesta1);
        encuesta2=(Button) findViewById(R.id.encuesta2);
        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);

        encuesta1.setOnClickListener(this);
        encuesta2.setOnClickListener(this);
        buttonRegresar.setOnClickListener(this);
    }


    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.encuesta1:
                i = new Intent(this, MiEncuesta.class);
                startActivity(i);

                break;
            case R.id.encuesta2:
                i = new Intent(this, MiEncuesta2.class);
                startActivity(i);
                break;
            case R.id.buttonRegresar:
                i = new Intent(this, Menu.class);
                startActivity(i);
                break;

            default:
                break;
        }

    }

}