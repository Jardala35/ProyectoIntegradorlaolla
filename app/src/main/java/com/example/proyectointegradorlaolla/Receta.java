package com.example.proyectointegradorlaolla;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Receta extends AppCompatActivity {

    private TextView nombre;
    private ImageView img;
    private TextView veg;
    private TextView cel;
    private TextView descr;
    private TextView tiempo;
    private TextView ingr;
    private TextView pasos;
    private FirebaseDatabase db;
    private FirebaseStorage fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        nombre = findViewById(R.id.nombrerec);
        img = findViewById(R.id.imageView3);
        veg = findViewById(R.id.vegetariano);
        cel = findViewById(R.id.celiaco);
        descr = findViewById(R.id.descripcion);
        tiempo = findViewById(R.id.tiempo);
        ingr = findViewById(R.id.ingredientes);
        pasos = findViewById(R.id.pasos);
        Intent intent = getIntent();
        String aux = intent.getStringExtra("position");
        obtenerDatos(aux);
    }

    public void obtenerImagen(String nombre){
       StorageReference sr = fs.getInstance().getReference().child(nombre + ".jpeg");
       sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Receta.this)
                        .load(uri)
                        .into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al obtener la URL de descarga de la imagen
                Log.e("Firebase", "Error al obtener la URL de descarga de la imagen: " + e.getMessage());
            }
        });


    }

    public void obtenerDatos(String pos){
            DatabaseReference dr = db.getInstance().getReference().child("recetas").child(pos);
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        nombre.setText(snapshot.child("nombre").getValue(String.class).toString());
                        if (snapshot.child("vegetariano").getValue(Boolean.class).equals(true)){
                            veg.setTextColor(Color.rgb(43,135,0));
                            veg.setText("Es apta para vegetarianos");
                        }else{
                            veg.setTextColor(Color.rgb(210,25,4));
                            veg.setText("No es apta para vegetarianos");
                        }
                        if (snapshot.child("celiaco").getValue(Boolean.class).equals(true)){
                            cel.setTextColor(Color.rgb(43,135,0));
                            cel.setText("Es apta para celiacos");
                        }else{
                            cel.setTextColor(Color.rgb(210,25,4));
                            cel.setText("No es apta para celiacos");
                        }
                        descr.setText(snapshot.child("descripcion").getValue(String.class).toString());
                        tiempo.setText(snapshot.child("tiempo").getValue(Long.class).toString()+" minutos");
                        ingr.setText(snapshot.child("ingredientes").getValue(String.class).toString());
                        pasos.setText(snapshot.child("pasos").getValue(String.class).toString());
                        obtenerImagen(snapshot.child("imagen").getValue(String.class).toString());
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
}