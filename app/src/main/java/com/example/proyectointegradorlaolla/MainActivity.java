package com.example.proyectointegradorlaolla;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
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
    private TabLayout tabLayout;

    ActivityResultLauncher<Intent> stfrores = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // Manejar el resultado de la actividad Receta.class o Anadir.class
            }
        }
    });

    public void abrirReceta(View view) {
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
                abrirReceta(v);
            }
        });
        miRecyclerView.setAdapter(miAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Abre la actividad MainActivity
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    stfrores.launch(intent);
                } else
                if (position == 1) {
                    // Abre la actividad Anadir
                    Intent intent = new Intent(MainActivity.this, Anadir.class);
                    stfrores.launch(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No es necesario hacer nada aquí
            }
        });
    }

    private void cargarDatos(ArrayList datos) {
        DatabaseReference dr = db.getInstance().getReference().child("recetas");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snoop : snapshot.getChildren()) {
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