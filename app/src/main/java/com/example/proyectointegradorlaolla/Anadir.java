package com.example.proyectointegradorlaolla;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Anadir extends AppCompatActivity {

    private EditText nombre;
    private EditText tiempo;
    private EditText descripcion;
    private EditText ingredientes;
    private EditText pasos;
    private Switch vegetariano;
    private Switch celiaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir);
        nombre = findViewById(R.id.editTextnombre);
        tiempo = findViewById(R.id.editTexttiempo);
        descripcion = findViewById(R.id.editTextdescripcion);
        ingredientes = findViewById(R.id.editTextingredientes);
        pasos = findViewById(R.id.editTextpreparacion);
        vegetariano = findViewById(R.id.swveg);
        celiaco = findViewById(R.id.swcel);

    }

    public void guardarReceta(View view){



        nombre.setText("");
        tiempo.setText("");
        descripcion.setText("");
        ingredientes.setText("");
        pasos.setText("");
        vegetariano.setChecked(false);
        celiaco.setChecked(false);
        finish();
    }
}
