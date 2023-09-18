package com.example.appvinculacion;

import android.view.View;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MetodosBDD db;

    //View objects
    private Button buttonSave;
    private Button buttonRegresar;

    private ListView listViewNames;

    private List<Name> names;
    private BroadcastReceiver broadcastReceiver;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    private NameAdapter nameAdapter;

    public static final String URL_SAVE_NAME = "http://192.188.58.82:3000/encuestador";

    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        db = new MetodosBDD(this);
        names = new ArrayList<>();

        listViewNames = (ListView) findViewById(R.id.listViewNames);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);


        // Configura un OnClickListener para el botón de "Regresar"
        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Configura un OnClickListener para el botón de "Actualizar"
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });


        loadNames();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                loadNames();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

    }

    // Función para actualizar la interfaz de usuario
    private void updateUI() {
        Log.d("MainActivity", "Botón de Actualizar presionado");
        refreshList();
    }

    private void loadNames() {
        names.clear();
        Cursor cursor = db.obtenerEncuestadoresBDD();
        if (cursor.moveToFirst()) {
            do {
                Name name = new Name(
                        cursor.getString(cursor.getColumnIndex(db.c_CODIGO)),
                        cursor.getString(cursor.getColumnIndex(db.c_NOMBRE)),
                        cursor.getInt(cursor.getColumnIndex(db.c_ESTADO))
                );
                names.add(name);
            } while (cursor.moveToNext());
        }

        nameAdapter = new NameAdapter(this, R.layout.names, names);
        listViewNames.setAdapter(nameAdapter);
        refreshList();
    }



    private void refreshList() {
        nameAdapter.notifyDataSetChanged();
    }

}