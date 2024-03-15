package com.example.proyectointegradorlaolla;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView miRecyclerView;
    private LinearLayoutManager miLayoutManager;
    private ElAdaptador miAdapter;
    private ArrayList<ItemLista> datos;
    private FirebaseDatabase db;
    private FirebaseStorage fs;


    ActivityResultLauncher<Intent> stfrores = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){

            }
        }
    });

    public void abrirReceta(View view){
        Intent intent = new Intent(this, Receta.class);
        intent.putExtra("position", miRecyclerView.getChildAdapterPosition(view));
        stfrores.launch(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.datos = new ArrayList<>();
        cargarDatos(datos);
        miRecyclerView = findViewById(R.id.recview);
        miRecyclerView.setHasFixedSize(true);
        miLayoutManager = new LinearLayoutManager(this);
        miRecyclerView.setLayoutManager(miLayoutManager);
        miAdapter = new ElAdaptador(datos);
        miAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementar el pasar a otra ventana con los datos de la receta
                abrirReceta(v);
                String msg = "Seleccionada la opción " + miRecyclerView.getChildAdapterPosition(v);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        miRecyclerView.setAdapter(miAdapter);
    }
    private void cargarDatos(ArrayList datos){
        DatabaseReference dr = db.getInstance().getReference().child("recetas");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot snoop: snapshot.getChildren()){
                   String uno = snoop.child("nombre").getValue(String.class);
                   String dos = snoop.child("imagen").getValue(String.class);
                   datos.add(new ItemLista(dos, uno));
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}