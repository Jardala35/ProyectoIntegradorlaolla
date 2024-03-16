package com.example.proyectointegradorlaolla;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

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
    private SearchView searchview;
    private TabLayout tl;
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
    ActivityResultLauncher<Intent> stfrores2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){

            }
        }
    });

    public void abrirReceta(View view){
        int pos = miRecyclerView.getChildAdapterPosition(view);
        ItemLista selectedItem = miAdapter.getItemFromPosition(pos);
        String nodo = selectedItem.getId();
        Intent intent = new Intent(this, Receta.class);
        intent.putExtra("position", nodo);
        stfrores.launch(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchview = findViewById(R.id.searchView);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                miAdapter.getFilter().filter(newText);
                return true;
            }
        });
        this.datos = new ArrayList<>();
        cargarDatos(datos);
        miRecyclerView = findViewById(R.id.recview);
        miRecyclerView.setHasFixedSize(true);
        miLayoutManager = new LinearLayoutManager(this);
        miRecyclerView.setLayoutManager(miLayoutManager);

        tl = findViewById(R.id.tablayout);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        break;
                    case 1:

                        Intent intent = new Intent(getApplicationContext(), Anadir.class);
                        stfrores2.launch(intent);
                        tl.getTabAt(0).select();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });}
    public void cargarDatos(ArrayList datos){
        DatabaseReference dr = db.getInstance().getReference().child("recetas");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemLista> aux = new ArrayList<>();
               for(DataSnapshot snoop: snapshot.getChildren()){
                   String uno = snoop.child("nombre").getValue(String.class);
                   String dos = snoop.child("imagen").getValue(String.class);

                   String id = snoop.child("id").getValue(String.class);
                   id = String.valueOf(Integer.parseInt(id) - 1);
                   datos.add(new ItemLista(id, dos, uno));
                   aux.add(new ItemLista(id, dos, uno));
               }
                if (miAdapter == null) {
                    miAdapter = new ElAdaptador(datos);
                    miAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            abrirReceta(v);

                        }
                    });
                    miRecyclerView.setAdapter(miAdapter);
                } else {
                    // Si el adaptador ya está establecido, actualiza los datos

                    miAdapter.actualizarDatos(aux);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
