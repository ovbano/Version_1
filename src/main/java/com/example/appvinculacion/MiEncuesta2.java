package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MiEncuesta2 extends AppCompatActivity implements View.OnClickListener{

    private Spinner comunidad_spinner;
    private Spinner barrio_spinner;
    private Spinner junta_spinner;

    private EditText encuestado_junta,encuestado_barrio,encuestado_comunidad;

    //private CheckBox check_cisterna,check_tanque,check_pozo,check_rio;
    private CheckBox check_turbia,check_solidos,check_coloracion,check_olor;
    private EditText texto_diagnostico;
    private TextView etHora;

    private EditText encuestado_nombre, lugar_muestra;

    private EditText cloroLibreResidual, medidapH, monocloroaminas, conductividad;

    MyDbHelper conn;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    //Widgets

    Button ibObtenerHora;


    //Mostrar nombre y codigo del encuestador
    private SharedPreferences preferences;

    private TextView TituloFecha,HoraInicio;
    private TextView latitud,longitud;
    private TextView direccion;
    private TextView encuesta_codigo;

    private MetodosBDD db;
    //View objects
    private Button buttonSave;
    private ListView listViewNames;
    private List<Name2> names;
    private BroadcastReceiver broadcastReceiver;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NameAdapter2 nameAdapter;
    public static final String URL_SAVE_NAME = "http://192.188.58.82:3000/encuesta2";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_encuesta2);

        registerReceiver(new NetworkStateChecker2(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        db = new MetodosBDD(this);
        names = new ArrayList<>();
        buttonSave = (Button) findViewById(R.id.buttonSave);
        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio = (TextView) findViewById(R.id.HoraInicio);

        conn=new MyDbHelper(getApplicationContext(), MetodosBDD.DB_NAME,null,1);
        encuestado_nombre=(EditText) findViewById(R.id.encuestado_nombre);
        encuesta_codigo=(TextView) findViewById(R.id.encuesta_codigo);
        lugar_muestra=(EditText) findViewById(R.id.lugar_muestra);

        cloroLibreResidual=(EditText) findViewById(R.id.cloroLibreResidual);
        medidapH=(EditText) findViewById(R.id.pH);
        monocloroaminas=(EditText) findViewById(R.id.monocloroaminas);
        conductividad=(EditText) findViewById(R.id.conductividad);

        init();

        generarCodigo();

        initFechahora();
        buttonSave.setOnClickListener(this);

        loadNames();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //loading the names again
                loadNames();
            }
        };
        //registrar el receptor de transmisión para actualizar el estado de sincronización
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }else {
            locationStart();
        }

    }

    private void init() {

        /*check_cisterna=(CheckBox) findViewById(R.id.check_cisterna);
        check_tanque=(CheckBox) findViewById(R.id.check_tanque);
        check_pozo=(CheckBox) findViewById(R.id.check_pozo);
        check_rio=(CheckBox) findViewById(R.id.check_rio);*/

        comunidad_spinner = (Spinner) findViewById(R.id.comunidad_spinner);
        barrio_spinner = (Spinner) findViewById(R.id.barrio_spinner);
        junta_spinner = (Spinner) findViewById(R.id.junta_spinner);

        encuestado_comunidad=(EditText) findViewById(R.id.encuestado_comunidad);
        encuestado_barrio=(EditText) findViewById(R.id.encuestado_barrio);
        encuestado_junta=(EditText) findViewById(R.id.encuestado_junta);

        check_turbia=(CheckBox) findViewById(R.id.check_turbia);
        check_solidos=(CheckBox) findViewById(R.id.check_solidos);
        check_coloracion=(CheckBox) findViewById(R.id.check_coloracion);
        check_olor=(CheckBox) findViewById(R.id.check_olor);


        texto_diagnostico=(EditText) findViewById(R.id.texto_diagnostico);

        latitud = (TextView) findViewById(R.id.txtLatitud);
        longitud = (TextView) findViewById(R.id.txtLongitud);
        direccion = (TextView) findViewById(R.id.txtDireccion);





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

    /*public String pregunta1(){

        Boolean opc1= check_cisterna.isChecked();
        Boolean opc2= check_tanque.isChecked();
        Boolean opc3= check_pozo.isChecked();
        Boolean opc4= check_rio.isChecked();

        int val1 = (opc1) ? 1 : 0;
        int val2 = (opc2) ? 1 : 0;
        int val3 = (opc3) ? 1 : 0;
        int val4 = (opc4) ? 1 : 0;

        if(val1==0&&val2==0&&val3==0&&val4==0){
            return "";
        }else{
            return val1+""+val2+""+val3+""+val4;
        }

    }*/

    public String pregunta2(){

        Boolean opc1= check_turbia.isChecked();
        Boolean opc2= check_solidos.isChecked();
        Boolean opc3= check_coloracion.isChecked();
        Boolean opc4= check_olor.isChecked();

        String val1 = (opc1) ? check_turbia.getText().toString()+", " : "";
        String val2 = (opc2) ? check_solidos.getText().toString()+", " : "";
        String val3 = (opc3) ? check_coloracion.getText().toString()+", " : "";
        String val4 = (opc4) ? check_olor.getText().toString()+", " : "";

        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");
        return respuesta;
    }
    public String codigoVinculacion(){
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        String usuario_codigo=preferences.getString("usuario_codigo", null);
        return usuario_codigo;
    }

    public String codigoEncuestador(){
        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        String usuario_codigo=preferences.getString("usuario_codigo", null);
        return usuario_codigo;
    }
    public String preguntaHoraMuestra(){
        String muestra=etHora.getText().toString().trim();
        return muestra;
    }

    public String pregunta_nombre(){
        String nombreencuestado=encuestado_nombre.getText().toString().trim();
        return nombreencuestado;
    }

    public String pregunta_lugar(){
        String lugarmuestra=lugar_muestra.getText().toString().trim();
        return lugarmuestra;
    }

    public String pregunta_cloroLibreResidual(){
        String clorolibreresidual=cloroLibreResidual.getText().toString().trim();
        return clorolibreresidual;
    }
    public String pregunta_pH(){
        String medidaph=medidapH.getText().toString().trim();
        return medidaph;
    }

    public String pregunta_monocloroaminas(){
        String medidamonocloroaminas=monocloroaminas.getText().toString().trim();
        return medidamonocloroaminas;
    }

    public String pregunta_conductividad(){
        String medidaconductividad=conductividad.getText().toString().trim();
        return medidaconductividad;
    }



    public boolean comprobarllenado(){

        if(pregunta_lugar().isEmpty() || pregunta2()==""){
            return true;
        }if(codigoVinculacion().isEmpty()||codigoEncuestador().isEmpty()||preguntaHoraMuestra().isEmpty()){
            return true;
        }if(pregunta_nombre().isEmpty() || pregunta_cloroLibreResidual().isEmpty() || pregunta_pH().isEmpty()){
            return true;
        }
        else{return false;}

    }

//Para obtener ubicacion

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MiEncuesta2.Localizacion Local = new MiEncuesta2.Localizacion();
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

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MiEncuesta2 miEncuesta2;
        public  MiEncuesta2 getUbicacionActivity() {
            return miEncuesta2;
        }
        public void setUbicacionActivity(MiEncuesta2 miEncuesta2) {
            this.miEncuesta2 = miEncuesta2;
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
            miEncuesta2.setLocation(loc);
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


    //Para obtener ubicacion
    private void initFechahora(){
        TituloFecha = (TextView) findViewById(R.id.TituloFecha);
        HoraInicio= (TextView) findViewById(R.id.HoraInicio);
        fechayhora();
        etHora = (TextView) findViewById(R.id.et_mostrar_hora_picker);
        ibObtenerHora = (Button) findViewById(R.id.ib_obtener_hora);
        ibObtenerHora.setOnClickListener(this);
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

    public void generarCodigo(){
        double numero = 1000000 * Math.random();
        double numero2 = (int) (Math.random() * 9) + 1;
        int f1= Integer.valueOf((int) numero);
        int f2=Integer.valueOf((int) numero2);

        String codigo=f1+""+f2;
        encuesta_codigo.setText(codigo);
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario


                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }


    //fecha y hora
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

    private void loadNames() {
        names.clear();
        Cursor cursor = db.obtenerEnc2BDD();
        if (cursor.moveToFirst()) {
            do {
                Name2 name = new Name2(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                        cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter2(this, R.layout.names, names);

    }

    private void saveNameToServer() {
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando en el servidor...");
        progressDialog.show();*/

        preferences = getSharedPreferences("Preferences",MODE_PRIVATE);

        String usuario_codigo=preferences.getString("usuario_codigo", null);

        final String codigo = usuario_codigo;
        //final String codigoPersona=codigoPersona();
        //final String codigoEncuesta=codigoVinculacion();
        final String codigoEncuesta=encuesta_codigo.getText().toString().trim();
        final String fecha = TituloFecha.getText().toString().trim();
        final String horaInicio = HoraInicio.getText().toString().trim();;
        final String horaFin = horaFinal().trim();
        final String horaMuestra=preguntaHoraMuestra().trim();
        final String Longitud = longitud.getText().toString().trim();
        final String Latitud = latitud.getText().toString().trim();
        final String nombre_persona=pregunta_nombre().trim();
        final String direccion_persona=direccion.getText().toString().trim();
        final String comunidad= encuestado_comunidad.getText().toString().trim();
        final String barrio= encuestado_barrio.getText().toString().trim();
        final String junta_agua= encuestado_junta.getText().toString().trim();
        final String lugar=pregunta_lugar().trim();
        final String aspecto=pregunta2().trim();
        final String observaciones=texto_diagnostico.getText().toString().trim();
        final String cloroLibreResidual=pregunta_cloroLibreResidual().trim();
        final String pH=pregunta_pH().trim();
        final String monocloroaminas=pregunta_monocloroaminas().trim();
        final String conductividad=pregunta_conductividad().trim();


        saveNameToLocalStorage(
                                        codigo,
                                        codigoEncuesta,
                                        fecha,
                                        horaInicio,
                                        horaFin,
                                        horaMuestra,
                                        lugar,
                                        aspecto,
                                        observaciones,
                                        nombre_persona,
                                        Longitud,
                                        Latitud,
                                        direccion_persona,
                                        comunidad,
                                        barrio,
                                        junta_agua,
                                        cloroLibreResidual,
                                        pH,
                                        monocloroaminas,
                                        conductividad,
                                        NAME_NOT_SYNCED_WITH_SERVER
        );
    }
             private void saveNameToLocalStorage(
                                        String codigo,
                                        String codigoEncuesta,
                                        String fecha,
                                        String horaInicio,
                                        String horaFin,
                                        String horaMuestra,
                                        String lugar,
                                        String aspecto,
                                        String observaciones,
                                        String nombre_persona,
                                        String Longitud,
                                        String Latitud,
                                        String direccion_persona,
                                        String comunidad,
                                        String barrio,
                                        String junta_agua,
                                        String cloroLibreResidual,
                                        String pH,
                                        String monocloroaminas,
                                        String conductividad,
                                        int status) {

        db.guardaEnc2BDD(codigo,
                codigoEncuesta,
                fecha, horaInicio,
                horaFin,
                horaMuestra,
                lugar,
                aspecto,
                observaciones,
                nombre_persona,
                Longitud,
                Latitud,
                direccion_persona,
                comunidad,
                barrio,
                junta_agua,
                cloroLibreResidual,
                pH,
                monocloroaminas,
                conductividad,
                status);
        Log.d("success",codigo+" el codogo");
        Log.d("success",codigoEncuesta+" el codogo enc");

        Name2 n = new Name2(codigo, fecha, horaFin, status);
        names.add(n);

        Toast.makeText(this,"Encuesta agregada ",Toast.LENGTH_SHORT).show();
        //refreshList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
            case R.id.buttonSave:
                if(comprobarllenado()){
                    Toast.makeText(this, "Existen campos sin llenar", Toast.LENGTH_SHORT).show();
                }else{
                    saveNameToServer();
                    finish();
                }
                break;
        }
    }
}