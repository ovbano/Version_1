package com.example.appvinculacion;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroUsuario extends AppCompatActivity {

    private EditText et_main_usuario,et_main_contrasena;
    private Button btn_main_nuevo,buttonRegresar;

    private MetodosBDD adapter;
    private UsuarioModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        init();

        btn_main_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = et_main_usuario.getText().toString().trim();
                String contrasena=et_main_contrasena.getText().toString().trim();
                boolean validarInterfaz = validarCampos(usuario,contrasena);
                if(validarInterfaz){

                    model=new UsuarioModel(usuario,contrasena);
                    adapter.openWrite();
                    long valorInsert=adapter.insert(model,0);
                    adapter.close();

                    if(valorInsert > 0){
                        Toast.makeText(RegistroUsuario.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                        Intent login = new Intent(RegistroUsuario.this,LoginActivity.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login);
                    }else{
                        Toast.makeText(RegistroUsuario.this, "No se realizó el registro", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void init(){
        et_main_usuario=findViewById(R.id.et_main_usuario);
        et_main_contrasena=findViewById(R.id.et_main_contrasena);
        btn_main_nuevo=findViewById(R.id.btn_main_nuevo);
        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);
        adapter=new MetodosBDD(getApplicationContext());
        model=new UsuarioModel();

    }

    public boolean validarCampos(String usuario,String contrasena){
        if (usuario.isEmpty()||contrasena.isEmpty()){
            Toast.makeText(this,"Por favor ingrese su nombre y código",Toast.LENGTH_LONG).show();
            return false;
        }else if (usuario.length()<6||contrasena.length()<2){
            Toast.makeText(this,"Por favor ingrese su nombre y código válidos",Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

}