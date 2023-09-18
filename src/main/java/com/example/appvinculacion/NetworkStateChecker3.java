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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker3 extends BroadcastReceiver {

    private Context context;
    private MetodosBDD db;
    private MainActivity3 main;
    private String URL_SAVE_NAME = "http://192.188.58.82:3000/persona_enc";

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
                Cursor cursor = db.obtenerPersonasEncuestadasDesincronizadasBDD();

                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                String ssid = null;
                if (connectionInfo != null) {
                    ssid = connectionInfo.getSSID();
                }
                if(ssid.contains("ESPE") || ssid.contains("INVITADOS") || ssid.contains("eduroam")  ){
                    URL_SAVE_NAME = "http://10.3.0.251:3000/persona_enc";
                }

                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(db.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
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
                    } while (cursor.moveToNext());
                }

            }
        }

    }
    private void saveName(
            final int id_persona,
            final String codigo_persona,
            final String nombre_persona,
            final String dir_persona,
            final String com_persona,
            final String barr_persona,
            final String jun_ag_persona,
            final String edad_persona,
            final String sexo_persona,
            final String longitud_persona,
            final String latitud_persona) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String obj1 = new JSONObject(response).getString("status");
                            Log.d("success","respuesta per_enc");
                            if ("persona_enc_c".equals(obj1)) {
                                Log.d("success","envia per_enc");
                                // actualizando el estado en sqlite
                                db.actualizarPersonaEncuestadaBDD(id_persona,MainActivity3.NAME_SYNCED_WITH_SERVER);

                                // enviando la transmisión para actualizar la lista
                                context.sendBroadcast(new Intent(MainActivity3.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("codigo_persona",codigo_persona );
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
