package com.example.appvinculacion;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class Acercade extends AppCompatActivity {
    private Button buttonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);

        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);

        // Configura un OnClickListener para el botón de "Regresar"
        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}