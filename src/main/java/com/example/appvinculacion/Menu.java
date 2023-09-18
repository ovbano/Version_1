package com.example.appvinculacion;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;


import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Menu extends AppCompatActivity implements View.OnClickListener{

    private CardView cardIniciar, cardRegistros, cardEncuestar, cardAcercade, cardCredito, cardCerrarSesion;
    private TextView textInicio;
    private SharedPreferences preferences;

    Button buttonFoto;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();

        String usuario_nombre=preferences.getString("usuario_nombre", null);
        String usuario_codigo=preferences.getString("usuario_codigo", null);

        if(usuario_codigo!=null && usuario_nombre!=null){
            textInicio.setText(usuario_codigo);

        }


    }




    @Override
    public void onClick(View v) {

        Intent i ;

        switch (v.getId()){
            //case R.id.cardIniciar: i=new Intent(this,MiPerfil.class);startActivity(i);break;
            case R.id. cardEncuestar: i=new Intent(this,MenuEncuesta.class);startActivity(i);break;
            case R.id.cardRegistros: i=new Intent(this,MenuRegistros.class);;startActivity(i);break;
            case R.id.cardAcercade: i=new Intent(this,Acercade.class);startActivity(i);break;
            // case R.id.cardCredito: i=new Intent(this,Credito.class);startActivity(i);break;
            case R.id.cardCerrarSesion: cerrarSesion();break;

            default:break;
        }

    }

    private void init(){
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        textInicio=findViewById(R.id.textInicio);

        //Definir Cards

        cardEncuestar=(CardView) findViewById(R.id.cardEncuestar);
        cardRegistros=(CardView) findViewById(R.id.cardRegistros);
        cardAcercade=(CardView) findViewById(R.id.cardAcercade);
        cardCerrarSesion=(CardView) findViewById(R.id.cardCerrarSesion);


        cardEncuestar.setOnClickListener(this);
        cardRegistros.setOnClickListener(this);
        cardAcercade.setOnClickListener(this);
        cardCerrarSesion.setOnClickListener(this);

    }

    private void cerrarSesion(){
        preferences.edit().clear().apply();
        irLogin();
    }
    private void irLogin(){
        Intent irlogin = new Intent(Menu.this,LoginActivity.class);
        irlogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(irlogin);
    }

}