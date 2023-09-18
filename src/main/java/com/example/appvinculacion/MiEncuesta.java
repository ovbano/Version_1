package com.example.appvinculacion;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MiEncuesta extends AppCompatActivity implements View.OnClickListener{

    private Spinner comunidad_spinner;
    private Spinner barrio_spinner;
    private Spinner junta_spinner;

    private EditText cantidadHombres,cantidadMujeres,cantidadNinos,cantidadDiscapacidad;

    Button buttonFotoo;
    String currentPhotoPatch;
    ImageView viewFoto;

    String imagenString = "";

    private Button seccion2;

    private View texto_info19;

    private LinearLayout textPregunta11,textPregunta13,textPregunta15,textPregunta17,textPregunta19,textPregunta20,textPregunta22,textPregunta23,textPregunta24,
            textPregunta25,textPregunta26,textPregunta27,textPregunta30,textPregunta32,textPregunta33,textPregunta34,textPregunta35,textPregunta36,textPregunta37;


    //CARACTERÍSTICAS DE LA VIVIENDA: (Varias Opciones)
    private CheckBox check_casa,check_departamento,check_cuarto,check_otros;
    private EditText campo_otros;

    //Nro. de pisos
    private CheckBox check_uno,check_dos,check_tres,check_cuatro;

    //Techo
    private CheckBox check_cinc,check_teja,check_loseta,check_loza;

    //Paredes
    private CheckBox check_madera,check_bloque,check_hormigon,check_loza1;

    //Piso
    private CheckBox check_madera1,check_hormigon1,check_porcelanato,check_tierra;//p5

    //DATOS DEL ENCUESTADO
    private TextView encuestado_codigo;
    private EditText encuestado_nombre,encuestado_direccion,encuestado_edad,encuestado_junta,encuestado_barrio,encuestado_comunidad;
    private CheckBox check_hombre,check_mujer;

    //LA VIVIENDA QUE HABITA ES
    private CheckBox check_propia,check_rentada,check_prestada,check_otros1;
    private TextView campo_otros1;

    //¿CUÁNTAS PERSONAS HABITAN LA VIVIENDA?
    private EditText encuestado_vivienda;

    //¿ALGÚN INTEGRANTE DE LA FAMILIA HA TENIDO PROBLEMAS ESTOMACALES O INTESTINALES EN ESTE AÑO?
    private CheckBox check_si,check_no;

    //¿TUVO TRATAMIENTO MÉDICO?
    private CheckBox check_si1,check_no1;

    //¿CUÁL FUE EL DIAGNÓSTICO?
    private CheckBox check_diarrea,check_gastroenteritis,check_amebiasis,check_echericha,check_colera,check_otras;
    private EditText texto_otros;

    //¿ALGÚN INTEGRANTE DE LA FAMILIA TENIDO ENFERMEDADES DE LA PIEL?
    private CheckBox check_si2,check_no2;

    //¿TUVO TRATAMIENTO MÉDICO?
    private CheckBox check_si3,check_no3;

    //¿CUÁL FUE EL DIAGNÓSTICO?
    private CheckBox  check_hongos,check_escabiosis,check_alergias,check_otra;
    private EditText  texto_diagnostico1;

    //¿DE DONDE SE ABASTECE PARA EL AGUA DE CONSUMO?
    private CheckBox check_potable,check_pozo,check_rio,check_lluvia,check_tanquero,check_embotellada,check_otros2;
    private EditText texto_nombreRio,campo_otros2;

    //¿TIENE CISTERNA O TANQUE ELEVADO?
    private CheckBox check_cisterna,check_tanque,check_ninguno;

    //TIENE ALGÚN INTEGRANTE DE LA FAMILIA HA TENIDO PROBLEMA...
    private CheckBox check_red_agua_llave,check_red_agua_cisterna_llave,check_red_agua_tanque_llave,check_red_agua_cisterna_tanque_llave,check_directo_pozo;

    //CAPACIDAD DE LA CISTERNA (metros^3)
    private CheckBox check_1,check_2,check_3,check_4,check_5,check_6,check_7;

    //CAPACIDAD DEL TANQUE ELEVADO (Litros)
    private CheckBox check_250,check_500,check_600,check_1000,check_1100,check_2000,check_2500;

    //¿CON QUÉ FRECUENCIA REALIZA LA LIMPIEZA O LAVADO?
    private CheckBox check_semanal,check_quincenal,check_mensual,check_trimestral,check_semestral,check_anual,check_bienal,check_nunca;

    //INDICAR SI REALIZA CLORACIÓN AL AGUA DE ESTOS ALMACENAMIENTOS
    private CheckBox check_si4,check_no4;

    //¿CON QUÉ FRECUENCIA CLORA EL TANQUE?
    private CheckBox check_semanal1,check_quincenal1,check_mensual1,check_bimensual1,check_trimestral1,check_cuatrimestral1,check_semestral1,check_anual1,check_otras1;
    private EditText texto_otros1;

    //¿CUAL ES LA DOSIFICACIÓN UTILIZADA?
    private CheckBox check_op1,check_op2,check_op3,check_op4,check_op5,check_op6,check_op7,check_op8,check_otras2;
    private EditText texto_otros2;

    //EL AGUA QUE USTED UTILIZA PARA BEBER ES:
    private CheckBox check_llave,check_hervida,check_filtrada,check_embotellada1;

    //REALIZA ALGÚN TIPO DE TRATAMIENTO AL AGUA QUE UTLIZA PARA BEBER
    private CheckBox check_si5,check_no5;

    //INDICAR QUE TRATAMIENTO
    private EditText tratamiento;

    //¿QUÉ USO LE DA AL AGUA?
    private CheckBox check_cocina,check_aseo,check_lavado,check_animales,check_riego;

    //EN EL CASO DE TENER ANIMALES QUE CONSUMEN AGUA INDICAR:
    private CheckBox check_animal_mascota,check_animal_consumo,check_animal_venta;

    //INDICAR ANIMALES PARA CONSUMO
    private EditText text_animal_consumo;

     //INDICAR ANIMALES PARA VENTA
    private EditText text_animal_venta;

    //EN EL CASO DE TENER SEMBRÍOS QUE CONSUMEN AGUA INDICAR
    private CheckBox check_ornamentales,check_sembrio_consumo,check_sembrio_venta;

    //INDICAR SEMBRÍOS PARA CONSUMO
    private EditText text_sembrios_consumo;

    //INDICAR SEMBRÍOS PARA VENTA
    private EditText text_sembrios_venta;


    private String nombre,codigo;

    private TextView TituloFecha,HoraInicio, latitud,longitud;
    private TextView direccion;

    private SharedPreferences preferences;

    private MetodosBDD db;
    private Button buttonSave;
    private ListView listViewNames;
    private List<Name1> names;
    private ListView listViewNames3;
    private List<Name3> names3;


    private BroadcastReceiver broadcastReceiver;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NameAdapter1 nameAdapter;
    private NameAdapter3 nameAdapter3;

    public static final String URL_SAVE_NAME = "http://192.188.58.82:3000/encuesta1";
    //public static final String URL_SAVE_NAME3 = "http://192.188.58.82:8081/appsincronizar/persona.php";

    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_encuesta);
        //matrices de permisos de inicio

        if((ContextCompat.checkSelfPermission(MiEncuesta.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MiEncuesta.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(MiEncuesta.this, new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }


        registerReceiver(new NetworkStateChecker1(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(new NetworkStateChecker3(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new MetodosBDD(this);
        names = new ArrayList<>();
        names3 = new ArrayList<>();


        init();


        generarCodigo();
        fechayhora();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        seccion2.setOnClickListener(this);

        loadNames();
        loadNames3();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                loadNames();
                loadNames3();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    public void init(){


        comunidad_spinner = (Spinner) findViewById(R.id.comunidad_spinner);
        barrio_spinner = (Spinner) findViewById(R.id.barrio_spinner);
        junta_spinner = (Spinner) findViewById(R.id.junta_spinner);

        cantidadHombres = (EditText) findViewById(R.id.cantidadHombres);
        cantidadMujeres = (EditText) findViewById(R.id.cantidadMujeres);
        cantidadNinos = (EditText) findViewById(R.id.cantidadNinos);
        cantidadDiscapacidad = (EditText) findViewById(R.id.cantidadDiscapacidad);

        check_casa = (CheckBox) findViewById(R.id.check_casa);
        check_departamento = (CheckBox) findViewById(R.id.check_departamento);
        check_cuarto = (CheckBox) findViewById(R.id.check_cuarto);
        check_otros = (CheckBox) findViewById(R.id.check_otros);
        campo_otros = findViewById(R.id.campo_otros);

        check_uno=(CheckBox) findViewById(R.id.check_uno);
        check_dos=(CheckBox) findViewById(R.id.check_dos);
        check_tres=(CheckBox) findViewById(R.id.check_tres);
        check_cuatro=(CheckBox) findViewById(R.id.check_cuatro);

        check_cinc=(CheckBox) findViewById(R.id.check_cinc);
        check_teja=(CheckBox) findViewById(R.id.check_teja);
        check_loseta=(CheckBox) findViewById(R.id.check_loseta);
        check_loza=(CheckBox) findViewById(R.id.check_loza);

        check_madera=(CheckBox) findViewById(R.id.check_madera);
        check_bloque=(CheckBox) findViewById(R.id.check_bloque);
        check_hormigon=(CheckBox) findViewById(R.id.check_hormigon);
        check_loza1=(CheckBox) findViewById(R.id.check_loza1);

        check_madera1=(CheckBox) findViewById(R.id.check_madera1);
        check_hormigon1=(CheckBox) findViewById(R.id.check_hormigon1);
        check_porcelanato=(CheckBox) findViewById(R.id.check_porcelanato);
        check_tierra=(CheckBox) findViewById(R.id.check_tierra);

        encuestado_codigo=(TextView) findViewById(R.id.encuestado_codigo);
        encuestado_nombre=(EditText) findViewById(R.id.encuestado_nombre);
        encuestado_direccion=(EditText) findViewById(R.id.encuestado_direccion);
        encuestado_comunidad=(EditText) findViewById(R.id.encuestado_comunidad);
        encuestado_barrio=(EditText) findViewById(R.id.encuestado_barrio);
        encuestado_junta=(EditText) findViewById(R.id.encuestado_junta);
        encuestado_edad=(EditText) findViewById(R.id.encuestado_edad);
        check_hombre=(CheckBox) findViewById(R.id.check_hombre);
        check_mujer=(CheckBox) findViewById(R.id.check_mujer);

        check_propia=(CheckBox) findViewById(R.id.check_propia);
        check_rentada=(CheckBox) findViewById(R.id.check_rentada);
        check_prestada=(CheckBox) findViewById(R.id.check_prestada);
        check_otros1=(CheckBox) findViewById(R.id.check_otros1);
        campo_otros1=(EditText) findViewById(R.id.campo_otros1);

        encuestado_vivienda=(EditText) findViewById(R.id.encuestado_vivienda);

        check_si=(CheckBox) findViewById(R.id.check_si);
        check_no=(CheckBox) findViewById(R.id.check_no);
        textPregunta11 = findViewById(R.id.textPregunta11);

        check_si1=(CheckBox) findViewById(R.id.check_si1);
        check_no1=(CheckBox) findViewById(R.id.check_no1);
        textPregunta13 = findViewById(R.id.textPregunta13);

        check_diarrea=(CheckBox) findViewById(R.id.check_diarrea);
        check_gastroenteritis=(CheckBox) findViewById(R.id.check_gastroenteritis);
        check_amebiasis=(CheckBox) findViewById(R.id.check_amebiasis);
        check_echericha=(CheckBox) findViewById(R.id.check_echericha);
        check_colera=(CheckBox) findViewById(R.id.check_colera);
        check_otras= (CheckBox) findViewById(R.id.check_otras);
        texto_otros= (EditText)findViewById(R.id.texto_otros);


        check_si2 = (CheckBox) findViewById(R.id.check_si2);
        check_no2= (CheckBox) findViewById(R.id.check_no2);
        textPregunta15 = findViewById(R.id.textPregunta15);

        check_si3 = (CheckBox) findViewById(R.id.check_si3);
        check_no3 = (CheckBox) findViewById(R.id.check_no3);
        textPregunta17 = findViewById(R.id.textPregunta17);

        check_hongos= (CheckBox) findViewById(R.id.check_hongos);
        check_escabiosis= (CheckBox) findViewById(R.id.check_escabiosis);
        check_alergias= (CheckBox) findViewById(R.id.check_alergias);
        check_otra= (CheckBox) findViewById(R.id.check_otra);

        check_potable=(CheckBox) findViewById(R.id.check_potable);
        check_pozo=(CheckBox) findViewById(R.id.check_pozo);
        check_rio=(CheckBox) findViewById(R.id.check_rio);
        check_lluvia=(CheckBox) findViewById(R.id.check_lluvia);
        check_tanquero=(CheckBox) findViewById(R.id.check_tanquero);
        check_embotellada=(CheckBox) findViewById(R.id.check_embotellada);
        check_otros2=(CheckBox) findViewById(R.id.check_otros2);
        textPregunta19 = findViewById(R.id.textPregunta19);
        textPregunta20 = findViewById(R.id.textPregunta20);
        texto_nombreRio=(EditText)findViewById(R.id.texto_nombreRio);
        campo_otros2=(EditText)findViewById(R.id.campo_otros2);

        check_cisterna = (CheckBox) findViewById(R.id.check_cisterna);
        textPregunta22 = findViewById(R.id.textPregunta22);
        check_tanque = (CheckBox) findViewById(R.id.check_tanque);
        textPregunta23 = findViewById(R.id.textPregunta23);
        check_ninguno= (CheckBox) findViewById(R.id.check_ninguno);

        check_red_agua_llave = (CheckBox) findViewById(R.id.check_red_agua_llave);
        check_red_agua_cisterna_llave = (CheckBox) findViewById(R.id.check_red_agua_cisterna_llave);
        check_red_agua_tanque_llave = (CheckBox) findViewById(R.id.check_red_agua_tanque_llave);
        check_red_agua_cisterna_tanque_llave = (CheckBox) findViewById(R.id.check_red_agua_cisterna_tanque_llave);
        check_directo_pozo = (CheckBox) findViewById(R.id.check_directo_pozo);

        check_1=(CheckBox) findViewById(R.id.check_1);
        check_2=(CheckBox) findViewById(R.id.check_2);
        check_3=(CheckBox) findViewById(R.id.check_3);
        check_4=(CheckBox) findViewById(R.id.check_4);
        check_5=(CheckBox) findViewById(R.id.check_5);
        check_6=(CheckBox) findViewById(R.id.check_6);
        check_7=(CheckBox) findViewById(R.id.check_7);

        check_250=(CheckBox) findViewById(R.id.check_250);
        check_500=(CheckBox) findViewById(R.id.check_500);
        check_600=(CheckBox) findViewById(R.id.check_600);
        check_1000=(CheckBox) findViewById(R.id.check_1000);
        check_1100=(CheckBox) findViewById(R.id.check_1100);
        check_2000=(CheckBox) findViewById(R.id.check_2000);
        check_2500=(CheckBox) findViewById(R.id.check_2500);

        check_semanal=(CheckBox) findViewById(R.id.check_semanal);
        check_quincenal=(CheckBox) findViewById(R.id.check_quincenal);
        check_mensual=(CheckBox) findViewById(R.id.check_mensual);
        check_trimestral=(CheckBox) findViewById(R.id.check_trimestral);
        check_semestral=(CheckBox) findViewById(R.id.check_semestral);
        check_anual=(CheckBox) findViewById(R.id.check_anual);
        check_bienal=(CheckBox) findViewById(R.id.check_bienal);
        check_nunca=(CheckBox) findViewById(R.id.check_nunca);

        check_si4=(CheckBox) findViewById(R.id.check_si4);
        textPregunta26=findViewById(R.id.textPregunta26);
        textPregunta27=findViewById(R.id.textPregunta27);
        check_no4=(CheckBox) findViewById(R.id.check_no4);


        check_semanal1=(CheckBox) findViewById(R.id.check_semanal1);
        check_quincenal1=(CheckBox) findViewById(R.id.check_quincenal1);
        check_mensual1=(CheckBox) findViewById(R.id.check_mensual1);
        check_bimensual1=(CheckBox) findViewById(R.id.check_bimensual1);
        check_trimestral1=(CheckBox) findViewById(R.id.check_trimestral1);
        check_cuatrimestral1=(CheckBox) findViewById(R.id.check_cuatrimestral1);
        check_semestral1=(CheckBox) findViewById(R.id.check_semestral1);
        check_anual1=(CheckBox) findViewById(R.id.check_anual1);
        check_otras1=(CheckBox) findViewById(R.id.check_otras1);
        texto_otros1= findViewById(R.id.texto_otros1);

        check_op1=(CheckBox) findViewById(R.id.check_op1);
        check_op2=(CheckBox) findViewById(R.id.check_op2);
        check_op3=(CheckBox) findViewById(R.id.check_op3);
        check_op4=(CheckBox) findViewById(R.id.check_op4);
        check_op5=(CheckBox) findViewById(R.id.check_op5);
        check_op6=(CheckBox) findViewById(R.id.check_op6);
        check_op7=(CheckBox) findViewById(R.id.check_op7);
        check_op8=(CheckBox) findViewById(R.id.check_op8);
        check_otras2=(CheckBox) findViewById(R.id.check_otras2);
        texto_otros2= findViewById(R.id.texto_otros2);

        check_llave=(CheckBox) findViewById(R.id.check_llave);
        check_hervida=(CheckBox) findViewById(R.id.check_hervida);
        check_filtrada=(CheckBox) findViewById(R.id.check_filtrada);
        check_embotellada1=(CheckBox) findViewById(R.id.check_embotellada1);


        check_si5=(CheckBox) findViewById(R.id.check_si5);
        textPregunta30= findViewById(R.id.textPregunta30);
        check_no5=(CheckBox) findViewById(R.id.check_no5);


        tratamiento=(EditText)findViewById(R.id.tratamiento);

        //¿QUÉ USO LE DA AL AGUA?
        check_cocina=(CheckBox) findViewById(R.id.check_cocina);
        check_aseo=(CheckBox) findViewById(R.id.check_aseo);
        check_lavado=(CheckBox) findViewById(R.id.check_lavado);
        check_animales=(CheckBox) findViewById(R.id.check_animales);
        check_riego=(CheckBox) findViewById(R.id.check_riego);
        textPregunta32= findViewById(R.id.textPregunta32);
        textPregunta35 = findViewById(R.id.textPregunta35);


        check_animal_mascota=(CheckBox) findViewById(R.id.check_animal_mascota);
        check_animal_consumo=(CheckBox) findViewById(R.id.check_animal_consumo);
        textPregunta33= findViewById(R.id.textPregunta33);
        check_animal_venta=(CheckBox) findViewById(R.id.check_animal_venta);
        textPregunta34 = findViewById(R.id.textPregunta34);
        text_animal_consumo=(EditText) findViewById(R.id.text_animal_consumo);
        text_animal_venta=(EditText) findViewById(R.id.text_animal_venta);



        check_ornamentales=(CheckBox) findViewById(R.id.check_ornamentales);
        check_sembrio_consumo=(CheckBox) findViewById(R.id.check_sembrio_consumo);
        check_sembrio_venta=(CheckBox) findViewById(R.id.check_sembrio_venta);
        textPregunta36 = findViewById(R.id.textPregunta36);
        textPregunta37 = findViewById(R.id.textPregunta37);

        text_sembrios_consumo=(EditText) findViewById(R.id.text_sembrios_consumo);
        text_sembrios_venta=(EditText) findViewById(R.id.text_sembrios_venta);


        texto_diagnostico1=(EditText)findViewById(R.id.texto_diagnostico1);


        textPregunta24 = findViewById(R.id.textPregunta24);
        texto_info19= findViewById(R.id.texto_info19);
        textPregunta25= findViewById(R.id.textPregunta25);

        seccion2= (Button) findViewById(R.id.seccion2);


        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio= (TextView) findViewById(R.id.HoraInicio);
        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);

        buttonFotoo = (Button) findViewById(R.id.buttonFotof);
        viewFoto = (ImageView) findViewById(R.id.imagenCap);


        buttonFotoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*odebo guardar la imagen como fichero*/
                String filename = encuestado_codigo.getText().toString().trim()+"_";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(filename,".jpg", storageDirectory);
                    currentPhotoPatch = imageFile.getAbsolutePath();
                    Uri imageUri = FileProvider.getUriForFile(MiEncuesta.this,
                            "com.example.appvinculacion.fileprovider",imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent,1);

                    //onActivityResult
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //Si da en otros se mostrará una caja de texto
        check_otros.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_casa.setChecked(false);
                check_departamento.setChecked(false);
                check_cuarto.setChecked(false);
                if(check_otros.isChecked()){
                    campo_otros.setVisibility(View.VISIBLE);
                    campo_otros.setText(null);
                }else {
                    campo_otros.setVisibility(View.GONE);
                    campo_otros1.setText(null);
                }
            }
        });

        //Si da en otros se mostrará una caja de texto
        check_otros1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_propia.setChecked(false);
                check_rentada.setChecked(false);
                check_prestada.setChecked(false);
                if(check_otros1.isChecked()){
                    campo_otros1.setVisibility(View.VISIBLE);
                    campo_otros1.setText(null);
                }else {
                    campo_otros1.setVisibility(View.GONE);
                    campo_otros1.setText(null);
                }

            }
        });

        //Si da en "si" se mostrará una pregunta más
        check_si.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(check_si.isChecked()){
                    check_no.setChecked(false);
                }
                check_si.setChecked(check_no.isChecked() ? false: true);
                textPregunta11.setVisibility(check_si.isChecked() ? View.VISIBLE : View.GONE);
                check_si1.setChecked(false);
                check_no1.setChecked(false);
                check_diarrea.setChecked(false);
                check_gastroenteritis.setChecked(false);
                check_amebiasis.setChecked(false);
                check_echericha.setChecked(false);
                check_colera.setChecked(false);
                check_otras.setChecked(false);
                texto_otros.setText(null);

            }
        });

        //Si da en "si" se mostrará una pregunta más
        check_si1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si1.isChecked()){
                    check_no1.setChecked(false);
                }
                check_si1.setChecked(check_no1.isChecked() ? false: true);

                textPregunta13.setVisibility(check_si1.isChecked() ? View.VISIBLE : View.GONE);

                //check_no1.setChecked(false);
                check_diarrea.setChecked(false);
                check_gastroenteritis.setChecked(false);
                check_amebiasis.setChecked(false);
                check_echericha.setChecked(false);
                check_colera.setChecked(false);
                check_otras.setChecked(false);
                texto_otros.setText(null);
            }
        });


        check_si2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si2.isChecked()){
                    check_no2.setChecked(false);
                }
                check_si2.setChecked(check_no2.isChecked() ? false: true);

                textPregunta15.setVisibility(check_si2.isChecked() ? View.VISIBLE : View.GONE);
                //check_no2.setChecked(false);
                check_si3.setChecked(false);
                check_no3.setChecked(false);
                check_hongos.setChecked(false);
                check_escabiosis.setChecked(false);
                check_alergias.setChecked(false);
                check_otra.setChecked(false);
                texto_diagnostico1.setText(null);
            }
        });

        check_si3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si3.isChecked()){
                    check_no3.setChecked(false);
                }
                check_si3.setChecked(check_no3.isChecked() ? false: true);
                textPregunta17.setVisibility(check_si3.isChecked() ? View.VISIBLE : View.GONE);
                //check_no3.setChecked(false);
                check_hongos.setChecked(false);
                check_escabiosis.setChecked(false);
                check_alergias.setChecked(false);
                check_otra.setChecked(false);
                texto_diagnostico1.setText(null);
            }
        });

        check_otra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_diagnostico1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_diagnostico1.setText(null);
            }
        });



        check_rio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_nombreRio.setText(null);
            }
        });

        check_otros2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta20.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                campo_otros2.setText(null);
            }
        });

        check_cisterna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta22.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta24.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_info19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta25.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                check_1.setChecked(false);
                check_2.setChecked(false);
                check_3.setChecked(false);
                check_4.setChecked(false);
                check_5.setChecked(false);
                check_6.setChecked(false);
                check_7.setChecked(false);



                check_semanal.setChecked(false);
                check_quincenal.setChecked(false);
                check_mensual.setChecked(false);
                check_trimestral.setChecked(false);
                check_semestral.setChecked(false);
                check_anual.setChecked(false);
                check_bienal.setChecked(false);
                check_nunca.setChecked(false);

                check_si4.setChecked(false);
                check_no4.setChecked(false);

                check_semanal1.setChecked(false);
                check_quincenal1.setChecked(false);
                check_mensual1.setChecked(false);
                check_bimensual1.setChecked(false);
                check_trimestral1.setChecked(false);
                check_cuatrimestral1.setChecked(false);
                check_semestral1.setChecked(false);
                check_anual1.setChecked(false);
                check_otras1.setChecked(false);
                texto_otros1.setText(null);

                check_op1.setChecked(false);
                check_op2.setChecked(false);
                check_op3.setChecked(false);
                check_op4.setChecked(false);
                check_op5.setChecked(false);
                check_op6.setChecked(false);
                check_op7.setChecked(false);
                check_op8.setChecked(false);
                check_otras2.setChecked(false);
                texto_otros2.setText(null);

            }
        });

        check_tanque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta23.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta24.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_info19.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta25.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                check_250.setChecked(false);
                check_500.setChecked(false);
                check_600.setChecked(false);
                check_1000.setChecked(false);
                check_1100.setChecked(false);
                check_2000.setChecked(false);
                check_2500.setChecked(false);

                check_semanal.setChecked(false);
                check_quincenal.setChecked(false);
                check_mensual.setChecked(false);
                check_trimestral.setChecked(false);
                check_semestral.setChecked(false);
                check_anual.setChecked(false);
                check_bienal.setChecked(false);
                check_nunca.setChecked(false);

                check_semanal1.setChecked(false);
                check_quincenal1.setChecked(false);
                check_mensual1.setChecked(false);
                check_bimensual1.setChecked(false);
                check_trimestral1.setChecked(false);
                check_cuatrimestral1.setChecked(false);
                check_semestral1.setChecked(false);
                check_anual1.setChecked(false);
                check_otras1.setChecked(false);
                texto_otros1.setText(null);

                check_op1.setChecked(false);
                check_op2.setChecked(false);
                check_op3.setChecked(false);
                check_op4.setChecked(false);
                check_op5.setChecked(false);
                check_op6.setChecked(false);
                check_op7.setChecked(false);
                check_op8.setChecked(false);
                check_otras2.setChecked(false);
                texto_otros2.setText(null);

            }
        });

        check_si4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_no4.setChecked(false);

                textPregunta26.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                textPregunta27.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                check_semanal1.setChecked(false);
                check_quincenal1.setChecked(false);
                check_mensual1.setChecked(false);
                check_bimensual1.setChecked(false);
                check_trimestral1.setChecked(false);
                check_cuatrimestral1.setChecked(false);
                check_semestral1.setChecked(false);
                check_anual1.setChecked(false);
                check_otras1.setChecked(false);
                texto_otros1.setText(null);


                check_op1.setChecked(false);
                check_op2.setChecked(false);
                check_op3.setChecked(false);
                check_op4.setChecked(false);
                check_op5.setChecked(false);
                check_op6.setChecked(false);
                check_op7.setChecked(false);
                check_op8.setChecked(false);
                check_otras2.setChecked(false);
                texto_otros2.setText(null);

            }
        });

        check_si5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si5.isChecked()){
                    check_no5.setChecked(false);
                }
                check_si5.setChecked(check_no5.isChecked() ? false: true);

                textPregunta30.setVisibility(check_si5.isChecked() ? View.VISIBLE : View.GONE);
                tratamiento.setText(null);
                //check_no5.setChecked(false);
            }
        });

        //En el caso de tener animales que consumen agua indicar:
        check_animales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta32.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                check_animal_mascota.setChecked(false);
                check_animal_consumo.setChecked(false);
                check_animal_venta.setChecked(false);
                text_animal_consumo.setText(null);
                text_animal_venta.setText(null);
            }
        });

        //animales para consumo
        check_animal_consumo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta33.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                text_animal_consumo.setText(null);
            }
        });

        //animales para venta
        check_animal_venta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta34.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                text_animal_venta.setText(null);
            }
        });
        //En el caso de tener sembrios que consumen agua indicar:
        check_riego.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta35.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                check_ornamentales.setChecked(false);
                check_sembrio_consumo.setChecked(false);
                check_sembrio_venta.setChecked(false);
                text_sembrios_consumo.setText(null);
                text_sembrios_venta.setText(null);
            }
        });
        //sembrios para consumo
        check_sembrio_consumo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta36.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                text_sembrios_consumo.setText(null);
            }
        });
        //sembrios para venta
        check_sembrio_venta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textPregunta37.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                text_sembrios_venta.setText(null);
            }
        });


        check_otras.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_otros.setText(null);
            }
        });


        check_otras1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros1.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_otros1.setText(null);
            }
        });

        check_otras2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                texto_otros2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                texto_otros2.setText(null);
            }
        });

        check_hombre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_mujer.setChecked(false);
            }
        });
        check_mujer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_hombre.setChecked(false);
            }
        });

        check_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(check_si.isChecked()==true){
                    check_si.setChecked(false);
                }
                //check_si.setChecked(false);
                check_si1.setChecked(false);
                check_no1.setChecked(false);
                check_diarrea.setChecked(false);
                check_gastroenteritis.setChecked(false);
                check_amebiasis.setChecked(false);
                check_echericha.setChecked(false);
                check_colera.setChecked(false);
                check_otras.setChecked(false);
                texto_otros.setText(null);

            }
        });
        check_no1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si1.isChecked()==true){
                    check_si1.setChecked(false);
                }
                //check_si1.setChecked(false);
                check_diarrea.setChecked(false);
                check_gastroenteritis.setChecked(false);
                check_amebiasis.setChecked(false);
                check_echericha.setChecked(false);
                check_colera.setChecked(false);
                check_otras.setChecked(false);
                texto_otros.setText(null);
            }
        });

        check_no2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si2.isChecked()==true){
                    check_si2.setChecked(false);
                }
                //check_si2.setChecked(false);
                check_si3.setChecked(false);
                check_no3.setChecked(false);
                check_hongos.setChecked(false);
                check_escabiosis.setChecked(false);
                check_alergias.setChecked(false);
                check_otra.setChecked(false);
                texto_diagnostico1.setText(null);
            }
        });

        check_no3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si3.isChecked()==true){
                    check_si3.setChecked(false);
                }
                //check_si3.setChecked(false);
                check_hongos.setChecked(false);
                check_escabiosis.setChecked(false);
                check_alergias.setChecked(false);
                check_otra.setChecked(false);
                texto_diagnostico1.setText(null);
            }
        });
        check_no4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_si4.setChecked(false);
            }
        });

        check_no5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(check_si5.isChecked()==true){
                    check_si5.setChecked(false);
                }
                //check_no5.setChecked(true);

            }
        });

        check_casa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_departamento.setChecked(false);
                check_cuarto.setChecked(false);
                check_otros.setChecked(false);
                //check_casa.setChecked(true);
            }
        });

        check_departamento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_casa.setChecked(false);
                check_cuarto.setChecked(false);
                check_otros.setChecked(false);
                //check_departamento.setChecked(true);
            }
        });

        check_cuarto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_casa.setChecked(false);
                check_departamento.setChecked(false);
                check_otros.setChecked(false);
                //check_cuarto.setChecked(true);
            }
        });

        check_uno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_dos.setChecked(false);
                check_tres.setChecked(false);
                check_cuatro.setChecked(false);
            }
        });

        check_dos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_uno.setChecked(false);
                check_tres.setChecked(false);
                check_cuatro.setChecked(false);
            }
        });

        check_tres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_dos.setChecked(false);
                check_uno.setChecked(false);
                check_cuatro.setChecked(false);
            }
        });

        check_cuatro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_dos.setChecked(false);
                check_tres.setChecked(false);
                check_uno.setChecked(false);
            }
        });

        check_propia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_rentada.setChecked(false);
                check_prestada.setChecked(false);
                check_otros1.setChecked(false);
            }
        });

        check_rentada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_propia.setChecked(false);
                check_prestada.setChecked(false);
                check_otros1.setChecked(false);
            }
        });

        check_prestada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check_propia.setChecked(false);
                check_rentada.setChecked(false);
                check_otros1.setChecked(false);
            }
        });


        encuestado_comunidad.setVisibility(View.GONE);

        llenarSpinnerComunidad();
        comunidad_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = comunidad_spinner.getSelectedItemPosition();
                String dato = comunidad_spinner.getSelectedItem().toString();
                encuestado_comunidad.setText(dato);
                boolean band = (dato.contains("Otro"));
                if(band) {
                    encuestado_comunidad.setText("");
                }
                encuestado_comunidad.setVisibility(band ? View.VISIBLE : View.GONE);
                llenarSpinnerBarrio(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        barrio_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dato = barrio_spinner.getSelectedItem().toString();
                encuestado_barrio.setText(dato);
                boolean band = (dato.contains("Otro")||dato.contains("N/A")||dato.contains("No tiene distribución barrial"));
                if(dato.contains("Otro"))
                    encuestado_barrio.setText("");
                encuestado_barrio.setVisibility(band ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        junta_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dato = junta_spinner.getSelectedItem().toString();
                encuestado_junta.setText(dato);
                boolean band = (dato.contains("Otro")||dato.contains("N/A")||dato.contains("No tienen junta de Agua"));
                if(dato.contains("Otro"))
                    encuestado_junta.setText("");
                encuestado_junta.setVisibility(band ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void llenarSpinnerComunidad(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opcionesComunidad, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.preference_category);
        // Apply the adapter to the spinner
                comunidad_spinner.setAdapter(adapter);
    }

    private void llenarSpinnerBarrio(int pos){
        String barriosAux[] = new String[0];
        switch (pos){
            case 0:
                barriosAux= new String[]{"Seleccione una comunidad previamente"};
                llenarSpinnerJunta(pos);
                break;
            case 1:
                barriosAux= new String[]{"Seleccione un barrio","San Carlos","Esmeraldas","6 de Septiembre","26 de Julio","2 de Agosto","Guadalupe","Mirador","San Pablo","Manantial","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 2:
            case 5:
            case 8:
            case 9:
            case 12:
            case 16:
            case 17:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
                barriosAux= new String[]{"N/A"};
                llenarSpinnerJunta(pos);
                break;
            case 3:
            case 4:
            case 11:
            case 13:
            case 15:
            case 18:
            case 25:
            case 30:
                barriosAux= new String[]{"No tiene distribución barrial"};
                llenarSpinnerJunta(pos);
                break;
            case 6:
                barriosAux= new String[]{"Seleccione un barrio","San Pablo","San José","San Camilo","Barrio Central","Los Naranjitos","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 7://san luis
                barriosAux= new String[]{"Seleccione un barrio","San José","La Ponderosa","Unidos Venceremos","Central","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 10:
                barriosAux= new String[]{"Seleccione un barrio","Unidos Venceremos","San José","Central","Mirador","Rafael Correa","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 14:
                barriosAux= new String[]{"Seleccione un barrio","Nueva Vida","Central","Unión y progreso","5 de marzo","13 de enero","El Arbolito","Nueva Esperanza","El Mirador","Ramal San Felipe","Ramal Cuenca","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 21:
                barriosAux= new String[]{"Seleccione un barrio","San Andres","Los Daniels","Mazaramudu","Los Hermanos","Otro"};
                llenarSpinnerJunta(pos);
                break;
            case 33:
                barriosAux= new String[]{"Otro"};
                llenarSpinnerJunta(pos);
                break;
        }

        List<String> barrios = new ArrayList<>();
        //barrios.add(0,"Seleccione un barrio");
        Collections.addAll(barrios,barriosAux);

        ArrayAdapter<CharSequence> adapter;
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,barrios);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.preference_category);
        // Apply the adapter to the spinner
        barrio_spinner.setAdapter(adapter);
    }

    private void llenarSpinnerJunta(int pos){
/*
        ArrayList<Object> juntasAgua = new ArrayList();
        ArrayList<String> strings = new ArrayList();
        strings.add("wsds");
        juntasAgua.add(strings);
        Log.d("hellow", juntasAgua.get(0).toString());
*/
        String juntasAux[] = new String[0];

        switch (pos){
            case 0:
                juntasAux= new String[]{"Seleccione un barrio previamente"};
                break;
            case 1:
                juntasAux= new String[]{"Seleccione una junta de agua","Puerto Limón","Otro"};
                break;
            case 2:
            case 5:
            case 8:
            case 9:
            case 12:
            case 16:
            case 17:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
                juntasAux= new String[]{"N/A"};
                break;
            case 3:
            case 4:
            case 11:
            case 13:
            case 14:
            case 15:
            case 18:
            case 30:
                juntasAux= new String[]{"No tienen junta de Agua"};
                break;
            case 6:
            case 7:
                juntasAux= new String[]{"Seleccione una junta de agua","San Luis","No tiene Junta de Agua","Otro"};
                break;
            case 10:
                juntasAux= new String[]{"Seleccione una junta de agua","Vicente Rocafuerte","Otro"};
                break;
            case 21:
                juntasAux= new String[]{"Seleccione una junta de agua","Comunidad Tsáchila El Naranjo","Otro"};
                break;
            case 25:
                juntasAux= new String[]{"Seleccione una junta de agua","Comunidad Tsáchila El Poste","No tienen Junta de Agua","Otro"};
                break;
            case 33:
                juntasAux= new String[]{"Otro"};
                break;
        }

        List<String> juntas = new ArrayList<>();
        //juntas.add(0,"Seleccione un barrio");
        Collections.addAll(juntas,juntasAux);

        ArrayAdapter<CharSequence> adapter;
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,juntas);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.preference_category);
        // Apply the adapter to the spinner
        junta_spinner.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 /*&& resultCode == RESULT_OK && data != null*/){

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPatch);
            viewFoto.setImageBitmap(bitmap);
            buttonFotoo.setText("ACTUALIZAR FOTO");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            // compress Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,stream);
            // Initialize byte array
            byte[] bytes=stream.toByteArray();
            // get base64 encoded string
            String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

            imagenString = sImage;

            //imageFile.deleteOnExit();
        }
    }

    public String pregunta1(){

        Boolean opc1= check_casa.isChecked();
        Boolean opc2= check_departamento.isChecked();
        Boolean opc3= check_cuarto.isChecked();
        Boolean opc4= check_otros.isChecked();

        String val1 = (opc1) ? check_casa.getText().toString()+", " : "";
        String val2 = (opc2) ? check_departamento.getText().toString()+", " : "";
        String val3 = (opc3) ? check_cuarto.getText().toString()+", " : "";
        String val4 = (opc4) ? check_otros.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;

    }
    public int pregunta2(){
        //check_uno,check_dos,check_tres,check_cuatro
        Boolean opc1= check_uno.isChecked();
        Boolean opc2= check_dos.isChecked();
        Boolean opc3= check_tres.isChecked();
        Boolean opc4= check_cuatro.isChecked();
        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=2;
        }if(opc3){
            val1=3;
        }if(opc4){
            val1=4;
        }
        return val1;
    }
    public String pregunta3(){
        Boolean opc1= check_cinc.isChecked();
        Boolean opc2= check_teja.isChecked();
        Boolean opc3= check_loseta.isChecked();
        Boolean opc4= check_loza.isChecked();

        String val1 = (opc1) ? check_cinc.getText().toString()+", " : "";
        String val2 = (opc2) ? check_teja.getText().toString()+", " : "";
        String val3 = (opc3) ? check_loseta.getText().toString()+", " : "";
        String val4 = (opc4) ? check_loza.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;


    }
    //paredes
    public String pregunta4(){
        Boolean opc1= check_madera.isChecked();
        Boolean opc2= check_bloque.isChecked();
        Boolean opc3= check_hormigon.isChecked();
        Boolean opc4= check_loza1.isChecked();

        String val1 = (opc1) ? check_madera.getText().toString()+", " : "";
        String val2 = (opc2) ? check_bloque.getText().toString()+", " : "";
        String val3 = (opc3) ? check_hormigon.getText().toString()+", " : "";
        String val4 = (opc4) ? check_loza1.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;


    }
    //piso
    public String pregunta5(){
        Boolean opc1= check_madera1.isChecked();
        Boolean opc2= check_hormigon1.isChecked();
        Boolean opc3= check_porcelanato.isChecked();
        Boolean opc4= check_tierra.isChecked();

        String val1 = (opc1) ? check_madera1.getText().toString()+", " : "";
        String val2 = (opc2) ? check_hormigon1.getText().toString()+", " : "";
        String val3 = (opc3) ? check_porcelanato.getText().toString()+", " : "";
        String val4 = (opc4) ? check_tierra.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;

    }
    public void generarCodigo(){
        double numero = 1000000 * Math.random();
        double numero2 = (int) (Math.random() * 9) + 1;
        int f1= Integer.valueOf((int) numero);
        int f2=Integer.valueOf((int) numero2);

        String codigo=f1+""+f2;
        encuestado_codigo.setText(codigo);
    }

    public String pregunta_nombre(){
        String nombreencuestado=encuestado_nombre.getText().toString().trim();
        return nombreencuestado;
    }
    public String pregunta_direccion(){
        String direncuestado=encuestado_direccion.getText().toString().trim();
        return direncuestado;
    }
    public String pregunta_comunidad(){
        String comencuestado=encuestado_comunidad.getText().toString().trim();
        return comencuestado;
    }
    public String pregunta_barrio(){
        String direncuestado=encuestado_barrio.getText().toString().trim();
        return direncuestado;
    }
    public String pregunta_junta(){
        String direncuestado=encuestado_junta.getText().toString().trim();
        return direncuestado;
    }
    public String pregunta_edad(){
        String edadencuestado=encuestado_edad.getText().toString().trim();
        return edadencuestado;
    }
    public String pregunta_sexo(){
        Boolean opc1= check_hombre.isChecked();
        Boolean opc2= check_mujer.isChecked();

        String val1=null;

        if(opc1){
            val1="Hombre";
        }if(opc2){
            val1="Mujer";
        }
        return val1;
    }

    //LA VIVIENDA QUE HABITA ES
    public String pregunta6(){

        Boolean opc1= check_propia.isChecked();
        Boolean opc2= check_rentada.isChecked();
        Boolean opc3= check_prestada.isChecked();
        Boolean opc4= check_otros1.isChecked();

        String val1=null;

        if(opc1){
            val1="Propia";
        }if(opc2){
            val1="Rentada";
        }if(opc3){
            val1="Prestada";
        }if(opc4){
            val1=campo_otros1.getText().toString().trim();
        }
        return val1;
    }

    //¿CUANTAS PERSONAS HABITAN LA VIVIENDA?
    public String pregunta7(){
        String numhabitantes=encuestado_vivienda.getText().toString().trim();
        return numhabitantes;
    }

    //aLGuN INTEGRANTE DE LA FAMILIA HA TENIDO PROBLEMAS ESTOMACALES
    public int pregunta8(){

        Boolean opc1= check_si.isChecked();
        Boolean opc2= check_no.isChecked();

        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=0;
        }
        return val1;
    }

    //¿Cual fue el diagnostico?
    public String pregunta9(){
        //check_diarrea,check_gastroenteritis,check_amebiasis,check_echericha,check_colera,check_otras
        Boolean opc1= check_diarrea.isChecked();
        Boolean opc2= check_gastroenteritis.isChecked();
        Boolean opc3= check_amebiasis.isChecked();
        Boolean opc4= check_echericha.isChecked();
        Boolean opc5= check_colera.isChecked();
        Boolean opc6= check_otras.isChecked();

        String val1 = (opc1) ? check_diarrea.getText().toString()+", " : "";
        String val2 = (opc2) ? check_gastroenteritis.getText().toString()+", " : "";
        String val3 = (opc3) ? check_amebiasis.getText().toString()+", " : "";
        String val4 = (opc4) ? check_echericha.getText().toString()+", " : "";
        String val5 = (opc5) ? check_colera.getText().toString()+", " : "";
        String val6 = (opc6) ? check_otras.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5+val6).replaceFirst("..$","");
        return respuesta;
    }

    //ALGuN INTEGRANTE DE LA FAMILIA HA TENIDO ENFERMEDADES DE LA PIEL
    public int pregunta10(){
        Boolean opc1= check_si2.isChecked();
        Boolean opc2= check_no2.isChecked();
        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=0;
        }
        return val1;
    }

    //¿Cuál fue el diagnóstico?
    public String pregunta11(){
        //check_hongos,check_escabiosis,check_alergias,check_otra
        Boolean opc1= check_hongos.isChecked();
        Boolean opc2= check_escabiosis.isChecked();
        Boolean opc3= check_alergias.isChecked();
        Boolean opc4= check_otra.isChecked();

        String val1 = (opc1) ? check_hongos.getText().toString()+", " : "";
        String val2 = (opc2) ? check_escabiosis.getText().toString()+", " : "";
        String val3 = (opc3) ? check_alergias.getText().toString()+", " : "";
        String val4 = (opc4) ? check_otra.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;
    }

    //de DONDE SE ABASTECE PARA EL AGUA DE CONSUMO
    public String pregunta12(){
        //check_potable,check_pozo,check_rio,check_lluvia,check_tanquero,check_embotellada,check_otros2
        Boolean opc1= check_potable.isChecked();
        Boolean opc2= check_pozo.isChecked();
        Boolean opc3= check_rio.isChecked();
        Boolean opc4= check_lluvia.isChecked();
        Boolean opc5= check_tanquero.isChecked();
        Boolean opc6= check_embotellada.isChecked();
        Boolean opc7= check_otros2.isChecked();

        String val1 = (opc1) ? check_potable.getText().toString()+", " : "";
        String val2 = (opc2) ? check_pozo.getText().toString()+", " : "";
        String val3 = (opc3) ? check_rio.getText().toString()+", " : "";
        String val4 = (opc4) ? check_lluvia.getText().toString()+", " : "";
        String val5 = (opc5) ? check_tanquero.getText().toString()+", " : "";
        String val6 = (opc6) ? check_embotellada.getText().toString()+", " : "";
        String val7 = (opc7) ? check_otros2.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5+val6+val7).replaceFirst("..$","");
        return respuesta;

    }

    //TIENE CISTERNA O TANQUE ELEVADO
    public String pregunta13(){
        //check_cisterna,check_tanque,check_ninguno
        Boolean opc1= check_cisterna.isChecked();
        Boolean opc2= check_tanque.isChecked();
        Boolean opc3= check_ninguno.isChecked();

        String val1 = (opc1) ? check_cisterna.getText().toString()+", " : "";
        String val2 = (opc2) ? check_tanque.getText().toString()+", " : "";
        String val3 = (opc3) ? check_ninguno.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3).replaceFirst("..$","");
        return respuesta;

    }


    //REGULARMENTE, EL AGUA DE USO DIARIO PARA SU HOGAR,  DESDE QUE LLEGA DE LA RED PUBLICA DE AGUA
    public String preguntaExtra(){
        Boolean opc1 = check_red_agua_llave.isChecked();
        Boolean opc2 = check_red_agua_cisterna_llave.isChecked();
        Boolean opc3 = check_red_agua_tanque_llave.isChecked();
        Boolean opc4 = check_red_agua_cisterna_tanque_llave.isChecked();
        Boolean opc5 = check_directo_pozo.isChecked();

        String val1 = (opc1) ? check_red_agua_llave.getText().toString()+", " : "";
        String val2 = (opc2) ? check_red_agua_cisterna_llave.getText().toString()+", " : "";
        String val3 = (opc3) ? check_red_agua_tanque_llave.getText().toString()+", " : "";
        String val4 = (opc4) ? check_red_agua_cisterna_tanque_llave.getText().toString()+", " : "";
        String val5 = (opc5) ? check_directo_pozo.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5).replaceFirst("..$","");
        return respuesta;

    }


    // Capacidad de cisterna (m3)
    public int pregunta14(){

        Boolean opc1= check_1.isChecked();
        Boolean opc2= check_2.isChecked();
        Boolean opc3= check_3.isChecked();
        Boolean opc4= check_4.isChecked();
        Boolean opc5= check_5.isChecked();
        Boolean opc6= check_6.isChecked();
        Boolean opc7= check_7.isChecked();

        int val1=0;

        if(opc1){
            val1=1;
        }if(opc2){
            val1=2;
        }if(opc3){
            val1=3;
        }if(opc4){
            val1=4;
        }if(opc5){
            val1=5;
        }if(opc6){
            val1=6;
        }if(opc7){
            val1=7;
        }
        return val1;
    }

    // Capacidad de tanque (litros)
    public int pregunta15(){

        Boolean opc1= check_250.isChecked();
        Boolean opc2= check_500.isChecked();
        Boolean opc3= check_600.isChecked();
        Boolean opc4= check_1000.isChecked();
        Boolean opc5= check_1100.isChecked();
        Boolean opc6= check_2000.isChecked();
        Boolean opc7= check_2500.isChecked();

        int val1=0;

        if(opc1){
            val1=250;
        }if(opc2){
            val1=500;
        }if(opc3){
            val1=600;
        }if(opc4){
            val1=1000;
        }if(opc5){
            val1=1100;
        }if(opc6){
            val1=2000;
        }if(opc7){
            val1=2500;
        }
        return val1;
    }

    //En caso de tener cisterna o tanque elevado indicar con qué frecuencia realiza la limpieza o lavado
    public String pregunta16(){
        Boolean opc1= check_semanal.isChecked();
        Boolean opc2= check_quincenal.isChecked();
        Boolean opc3= check_mensual.isChecked();
        Boolean opc4= check_trimestral.isChecked();
        Boolean opc5= check_semestral.isChecked();
        Boolean opc6= check_anual.isChecked();
        Boolean opc7= check_bienal.isChecked();
        Boolean opc8= check_nunca.isChecked();

        String val1 = (opc1) ? check_semanal.getText().toString()+", " : "";
        String val2 = (opc2) ? check_quincenal.getText().toString()+", " : "";
        String val3 = (opc3) ? check_mensual.getText().toString()+", " : "";
        String val4 = (opc4) ? check_trimestral.getText().toString()+", " : "";
        String val5 = (opc5) ? check_semestral.getText().toString()+", " : "";
        String val6 = (opc6) ? check_anual.getText().toString()+", " : "";
        String val7 = (opc7) ? check_bienal.getText().toString()+", " : "";
        String val8 = (opc8) ? check_nunca.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5+val6+val7+val8).replaceFirst("..$","");
        return respuesta;

    }

    //Con que frecuencia clora el tanque
    public String pregunta17(){
        Boolean opc1= check_semanal1.isChecked();
        Boolean opc2= check_quincenal1.isChecked();
        Boolean opc3= check_mensual1.isChecked();
        Boolean opc4= check_bimensual1.isChecked();
        Boolean opc5= check_trimestral1.isChecked();
        Boolean opc6= check_cuatrimestral1.isChecked();
        Boolean opc7= check_semestral1.isChecked();
        Boolean opc8= check_anual1.isChecked();
        Boolean opc9= check_otras1.isChecked();

        String val1 = (opc1) ? check_semanal1.getText().toString()+", " : "";
        String val2 = (opc2) ? check_quincenal1.getText().toString()+", " : "";
        String val3 = (opc3) ? check_mensual1.getText().toString()+", " : "";
        String val4 = (opc4) ? check_bimensual1.getText().toString()+", " : "";
        String val5 = (opc5) ? check_trimestral1.getText().toString()+", " : "";
        String val6 = (opc6) ? check_cuatrimestral1.getText().toString()+", " : "";
        String val7 = (opc7) ? check_semestral1.getText().toString()+", " : "";
        String val8 = (opc8) ? check_anual1.getText().toString()+", " : "";
        String val9 = (opc9) ? check_otras1.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5+val6+val7+val8+val9).replaceFirst("..$","");
        return respuesta;


    }

    //Cual es la dosificación utilizada:
    public String pregunta18(){
        Boolean opc1= check_op1.isChecked();
        Boolean opc2= check_op2.isChecked();
        Boolean opc3= check_op3.isChecked();
        Boolean opc4= check_op4.isChecked();
        Boolean opc5= check_op5.isChecked();
        Boolean opc6= check_op6.isChecked();
        Boolean opc7= check_op7.isChecked();
        Boolean opc8= check_op8.isChecked();
        Boolean opc9= check_otras2.isChecked();

        String val1 = (opc1) ? check_op1.getText().toString()+", " : "";
        String val2 = (opc2) ? check_op2.getText().toString()+", " : "";
        String val3 = (opc3) ? check_op3.getText().toString()+", " : "";
        String val4 = (opc4) ? check_op4.getText().toString()+", " : "";
        String val5 = (opc5) ? check_op5.getText().toString()+", " : "";
        String val6 = (opc6) ? check_op6.getText().toString()+", " : "";
        String val7 = (opc7) ? check_op7.getText().toString()+", " : "";
        String val8 = (opc8) ? check_op8.getText().toString()+", " : "";
        String val9 = (opc9) ? check_otras2.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5+val6+val7+val8+val9).replaceFirst("..$","");
        return respuesta;

    }

    //EL AGUA QUE USTED UTILIZA PARA BEBER ES
    public String pregunta19(){
        Boolean opc1= check_llave.isChecked();
        Boolean opc2= check_hervida.isChecked();
        Boolean opc3= check_filtrada.isChecked();
        Boolean opc4= check_embotellada1.isChecked();

        String val1 = (opc1) ? check_llave.getText().toString()+", " : "";
        String val2 = (opc2) ? check_hervida.getText().toString()+", " : "";
        String val3 = (opc3) ? check_filtrada.getText().toString()+", " : "";
        String val4 = (opc4) ? check_embotellada1.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;
    }

    //¿QUe USO LE DA AL AGUA?
    public String pregunta20(){
        Boolean opc1= check_cocina.isChecked();
        Boolean opc2= check_aseo.isChecked();
        Boolean opc3= check_lavado.isChecked();
        Boolean opc4= check_animales.isChecked();
        Boolean opc5= check_riego.isChecked();

        String val1 = (opc1) ? check_cocina.getText().toString()+", " : "";
        String val2 = (opc2) ? check_aseo.getText().toString()+", " : "";
        String val3 = (opc3) ? check_lavado.getText().toString()+", " : "";
        String val4 = (opc4) ? check_animales.getText().toString()+", " : "";
        String val5 = (opc5) ? check_riego.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4+val5).replaceFirst("..$","");
        return respuesta;
    }

    //En el caso de tener animales que consumen agua indicar estos son
    //MASCOTAS
    public int pregunta21(){

        Boolean opc1= check_animal_mascota.isChecked();

        int val1;

        if(opc1){
            val1=1;
        }else{
            val1=0;
        }
        return val1;
    }

    //ORNAMENTALES
    public int pregunta22(){

        Boolean opc1= check_ornamentales.isChecked();

        int val1;

        if(opc1){
            val1=1;
        }else{
            val1=0;
        }
        return val1;
    }


    public boolean comprobarllenado(){

        
        if(pregunta1()==""||pregunta2()==0||pregunta3()==""||pregunta4()==""||pregunta5()==""){
            return true;
        }if(pregunta_nombre().isEmpty()||pregunta_direccion().isEmpty()||pregunta_edad().isEmpty()|| pregunta_sexo()==null){
            return true;
        }if(pregunta6()==null||pregunta7().isEmpty()||pregunta12()==""||pregunta13()==""||pregunta19()==""||pregunta20()==""){
            return true;
        }
        else{return false;}

    }


    //Para obtener ubicacion

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MiEncuesta.Localizacion Local = new MiEncuesta.Localizacion();
        Local.setUbicacionActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        latitud.setText("Localización agregada");
        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MiEncuesta miEncuesta;
        public  MiEncuesta getUbicacionActivity() {
            return miEncuesta;
        }
        public void setUbicacionActivity(MiEncuesta miEncuesta) {
            this.miEncuesta = miEncuesta;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String sLatitud = String.valueOf(loc.getLatitude());
            String sLongitud = String.valueOf(loc.getLongitude());
            latitud.setText(sLatitud);
            longitud.setText(sLongitud);
            miEncuesta.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            latitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            latitud.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void fechayhora() {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha = formatoFecha.format(date);
        String Hora = formatoHora.format(date);
        TituloFecha.setText(fecha);
        HoraInicio.setText(Hora);
    }

    public String horaFinal() {

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        String Hora = formatoHora.format(date);
        return Hora;
    }

    ///CONEXION CON LA DBB Y ENVÍO DE DATOS

    private void loadNames() {
        names.clear();
        Cursor cursor = db.obtenerEnc1BDD();
        if (cursor.moveToFirst()) {
            do {
                Name1 name = new Name1(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter1(this, R.layout.names, names);

    }

    private void loadNames3() {
        names3.clear();
        Cursor cursor = db.obtenerPersonasEncuestadasBDD();
        if (cursor.moveToFirst()) {
            do {
                Name3 name = new Name3(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(db.c_DIRECCION)),
                        cursor.getString(cursor.getColumnIndex(db.c_COMUNIDAD)),
                        cursor.getString(cursor.getColumnIndex(db.c_BARRIO)),
                        cursor.getString(cursor.getColumnIndex(db.c_JUNTA)),
                        cursor.getString(cursor.getColumnIndex(db.c_SEXO)),
                        cursor.getString(cursor.getColumnIndex(db.c_EDAD)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LO)),
                        cursor.getString(cursor.getColumnIndex(db.c_UTM_LA)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names3.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter3 = new NameAdapter3(this, R.layout.names3, names3);

    }



    private void saveNameToLocalStorage(String codigo_encuestador,
                                         String codigo_per,
                                         String fecha,
                                         String horaInicio,
                                         String horaFin,
                                         String foto,
                                         String tipoVivienda,
                                         String otroTipoVivienda,
                                         String numeroPisos,
                                         String techo,
                                         String paredes,
                                         String piso,
                                         String vivienda,
                                         String numeroPersonas,
                                         String problemasEstomacales,
                                         String tipoProblemasEstomacales,
                                         String otroProblemasEstomacales,
                                         String enfermedadPiel,
                                         String tipoEnfermedadPiel,
                                         String otraEnfermedadPiel,
                                         String abastecimientoAgua,
                                         String nombreRio,
                                         String otroAbastecimientoAgua,
                                         String sisternaTanque,
                                         String desdeDondeLlegaAgua,
                                         String origenAguaBeber,
                                         String tratamientoOrigenAgua,
                                         String usoAgua,
                                         String capacidadTanque,
                                         String capacidadSisterna,
                                         String frecuenciaLimpieza,
                                         String frecuenciaCloracion,
                                         String otroFrecuenciaCloracion,
                                         String dosisCloracion,
                                         String otroDosisCloracion,
                                         String mascotas_animal,
                                         String consumo_animal,
                                         String venta_animal,
                                         String ornamentales_riego,
                                         String consumo_riego,
                                         String venta_riego,
                                         String cantHombres,
                                         String cantMujeres,
                                         String cantNinos,
                                         String cantDiscapacidad,

                                        String nombre_persona,
                                        String dir_persona,
                                        String comu_persona,
                                        String barr_persona,
                                        String junta_persona,
                                        String edad_persona,
                                        String sexo_persona,
                                        String longitud_persona,
                                        String latitud_persona,

                                         int status
                                         ) {

        db.guardaEnc1BDD(codigo_encuestador,codigo_per, fecha, horaInicio, horaFin, foto, tipoVivienda, otroTipoVivienda, numeroPisos, techo, paredes, piso, vivienda,
                numeroPersonas, problemasEstomacales, tipoProblemasEstomacales, otroProblemasEstomacales, enfermedadPiel, tipoEnfermedadPiel,
                otraEnfermedadPiel, abastecimientoAgua, nombreRio, otroAbastecimientoAgua, sisternaTanque, desdeDondeLlegaAgua, origenAguaBeber, tratamientoOrigenAgua,
                usoAgua, capacidadTanque, capacidadSisterna, frecuenciaLimpieza, frecuenciaCloracion, otroFrecuenciaCloracion, dosisCloracion,
                otroDosisCloracion, mascotas_animal, consumo_animal, venta_animal, ornamentales_riego, consumo_riego, venta_riego,cantHombres,cantMujeres,cantNinos,cantDiscapacidad,
                nombre_persona, dir_persona, comu_persona, barr_persona, junta_persona, edad_persona, sexo_persona, longitud_persona, latitud_persona, status);


        Name1 n = new Name1(codigo_per, horaFin, status);
        names.add(n);

        Toast.makeText(this,"Encuesta  agregada ",Toast.LENGTH_SHORT).show();
        //refreshList();

    }



    private void saveNameToLocalStorage3(String codigo_persona,
                                        String nombre_persona,
                                        String dir_persona,
                                        String comu_persona,
                                        String barr_persona,
                                        String junta_persona,
                                        String edad_persona,
                                        String sexo_persona,
                                        String longitud_persona,
                                        String latitud_persona,
                                        int status_persona) {
        db.guardaPersonaEncuestadaBDD(codigo_persona, nombre_persona, dir_persona, comu_persona, barr_persona, junta_persona, edad_persona, sexo_persona, longitud_persona, latitud_persona, status_persona);


        Name3 n = new Name3(codigo_persona, nombre_persona,dir_persona, comu_persona, barr_persona, junta_persona,  edad_persona, sexo_persona, longitud_persona, latitud_persona, status_persona);
        names3.add(n);


        //refreshList();
    }

    private void saveNameToServer() {
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando los datos");
        progressDialog.show();*/

        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        //String usuario_nombre=preferences.getString("usuario_nombre", null);
        String usuario_codigo=preferences.getString("usuario_codigo", null);

        final String codigo_encuestador = usuario_codigo;
        final String fecha = TituloFecha.getText().toString().trim();
        final String horaInicio = HoraInicio.getText().toString().trim();
        final String horaFin = this.horaFinal();



        final String tipoVivienda=this.pregunta1();
        final String otroTipoVivienda=campo_otros.getText().toString().trim();
        final String numeroPisos=String.valueOf(pregunta2());
        final String techo=this.pregunta3();
        final String paredes=this.pregunta4();
        final String piso=this.pregunta5();
        final String vivienda=this.pregunta6();
        final String numeroPersonas=this.pregunta7();
        final String problemasEstomacales=String.valueOf(pregunta8());
        final String tipoProblemasEstomacales=this.pregunta9();
        final String otroProblemasEstomacales=texto_otros.getText().toString().trim();
        final String enfermedadPiel=String.valueOf(pregunta10());
        final String tipoEnfermedadPiel=this.pregunta11();
        final String otraEnfermedadPiel=texto_diagnostico1.getText().toString().trim();
        final String abastecimientoAgua=this.pregunta12();
        final String nombreRio=texto_nombreRio.getText().toString().trim();
        final String otroAbastecimientoAgua=campo_otros2.getText().toString().trim();
        final String sisternaTanque=this.pregunta13();
        final String desdeDondeLlegaAgua=this.preguntaExtra();
        final String origenAguaBeber=this.pregunta19();
        final String tratamientoOrigenAgua=tratamiento.getText().toString().trim();
        final String usoAgua=this.pregunta20();
        final String capacidadTanque=String.valueOf(pregunta15());
        final String capacidadSisterna=String.valueOf(pregunta14());
        final String frecuenciaLimpieza=this.pregunta16();
        final String frecuenciaCloracion=this.pregunta17();
        final String otroFrecuenciaCloracion=texto_otros1.getText().toString().trim();
        final String dosisCloracion=this.pregunta18();
        final String otroDosisCloracion=texto_otros2.getText().toString();
        final String mascotas_animal=String.valueOf(pregunta21());
        final String consumo_animal=text_animal_consumo.getText().toString().trim();
        final String venta_animal=text_animal_venta.getText().toString().trim();
        final String ornamentales_riego=String.valueOf(pregunta22());
        final String consumo_riego=text_sembrios_consumo.getText().toString().trim();
        final String venta_riego=text_sembrios_venta.getText().toString().trim();
        final String foto = imagenString;
        //final String codigo_persona=encuestado_codigo.getText().toString().trim();
        final String cantHombres = cantidadHombres.getText().toString().trim();
        final String cantMujeres = cantidadMujeres.getText().toString().trim();
        final String cantNinos = cantidadNinos.getText().toString().trim();
        final String cantDiscapacidad = cantidadDiscapacidad.getText().toString().trim();

        final String codigo_persona=encuestado_codigo.getText().toString().trim();
        final String nombre_persona=pregunta_nombre();
        final String dir_persona=pregunta_direccion();
        final String comu_persona = pregunta_comunidad();
        final String barr_persona = pregunta_barrio();
        final String junta_persona = pregunta_junta();
        final String edad_persona=pregunta_edad();
        final String sexo_persona=pregunta_sexo();

        final String Longitud = longitud.getText().toString().trim();
        final String Latitud = latitud.getText().toString().trim();



        saveNameToLocalStorage(codigo_encuestador, codigo_persona, fecha, horaInicio, horaFin, foto, tipoVivienda, otroTipoVivienda, numeroPisos, techo, paredes, piso, vivienda,
                numeroPersonas, problemasEstomacales, tipoProblemasEstomacales, otroProblemasEstomacales, enfermedadPiel, tipoEnfermedadPiel,
                otraEnfermedadPiel, abastecimientoAgua, nombreRio, otroAbastecimientoAgua, sisternaTanque,desdeDondeLlegaAgua, origenAguaBeber, tratamientoOrigenAgua,
                usoAgua, capacidadTanque, capacidadSisterna, frecuenciaLimpieza, frecuenciaCloracion, otroFrecuenciaCloracion, dosisCloracion,
                otroDosisCloracion, mascotas_animal, consumo_animal, venta_animal, ornamentales_riego, consumo_riego, venta_riego,cantHombres,cantMujeres,cantNinos,cantDiscapacidad,
                nombre_persona, dir_persona, comu_persona, barr_persona, junta_persona, edad_persona, sexo_persona, Longitud, Latitud,
                NAME_NOT_SYNCED_WITH_SERVER
                );



    }

    private void saveNameToServer3() {
       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Estableciendo conexión");
        progressDialog.show();*/



        final String codigo_persona=encuestado_codigo.getText().toString().trim();
        final String nombre_persona=pregunta_nombre();
        final String dir_persona=pregunta_direccion();
        final String comu_persona = pregunta_comunidad();
        final String barr_persona = pregunta_barrio();
        final String junta_persona = pregunta_junta();
        final String edad_persona=pregunta_edad();
        final String sexo_persona=pregunta_sexo();

        final String Longitud = longitud.getText().toString().trim();
        final String Latitud = latitud.getText().toString().trim();

        saveNameToLocalStorage3(codigo_persona, nombre_persona, dir_persona, comu_persona, barr_persona, junta_persona, edad_persona, sexo_persona, Longitud, Latitud, NAME_NOT_SYNCED_WITH_SERVER);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seccion2:
                if(comprobarllenado()){
                    Toast.makeText(this, "Existen campos sin llenar", Toast.LENGTH_SHORT).show();
                }else{
                    saveNameToServer();
                    //saveNameToServer3();
                    finish();
                }
                break;
        }
    }
}