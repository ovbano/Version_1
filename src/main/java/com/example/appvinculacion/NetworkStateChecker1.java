package com.example.appvinculacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class NetworkStateChecker1 extends BroadcastReceiver {
    private Context context;
    private MetodosBDD db;

    private String URL_SAVE_NAME = "http://192.188.58.82:3000/encuesta1";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        db = new MetodosBDD(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // si hay una red
        if (activeNetwork != null) {
            /// Si está conectado a wifi o plan de datos móviles
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                // obtener todas las encuestas no sincronizadas
                Cursor cursor = db.obtenerDesincronizadoEnc1BDD();

                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                String ssid = null;
                if (connectionInfo != null) {
                    ssid = connectionInfo.getSSID();
                }
                if(ssid.contains("ESPE") || ssid.contains("INVITADOS") || ssid.contains("eduroam")  ){
                    URL_SAVE_NAME = "http://10.3.0.251:3000/encuesta1";
                }

                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        guardarEnc1Servidor(
                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGOENCUESTA)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO_PERSONA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAINICIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                                cursor.getString(cursor.getColumnIndex(db.c_FOTO)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOVIVIENDA )),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROTIPOVIVIENDA)),
                                cursor.getString(cursor.getColumnIndex(db.c_NUMEROPISOS)),
                                cursor.getString(cursor.getColumnIndex(db.c_TECHO)),
                                cursor.getString(cursor.getColumnIndex(db.c_PAREDES)),
                                cursor.getString(cursor.getColumnIndex(db.c_PISO)),
                                cursor.getString(cursor.getColumnIndex(db.c_VIVIENDA)),
                                cursor.getString(cursor.getColumnIndex(db.c_NUMEROPERSONAS)),
                                cursor.getString(cursor.getColumnIndex(db.c_PROBLEMASESTOMACALES)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOPROBLEMASESTOMACALES)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROTIPOPROBLEMASESTOMACALES)),
                                cursor.getString(cursor.getColumnIndex(db.c_ENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_TIPOENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTRAENFERMEDADPIEL)),
                                cursor.getString(cursor.getColumnIndex(db.c_ABASTECIMIENTOAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_NOMBRERIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROABASTECIMIENTOAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_SISTERNATANQUE)),
                                cursor.getString(cursor.getColumnIndex(db.c_DESDEDONDELLEGAAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_ORIGENAGUA_BEBER)),
                                cursor.getString(cursor.getColumnIndex(db.c_TRATAMIENTOORIGENAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_USOAGUA)),
                                cursor.getString(cursor.getColumnIndex(db.c_CAPACIDADTANQUE)),
                                cursor.getString(cursor.getColumnIndex(db.c_CAPACIDADSISTERNA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FRECUENCIALIMPIEZA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FRECUENCIACLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTROFRECUENCIACLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_DOSISCLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_OTRODOSISCLORACION)),
                                cursor.getString(cursor.getColumnIndex(db.c_MASCOTASANIMAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_CONSUMOSANIMAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_VENTAANIMAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_ORNAMENTALESRIEGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CONSUMORIEGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_VENTARIEGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CANTIDADHOMBRES)),
                                cursor.getString(cursor.getColumnIndex(db.c_CANTIDADMUJERES)),
                                cursor.getString(cursor.getColumnIndex(db.c_CANTIDADNINOS)),
                                cursor.getString(cursor.getColumnIndex(db.c_CANTIDADDISCAPACIDAD)),
                                cursor.getString(cursor.getColumnIndex(db.c_NOMBRE)),
                                cursor.getString(cursor.getColumnIndex(db.c_DIRECCION)),
                                cursor.getString(cursor.getColumnIndex(db.c_COMUNIDAD)),
                                cursor.getString(cursor.getColumnIndex(db.c_BARRIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_JUNTA)),
                                cursor.getString(cursor.getColumnIndex(db.c_EDAD)),
                                cursor.getString(cursor.getColumnIndex(db.c_SEXO)),
                                cursor.getString(cursor.getColumnIndex(db.c_UTM_LO)),
                                cursor.getString(cursor.getColumnIndex(db.c_UTM_LA))

                        );
                        Log.d("success", cursor.getString(cursor.getColumnIndex(db.COLUMN_ID)));
                    } while (cursor.moveToNext());
                }
            }
        }

    }
    private void guardarEnc1Servidor(
            final int id,
            final String codigo_encuesta,
            final String codigo_encuestador,
            final String codigo_per,
            final String fecha,
            final String horaInicio,
            final String horaFin,
            final String foto,
            final String tipoVivienda,
            final String otroTipoVivienda,
            final String numeroPisos,
            final String techo,
            final String paredes,
            final String piso,
            final String vivienda,
            final String numeroPersonas,
            final String problemasEstomacales,
            final String tipoProblemasEstomacales,
            final String otroProblemasEstomacales,
            final String enfermedadPiel,
            final String tipoEnfermedadPiel,
            final String otraEnfermedadPiel,
            final String abastecimientoAgua,
            final String nombreRio,
            final String otroAbastecimientoAgua,
            final String sisternaTanque,
            final String desdeDondeLlegaAgua,
            final String origenAguaBeber,
            final String tratamientoOrigenAgua,
            final String usoAgua,
            final String capacidadTanque,
            final String capacidadSisterna,
            final String frecuenciaLimpieza,
            final String frecuenciaCloracion,
            final String otroFrecuenciaCloracion,
            final String dosisCloracion,
            final String otroDosisCloracion,
            final  String mascotas_animal,
            final String consumo_animal,
            final String venta_animal,
            final  String ornamentales_riego,
            final  String consumo_riego,
            final String venta_riego,
            final String cantHombres,
            final String cantMujeres,
            final String cantNinos,
            final String cantDiscapacidad,
            final String nombre_persona,
            final String dir_persona,
            final String com_persona,
            final String barr_persona,
            final String jun_ag_persona,
            final String edad_persona,
            final String sexo_persona,
            final String longitud_persona,
            final String latitud_persona
    ) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String obj2 = new JSONObject(response).getString("status");
                            Log.d("success","respuesta enc1");

                            if("encuesta1_c".equals(obj2)){
                                // actualizando el estado en sqlite
                                Log.d("success","envia enc1");
                                db.actualizarEstadoEnc1BDD(id,MainActivity1.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity1.DATA_SAVED_BROADCAST));
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected  HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("codigo_encuesta",codigo_encuesta);
                params.put("codigo_encuestador",codigo_encuestador);
                params.put("codigo_per",codigo_per);
                params.put("fecha",fecha);
                params.put("horaInicio",horaInicio);
                params.put("horaFin",horaFin);
                params.put("foto",foto);
                params.put("tipoVivienda",tipoVivienda);
                params.put("otroTipoVivienda",otroTipoVivienda);
                params.put("numeroPisos",numeroPisos);
                params.put("techo",techo);
                params.put("paredes",paredes );
                params.put("piso",piso );
                params.put("vivienda",vivienda);
                params.put("numeroPersonas",numeroPersonas);
                params.put("problemasEstomacales",problemasEstomacales);
                params.put("tipoProblemasEstomacales",tipoProblemasEstomacales);
                params.put("otroProblemasEstomacales",otroProblemasEstomacales);
                params.put("enfermedadPiel",enfermedadPiel);
                params.put("tipoEnfermedadPiel",tipoEnfermedadPiel );
                params.put("otraEnfermedadPiel",otraEnfermedadPiel );
                params.put("abastecimientoAgua",abastecimientoAgua );
                params.put("nombreRio",nombreRio );
                params.put("otroAbastecimientoAgua",otroAbastecimientoAgua);
                params.put("sisternaTanque",sisternaTanque );
                params.put("desdeDondeLlegaAgua",desdeDondeLlegaAgua);
                params.put("origenAguaBeber",origenAguaBeber );
                params.put("tratamientoOrigenAgua",tratamientoOrigenAgua);
                params.put("usoAgua",usoAgua );
                params.put("capacidadTanque",capacidadTanque);
                params.put("capacidadSisterna",capacidadSisterna);
                params.put("frecuenciaLimpieza",frecuenciaLimpieza);
                params.put("frecuenciaCloracion",frecuenciaCloracion);
                params.put("otroFrecuenciaCloracion",otroFrecuenciaCloracion);
                params.put("dosisCloracion",dosisCloracion);
                params.put("otroDosisCloracion",otroDosisCloracion);
                params.put("mascotas_animal",mascotas_animal);
                params.put("consumo_animal",consumo_animal );
                params.put("venta_animal",venta_animal );
                params.put("ornamentales_riego",ornamentales_riego);
                params.put("consumo_riego",consumo_riego );
                params.put("venta_riego",venta_riego );
                params.put("cantHombres",cantHombres );
                params.put("cantMujeres",cantMujeres );
                params.put("cantNinos",cantNinos );
                params.put("cantDiscapacidad",cantDiscapacidad );
                params.put("nombre_persona",nombre_persona );
                params.put("dir_persona",dir_persona );
                params.put("com_persona",com_persona);
                params.put("barr_persona",barr_persona);
                params.put("jun_ag_persona",jun_ag_persona);
                params.put("edad_persona",edad_persona );
                params.put("sexo_persona",sexo_persona );
                params.put("longitud_persona",longitud_persona );
                params.put("latitud_persona",latitud_persona );

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }





}
