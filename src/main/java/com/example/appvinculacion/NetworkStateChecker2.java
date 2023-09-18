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
import java.util.Map;

public class NetworkStateChecker2 extends BroadcastReceiver {

    private Context context;
    private MetodosBDD db;
    private MainActivity2 main;
    private String URL_SAVE_NAME = "http://192.188.58.82:3000/encuesta2";
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

                //getting all the unsynced names
                Cursor cursor = db.obtenerDesincronizadoEnc2BDD();

                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                String ssid = null;
                if (connectionInfo != null) {
                    ssid = connectionInfo.getSSID();
                }
                if(ssid.contains("ESPE") || ssid.contains("INVITADOS") || ssid.contains("eduroam")  ){
                    URL_SAVE_NAME = "http://10.3.0.251:3000/encuesta2";
                }

                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        guardarEnc2Servidor(
                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGOENCUESTA)),
                                cursor.getString(cursor.getColumnIndex(db.c_FECHA)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAINICIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAFIN)),
                                cursor.getString(cursor.getColumnIndex(db.c_HORAMUESTRA)),
                                cursor.getString(cursor.getColumnIndex(db.c_LUGAR)),
                                cursor.getString(cursor.getColumnIndex(db.c_ASPECTO)),
                                cursor.getString(cursor.getColumnIndex(db.c_OBSERVACIONES)),
                                cursor.getString(cursor.getColumnIndex(db.c_NOMBREPERSONA)),
                                cursor.getString(cursor.getColumnIndex(db.c_LONGITUD)),
                                cursor.getString(cursor.getColumnIndex(db.c_LATITUD)),
                                cursor.getString(cursor.getColumnIndex(db.c_DIRECCIONPERSONA)),
                                cursor.getString(cursor.getColumnIndex(db.c_COMUNIDAD)),
                                cursor.getString(cursor.getColumnIndex(db.c_BARRIO)),
                                cursor.getString(cursor.getColumnIndex(db.c_JUNTA)),
                                cursor.getString(cursor.getColumnIndex(db.c_CLOROLIBRERESIDUAL)),
                                cursor.getString(cursor.getColumnIndex(db.c_PH)),
                                cursor.getString(cursor.getColumnIndex(db.c_MONOCLOROAMINAS)),
                                cursor.getString(cursor.getColumnIndex(db.c_CONDUCTIVIDAD))

                        );
                        Log.d("success", cursor.getString(cursor.getColumnIndex(db.COLUMN_ID)));
                    } while (cursor.moveToNext());
                }
            }
        }

    }

    private void guardarEnc2Servidor(final int id,
                                     final String codigo,
                                     final String codigoEncuesta,
                                     final String fecha,
                                     final String horaInicio,
                                     final String horaFin,
                                     final String horaMuestra,
                                     final String lugar,
                                     final String aspecto,
                                     final String observaciones,
                                     final String nombre_persona,
                                     final String Longitud,
                                     final String Latitud,
                                     final String direccion_persona,
                                     final String comunidad,
                                     final String barrio,
                                     final String junta,
                                     final String cloroLibreResidual,
                                     final String pH,
                                     final String monocloroaminas,
                                     final String conductividad) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String obj2 = new JSONObject(response).getString("status");
                            Log.d("success","respuesta enc2");

                            if ("encuesta2_c".equals(obj2)) {
                                // actualizando el estado en sqlite
                                Log.d("success","envia enc2");
                                db.actualizarEstadoEnc2BDD(id,MainActivity2.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity2.DATA_SAVED_BROADCAST));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_encuestador",codigo);
                params.put("codigo_encuesta",codigoEncuesta);
                params.put("lugar",lugar);
                params.put("longitud",Longitud);
                params.put("latitud",Latitud);
                params.put("fecha",fecha);
                params.put("horaInicio",horaInicio);
                params.put("horaFin",horaFin);
                params.put("horaMuestra",horaMuestra);
                params.put("nombre_persona",nombre_persona);
                params.put("direccion_persona",direccion_persona);
                params.put("comunidad",comunidad);
                params.put("barrio",barrio);
                params.put("junta",junta);
                params.put("aspecto",aspecto);
                params.put("observaciones",observaciones);
                params.put("cloroLibreResidual",cloroLibreResidual);
                params.put("pH",pH);
                params.put("monocloroaminas",monocloroaminas);
                params.put("conductividad",conductividad);


                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
